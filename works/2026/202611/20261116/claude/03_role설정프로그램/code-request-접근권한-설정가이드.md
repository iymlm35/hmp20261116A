# 코드 등록 요청 관리(code-request) 접근 권한 설정 가이드

작성: 2026-11-18 · 근거: products-erp-h 의 `docs/work/common-admin`(auth-def · S1~S6 · SCREENS-COMMON) · `db/common-admin` SQL · 프로그램관리·메뉴 화면 캡처
주의: 화면 코드(Java·JS)는 이 저장소에 없다(외부 모듈 `hmp-feature-data-standard`). 아래는 정의표·SQL·캡처로 확인한 내용이며, 화면 버튼의 실제 동작은 실행 화면에서 한 번 더 확인할 것.

## 1. code-request 가 하는 일

| 항목 | 내용 |
|---|---|
| 메뉴 | 시스템 › 개발도구 › DB·데이터 › 코드 등록 요청 관리 |
| 프로그램 / 서비스 | `code-request` (SYS_CODE · DATA_STANDARD) · 시스템 메뉴판용 별칭 `sys-ca-code-request` |
| 용도 | 시스템관리자가 코드 신규·수정·폐기 요청을 보고 **승인·반려** |
| 요청 작성 | 이 화면이 아니라 `ca-code-request-form` 팝업. 공통코드 조회 화면(`ca-code`·`sys-ca-code`)의 [등록 요청]·[수정 요청]·[폐기 요청] 버튼이 연다 |
| 승인 효과 | NEW = 코드 행 생성 · MODIFY = 이름·속성 갱신 · DELETE = 사용여부 N(물리 삭제 아님) |
| 삭제 | 없음. 요청은 반려 포함 이력으로 남김 |

API 등급: `/code-request/list`·`save`·`my-list` = 1 · `/code-request/approve`·`reject` = 2 (REQUESTED 상태만, 아니면 서버 INVALID_STATUS)

## 2. 부여해야 할 값 (P3-29 · P32 기준 설계값)

| 서비스 | 역할 | 등급 | 데이터 범위 | 비고 |
|---|---|---|---|---|
| `code-request` | SYS_ADMIN | 3 | ALL | 승인·반려 |
| `code-request` | DEVELOPER | 1 | ALL | 조회 + 요청 API(`save`·`my-list`) 통과용 |
| `sys-ca-code-request` | SYS_ADMIN | 3 | ALL | 시스템 메뉴판. 11/17 P32 가 결손 보강 |
| `sys-ca-code-request` | DEVELOPER | 1 | ALL | 〃 |
| `ca-code-request-form` | SYS_ADMIN · DEVELOPER | 3 | ALL | 요청 작성 팝업(`is_menu` N · 메뉴 없음) |
| `ca-code-request` (공통 › 기준정보) | — | — | — | 부여 0 유지(숨김). P3-19 · P3-29 |

파일 다운로드·업로드·엑셀·인쇄 허용은 모두 N. 설계자 역할은 시드에 없어 DEVELOPER 만 부여(설계자 역할 신설 시 같은 부여 필요).

## 3. 권한을 설정하는 화면 = 「권한/메뉴매핑」

화면 이름이 세 군데에서 다르다. 같은 프로그램이다.

| 어디서 | 이름 |
|---|---|
| 프로그램관리(프로그램 목록) | 권한서비스 매핑 · 프로그램ID `role-service` · 경로 `role/role-service` · 분류 SYS_ROLE(역할/권한 관리) |
| 메뉴에 보이는 이름 | **권한/메뉴매핑** (서비스 `ca-role-service`, 시스템 메뉴판 `sys-ca-role-service`) |
| 정의서(auth-def) | ca-role-service — 서비스 권한(역할↔서비스 부여) |

메뉴 위치: 시스템 › 권한 › 메뉴·업무그룹 › 권한/메뉴매핑 (공통 메뉴판에도 같은 폴더에 있음).
메뉴·업무그룹 폴더 구성: 업무그룹 관리 · (프로그램관리는 시스템만) · 메뉴트리관리 · 권한/메뉴매핑 · 매뉴얼.

헷갈리기 쉬운 이웃 화면(권한 › 권한 부여 폴더):

| 메뉴 이름 | 서비스 | 연결 대상 |
|---|---|---|
| 권한/사용자매핑 | `ca-user-role` | 권한 기준으로 사용자 붙임 |
| 사용자/권한매핑 | `ca-user-role-by-user` | 사용자 기준으로 권한 붙임(같은 프로그램 `user-role`) |
| **권한/메뉴매핑** | `ca-role-service` | **역할 ↔ 서비스(메뉴·API)** ← code-request 부여는 여기 |

## 4. 적용 순서

0. 위 2번 표로 무엇을 줄지 정한다.
1. **서비스 존재 확인** — 메뉴트리관리나 서비스 관리(프로그램 `service`)에서 `code-request`·`sys-ca-code-request` 검색. 없으면 화면 문제가 아니라 모듈 설치 문제(`hmp-feature-data-standard`).
2. **권한/메뉴매핑에서 부여**
   - 좌측 서비스 그리드에서 `code-request` 체크(다중 체크 방식)
   - 우측 트리에서 역할(SYS_ADMIN) 선택
   - **→ 부여**로 등급·데이터 범위 지정 후 **저장**. 회수는 **← 회수**
   - DEVELOPER 는 등급 1로 반복. `sys-ca-code-request`·`ca-code-request-form` 도 같은 방식
3. **반영 확인** — 서비스 더블클릭 → 그 서비스를 가진 권한 목록 / 시스템 › 권한 이력의 권한서비스 로그(`log-role-service`) / 사용자권한 조회 / 사용자권한 관리의 메뉴 미리보기
4. **사용자에게 역할이 붙어 있는지 확인** — 사용자/권한매핑 또는 권한/사용자매핑. 역할에 서비스를 붙여도 사용자에게 그 역할이 없으면 안 보인다. 본사·현장 권한은 같은 조직에 함께 줄 수 없다(검사 있음).
5. **메뉴 캐시 갱신** — `reload-menu`(`/cache/reload-menu`). 화면에서 쓰는 방법은 확인 못 함 → 관리자 요청.

## 5. 주의

- 부여·회수는 `role-service` 화면 등급 2 이상(역할 관리자). SYS_ADMIN 역할의 부여 변경은 시스템관리자만 가능(`requireSystemAdminForRole`).
- 조회(등급 1)는 `/role-service/list`·`detail`. 부여·회수·등급 변경·기본권한 생성은 `/role-service/save`·`create`·`update`·`delete`·`copy` (@AuthLevel 2).
- 이 저장소는 SQL 스크립트에 서명을 받아 일괄 반영하는 절차를 쓴다. 화면에서 직접 바꾸면 `role_service_log` 에는 남지만, 이후 시드 스크립트가 다시 돌면 값이 덮이거나 어긋날 수 있다. 영구 반영은 SQL 경로(P3-29 · P5-sqldev-19 · P32)도 확인.
- 시스템 메뉴판에는 `sys-ca-role-service` 가 없는 환경이 있다(11/11 P6 기록: sql-dev 에는 행 없음). 그러면 공통 › 권한 › 메뉴·업무그룹에서 찾는다.
- 화면 데이터 범위는 현재 시스템(플랫폼관리자 모드에서 고른 시스템). 서비스·역할이 안 보이면 시스템 선택부터 확인.

## 6. 정책 변경 경위

| 일자 | 내용 |
|---|---|
| 11/09 | 코드 요청 작성 = 전원(BIZ_COMMON 2 SELF) |
| 11/10 · P3-19 | 승인·반려를 시스템 › 데이터 표준으로 이동 · 공통 `ca-code-request` 부여 삭제 |
| 11/12 · P3-29 | 「설계자·개발자 → 시스템관리자」로 변경. 요청 팝업은 메뉴에서 뺌 · BIZ_COMMON 회수 · DEVELOPER 3 · SYS_ADMIN 3 |
| 11/15 | sql-dev 용 `P5-sqldev-19` · `P5-sqldev-H6` 작성(HOLD 수리 재서명 대기 #195109) |
| 11/17 · P32 | `sys-ca-code-request` role_service 결손 보강(SYS_ADMIN 3 · DEVELOPER 1) · 서명 요청 중 |

## 7. 관련 파일 (products-erp-h)

- docs/work/common-admin/dev-guide/auth-def/ca-code-request.md · ca-code-request-form.md · ca-role-service.md
- docs/work/common-admin/dev-guide/SCREENS-COMMON.md (ca-role-service · ca-user-role)
- db/common-admin/P3-19-code-request-to-system.sql · P3-29-code-request-dev-only.sql · P5-sqldev-19-code-request-to-system-mssql.sql · P32-seed-gap-repair-mssql.sql
- db/common-admin/P31-menu-order-20261116-mssql.sql (메뉴 순서 · 권한/메뉴매핑 위치)
- db/common-admin/input/sqldev-service-20261115.psv (서비스 이름 대조: 권한/메뉴매핑 · 권한/사용자매핑 · 사용자/권한매핑)
