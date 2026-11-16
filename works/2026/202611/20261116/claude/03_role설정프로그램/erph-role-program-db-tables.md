# erp-h 권한 프로그램별 DB 테이블 요약 (백엔드 소스 기준)

> 작성 2026-11-16 · 대상: `erph-role-menu-tree-common-vs-system.md` 「권한」 폴더의 화면 9개
> 근거 소스: `D:\root\docs\DEV\vsc_workspaces\hmp202061112-hmpcoms\platform-hmp` (hmp-core) · `co_mobileAuthMapp` 은 erp-h 소스(`app/src/main/...`)
> 방법: 화면 JS(`core/src/main/resources/pages/role/*.js`)가 부르는 API → 컨트롤러 → 서비스 → 리포지토리/엔티티(`@Table`) 순으로 따라갔다. 소스를 읽어 정리한 것이고 **DB 를 조회하거나 실행해 확인하지 않았다.**
> 소스 버전: erp-h 는 hmp-core **2.2.109**(gitSha `9507f3514`)를 쓴다. platform-hmp HEAD(`58882d8b3`)는 그보다 4커밋 뒤이고, 이 문서가 본 권한 관련 파일(컨트롤러·서비스·엔티티·`pages/role`·`permission`)은 그 사이 변경이 없다.
> 이 문서의 표는 「공통 › 권한」과 「시스템 › 권한」(`sys-*` 별칭) 양쪽에 똑같이 적용된다. 두 탭은 같은 테이블·쿼리를 쓰고 **접근 판정만 serviceId 별로 따로** 한다(§1-1).
> 표 이름은 스키마 `HMP.` 아래 표다(엔티티 `@Table` 이름 · 모두 `system_id`·`tenant_id` 로 구분되는 표가 많다).

## 1. 프로그램 ↔ 소스 대응

| 화면(공통 탭) | serviceId | 프로그램/화면 파일 | 백엔드 |
|---|---|---|---|
| 권한 관리 | `ca-role` | `pages/role/role.js` | `RoleManageController` `/role-manage/*` → `RoleServiceImpl` |
| 공통권한 관리 | `ca-role-common` | `pages/role/role-common.js` | 〃 |
| 발령권한 관리 | `ca-role-rule` | `pages/role/role-rule.js` (+ `role-condition.js`) | 〃 (조건 API) · 발령 자동 부여 `RoleRuleServiceImpl` |
| 권한/사용자매핑 | `ca-user-role` | `pages/role/user-role.js` — `params mode=byRole` | 〃 |
| 사용자/권한매핑 | `ca-user-role-by-user` | `pages/role/user-role.js` — `params mode=byUser` | 〃 |
| 지정권한 관리 | `ca-role-assign` | `pages/role/role-assign.js` | 〃 |
| 권한위임 관리 | `ca-role-delegation` | `pages/role/role-delegation.js` | `RoleDelegationController` `/role-delegation/*` → `RoleDelegationServiceImpl` |
| 사용자권한 조회 | `ca-user-role-inquiry` | `pages/role/user-role-inquiry.js` | `RoleManageController` + `RoleServiceController`·`ServiceController` |
| 사용자(모바일) | `co_mobileAuthMapp` | erp-h `pages/co/mobileAuthMapp.js` | erp-h `MobileAuthMappController` `/auth/*` → `MobileAuthMappService` + mapper |

- **`ca-user-role` 과 `ca-user-role-by-user` 는 같은 화면 소스(`user-role`)** 이고 시작 방향만 다르다(11-12 업그레이드 `user-role-two-directions`가 같은 program 에 `mode` 를 `byRole`/`byUser` 로 나눔). 메뉴 이름은 「권한/사용자매핑」=권한에서 시작, 「사용자/권한매핑」=사용자에서 시작이다. 이전 안내에서 용도를 확정하지 못했던 부분이 이것으로 정리된다.
- 시스템 탭의 `sys-*` 화면은 같은 프로그램을 쓰므로 표도 같다. 근거와 차이는 아래 1-1 에 정리했다.
- 프로그램 ID 는 화면 파일 이름(`role`, `role-common` …)과 같다고 보았다(`user-role` 은 시드 SQL 로 확인, 나머지는 파일명 기준).

### 1-1. 공통 탭 ↔ 시스템 탭: 같은 테이블·같은 쿼리, 다른 접근 판정

「권한」 폴더는 공통 탭과 시스템 탭에 각각 9개 화면이 있다(`ca-role` ↔ `sys-ca-role` …). 이 문서 아래의 모든 표는 **두 탭에 똑같이 적용된다.**

**같은 점 (소스로 확인)**
- `sys-*` 는 별칭 행이다. hmp 업그레이드 SQL(`2026-11-11-menu-system-full-common-subset-*`)이 `sys-<원ID>` 를 만들면서 **같은 `program_id` 를 복사**하고 `params` 에 `"sharedProgram":true` 를 더한다. erp-h 전용 화면(`co_mobileAuthMapp` ↔ `sys-co_mobileAuthMapp`)도 `P5-menu-system-full.sql` 이 같은 방식으로 만든다.
- 화면 JS 가 하나이므로 부르는 API(`/role-manage/*` · `/role-delegation/*` …)도 같고, 컨트롤러 → 서비스 → 엔티티(표)도 같다.
- 백엔드(`RoleManageController` · `RoleServiceImpl` · `RoleDelegationServiceImpl` · `permission` · `auth`)에 `sys-` 접두어나 serviceId 로 쿼리를 바꾸는 코드는 없다. 두 탭 모두 같은 시스템(ERP)이라 `system_id` 조건도 같다.
- 결과적으로 공통 탭에서 만든 역할·부여는 시스템 탭에서 그대로 보인다. 데이터가 따로 있지 않다.

**다른 점: 쿼리가 아니라 「실행해도 되는가」의 판정**
1. 화면이 API 를 부를 때 자기 serviceId 를 요청 헤더 `X-Service-Id` 에 싣는다(공통 탭 `ca-role` · 시스템 탭 `sys-ca-role`).
2. 서버(`RoleManageController.hasRoleManagerLevel`)가 그 serviceId 가 이 프로그램군 화면인지(`ROLE_MANAGE_PROGRAMS` 의 program_id) 확인한다.
3. 로그인 계정의 role 들이 **그 serviceId 에 받은 등급**(`role_service.auth_level`)의 최댓값을 구해 필요한 등급(저장 2 · 삭제 3)과 비교한다.
4. 통과하면 같은 서비스 메서드·같은 쿼리를 실행하고, 못 하면 쿼리를 실행하지 않고 거부한다. 플랫폼 관리자·시스템 관리자는 이 판정을 건너뛴다.

그래서 부여는 serviceId 단위로 따로 필요하다. 예를 들어 어떤 계정이 `ca-role` 에만 등급 3 을 받았다면 공통 탭 화면에서는 저장·삭제가 되고, 같은 계정이 시스템 탭 `sys-ca-role` 에서 호출하면 거부될 수 있다. (이 판정은 이 문서 §3 의 `AL0` 표시 API 에는 적용되지 않고, 그 API 들은 메서드 안의 역할 관리자 판정을 쓴다 — §6-6.)

**쿼리에 들어가는 파라미터는 탭이 아니라 이런 값으로 바뀐다**
- 로그인 계정 정보: 법인 가시범위 · 시스템 ID · 사용자 조건. 탭과 무관하다.
- 화면 `service.params`: `mode`(`byRole`/`byUser`), `systemScope`(플랫폼 관리자가 전체 시스템을 고를 때 `ALL`). 접두어가 아니라 서비스 행에 적힌 값이다. 별칭이 원본의 `params` 를 그대로 가지는지는 확인하지 못했다(§7).

| 구분 | 공통 탭 화면 | 시스템 탭 화면 |
|---|---|---|
| 테이블 · 쿼리 | 같음 | 같음 |
| 화면 소스(JS) | 같음 | 같음 |
| 허용 판정 기준 | `ca-*` 에 받은 부여 | `sys-ca-*` 에 받은 부여 |
| 화면 `params` | 원본 값 | 원본 값 + `sharedProgram:true` (그 밖은 확인 못 함) |

참고: `role-delegation.js` 에 serviceId 가 `common-` 으로 시작하는지 보는 코드가 있으나, 이는 개인 메뉴 「권한위임」과 관리 화면을 가르는 것이고 공통·시스템 탭 구분과 무관하다.

## 2. 프로그램별 요약

표기: **R** 읽기 · **W** 쓰기(INSERT/UPDATE/DELETE) · 괄호는 간접 접근.

| 프로그램 | 핵심 테이블 (쓰기) | 읽는 테이블 | 비고 |
|---|---|---|---|
| **ca-role** 권한 관리 | `role` · `role_i18n` · `log_role` | `user_role`(역할별 사용자) · `biz_group` · `system_language` | 역할 삭제 시 JPA 연쇄 삭제 — §4 |
| **ca-role-common** 공통권한 관리 | `role`(COMMON 유형) · `role_i18n` · `log_role` | `biz_group` · `user_role` | COMMON 역할 삭제는 서버가 거부 |
| **ca-role-rule** 발령권한 관리 | `role`(RULE) · `role_i18n` · `role_condition` · `log_role` | `code`·`code_group`(조건값 이름) · 부서·현장(조건값 이름) | 화면은 조건만 저장. 자동 부여는 §3-3 |
| **ca-user-role** · **ca-user-role-by-user** | `user_role` · `log_role_grant` | `role` · `role_service`·`service`(실효 메뉴 미리보기) · `hmp_user`·`employee`(사용자 목록) · `biz_group` · `config`(본사/현장 구분) · `sod_rule`(직무분리) | 같은 화면, 방향만 다름 |
| **ca-role-assign** 지정권한 관리 | `role` · `role_i18n` · `log_role` · `user_role` · `log_role_grant` | `hmp_user` · `biz_group` · `config` · `sod_rule` | 역할 정의 + 부여를 한 화면에서 |
| **ca-role-delegation** 권한위임 관리 | `role_delegation` · `log_role_grant`(DELEGATE/REVOKE) | `user_role`(위임자 보유 권한 검증) · `code` | 만료는 스케줄러가 갱신 |
| **ca-user-role-inquiry** 사용자권한 조회 | 없음(조회 전용) | `user_role` · `role` · `role_delegation`(보유자) · `role_service`·`service`(실효 메뉴) · `hmp_user`·`user_i18n`(이름) · 부서·현장(이름) | 기본권한(COMMON) 역할을 화면에서 합성해 보여 줌 |
| **co_mobileAuthMapp** 사용자(모바일) | `user_role`(역할 `MOBILE_USER`) · `log_role_grant` | `hmp_user` · `user_i18n` · `employee` · `employee_dept` · `org_assignment` · `dept` · `org_unit` · `user_company` · `user_role` | erp-h 소스. 쓰기는 core `RoleService` 를 거침 |

## 3. 프로그램별 상세

### 3-1. 역할 정의 화면 — ca-role · ca-role-common · ca-role-rule · ca-role-assign (정의 부분)

세 화면 모두 `/role-manage/*` 를 쓴다(역할 유형만 다름).

| API | 서비스 | 테이블 |
|---|---|---|
| `/list` | `findBySystemId…` (+ 업무그룹 유형 필터 `BizGroupService`) | R `role` · `biz_group` |
| `/save` (등록·수정·삭제) | `batchSave` → `create`/`update`/`delete` | W `role` · 로그 `log_role`(생성·수정·삭제를 `logTx` 별도 트랜잭션으로 기록) · 최상위 업무그룹 검증 R `biz_group` |
| `/i18n-list`·`/i18n-save-all` | `findI18nByRoleId` · `saveAllI18n` | R·W `role_i18n` |
| `/condition-list`·`/condition-save` (ca-role-rule) | `findConditionsByRoleId` · `createCondition`/`updateCondition`/`deleteCondition` | R·W `role_condition` · 조건값 이름 R `code`·`code_group` · 조직 이름 R `dept`·`org_unit`(OrgScopeProvider) |
| `/user-role-list` | `findUserRolesByRoleId` | R `user_role` |
| (보조) `/sys/biz-group/list` · `/sys/system-language/list` · `/sys/user-scope/user-types` | | R `biz_group` · `system_language` · 사용자 유형 목록(코드/상수) |

- **role 테이블 쓰기 규칙**: 시스템관리자 역할은 시스템관리자만 수정, COMMON 역할은 삭제 불가(`COMMON_ROLE_DELETE_DENIED`).

### 3-2. 부여 화면 — ca-user-role · ca-user-role-by-user · ca-role-assign (부여 부분)

| API | 서비스 | 테이블 |
|---|---|---|
| `/user-role-list` (AL 0) | `findUserRolesByRoleId` · `searchUserRoles` | R `user_role` (+ 이름 풀이: `hmp_user`·`user_i18n`·부서·현장) |
| `/user-role-save` | `batchSaveUserRoles` → `createUserRole`/`updateUserRole`/`deleteUserRole` | **W `user_role`** · **W `log_role_grant`**(부여/회수 이력, `logTx` 별도 트랜잭션) |
| 부여 전 검사 | `assertExclusiveRoleGroups` | R `user_role`(같은 사용자의 기존 부여) · `role`(본사/현장 그룹) · `config`(`SECURITY.role-group.hq`·`.site`) |
| 부여 전 검사 | `SodRuleService.checkGrant` | R `sod_rule` · `user_role` · `role` |
| 부여 전 검사 | `assertGrantableOrgType` · `requireGrantInScope` | R `role`(부여 가능 조직 유형) · 법인 범위 검사는 `OrgScopeProvider`·법인 가시범위를 거친다(하부 쿼리는 따라가지 않음 — §7) |
| `/exclusive-role-groups` (AL 0) | `findExclusiveRoleGroups` | R `config` |
| `/user-menu-preview` (AL 0) | `loginService.resolveRoleIds` → 메뉴 서비스 | R `hmp_user` · `user_role` · `role` · `role_service` · `service` |
| `/sys/user/list` | `HmpUserService.findActiveWithEmployee` | R `hmp_user` (+ `employee`) |
| `/sys/user-scope/org-types` | 조직 유형 목록 | 조직 범위 공급자(부서·현장 등) |

- 부여 행의 키는 `(system_id, user_id, role_id, org_type, org_id, start_date)` 이다(`user_role` PK 에 조직·시작일 포함).
- 종료일·부여 유형(`ASSIGN`/`RULE`/…)·부여 사유가 같은 행에 남는다.

### 3-3. 발령권한(RULE) 자동 부여 — 화면이 아닌 백엔드 (ca-role-rule 과 이어짐)

| 구성 | 테이블 |
|---|---|
| `RoleRuleServiceImpl` (`ROLE_RULE_SYNC` 스케줄러 · 발령 이벤트) | R `role` · `role_condition` · `hmp_user` · 소속(`org_assignment` 계열, `OrgMembershipProvider`) · **W `user_role`**(grant_type `RULE`) · **W `log_role_grant`** · R `sod_rule` |
| `RoleExpiryService` (매일 01:20 `ROLE_EXPIRY`) | R·W `user_role`(종료일 지난 행 회수) |

### 3-4. 권한위임 — ca-role-delegation

| API | 서비스 | 테이블 |
|---|---|---|
| `/list` · `/detail` | `search` · `detail` | R `role_delegation` |
| `/save`·`/create`·`/revoke`·`/delete` (AL 1) | `batchSave`/`create`/`revoke`/`delete` | **W `role_delegation`** · **W `log_role_grant`** · 검증 R `user_role`(위임자가 보유한 권한만 위임 가능) |
| `/sys/code/list` (화면 보조) | 코드 조회 | R `code` |
| 로그인 병합 | `getActiveDelegationsForLogin` | R `role_delegation`(활성 위임이 로그인 역할에 더해짐) |
| 만료 | `expireDelegations`(`RoleDelegationExpireJob`) | W `role_delegation`(상태 갱신) |

### 3-5. 사용자권한 조회 — ca-user-role-inquiry

| API | 서비스 | 테이블 |
|---|---|---|
| `/user-role-inquiry` | `inquireUserRoles` → `UserRoleRepository.findForInquiry` | R `user_role` JOIN `role` (조직 유형·조직 ID·역할 유형으로 필터) · 기본권한(COMMON)은 `role` 에서 합성 |
| `/role-holders` (AL 0) | `loginService.listRoleHolders` | R `user_role` · `role` · `role_delegation` |
| `/user-menu-preview` (AL 0) | §3-2 와 같음 | R `role_service` · `service` |
| `/role-service/list` | `ServiceService.findRoleServicesByRoleId` | R `role_service` |
| `/service-manage/list` | `ServiceController` | R `service` |
| (보조) `/sys/biz-group/list` · `/sys/user-scope/*` | | R `biz_group` · 조직 범위 목록 |

### 3-6. 사용자(모바일) — co_mobileAuthMapp (erp-h)

| API | 서비스·매퍼 | 테이블 |
|---|---|---|
| `/auth/getGrd_MobileUserList` | `MobileAuthMappMapper.selectUserDeptList` (MyBatis · `erp-co-mobileAuthMapp.xml`) | R `HMP.hmp_user` · `user_i18n` · `employee` · `employee_dept` · `org_assignment` · `dept` · `org_unit` · `user_company` · `user_role` |
| `/auth/setGrd_MobileUserCode` | `roleService.createUserRole` / `updateUserRole` / `deleteUserRole` (역할 `MOBILE_USER` 고정) | **W `user_role`** · **W `log_role_grant`** (core 가 기록) |
| 보유자 조회 | `roleService.findUserRolesByRoleId("ERP","MOBILE_USER")` | R `user_role` |

- 이 프로그램만 erp-h 자체 SQL(MyBatis)이 `HMP.*` 표를 직접 읽는다. 나머지 8개는 core(JPA)가 처리한다.

## 4. 테이블 × 프로그램 매트릭스

R 읽기 · W 쓰기 · — 안 씀.

| 테이블 | ca-role | ca-role-common | ca-role-rule | ca-user-role (둘) | ca-role-assign | ca-role-delegation | ca-user-role-inquiry | co_mobileAuthMapp |
|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| `role` | W | W | W | R | W | — | R | — |
| `role_i18n` | W | W | W | — | W | — | — | — |
| `role_condition` | — | — | W | — | — | — | — | — |
| `user_role` | R | R | R | W | W | R | R | W |
| `log_role` | W | W | W | — | W | — | — | — |
| `log_role_grant` | — | — | — | W | W | W | — | W |
| `role_delegation` | — | — | — | — | — | W | R | — |
| `role_service` | — | — | — | R | — | — | R | — |
| `service` | — | — | — | R | — | — | R | — |
| `biz_group` | R | R | R | R | R | — | R | — |
| `sod_rule` | — | — | — | R | R | — | — | — |
| `config` | — | — | — | R | R | — | — | — |
| `hmp_user` / `user_i18n` | — | — | — | R | R | — | R | R |
| `code` / `code_group` | — | — | R | — | — | R | — | — |
| `system_language` | R | — | R | — | R | — | — | — |
| `dept` · `org_unit` (조직 이름 풀이 · `OrgScopeProvider`) | — | — | R | R | R | — | R | R |
| `employee` · `employee_dept` · `org_assignment` · `user_company` | — | — | — | — | — | — | — | R (erp-h mapper) |

## 5. 테이블 사전

| 테이블 | 엔티티 | 내용 | 키 |
|---|---|---|---|
| `role` | `Role` | 역할 정의(유형 ASSIGN/COMMON/RULE · 업무관리자·시스템관리자 여부 · 사용여부 · 업무그룹) | `(system_id, tenant_id, role_id)` |
| `role_i18n` | `RoleI18n` | 역할 이름 다국어 | `(system_id, role_id, language)` |
| `role_condition` | `RoleCondition` | 발령권한(RULE) 부여 조건 | `(system_id, role_id, condition_seq)` |
| `user_role` | `UserRole` | 사용자↔역할 부여(조직·기간·부여유형 `ASSIGN`/`RULE`) | `(system_id, user_id, role_id, org_type, org_id, start_date)` |
| `role_service` | `RoleService` | 역할↔서비스(메뉴·API) 부여, 등급·범위 | `(system_id, role_id, service_id)` |
| `role_delegation` | `RoleDelegation` | 권한 위임(위임자→수임자, 기간, 상태) | 위임 ID |
| `log_role` | `RoleLog` | 역할 정의 변경 이력(생성·수정·삭제) | — |
| `log_role_grant` | `RoleGrantLog` | 부여·회수·위임 이력 | — |
| `log_role_service` | `RoleServiceLog` | 역할↔서비스 부여 변경 이력 (이 9개 화면은 쓰지 않음 — `ca-role-service` 가 씀) | — |
| `role_request` · `role_request_target` | `RoleRequest` · `RoleRequestTarget` | 권한·겸직 신청 (개인 메뉴 `ca-role-request` 가 씀 — 이 9개 화면 밖) | — |
| `sod_rule` | `SodRule` | 직무분리 규칙(부여 전 검사) | — |
| `hmp_user` | `HmpUser` | 로그인 계정 | `user_id` |

## 6. 소스에서 발견한 점

1. **역할을 삭제하면 `role_i18n` · `role_condition` · `role_service` 도 JPA 연쇄로 함께 지워진다.** `Role` 엔티티의 세 컬렉션이 `cascade = ALL, orphanRemoval = true` 이다(`Role.java` 89~96행). 반면 `user_role` 은 `Role` 에 연결된 컬렉션이 없어 **지워지지 않고 남는다**(고아 부여). 이전 권한 정의표(`auth-def/ca-role.md`)는 「role_service · user_role 부여가 고아로 남는다」라고 적었는데, 소스 기준으로 role_service 는 고아로 남지 않는다. 실제 DB 의 FK 설정은 확인하지 못해, 삭제 전에 `user_role` 을 먼저 정리하는 것이 안전하다.
2. **부여·회수 이력은 별도 트랜잭션(`logTx`)으로 기록한다.** `log_role_grant`·`log_role` 저장이 본 작업 트랜잭션과 분리되어, 이력 저장 실패가 부여를 막지 않는다는 구조다(`TransactionTemplate logTx`).
3. **`ca-role-assign`(지정권한 관리) 은 역할 정의(`role`, `role_i18n`)와 부여(`user_role`)를 한 화면에서 쓴다.** 두 종류의 쓰기 권한(역할 관리자 등급 2 이상)이 같은 화면에 섞여 있다.
4. **본사/현장 배타·현장 1개 규칙은 DB 가 아니라 설정에서 온다.** `config` 의 `SECURITY.role-group.hq`(기본 `HQ,DT`) · `SECURITY.role-group.site`(기본 `SITE,DY`) 값과 `role` 의 그룹으로 판정한다(`user_role` 저장 직전).
5. **`user_role` 에 쓰는 경로가 4개다.** ① `ca-user-role`/`ca-role-assign` 화면 ② `co_mobileAuthMapp`(MOBILE_USER) ③ 발령 자동 부여 `RoleRuleServiceImpl`(RULE) ④ 권한 신청 승인(`ca-role-request`, 이 문서 범위 밖). 회수는 ①②와 만료 스케줄러(`RoleExpiryService`)·`RoleRuleServiceImpl` 이 한다.
6. **`RoleManageController` 의 읽기 API 일부는 `@AuthLevel(0)`**(로그인만으로 열림, 예: `user-role-list` · `user-menu-preview` · `role-holders` · `exclusive-role-groups`)이고, 메서드 안에서 역할 관리자인지 따로 판정한다. 읽기 접근이 등급 표만으로는 결정되지 않는다.
7. **`role_delegation` 쓰기 API 는 등급 1(`@AuthLevel(1)`)로 열려 있다.** 개인 메뉴 「권한위임」과 같은 컨트롤러를 쓰기 때문이다(소스 주석). 서비스 계층이 본인 위임만 허용하도록 막는다.
8. **쓰기 허용 여부는 호출 화면의 serviceId 기준이라 두 탭이 따로 판정된다**(§1-1). 쿼리와 표는 같아도 `ca-role` 에만 부여받은 계정은 `sys-ca-role` 에서 저장이 거부될 수 있다. 앞의 비교 파일들에서 두 탭 등급이 같게 나온 것은 erpadmin·dev01 두 계정의 결과이고, 다른 계정으로는 달라질 수 있다.

## 7. 확인하지 못한 것

- **실제 DB 의 DDL·FK·인덱스**: 소스의 엔티티 매핑만 읽었다. 연쇄 삭제가 DB FK 에서도 같은지, `user_role` 에 FK 가 있는지는 모른다.
- **사용자 목록·조직 이름 풀이의 하부 쿼리**: `HmpUserService.findActiveWithEmployee`, `OrgScopeProvider`(부서·현장 이름) 구현은 `feature-org-dept`·`feature-org-unit` 모듈이다. 여기서는 읽는 표만 적었고 쿼리를 끝까지 따라가지는 않았다. `user_i18n` 읽기는 `UserNameResolver`(JdbcTemplate) 소스로 확인했다.
- **`user-menu-preview` 의 메뉴 계산**: `MenuService` 내부 쿼리는 따라가지 않았다. `role_service`·`service` 를 쓴다고 정리한 것은 컨트롤러가 부르는 `roleServiceRepository`·`menuService.getRoleServices` 호출을 근거로 한 것이다.
- **법인 범위 검사(`requireGrantInScope`)·조직 이름 풀이가 읽는 표**: `OrgScopeProvider` 구현(부서·현장)에 위임하는 부분이라 `dept`·`org_unit` 으로 적었으나, 법인 가시범위 계산이 어떤 표(`user_company` 등)를 읽는지는 확인하지 않았다.
- **공통·시스템 별칭의 `service.params` 값**: DB 의 `service` 행에서 `ca-*` 와 `sys-ca-*` 의 `params` 가 같은지(특히 `mode`·`systemScope`) 읽지 못했다. 화면 동작이 두 탭에서 완전히 같은지는 이 값과 실제 화면 비교가 필요하다.
- **두 탭 사이 부여가 다른 계정**: `ca-*` 와 `sys-ca-*` 에 서로 다른 등급을 받은 계정을 찾지 못해, §1-1 의 판정 차이를 실제 계정으로는 시험하지 못했다.
- **스케줄러 정확한 주기·설정**: `ROLE_RULE_SYNC`·`ROLE_EXPIRY` 이름과 매일 01:20 은 이전 시험 계정 문서의 설명이다. 소스의 크론식은 확인하지 않았다.
- 화면을 실행해 실제로 발생하는 SQL 을 로그로 확인하지는 않았다.
