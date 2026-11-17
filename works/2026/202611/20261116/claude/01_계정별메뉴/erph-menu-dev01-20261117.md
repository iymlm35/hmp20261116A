# erp-h 상단메뉴별 left메뉴 트리 — 로그인 계정 「개발자1 (IT팀)」 (2026-11-17 재조회)

- 조회: 2026-11-17 · `http://localhost:8080/` · 화면 우상단 사용자 표시 「개발자1 (IT팀)」 (login id 는 화면에 안 나와 확인하지 못함 · `h.dev01` 로 추정)
- 방법: Aside 브라우저로 로그인된 탭의 left메뉴 `.menu-tree` 를 읽고(상단 탭 「공통」·「시스템」 하나씩), 메뉴 컴포넌트가 화면에 그리는 메뉴 데이터(`filteredMenuTree`)의 `label`·`serviceId`·`authLevel` 을 그대로 옮겼다. 메뉴 API(`POST /sys/menu/list`)는 호출하지 않았다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>`, 등급 순서. 폴더는 `이름/`. 화면 주소는 `/page/<serviceId>` 규칙을 따른 것이며 메뉴를 눌러 주소창을 대조하지는 않았다.
- 등급: 1 조회 · 2 저장 · 3 삭제·특수 (`authLevel`).
- 전날 만든 `erph-menu-dev01.md`(2026-11-16 조회)는 그대로 두었다. 두 문서의 차이는 §3 에 있다.

| 상단 탭 | 폴더 | 화면 | 비고 |
|---|---:|---:|---|
| 공통 | 16 | 48 | DOM 에서 읽은 메뉴 데이터 기준 |
| 시스템 | 2 | 7 | 개발도구 폴더 하나만 있음 |
| 개인 | - | - | 상단 탭에 안 나옴 · 이번에는 확인하지 않음 |

- 상단 탭은 「공통」·「시스템」 두 개다. left메뉴 아래쪽 고정 버튼 「업무 지도」는 메뉴 트리 데이터에 없고, 누르면 화면 탭이 열린다(두 상단 탭 모두).

## 1. 트리

```text
상단메뉴
├── [공통]  key=MENU_BIZ_COMMON (폴더 16 · 화면 48)
│   ├── 조직·사용자/
│   │   ├── 조직/
│   │   │   ├── 회사 관리  `ca-company` → `/page/ca-company` · 등급 3
│   │   │   ├── 사업장 관리  `ca-business-place` → `/page/ca-business-place` · 등급 3
│   │   │   └── 부서/조직 정보  `co_deptMng` → `/page/co_deptMng` · 등급 3
│   │   ├── 사용자/
│   │   │   ├── 임직원 관리  `ca-employee` → `/page/ca-employee` · 등급 3
│   │   │   ├── 사용자 관리  `ca-hmp-user-manage` → `/page/ca-hmp-user-manage` · 등급 3
│   │   │   ├── 사용자 법인 소속 관리  `ca-user-company` → `/page/ca-user-company` · 등급 3
│   │   │   └── 사용자 발령 관리  `ca-org-assignment` → `/page/ca-org-assignment` · 등급 3
│   │   └── 협력업체/
│   │       ├── 협력업체 관리  `ca-partner` → `/page/ca-partner` · 등급 3
│   │       └── 협력업체사용자 관리  `ca-partner-user` → `/page/ca-partner-user` · 등급 3
│   ├── 권한/
│   │   ├── 권한 정의/
│   │   │   ├── 권한 관리  `ca-role` → `/page/ca-role` · 등급 3
│   │   │   ├── 공통권한 관리  `ca-role-common` → `/page/ca-role-common` · 등급 3
│   │   │   └── 발령권한 관리  `ca-role-rule` → `/page/ca-role-rule` · 등급 3
│   │   └── 권한 부여/
│   │       ├── 권한/사용자매핑  `ca-user-role` → `/page/ca-user-role` · 등급 3
│   │       ├── 사용자/권한매핑  `ca-user-role-by-user` → `/page/ca-user-role-by-user` · 등급 3
│   │       ├── 지정권한 관리  `ca-role-assign` → `/page/ca-role-assign` · 등급 3
│   │       ├── 권한위임 관리  `ca-role-delegation` → `/page/ca-role-delegation` · 등급 3
│   │       ├── 사용자(모바일)  `co_mobileAuthMapp` → `/page/co_mobileAuthMapp` · 등급 3
│   │       └── 사용자권한 조회  `ca-user-role-inquiry` → `/page/ca-user-role-inquiry` · 등급 3
│   ├── 메뉴·업무그룹/
│   │   ├── 업무그룹 관리  `ca-biz-group` → `/page/ca-biz-group` · 등급 3
│   │   ├── 메뉴트리관리  `ca-service` → `/page/ca-service` · 등급 3
│   │   ├── 권한/메뉴매핑  `ca-role-service` → `/page/ca-role-service` · 등급 3
│   │   └── 매뉴얼  `ca-manual-viewer` → `/page/ca-manual-viewer` · 등급 3
│   ├── 기준정보/
│   │   ├── 코드 관리  `ca-code` → `/page/ca-code` · 등급 3
│   │   ├── 국가코드 관리  `ca-country` → `/page/ca-country` · 등급 3
│   │   ├── 화폐 관리  `ca-currency` → `/page/ca-currency` · 등급 3
│   │   ├── 환율 관리  `ca-exchange-rate` → `/page/ca-exchange-rate` · 등급 3
│   │   ├── 은행 관리  `ca-bank` → `/page/ca-bank` · 등급 3
│   │   ├── 공휴일 관리  `ca-holiday` → `/page/ca-holiday` · 등급 3
│   │   ├── 채번 규칙  `ca-numbering-rule` → `/page/ca-numbering-rule` · 등급 3
│   │   └── 결정표  `ca-decision-table` → `/page/ca-decision-table` · 등급 3
│   ├── 결재·문서/
│   │   ├── 결재/
│   │   │   ├── 결재 현황  `ca-approval-dashboard` → `/page/ca-approval-dashboard` · 등급 3
│   │   │   ├── 결재 양식 관리  `ca-approval-form-template` → `/page/ca-approval-form-template` · 등급 3
│   │   │   └── 문서양식관리  `co_elcDoc` → `/page/co_elcDoc` · 등급 3
│   │   ├── 문서 양식/
│   │   │   ├── 문서 유형 관리  `ca-smart-doc-type` → `/page/ca-smart-doc-type` · 등급 3
│   │   │   ├── 문서 템플릿  `ca-doc-template` → `/page/ca-doc-template` · 등급 3
│   │   │   ├── 문서 템플릿 관리  `ca-smart-doc-template` → `/page/ca-smart-doc-template` · 등급 3
│   │   │   └── 전자 양식 디자이너  `ca-eform-designer` → `/page/ca-eform-designer` · 등급 3
│   │   └── 문서 관리/
│   │       ├── 문서 탐색  `ca-edms-browse` → `/page/ca-edms-browse` · 등급 3
│   │       ├── 문서 등록/편집  `ca-edms-doc-manage` → `/page/ca-edms-doc-manage` · 등급 3
│   │       ├── 문서 보안 처리  `ca-common-doc-protect` → `/page/ca-common-doc-protect` · 등급 3
│   │       └── 보존 정책 관리  `ca-retention-policy` → `/page/ca-retention-policy` · 등급 3
│   └── 게시·알림/
│       ├── 게시/
│       │   ├── 게시판관리  `ca-board-manage` → `/page/ca-board-manage` · 등급 3
│       │   └── 문의 관리  `ca-inquiry` → `/page/ca-inquiry` · 등급 3
│       └── 메시지·알림/
│           ├── 통합 메시지 발송  `ca-msg-send` → `/page/ca-msg-send` · 등급 3
│           ├── 메시지템플릿 관리  `ca-msg-template` → `/page/ca-msg-template` · 등급 3
│           ├── 메시지 이력  `ca-msg-history` → `/page/ca-msg-history` · 등급 3
│           ├── 수신거부 관리  `ca-msg-opt-out` → `/page/ca-msg-opt-out` · 등급 3
│           └── 알림 관리  `ca-notification-manage` → `/page/ca-notification-manage` · 등급 3
└── [시스템]  key=MENU_COMMON_ADMIN (폴더 2 · 화면 7)
    └── 개발도구/
        └── 테스트/
            ├── 시험 현황  `test-dashboard` → `/page/test-dashboard` · 등급 3
            ├── 시험 대상  `test-target` → `/page/test-target` · 등급 3
            ├── 시험 사례  `test-scenario` → `/page/test-scenario` · 등급 3
            ├── 시험 묶음  `test-suite` → `/page/test-suite` · 등급 3
            ├── 시험 실행  `test-run` → `/page/test-run` · 등급 3
            ├── 테스트 점검항목  `test-check-item` → `/page/test-check-item` · 등급 3
            └── 테스트 대상 서비스  `test-service-item` → `/page/test-service-item` · 등급 3
```

## 2. 화면별 메뉴 경로 표

| 상단 탭 | 메뉴 경로 | serviceId | 등급 | 화면 주소 |
|---|---|---|---:|---|
| 공통 | 조직·사용자 › 조직 › **회사 관리** | `ca-company` | 3 | `http://localhost:8080/page/ca-company` |
| 공통 | 조직·사용자 › 조직 › **사업장 관리** | `ca-business-place` | 3 | `http://localhost:8080/page/ca-business-place` |
| 공통 | 조직·사용자 › 조직 › **부서/조직 정보** | `co_deptMng` | 3 | `http://localhost:8080/page/co_deptMng` |
| 공통 | 조직·사용자 › 사용자 › **임직원 관리** | `ca-employee` | 3 | `http://localhost:8080/page/ca-employee` |
| 공통 | 조직·사용자 › 사용자 › **사용자 관리** | `ca-hmp-user-manage` | 3 | `http://localhost:8080/page/ca-hmp-user-manage` |
| 공통 | 조직·사용자 › 사용자 › **사용자 법인 소속 관리** | `ca-user-company` | 3 | `http://localhost:8080/page/ca-user-company` |
| 공통 | 조직·사용자 › 사용자 › **사용자 발령 관리** | `ca-org-assignment` | 3 | `http://localhost:8080/page/ca-org-assignment` |
| 공통 | 조직·사용자 › 협력업체 › **협력업체 관리** | `ca-partner` | 3 | `http://localhost:8080/page/ca-partner` |
| 공통 | 조직·사용자 › 협력업체 › **협력업체사용자 관리** | `ca-partner-user` | 3 | `http://localhost:8080/page/ca-partner-user` |
| 공통 | 권한 › 권한 정의 › **권한 관리** | `ca-role` | 3 | `http://localhost:8080/page/ca-role` |
| 공통 | 권한 › 권한 정의 › **공통권한 관리** | `ca-role-common` | 3 | `http://localhost:8080/page/ca-role-common` |
| 공통 | 권한 › 권한 정의 › **발령권한 관리** | `ca-role-rule` | 3 | `http://localhost:8080/page/ca-role-rule` |
| 공통 | 권한 › 권한 부여 › **권한/사용자매핑** | `ca-user-role` | 3 | `http://localhost:8080/page/ca-user-role` |
| 공통 | 권한 › 권한 부여 › **사용자/권한매핑** | `ca-user-role-by-user` | 3 | `http://localhost:8080/page/ca-user-role-by-user` |
| 공통 | 권한 › 권한 부여 › **지정권한 관리** | `ca-role-assign` | 3 | `http://localhost:8080/page/ca-role-assign` |
| 공통 | 권한 › 권한 부여 › **권한위임 관리** | `ca-role-delegation` | 3 | `http://localhost:8080/page/ca-role-delegation` |
| 공통 | 권한 › 권한 부여 › **사용자(모바일)** | `co_mobileAuthMapp` | 3 | `http://localhost:8080/page/co_mobileAuthMapp` |
| 공통 | 권한 › 권한 부여 › **사용자권한 조회** | `ca-user-role-inquiry` | 3 | `http://localhost:8080/page/ca-user-role-inquiry` |
| 공통 | 메뉴·업무그룹 › **업무그룹 관리** | `ca-biz-group` | 3 | `http://localhost:8080/page/ca-biz-group` |
| 공통 | 메뉴·업무그룹 › **메뉴트리관리** | `ca-service` | 3 | `http://localhost:8080/page/ca-service` |
| 공통 | 메뉴·업무그룹 › **권한/메뉴매핑** | `ca-role-service` | 3 | `http://localhost:8080/page/ca-role-service` |
| 공통 | 메뉴·업무그룹 › **매뉴얼** | `ca-manual-viewer` | 3 | `http://localhost:8080/page/ca-manual-viewer` |
| 공통 | 기준정보 › **코드 관리** | `ca-code` | 3 | `http://localhost:8080/page/ca-code` |
| 공통 | 기준정보 › **국가코드 관리** | `ca-country` | 3 | `http://localhost:8080/page/ca-country` |
| 공통 | 기준정보 › **화폐 관리** | `ca-currency` | 3 | `http://localhost:8080/page/ca-currency` |
| 공통 | 기준정보 › **환율 관리** | `ca-exchange-rate` | 3 | `http://localhost:8080/page/ca-exchange-rate` |
| 공통 | 기준정보 › **은행 관리** | `ca-bank` | 3 | `http://localhost:8080/page/ca-bank` |
| 공통 | 기준정보 › **공휴일 관리** | `ca-holiday` | 3 | `http://localhost:8080/page/ca-holiday` |
| 공통 | 기준정보 › **채번 규칙** | `ca-numbering-rule` | 3 | `http://localhost:8080/page/ca-numbering-rule` |
| 공통 | 기준정보 › **결정표** | `ca-decision-table` | 3 | `http://localhost:8080/page/ca-decision-table` |
| 공통 | 결재·문서 › 결재 › **결재 현황** | `ca-approval-dashboard` | 3 | `http://localhost:8080/page/ca-approval-dashboard` |
| 공통 | 결재·문서 › 결재 › **결재 양식 관리** | `ca-approval-form-template` | 3 | `http://localhost:8080/page/ca-approval-form-template` |
| 공통 | 결재·문서 › 결재 › **문서양식관리** | `co_elcDoc` | 3 | `http://localhost:8080/page/co_elcDoc` |
| 공통 | 결재·문서 › 문서 양식 › **문서 유형 관리** | `ca-smart-doc-type` | 3 | `http://localhost:8080/page/ca-smart-doc-type` |
| 공통 | 결재·문서 › 문서 양식 › **문서 템플릿** | `ca-doc-template` | 3 | `http://localhost:8080/page/ca-doc-template` |
| 공통 | 결재·문서 › 문서 양식 › **문서 템플릿 관리** | `ca-smart-doc-template` | 3 | `http://localhost:8080/page/ca-smart-doc-template` |
| 공통 | 결재·문서 › 문서 양식 › **전자 양식 디자이너** | `ca-eform-designer` | 3 | `http://localhost:8080/page/ca-eform-designer` |
| 공통 | 결재·문서 › 문서 관리 › **문서 탐색** | `ca-edms-browse` | 3 | `http://localhost:8080/page/ca-edms-browse` |
| 공통 | 결재·문서 › 문서 관리 › **문서 등록/편집** | `ca-edms-doc-manage` | 3 | `http://localhost:8080/page/ca-edms-doc-manage` |
| 공통 | 결재·문서 › 문서 관리 › **문서 보안 처리** | `ca-common-doc-protect` | 3 | `http://localhost:8080/page/ca-common-doc-protect` |
| 공통 | 결재·문서 › 문서 관리 › **보존 정책 관리** | `ca-retention-policy` | 3 | `http://localhost:8080/page/ca-retention-policy` |
| 공통 | 게시·알림 › 게시 › **게시판관리** | `ca-board-manage` | 3 | `http://localhost:8080/page/ca-board-manage` |
| 공통 | 게시·알림 › 게시 › **문의 관리** | `ca-inquiry` | 3 | `http://localhost:8080/page/ca-inquiry` |
| 공통 | 게시·알림 › 메시지·알림 › **통합 메시지 발송** | `ca-msg-send` | 3 | `http://localhost:8080/page/ca-msg-send` |
| 공통 | 게시·알림 › 메시지·알림 › **메시지템플릿 관리** | `ca-msg-template` | 3 | `http://localhost:8080/page/ca-msg-template` |
| 공통 | 게시·알림 › 메시지·알림 › **메시지 이력** | `ca-msg-history` | 3 | `http://localhost:8080/page/ca-msg-history` |
| 공통 | 게시·알림 › 메시지·알림 › **수신거부 관리** | `ca-msg-opt-out` | 3 | `http://localhost:8080/page/ca-msg-opt-out` |
| 공통 | 게시·알림 › 메시지·알림 › **알림 관리** | `ca-notification-manage` | 3 | `http://localhost:8080/page/ca-notification-manage` |
| 시스템 | 개발도구 › 테스트 › **시험 현황** | `test-dashboard` | 3 | `http://localhost:8080/page/test-dashboard` |
| 시스템 | 개발도구 › 테스트 › **시험 대상** | `test-target` | 3 | `http://localhost:8080/page/test-target` |
| 시스템 | 개발도구 › 테스트 › **시험 사례** | `test-scenario` | 3 | `http://localhost:8080/page/test-scenario` |
| 시스템 | 개발도구 › 테스트 › **시험 묶음** | `test-suite` | 3 | `http://localhost:8080/page/test-suite` |
| 시스템 | 개발도구 › 테스트 › **시험 실행** | `test-run` | 3 | `http://localhost:8080/page/test-run` |
| 시스템 | 개발도구 › 테스트 › **테스트 점검항목** | `test-check-item` | 3 | `http://localhost:8080/page/test-check-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 대상 서비스** | `test-service-item` | 3 | `http://localhost:8080/page/test-service-item` |

## 3. 2026-11-16 문서(`erph-menu-dev01.md`)와 다른 점

| 항목 | 2026-11-16 문서 | 이번 조회 |
|---|---|---|
| 공통 화면 수 | 49 | 48 |
| 시스템 화면 수 | 172 (플랫폼·보안·운영·연동·AI·개발도구 등 폴더 43) | 7 (폴더 2, 개발도구 › 테스트만) |
| 공통 등급 분포 | 전부 1(조회) | 3 48개 |
| 시스템 등급 분포 | 개발도구 33개만 3 | 3 7개 |

- 공통 탭에서 이번에 없는 화면(옛 문서에만 있음): `ca-org-structure`
- 공통 탭에서 이번에 새로 보이는 화면: 없음
- 시스템 탭 화면 7개는 모두 옛 문서의 「개발도구 › 테스트」 안에 있던 것이고, 옛 문서에 있던 나머지 폴더(조직·사용자 ~ AI, 개발도구의 다른 하위 폴더)는 이번에 안 보인다.
- 같은 화면 표시 이름(개발자1 (IT팀))이어도 role·메뉴 권한이 그사이 바뀌었거나, 다른 계정으로 로그인했을 수 있다. login id 가 화면에 안 나와 구분하지 못했다.

## 4. 한계

- 「개인」 탭(마이페이지·결재·문서·소통 등)은 상단 탭 바에 나오지 않아 이번에 읽지 않았다.
- 화면 이름·순서·serviceId·등급은 화면이 쓰는 메뉴 데이터 그대로다. 메뉴 API 응답과 직접 대조하지는 않았다.
- 메뉴 노출만 본 결과다. 화면 안 버튼, 데이터 범위(법인·현장)는 이 문서에 없다.
