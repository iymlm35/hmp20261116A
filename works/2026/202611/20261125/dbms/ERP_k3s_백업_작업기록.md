# ERP DB(sql-h) k3s 환경 백업 작업 기록

- 작업일: 2026-11-25
- 대상: `192.168.1.170,1434` 의 ERP 데이터베이스 (k3s 파드 `infra/sql-h`)
- 성격: **리허설 백업**(서비스 중, COPY_ONLY). 최종 이전용 백업이 아님.
- 이전 문서 `ERP_bak_백업_SSH_가이드.md` 는 Docker 가정이었으며, 실제 환경(k3s)이 확인되어 이 문서가 기준이다.

## 1. 환경 확정 결과

| 항목 | 값 | 확인 근거 |
|---|---|---|
| SQL Server | 2022 (16.0.4295 RTM), Developer Edition 64-bit | SSMS 연결 속성 |
| OS | Linux / Ubuntu | `sys.dm_os_host_info` |
| 서버 기본 정렬 | SQL_Latin1_General_CP1_CI_AS | SSMS 연결 속성 |
| 실행 형태 | **k3s 파드** (Docker 아님) | 서버IP `10.42.3.62` (k3s 파드 대역), `/var/run/docker.sock` 없음 |
| 네임스페이스 / 파드 | `infra` / `sql-h-d95957cc5-mj2dk` | `kubectl get pods -A -o wide` |
| 서비스 | `infra/sql-h` ClusterIP `10.43.186.145:1433` | `kubectl get svc -A` |
| 노드 | `hmplus-dev01` (SSH 접속 서버와 동일) | |
| 외부 접속 | `192.168.1.170:1434` (포트 1434는 호스트 `ss` 에 리스너가 없음 → k3s 포워딩) | |
| 호스트 SSH 계정 | `hmplus` (`usr21` 아님) | |
| 데이터 저장 | 호스트 `hostPath: /data/db/sql-h` → 파드 `/var/opt/mssql` (PVC 없음) | `kubectl get pod -o jsonpath` |
| 데이터 디스크 | `/dev/nvme0n1p1` (호스트 `/`) 116G 중 여유 **28G** (75% 사용) | `df -h` |
| 백업 보관 디스크 | `/media/hmplus/data` 458G 중 여유 133G | `df -h` |
| 논리 파일명 | 데이터 `ERP`, 로그 `ERP_log` | 백업 출력 |
| ERP.mdf / ERP_log.ldf | 6,719,275,008 B (≈6.26GiB) / 2,290,089,984 B (≈2.13GiB) | `ls -l` |
| Enterprise 전용 기능 | **없음** (`sys.dm_db_persisted_sku_features` 결과 없음) | SSMS 쿼리 |
| 파드 환경변수 `MSSQL_SA_PASSWORD` | 설정됨 (값은 기록하지 않음) | `test -n` 확인 |

- `sql-dev` 파드/서비스(`apps`, `infra`, `platform` 네임스페이스)는 **다른 SQL Server**이며 ERP 백업 대상이 아니다. 파드 IP `10.42.3.62` 일치로 `sql-h` 임을 확인했다.
- SSMS 서버명 `sql-h` 는 `@@SERVERNAME`(파드 호스트명)이며, 실제 접속 주소는 `192.168.1.170,1434` 이다.
- 앱 설정: `products-erp-h/app/src/main/resources/datasource.yml` 에서 `PLATFORM_DB_URL` 환경변수로 override 가능, 기본값은 `sql-h.infra.svc.cluster.local:1433/ERP`, 업무 DB 주소는 `hmp.datasource` 테이블(`ERP:MAIN`)에 저장, 비밀번호는 `ENC(...)`.

## 2. 진단 과정에서 정정된 판단

1. `ss -ltnp | grep 1434` 결과가 없어 "다른 서버"로 추정했으나, 실제로는 포트가 열려 있었다(Test-NetConnection 22/1433/1434 모두 True). 원인은 k3s 가 iptables 로 포워딩하기 때문이다.
2. `docker` 명령 불가(`no such file or directory`)는 권한 문제가 아니라 Docker 가 아닌 containerd(k3s)를 쓰기 때문이다. → `kubectl` 사용.
3. 이 서버는 SSH 가 비밀번호 인증이라 자동화 도구에서 접속할 수 없다. 서버 명령은 사용자가 SSH 세션에서 직접 실행했다.

## 3. 리허설 백업 수행 내역 (SSH, hmplus@hmplus-dev01)

변수:
```bash
NS=infra
POD=$(sudo kubectl get pod -n $NS -o name | grep sql-h | head -1)   # pod/sql-h-d95957cc5-mj2dk
```

### 3-1. 사전 확인 (읽기 전용) — 모두 통과
```bash
sudo kubectl exec -n $NS $POD -- ls /opt/mssql-tools18/bin/sqlcmd
sudo kubectl exec -n $NS $POD -- bash -c 'test -n "$MSSQL_SA_PASSWORD" && echo 있음 || echo 없음'
sudo ls -l /data/db/sql-h/data | head -3
```

### 3-2. 백업
```bash
STAMP=$(date +%Y%m%d_%H%M)
SQL="BACKUP DATABASE [ERP] TO DISK = N'/var/opt/mssql/data/ERP_${STAMP}.bak' WITH COPY_ONLY, INIT, COMPRESSION, CHECKSUM, STATS = 10"
time sudo kubectl exec -n $NS $POD -- bash -c '/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -b -Q "$1"' _ "$SQL"
```
- 쿼리를 `$1` 인자로 넘기면 따옴표 중첩 문제가 없다.
- 비밀번호는 파드 환경변수를 사용(명령 기록에 남지 않음).

결과:

| 항목 | 값 |
|---|---|
| 처리 페이지 | 815,112(ERP) + 7(ERP_log) |
| 소요 시간 | **19.85초** (real 20.3초), 320.8 MB/s |
| 결과 | `BACKUP DATABASE successfully processed` |
| 파일 | `ERP_20261125_1536.bak`, **1.1GB** (압축률 약 85%) |

### 3-3. 검증
```bash
SQL="RESTORE VERIFYONLY FROM DISK = N'/var/opt/mssql/data/ERP_${STAMP}.bak' WITH CHECKSUM"
sudo kubectl exec -n $NS $POD -- bash -c '/opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -b -Q "$1"' _ "$SQL"
```
결과: `The backup set on file 1 is valid.`

### 3-4. 호스트 데이터 디스크로 복사 + 해시 비교
`/var/opt/mssql` 이 호스트 `/data/db/sql-h` 이므로 `kubectl cp` 없이 호스트에서 직접 복사한다.
```bash
sudo cp /data/db/sql-h/data/ERP_${STAMP}.bak /media/hmplus/data/db-backup/
sudo chown hmplus:hmplus /media/hmplus/data/db-backup/ERP_${STAMP}.bak
sudo sha256sum /data/db/sql-h/data/ERP_${STAMP}.bak
sha256sum /media/hmplus/data/db-backup/ERP_${STAMP}.bak
# 두 해시가 같을 때만:
sudo rm /data/db/sql-h/data/ERP_${STAMP}.bak
```

결과 (일치):
```
6ec33252098d0a2c7eb51bf40819a5ef4e2e8a99ff00c447c9c5c635fe46525b  ERP_20261125_1536.bak
```
- 서버 복사본: `/media/hmplus/data/db-backup/ERP_20261125_1536.bak`

## 4. 내 PC 로 가져오기 (PowerShell, 로컬)

SSH 세션(원격 Linux 셸)이 아니라 **내 PC 의 PowerShell** 에서 실행해야 한다.
```powershell
New-Item -ItemType Directory -Force D:\root\zz-works | Out-Null
scp hmplus@192.168.1.170:/media/hmplus/data/db-backup/ERP_20261125_1536.bak D:\root\zz-works\
Get-FileHash D:\root\zz-works\ERP_20261125_1536.bak -Algorithm SHA256
```
결과: 크기 1,111,859,200 B, SHA-256 **일치** (`6ec33252…46525b`).
- 현재 위치: `D:\root\zz-works\ERP_20261125_1536.bak`
- 운영 데이터가 들어 있으므로 git 저장소 안이면 `*.bak` 을 `.gitignore` 에 추가한다.

## 5. 리허설에서 확인된 사항

- 백업 자체는 **20초**로 짧다. 이전 작업의 소요 시간은 전송과 복원이 좌우한다.
- 노드 디스크(`/` 여유 28G) 압박 위험은 없다. 최악(비압축)도 약 6.3GiB. k3s 는 노드 디스크 여유가 약 10~15% 미만이면 파드를 퇴거시킬 수 있으므로 큰 파일을 `/` 에 오래 두지 않는다.
- 파드는 hostPath 를 쓰므로 파드가 재생성되어도 데이터는 호스트 폴더에 남는다.
- 어제 만든 `ERP_before_cm_user_role_cleanup_20261124.bak`(0.94GiB)이 같은 폴더(`/data/db/sql-h/data`)에 남아 있다. 삭제하지 않았다.

## 6. 계정(로그인) 관련 정리

- 백업(.bak)에는 **DB 내부 사용자·역할·권한은 포함**되지만 **서버 수준 로그인은 포함되지 않는다**. 새 서버에 같은 **SID** 로 로그인을 먼저 만들어야 사용자 매핑이 유지된다.
- "DBA" 라는 이름의 기본 계정은 없다. 백업 권한은 서버 `sysadmin`(`sa`) 또는 DB `db_backupoperator`/`db_owner` 역할이다.
- 이전 후 정기 백업용으로 전용 계정을 둘 수 있다(최소 권한).
```sql
USE [master];
CREATE LOGIN [bk_operator] WITH PASSWORD = N'<강력한비밀번호>', CHECK_POLICY = ON;
USE [ERP];
CREATE USER [bk_operator] FOR LOGIN [bk_operator];
ALTER ROLE db_backupoperator ADD MEMBER [bk_operator];
```
  SQL Server 2022 에서 `RESTORE VERIFYONLY` 에는 `CREATE ANY DATABASE` 권한이 추가로 필요할 수 있으므로 실제로 실행해 확인한다.
- 로그인 스크립트에는 비밀번호 해시가 있으므로 외부 공유 금지, 이전 후 삭제한다.

## 7. 대상 서버(SSMS) 복원 절차 — 대상 서버 미확정

규칙: SSMS 복원 창의 파일 목록은 **대상 서버의 디스크**이다. `.bak` 을 대상 서버가 읽을 수 있는 위치에 먼저 둔다(Linux 계열은 `mssql` 계정(UID 10001)이 읽을 수 있어야 함).

1. `.bak` 전송 후 해시 재확인 (`6ec33252…46525b`).
2. 대상 서버 사전 확인: 버전 2022(16.x) 이상, 에디션, 서버 정렬, 같은 이름 DB 존재 여부, **디스크 여유 10GB 이상**(복원 시 mdf 6.3GiB + ldf 2.1GiB).
3. 대상에 로그인 먼저 생성(같은 SID).
4. SSMS 에서 복원. 리허설은 `ERP_TEST` 로:
```sql
RESTORE FILELISTONLY FROM DISK = N'<대상 서버 기준 경로>\ERP_20261125_1536.bak';

RESTORE DATABASE [ERP_TEST]
FROM DISK = N'<대상 서버 기준 경로>\ERP_20261125_1536.bak'
WITH MOVE N'ERP'     TO N'<데이터 폴더>\ERP_TEST.mdf',
     MOVE N'ERP_log' TO N'<로그 폴더>\ERP_TEST_log.ldf',
     CHECKSUM, STATS = 10;
```
5. 복원 후 보정/검증:
```sql
USE [ERP_TEST];
EXEC sp_change_users_login 'Report';                        -- 고아 사용자
ALTER AUTHORIZATION ON DATABASE::[ERP_TEST] TO [sa];        -- 소유자 원복
SELECT name, compatibility_level, recovery_model_desc,
       is_trustworthy_on, is_read_committed_snapshot_on
FROM sys.databases WHERE name = 'ERP_TEST';                 -- 원본 값과 비교
DBCC CHECKDB ([ERP_TEST]) WITH NO_INFOMSGS;
```
   TRUSTWORTHY 는 복원 시 OFF 로 초기화되므로 원본이 ON 이었다면 다시 설정한다.

## 8. 전달받은 "작업 순서" 가이드 검토 결과 (붙여넣은 12단계)

큰 흐름은 타당하며 확인된 환경과 일치한다. 보강할 점:

1. **노드 디스크 압박**: 이번 크기(1.1GB)에서는 문제없음. 단, `/` 에 큰 파일을 오래 두지 않는다.
2. **사전 조사 쿼리**(원본에서 실행):
```sql
USE [ERP];
SELECT name, is_disabled FROM msdb.dbo.sysjobs;                                  -- Agent 작업
SELECT name, product, provider, data_source FROM sys.servers WHERE server_id > 0; -- 링크드 서버
SELECT name FROM sys.assemblies WHERE is_user_defined = 1;                       -- CLR
SELECT DB_NAME(database_id) AS db, encryption_state FROM sys.dm_database_encryption_keys; -- TDE
SELECT DISTINCT referenced_database_name FROM sys.sql_expression_dependencies
WHERE referenced_database_name IS NOT NULL;                                       -- 다른 DB 참조(예: HS_MSG)
SELECT name, is_trustworthy_on, is_read_committed_snapshot_on, compatibility_level, recovery_model_desc
FROM sys.databases WHERE name = 'ERP';                                            -- 보정 비교용
```
   SQL Server on Linux 는 Agent 가 꺼져 있는 경우가 많다.
3. **복원 후 보정 추가**: DB 소유자 원복, TRUSTWORTHY 재설정, DB 마스터 키·인증서·대칭 키 점검(`sys.symmetric_keys`, `sys.certificates`).
4. **로그인**: `sp_help_revlogin` 이 정책(만료/비활성)까지 만들어 준다. `sys.sql_logins` 단순 쿼리는 이 부분이 빠진다. 서버 역할 멤버십·서버 권한은 둘 다 만들어 주지 않으므로 별도 이관한다.
5. **복원 경로**: 문서의 `D:\bak\...` 는 대상이 Windows 일 때만 해당. 파일이 2개를 초과하면 모든 논리 파일에 `MOVE` 지정.
6. **리허설 부하**: 압축 백업은 CPU 를 쓰므로 파드 CPU/메모리 limit 확인(`kubectl describe pod`).
7. **앱 연결 변경**: 환경변수 `PLATFORM_DB_URL` 로 가능(코드 수정 불필요). `hmp.datasource` 테이블의 `ERP:MAIN` 행도 변경 대상. 스키마/테이블 이름 대소문자(`hmp` vs `HMP`)는 쿼리로 확인.
8. **암호화 키**: 새 앱에서도 `ENC(...)` 가 풀리도록 같은 암호화 키를 사용한다.

## 9. 남은 작업 / 미결정

| 항목 | 상태 |
|---|---|
| 복원 대상 서버 (OS, 버전 2022 이상, 에디션) | **미확정** — SSMS 로 작업할 서버로 `.bak` 이동 예정 |
| 원본 사전 조사 쿼리 실행 (위 8-2) | 미실행 |
| 로그인 이관 스크립트 추출 (`sp_help_revlogin`) | 미실행 |
| 기준값 저장 (테이블별 행 수, 객체 수, 사용자·권한, 시퀀스) | 미실행 |
| 서버 쪽 복사본 정리 (`/media/hmplus/data/db-backup/ERP_20261125_1536.bak`) | 이전 완료 후 판단 |
| 어제 백업 `ERP_before_cm_user_role_cleanup_20261124.bak` 정리 | 사용자 판단 |
| 최종 이전용 백업 (앱·배치 중단 후 재실행) | 이전 시점에 수행 |
| 이전 후 정기 백업 설정, 전용 계정 | 이전 후 |

## 10. 보안 주의

- `.bak` 과 로그인 스크립트에는 운영 데이터/비밀번호 해시가 있다. 작업 폴더(git 저장소)에 두지 않는다.
- SSH 비밀번호, `sa` 비밀번호, `ENC(...)` 키는 이 문서에 기록하지 않았다.
