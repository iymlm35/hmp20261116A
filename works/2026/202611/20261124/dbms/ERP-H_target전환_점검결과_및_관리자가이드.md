# products-erp-h target DB 전환 점검 결과 및 서버 관리자 가이드

- 작성일: 2026-11-25
- 대상: products-erp-h 의 데이터베이스를 source(192.168.1.170,1434 / ERP)에서 target(192.168.1.21,23302 / ERPH202611A)으로 전환
- 관련 문서: [ERP_복제결과리포트.md](ERP_복제결과리포트.md), [ERP_복제계획서.md](ERP_복제계획서.md), [note.md](note.md)
- 점검 방식: 코드 정적 분석 + target 읽기 전용 조회 + 사용자 기동 시 오류 로그 분석 (화면 실행 검증은 하지 않음)

---

## 1. 종합

| 구분 | 내용 | 복제 실패와의 관계 | 상태 |
|---|---|---|---|
| 기동 실패 | 접속 계정 기본 스키마가 dbo 라서 Flowable 스키마 업그레이드 SQL 실패 | 무관 | 관리자 조치 완료, hmp202611A 세션 검증 통과 (2.6), 재기동 확인 대기 |
| 업무 DB 접속처 | 복제된 HMP.datasource 의 MAIN/local 행이 source 를 가리킴 | 무관 | UPDATE 필요 (2.2, 4장) |
| 업무 DB 접속 계정 | MAIN 행의 접속 계정이 vplus40 이며 target 에 로그인이 없어 기동 후 업무 요청에서 로그인 실패 발생, 기본 스키마 가설 미검증 | 무관 (로그인은 복제 대상이 아님) | 관리자 조치 필요 (2.8, 4.6) |
| 시퀀스 값 | 시퀀스 8개가 테이블 최대 키보다 뒤처져 log_login 등에서 중복 키 오류 발생 | 복제 작업의 결손 (시퀀스 복원 시점 차이) | 보정 완료, 확인됨 (2.9) |
| 암호화 모듈 116개 부재 | 112개를 mapper·Java SQL 에서 호출, 화면 실행 시 오류 | 복제 실패 대상 | 백업/복원(B안) 필요 (2.3) |
| DEFAULT 제약 30건 누락 | 전 열 NULL 허용, 오류 없이 NULL 입력 | 복제 실패 대상 | 영향 제한적 (2.4) |

## 2. 점검 결과

### 2.1 설정 위치
- 플랫폼 DB: app/src/main/resources/datasource.yml 의 spring.datasource (local 프로파일 기본값이 source)
- 접속 정보 덮어쓰기: 환경변수 PLATFORM_DB_URL, PLATFORM_DB_USER, PLATFORM_DB_PASSWORD
- 기본값의 암호문은 source 계정(hmp)의 것이므로 target 계정용으로 다시 만들어야 함

### 2.2 업무 DB(ERP:MAIN) 접속처가 source 로 남음
- 앱은 DB 연결을 두 개 사용함

| 구분 | 정하는 곳 | 쓰임새 | 현재 가리키는 곳 |
|---|---|---|---|
| 플랫폼 DB | datasource.yml 의 spring.datasource | 프레임워크 테이블 (사용자, 설정, 결재 이력, Flowable 등) | target (사용자가 변경) |
| 업무 DB (ERP:MAIN) | target 의 HMP.datasource 테이블 | 업무 화면 데이터 (VPLUS40 테이블, 업무 함수, 뷰) | source (192.168.1.170:1434) |

- 근거
  - datasource.yml 주석: 업무 DB 는 여기 두지 않고 ERP:MAIN 으로 hmp.datasource 테이블에 등록되어 동적 로드됨
  - 서비스 클래스 404개에 @UseDataSource(systemId = "ERP", datasourceId = "MAIN") 가 붙어 업무 서비스가 모두 MAIN 연결을 사용함
- source 에서는 플랫폼 DB 와 MAIN 이 같은 ERP 데이터베이스였으나, 복제로 HMP.datasource 의 source 값이 target 에 그대로 들어옴
- 앱은 기동 시 플랫폼 DB(target)에서 HMP.datasource 를 읽어 현재 프로파일(local)의 행으로 업무 DB 를 연결하므로 업무 연결이 source 로 향함
- 복제된 행 현황 (2026-11-25 SSMS 조회로 db_username 확인)

| datasource_id | profile | 접속처 | db_username |
|---|---|---|---|
| MAIN | dev | sql-h.infra.svc.cluster.local:1433 / ERP | vplus40 |
| MAIN | local | 192.168.1.170:1434 / ERP (source) | vplus40 |
| SHARED_KNOWLEDGE | dev | pgb-dev.infra.svc.cluster.local:6432 (PostgreSQL) | hmp |
| SHARED_KNOWLEDGE | local | pg-dev.hmplus.io:6432 (PostgreSQL) | hmp |

- 영향
  - 플랫폼 데이터(사용자, 권한, 결재 이력)는 target, 업무 데이터(예산, 계약, 현장)는 source 에 읽고 쓰게 됨
  - source 는 운영 중인 DB 라서 테스트 입력이 source 에 들어갈 수 있으며 되돌리기 어려움
  - 두 DB 에 걸친 트랜잭션으로 데이터가 어긋날 수 있음 (코드 주석에 3-DB 주의 있음)
  - 업무 쿼리가 source 로 가면 암호화 모듈 116개 부재(2.3)가 가려져 target 검증이 되지 않음
- 조치: MAIN/local 행의 db_url 을 target 으로 변경, 접속 계정과 비밀번호는 2.8 에 따라 결정 (변경 SQL 은 4장, 비밀번호 컬럼은 이번 점검에서 읽지 않음)
- 재기동 없이 반영되는지는 미확인이며 변경 후 재기동 필요로 간주
- 경과 (2026-11-25 재조회)
  - MAIN/local 행의 db_url 이 target(192.168.1.21:23302 / ERPH202611A)으로 변경된 것을 확인함
  - upd_datetime(2026-11-03), upd_user_id(cs)는 갱신되지 않아 감사 컬럼 없이 직접 수정된 것으로 보임
  - db_username 은 vplus40 그대로이며 이 때문에 2.8 의 로그인 실패가 발생함

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
- 정정: 스키마 없이 쓴 VPLUS40 테이블명 1,320건(예: TCSB_ACP_DTLS)에 대해 초기 점검에서 source 에서도 해석되지 않는다고 기록하였으나 철회함
  - 확인에 사용한 계정이 hmp(플랫폼 DB 계정)였고, 이 참조들을 쓰는 업무 연결은 vplus40 계정(2.2)이라 근거가 부족함
  - 이 문제는 업무 DB 접속 계정의 기본 스키마와 관련되며 2.8 에서 다룸

### 2.6 적용 시도와 결과
- 시도: ALTER USER hmp202611A WITH DEFAULT_SCHEMA = HMP
- 결과: 거부 (사용자가 없거나 권한이 없어서 변경할 수 없습니다). target 변경 사항 없음
- 원인: hmp202611A 가 DB 소유자라서 dbo 로 매핑되며, dbo 의 기본 스키마는 변경 불가
- 권한 확인: sysadmin 아님, ALTER ANY LOGIN 없음 (DB 소유자 변경 불가)
- 이후 경과: 서버 관리자가 3장 가이드를 적용함
  - 사용자 제공 캡처(sa 세션)는 db_user = dbo, default_schema = dbo, 두 OBJECT_ID 가 NULL 로 나왔으나 sa 는 항상 dbo 로 매핑되므로 검증으로 인정하지 않음 (3.5 는 hmp202611A 로 접속하여 실행해야 함)
  - hmp202611A 세션 직접 검증 결과 (2026-11-25)

| 항목 | 기대값 | 실제 |
|---|---|---|
| db_user | hmp202611A | hmp202611A |
| default_schema | HMP | HMP |
| OBJECT_ID('ACT_GE_PROPERTY') | NULL 아님 | 148195578 |
| OBJECT_ID('hmp_user') | NULL 아님 | 1585440722 |
| IS_MEMBER('db_owner') | 1 | 1 |
| DB 소유자 로그인 | 관리자 로그인 | sa |

  - 스키마 없는 이름이 해석되므로 Flowable 오류는 해소될 것으로 판단하나 재기동 결과는 미확인

### 2.7 대안 비교

| 방법 | 내용 | 해소 범위 |
|---|---|---|
| A. 서버 관리자 처리 (권장) | DB 소유자 변경 후 앱 계정을 일반 사용자로 등록 | Flowable·코어 모두 |
| B. 앱 설정으로 Flowable 만 우회 | flowable.database-schema=HMP 지정 | Flowable 만, 코어 SQL 은 실행 시점에 재실패 가능 |
| C. dbo 시노님 생성 | HMP 개체 1,330개에 dbo 시노님 | 대부분 해소, 구조가 원본과 달라짐 |

- 채택: A (관리자 처리 완료, 2.6 참조)

### 2.8 업무 DB 접속 계정(vplus40) 문제
- 계정 구성 (source 기준)
  - 플랫폼 DB: hmp 계정 (기본 스키마 HMP)
  - 업무 DB(MAIN): vplus40 계정
- target 현황
  - 로그인은 hmp202611A 하나뿐
  - DB 사용자 hmp, vplus40 은 복제 시 소유자 용도로 로그인 없이(WITHOUT LOGIN) 생성되었고 기본 스키마는 둘 다 dbo
- 영향: MAIN 행을 hmp202611A 로 바꾸면 기본 스키마가 HMP 라서, 스키마 없이 쓴 VPLUS40 테이블 참조 1,320건(예: TCSB_ACP_DTLS)이 해석되지 않을 가능성이 큼
- 가설: source 의 vplus40 로그인은 기본 스키마가 VPLUS40 이고 이 때문에 위 참조가 source 에서 동작함
- 미검증 사항: source 에 hmp 계정으로 조회했을 때 vplus40 사용자가 메타데이터에 보이지 않아 기본 스키마를 확인하지 못함 (관리자 권한 조회 필요, 4.5 참조)
- 접속 계정 방안

| 방안 | 내용 | 평가 |
|---|---|---|
| 1. vplus40 로그인 구성 (권장) | 관리자가 target 에 vplus40 로그인을 만들어 DB 사용자 vplus40 에 연결하고 기본 스키마를 VPLUS40 으로 맞춤, MAIN 행은 db_username 유지 | 원본과 같은 구성 |
| 2. hmp202611A 로 업무 DB 접속 | MAIN 행의 db_username 을 hmp202611A 로 변경 | 임시용, 기본 스키마가 HMP 라서 업무 화면 전체에서 실패할 가능성이 큼 |

- 채택: 방안 1 (관리자 처리, 4.6 참조)
- 발생한 오류 (2026-11-25 10:08 재기동 후 콘솔 로그)
  - 요청: /cse/getGrd_JcAnncBrdLst, 예외 위치: WorkCorp.allGrant, WorkCorpMapper.countAllGrant (erp-common-workCorp.xml)
  - 원인 예외: HikariPool 초기화 실패, 사용자 'vplus40'이(가) 로그인하지 못했습니다 (SQLServerException)
  - 해당 쿼리는 HMP.user_role 을 조회하는 권한 확인이며 MAIN 연결로 실행되므로, 업무 DB 연결을 쓰는 요청 대부분이 같은 오류로 실패할 수 있음
  - 업무 계정은 VPLUS40 스키마뿐 아니라 HMP 스키마 읽기 권한도 필요함
- 원인: target 에는 로그인 vplus40 이 없고 로그인 없는(WITHOUT LOGIN) DB 사용자 vplus40 만 있어 접속이 거부됨
- 구분 방법: 서버 오류 로그(SQL Server ERRORLOG)의 Login failed for user 'vplus40' 사유가 로그인 없음인지 비밀번호 불일치인지 확인, 현재는 로그인 부재로 판단하나 미확정
- 경과 (2026-11-25 10:22 재기동 후 재발생)
  - 오류 메시지가 로그인에서 요청한 데이터베이스 "ERPH202611A"을(를) 열 수 없습니다 로 바뀜 (SQL Server 오류 4060)
  - 해석: 서버 로그인은 인증을 통과했으나 ERPH202611A 에 그 로그인에 매핑된 DB 사용자가 없음
  - target 조회 결과: DB 사용자 vplus40 의 기본 스키마는 VPLUS40 으로 변경돼 있으나 인증 방식이 NONE(로그인에 연결 안 됨)이며, guest 접근은 허용되지 않음
- 경과 (10:25 ALTER USER 시도)
  - ALTER USER [vplus40] WITH LOGIN = [vplus40] 실행 시 오류 33016 으로 거부됨 (SSMS 캡처로 확인)
  - 원인: 로그인 없이 만든 사용자는 WITH LOGIN 으로 연결할 수 없음, 4.6.5 의 최초 가이드가 부정확했으며 2-b 로 정정함

### 2.9 시퀀스가 테이블 최대 키보다 뒤처짐 (복제 작업의 결손)
- 증상: 재기동 후 PRIMARY KEY 제약 조건 'PK_log_login_seqfix' 위반, 개체 'HMP.log_login' 중복 키 (65316), 일괄 INSERT(BatchUpdateException)
- 구조: HMP.log_login 의 log_id 는 IDENTITY 가 아니라 시퀀스 HMP.log_login_log_id_seq 로 채번
- 원인: 시퀀스 다음 값은 65316 인데 테이블에는 log_id 65321 까지 존재 (9,865행, 마지막 기록 2026-11-24 18:20)
  - 복제 리포트는 시퀀스를 현재값 이후부터 이어지도록 RESTART 했다고 하나 이 값은 데이터 복사 전 추출 시점 값이며, 그 사이 source 에 행이 추가됨
  - 시퀀스 대비 테이블 최대값 검증은 복제 검증 항목에 없었음
- 전수 대조: 시퀀스 390개 중 이름 규칙(<테이블>_<열>_seq)으로 테이블과 연결되는 370개를 테이블 최대값과 비교, 8개가 뒤처짐 (이름 규칙에 맞지 않는 20개는 대조 제외)

| 시퀀스 | 시퀀스 현재값 | 테이블 최대값 | 충돌 번호 수 |
|---|---|---|---|
| scheduler_history_history_id_seq | 499,825 | 500,659 | 835 |
| metric_snapshot_id_seq | 796,636 | 796,860 | 225 |
| log_service_log_id_seq | 1,772,829 | 1,773,040 | 212 |
| schema_migration_installed_rank_seq | 4 | 34 | 31 |
| log_login_log_id_seq | 65,316 | 65,321 | 6 |
| log_page_log_id_seq | 336,207 | 336,212 | 6 |
| notification_notification_id_seq | 758 | 758 | 1 |
| log_error_log_id_seq | 272,220 | 272,220 | 1 |

- 영향: 새 번호가 기존 번호를 지날 때까지 중복 키 오류가 반복되고, 해당 시점의 로그 기록이 저장되지 않을 수 있음
- 조치: 시퀀스를 테이블 최대값 + 1 로 RESTART (데이터 변경 없음, 값 되돌리기 가능), 아래 스크립트는 사용자가 target 에 적용함
```sql
USE ERPH202611A;
SET NOCOUNT ON;
DECLARE @seq sysname, @sch sysname, @tbl sysname, @col sysname, @cur bigint, @mx bigint, @sql nvarchar(max);
DECLARE c CURSOR LOCAL FAST_FORWARD FOR
  SELECT SCHEMA_NAME(s.schema_id), s.name, t.name, col.name, CAST(s.current_value AS bigint)
  FROM sys.sequences s
  JOIN sys.tables t    ON t.schema_id = s.schema_id
  JOIN sys.columns col ON col.object_id = t.object_id AND s.name = t.name + '_' + col.name + '_seq'
  JOIN sys.types ty    ON ty.user_type_id = col.user_type_id AND ty.name IN ('bigint','int','smallint');
OPEN c;
FETCH NEXT FROM c INTO @sch, @seq, @tbl, @col, @cur;
WHILE @@FETCH_STATUS = 0
BEGIN
  SET @sql = N'SELECT @mx = MAX(' + QUOTENAME(@col) + N') FROM ' + QUOTENAME(@sch) + N'.' + QUOTENAME(@tbl);
  EXEC sp_executesql @sql, N'@mx bigint OUTPUT', @mx = @mx OUTPUT;
  IF @mx IS NOT NULL AND @mx >= @cur
  BEGIN
    SET @sql = N'ALTER SEQUENCE ' + QUOTENAME(@sch) + N'.' + QUOTENAME(@seq) + N' RESTART WITH ' + CAST(@mx + 1 AS nvarchar(20));
    PRINT @sql;
    EXEC sp_executesql @sql;
  END
  FETCH NEXT FROM c INTO @sch, @seq, @tbl, @col, @cur;
END
CLOSE c; DEALLOCATE c;
```
- 적용 결과 확인 (2026-11-25 재기동 후 재조회)

| 테이블 | 테이블 최대 키 | 시퀀스 (사용된 마지막 값) |
|---|---|---|
| log_login | 65,322 | 65,322 |
| log_page | 336,214 | 336,214 |
| scheduler_history | 500,738 | 500,738 |
| log_service | 1,773,075 | 1,773,240 |

  - 8개 시퀀스 모두 이전 최대값보다 큰 값으로 진행된 것을 확인, 위 4개 테이블은 최대 키와 직접 비교함
  - 재기동 후 콘솔 로그에 PK_log_login_seqfix 중복 키 오류는 나타나지 않음
- 재발 방지: B안(백업/복원)으로 재복제하면 시퀀스도 함께 복원되므로 이 문제는 해소되나, 복제 후에는 시퀀스와 테이블 최대값 대조를 검증 항목에 포함하는 것이 바람직함

### 2.10 기타 기동 로그 (Redis)
- WARN: Unable to connect to Redis (co-edit relay publish failed for data:SCHEDULER_HISTORY, co-edit sweep failed)
- DB 전환과 무관하며 로컬에 Redis 가 기동되지 않은 상태로 판단, 공동 편집 기능에만 영향이 있을 가능성이 큼
- app/docker-compose.yml 에 redis 서비스가 있으므로 필요 시 기동, 전환 전에도 동일했는지는 미확인

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
- 반드시 hmp202611A 로 접속하여 실행 (sa 등 관리자 세션은 항상 dbo 로 매핑되어 검증이 되지 않음)
- 수행 결과는 2.6 참조 (전 항목 통과)
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

## 4. 업무 DB 접속 설정 보정 (HMP.datasource)

### 4.1 위치
- HMP.datasource 는 파일이 아니라 target 데이터베이스 안의 테이블임

| 단계 | 값 |
|---|---|
| 서버 | 192.168.1.21,23302 (SQL Server 2022) |
| 데이터베이스 | ERPH202611A |
| 스키마 | HMP |
| 테이블 | datasource (36개 컬럼, 4행) |
| 전체 이름 | ERPH202611A.HMP.datasource |

- source(192.168.1.170,1434 / ERP)에도 같은 테이블이 있으며 복제로 target 에 그대로 들어옴, 수정은 반드시 target 에서 수행하고 source 는 수정하지 않음
- 저장소(products-erp-h)의 .sql 파일에서 이 테이블의 시드는 찾지 못함, 값 변경은 DB 의 행을 직접 수정

### 4.2 조회
```sql
USE ERPH202611A;
SELECT datasource_id, profile, system_id, db_url, db_username, use_yn
FROM HMP.datasource;
```
- 수정 대상: system_id = 'ERP', datasource_id = 'MAIN', profile = 'local' 인 1행
- dev 프로파일 행(sql-h)과 SHARED_KNOWLEDGE 행(PostgreSQL)은 이번 전환과 무관하므로 수정하지 않음

### 4.3 UPDATE (sa 세션으로 실행 가능, 모든 이름에 스키마 명시)
방식 1 (권장): 관리자가 target 에 vplus40 로그인을 구성한 뒤 실행, 접속 주소만 변경하고 db_username 은 vplus40 유지
```sql
USE ERPH202611A;

BEGIN TRAN;

UPDATE HMP.datasource
   SET db_url = 'jdbc:sqlserver://192.168.1.21:23302;databaseName=ERPH202611A;encrypt=false;trustServerCertificate=true'
 OUTPUT deleted.datasource_id, deleted.profile, deleted.db_url AS old_url,
        inserted.db_url AS new_url, inserted.db_username
 WHERE system_id     = 'ERP'
   AND datasource_id = 'MAIN'
   AND profile       = 'local';

-- 정확히 1행이 아니면 되돌림
IF @@ROWCOUNT <> 1
BEGIN
    ROLLBACK;
    PRINT '대상이 1행이 아님 - 롤백';
END
ELSE
BEGIN
    COMMIT;
    PRINT '완료';
END
```
- OUTPUT 으로 변경 전후 값이 결과 창에 표시되며, 원복은 old_url 값을 다시 넣으면 됨
- 비밀번호(db_password)는 위 SQL 에 포함하지 않음, 4.4 참조
- 2026-11-25 재조회 시점에 db_url 은 이미 target 값으로 변경돼 있어 이 UPDATE 는 다시 실행할 필요가 없음 (2.2 경과)

방식 2 (임시, 실패 가능성 큼): hmp202611A 로 업무 DB 도 접속하는 경우, 위 UPDATE 의 SET 절을 아래로 교체
```sql
   SET db_url      = 'jdbc:sqlserver://192.168.1.21:23302;databaseName=ERPH202611A;encrypt=false;trustServerCertificate=true',
       db_username = 'hmp202611A'
```
- 기본 스키마가 HMP 라서 스키마 없는 VPLUS40 테이블 참조가 실패하여 기동은 되어도 업무 화면에서 Invalid object name 오류가 날 가능성이 큼 (2.8)

### 4.4 비밀번호(db_password)
- datasource.yml 의 ENC(...) 와 같은 암호문 형식일 가능성이 큼, 컬럼 값은 읽지 않아 형식 미확인
- 평문을 넣지 말고 프로젝트의 암호화 방식으로 target 계정 비밀번호의 암호문을 생성하여 입력
- 암호문 생성 방법은 확인 필요

### 4.5 진행 순서
1. 관리자가 source 에서 vplus40 사용자의 기본 스키마 확인
   ```sql
   USE ERP;
   SELECT name, default_schema_name FROM sys.database_principals WHERE name = 'vplus40';
   ```
2. 결과가 VPLUS40 이면 관리자가 target 에 vplus40 로그인을 만들어 DB 사용자 vplus40 에 연결하고 기본 스키마를 VPLUS40 으로 맞춤, 권한은 source 와 동일하게 부여 (구체 SQL 은 4.6)
3. 4.3 방식 1 UPDATE 와 비밀번호 교체
4. erp-h 재기동 후 업무 화면 확인
- B안(백업/복원)을 수행하면 HMP.datasource 와 DB 사용자 매핑이 원본 기준으로 다시 덮어써지므로, B안을 할 계획이면 복원 후에 2~3번을 수행

### 4.6 서버 관리자 가이드: 업무 DB 계정 vplus40 구성 (방안 1)

#### 4.6.1 배경
- 앱이 업무 DB(MAIN) 연결에 계정 vplus40 을 사용하나, target 에는 로그인 vplus40 이 없어 접속이 거부됨 (2.8)
- target 의 DB 사용자 vplus40 은 복제 시 VPLUS40 스키마의 소유자 용도로 로그인 없이 만들어졌으며 기본 스키마가 dbo
- 앱 코드에는 스키마 없이 쓴 VPLUS40 테이블 참조 1,320건이 있어 업무 계정의 기본 스키마가 VPLUS40 이어야 할 가능성이 큼 (가설, 1단계에서 source 로 확인)
- 업무 연결이 HMP 스키마의 테이블(예: HMP.user_role)도 조회하므로 HMP 스키마 읽기 권한이 필요함

#### 4.6.2 목표 상태
- target 서버에 로그인 vplus40 이 있고 DB ERPH202611A 의 사용자 vplus40 에 연결됨
- 기본 스키마는 source 의 vplus40 과 동일 (VPLUS40 으로 추정), 권한도 source 와 동일
- 로그인 비밀번호는 source 의 vplus40 비밀번호와 동일하게 설정하면 HMP.datasource 의 db_password 를 바꿀 필요가 없음
  - 앱이 보관한 비밀번호는 암호문이라 개발 측이 평문을 알 수 없으므로 비밀번호는 관리자가 source 와 동일하게 설정하는 방식을 권장
  - 다른 비밀번호를 쓰려면 개발 측이 새 암호문을 만들어 HMP.datasource 에 반영해야 함 (4.4)

#### 4.6.3 사전 조건
- erp-h 종료 (연결 풀에 남은 실패 상태 제거)
- target 서버 sysadmin 권한과 source 서버 조회 권한 필요
- 이 작업은 로그인과 DB 사용자 설정만 바꾸며 데이터는 변경하지 않음

#### 4.6.4 1단계: source 에서 vplus40 구성 조회 (source 는 읽기만 수행)
```sql
USE ERP;

-- 기본 스키마 (기대: VPLUS40 로 추정)
SELECT name, type_desc, default_schema_name
FROM sys.database_principals WHERE name = 'vplus40';

-- 소유한 스키마
SELECT s.name AS owned_schema
FROM sys.schemas s JOIN sys.database_principals p ON p.principal_id = s.principal_id
WHERE p.name = 'vplus40';

-- 소속 역할
SELECT r.name AS role_name
FROM sys.database_role_members m
JOIN sys.database_principals r ON r.principal_id = m.role_principal_id
JOIN sys.database_principals u ON u.principal_id = m.member_principal_id
WHERE u.name = 'vplus40';

-- 개별 권한 (스키마·개체 단위)
SELECT p.state_desc, p.permission_name, p.class_desc,
       CASE p.class WHEN 3 THEN SCHEMA_NAME(p.major_id) WHEN 1 THEN OBJECT_NAME(p.major_id) ELSE NULL END AS target
FROM sys.database_permissions p
JOIN sys.database_principals u ON u.principal_id = p.grantee_principal_id
WHERE u.name = 'vplus40';
```
- 결과 4개를 개발 측에 회신 (이후 단계의 기본 스키마와 권한을 이 결과에 맞춤)

#### 4.6.5 2단계: target 에서 로그인 생성 및 사용자 연결
2-a. 서버 로그인 생성 (sysadmin)
```sql
USE master;
CREATE LOGIN [vplus40] WITH PASSWORD = '<source 의 vplus40 비밀번호와 동일>', CHECK_POLICY = OFF;
```
- 로그인 이름이 이미 있으면 CREATE LOGIN 이 실패하므로 기존 로그인을 확인하고 이 단계는 생략

2-b. DB 사용자 재생성 (ERPH202611A 의 db_owner 또는 sysadmin)
- 정정: 최초 가이드의 ALTER USER [vplus40] WITH LOGIN = [vplus40] 는 사용할 수 없음
  - 실제 실행 시 오류 33016 (사용자를 로그인에 다시 매핑할 수 없습니다. Windows 또는 SQL 로그인에 매핑된 사용자만 다시 매핑할 수 있습니다) 발생을 2026-11-25 10:25 에 확인함
  - target 의 vplus40 은 로그인 없이(WITHOUT LOGIN) 생성된 사용자라서 WITH LOGIN 으로 연결할 수 없고, 사용자를 삭제 후 로그인 기반으로 다시 생성해야 함
- 현황 조사 결과 (2026-11-25, 읽기 전용): vplus40 은 스키마 VPLUS40 만 소유하며 소유 개체, 역할 소속, 개별 권한은 없고 CONNECT 권한만 있음. 따라서 아래 순서로 안전하게 재생성 가능
```sql
USE [ERPH202611A];
BEGIN TRY
  BEGIN TRAN;
    ALTER AUTHORIZATION ON SCHEMA::[VPLUS40] TO [dbo];                          -- 스키마 소유를 잠시 dbo 로 (사용자 삭제 위해)
    DROP USER [vplus40];
    CREATE USER [vplus40] FOR LOGIN [vplus40] WITH DEFAULT_SCHEMA = [VPLUS40];  -- 1단계 결과가 다르면 그 값으로
    ALTER AUTHORIZATION ON SCHEMA::[VPLUS40] TO [vplus40];                      -- 스키마 소유를 다시 vplus40 으로
  COMMIT;
END TRY
BEGIN CATCH
  IF @@TRANCOUNT > 0 ROLLBACK;
  THROW;
END CATCH;
```
- 트랜잭션으로 묶여 있어 로그인 vplus40 이 없는 등으로 CREATE USER 가 실패하면 전체가 롤백되고 원래 상태(로그인 없는 사용자, 스키마 소유 vplus40)가 유지됨
- 이 SQL 은 작성만 되었고 실행 검증은 하지 않음 (DDL 구문의 문법은 SQL Server 규칙에 따름)

#### 4.6.6 3단계: 권한 부여
- 1단계에서 조회한 역할과 권한을 target 의 사용자 vplus40 에 동일하게 부여
```sql
USE [ERPH202611A];
-- 예: 소속 역할 (1단계 결과의 역할 이름으로 교체)
-- ALTER ROLE [<role_name>] ADD MEMBER [vplus40];

-- 예: HMP 스키마 읽기 (source 에서도 동일 권한이 있었는지 1단계 결과로 확인 후 부여)
-- GRANT SELECT ON SCHEMA::HMP TO [vplus40];
```
- VPLUS40 스키마는 vplus40 이 소유자이므로 별도 부여가 필요하지 않을 가능성이 높음 (1단계의 owned_schema 로 확인)
- 필요 이상의 권한(db_owner 등)은 1단계 결과에 있는 경우에만 부여

#### 4.6.7 4단계: 검증 (vplus40 로 접속하여 실행, sa 세션 불가)
```sql
SELECT USER_NAME()                          AS db_user,         -- 기대: vplus40
       SCHEMA_NAME()                        AS default_schema,  -- 기대: VPLUS40 (1단계 결과와 동일)
       OBJECT_ID('TCSB_ACP_DTLS')           AS vplus_tbl,       -- 기대: NULL 아님 (스키마 없이 해석됨)
       OBJECT_ID('HMP.user_role')           AS hmp_tbl,         -- 기대: NULL 아님
       (SELECT COUNT(*) FROM HMP.user_role) AS hmp_read;        -- 기대: 오류 없이 조회됨
```
- 통과 후 erp-h 를 재기동하여 업무 요청(예: /cse/getGrd_JcAnncBrdLst)에서 로그인 실패가 사라지는지 확인

#### 4.6.8 원복
```sql
-- 최초 상태(로그인 없는 사용자, 기본 스키마 dbo)로 되돌리기
USE [ERPH202611A];
ALTER AUTHORIZATION ON SCHEMA::[VPLUS40] TO [dbo];
DROP USER [vplus40];
CREATE USER [vplus40] WITHOUT LOGIN WITH DEFAULT_SCHEMA = [dbo];
ALTER AUTHORIZATION ON SCHEMA::[VPLUS40] TO [vplus40];

USE master;
DROP LOGIN [vplus40];   -- 서버 로그인도 제거할 경우
```
- 로그인만 지우면 DB 사용자가 존재하지 않는 로그인에 매핑된 상태로 남아 원래의 로그인 없는 사용자와 다르므로, 위처럼 사용자를 다시 만드는 방식으로 원복

#### 4.6.9 주의사항
- B안(백업/복원)을 수행하면 DB 가 교체되어 사용자 매핑이 원본 기준으로 되돌아감 (로그인은 서버 객체라 유지됨)
  - 복원된 DB 의 vplus40 은 source 의 로그인에 매핑된 사용자라서, target 로그인과 SID 가 달라 연결이 끊긴(orphan) 상태가 됨
  - 이 경우에는 ALTER USER [vplus40] WITH LOGIN = [vplus40] 이 사용 가능하며 2-b 의 재생성은 필요하지 않음
- 앞서 수행한 hmp202611A 의 기본 스키마 보정(3장)도 B안 복원 후 다시 확인

#### 4.6.10 관리자 회신 요청 항목
- 1단계 쿼리 4개의 결과
- 3단계에서 실제 부여한 역할·권한 목록
- 4단계 검증 쿼리 결과

---

## 5. 후속 작업 체크리스트

| 순번 | 작업 | 담당 | 상태·비고 |
|---|---|---|---|
| 1 | 3장 가이드 수행 (기본 스키마 보정) | 서버 관리자 | 완료, hmp202611A 세션 검증 통과 (2.6) |
| 2 | erp-h 재기동으로 Flowable 오류 해소 확인 | 개발 | 해소로 판단 (재기동 후 업무 요청 처리 단계까지 진행, 2.10 로그에 Flowable 오류 없음) |
| 3 | 시퀀스 8개 보정 | 개발 | 완료, 재조회로 확인 (2.9) |
| 4 | HMP.datasource MAIN/local 행의 db_url 변경 | 개발 | 완료, 재조회로 확인 (2.2) |
| 5 | source 에서 vplus40 구성 조회 (기본 스키마·소유 스키마·역할·권한) | 서버 관리자 | 미수행, 4.6.4 |
| 6 | target 에 로그인 vplus40 생성 및 사용자 재생성, 기본 스키마·권한 구성 | 서버 관리자 | 로그인 생성과 기본 스키마 변경은 진행된 것으로 보이나 사용자 재생성(4.6.5 의 2-b)이 미완료, 권한(4.6.6) 미수행 |
| 7 | vplus40 세션 검증 후 erp-h 재기동, 업무 요청의 로그인 실패 해소 확인 | 서버 관리자·개발 | 미수행, 4.6.7 |
| 8 | 비밀번호 방침 결정 (source 와 동일하게 로그인 생성 시 db_password 변경 불필요) | 개발·서버 관리자 | 동일 비밀번호 방식 권장 (4.6.2) |
| 9 | 백업/복원(B안) 재복제 | 서버 관리자 | 암호화 모듈 116개 복구, 수행하면 3장·4장 조치를 복원 후에 다시 확인 (4.6.9) |
| 10 | 재기동 후 주요 화면 확인 | 개발 | 대시보드·예산 화면 우선 (2.3 목록) |
| 11 | DEFAULT 30건 영향 코드 확인 | 개발 | ins_datetime, ETC_AMT, TSOC_JOINT_INFO |
| 12 | 로컬 Redis 기동 여부 확인 (선택) | 개발 | 공동 편집 경고, 2.10 |

## 6. 점검 한계
- 정적 분석 기반이며 화면을 실제로 열어 확인하지 않음
- 호출 mapper 가 모든 화면에서 사용되는지는 구분하지 않음
- target 에는 읽기 전용 조회만 수행하였고, 변경 시도(ALTER USER)는 거부되어 반영된 것이 없음 (기본 스키마 보정은 서버 관리자가 별도로 적용)
- source 에는 hmp, dbo 사용자의 기본 스키마와 hmp/vplus40 사용자 존재 여부를 읽기 전용으로 조회함 (vplus40 은 조회되지 않음)
- vplus40 의 source 기본 스키마와 1,320건 참조의 동작 원인은 가설이며 4.5 의 1번 결과로 확정 필요
- HMP.datasource 의 db_url 변경은 사용자가 직접 수행하였고(변경 SQL 은 작성만 함), db_username·db_password 는 변경되지 않음
- 시퀀스 대조는 이름 규칙에 맞는 370개만 대상이며 나머지 20개는 미확인
- 시퀀스 보정 스크립트는 사용자가 적용하였고, 적용 후 확인은 4개 테이블의 최대 키와 직접 비교하고 나머지 4개는 시퀀스 값이 이전 최대값보다 커진 것만 확인함
- 4.6 의 로그인 생성·권한 SQL 은 작성만 하였고 실행·검증하지 않았으며, 기본 스키마(VPLUS40)와 필요 권한은 4.6.4 의 source 조회 결과로 확정 필요
- 4.6.5 의 최초 가이드(ALTER USER ... WITH LOGIN)는 로그인 없는 사용자에는 적용할 수 없는 오류였고 2-b 로 정정함, 정정한 재생성 SQL 도 실행 전임
- vplus40 로그인 실패의 구체 사유(로그인 없음/비밀번호 불일치)는 서버 오류 로그 확인 전까지 미확정
- Redis 경고가 전환 전에도 있었는지는 확인하지 못함
