# ERP → ERPH202611A 데이터베이스 복제 계획서

- 작성일: 2026-11-24
- 목적: 원본 서버의 `ERP` 데이터베이스를 개발·검증용 서버의 `ERPH202611A` 데이터베이스로 전체 복제
- 근거 문서: [note.md](note.md) — 접속 정보(IP/포트/계정/암호)는 이 문서에 기재하지 않고 note.md를 참조한다
- 결론 요약: **백업/복원(BACKUP/RESTORE) 방식으로 진행한다.** 원본에 암호화(WITH ENCRYPTION) 모듈이 116개 있어 bacpac·스크립팅 방식은 실측 결과 불가능으로 판정되었다. 백업 파일 반출·반입을 위해 **서버 OS(컨테이너) 관리자 협조가 필수**다.

---

## 1. 환경 정보 (2026-11-24 실측 확인)

| 항목 | SOURCE | TARGET |
|---|---|---|
| 서버 | 192.168.1.170,1434 | 192.168.1.21,23302 |
| 접속 계정 | hmp (암호는 note.md) | hmp202611A (암호는 note.md) |
| DB | ERP | ERPH202611A |
| SQL Server 버전 | 2022 RTM-CU27 (16.0.4295.3) | 2022 RTM (16.0.1000.6) |
| 에디션 | Developer (64-bit) | Developer (64-bit) |
| OS | Linux (`/var/opt/mssql/data/`) — 컨테이너 가능성 높음 | 미확인 (포트 23302 → 컨테이너 가능성) |
| 서버/DB Collation | SQL_Latin1_General_CP1_CI_AS | SQL_Latin1_General_CP1_CI_AS (동일) |
| 호환성 수준 | 160 | 160 |
| 복구 모델 | SIMPLE | FULL |
| DB 상태 | ONLINE | ONLINE, **빈 DB** (테이블 0개, 사용 3.7MB) |
| 파일 크기 | 데이터 5,896MB + 로그 2,184MB | 16MB |
| 계정 서버 권한 | sysadmin ✗, dbcreator ✗ | sysadmin ✗, dbcreator ✗ |
| 계정 DB 권한 | ERP의 db_owner | **ERPH202611A의 소유자(dbo)** |
| DB 소유자(owner_sid) | sa | hmp202611A |

### 1.1 원본 DB 개체 현황 (복제 후 검증 기준값)

| 개체 | 수 | 개체 | 수 |
|---|---|---|---|
| 테이블 | 1,418 | 인덱스(테이블, index_id>0) | 2,087 |
| 뷰 | 16 | FK 제약 | 120 (그중 45개 비활성·untrusted) |
| 저장 프로시저 | 3 | CHECK 제약 | 85 |
| 함수 (FN/IF/TF) | 146 | DEFAULT 제약 | 1,453 |
| 시퀀스 | 390 | 시노님 | 3 |
| IDENTITY 열 | 28 | 사용자 스키마 | 3 |
| 트리거 | 0 | 사용자 정의 타입 | 0 |
| DB 사용자 (principal_id>4) | 1 (hmp) | | |

최대 행수 테이블(참고): log_service 604,206 / TCSB_LTRM_NOTI_DTL 517,811 / scheduler_history 485,546 / TCMA_EBGT_DTL 484,674 / TCMW_WRK_CTS 427,999

### 1.2 복제 방식 결정에 영향을 준 실측 결과

| 점검 항목 | 결과 | 영향 |
|---|---|---|
| **WITH ENCRYPTION 모듈** | **116개** (함수·프로시저 대부분) | **bacpac Export·스크립팅 불가** — 암호화 모듈은 정의 추출이 안 되므로 백업/복원만이 완전 복제 수단 |
| 교차 DB/서버 참조 (sys.sql_expression_dependencies) | 없음 | 단일 DB 백업만으로 충분 |
| 비활성·untrusted FK | 45개 (Hibernate 생성 패턴 이름) | 백업/복원은 상태 그대로 보존 — 문제 없음. 복제 후 "비활성 FK 45개"가 정상 상태임을 기억할 것 |
| CLR 어셈블리 / 전체 텍스트 카탈로그 / 메모리 최적화 테이블 | 없음 | 특이 사항 없음 |

### 1.3 사전 점검 판정

- ✅ 양쪽 모두 SQL Server 2022, Collation·호환성 수준 동일 → 복원 호환성 문제 없음
- ✅ 동일 메이저 버전 내 CU 차이(CU27 → RTM)는 복원을 막지 않는다. 상위 CU에서 받은 백업의 하위 CU 복원은 정상 지원된다 (타깃을 최신 CU로 패치하는 것은 보안·버그 관점의 별도 권장 사항일 뿐, 복원의 전제 조건이 아님)
- ✅ 타깃 DB가 존재하고 비어 있으며, 접속 계정 hmp202611A가 그 DB의 **소유자** → 기존 DB를 덮어쓰는 `RESTORE ... WITH REPLACE` 권한 충족 (소유자는 sysadmin/dbcreator 없이도 기존 DB 복원 가능)
- ✅ hmp는 ERP의 db_owner → `BACKUP DATABASE` 권한 충족
- ⚠️ 두 계정 모두 서버 수준 권한 없음 → DB 신규 생성 불가. 반드시 기존 `ERPH202611A`를 대상으로 복원
- ⚠️ 백업 파일이 서버 파일시스템(`/var/opt/mssql/...`)에 생성되므로 **파일 반출·반입은 OS/컨테이너 관리자 협조 필수**

---

## 2. 복제 방안 비교 및 결정

| 구분 | A안: BACPAC (SqlPackage) | **B안: 백업/복원 ← 채택** | C안: 스크립팅 + BCP |
|---|---|---|---|
| 판정 | **불가** — 암호화 모듈 116개는 추출 자체가 안 되어 Export 실패 | **가능** — 암호화 모듈 포함 완전 복제 | **불가** — 암호화 모듈은 스크립팅 불가 |
| 필요 접근 | (SQL 계정만) | SQL 계정 + **서버 파일 접근(관리자 협조)** | (SQL 계정만) |
| 충실도 | — | 가장 충실 (페이지 수준 동일 복제) | — |
| 속도 (약 6GB) | — | 백업·복원 각 수 분 + 파일 전송 수 분 | — |

> A·C안은 "SQL 계정만으로 가능"하다는 장점이 있었으나 1.2의 실측(암호화 모듈 116개)으로 배제되었다. 암호화 모듈을 TDS 접속만으로 추출하는 방법은 없으므로, 관리자 협조 없이 이 복제를 완료할 방법은 없다.

---

## 3. 관리자 협조 요청 사항 (작업 전 확정 필요)

B안은 아래 협조가 전제다. 작업일 확정 전에 미리 합의한다.

1. **소스 서버(192.168.1.170)**: `/var/opt/mssql/data/` 볼륨에 백업 파일 여유 공간 확인 (압축 백업 기준 약 2~4GB 예상, 여유 10GB 권장)
2. **파일 전송**: 소스 → 타깃(192.168.1.21) 백업 파일 복사 (컨테이너라면 `docker cp` + `scp`)
3. **타깃 서버**: 복사된 백업 파일을 SQL Server 프로세스(mssql)가 읽을 수 있는 경로·권한으로 배치
4. **(사전 합의)** 복원 후 hmp202611A 계정이 DB 접근을 잃는 경우(5.4 참조) 관리자가 다음을 실행해 주기로 합의:
   ```sql
   ALTER AUTHORIZATION ON DATABASE::ERPH202611A TO [hmp202611A];
   ```
5. **(대안)** 관리자가 sa로 복원까지 직접 수행하는 방식도 가능 — 이 경우 복원 후 4번 명령만 실행해 주면 되고, 5장의 복원 절차는 관리자 작업으로 대체된다

---

## 4. 백업 단계 (SOURCE)

### 4.1 전제: 쓰기 없는 시간대 확보 (필수)

- 백업 자체는 온라인 백업으로 일관성이 보장되지만, **백업 시점 이후의 변경은 복제본에 반영되지 않는다.** 기준 시점을 명확히 하기 위해 업무 외 시간(쓰기 없는 시간대)에 수행한다.
- 백업 직전 활성 세션 확인(선택):
  ```sql
  SELECT COUNT(*) FROM sys.dm_exec_sessions WHERE database_id = DB_ID('ERP') AND session_id <> @@SPID;
  ```
  (권한 부족으로 조회가 거부되면 생략하고 시간대 통제로 갈음)

### 4.2 백업 실행 (hmp 계정, db_owner 권한으로 가능)

```sql
BACKUP DATABASE ERP TO DISK = '/var/opt/mssql/data/ERP_20261124.bak'
WITH COPY_ONLY, COMPRESSION, CHECKSUM, STATS = 10;
```

- `COPY_ONLY`: 원본의 기존 백업 체인(차등 백업 기준점)을 건드리지 않기 위함 — **원본 무변경 원칙**
- `CHECKSUM`: 백업 파일 손상 조기 감지
- 소요 예상: 수 분 (데이터 약 6GB, 압축 적용)

### 4.3 검증 기준값 채취 (백업 직후, 같은 시간대에 — 필수)

소스는 살아 있는 DB이므로 **백업 직후** 7장의 검증 쿼리 전부를 소스에서 실행해 결과를 파일로 저장한다. 복제 후 비교 기준은 이 문서 1.1의 값이 아니라 **이 시점의 저장 결과**다. (나중에 소스에서 다시 뽑으면 그 사이 변경 때문에 가짜 불일치가 생긴다.)

---

## 5. 파일 전송 및 복원 단계 (TARGET)

### 5.1 파일 반출·반입 (관리자)

```bash
# 소스가 컨테이너인 경우 예시
docker cp <source컨테이너>:/var/opt/mssql/data/ERP_20261124.bak ./
scp ERP_20261124.bak <user>@192.168.1.21:/tmp/
docker cp /tmp/ERP_20261124.bak <target컨테이너>:/var/opt/mssql/data/
# 컨테이너 내부에서 mssql 계정이 읽을 수 있는지 확인 (chown mssql / chmod 644)
```

### 5.2 복원 전 확인 (hmp202611A 계정)

```sql
-- 1) 타깃 DB의 현재 파일 경로 확인 (ERPH202611A 컨텍스트에서) → MOVE 대상 경로
SELECT name, physical_name FROM sys.database_files;

-- 2) 백업 파일의 논리 파일명 확인
RESTORE FILELISTONLY FROM DISK = '/var/opt/mssql/data/ERP_20261124.bak';
```

### 5.3 복원 실행 (master 컨텍스트에서, 소유자 권한으로 가능)

```sql
-- 기존 연결 정리 후 복원 (hmp202611A는 ERPH202611A 소유자이므로 둘 다 실행 가능)
ALTER DATABASE ERPH202611A SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

RESTORE DATABASE ERPH202611A FROM DISK = '/var/opt/mssql/data/ERP_20261124.bak'
WITH REPLACE, CHECKSUM, STATS = 10,
     MOVE 'ERP'     TO '<5.2의 1)에서 확인한 데이터 파일 경로>',
     MOVE 'ERP_log' TO '<5.2의 1)에서 확인한 로그 파일 경로>';

ALTER DATABASE ERPH202611A SET MULTI_USER;
```

- 접속은 `Initial Catalog=master`(기본 DB) 상태에서 실행한다 (복원 대상 DB에 접속한 채로는 복원 불가)
- 소요 예상: 수 분

### 5.4 복원 직후 접근 확인 (중요)

복원된 DB 내부의 `dbo` 사용자는 소스 소유자(sa)의 SID를 갖게 되므로, 복원 후 `hmp202611A`의 dbo 매핑이 깨져 접근을 잃을 수 있다. 즉시 확인한다:

```sql
USE ERPH202611A;  -- 접속이 되는지, USER_NAME()이 dbo인지 확인
SELECT USER_NAME(), SUSER_SNAME();
```

- 접근을 잃은 경우: 본인 권한으로는 복구 불가 → **3장 4번의 사전 합의대로 관리자가 `ALTER AUTHORIZATION` 실행** (이 때문에 사전 합의가 필수)

### 5.5 복원 후 정리

1. 복구 모델을 SIMPLE로 전환 (복원되면 소스 설정인 SIMPLE로 따라오지만 확인; 개발용 DB의 로그 증가 방지):
   ```sql
   SELECT recovery_model_desc FROM sys.databases WHERE name='ERPH202611A';
   ALTER DATABASE ERPH202611A SET RECOVERY SIMPLE;  -- FULL이면 실행
   ```
2. **고아 사용자 정리**: 복원된 DB에는 소스의 DB 사용자 `hmp`가 들어오며, 타깃 서버에 `hmp` 로그인이 없으면 고아 사용자가 된다. 애플리케이션이 hmp202611A로 접속한다면 제거 가능:
   ```sql
   -- hmp가 스키마를 소유하는지 먼저 확인 (소유 중이면 DROP USER가 거부됨)
   SELECT s.name FROM sys.schemas s JOIN sys.database_principals p ON s.principal_id = p.principal_id WHERE p.name = 'hmp';
   -- 소유 스키마가 있으면: ALTER AUTHORIZATION ON SCHEMA::<스키마명> TO dbo; 후
   DROP USER hmp;
   ```
3. 애플리케이션 접속 테스트: hmp202611A 계정으로 주요 테이블 SELECT/INSERT 확인

---

## 6. 재시도·롤백 계획

- 타깃은 현재 빈 DB이므로 보호할 데이터가 없다 → 실패해도 손실 없음
- **복원 실패 시 재시도**: `RESTORE ... WITH REPLACE`는 몇 번이고 다시 실행 가능하다 (bacpac과 달리 "비우기" 작업 불필요). 실패 원인(파일 경로·권한·공간)을 수정하고 같은 명령을 재실행하면 된다
- **주의 — DB를 삭제하지 말 것**: hmp202611A는 소유자이므로 `DROP DATABASE`가 *가능*하지만, dbcreator 권한이 없어 삭제 후 스스로 재생성할 수 없다. 어떤 상황에서도 삭제 대신 REPLACE 복원으로 해결한다
- **원본 무변경 원칙**: 원본 ERP에는 COPY_ONLY 백업 외 어떤 작업도 하지 않는다. 백업 파일은 검증 완료 후 소스 서버에서 삭제(관리자)

---

## 7. 검증 계획

복원 완료 후, 4.3에서 저장한 **백업 시점 기준값**과 비교한다.

1. **개체 수 비교** (기준값은 4.3 저장본; 참고로 2026-11-24 실측은 1.1 표):
   ```sql
   SELECT
     (SELECT COUNT(*) FROM sys.tables)  AS tables,
     (SELECT COUNT(*) FROM sys.views)   AS views,
     (SELECT COUNT(*) FROM sys.procedures) AS procs,
     (SELECT COUNT(*) FROM sys.objects WHERE type IN ('FN','IF','TF')) AS funcs,
     (SELECT COUNT(*) FROM sys.indexes WHERE index_id>0 AND object_id IN (SELECT object_id FROM sys.tables)) AS indexes_on_tables,
     (SELECT COUNT(*) FROM sys.foreign_keys) AS fks,
     (SELECT COUNT(*) FROM sys.foreign_keys WHERE is_disabled=1) AS fks_disabled,  -- 45가 정상
     (SELECT COUNT(*) FROM sys.check_constraints) AS checks,
     (SELECT COUNT(*) FROM sys.default_constraints) AS defaults,
     (SELECT COUNT(*) FROM sys.sequences) AS sequences,
     (SELECT COUNT(*) FROM sys.synonyms) AS synonyms,
     (SELECT COUNT(*) FROM sys.triggers) AS triggers;
   ```
2. **전체 테이블 행수 비교** — 양쪽 결과를 파일로 저장해 diff:
   ```sql
   SELECT s.name + '.' + t.name AS tbl, SUM(p.rows) AS rows
   FROM sys.tables t
   JOIN sys.schemas s ON s.schema_id = t.schema_id
   JOIN sys.partitions p ON p.object_id = t.object_id AND p.index_id IN (0,1)
   GROUP BY s.name, t.name ORDER BY 1;
   ```
3. **IDENTITY 현재값 비교** (28개 열) — 어긋나면 복제 직후 INSERT부터 깨진다:
   ```sql
   SELECT OBJECT_SCHEMA_NAME(object_id) + '.' + OBJECT_NAME(object_id) AS tbl, name, CAST(last_value AS BIGINT) AS last_value
   FROM sys.identity_columns WHERE object_id IN (SELECT object_id FROM sys.tables) ORDER BY 1;
   ```
   (백업/복원은 페이지 수준 복제라 원칙적으로 일치하지만, 확인 1분이면 충분)
4. **시퀀스 현재값 비교** (390개):
   ```sql
   SELECT name, CAST(current_value AS BIGINT) AS cur FROM sys.sequences ORDER BY 1;
   ```
5. **샘플 데이터 체크섬** — 최대 행수 상위 5개 테이블(1.1 참조):
   ```sql
   SELECT CHECKSUM_AGG(CHECKSUM(*)) FROM <테이블> WITH (NOLOCK);
   ```
   ⚠️ `CHECKSUM(*)`은 xml/text/ntext/image 열이 있으면 오류가 난다. 오류 나는 테이블은 행수 + 키 열 MIN/MAX 비교로 대체한다.
6. **기능 확인**: 암호화 모듈이 정상 복제되었는지 대표 함수 1–2개 실행, 뷰 1–2개 SELECT, 애플리케이션 접속 테스트

---

## 8. 위험 요소 및 대응

| # | 위험 | 가능성 | 대응 |
|---|---|---|---|
| 1 | 관리자 협조(파일 전송) 지연으로 일정 밀림 | 중 | 3장 협조 요청을 작업일 확정 전에 합의. 당일 작업은 관리자 가용 시간대에 맞춤 |
| 2 | 복원 후 hmp202611A 접근 상실 (dbo SID 불일치) | 중 | 5.4 절차 + 3장 4번 사전 합의된 ALTER AUTHORIZATION |
| 3 | 백업 시점 이후 원본 변경분 미반영 | 확정적 | 쓰기 없는 시간대에 백업하고 그 시점을 복제 기준 시점으로 공지 |
| 4 | 소스/타깃 디스크 공간 부족 | 중 | 3장 1·3번에서 사전 확인 (소스 여유 10GB, 타깃은 데이터+로그 약 8GB 이상 + 백업 파일) |
| 5 | CU27 백업의 RTM 복원 이상 | 매우 낮음 | 동일 메이저 버전 내 복원은 지원됨. 만일 실패 시에만 타깃 CU 패치 후 재복원 |
| 6 | 고아 사용자(hmp)로 인한 혼선 | 낮음 | 5.5 정리 절차 |
| 7 | 백업 파일 손상 | 낮음 | 백업·복원 모두 CHECKSUM 옵션 사용 |
| 8 | 타깃 DB가 다른 용도로 사용 중일 가능성 | 낮음 | 실측상 빈 DB(3.7MB). 복원 직전 `SELECT COUNT(*) FROM sys.tables`로 재확인 |
| 9 | 복제본에 비활성 FK 45개 존재를 "복제 오류"로 오인 | 낮음 | 원본과 동일한 정상 상태임을 본 문서 1.2에 기록 완료 |

---

## 9. 일정(안) — 당일 완료 기준

관리자 가용 시간이 확보된 날의 업무 외 시간대에 아래를 연속 수행한다. 재시도 여유를 포함해 **3–4시간 창**을 확보한다.

| 순서 | 내용 | 담당 | 소요 예상 |
|---|---|---|---|
| 사전(D-1 이전) | 3장 협조 합의, 소스·타깃 디스크 공간 확인 | 관리자+작업자 | 협의 |
| 1 | 백업 (4장) + 기준값 채취 (4.3) | 작업자 | 15–30분 |
| 2 | 파일 반출·반입 (5.1) | 관리자 | 15–30분 |
| 3 | 복원 (5.2–5.4) | 작업자 (또는 관리자) | 15–30분 |
| 4 | 검증 (7장) + 정리 (5.5) | 작업자 | 30–60분 |
| 5 | 결과 기록 (note.md에 결과·기준 시점 기재), 백업 파일 정리 | 작업자 | 10분 |

실패 시: 당일 창 내에서 원인 수정 후 재복원(6장). 창을 넘기면 중단하고 재작업일을 잡는다 (타깃은 빈/부분 상태로 둬도 무방).

---

## 10. 체크리스트

사전:
- [ ] 관리자 협조 합의 완료 (3장 1–4번, 특히 ALTER AUTHORIZATION 사전 합의)
- [ ] 소스 `/var/opt/mssql` 볼륨 여유 10GB 이상 (관리자)
- [ ] 타깃 볼륨 여유 15GB 이상 (관리자)
- [ ] 쓰기 없는 시간대 확보·공지
- [ ] 타깃 DB가 비어 있음 재확인

당일:
- [ ] COPY_ONLY + CHECKSUM 백업 완료, 파일 크기 정상(약 2–4GB) 확인
- [ ] 백업 직후 소스 기준값 채취·저장 (4.3 — 7장 쿼리 전부)
- [ ] 파일 전송 완료, mssql 프로세스가 읽을 수 있는 권한 확인
- [ ] RESTORE FILELISTONLY로 논리 파일명 확인
- [ ] REPLACE 복원 완료 (master 컨텍스트, SINGLE_USER → MULTI_USER)
- [ ] hmp202611A 접근 확인 (5.4) — 상실 시 관리자 ALTER AUTHORIZATION
- [ ] 복구 모델 SIMPLE 확인
- [ ] 검증: 개체 수(비활성 FK 45 포함)·행수 diff·IDENTITY·시퀀스·체크섬 일치
- [ ] 암호화 함수 실행 테스트, 애플리케이션 접속 테스트
- [ ] 고아 사용자 hmp 정리
- [ ] 소스·타깃·전송 경로의 백업 파일 삭제 또는 안전 보관 결정 (업무 데이터 전체가 포함되므로 주의)
- [ ] note.md에 결과·복제 기준 시점 기록

---

## 부록 A. 작업 PC에서 실행 시 주의

- 쿼리 실행은 SSMS, Azure Data Studio, 또는 sqlcmd(현재 작업 PC 미설치 — 필요 시 `winget install sqlcmd`) 등 임의 도구로 가능. 백업/복원은 T-SQL만으로 수행되므로 특별한 클라이언트 요건 없음
- 암호를 명령행 인수로 넣으면 셸 히스토리(PSReadLine `ConsoleHost_history.txt`)와 프로세스 목록에 남는다. 가능하면 접속 대화상자/프롬프트로 입력하고, 명령행 사용 시 작업 후 히스토리를 정리한다
- PowerShell에서 `!`·`@` 포함 암호는 작은따옴표로 감싼다 (cmd.exe에서는 `!`가 소실될 수 있으므로 사용하지 말 것)

## 부록 B. (참고) bacpac 방식이 배제된 근거 상세

- `sys.sql_modules`에서 `definition IS NULL`인 모듈 116개 확인 (2026-11-24) — WITH ENCRYPTION으로 생성된 함수·프로시저는 DacFx가 정의를 추출할 수 없어 Export가 실패한다
- 같은 이유로 스크립팅(C안)도 불가. 암호화 모듈의 원본 소스를 별도 보유하고 있다면 추후 A·C안 재검토 가능하나, 백업/복원이 가능한 이상 필요 없음
- 그 외 bacpac 제약(트랜잭션 일관성 미보장, 타깃 로그인 부재 시 Import 실패, 기본 60초 CommandTimeout 등)도 검토 과정에서 확인되었으나, 방식 배제로 본 계획과는 무관해짐
