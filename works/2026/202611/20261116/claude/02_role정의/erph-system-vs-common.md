# erp-h 「시스템」 권한 ↔ 「공통」 권한 비교

> 작성 2026-11-16 · 해석: 메뉴의 「시스템」 탭과 「공통」 탭에 있는 같은 이름의 권한 메뉴를 비교하고, 두 탭이 쓰는 role 층(`SYS_*` ↔ `BIZ_*`)도 함께 정리했다.
> 근거: ① Aside 로 읽은 메뉴 API(erpadmin · dev01) ② `db/common-admin/P5-menu-system-full.sql`(두 탭을 만든 SQL) ③ `P5-menu-system-full-projection.tsv`(2026-11-11 계정별 메뉴 수 실측) ④ `S6-role-codes.md` · `P3-04-roles.sql` · `P3-27` · `P3-28`
> DB 는 직접 읽지 않았다. 화면을 열어 동작을 비교하지도 않았다.

## 1. 한눈에 보기

| 항목 | 공통 | 시스템 |
|---|---|---|
| 메뉴 구성 | 폴더 6개 · 화면 49개 | 폴더 12개 · 화면 172개 |
| 성격 | 업무 전체 관리 (`BIZ_*` role 이 담당) | 플랫폼·개발·보안까지 전체 (`SYS_*` · `DEVELOPER` 가 담당) |
| 관계 | **시스템의 부분집합** — 같은 폴더 구조의 일부를 「공통」에 따로 둠 | 전체 메뉴 |
| 화면 소스 | 같음 — 시스템 쪽은 같은 화면 소스를 쓰는 별칭(`sys-` 접두어 · sharedProgram) | 〃 |
| 권한(role_service) | 화면(serviceId)마다 따로 부여 | `sys-` 별칭마다 따로 부여 |
| 이 탭을 보는 계정 | erpadmin · dev01 (그 밖에 `BIZ_ADMIN` 계열) | erpadmin · dev01 (그 밖에 `SYS_ADMIN` 계열) |

핵심: **같은 화면이어도 공통 탭의 권한과 시스템 탭의 권한은 별개 부여다.** 공통의 `ca-role` 을 열 수 있어도 시스템의 `sys-ca-role` 은 따로 부여받아야 열린다.

## 2. 구조 비교

### 2-1. 폴더

| 폴더 | 공통 | 시스템 |
|---|---:|---:|
| 조직·사용자 | 10 | 10 |
| 권한 | 9 | 9 |
| 메뉴·업무그룹 | 4 | 5 |
| 기준정보 | 8 | 8 |
| 결재·문서 | 11 | 11 |
| 게시·알림 | 7 | 7 |
| 플랫폼 | · | 16 |
| 보안 | · | 21 |
| 운영·로그 | · | 22 |
| 연동 | · | 15 |
| 문서 설정 | · | 5 |
| AI | · | 11 |
| 개발도구 | · | 32 |

- 공통에만 있는 폴더: 없음. 시스템에만 있는 폴더: 플랫폼, 보안, 운영·로그, 연동, 문서 설정, AI, 개발도구.
- 공통 49개 화면 중 시스템에 같은 `sys-<id>` 별칭이 있는 것: **49개 (전부)**. 공통은 시스템의 부분집합이라는 설계가 메뉴 데이터에서 그대로 확인된다.
- 시스템의 공통 폴더 6개 영역에 있으면서 공통에는 없는 화면: `ca-program`(프로그램관리 · 메뉴·업무그룹).

### 2-2. 서비스 id 규칙

- 시스템 별칭의 id 는 공통 id 앞에 `sys-` 를 붙인 것이다(`ca-role` ↔ `sys-ca-role` · `co_deptMng` ↔ `sys-co_deptMng`).
- 시스템 탭에서 공통과 같은 6개 폴더 안의 화면은 50개이고, 그중 공통에 없는 것은 `ca-program`(프로그램관리) 하나다. 시스템에만 있는 화면 123개(= 172 − 49)는 시스템 전용 폴더 7개의 122개 + `ca-program` 이다.

## 3. 「권한」 메뉴 비교 (공통 › 권한 ↔ 시스템 › 권한)

| 메뉴 경로 | 공통 serviceId | 시스템 serviceId | erpadmin 공통/시스템 | dev01 공통/시스템 | 기능 허용(UIDPE) 공통/시스템 |
|---|---|---|---|---|---|
| 권한 › 권한 정의 › **권한 관리** | `ca-role` | `sys-ca-role` | 3 / 3 | 1 / 1 | - / - |
| 권한 › 권한 정의 › **공통권한 관리** | `ca-role-common` | `sys-ca-role-common` | 3 / 3 | 1 / 1 | - / - |
| 권한 › 권한 정의 › **발령권한 관리** | `ca-role-rule` | `sys-ca-role-rule` | 3 / 3 | 1 / 1 | - / - |
| 권한 › 권한 부여 › **권한/사용자매핑** | `ca-user-role` | `sys-ca-user-role` | 3 / 3 | 1 / 1 | UIDPE / UIDPE |
| 권한 › 권한 부여 › **사용자/권한매핑** | `ca-user-role-by-user` | `sys-ca-user-role-by-user` | 3 / 3 | 1 / 1 | UIDPE / UIDPE |
| 권한 › 권한 부여 › **지정권한 관리** | `ca-role-assign` | `sys-ca-role-assign` | 3 / 3 | 1 / 1 | UIDPE / UIDPE |
| 권한 › 권한 부여 › **권한위임 관리** | `ca-role-delegation` | `sys-ca-role-delegation` | 3 / 3 | 1 / 1 | UIDPE / UIDPE |
| 권한 › 권한 부여 › **사용자(모바일)** | `co_mobileAuthMapp` | `sys-co_mobileAuthMapp` | 3 / 3 | 1 / 1 | UIDPE / UIDPE |
| 권한 › 권한 부여 › **사용자권한 조회** | `ca-user-role-inquiry` | `sys-ca-user-role-inquiry` | 3 / 3 | 1 / 1 | - / - |
| 메뉴·업무그룹 › **권한/메뉴매핑** | `ca-role-service` | `sys-ca-role-service` | 3 / 3 | 1 / 1 | IE / - |

- 공통 「권한」 폴더는 9개 화면이고 시스템 「권한」 폴더도 같은 9개다(2026-11-11 실측 projection 에서도 공통 > 권한 9 · 시스템 > 권한 9).
- 등급은 두 탭 모두 erpadmin 3 · dev01 1 로 같다.
- 기능 허용(U 업로드 · I 엑셀 가져오기 · D 다운로드 · P 인쇄 · E 엑셀 내보내기)도 같은데 **`권한/메뉴매핑`(`ca-role-service`)만 다르다** — 공통은 I·E 허용, 시스템 별칭 `sys-ca-role-service` 는 없음. 같은 화면 소스라도 부여 행이 따로여서 생긴 차이다.

### 3-1. 권한·인증·접근과 관련하지만 시스템 탭에만 있는 화면

| 메뉴 경로 | serviceId | erpadmin | dev01 |
|---|---|---:|---:|
| 보안 › 인증 › **인증정책 관리** | `ca-auth-policy` | 3 | 1 |
| 보안 › 인증 › **통합 인증 공급자** | `ca-auth-provider` | 3 | 1 |
| 보안 › 인증 › **인증항목패턴 관리** | `ca-auth-item-pattern` | 3 | 1 |
| 보안 › 인증 › **외부 사용자 초대·승인·만료** | `ca-external-access` | 3 | 1 |
| 보안 › 접근 통제 › **접근정책 관리** | `ca-network-access-policy` | 3 | 1 |
| 보안 › 접근 통제 › **서비스접근 관리** | `ca-service-access` | 3 | 1 |
| 보안 › 접근 통제 › **접근 정책 규칙 관리** | `ca-access-rule` | 3 | 1 |
| 보안 › 권한 정책 › **ABAC 접근 정책 관리** | `ca-access-policy` | 3 | 1 |
| 보안 › 권한 정책 › **ReBAC 권한 튜플 관리** | `ca-permission-tuple` | 3 | 1 |
| 보안 › 권한 정책 › **권한 매트릭스** | `ca-auth-matrix` | 3 | 1 |
| 보안 › 권한 정책 › **권한조절 공급자** | `ca-auth-adjust-provider` | 3 | 1 |
| 운영·로그 › 로그 › **권한메뉴 로그** | `ca-log-role-service` | 3 | 1 |
| 운영·로그 › 로그 › **권한부여 로그** | `ca-log-role-grant` | 3 | 1 |
| 문서 설정 › **문서 접근 로그** | `ca-smart-doc-access-log` | 3 | 1 |
| AI › AI 설정 › **AI 권한 설명기** | `ca-ai-access-explainer` | 3 | 1 |

- 공통 탭에는 권한 정책(ABAC·ReBAC·권한 매트릭스)·권한 로그·AI 권한 설명기가 없다. 권한을 **부여·조회**하는 일은 공통에서, 권한 **정책·감사**는 시스템에서 한다.

## 4. 등급 비교 (erpadmin ↔ dev01)

| 탭 | 화면 | erpadmin 등급 1/2/3 | dev01 등급 1/2/3 |
|---|---:|---|---|
| 공통 | 49 | 0/0/49 | 49/0/0 |
| 시스템 | 172 | 0/0/172 | 139/0/33 |

- erpadmin 은 두 탭 모두 전부 3. dev01 은 공통 전부 1(조회), 시스템은 개발도구 33개만 3 · 나머지 1.

## 5. 계정별로 어느 탭이 보이는가 (2026-11-11 실측 projection + 이번 Aside 실측)

| 계정 | 공통 | 시스템 | 출처 |
|---|---:|---:|---|
| h.bizadm (BIZ_ADMIN) | 45 | 없음 | projection 11-11 |
| h.sysadm (SYS_ADMIN) | 46 | 167 | projection 11-11 |
| h.dev01 (DEVELOPER) | 46 | 167 | projection 11-11 |
| h.hr01 (BIZ_MNG_01) | 7 | 없음 | projection 11-11 |
| h.acc01 (BIZ_MNG_02) | 7 | 없음 | projection 11-11 |
| erpadmin | 49 | 172 | Aside 11-16 |
| dev01(「개발자1」) | 49 | 172 | Aside 11-16 |
| csadm·cs01·cs02·cs03·site01 | 없음 | 없음 | Aside 11-16 (하자 탭만) |

- projection(11-11)과 Aside(11-16)의 화면 수 차이(공통 46→49, 시스템 167→172)는 그 사이 화면이 추가된 결과로 보인다(예: `ca-user-role-by-user` · `ca-page-source-editor`). 원인을 확인하지는 않았다.
- 업무 관리자(`BIZ_ADMIN`)만 가진 계정은 「공통」만, 시스템 관리자 계열은 둘 다 보인다.

## 6. role 층 비교: `SYS_*` ↔ `BIZ_*`

(설계서 `S6-role-codes.md` · `P3-04-roles.sql` 기준)

| 구분 | 시스템 층 | 공통(업무) 층 |
|---|---|---|
| 관리자 role | `SYS_ADMIN` — 「시스템」 전체, 터미널·PBE 복호화 전용 | `BIZ_ADMIN` — 「공통」 전체, 요청 수신(`is_biz_admin=Y`) |
| 개발 role | `DEVELOPER` — 개발도구 전체 3 + 그 밖 조회 1 | (없음) |
| 위임 담당 | (없음) | `BIZ_MNG_01`(사용자·조직) · `BIZ_MNG_02`(기준정보) |
| 전원 role(COMMON) | `SYS_COMMON` — 개인 서비스·헤더 기능·플랫폼 필수 API | `BIZ_COMMON`(내부 직원) · `PTNR_COMMON`(협력업체) |
| 발령 role(RULE) | — | `HQ_COMMON`(본사 부서) · `SITE_COMMON`(현장) |

### 6-1. 두 층 사이의 연결 (패치로 바뀐 것)

- **P3-27 (11-11)**: `SYS_ADMIN` 보유자에게 `BIZ_ADMIN` 도 부여. 초안(U-2)은 「erpadmin = SYS_ADMIN 만」이었으나 「시스템관리자도 공통을 본다」로 바뀌었다. erpadmin 이 두 탭을 모두 보는 이유다.
- **P5-menu-system-full (11-11)**: 시스템 별칭 `sys-*` 를 만들면서 `SYS_ADMIN` · `DEVELOPER` 에게 자기 부여를 복사하고, `SYS_ADMIN` 이 없으면 `BIZ_ADMIN` 부여를 사본으로 만들도록 했다.
- **P3-28 (11-11)**: `BIZ_ADMIN` 의 불필요한 「시스템」 API 첫 마디 51개를 회수(162→111). 공통 업무 관리자가 시스템 화면의 데이터 API 를 URL 로 호출해도 열리지 않게 한 것.
- 계획표(P3) 기준으로 `BIZ_ADMIN` 이 「시스템」 탭 화면 중 가진 것은 보안 2개뿐이다. 「공통」 탭의 같은 화면은 공통 serviceId 로 따로 부여받는다.

## 7. 정리

| 질문 | 답 |
|---|---|
| 같은 화면인가 | 소스는 같고, serviceId 는 다르다 (`ca-role` ↔ `sys-ca-role`) |
| 권한은 공유되는가 | 아니다. 각각 부여해야 한다 |
| 화면에서 하는 일이 다른가 | 같은 화면 소스이므로 같은 기능. 차이는 누가 열 수 있고 어떤 등급을 받는지 |
| 어느 탭에서 권한 부여 작업을 해야 하나 | 공통 업무 담당자는 「공통 › 권한」, 시스템 관리자도 「공통 › 권한」으로 같은 일을 할 수 있다. 시스템 탭은 정책·감사 화면이 더해진 전체 메뉴 |
| 등급 | erpadmin 두 탭 3 · dev01 두 탭에서 권한 화면 1(조회만) |

## 8. 확인하지 못한 것

- 두 탭의 같은 화면(`ca-role` ↔ `sys-ca-role`)이 실제로 같은 동작을 하는지 화면을 열어 비교하지 않았다. 문서(`sharedProgram`)가 같은 화면 소스라고 한 것을 따랐다.
- 시스템 탭 `sys-*` 화면의 role_service 부여 행(어느 role 이 몇 등급으로 가졌는지)은 계획표(P3)에 없고 P5-menu-system-full 로 생성되어, DB 를 읽지 않으면 role 별로 확인할 수 없다.
- 질문이 「메뉴 탭 비교」가 아니라 「role 의 SYS 층 ↔ BIZ 층」만을 뜻했다면 §6 이 해당 부분이다. 방향을 알려 주면 그쪽을 더 깊게 하겠다.
