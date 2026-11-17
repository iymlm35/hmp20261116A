# erp-h 상단메뉴별 left메뉴 트리 — `h.dev01` 에 role `SYS_ADMIN` 을 부여한 경우 (2026-11-17 조회)

- 조회: 2026-11-17 · `http://localhost:8080/` · 화면 우상단 사용자 표시 「개발자1 (IT팀)」
- 계정·role: `h.dev01` 에 `SYS_ADMIN` 이 부여된 상태의 메뉴다(작성자 확인). 화면에는 login id 가 나오지 않는다. 다른 role 을 함께 가졌는지는 화면에서 확인하지 못했다.
- 방법: Aside 브라우저로 로그인된 탭에서 상단 탭 「공통」·「시스템」을 하나씩 눌러 left메뉴 `.menu-tree` DOM 을 읽었다. 같은 시점에 메뉴 컴포넌트가 그리는 메뉴 데이터(`filteredMenuTree`)의 `label`·`serviceId`·`authLevel` 을 옮겼다. 메뉴 API(`POST /sys/menu/list`)는 호출하지 않았다.
- DOM ↔ 메뉴 데이터 대조: 공통 DOM 화면 3 · 폴더 3 / 데이터 3 · 3, 시스템 DOM 화면 173 · 폴더 43 / 데이터 173 · 43. 두 탭 모두 이름·순서가 일치했다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>`, 등급 순서. 폴더는 `이름/`. 화면 주소는 `/page/<serviceId>` 규칙을 따른 것이며 메뉴를 눌러 주소창을 대조하지는 않았다.
- 등급: 1 조회 · 2 저장 · 3 삭제·특수 (`authLevel`).
- 같은 계정의 다른 role 문서: `erph-menu-dev01[DEVELOPER].md`(추정) · `erph-menu-dev01[BIZ_ADMIN].md`. 차이는 §3.

| 상단 탭 | 폴더 | 화면 | DOM ↔ 데이터 대조 |
|---|---:|---:|---|
| 공통 | 3 | 3 | DOM 3 · 데이터 3 · 일치 |
| 시스템 | 43 | 173 | DOM 173 · 데이터 173 · 일치 |
| 개인 | - | - | 상단 탭에 안 나옴 · 이번에는 확인하지 않음 |

- left메뉴 아래쪽 고정 버튼 「업무 지도」는 메뉴 트리 데이터에 없다.

## 1. 트리

```text
상단메뉴
├── [공통]  key=MENU_BIZ_COMMON (폴더 3 · 화면 3)
│   ├── 조직·사용자/
│   │   └── 조직/
│   │       └── 조직 구조 관리  `ca-org-structure` → `/page/ca-org-structure` · 등급 3
│   └── 기준정보/
│       ├── 채번 규칙  `ca-numbering-rule` → `/page/ca-numbering-rule` · 등급 3
│       └── 결정표  `ca-decision-table` → `/page/ca-decision-table` · 등급 3
└── [시스템]  key=MENU_COMMON_ADMIN (폴더 43 · 화면 173)
    ├── 조직·사용자/
    │   ├── 조직/
    │   │   ├── 회사 관리  `sys-ca-company` → `/page/sys-ca-company` · 등급 3
    │   │   ├── 사업장 관리  `sys-ca-business-place` → `/page/sys-ca-business-place` · 등급 3
    │   │   ├── 부서/조직 정보  `sys-co_deptMng` → `/page/sys-co_deptMng` · 등급 3
    │   │   └── 조직 구조 관리  `sys-ca-org-structure` → `/page/sys-ca-org-structure` · 등급 3
    │   ├── 사용자/
    │   │   ├── 임직원 관리  `sys-ca-employee` → `/page/sys-ca-employee` · 등급 3
    │   │   ├── 사용자 관리  `sys-ca-hmp-user-manage` → `/page/sys-ca-hmp-user-manage` · 등급 3
    │   │   ├── 사용자 법인 소속 관리  `sys-ca-user-company` → `/page/sys-ca-user-company` · 등급 3
    │   │   └── 사용자 발령 관리  `sys-ca-org-assignment` → `/page/sys-ca-org-assignment` · 등급 3
    │   └── 협력업체/
    │       ├── 협력업체 관리  `sys-ca-partner` → `/page/sys-ca-partner` · 등급 3
    │       └── 협력업체사용자 관리  `sys-ca-partner-user` → `/page/sys-ca-partner-user` · 등급 3
    ├── 권한/
    │   ├── 권한 정의/
    │   │   ├── 권한 관리  `sys-ca-role` → `/page/sys-ca-role` · 등급 3
    │   │   ├── 공통권한 관리  `sys-ca-role-common` → `/page/sys-ca-role-common` · 등급 3
    │   │   └── 발령권한 관리  `sys-ca-role-rule` → `/page/sys-ca-role-rule` · 등급 3
    │   └── 권한 부여/
    │       ├── 권한/사용자매핑  `sys-ca-user-role` → `/page/sys-ca-user-role` · 등급 3
    │       ├── 사용자/권한매핑  `sys-ca-user-role-by-user` → `/page/sys-ca-user-role-by-user` · 등급 3
    │       ├── 지정권한 관리  `sys-ca-role-assign` → `/page/sys-ca-role-assign` · 등급 3
    │       ├── 권한위임 관리  `sys-ca-role-delegation` → `/page/sys-ca-role-delegation` · 등급 3
    │       ├── 사용자(모바일)  `sys-co_mobileAuthMapp` → `/page/sys-co_mobileAuthMapp` · 등급 3
    │       └── 사용자권한 조회  `sys-ca-user-role-inquiry` → `/page/sys-ca-user-role-inquiry` · 등급 3
    ├── 메뉴·업무그룹/
    │   ├── 업무그룹 관리  `sys-ca-biz-group` → `/page/sys-ca-biz-group` · 등급 3
    │   ├── 프로그램관리  `ca-program` → `/page/ca-program` · 등급 3
    │   ├── 메뉴트리관리  `sys-ca-service` → `/page/sys-ca-service` · 등급 3
    │   ├── 권한/메뉴매핑  `sys-ca-role-service` → `/page/sys-ca-role-service` · 등급 3
    │   └── 매뉴얼  `sys-ca-manual-viewer` → `/page/sys-ca-manual-viewer` · 등급 3
    ├── 기준정보/
    │   ├── 코드 관리  `sys-ca-code` → `/page/sys-ca-code` · 등급 3
    │   ├── 국가코드 관리  `sys-ca-country` → `/page/sys-ca-country` · 등급 3
    │   ├── 화폐 관리  `sys-ca-currency` → `/page/sys-ca-currency` · 등급 3
    │   ├── 환율 관리  `sys-ca-exchange-rate` → `/page/sys-ca-exchange-rate` · 등급 3
    │   ├── 은행 관리  `sys-ca-bank` → `/page/sys-ca-bank` · 등급 3
    │   ├── 공휴일 관리  `sys-ca-holiday` → `/page/sys-ca-holiday` · 등급 3
    │   ├── 채번 규칙  `sys-ca-numbering-rule` → `/page/sys-ca-numbering-rule` · 등급 3
    │   ├── 코드 등록 요청 관리  `sys-ca-code-request` → `/page/sys-ca-code-request` · 등급 3
    │   └── 결정표  `sys-ca-decision-table` → `/page/sys-ca-decision-table` · 등급 3
    ├── 결재·문서/
    │   ├── 결재/
    │   │   ├── 결재 현황  `sys-ca-approval-dashboard` → `/page/sys-ca-approval-dashboard` · 등급 3
    │   │   ├── 결재 양식 관리  `sys-ca-approval-form-template` → `/page/sys-ca-approval-form-template` · 등급 3
    │   │   └── 문서양식관리  `sys-co_elcDoc` → `/page/sys-co_elcDoc` · 등급 3
    │   ├── 문서 양식/
    │   │   ├── 문서 유형 관리  `sys-ca-smart-doc-type` → `/page/sys-ca-smart-doc-type` · 등급 3
    │   │   ├── 문서 템플릿  `sys-ca-doc-template` → `/page/sys-ca-doc-template` · 등급 3
    │   │   ├── 문서 템플릿 관리  `sys-ca-smart-doc-template` → `/page/sys-ca-smart-doc-template` · 등급 3
    │   │   └── 전자 양식 디자이너  `sys-ca-eform-designer` → `/page/sys-ca-eform-designer` · 등급 3
    │   └── 문서 관리/
    │       ├── 문서 탐색  `sys-ca-edms-browse` → `/page/sys-ca-edms-browse` · 등급 3
    │       ├── 문서 등록/편집  `sys-ca-edms-doc-manage` → `/page/sys-ca-edms-doc-manage` · 등급 3
    │       ├── 문서 보안 처리  `sys-ca-common-doc-protect` → `/page/sys-ca-common-doc-protect` · 등급 3
    │       └── 보존 정책 관리  `sys-ca-retention-policy` → `/page/sys-ca-retention-policy` · 등급 3
    ├── 게시·알림/
    │   ├── 게시/
    │   │   ├── 게시판관리  `sys-ca-board-manage` → `/page/sys-ca-board-manage` · 등급 3
    │   │   └── 문의 관리  `sys-ca-inquiry` → `/page/sys-ca-inquiry` · 등급 3
    │   └── 메시지·알림/
    │       ├── 통합 메시지 발송  `sys-ca-msg-send` → `/page/sys-ca-msg-send` · 등급 3
    │       ├── 메시지템플릿 관리  `sys-ca-msg-template` → `/page/sys-ca-msg-template` · 등급 3
    │       ├── 메시지 이력  `sys-ca-msg-history` → `/page/sys-ca-msg-history` · 등급 3
    │       ├── 수신거부 관리  `sys-ca-msg-opt-out` → `/page/sys-ca-msg-opt-out` · 등급 3
    │       └── 알림 관리  `sys-ca-notification-manage` → `/page/sys-ca-notification-manage` · 등급 3
    ├── 플랫폼/
    │   ├── 시스템·프로그램/
    │   │   ├── 시스템 관리  `ca-system` → `/page/ca-system` · 등급 3
    │   │   ├── 기능모듈 관리  `ca-feature-module` → `/page/ca-feature-module` · 등급 3
    │   │   ├── 기능 플래그  `ca-feature-flag` → `/page/ca-feature-flag` · 등급 3
    │   │   └── 시스템 가이드  `ca-system-guide` → `/page/ca-system-guide` · 등급 3
    │   ├── 설정·정책/
    │   │   ├── 설정 관리  `ca-config` → `/page/ca-config` · 등급 3
    │   │   ├── 데이터소스 관리  `ca-datasource` → `/page/ca-datasource` · 등급 3
    │   │   ├── 데이터캐시정책 관리  `ca-data-cache-policy` → `/page/ca-data-cache-policy` · 등급 3
    │   │   ├── HTTP캐시정책 관리  `ca-http-cache-policy` → `/page/ca-http-cache-policy` · 등급 3
    │   │   ├── HTTP정책 관리  `ca-http-policy` → `/page/ca-http-policy` · 등급 3
    │   │   ├── 페이지정책 관리  `ca-page-policy` → `/page/ca-page-policy` · 등급 3
    │   │   └── 홈 화면 규칙  `ca-home-service-rule` → `/page/ca-home-service-rule` · 등급 3
    │   └── 다국어/
    │       ├── 언어코드 관리  `ca-language` → `/page/ca-language` · 등급 3
    │       ├── 시스템언어 관리  `ca-system-language` → `/page/ca-system-language` · 등급 3
    │       ├── 라벨 관리  `ca-label` → `/page/ca-label` · 등급 3
    │       ├── 메시지 관리  `ca-message` → `/page/ca-message` · 등급 3
    │       └── 포맷정책 관리  `ca-format-policy` → `/page/ca-format-policy` · 등급 3
    ├── 보안/
    │   ├── 인증/
    │   │   ├── 인증정책 관리  `ca-auth-policy` → `/page/ca-auth-policy` · 등급 3
    │   │   ├── 통합 인증 공급자  `ca-auth-provider` → `/page/ca-auth-provider` · 등급 3
    │   │   ├── SCIM 2.0 공급자  `ca-scim-provider` → `/page/ca-scim-provider` · 등급 3
    │   │   ├── 인증항목패턴 관리  `ca-auth-item-pattern` → `/page/ca-auth-item-pattern` · 등급 3
    │   │   ├── 사용자 세션 관리  `ca-user-session` → `/page/ca-user-session` · 등급 3
    │   │   └── 외부 사용자 초대·승인·만료  `ca-external-access` → `/page/ca-external-access` · 등급 3
    │   ├── 접근 통제/
    │   │   ├── 허용IP 관리  `ca-allowed-ip` → `/page/ca-allowed-ip` · 등급 3
    │   │   ├── 내부IP 관리  `ca-internal-ip` → `/page/ca-internal-ip` · 등급 3
    │   │   ├── CORS 허용도메인 관리  `ca-cors-allowed-origin` → `/page/ca-cors-allowed-origin` · 등급 3
    │   │   ├── 접근정책 관리  `ca-network-access-policy` → `/page/ca-network-access-policy` · 등급 3
    │   │   ├── 서비스접근 관리  `ca-service-access` → `/page/ca-service-access` · 등급 3
    │   │   ├── 접근 정책 규칙 관리  `ca-access-rule` → `/page/ca-access-rule` · 등급 3
    │   │   └── 보안정책 관리  `ca-security-policy` → `/page/ca-security-policy` · 등급 3
    │   ├── 권한 정책/
    │   │   ├── ABAC 접근 정책 관리  `ca-access-policy` → `/page/ca-access-policy` · 등급 3
    │   │   ├── ReBAC 권한 튜플 관리  `ca-permission-tuple` → `/page/ca-permission-tuple` · 등급 3
    │   │   ├── 권한 매트릭스  `ca-auth-matrix` → `/page/ca-auth-matrix` · 등급 3
    │   │   └── 권한조절 공급자  `ca-auth-adjust-provider` → `/page/ca-auth-adjust-provider` · 등급 3
    │   └── 개인정보/
    │       ├── 개인정보컬럼 관리  `ca-privacy-column` → `/page/ca-privacy-column` · 등급 3
    │       ├── 개인정보 보존/파기  `ca-privacy-retention` → `/page/ca-privacy-retention` · 등급 3
    │       ├── 개인정보 로그  `ca-log-privacy` → `/page/ca-log-privacy` · 등급 3
    │       └── 암호화 도구  `ca-crypto-tool` → `/page/ca-crypto-tool` · 등급 3
    ├── 운영·로그/
    │   ├── 로그/
    │   │   ├── 오류 로그  `ca-log-error` → `/page/ca-log-error` · 등급 3
    │   │   ├── 로그인 로그  `ca-log-login` → `/page/ca-log-login` · 등급 3
    │   │   ├── 페이지 로그  `ca-log-page` → `/page/ca-log-page` · 등급 3
    │   │   ├── API 로그  `ca-log-service` → `/page/ca-log-service` · 등급 3
    │   │   ├── 파일다운로드 이력  `ca-log-file-download` → `/page/ca-log-file-download` · 등급 3
    │   │   ├── 권한메뉴 로그  `ca-log-role-service` → `/page/ca-log-role-service` · 등급 3
    │   │   ├── 권한부여 로그  `ca-log-role-grant` → `/page/ca-log-role-grant` · 등급 3
    │   │   ├── 로그 대시보드  `ca-log-dashboard` → `/page/ca-log-dashboard` · 등급 3
    │   │   ├── 로그설정 관리  `ca-log-config` → `/page/ca-log-config` · 등급 3
    │   │   └── 로그 파일 뷰어  `ca-log-file-viewer` → `/page/ca-log-file-viewer` · 등급 3
    │   ├── 모니터링/
    │   │   ├── 모니터링 대시보드  `ca-monitoring-dashboard` → `/page/ca-monitoring-dashboard` · 등급 3
    │   │   ├── 성능 인사이트  `ca-performance-insight` → `/page/ca-performance-insight` · 등급 3
    │   │   ├── 모듈 상태  `ca-module-health` → `/page/ca-module-health` · 등급 3
    │   │   └── 화면 모니터링  `ca-screen-viewer` → `/page/ca-screen-viewer` · 등급 3
    │   └── 작업·파일/
    │       ├── 스케줄러 관리  `ca-scheduler` → `/page/ca-scheduler` · 등급 3
    │       ├── 스케줄러 실행이력  `ca-scheduler-history` → `/page/ca-scheduler-history` · 등급 3
    │       ├── AI 스케줄러 운영  `ca-ai-scheduler` → `/page/ca-ai-scheduler` · 등급 3
    │       ├── 대량처리 작업  `ca-excel-job` → `/page/ca-excel-job` · 등급 3
    │       ├── 점검 모드  `ca-maintenance-mode` → `/page/ca-maintenance-mode` · 등급 3
    │       ├── 파일 관리  `ca-attached-file` → `/page/ca-attached-file` · 등급 3
    │       ├── 파일그룹 관리  `ca-file-group` → `/page/ca-file-group` · 등급 3
    │       └── 서버파일 관리  `ca-server-file` → `/page/ca-server-file` · 등급 3
    ├── 연동/
    │   ├── EAI/
    │   │   ├── EAI 어댑터 설정  `ca-eai-adapter` → `/page/ca-eai-adapter` · 등급 3
    │   │   ├── 연계 커넥터  `ca-eai-connector` → `/page/ca-eai-connector` · 등급 3
    │   │   ├── EAI 인터페이스 관리  `ca-eai-interface` → `/page/ca-eai-interface` · 등급 3
    │   │   ├── EAI 실행 이력  `ca-eai-log` → `/page/ca-eai-log` · 등급 3
    │   │   ├── EAI 모니터링  `ca-eai-monitor` → `/page/ca-eai-monitor` · 등급 3
    │   │   └── 외부API 관리  `ca-eai-system` → `/page/ca-eai-system` · 등급 3
    │   ├── 외부 동기화/
    │   │   ├── 외부 동기화 매핑  `ca-sync-mapping` → `/page/ca-sync-mapping` · 등급 3
    │   │   ├── 외부 동기화 스케줄  `ca-sync-schedule` → `/page/ca-sync-schedule` · 등급 3
    │   │   └── 외부 동기화 로그  `ca-sync-log` → `/page/ca-sync-log` · 등급 3
    │   └── 결재·메시지 연동/
    │       ├── GW결재 목록  `ca-gw-approval-list` → `/page/ca-gw-approval-list` · 등급 3
    │       ├── GW결재 콜백 관리  `ca-gw-approval-callback` → `/page/ca-gw-approval-callback` · 등급 3
    │       ├── 결재 체이닝  `ca-approval-chain-manage` → `/page/ca-approval-chain-manage` · 등급 3
    │       ├── 메시지채널 관리  `ca-msg-channel` → `/page/ca-msg-channel` · 등급 3
    │       ├── 웹훅  `ca-webhook` → `/page/ca-webhook` · 등급 3
    │       └── 단축 URL  `ca-short-url` → `/page/ca-short-url` · 등급 3
    ├── 문서 설정/
    │   ├── 문서 편집기 설정  `ca-smart-doc-editor-config` → `/page/ca-smart-doc-editor-config` · 등급 3
    │   ├── 문서 접근 로그  `ca-smart-doc-access-log` → `/page/ca-smart-doc-access-log` · 등급 3
    │   ├── 폐기/아카이브 이력  `ca-smart-doc-disposal-log` → `/page/ca-smart-doc-disposal-log` · 등급 3
    │   ├── 워터마크 설정  `ca-watermark-config` → `/page/ca-watermark-config` · 등급 3
    │   └── DRM설정 관리  `ca-drm-config` → `/page/ca-drm-config` · 등급 3
    ├── AI/
    │   ├── AI 설정/
    │   │   ├── AI 연결  `ca-ai-provider` → `/page/ca-ai-provider` · 등급 3
    │   │   ├── AI 가 볼 수 있는 데이터  `ca-ai-admin` → `/page/ca-ai-admin` · 등급 3
    │   │   ├── 프롬프트 규칙  `ca-ai-prompt-template` → `/page/ca-ai-prompt-template` · 등급 3
    │   │   ├── 스키마 컨텍스트  `ca-ai-schema-context` → `/page/ca-ai-schema-context` · 등급 3
    │   │   ├── 외부 컬럼 매핑  `ca-ai-external-column-map` → `/page/ca-ai-external-column-map` · 등급 3
    │   │   ├── 이상감지 규칙  `ca-ai-anomaly-rule` → `/page/ca-ai-anomaly-rule` · 등급 3
    │   │   ├── AI 사용량·한도  `ca-ai-usage-dashboard` → `/page/ca-ai-usage-dashboard` · 등급 3
    │   │   └── AI 권한 설명기  `ca-ai-access-explainer` → `/page/ca-ai-access-explainer` · 등급 3
    │   └── 에이전트/
    │       ├── AI 계획 승인  `ai-plan-approval` → `/page/ai-plan-approval` · 등급 3
    │       ├── 개선안 보고서  `ai-improvement` → `/page/ai-improvement` · 등급 3
    │       └── 사용자 프로그램 관리  `ca-user-program-admin` → `/page/ca-user-program-admin` · 등급 3
    └── 개발도구/
        ├── 화면·코드 생성/
        │   ├── 화면 등록  `ca-screen-registration` → `/page/ca-screen-registration` · 등급 3
        │   ├── 화면 빌더  `ca-screen-builder` → `/page/ca-screen-builder` · 등급 3
        │   ├── 서버 빌더  `ca-server-builder` → `/page/ca-server-builder` · 등급 3
        │   ├── 화면 소스 편집  `ca-page-source-editor` → `/page/ca-page-source-editor` · 등급 3
        │   ├── 생성 마법사  `ca-scaffold-wizard` → `/page/ca-scaffold-wizard` · 등급 3
        │   ├── 커스텀 컴포넌트 빌더  `ca-custom-component` → `/page/ca-custom-component` · 등급 3
        │   ├── 커스텀 필드 관리  `ca-custom-field` → `/page/ca-custom-field` · 등급 3
        │   ├── 스니펫관리  `ca-snippet-manage` → `/page/ca-snippet-manage` · 등급 3
        │   └── 문서 정의 편집  `document-definition-editor` → `/page/document-definition-editor` · 등급 3
        ├── DB·데이터/
        │   ├── DB 탐색기  `ca-db-explorer` → `/page/ca-db-explorer` · 등급 3
        │   ├── DB 빌더  `ca-db-builder` → `/page/ca-db-builder` · 등급 3
        │   ├── DDL 실행 이력  `ca-ddl-history` → `/page/ca-ddl-history` · 등급 3
        │   ├── 데이터 표준 사전  `ca-data-standard` → `/page/ca-data-standard` · 등급 3
        │   ├── 이력 조회  `ca-entity-version` → `/page/ca-entity-version` · 등급 3
        │   ├── CRUD 매트릭스  `ca-crud-matrix` → `/page/ca-crud-matrix` · 등급 3
        │   └── i18n 스캔  `ca-i18n-scan` → `/page/ca-i18n-scan` · 등급 3
        ├── 테스트/
        │   ├── 시험 현황  `test-dashboard` → `/page/test-dashboard` · 등급 3
        │   ├── 시험 대상  `test-target` → `/page/test-target` · 등급 3
        │   ├── 시험 사례  `test-scenario` → `/page/test-scenario` · 등급 3
        │   ├── 시험 묶음  `test-suite` → `/page/test-suite` · 등급 3
        │   ├── 시험 실행  `test-run` → `/page/test-run` · 등급 3
        │   ├── 테스트 점검항목  `test-check-item` → `/page/test-check-item` · 등급 3
        │   ├── 테스트 대상 서비스  `test-service-item` → `/page/test-service-item` · 등급 3
        │   ├── 테스트 커버리지  `ca-test-coverage` → `/page/ca-test-coverage` · 등급 3
        │   ├── 테스트 데이터 팩토리  `ca-test-data-factory` → `/page/ca-test-data-factory` · 등급 3
        │   └── API 테스트  `ca-api-playground` → `/page/ca-api-playground` · 등급 3
        ├── 운영 도구/
        │   ├── DevOps 콘솔  `ca-devops-console` → `/page/ca-devops-console` · 등급 3
        │   ├── Hotfix 관리  `ca-hotfix` → `/page/ca-hotfix` · 등급 3
        │   ├── 디버그정책 관리  `ca-debug-policy` → `/page/ca-debug-policy` · 등급 3
        │   ├── 개발모듈오버라이드  `ca-dev-module-override` → `/page/ca-dev-module-override` · 등급 3
        │   └── 서비스 의존성  `ca-service-dependency` → `/page/ca-service-dependency` · 등급 3
        └── 확장 개발/
            └── 페이지 확장  `ca-page-extension` → `/page/ca-page-extension` · 등급 3
```

## 2. 화면별 메뉴 경로 표

| 상단 탭 | 메뉴 경로 | serviceId | 등급 | 화면 주소 |
|---|---|---|---:|---|
| 공통 | 조직·사용자 › 조직 › **조직 구조 관리** | `ca-org-structure` | 3 | `http://localhost:8080/page/ca-org-structure` |
| 공통 | 기준정보 › **채번 규칙** | `ca-numbering-rule` | 3 | `http://localhost:8080/page/ca-numbering-rule` |
| 공통 | 기준정보 › **결정표** | `ca-decision-table` | 3 | `http://localhost:8080/page/ca-decision-table` |
| 시스템 | 조직·사용자 › 조직 › **회사 관리** | `sys-ca-company` | 3 | `http://localhost:8080/page/sys-ca-company` |
| 시스템 | 조직·사용자 › 조직 › **사업장 관리** | `sys-ca-business-place` | 3 | `http://localhost:8080/page/sys-ca-business-place` |
| 시스템 | 조직·사용자 › 조직 › **부서/조직 정보** | `sys-co_deptMng` | 3 | `http://localhost:8080/page/sys-co_deptMng` |
| 시스템 | 조직·사용자 › 조직 › **조직 구조 관리** | `sys-ca-org-structure` | 3 | `http://localhost:8080/page/sys-ca-org-structure` |
| 시스템 | 조직·사용자 › 사용자 › **임직원 관리** | `sys-ca-employee` | 3 | `http://localhost:8080/page/sys-ca-employee` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 관리** | `sys-ca-hmp-user-manage` | 3 | `http://localhost:8080/page/sys-ca-hmp-user-manage` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 법인 소속 관리** | `sys-ca-user-company` | 3 | `http://localhost:8080/page/sys-ca-user-company` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 발령 관리** | `sys-ca-org-assignment` | 3 | `http://localhost:8080/page/sys-ca-org-assignment` |
| 시스템 | 조직·사용자 › 협력업체 › **협력업체 관리** | `sys-ca-partner` | 3 | `http://localhost:8080/page/sys-ca-partner` |
| 시스템 | 조직·사용자 › 협력업체 › **협력업체사용자 관리** | `sys-ca-partner-user` | 3 | `http://localhost:8080/page/sys-ca-partner-user` |
| 시스템 | 권한 › 권한 정의 › **권한 관리** | `sys-ca-role` | 3 | `http://localhost:8080/page/sys-ca-role` |
| 시스템 | 권한 › 권한 정의 › **공통권한 관리** | `sys-ca-role-common` | 3 | `http://localhost:8080/page/sys-ca-role-common` |
| 시스템 | 권한 › 권한 정의 › **발령권한 관리** | `sys-ca-role-rule` | 3 | `http://localhost:8080/page/sys-ca-role-rule` |
| 시스템 | 권한 › 권한 부여 › **권한/사용자매핑** | `sys-ca-user-role` | 3 | `http://localhost:8080/page/sys-ca-user-role` |
| 시스템 | 권한 › 권한 부여 › **사용자/권한매핑** | `sys-ca-user-role-by-user` | 3 | `http://localhost:8080/page/sys-ca-user-role-by-user` |
| 시스템 | 권한 › 권한 부여 › **지정권한 관리** | `sys-ca-role-assign` | 3 | `http://localhost:8080/page/sys-ca-role-assign` |
| 시스템 | 권한 › 권한 부여 › **권한위임 관리** | `sys-ca-role-delegation` | 3 | `http://localhost:8080/page/sys-ca-role-delegation` |
| 시스템 | 권한 › 권한 부여 › **사용자(모바일)** | `sys-co_mobileAuthMapp` | 3 | `http://localhost:8080/page/sys-co_mobileAuthMapp` |
| 시스템 | 권한 › 권한 부여 › **사용자권한 조회** | `sys-ca-user-role-inquiry` | 3 | `http://localhost:8080/page/sys-ca-user-role-inquiry` |
| 시스템 | 메뉴·업무그룹 › **업무그룹 관리** | `sys-ca-biz-group` | 3 | `http://localhost:8080/page/sys-ca-biz-group` |
| 시스템 | 메뉴·업무그룹 › **프로그램관리** | `ca-program` | 3 | `http://localhost:8080/page/ca-program` |
| 시스템 | 메뉴·업무그룹 › **메뉴트리관리** | `sys-ca-service` | 3 | `http://localhost:8080/page/sys-ca-service` |
| 시스템 | 메뉴·업무그룹 › **권한/메뉴매핑** | `sys-ca-role-service` | 3 | `http://localhost:8080/page/sys-ca-role-service` |
| 시스템 | 메뉴·업무그룹 › **매뉴얼** | `sys-ca-manual-viewer` | 3 | `http://localhost:8080/page/sys-ca-manual-viewer` |
| 시스템 | 기준정보 › **코드 관리** | `sys-ca-code` | 3 | `http://localhost:8080/page/sys-ca-code` |
| 시스템 | 기준정보 › **국가코드 관리** | `sys-ca-country` | 3 | `http://localhost:8080/page/sys-ca-country` |
| 시스템 | 기준정보 › **화폐 관리** | `sys-ca-currency` | 3 | `http://localhost:8080/page/sys-ca-currency` |
| 시스템 | 기준정보 › **환율 관리** | `sys-ca-exchange-rate` | 3 | `http://localhost:8080/page/sys-ca-exchange-rate` |
| 시스템 | 기준정보 › **은행 관리** | `sys-ca-bank` | 3 | `http://localhost:8080/page/sys-ca-bank` |
| 시스템 | 기준정보 › **공휴일 관리** | `sys-ca-holiday` | 3 | `http://localhost:8080/page/sys-ca-holiday` |
| 시스템 | 기준정보 › **채번 규칙** | `sys-ca-numbering-rule` | 3 | `http://localhost:8080/page/sys-ca-numbering-rule` |
| 시스템 | 기준정보 › **코드 등록 요청 관리** | `sys-ca-code-request` | 3 | `http://localhost:8080/page/sys-ca-code-request` |
| 시스템 | 기준정보 › **결정표** | `sys-ca-decision-table` | 3 | `http://localhost:8080/page/sys-ca-decision-table` |
| 시스템 | 결재·문서 › 결재 › **결재 현황** | `sys-ca-approval-dashboard` | 3 | `http://localhost:8080/page/sys-ca-approval-dashboard` |
| 시스템 | 결재·문서 › 결재 › **결재 양식 관리** | `sys-ca-approval-form-template` | 3 | `http://localhost:8080/page/sys-ca-approval-form-template` |
| 시스템 | 결재·문서 › 결재 › **문서양식관리** | `sys-co_elcDoc` | 3 | `http://localhost:8080/page/sys-co_elcDoc` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 유형 관리** | `sys-ca-smart-doc-type` | 3 | `http://localhost:8080/page/sys-ca-smart-doc-type` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 템플릿** | `sys-ca-doc-template` | 3 | `http://localhost:8080/page/sys-ca-doc-template` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 템플릿 관리** | `sys-ca-smart-doc-template` | 3 | `http://localhost:8080/page/sys-ca-smart-doc-template` |
| 시스템 | 결재·문서 › 문서 양식 › **전자 양식 디자이너** | `sys-ca-eform-designer` | 3 | `http://localhost:8080/page/sys-ca-eform-designer` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 탐색** | `sys-ca-edms-browse` | 3 | `http://localhost:8080/page/sys-ca-edms-browse` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 등록/편집** | `sys-ca-edms-doc-manage` | 3 | `http://localhost:8080/page/sys-ca-edms-doc-manage` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 보안 처리** | `sys-ca-common-doc-protect` | 3 | `http://localhost:8080/page/sys-ca-common-doc-protect` |
| 시스템 | 결재·문서 › 문서 관리 › **보존 정책 관리** | `sys-ca-retention-policy` | 3 | `http://localhost:8080/page/sys-ca-retention-policy` |
| 시스템 | 게시·알림 › 게시 › **게시판관리** | `sys-ca-board-manage` | 3 | `http://localhost:8080/page/sys-ca-board-manage` |
| 시스템 | 게시·알림 › 게시 › **문의 관리** | `sys-ca-inquiry` | 3 | `http://localhost:8080/page/sys-ca-inquiry` |
| 시스템 | 게시·알림 › 메시지·알림 › **통합 메시지 발송** | `sys-ca-msg-send` | 3 | `http://localhost:8080/page/sys-ca-msg-send` |
| 시스템 | 게시·알림 › 메시지·알림 › **메시지템플릿 관리** | `sys-ca-msg-template` | 3 | `http://localhost:8080/page/sys-ca-msg-template` |
| 시스템 | 게시·알림 › 메시지·알림 › **메시지 이력** | `sys-ca-msg-history` | 3 | `http://localhost:8080/page/sys-ca-msg-history` |
| 시스템 | 게시·알림 › 메시지·알림 › **수신거부 관리** | `sys-ca-msg-opt-out` | 3 | `http://localhost:8080/page/sys-ca-msg-opt-out` |
| 시스템 | 게시·알림 › 메시지·알림 › **알림 관리** | `sys-ca-notification-manage` | 3 | `http://localhost:8080/page/sys-ca-notification-manage` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **시스템 관리** | `ca-system` | 3 | `http://localhost:8080/page/ca-system` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **기능모듈 관리** | `ca-feature-module` | 3 | `http://localhost:8080/page/ca-feature-module` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **기능 플래그** | `ca-feature-flag` | 3 | `http://localhost:8080/page/ca-feature-flag` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **시스템 가이드** | `ca-system-guide` | 3 | `http://localhost:8080/page/ca-system-guide` |
| 시스템 | 플랫폼 › 설정·정책 › **설정 관리** | `ca-config` | 3 | `http://localhost:8080/page/ca-config` |
| 시스템 | 플랫폼 › 설정·정책 › **데이터소스 관리** | `ca-datasource` | 3 | `http://localhost:8080/page/ca-datasource` |
| 시스템 | 플랫폼 › 설정·정책 › **데이터캐시정책 관리** | `ca-data-cache-policy` | 3 | `http://localhost:8080/page/ca-data-cache-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **HTTP캐시정책 관리** | `ca-http-cache-policy` | 3 | `http://localhost:8080/page/ca-http-cache-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **HTTP정책 관리** | `ca-http-policy` | 3 | `http://localhost:8080/page/ca-http-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **페이지정책 관리** | `ca-page-policy` | 3 | `http://localhost:8080/page/ca-page-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **홈 화면 규칙** | `ca-home-service-rule` | 3 | `http://localhost:8080/page/ca-home-service-rule` |
| 시스템 | 플랫폼 › 다국어 › **언어코드 관리** | `ca-language` | 3 | `http://localhost:8080/page/ca-language` |
| 시스템 | 플랫폼 › 다국어 › **시스템언어 관리** | `ca-system-language` | 3 | `http://localhost:8080/page/ca-system-language` |
| 시스템 | 플랫폼 › 다국어 › **라벨 관리** | `ca-label` | 3 | `http://localhost:8080/page/ca-label` |
| 시스템 | 플랫폼 › 다국어 › **메시지 관리** | `ca-message` | 3 | `http://localhost:8080/page/ca-message` |
| 시스템 | 플랫폼 › 다국어 › **포맷정책 관리** | `ca-format-policy` | 3 | `http://localhost:8080/page/ca-format-policy` |
| 시스템 | 보안 › 인증 › **인증정책 관리** | `ca-auth-policy` | 3 | `http://localhost:8080/page/ca-auth-policy` |
| 시스템 | 보안 › 인증 › **통합 인증 공급자** | `ca-auth-provider` | 3 | `http://localhost:8080/page/ca-auth-provider` |
| 시스템 | 보안 › 인증 › **SCIM 2.0 공급자** | `ca-scim-provider` | 3 | `http://localhost:8080/page/ca-scim-provider` |
| 시스템 | 보안 › 인증 › **인증항목패턴 관리** | `ca-auth-item-pattern` | 3 | `http://localhost:8080/page/ca-auth-item-pattern` |
| 시스템 | 보안 › 인증 › **사용자 세션 관리** | `ca-user-session` | 3 | `http://localhost:8080/page/ca-user-session` |
| 시스템 | 보안 › 인증 › **외부 사용자 초대·승인·만료** | `ca-external-access` | 3 | `http://localhost:8080/page/ca-external-access` |
| 시스템 | 보안 › 접근 통제 › **허용IP 관리** | `ca-allowed-ip` | 3 | `http://localhost:8080/page/ca-allowed-ip` |
| 시스템 | 보안 › 접근 통제 › **내부IP 관리** | `ca-internal-ip` | 3 | `http://localhost:8080/page/ca-internal-ip` |
| 시스템 | 보안 › 접근 통제 › **CORS 허용도메인 관리** | `ca-cors-allowed-origin` | 3 | `http://localhost:8080/page/ca-cors-allowed-origin` |
| 시스템 | 보안 › 접근 통제 › **접근정책 관리** | `ca-network-access-policy` | 3 | `http://localhost:8080/page/ca-network-access-policy` |
| 시스템 | 보안 › 접근 통제 › **서비스접근 관리** | `ca-service-access` | 3 | `http://localhost:8080/page/ca-service-access` |
| 시스템 | 보안 › 접근 통제 › **접근 정책 규칙 관리** | `ca-access-rule` | 3 | `http://localhost:8080/page/ca-access-rule` |
| 시스템 | 보안 › 접근 통제 › **보안정책 관리** | `ca-security-policy` | 3 | `http://localhost:8080/page/ca-security-policy` |
| 시스템 | 보안 › 권한 정책 › **ABAC 접근 정책 관리** | `ca-access-policy` | 3 | `http://localhost:8080/page/ca-access-policy` |
| 시스템 | 보안 › 권한 정책 › **ReBAC 권한 튜플 관리** | `ca-permission-tuple` | 3 | `http://localhost:8080/page/ca-permission-tuple` |
| 시스템 | 보안 › 권한 정책 › **권한 매트릭스** | `ca-auth-matrix` | 3 | `http://localhost:8080/page/ca-auth-matrix` |
| 시스템 | 보안 › 권한 정책 › **권한조절 공급자** | `ca-auth-adjust-provider` | 3 | `http://localhost:8080/page/ca-auth-adjust-provider` |
| 시스템 | 보안 › 개인정보 › **개인정보컬럼 관리** | `ca-privacy-column` | 3 | `http://localhost:8080/page/ca-privacy-column` |
| 시스템 | 보안 › 개인정보 › **개인정보 보존/파기** | `ca-privacy-retention` | 3 | `http://localhost:8080/page/ca-privacy-retention` |
| 시스템 | 보안 › 개인정보 › **개인정보 로그** | `ca-log-privacy` | 3 | `http://localhost:8080/page/ca-log-privacy` |
| 시스템 | 보안 › 개인정보 › **암호화 도구** | `ca-crypto-tool` | 3 | `http://localhost:8080/page/ca-crypto-tool` |
| 시스템 | 운영·로그 › 로그 › **오류 로그** | `ca-log-error` | 3 | `http://localhost:8080/page/ca-log-error` |
| 시스템 | 운영·로그 › 로그 › **로그인 로그** | `ca-log-login` | 3 | `http://localhost:8080/page/ca-log-login` |
| 시스템 | 운영·로그 › 로그 › **페이지 로그** | `ca-log-page` | 3 | `http://localhost:8080/page/ca-log-page` |
| 시스템 | 운영·로그 › 로그 › **API 로그** | `ca-log-service` | 3 | `http://localhost:8080/page/ca-log-service` |
| 시스템 | 운영·로그 › 로그 › **파일다운로드 이력** | `ca-log-file-download` | 3 | `http://localhost:8080/page/ca-log-file-download` |
| 시스템 | 운영·로그 › 로그 › **권한메뉴 로그** | `ca-log-role-service` | 3 | `http://localhost:8080/page/ca-log-role-service` |
| 시스템 | 운영·로그 › 로그 › **권한부여 로그** | `ca-log-role-grant` | 3 | `http://localhost:8080/page/ca-log-role-grant` |
| 시스템 | 운영·로그 › 로그 › **로그 대시보드** | `ca-log-dashboard` | 3 | `http://localhost:8080/page/ca-log-dashboard` |
| 시스템 | 운영·로그 › 로그 › **로그설정 관리** | `ca-log-config` | 3 | `http://localhost:8080/page/ca-log-config` |
| 시스템 | 운영·로그 › 로그 › **로그 파일 뷰어** | `ca-log-file-viewer` | 3 | `http://localhost:8080/page/ca-log-file-viewer` |
| 시스템 | 운영·로그 › 모니터링 › **모니터링 대시보드** | `ca-monitoring-dashboard` | 3 | `http://localhost:8080/page/ca-monitoring-dashboard` |
| 시스템 | 운영·로그 › 모니터링 › **성능 인사이트** | `ca-performance-insight` | 3 | `http://localhost:8080/page/ca-performance-insight` |
| 시스템 | 운영·로그 › 모니터링 › **모듈 상태** | `ca-module-health` | 3 | `http://localhost:8080/page/ca-module-health` |
| 시스템 | 운영·로그 › 모니터링 › **화면 모니터링** | `ca-screen-viewer` | 3 | `http://localhost:8080/page/ca-screen-viewer` |
| 시스템 | 운영·로그 › 작업·파일 › **스케줄러 관리** | `ca-scheduler` | 3 | `http://localhost:8080/page/ca-scheduler` |
| 시스템 | 운영·로그 › 작업·파일 › **스케줄러 실행이력** | `ca-scheduler-history` | 3 | `http://localhost:8080/page/ca-scheduler-history` |
| 시스템 | 운영·로그 › 작업·파일 › **AI 스케줄러 운영** | `ca-ai-scheduler` | 3 | `http://localhost:8080/page/ca-ai-scheduler` |
| 시스템 | 운영·로그 › 작업·파일 › **대량처리 작업** | `ca-excel-job` | 3 | `http://localhost:8080/page/ca-excel-job` |
| 시스템 | 운영·로그 › 작업·파일 › **점검 모드** | `ca-maintenance-mode` | 3 | `http://localhost:8080/page/ca-maintenance-mode` |
| 시스템 | 운영·로그 › 작업·파일 › **파일 관리** | `ca-attached-file` | 3 | `http://localhost:8080/page/ca-attached-file` |
| 시스템 | 운영·로그 › 작업·파일 › **파일그룹 관리** | `ca-file-group` | 3 | `http://localhost:8080/page/ca-file-group` |
| 시스템 | 운영·로그 › 작업·파일 › **서버파일 관리** | `ca-server-file` | 3 | `http://localhost:8080/page/ca-server-file` |
| 시스템 | 연동 › EAI › **EAI 어댑터 설정** | `ca-eai-adapter` | 3 | `http://localhost:8080/page/ca-eai-adapter` |
| 시스템 | 연동 › EAI › **연계 커넥터** | `ca-eai-connector` | 3 | `http://localhost:8080/page/ca-eai-connector` |
| 시스템 | 연동 › EAI › **EAI 인터페이스 관리** | `ca-eai-interface` | 3 | `http://localhost:8080/page/ca-eai-interface` |
| 시스템 | 연동 › EAI › **EAI 실행 이력** | `ca-eai-log` | 3 | `http://localhost:8080/page/ca-eai-log` |
| 시스템 | 연동 › EAI › **EAI 모니터링** | `ca-eai-monitor` | 3 | `http://localhost:8080/page/ca-eai-monitor` |
| 시스템 | 연동 › EAI › **외부API 관리** | `ca-eai-system` | 3 | `http://localhost:8080/page/ca-eai-system` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 매핑** | `ca-sync-mapping` | 3 | `http://localhost:8080/page/ca-sync-mapping` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 스케줄** | `ca-sync-schedule` | 3 | `http://localhost:8080/page/ca-sync-schedule` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 로그** | `ca-sync-log` | 3 | `http://localhost:8080/page/ca-sync-log` |
| 시스템 | 연동 › 결재·메시지 연동 › **GW결재 목록** | `ca-gw-approval-list` | 3 | `http://localhost:8080/page/ca-gw-approval-list` |
| 시스템 | 연동 › 결재·메시지 연동 › **GW결재 콜백 관리** | `ca-gw-approval-callback` | 3 | `http://localhost:8080/page/ca-gw-approval-callback` |
| 시스템 | 연동 › 결재·메시지 연동 › **결재 체이닝** | `ca-approval-chain-manage` | 3 | `http://localhost:8080/page/ca-approval-chain-manage` |
| 시스템 | 연동 › 결재·메시지 연동 › **메시지채널 관리** | `ca-msg-channel` | 3 | `http://localhost:8080/page/ca-msg-channel` |
| 시스템 | 연동 › 결재·메시지 연동 › **웹훅** | `ca-webhook` | 3 | `http://localhost:8080/page/ca-webhook` |
| 시스템 | 연동 › 결재·메시지 연동 › **단축 URL** | `ca-short-url` | 3 | `http://localhost:8080/page/ca-short-url` |
| 시스템 | 문서 설정 › **문서 편집기 설정** | `ca-smart-doc-editor-config` | 3 | `http://localhost:8080/page/ca-smart-doc-editor-config` |
| 시스템 | 문서 설정 › **문서 접근 로그** | `ca-smart-doc-access-log` | 3 | `http://localhost:8080/page/ca-smart-doc-access-log` |
| 시스템 | 문서 설정 › **폐기/아카이브 이력** | `ca-smart-doc-disposal-log` | 3 | `http://localhost:8080/page/ca-smart-doc-disposal-log` |
| 시스템 | 문서 설정 › **워터마크 설정** | `ca-watermark-config` | 3 | `http://localhost:8080/page/ca-watermark-config` |
| 시스템 | 문서 설정 › **DRM설정 관리** | `ca-drm-config` | 3 | `http://localhost:8080/page/ca-drm-config` |
| 시스템 | AI › AI 설정 › **AI 연결** | `ca-ai-provider` | 3 | `http://localhost:8080/page/ca-ai-provider` |
| 시스템 | AI › AI 설정 › **AI 가 볼 수 있는 데이터** | `ca-ai-admin` | 3 | `http://localhost:8080/page/ca-ai-admin` |
| 시스템 | AI › AI 설정 › **프롬프트 규칙** | `ca-ai-prompt-template` | 3 | `http://localhost:8080/page/ca-ai-prompt-template` |
| 시스템 | AI › AI 설정 › **스키마 컨텍스트** | `ca-ai-schema-context` | 3 | `http://localhost:8080/page/ca-ai-schema-context` |
| 시스템 | AI › AI 설정 › **외부 컬럼 매핑** | `ca-ai-external-column-map` | 3 | `http://localhost:8080/page/ca-ai-external-column-map` |
| 시스템 | AI › AI 설정 › **이상감지 규칙** | `ca-ai-anomaly-rule` | 3 | `http://localhost:8080/page/ca-ai-anomaly-rule` |
| 시스템 | AI › AI 설정 › **AI 사용량·한도** | `ca-ai-usage-dashboard` | 3 | `http://localhost:8080/page/ca-ai-usage-dashboard` |
| 시스템 | AI › AI 설정 › **AI 권한 설명기** | `ca-ai-access-explainer` | 3 | `http://localhost:8080/page/ca-ai-access-explainer` |
| 시스템 | AI › 에이전트 › **AI 계획 승인** | `ai-plan-approval` | 3 | `http://localhost:8080/page/ai-plan-approval` |
| 시스템 | AI › 에이전트 › **개선안 보고서** | `ai-improvement` | 3 | `http://localhost:8080/page/ai-improvement` |
| 시스템 | AI › 에이전트 › **사용자 프로그램 관리** | `ca-user-program-admin` | 3 | `http://localhost:8080/page/ca-user-program-admin` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 등록** | `ca-screen-registration` | 3 | `http://localhost:8080/page/ca-screen-registration` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 빌더** | `ca-screen-builder` | 3 | `http://localhost:8080/page/ca-screen-builder` |
| 시스템 | 개발도구 › 화면·코드 생성 › **서버 빌더** | `ca-server-builder` | 3 | `http://localhost:8080/page/ca-server-builder` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 소스 편집** | `ca-page-source-editor` | 3 | `http://localhost:8080/page/ca-page-source-editor` |
| 시스템 | 개발도구 › 화면·코드 생성 › **생성 마법사** | `ca-scaffold-wizard` | 3 | `http://localhost:8080/page/ca-scaffold-wizard` |
| 시스템 | 개발도구 › 화면·코드 생성 › **커스텀 컴포넌트 빌더** | `ca-custom-component` | 3 | `http://localhost:8080/page/ca-custom-component` |
| 시스템 | 개발도구 › 화면·코드 생성 › **커스텀 필드 관리** | `ca-custom-field` | 3 | `http://localhost:8080/page/ca-custom-field` |
| 시스템 | 개발도구 › 화면·코드 생성 › **스니펫관리** | `ca-snippet-manage` | 3 | `http://localhost:8080/page/ca-snippet-manage` |
| 시스템 | 개발도구 › 화면·코드 생성 › **문서 정의 편집** | `document-definition-editor` | 3 | `http://localhost:8080/page/document-definition-editor` |
| 시스템 | 개발도구 › DB·데이터 › **DB 탐색기** | `ca-db-explorer` | 3 | `http://localhost:8080/page/ca-db-explorer` |
| 시스템 | 개발도구 › DB·데이터 › **DB 빌더** | `ca-db-builder` | 3 | `http://localhost:8080/page/ca-db-builder` |
| 시스템 | 개발도구 › DB·데이터 › **DDL 실행 이력** | `ca-ddl-history` | 3 | `http://localhost:8080/page/ca-ddl-history` |
| 시스템 | 개발도구 › DB·데이터 › **데이터 표준 사전** | `ca-data-standard` | 3 | `http://localhost:8080/page/ca-data-standard` |
| 시스템 | 개발도구 › DB·데이터 › **이력 조회** | `ca-entity-version` | 3 | `http://localhost:8080/page/ca-entity-version` |
| 시스템 | 개발도구 › DB·데이터 › **CRUD 매트릭스** | `ca-crud-matrix` | 3 | `http://localhost:8080/page/ca-crud-matrix` |
| 시스템 | 개발도구 › DB·데이터 › **i18n 스캔** | `ca-i18n-scan` | 3 | `http://localhost:8080/page/ca-i18n-scan` |
| 시스템 | 개발도구 › 테스트 › **시험 현황** | `test-dashboard` | 3 | `http://localhost:8080/page/test-dashboard` |
| 시스템 | 개발도구 › 테스트 › **시험 대상** | `test-target` | 3 | `http://localhost:8080/page/test-target` |
| 시스템 | 개발도구 › 테스트 › **시험 사례** | `test-scenario` | 3 | `http://localhost:8080/page/test-scenario` |
| 시스템 | 개발도구 › 테스트 › **시험 묶음** | `test-suite` | 3 | `http://localhost:8080/page/test-suite` |
| 시스템 | 개발도구 › 테스트 › **시험 실행** | `test-run` | 3 | `http://localhost:8080/page/test-run` |
| 시스템 | 개발도구 › 테스트 › **테스트 점검항목** | `test-check-item` | 3 | `http://localhost:8080/page/test-check-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 대상 서비스** | `test-service-item` | 3 | `http://localhost:8080/page/test-service-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 커버리지** | `ca-test-coverage` | 3 | `http://localhost:8080/page/ca-test-coverage` |
| 시스템 | 개발도구 › 테스트 › **테스트 데이터 팩토리** | `ca-test-data-factory` | 3 | `http://localhost:8080/page/ca-test-data-factory` |
| 시스템 | 개발도구 › 테스트 › **API 테스트** | `ca-api-playground` | 3 | `http://localhost:8080/page/ca-api-playground` |
| 시스템 | 개발도구 › 운영 도구 › **DevOps 콘솔** | `ca-devops-console` | 3 | `http://localhost:8080/page/ca-devops-console` |
| 시스템 | 개발도구 › 운영 도구 › **Hotfix 관리** | `ca-hotfix` | 3 | `http://localhost:8080/page/ca-hotfix` |
| 시스템 | 개발도구 › 운영 도구 › **디버그정책 관리** | `ca-debug-policy` | 3 | `http://localhost:8080/page/ca-debug-policy` |
| 시스템 | 개발도구 › 운영 도구 › **개발모듈오버라이드** | `ca-dev-module-override` | 3 | `http://localhost:8080/page/ca-dev-module-override` |
| 시스템 | 개발도구 › 운영 도구 › **서비스 의존성** | `ca-service-dependency` | 3 | `http://localhost:8080/page/ca-service-dependency` |
| 시스템 | 개발도구 › 확장 개발 › **페이지 확장** | `ca-page-extension` | 3 | `http://localhost:8080/page/ca-page-extension` |

## 3. 다른 role 문서와 다른 점

| 항목 | `DEVELOPER` 추정 (2026-11-16) | `BIZ_ADMIN` 만 (2026-11-17) | `SYS_ADMIN` (이번) |
|---|---|---|---|
| 공통 화면 수 | 49 | 48 | 3 |
| 시스템 화면 수 | 172 | 7 | 173 (폴더 43) |
| 공통 등급 분포 | 전부 1(조회) | 3 48개 | 3 3개 |
| 시스템 등급 분포 | 개발도구 33개만 3 | 3 7개 | 3 173개 |

- `SYS_ADMIN` 공통 탭 3개는 `ca-decision-table`, `ca-numbering-rule`, `ca-org-structure` 이다. `BIZ_ADMIN` 은 공통 48개를 열어 주었다. 이번 3개 중 `BIZ_ADMIN` 에 없는 것은 `ca-org-structure` 이고, 이번에 없는 `BIZ_ADMIN` 화면은 46개다.
- 시스템 탭: `DEVELOPER` 문서에만 있는 화면 0개 · 이번에만 있는 화면 1개 · 둘 다 있는 화면 172개.
  - 이번에만 있는 화면: `sys-ca-code-request`
  - `DEVELOPER` 문서에만 있는 화면: 없음
- `BIZ_ADMIN` 문서의 시스템 탭 7개는 모두 이번 `SYS_ADMIN` 시스템 탭에도 있다: 예.
- 위 비교의 `DEVELOPER` 쪽 role 은 시험 계정 문서 기준 추정이다. 확인한 값이 아니다.

## 4. 한계

- 「개인」 탭(마이페이지·결재·문서·소통 등)은 상단 탭 바에 나오지 않아 이번에 읽지 않았다.
- 화면 이름·순서·serviceId·등급은 화면이 쓰는 메뉴 데이터 그대로다. 메뉴 API 응답과 직접 대조하지는 않았다.
- 메뉴 노출만 본 결과다. 화면 안 버튼, 데이터 범위(법인·현장)는 이 문서에 없다.
