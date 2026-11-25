# ERP DB .bak 백업 가이드 (Docker MS-SQL, SSH 진행)

> **주의: 이 문서는 Docker 가정으로 작성되었다. 실제 환경은 k3s 파드(`infra/sql-h`)로 확인되었으므로 `docker` 명령은 쓸 수 없다. 실제 수행 절차와 결과는 `ERP_k3s_백업_작업기록.md` 를 따른다.**

- 작성일: 2026-11-25
- 대상: 192.168.1.170:1434 의 ERP 데이터베이스 (Docker 컨테이너, SQL Server on Linux)
- 호스트: hmplus@hmplus-dev01 (192.168.1.170)
- 저장 경로(호스트): `/media/hmplus/data/db-backup`

## 사전 정보

- BACPAC은 서버 수준 로그인을 복제하지 않는다. 계정까지 같은 환경으로 옮기려면 `.bak` 백업/복원 + 로그인 별도 이관(같은 SID)이 적합하다.
- 컨테이너 안에서 보이는 경로는 컨테이너의 Linux 파일시스템이다. 호스트의 `/media/hmplus/data`는 `-v`로 마운트하지 않았다면 컨테이너 안에서 보이지 않는다.
- 호스트 디스크(확인 시점): `/` 116G 중 여유 28G / `/media/hmplus/data` 458G 중 여유 133G
- 마운트가 없으면 백업은 컨테이너 안(= 호스트 `/` 의 Docker 저장소)에 일시적으로 생기므로, DB 크기를 먼저 확인한다.

## 0단계: 변수 설정과 사전 확인

```bash
CN=컨테이너명          # docker ps 결과의 NAMES 값
DB=ERP
BK=/media/hmplus/data/db-backup
STAMP=$(date +%Y%m%d_%H%M)

docker ps --format 'table {{.Names}}\t{{.Image}}\t{{.Ports}}'
```

`docker` 권한 오류가 나면 아래 명령 앞에 `sudo`를 붙인다.

```bash
# sqlcmd 위치 확인 (존재하는 쪽 사용)
docker exec $CN ls /opt/mssql-tools18/bin/sqlcmd /opt/mssql-tools/bin/sqlcmd 2>&1

# 마운트 확인 (비어 있으면 마운트 없음)
docker inspect $CN --format '{{range .Mounts}}{{.Source}} -> {{.Destination}}{{"\n"}}{{end}}'

# 컨테이너 안 여유 공간
docker exec $CN df -h /var/opt/mssql
```

- 이후 명령은 `/opt/mssql-tools18/bin/sqlcmd` 기준. `tools`(18 없음)만 있으면 경로를 바꾸고 `-C` 옵션을 뺀다.
- 마운트 출력에 `/media/hmplus/data/...` 가 이미 있으면 3단계 경로를 그 마운트의 컨테이너 쪽 경로로 바꾸고 5~6단계(복사)는 생략한다.

## 1단계: 호스트에 저장 폴더 준비

```bash
mkdir -p $BK
df -h /media/hmplus/data     # 여유 공간 재확인
```

## 2단계: sa 비밀번호를 환경변수로 입력 (명령 기록에 남지 않게)

```bash
read -rs -p "sa password: " SA_PW; echo
```

## 3단계: 컨테이너 안에서 백업 실행

```bash
docker exec -e SQLCMDPASSWORD="$SA_PW" $CN \
  /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -C -b \
  -Q "BACKUP DATABASE [$DB] TO DISK = N'/var/opt/mssql/data/${DB}_${STAMP}.bak' WITH COPY_ONLY, INIT, COMPRESSION, CHECKSUM, STATS = 10"
```

- 성공 시 `BACKUP DATABASE successfully processed ...` 출력.
- 컨테이너 안 `/` 여유가 부족해 보이면 먼저 DB 크기를 확인한다.

## 4단계: 백업 무결성 검증 (컨테이너 안)

```bash
docker exec -e SQLCMDPASSWORD="$SA_PW" $CN \
  /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -C -b \
  -Q "RESTORE VERIFYONLY FROM DISK = N'/var/opt/mssql/data/${DB}_${STAMP}.bak' WITH CHECKSUM"
```

`The backup set on file 1 is valid.` 가 나와야 한다.

## 5단계: 호스트 폴더로 복사

```bash
docker cp $CN:/var/opt/mssql/data/${DB}_${STAMP}.bak $BK/
ls -lh $BK/
```

## 6단계: 복사본 일치 확인 후 컨테이너 쪽 삭제

```bash
docker exec $CN sha256sum /var/opt/mssql/data/${DB}_${STAMP}.bak
sha256sum $BK/${DB}_${STAMP}.bak
```

두 해시가 같으면 컨테이너 안 파일을 삭제한다. **해시가 다르면 삭제하지 말고 5단계를 다시 한다.**

```bash
docker exec $CN rm /var/opt/mssql/data/${DB}_${STAMP}.bak
```

## 7단계: 로그인 이관 스크립트도 같이 저장

```bash
docker exec -e SQLCMDPASSWORD="$SA_PW" $CN \
  /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -C -h -1 -W -y 0 \
  -Q "SET NOCOUNT ON; SELECT 'CREATE LOGIN ' + QUOTENAME(name) + ' WITH PASSWORD = ' + CONVERT(varchar(max), password_hash, 1) + ' HASHED, SID = ' + CONVERT(varchar(max), sid, 1) + ', DEFAULT_DATABASE = ' + QUOTENAME(default_database_name) + ';' FROM sys.sql_logins WHERE name NOT LIKE '##%' AND name <> 'sa';" \
  > $BK/logins_${STAMP}.sql

chmod 600 $BK/logins_${STAMP}.sql
cat $BK/logins_${STAMP}.sql
```

- 비밀번호 해시가 들어 있으므로 외부 공유 금지, 이관 후 삭제.

## 8단계: 정리와 내려받기

```bash
unset SA_PW
ls -lh $BK/
```

내 PC(Windows PowerShell)로 가져오기:

```powershell
scp hmplus@192.168.1.170:/media/hmplus/data/db-backup/ERP_*.bak D:\backup\
scp hmplus@192.168.1.170:/media/hmplus/data/db-backup/logins_*.sql D:\backup\
```

## 대상 서버 복원 요약

1. `logins_*.sql` 의 `CREATE LOGIN` 을 먼저 실행한다 (같은 SID 유지).
2. 논리 파일명 확인: `RESTORE FILELISTONLY FROM DISK = N'경로\ERP_....bak';`
3. 복원 (Linux → Windows 이면 `WITH MOVE` 필요):

```sql
RESTORE DATABASE [ERP]
FROM DISK = N'D:\backup\ERP_....bak'
WITH MOVE N'논리데이터명' TO N'D:\SQLData\ERP.mdf',
     MOVE N'논리로그명'   TO N'D:\SQLData\ERP_log.ldf',
     RECOVERY, STATS = 10;
```

4. 고아 사용자 점검: `EXEC sp_change_users_login 'Report';` → 필요 시 `ALTER USER [사용자] WITH LOGIN = [로그인];`
5. 대상 서버 버전은 원본과 같거나 높아야 한다.

## 백업 크기 예상 (SSMS 쿼리)

```sql
USE [ERP];
SELECT
    CAST(SUM(FILEPROPERTY(name,'SpaceUsed'))/128.0 AS decimal(18,1))     AS 비압축_예상_MB,
    CAST(SUM(FILEPROPERTY(name,'SpaceUsed'))/128.0*0.5 AS decimal(18,1)) AS 압축_예상_MB_50퍼센트
FROM sys.database_files WHERE type_desc = 'ROWS';

-- 이전 백업 기록(가장 정확)
SELECT TOP 10 backup_start_date, type,
    CAST(backup_size/1048576.0 AS decimal(18,1))            AS 백업크기_MB,
    CAST(compressed_backup_size/1048576.0 AS decimal(18,1)) AS 압축후_MB
FROM msdb.dbo.backupset WHERE database_name = 'ERP' ORDER BY backup_start_date DESC;
```

| 비압축 예상 크기 | 판단 (호스트 `/` 여유 28G 기준) |
|---|---|
| 약 10GB 이하 | 컨테이너 안에 백업해도 안전 |
| 약 10~14GB | 압축 백업 + 컨테이너 쪽 파일 즉시 삭제 |
| 약 14GB 초과 | `/` 가득 참 위험. 데이터 디스크를 컨테이너에 마운트하는 방법 검토 |

## 문제 발생 시

| 증상 | 조치 |
|---|---|
| `Login failed for user 'sa'` | 비밀번호 확인 (`MSSQL_SA_PASSWORD` 값) |
| `sqlcmd: No such file` | 0단계에서 확인한 경로(`tools` / `tools18`)로 수정 |
| `SSL / certificate` 오류 | `-C` 옵션 누락 (tools18에서 필요) |
| `Operating system error 112 (disk full)` | 컨테이너 안 디스크 부족. 백업 파일 삭제 후 DB 크기 재검토 |
| 복사 도중 `/` 가득 참 | 호스트 `/` 여유가 28G뿐이므로 백업 크기를 먼저 확인 |
