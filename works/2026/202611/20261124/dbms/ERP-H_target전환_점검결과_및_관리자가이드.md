# products-erp-h target DB 전환 점검 결과 및 서버 관리자 가이드

- 작성일: 2026-11-25
- 대상: products-erp-h 의 데이터베이스를 source(192.168.1.170,1434 / ERP)에서 target(192.168.1.21,23302 / ERPH202611A)으로 전환
- 관련 문서: [ERP_복제결과리포트.md](ERP_복제결과리포트.md), [ERP_복제계획서.md](ERP_복제계획서.md), [note.md](note.md)
- 점검 방식: 코드 정적 분석 + target 읽기 전용 조회 + 사용자 기동 시 오류 로그 분석 (화면 실행 검증은 하지 않음)

---

## 1. 종합

| 구분 | 내용 | 복제 실패와의 관계 | 상태 |
|---|---|---|---|
| 기동 실패 | 접속 계정 기본 스키마가 dbo 라서 Flowable 스키마 업그레이드 SQL 실패 | 무관 | 관리자 조치 필요 (3장) |
| 업무 DB 접속처 | 복제된 HMP.datasource 의 MAIN/local 행이 source 를 가리킴 | 무관 | 조치 필요 (2.2) |
| 암호화 모듈 116개 부재 | 112개를 mapper·Java SQL 에서 호출, 화면 실행 시 오류 | 복제 실패 대상 | 백업/복원(B안) 필요 (2.3) |
| DEFAULT 제약 30건 누락 | 전 열 NULL 허용, 오류 없이 NULL 입력 | 복제 실패 대상 | 영향 제한적 (2.4) |

## 2. 점검 결과

### 2.1 설정 위치
- 플랫폼 DB: app/src/main/resources/datasource.yml 의 spring.datasource (local 프로파일 기본값이 source)
- 접속 정보 덮어쓰기: 환경변수 PLATFORM_DB_URL, PLATFORM_DB_USER, PLATFORM_DB_PASSWORD
- 기본값의 암호문은 source 계정(hmp)의 것이므로 target 계정용으로 다시 만들어야 함

### 2.2 업무 DB(ERP:MAIN) 접속처가 source 로 남음
- 업무 DB 는 spring.datasource 가 아니라 target 의 HMP.datasource 테이블에서 동적 로드됨
- 복제된 행 현황

| datasource_id | profile | 접속처 |
|---|---|---|
| MAIN | dev | sql-h.infra.svc.cluster.local:1433 / ERP |
| MAIN | local | 192.168.1.170:1434 / ERP (source) |
| SHARED_KNOWLEDGE | dev | pgb-dev.infra.svc.cluster.local:6432 (PostgreSQL) |
| SHARED_KNOWLEDGE | local | pg-dev.hmplus.io:6432 (PostgreSQL) |

- 영향: 플랫폼 DB 만 target 으로 바꾸면 업무 쿼리와 쓰기가 source 로 향함
- 조치: MAIN/local 행의 db_url, db_username, db_password 를 target 으로 변경 (비밀번호 컬럼은 이번 점검에서 읽지 않음)

### 2.3 암호화 모듈 116개 부재
- target 존재 여부: 0/116 (전부 없음)
- 실제 SQL(주석 제외)에서 호출하는 개체: 112개, 영향 파일 225개
- 호출하지 않는 4개: FCMA_CHG_OBJ_YN, FSMC_PRPD_PAY, VQMZ_VENDOR, VSOZ_PJT_INFO
- 호출 빈도가 높은 개체(파일 수): STR_SUB 103, STR_CAT2 49, STR_NUM 37, FMT_DT8 26, FCMA_EBGT_NM 28, FCOZ_GET_DATE 28
- 오류 시점: 기동이 아니라 해당 화면 실행 시점 (Invalid object name 계열)
- 영향이 확실한 화면·매퍼
  - 대시보드: dashBoardCM, dashBoardSM, dashBoardSite
  - 예산: EbgtSave, EbgtEvalQ, EbgtXcpt, EbgtXlsVldt
  - 기타: fndReq, prgsObjCurst, closMont, hoTrnsfXls
- 복구 경로
  - 백업/복원(B안): 116개 전체 복구 가능
  - 저장소 SQL: 58개는 CREATE 원문이 있음 (db/cm-module/cm-module-ddl-mssql.sql, cs 설치 스크립트). 원본 정의와 동일한지는 미확인
  - STR_SUB, STR_CAT2, FMT_DT8, FCOZ_GET_DATE 등 핵심 함수는 저장소에 원문이 없어 B안으로만 복구 가능

### 2.4 DEFAULT 제약 30건 누락
- 대상: VPLUS40 스키마 23개 테이블의 30개 열 (ins_datetime 위주, TLAB_MONMAGAM.ETC_AMT, TSOC_JOINT_INFO 7개 열)
- 전 열이 NULL 허용이라 INSERT 는 실패하지 않음
- 영향: 코드가 값을 직접 넣지 않으면 기본값 대신 NULL 이 입력됨. 금액 열은 계산에 NULL 이 섞일 수 있음 (해당 코드는 미확인)

### 2.5 기본 스키마 차이 (기동 실패 원인)
- 계정별 기본 스키마

| 환경 | 로그인 | DB 사용자 | 기본 스키마 |
|---|---|---|---|
| source | hmp | hmp | HMP |
| target | hmp202611A | dbo | dbo |

- target 의 HMP·VPLUS40·hmp_ai 스키마 개체는 그대로 존재하나, 스키마 없는 SQL 은 기본 스키마(dbo)에서만 이름을 찾음
- Flowable 테이블(ACT_* 43개, FLW_* 6개)은 모두 HMP 스키마에 있음
- 기동 오류 발생 과정
  1. Flowable 이 기존 ACT_* 테이블을 찾아 스키마 업그레이드 경로로 진입
  2. 스키마 없는 insert into ACT_GE_PROPERTY 를 실행
  3. dbo 에서 이름을 찾지 못해 SQLServerException 발생 (개체 이름 'ACT_GE_PROPERTY'이(가) 유효하지 않습니다)
  4. processEngine 부터 dmnEngine, DecisionService, ToolExecutor 까지 빈 생성이 연쇄 실패
- 같은 원인의 추가 영향: 코어의 스키마 없는 SQL(FROM hmp_user)도 실패할 수 있음 (UsrMngLinkService.java 주석에 11/06 sql-h 동일 증상 이력)
- 스키마 없이 쓴 VPLUS40 테이블명 1,320건은 source 에서도 해석되지 않음을 표본 1건으로 확인 (target 에서 새로 생기는 문제는 아닌 것으로 판단, 전수 미검증)

### 2.6 적용 시도와 결과
- 시도: ALTER USER hmp202611A WITH DEFAULT_SCHEMA = HMP
- 결과: 거부 (사용자가 없거나 권한이 없어서 변경할 수 없습니다). target 변경 사항 없음
- 원인: hmp202611A 가 DB 소유자라서 dbo 로 매핑되며, dbo 의 기본 스키마는 변경 불가
- 권한 확인: sysadmin 아님, ALTER ANY LOGIN 없음 (DB 소유자 변경 불가)

### 2.7 대안 비교

| 방법 | 내용 | 해소 범위 |
|---|---|---|
| A. 서버 관리자 처리 (권장) | DB 소유자 변경 후 앱 계정을 일반 사용자로 등록 | Flowable·코어 모두 |
| B. 앱 설정으로 Flowable 만 우회 | flowable.database-schema=HMP 지정 | Flowable 만, 코어 SQL 은 실행 시점에 재실패 가능 |
| C. dbo 시노님 생성 | HMP 개체 1,330개에 dbo 시노님 | 대부분 해소, 구조가 원본과 달라짐 |

---

## 3. 서버 관리자 가이드 (대안 A)

### 3.1 배경
- 로그인 hmp202611A 가 DB 소유자라서 DB 안에서 dbo 로 매핑되고 기본 스키마가 dbo 로 고정됨
- 앱은 스키마 없는 SQL 을 쓰며 개체는 모두 HMP 스키마에 있어 기동이 실패함

### 3.2 목표 상태
- DB 소유자: 관리자 로그인(예: sa)
- hmp202611A: 일반 DB 사용자, 기본 스키마 HMP, 역할 db_owner
- 앱 접속 정보(계정, 비밀번호)는 변경 없음

### 3.3 사전 조건
- erp-h 종료 (기존 세션은 재접속 전까지 dbo 로 남음)
- 기본 DB 백업 권장 (메타데이터만 변경하며 데이터는 변경하지 않음)
- 새 소유자로 지정할 로그인이 이 DB 에 사용자로 등록돼 있지 않아야 함 (sa 는 해당 없음)
- 백업/복원(B안)을 함께 수행할 경우 복원을 먼저 하고 본 작업을 나중에 수행 (3.8 참조)

### 3.4 실행 (sysadmin)
```sql
-- 1) 현재 상태 확인 (기대: owner_login = hmp202611A)
SELECT d.name, SUSER_SNAME(d.owner_sid) AS owner_login
FROM sys.databases d WHERE d.name = 'ERPH202611A';

-- 2) DB 소유자 변경
ALTER AUTHORIZATION ON DATABASE::[ERPH202611A] TO [sa];

-- 3) 앱 계정을 일반 사용자로 등록 (기본 스키마 HMP)
USE [ERPH202611A];
CREATE USER [hmp202611A] FOR LOGIN [hmp202611A] WITH DEFAULT_SCHEMA = [HMP];
ALTER ROLE db_owner ADD MEMBER [hmp202611A];
```

### 3.5 검증 (hmp202611A 로 접속하여 실행)
```sql
SELECT USER_NAME()                  AS db_user,         -- 기대: hmp202611A
       SCHEMA_NAME()                AS default_schema,  -- 기대: HMP
       OBJECT_ID('ACT_GE_PROPERTY') AS act_tbl,         -- 기대: NULL 아님
       OBJECT_ID('hmp_user')        AS user_tbl,        -- 기대: NULL 아님
       IS_MEMBER('db_owner')        AS is_owner;        -- 기대: 1
```
- 통과 후 erp-h 를 재기동하여 Flowable 오류 해소 여부 확인

### 3.6 원복
```sql
USE [ERPH202611A];
DROP USER [hmp202611A];
ALTER AUTHORIZATION ON DATABASE::[ERPH202611A] TO [hmp202611A];
```
- 새 소유자가 이미 DB 사용자로 등록돼 있으면 소유자 변경이 실패하므로 DROP USER 를 먼저 수행

### 3.7 주의사항
- db_owner 멤버이므로 DB 안의 작업 권한은 기존과 동일
- 이후 앱이 새로 만드는 테이블(예: Flowable)은 dbo 가 아니라 HMP 스키마에 생성됨 (의도한 동작)
- 서버 단위 권한은 원래 없었으므로 변동 없음

### 3.8 백업/복원(B안)을 함께 수행하는 경우
- 순서: 복원 후 본 보정 수행 (복원 시 DB 가 통째로 교체되기 때문)
- 원본의 DB 사용자 hmp 는 기본 스키마가 HMP 이며, 복원 직후에는 로그인에 연결되지 않은 상태(orphan)임
- 소유자 변경 후 아래로 연결하면 3.4 의 CREATE USER 는 불필요
  ```sql
  ALTER USER [hmp] WITH LOGIN = [hmp202611A];
  ALTER ROLE db_owner ADD MEMBER [hmp];
  ```
- 이 경우 앱 계정은 hmp 사용자(기본 스키마 HMP)로 접속하게 됨

### 3.9 관리자 회신 요청 항목
- 소유자 로그인으로 지정한 계정명 (sa 등)
- 3.5 검증 쿼리 결과

---

## 4. 후속 작업 체크리스트

| 순번 | 작업 | 담당 | 비고 |
|---|---|---|---|
| 1 | 3장 가이드 수행 (기본 스키마 보정) | 서버 관리자 | 기동 실패 해소 |
| 2 | 백업/복원(B안) 재복제 | 서버 관리자 | 암호화 모듈 116개 복구, 수행 시 1번을 복원 후에 수행 |
| 3 | HMP.datasource MAIN/local 행을 target 으로 변경 | 개발 | 업무 DB 접속처 |
| 4 | datasource.yml(또는 환경변수)의 접속 계정·암호문을 target 용으로 교체 | 개발 | 현재 암호문은 source 계정용 |
| 5 | 재기동 후 주요 화면 확인 | 개발 | 대시보드·예산 화면 우선 (2.3 목록) |
| 6 | DEFAULT 30건 영향 코드 확인 | 개발 | ins_datetime, ETC_AMT, TSOC_JOINT_INFO |

## 5. 점검 한계
- 정적 분석 기반이며 화면을 실제로 열어 확인하지 않음
- 호출 mapper 가 모든 화면에서 사용되는지는 구분하지 않음
- target 에는 읽기 전용 조회만 수행하였고, 변경 시도(ALTER USER)는 거부되어 반영된 것이 없음
- source 에는 hmp 사용자의 기본 스키마 1건을 읽기 전용으로 조회함
