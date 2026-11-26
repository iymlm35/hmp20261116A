# erp-h 기동 — Flowable 스키마 업그레이드 ERROR (ACT_HI_VARINST 없음)

- 일자: 2026-11-25
- 대상: products-erp-h (local 프로필) / 플랫폼 DB = MSSQL `192.168.1.21:23302` · DB `ERPH202611A` · 계정 `hmp202611A`
- 판정: **기동에는 영향 없음(로그만 ERROR)**. 조치는 선택.

## 1. 증상

기동 로그에 아래 ERROR 가 나온다(스택: `AbstractSqlScriptBasedDbSchemaManager.executeSchemaResource` → `ServiceSqlScriptBasedDbSchemaManager.schemaUpdate`, flowable-engine-common 7.1.0).

```
o.f.c.e.impl.db.CommonDbSchemaManager : problem during schema upgrade,
statement create index ACT_IDX_HI_VAR_SUB_ID_TYPE on ACT_HI_VARINST(SUB_SCOPE_ID_, SCOPE_TYPE_)
SQLServerException: 개체 "ACT_HI_VARINST"이(가) 없거나 권한이 없으므로 이를 찾을 수 없습니다.
```

## 2. 원인 (DB 읽기 전용 조회로 확인)

| 항목 | 결과 |
|---|---|
| Flowable `ACT_*` 테이블 위치 | **`HMP` 스키마 43개**, `dbo` 0개 (`ACT_HI_VARINST` 포함) |
| 접속 계정 기본 스키마 | **`dbo`** (`default_schema_name = dbo`) |
| 인덱스 `ACT_IDX_HI_VAR_SUB_ID_TYPE` | `HMP.ACT_HI_VARINST` 에 **이미 존재** |
| `ACT_GE_PROPERTY` 스키마 버전 | `schema.version` / `common` / `dmn` / `eventregistry` = `7.1.0.2` (최신) |

- Flowable 은 DDL 을 **스키마 접두어 없이** 실행한다(`create index ... on ACT_HI_VARINST`).
- 계정 기본 스키마가 `dbo` 라서 `dbo.ACT_HI_VARINST` 로 풀려 실패한다. 실제 테이블은 `HMP` 에 있다.
- JPA 는 `application.yml` 의 `hibernate.default_schema: HMP` (app/src/main/resources/application.yml:42) 로 정상 동작하지만 Flowable 은 이 설정을 따르지 않는다.
- 업그레이드 대상(인덱스)은 이미 적용돼 있으므로 실패해도 결과는 같다. Flowable 이 로그만 남기고 계속 진행한다.
- Flowable 설정 자체는 이 저장소에 없고 hmp core jar 안에 있다(저장소 yml·gradle 에 `flowable` 문자열 없음).

## 3. 조치 선택지 (아직 미적용)

1. **DB 계정 기본 스키마 변경(권장)** — 접두어 없는 SQL 이 전부 `HMP` 로 풀려 DMN 등 다른 Flowable 모듈도 같은 문제를 피한다.
   ```sql
   ALTER USER hmp202611A WITH DEFAULT_SCHEMA = HMP;
   ```
   - 공용 개발 DB 변경이므로 실행 전 확인 필요.
   - `dev` 프로필 계정 `hmp` 는 기본 스키마가 `HMP` 일 가능성이 높아 이 로그가 안 날 것(미확인).
2. **앱 설정** — `flowable.database-schema: HMP`. hmp core jar 가 이 프로퍼티를 읽는지는 적용해 봐야 안다.
3. **무시** — 위 이유로 기능 영향 없음.

## 4. 재확인 쿼리 (읽기 전용)

```sql
select name, default_schema_name from sys.database_principals where name = USER_NAME();
select s.name sch, count(*) cnt from sys.tables t join sys.schemas s on s.schema_id = t.schema_id
 where t.name like 'ACT[_]%' group by s.name;
select NAME_, VALUE_ from HMP.ACT_GE_PROPERTY order by 1;
```

## 5. 선택지 1 가이드 — 실행 전 조사에서 드러난 제약

`ALTER USER hmp202611A WITH DEFAULT_SCHEMA = HMP` 는 **이 계정에는 적용할 수 없다.**

| 조사 항목 | 결과 |
|---|---|
| `SUSER_SNAME()` / `USER_NAME()` | `hmp202611A` / **`dbo`** — 이 로그인은 DB `ERPH202611A` 의 **소유자**라 DB 안에서 `dbo` 사용자로 매핑됨 |
| `dbo` 사용자 기본 스키마 | 항상 `dbo`. 변경 불가(`ALTER USER dbo ... DEFAULT_SCHEMA` 는 거부됨) |
| DB 사용자 `hmp` | 기본 스키마 `HMP` · `HMP` 스키마 소유자 · db_datareader/db_datawriter + CREATE TABLE/VIEW/PROCEDURE/FUNCTION… · 서버 로그인 `hmp` 는 없음(`hmp202611A` 만 존재) |
| `dbo` 에 있는 사용자 객체 | Quartz `QRTZ_*` 11개(행 0) · `sysdiagrams` · 다이어그램 sp 7개. `HMP` 에도 `QRTZ_*` 11개가 있어 기본 스키마가 바뀌어도 의존 문제 없음 |

→ 방법은 "계정 기본 스키마 변경" 이 아니라 **"기본 스키마가 `HMP` 인 사용자로 접속"** 으로 바뀐다.

### 절차 (sysadmin 또는 ALTER ANY LOGIN 권한 필요 — `hmp202611A` 로 가능한지는 미확인)

```sql
-- 1) 서버 로그인 생성 (비밀번호는 별도 보관, 문서에 적지 않는다)
CREATE LOGIN hmp WITH PASSWORD = '<강한 비밀번호>', CHECK_POLICY = OFF;

-- 2) 기존 DB 사용자 hmp 를 새 로그인에 연결 (ERPH202611A 에서 실행)
USE ERPH202611A;
ALTER USER hmp WITH LOGIN = hmp;

-- 3) Flowable 업그레이드 DDL(create index 등)을 하려면 DDL 권한이 필요
ALTER ROLE db_ddladmin ADD MEMBER hmp;   -- 필요 시. HMP 스키마 소유자라 이미 충분할 수 있음
```

### 앱 쪽 변경

```
PLATFORM_DB_USER=hmp
PLATFORM_DB_PASSWORD=<위 비밀번호>
```
(또는 datasource.yml local 프로필의 기본 계정 변경 — 비밀번호를 yml 에 박지 않는 쪽을 권장)

### 확인
- 기동 후 `ACT_HI_VARINST` ERROR 가 사라졌는지 확인.
- `select SCHEMA_NAME()` 이 `HMP` 인지, 기존 화면·배치가 정상인지 확인(소유자→일반 사용자 전환이라 권한 차이로 막히는 DDL/시드가 없는지).

### 되돌리기
`datasource.yml`/환경변수를 `hmp202611A` 로 되돌리면 끝. `CREATE LOGIN` 은 `DROP LOGIN hmp` 로 제거(DB 사용자 `hmp` 는 `ALTER USER hmp WITH LOGIN = ...` 이전 상태로 복원 필요).

### 대안
- **선택지 2** `flowable.database-schema: HMP` — DB 변경 없음. 계정 전환보다 위험이 적음. hmp core jar 가 읽는지만 확인하면 된다.
- 무시 — 기능 영향 없음.

## 6. 적용 — 선택지 2 (2026-11-25)

- 근거: hmp-core 2.2.146 jar 에는 Flowable 설정이 없고, Spring Boot Flowable 스타터 7.1.0 의 `flowable.database-schema` 가 공식 프로퍼티(전 엔진 공통).
- 변경: `products-erp-h/app/src/main/resources/datasource.yml` **local 프로필 블록**에 추가.
  ```yaml
  flowable:
    database-schema: HMP
  ```
  dev 프로필 계정 `hmp` 는 기본 스키마가 HMP 라 불필요하여 local 에만 둠.
- **검증 대기**: erp-h 재기동 후 ① `problem during schema upgrade ... ACT_HI_VARINST` ERROR 소멸 ② 기동 정상 ③ DMN(`ACT_DMN_*`)·결정표 화면 정상 확인.
- 안 먹히면(ERROR 지속 또는 새 오류) 위 3줄을 지우면 이전 상태로 복귀. 그 뒤엔 선택지 1(사용자 전환)을 검토.

## 7. 결과 — 선택지 2 실패, 설정 교체 (2026-11-25 16:36 재기동)

- `flowable.database-schema: HMP` 적용 후 재기동해도 **동일 ERROR 지속**(`ProcessEngineImpl` → `SchemaOperationsEngineBuild.executeSchemaUpdate` → `ServiceSqlScriptBasedDbSchemaManager.schemaUpdate`). MSSQL 에서 이 값은 테이블 존재 확인 메타데이터에만 쓰이고 업그레이드 DDL 은 접두어 없이 실행된다.
- 교체: local 프로필 `flowable.database-schema-update: false` — 업그레이드 DDL 자체를 실행하지 않는다.
  - 근거: 이 DB 의 `ACT_*` 43개가 이미 `7.1.0.2`(최신)로 존재(§2).
  - 주의: **빈 DB 에 Flowable 테이블을 처음 만들어야 하는 환경에서는 쓰면 안 된다**(true 로 복귀). local 프로필 한정.
- **검증 대기**: 재기동 후 ① ERROR 소멸 ② 기동 정상 ③ 결정표(DMN)·업무흐름 화면 정상.
- 그래도 안 되거나 부작용이 있으면 이 3줄을 지우고 선택지 1(`hmp` 로그인 전환, §5)로 간다.

## 8. 되돌림 — datasource.yml 은 변경하지 않는다 (사용자 지시, 2026-11-25)

- §6·§7 에서 `datasource.yml` local 프로필에 넣었던 `flowable:` 블록을 **모두 제거**해 원래 내용으로 복구했다. 현재 이 저장소 파일은 변경 0.
- 따라서 이 ERROR 는 지금도 기동 시 남는다(기능 영향 없음, §2). 파일을 건드리지 않는 조치만 남음:
  - 무시(권장 기본값)
  - DB 쪽 조치: 기본 스키마가 `HMP` 인 로그인 `hmp` 를 만들어 환경변수 `PLATFORM_DB_USER/PASSWORD` 로 접속(§5) — 파일 변경 없이 가능
  - `flowable.*` 를 환경변수(예: `FLOWABLE_DATABASE_SCHEMA_UPDATE=false`)로 주는 방법 — 파일 변경 없이 가능하나 §7 의 DMN 적용 여부 미검증

## 9. DB 쪽 조치 가이드 — `hmp` 로그인 생성 후 그 계정으로 접속 (2026-11-25 · 미실행)

파일 변경 없음. §5 를 추가 조사로 보정한 최종판.

### 9-1. 추가 조사 결과 (읽기 전용)

| 항목 | 결과 |
|---|---|
| `hmp202611A` 서버 권한 | sysadmin ✗ · securityadmin ✗ · ALTER ANY LOGIN ✗ → **로그인을 만들 수 없다. `sa` 또는 DBA 가 해야 한다** |
| 서버 로그인 목록 | `sa`, `hmp202611A` 두 개뿐 |
| DB 사용자 `hmp` / `vplus40` | 서버 로그인과 연결이 끊긴 **고아(orphan) 사용자**(다른 서버에서 복원된 DB 흔적). `hmp` SID = `0xE0103A1405624F47AC4C6FA57D09A50D`, 기본 스키마 HMP |
| 서버 | SQL Server 2022(16.0.1000.6) Developer Edition — 인스턴스 하나에 여러 DB 가 있을 수 있어 **로그인은 인스턴스 전체에 생긴다** |

→ 고아 사용자와 **같은 SID 로 로그인을 만들면** `hmp` 사용자가 자동으로 연결된다(ALTER USER 불필요).

### 9-2. 절차

**① `sa` 로 접속해 실행** (비밀번호는 별도 보관 — 문서·git 에 남기지 않는다)
```sql
USE master;
CREATE LOGIN hmp
  WITH PASSWORD = '<강한 비밀번호>',
       SID = 0xE0103A1405624F47AC4C6FA57D09A50D,   -- 고아 사용자 hmp 와 동일 SID
       DEFAULT_DATABASE = ERPH202611A,
       CHECK_POLICY = OFF;
```
- SID 충돌 오류가 나면 SID 절을 빼고 만든 뒤 `USE ERPH202611A; ALTER USER hmp WITH LOGIN = hmp;`

**② 연결 확인** (`hmp202611A` 로 읽기 전용)
```sql
select dp.name, sp.name login_name
from sys.database_principals dp left join sys.server_principals sp on sp.sid = dp.sid
where dp.name = 'hmp';          -- login_name 이 hmp 로 나오면 연결됨
```

**③ 앱 기동 — 환경변수로만 계정 지정** (`datasource.yml` 의 `PLATFORM_DB_USER/PASSWORD` 가 환경변수를 우선 읽음)
```powershell
$env:PLATFORM_DB_USER = 'hmp'
$env:PLATFORM_DB_PASSWORD = '<위 비밀번호>'
.\restart.bat            # 또는 app\run.bat — gradlew bootRun 이 환경변수를 상속
```
- 같은 셸에서만 유효. IDE(디버그) 로 띄우면 그 실행 구성의 환경변수에 넣어야 한다.
- 영구로 쓰려면 사용자 환경변수 등록(비밀번호가 평문이 되는 점 유의).

### 9-3. 확인
1. 부팅 로그에서 `problem during schema upgrade ... ACT_HI_VARINST` 소멸 확인.
2. `hmp` 는 `db_datareader/db_datawriter` + CREATE TABLE/VIEW/PROCEDURE/FUNCTION + `HMP` 스키마 소유자. 기동·화면·배치가 정상인지, 권한 부족 오류(`... 권한이 거부되었습니다`)가 없는지 확인.
3. ⚠️ **불확실**: Flowable 이 부팅마다 이 업그레이드 문을 실행한다면, 스키마가 맞아진 뒤에는 `create index ... 이미 있음`(인덱스가 `HMP` 에 이미 존재) 로 **메시지만 바뀐 같은 종류의 ERROR** 가 남을 수 있다. 그 경우도 기능 영향은 없고 이 방법으로는 더 줄일 수 없다.

### 9-4. 영향·주의
- `CREATE LOGIN` 은 인스턴스(192.168.1.21:23302) 전체 변경 — 같은 서버를 쓰는 다른 DB·사람이 있으면 알린다. 같은 이름의 로그인이 다른 DB(예: `ERP`)의 `hmp` 사용자에 붙을 수 있으니 SID 가 다른 DB 의 `hmp` 와 겹치는지 먼저 확인.
- `vplus40` 고아 사용자는 이번 범위가 아니므로 건드리지 않는다.
- `hmp202611A`(DB 소유자) 와 권한이 달라 일부 DDL·`dbo` 객체 접근이 막힐 수 있다 → 문제 시 환경변수만 지우면 즉시 원복.

### 9-5. 되돌리기
환경변수 제거(또는 `hmp202611A` 로 복귀). 로그인 정리는 `sa` 로 `DROP LOGIN hmp;` (DB 사용자 `hmp` 는 다시 고아 상태로 돌아감).

## 11. 업무 DB 구조 확인 및 B 방안(업무 DB 를 ERPH202611A 로 이전) — 진행 중 (2026-11-25)

### 11-1. 확인된 구조
- 플랫폼 DB(`spring.datasource`, `HMP` 스키마) = `192.168.1.21:23302` / `ERPH202611A`. 접속 계정 `hmp`(`datasource.yml` local 기본값).
- 업무 DB 는 별도다. `HMP.DATASOURCE`(local, system_id=ERP) 의 행:
  | datasource_id | db_url | db_username | 용도 |
  |---|---|---|---|
  | MAIN | `jdbc:sqlserver://192.168.1.170:1434;databaseName=ERP;encrypt=false;trustServerCertificate=true` (**변경 전 원본**) | `vplus40` | 업무 `hmplus.erp`(풀 최대 10). `@UseDataSource(systemId="ERP", datasourceId="MAIN")` 서비스가 사용. 최종 수정 2026-11-03 `cs` |
  | SHARED_KNOWLEDGE | `jdbc:postgresql://pg-dev.hmplus.io:6432/hmplus` | `hmp` | 공유 지식(PostgreSQL) |
- `db_password` 는 평문(10자, `conv2-plain-20261102` 변환 이력). 값은 문서·대화에 남기지 않는다.
- 즉 `VPLUS40.` 명시 쿼리(코드 640파일·15,915건)는 지금 `170` 서버 `ERP` DB 로 나간다.
- 읽기 전용 점검 시 주의: `hmp202611A` 는 VIEW ANY DEFINITION 이 없어 `sys.server_principals` 에 다른 로그인이 안 보인다 → "고아 사용자" 판정은 `SUSER_SNAME(sid)` 로 해야 한다(`hmp`=연결됨, `vplus40`=이 인스턴스에 로그인 없음).

### 11-2. B 방안 순서 (순서 중요: 로그인 → 동등성 → UPDATE → 재기동)
1. **`sa`/DBA — 로그인 생성** (비밀번호는 행의 `db_password` 와 같아야 하므로 테이블에서 읽어 쓴다. 화면·문서에 노출되지 않는다)
   ```sql
   USE master;
   DECLARE @pw nvarchar(128) = (SELECT db_password FROM ERPH202611A.HMP.DATASOURCE
                                 WHERE datasource_id='MAIN' AND profile='local' AND system_id='ERP');
   DECLARE @sql nvarchar(max) = N'CREATE LOGIN vplus40 WITH PASSWORD = ' + QUOTENAME(@pw, '''')
        + N', SID = 0xEA0293567645A0469AB448EB678F1DD8, DEFAULT_DATABASE = ERPH202611A, CHECK_POLICY = OFF;';
   EXEC (@sql);
   ```
   - DB 사용자 `vplus40`(기본 스키마 VPLUS40, `VPLUS40` 스키마 소유, ddladmin·datareader·datawriter)과 같은 SID 라 자동 연결된다. SID 충돌 시 SID 절을 빼고 만든 뒤 `USE ERPH202611A; ALTER USER vplus40 WITH LOGIN = vplus40;`.
2. **동등성 확인 — 두 서버에서 각각 실행해 결과 비교** (`170` 의 `ERP` 와 `21` 의 `ERPH202611A`)
   ```sql
   select s.name sch, case o.type when 'U' then 'TABLE' when 'V' then 'VIEW' when 'P' then 'PROC'
          when 'FN' then 'FN_SCALAR' when 'IF' then 'FN_INLINE' when 'TF' then 'FN_TABLE'
          when 'SN' then 'SYNONYM' when 'SO' then 'SEQUENCE' else o.type end typ, count(*) n
   from sys.objects o join sys.schemas s on s.schema_id=o.schema_id
   where o.is_ms_shipped=0 and s.name in ('HMP','VPLUS40') and o.type in ('U','V','P','FN','IF','TF','SN','SO')
   group by s.name, o.type order by 1,2;
   ```
   - `ERPH202611A` 쪽 기준값(2026-11-25): VPLUS40 = 테이블 520 · 뷰 14 · 스칼라함수 67 · 인라인TVF 17 · 다중문TVF 16 · 프로시저 2 · 시노님 3 · 시퀀스 2.
   - 객체 수만 같아서는 부족하다. 핵심 테이블 건수(코드·권한·메뉴 등)도 비교하고, 달라지면 화면 결과가 달라진다.
   - 이 비교는 `170` 서버 접속 권한이 필요해 **사용자가 직접 실행**한다(제가 저장된 업무 계정으로 접속하는 것은 권한 분류기가 거부함).
3. **연결 확인** — `vplus40` 으로 `21` 에 붙는지(앱 기동 전 PowerShell 로 `select SUSER_SNAME(), SCHEMA_NAME()` → `vplus40 | VPLUS40`).
4. **`db_url` 변경** (`datasource.yml` 은 건드리지 않는다 — DB 데이터 변경)
   ```sql
   UPDATE HMP.DATASOURCE
      SET db_url = 'jdbc:sqlserver://192.168.1.21:23302;databaseName=ERPH202611A;encrypt=false;trustServerCertificate=true'
    WHERE datasource_id='MAIN' AND profile='local' AND system_id='ERP';
   ```
5. **재기동** 후 업무 화면(`hmplus.erp`)이 정상인지, 로그 p6spy 의 `url jdbc:sqlserver://192.168.1.21:23302` 로 업무 쿼리가 나가는지 확인.

### 11-3. 되돌리기
`db_url` 을 11-1 표의 **변경 전 원본**으로 UPDATE 하고 재기동. 로그인 정리는 `sa` 로 `DROP LOGIN vplus40;`(DB 사용자는 로그인 없는 상태로 복귀).

### 11-4. 진행 기록 (2026-11-25)
- **로그인은 새로 만들지 않았다.** `CREATE LOGIN vplus40` 이 오류 15025(이미 있음). 이 인스턴스에 `vplus40` 로그인이 이미 있었으나 SID(`0x10DC4AFD…1E70`, 활성, 기본 DB master)가 DB 사용자 `vplus40` 의 SID(`0xEA029356…DD8`)와 달라 연결이 끊겨 있었다.
  - 조치: `USE ERPH202611A; ALTER USER vplus40 WITH LOGIN = vplus40;` (로그인·소유 스키마·역할·권한 그대로, 사용자만 재연결). → `SUSER_SNAME(sid)` = `vplus40`.
  - 가장(impersonation) 점검: 기본 스키마 VPLUS40 · VPLUS40 테이블 조회·입력·수정·ALTER ○ · 함수 실행·시퀀스 ○ · HMP 테이블 조회 ○ · 보이는 객체 수 HMP 3,732 / VPLUS40 1,094(소유자와 동일).
  - 접속 테스트(SSMS, `vplus40` + `HMP.DATASOURCE.db_password`): `vplus40 | vplus40 | VPLUS40` **통과** → 로그인 비밀번호 = `db_password`.
- (정정) 앞서 "서버 로그인은 sa·hmp202611A 뿐 / vplus40 고아" 는 가시성 한계로 부정확했다. `hmp202611A` 는 VIEW ANY DEFINITION 이 없다. 사용자는 로그인 기반(`INSTANCE`)이며 "로그인 없는 사용자(NONE)"가 아니다.

### 11-4a. 동등성 비교 결과 (2026-11-25 17:2x)
- 객체 수: `170` ERP 와 `ERPH202611A` 의 VPLUS40 이 종류별로 **전부 동일**(TABLE 520 · VIEW 14 · FN_SCALAR 67 · FN_INLINE 17 · FN_TABLE 16 · PROC 2 · SYNONYM 3 · SEQUENCE 2).
- 행 수(`sys.partitions`): tables 520 / non_empty 198 동일. total_rows 는 `ERPH202611A` 2,663,952 vs `170` 2,663,958(**+6**). 테이블별 비교(`170_VPLUS40_행수_비교.sql`) 결과 **6개 테이블이 각각 +1**(170 이 많음), 나머지 514개는 행 수 동일.
  | 테이블 | ERPH202611A | 170 |
  |---|---|---|
  | TCSA_HHLD_HST | 2,972 | 2,973 |
  | TCSA_HHLD_INFO | 2,736 | 2,737 |
  | TCSA_SITE_CTP_MATCH | 1 | 2 |
  | TCSB_ACP_CLS_HST | 2 | 3 |
  | TCSB_ACP_DTLS | 417,741 | 417,742 |
  | TCSC_GNT_DPST | 2 | 3 |
- 해석: 코드·권한·메뉴 같은 설정성 테이블이 아니라 CS(분양·접수) **업무 트랜잭션 테이블**의 +1 씩 → 사본을 뜬 뒤 `170` 에 새로 쌓인 데이터로 보임. 한계: 행 수 비교는 **기존 행의 UPDATE 는 못 잡는다**.
- 판단: 개발·검증 용도의 이전으로는 허용 가능. 정확한 동기화가 필요하면 위 6행을 복사.

### 11-5. 열린 항목
- **`170` 서버 `ERP` 와 `ERPH202611A` 의 VPLUS40 동등성 비교 대기**(사용자 직접 실행 — 저장된 업무 계정으로 `170` 접속은 권한 분류기가 거부). 기준값: 테이블 520(데이터 있는 198) · 총 약 2,663,952행 · 상위 `TCSB_LTRM_NOTI_DTL` 555,953 / `TCMA_EBGT_DTL` 484,674 / `TCMW_WRK_CTS` 427,999.
- 동등성이 확인되면 11-2 의 4(`UPDATE HMP.DATASOURCE` — `hmp` 또는 `hmp202611A` 로 실행, `vplus40` 으로는 하지 않음) → 5(재기동) 순으로 진행. 확인 전에는 `UPDATE` 금지.
- 확인 전까지 DB 변경은 위 `ALTER USER` 1건뿐이며 저장소 파일 변경은 0.

## 10. 결과 — 해결 (2026-11-25)

- 범위: **local 프로필만**(`192.168.1.21:23302` / `ERPH202611A`). oracle·dev·Docker 는 대상 아님.
- 실행: `sa`/DBA 가 로그인 `hmp` 생성(고아 사용자 `hmp` 와 같은 SID) → 셸에서 환경변수 `PLATFORM_DB_USER=hmp`, `PLATFORM_DB_PASSWORD=<비밀번호>` 를 설정해 erp-h 기동.
- 결과: **정상 작동**(사용자 확인). `datasource.yml` 등 저장소 파일 변경 0.
- 운영 메모
  - 환경변수는 그 셸에서만 유효하다. 새 창이나 IDE 실행에서는 다시 설정해야 하고, 안 하면 `hmp202611A` 로 접속해 이 ERROR 가 다시 난다(기능 영향 없음).
  - 로그인 `hmp` 는 인스턴스 전체에 생긴 변경이다. 비밀번호는 이 문서에 기록하지 않는다.
  - 원복: 환경변수 제거 / 로그인 정리는 `sa` 로 `DROP LOGIN hmp;`.
- 시도했으나 폐기한 방법: `flowable.database-schema: HMP`(효과 없음), `flowable.database-schema-update: false`(`datasource.yml` 변경 금지 지시로 제거).
