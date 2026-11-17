# erpadmin ↔ dev01[SYS_ADMIN] 메뉴 비교 (공통·시스템)

`erph-menu-erpadmin.md`(2026-11-16) ↔ `erph-menu-dev01[SYS_ADMIN].md`(2026-11-17). serviceId 기준.

## 1. 메뉴 개수

| 상단 탭 | erpadmin | dev01[SYS_ADMIN] | 둘 다 | erpadmin에만 | dev01[SYS_ADMIN]에만 |
|---|---:|---:|---:|---:|---:|
| 공통 | 49 | 3 | 3 | 46 | 0 |
| 시스템 | 172 | 173 | 172 | 0 | 1 |

## 2. dev01[SYS_ADMIN]에만 있는 메뉴

| 상단 탭 | 메뉴 경로 | serviceId |
|---|---|---|
| 시스템 | 기준정보 › **코드 등록 요청 관리** | `sys-ca-code-request` |

## 3. erpadmin에만 있는 메뉴

| 상단 탭 | 메뉴 경로 | serviceId |
|---|---|---|
| 공통 | 조직·사용자 › 조직 › **회사 관리** | `ca-company` |
| 공통 | 조직·사용자 › 조직 › **사업장 관리** | `ca-business-place` |
| 공통 | 조직·사용자 › 조직 › **부서/조직 정보** | `co_deptMng` |
| 공통 | 조직·사용자 › 사용자 › **임직원 관리** | `ca-employee` |
| 공통 | 조직·사용자 › 사용자 › **사용자 관리** | `ca-hmp-user-manage` |
| 공통 | 조직·사용자 › 사용자 › **사용자 법인 소속 관리** | `ca-user-company` |
| 공통 | 조직·사용자 › 사용자 › **사용자 발령 관리** | `ca-org-assignment` |
| 공통 | 조직·사용자 › 협력업체 › **협력업체 관리** | `ca-partner` |
| 공통 | 조직·사용자 › 협력업체 › **협력업체사용자 관리** | `ca-partner-user` |
| 공통 | 권한 › 권한 정의 › **권한 관리** | `ca-role` |
| 공통 | 권한 › 권한 정의 › **공통권한 관리** | `ca-role-common` |
| 공통 | 권한 › 권한 정의 › **발령권한 관리** | `ca-role-rule` |
| 공통 | 권한 › 권한 부여 › **권한/사용자매핑** | `ca-user-role` |
| 공통 | 권한 › 권한 부여 › **사용자/권한매핑** | `ca-user-role-by-user` |
| 공통 | 권한 › 권한 부여 › **지정권한 관리** | `ca-role-assign` |
| 공통 | 권한 › 권한 부여 › **권한위임 관리** | `ca-role-delegation` |
| 공통 | 권한 › 권한 부여 › **사용자(모바일)** | `co_mobileAuthMapp` |
| 공통 | 권한 › 권한 부여 › **사용자권한 조회** | `ca-user-role-inquiry` |
| 공통 | 메뉴·업무그룹 › **업무그룹 관리** | `ca-biz-group` |
| 공통 | 메뉴·업무그룹 › **메뉴트리관리** | `ca-service` |
| 공통 | 메뉴·업무그룹 › **권한/메뉴매핑** | `ca-role-service` |
| 공통 | 메뉴·업무그룹 › **매뉴얼** | `ca-manual-viewer` |
| 공통 | 기준정보 › **코드 관리** | `ca-code` |
| 공통 | 기준정보 › **국가코드 관리** | `ca-country` |
| 공통 | 기준정보 › **화폐 관리** | `ca-currency` |
| 공통 | 기준정보 › **환율 관리** | `ca-exchange-rate` |
| 공통 | 기준정보 › **은행 관리** | `ca-bank` |
| 공통 | 기준정보 › **공휴일 관리** | `ca-holiday` |
| 공통 | 결재·문서 › 결재 › **결재 현황** | `ca-approval-dashboard` |
| 공통 | 결재·문서 › 결재 › **결재 양식 관리** | `ca-approval-form-template` |
| 공통 | 결재·문서 › 결재 › **문서양식관리** | `co_elcDoc` |
| 공통 | 결재·문서 › 문서 양식 › **문서 유형 관리** | `ca-smart-doc-type` |
| 공통 | 결재·문서 › 문서 양식 › **문서 템플릿** | `ca-doc-template` |
| 공통 | 결재·문서 › 문서 양식 › **문서 템플릿 관리** | `ca-smart-doc-template` |
| 공통 | 결재·문서 › 문서 양식 › **전자 양식 디자이너** | `ca-eform-designer` |
| 공통 | 결재·문서 › 문서 관리 › **문서 탐색** | `ca-edms-browse` |
| 공통 | 결재·문서 › 문서 관리 › **문서 등록/편집** | `ca-edms-doc-manage` |
| 공통 | 결재·문서 › 문서 관리 › **문서 보안 처리** | `ca-common-doc-protect` |
| 공통 | 결재·문서 › 문서 관리 › **보존 정책 관리** | `ca-retention-policy` |
| 공통 | 게시·알림 › 게시 › **게시판관리** | `ca-board-manage` |
| 공통 | 게시·알림 › 게시 › **문의 관리** | `ca-inquiry` |
| 공통 | 게시·알림 › 메시지·알림 › **통합 메시지 발송** | `ca-msg-send` |
| 공통 | 게시·알림 › 메시지·알림 › **메시지템플릿 관리** | `ca-msg-template` |
| 공통 | 게시·알림 › 메시지·알림 › **메시지 이력** | `ca-msg-history` |
| 공통 | 게시·알림 › 메시지·알림 › **수신거부 관리** | `ca-msg-opt-out` |
| 공통 | 게시·알림 › 메시지·알림 › **알림 관리** | `ca-notification-manage` |


## 4. 둘 다 있는 메뉴

| 상단 탭 | 메뉴 경로 | serviceId |
|---|---|---|
| 공통 | 조직·사용자 › 조직 › **조직 구조 관리** | `ca-org-structure` |
| 공통 | 기준정보 › **채번 규칙** | `ca-numbering-rule` |
| 공통 | 기준정보 › **결정표** | `ca-decision-table` |
| 시스템 | 조직·사용자 › 조직 › **회사 관리** | `sys-ca-company` |
| 시스템 | 조직·사용자 › 조직 › **사업장 관리** | `sys-ca-business-place` |
| 시스템 | 조직·사용자 › 조직 › **부서/조직 정보** | `sys-co_deptMng` |
| 시스템 | 조직·사용자 › 조직 › **조직 구조 관리** | `sys-ca-org-structure` |
| 시스템 | 조직·사용자 › 사용자 › **임직원 관리** | `sys-ca-employee` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 관리** | `sys-ca-hmp-user-manage` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 법인 소속 관리** | `sys-ca-user-company` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 발령 관리** | `sys-ca-org-assignment` |
| 시스템 | 조직·사용자 › 협력업체 › **협력업체 관리** | `sys-ca-partner` |
| 시스템 | 조직·사용자 › 협력업체 › **협력업체사용자 관리** | `sys-ca-partner-user` |
| 시스템 | 권한 › 권한 정의 › **권한 관리** | `sys-ca-role` |
| 시스템 | 권한 › 권한 정의 › **공통권한 관리** | `sys-ca-role-common` |
| 시스템 | 권한 › 권한 정의 › **발령권한 관리** | `sys-ca-role-rule` |
| 시스템 | 권한 › 권한 부여 › **권한/사용자매핑** | `sys-ca-user-role` |
| 시스템 | 권한 › 권한 부여 › **사용자/권한매핑** | `sys-ca-user-role-by-user` |
| 시스템 | 권한 › 권한 부여 › **지정권한 관리** | `sys-ca-role-assign` |
| 시스템 | 권한 › 권한 부여 › **권한위임 관리** | `sys-ca-role-delegation` |
| 시스템 | 권한 › 권한 부여 › **사용자(모바일)** | `sys-co_mobileAuthMapp` |
| 시스템 | 권한 › 권한 부여 › **사용자권한 조회** | `sys-ca-user-role-inquiry` |
| 시스템 | 메뉴·업무그룹 › **업무그룹 관리** | `sys-ca-biz-group` |
| 시스템 | 메뉴·업무그룹 › **프로그램관리** | `ca-program` |
| 시스템 | 메뉴·업무그룹 › **메뉴트리관리** | `sys-ca-service` |
| 시스템 | 메뉴·업무그룹 › **권한/메뉴매핑** | `sys-ca-role-service` |
| 시스템 | 메뉴·업무그룹 › **매뉴얼** | `sys-ca-manual-viewer` |
| 시스템 | 기준정보 › **코드 관리** | `sys-ca-code` |
| 시스템 | 기준정보 › **국가코드 관리** | `sys-ca-country` |
| 시스템 | 기준정보 › **화폐 관리** | `sys-ca-currency` |
| 시스템 | 기준정보 › **환율 관리** | `sys-ca-exchange-rate` |
| 시스템 | 기준정보 › **은행 관리** | `sys-ca-bank` |
| 시스템 | 기준정보 › **공휴일 관리** | `sys-ca-holiday` |
| 시스템 | 기준정보 › **채번 규칙** | `sys-ca-numbering-rule` |
| 시스템 | 기준정보 › **결정표** | `sys-ca-decision-table` |
| 시스템 | 결재·문서 › 결재 › **결재 현황** | `sys-ca-approval-dashboard` |
| 시스템 | 결재·문서 › 결재 › **결재 양식 관리** | `sys-ca-approval-form-template` |
| 시스템 | 결재·문서 › 결재 › **문서양식관리** | `sys-co_elcDoc` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 유형 관리** | `sys-ca-smart-doc-type` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 템플릿** | `sys-ca-doc-template` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 템플릿 관리** | `sys-ca-smart-doc-template` |
| 시스템 | 결재·문서 › 문서 양식 › **전자 양식 디자이너** | `sys-ca-eform-designer` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 탐색** | `sys-ca-edms-browse` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 등록/편집** | `sys-ca-edms-doc-manage` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 보안 처리** | `sys-ca-common-doc-protect` |
| 시스템 | 결재·문서 › 문서 관리 › **보존 정책 관리** | `sys-ca-retention-policy` |
| 시스템 | 게시·알림 › 게시 › **게시판관리** | `sys-ca-board-manage` |
| 시스템 | 게시·알림 › 게시 › **문의 관리** | `sys-ca-inquiry` |
| 시스템 | 게시·알림 › 메시지·알림 › **통합 메시지 발송** | `sys-ca-msg-send` |
| 시스템 | 게시·알림 › 메시지·알림 › **메시지템플릿 관리** | `sys-ca-msg-template` |
| 시스템 | 게시·알림 › 메시지·알림 › **메시지 이력** | `sys-ca-msg-history` |
| 시스템 | 게시·알림 › 메시지·알림 › **수신거부 관리** | `sys-ca-msg-opt-out` |
| 시스템 | 게시·알림 › 메시지·알림 › **알림 관리** | `sys-ca-notification-manage` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **시스템 관리** | `ca-system` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **기능모듈 관리** | `ca-feature-module` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **기능 플래그** | `ca-feature-flag` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **시스템 가이드** | `ca-system-guide` |
| 시스템 | 플랫폼 › 설정·정책 › **설정 관리** | `ca-config` |
| 시스템 | 플랫폼 › 설정·정책 › **데이터소스 관리** | `ca-datasource` |
| 시스템 | 플랫폼 › 설정·정책 › **데이터캐시정책 관리** | `ca-data-cache-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **HTTP캐시정책 관리** | `ca-http-cache-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **HTTP정책 관리** | `ca-http-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **페이지정책 관리** | `ca-page-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **홈 화면 규칙** | `ca-home-service-rule` |
| 시스템 | 플랫폼 › 다국어 › **언어코드 관리** | `ca-language` |
| 시스템 | 플랫폼 › 다국어 › **시스템언어 관리** | `ca-system-language` |
| 시스템 | 플랫폼 › 다국어 › **라벨 관리** | `ca-label` |
| 시스템 | 플랫폼 › 다국어 › **메시지 관리** | `ca-message` |
| 시스템 | 플랫폼 › 다국어 › **포맷정책 관리** | `ca-format-policy` |
| 시스템 | 보안 › 인증 › **인증정책 관리** | `ca-auth-policy` |
| 시스템 | 보안 › 인증 › **통합 인증 공급자** | `ca-auth-provider` |
| 시스템 | 보안 › 인증 › **SCIM 2.0 공급자** | `ca-scim-provider` |
| 시스템 | 보안 › 인증 › **인증항목패턴 관리** | `ca-auth-item-pattern` |
| 시스템 | 보안 › 인증 › **사용자 세션 관리** | `ca-user-session` |
| 시스템 | 보안 › 인증 › **외부 사용자 초대·승인·만료** | `ca-external-access` |
| 시스템 | 보안 › 접근 통제 › **허용IP 관리** | `ca-allowed-ip` |
| 시스템 | 보안 › 접근 통제 › **내부IP 관리** | `ca-internal-ip` |
| 시스템 | 보안 › 접근 통제 › **CORS 허용도메인 관리** | `ca-cors-allowed-origin` |
| 시스템 | 보안 › 접근 통제 › **접근정책 관리** | `ca-network-access-policy` |
| 시스템 | 보안 › 접근 통제 › **서비스접근 관리** | `ca-service-access` |
| 시스템 | 보안 › 접근 통제 › **접근 정책 규칙 관리** | `ca-access-rule` |
| 시스템 | 보안 › 접근 통제 › **보안정책 관리** | `ca-security-policy` |
| 시스템 | 보안 › 권한 정책 › **ABAC 접근 정책 관리** | `ca-access-policy` |
| 시스템 | 보안 › 권한 정책 › **ReBAC 권한 튜플 관리** | `ca-permission-tuple` |
| 시스템 | 보안 › 권한 정책 › **권한 매트릭스** | `ca-auth-matrix` |
| 시스템 | 보안 › 권한 정책 › **권한조절 공급자** | `ca-auth-adjust-provider` |
| 시스템 | 보안 › 개인정보 › **개인정보컬럼 관리** | `ca-privacy-column` |
| 시스템 | 보안 › 개인정보 › **개인정보 보존/파기** | `ca-privacy-retention` |
| 시스템 | 보안 › 개인정보 › **개인정보 로그** | `ca-log-privacy` |
| 시스템 | 보안 › 개인정보 › **암호화 도구** | `ca-crypto-tool` |
| 시스템 | 운영·로그 › 로그 › **오류 로그** | `ca-log-error` |
| 시스템 | 운영·로그 › 로그 › **로그인 로그** | `ca-log-login` |
| 시스템 | 운영·로그 › 로그 › **페이지 로그** | `ca-log-page` |
| 시스템 | 운영·로그 › 로그 › **API 로그** | `ca-log-service` |
| 시스템 | 운영·로그 › 로그 › **파일다운로드 이력** | `ca-log-file-download` |
| 시스템 | 운영·로그 › 로그 › **권한메뉴 로그** | `ca-log-role-service` |
| 시스템 | 운영·로그 › 로그 › **권한부여 로그** | `ca-log-role-grant` |
| 시스템 | 운영·로그 › 로그 › **로그 대시보드** | `ca-log-dashboard` |
| 시스템 | 운영·로그 › 로그 › **로그설정 관리** | `ca-log-config` |
| 시스템 | 운영·로그 › 로그 › **로그 파일 뷰어** | `ca-log-file-viewer` |
| 시스템 | 운영·로그 › 모니터링 › **모니터링 대시보드** | `ca-monitoring-dashboard` |
| 시스템 | 운영·로그 › 모니터링 › **성능 인사이트** | `ca-performance-insight` |
| 시스템 | 운영·로그 › 모니터링 › **모듈 상태** | `ca-module-health` |
| 시스템 | 운영·로그 › 모니터링 › **화면 모니터링** | `ca-screen-viewer` |
| 시스템 | 운영·로그 › 작업·파일 › **스케줄러 관리** | `ca-scheduler` |
| 시스템 | 운영·로그 › 작업·파일 › **스케줄러 실행이력** | `ca-scheduler-history` |
| 시스템 | 운영·로그 › 작업·파일 › **AI 스케줄러 운영** | `ca-ai-scheduler` |
| 시스템 | 운영·로그 › 작업·파일 › **대량처리 작업** | `ca-excel-job` |
| 시스템 | 운영·로그 › 작업·파일 › **점검 모드** | `ca-maintenance-mode` |
| 시스템 | 운영·로그 › 작업·파일 › **파일 관리** | `ca-attached-file` |
| 시스템 | 운영·로그 › 작업·파일 › **파일그룹 관리** | `ca-file-group` |
| 시스템 | 운영·로그 › 작업·파일 › **서버파일 관리** | `ca-server-file` |
| 시스템 | 연동 › EAI › **EAI 어댑터 설정** | `ca-eai-adapter` |
| 시스템 | 연동 › EAI › **연계 커넥터** | `ca-eai-connector` |
| 시스템 | 연동 › EAI › **EAI 인터페이스 관리** | `ca-eai-interface` |
| 시스템 | 연동 › EAI › **EAI 실행 이력** | `ca-eai-log` |
| 시스템 | 연동 › EAI › **EAI 모니터링** | `ca-eai-monitor` |
| 시스템 | 연동 › EAI › **외부API 관리** | `ca-eai-system` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 매핑** | `ca-sync-mapping` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 스케줄** | `ca-sync-schedule` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 로그** | `ca-sync-log` |
| 시스템 | 연동 › 결재·메시지 연동 › **GW결재 목록** | `ca-gw-approval-list` |
| 시스템 | 연동 › 결재·메시지 연동 › **GW결재 콜백 관리** | `ca-gw-approval-callback` |
| 시스템 | 연동 › 결재·메시지 연동 › **결재 체이닝** | `ca-approval-chain-manage` |
| 시스템 | 연동 › 결재·메시지 연동 › **메시지채널 관리** | `ca-msg-channel` |
| 시스템 | 연동 › 결재·메시지 연동 › **웹훅** | `ca-webhook` |
| 시스템 | 연동 › 결재·메시지 연동 › **단축 URL** | `ca-short-url` |
| 시스템 | 문서 설정 › **문서 편집기 설정** | `ca-smart-doc-editor-config` |
| 시스템 | 문서 설정 › **문서 접근 로그** | `ca-smart-doc-access-log` |
| 시스템 | 문서 설정 › **폐기/아카이브 이력** | `ca-smart-doc-disposal-log` |
| 시스템 | 문서 설정 › **워터마크 설정** | `ca-watermark-config` |
| 시스템 | 문서 설정 › **DRM설정 관리** | `ca-drm-config` |
| 시스템 | AI › AI 설정 › **AI 연결** | `ca-ai-provider` |
| 시스템 | AI › AI 설정 › **AI 가 볼 수 있는 데이터** | `ca-ai-admin` |
| 시스템 | AI › AI 설정 › **프롬프트 규칙** | `ca-ai-prompt-template` |
| 시스템 | AI › AI 설정 › **스키마 컨텍스트** | `ca-ai-schema-context` |
| 시스템 | AI › AI 설정 › **외부 컬럼 매핑** | `ca-ai-external-column-map` |
| 시스템 | AI › AI 설정 › **이상감지 규칙** | `ca-ai-anomaly-rule` |
| 시스템 | AI › AI 설정 › **AI 사용량·한도** | `ca-ai-usage-dashboard` |
| 시스템 | AI › AI 설정 › **AI 권한 설명기** | `ca-ai-access-explainer` |
| 시스템 | AI › 에이전트 › **AI 계획 승인** | `ai-plan-approval` |
| 시스템 | AI › 에이전트 › **개선안 보고서** | `ai-improvement` |
| 시스템 | AI › 에이전트 › **사용자 프로그램 관리** | `ca-user-program-admin` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 등록** | `ca-screen-registration` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 빌더** | `ca-screen-builder` |
| 시스템 | 개발도구 › 화면·코드 생성 › **서버 빌더** | `ca-server-builder` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 소스 편집** | `ca-page-source-editor` |
| 시스템 | 개발도구 › 화면·코드 생성 › **생성 마법사** | `ca-scaffold-wizard` |
| 시스템 | 개발도구 › 화면·코드 생성 › **커스텀 컴포넌트 빌더** | `ca-custom-component` |
| 시스템 | 개발도구 › 화면·코드 생성 › **커스텀 필드 관리** | `ca-custom-field` |
| 시스템 | 개발도구 › 화면·코드 생성 › **스니펫관리** | `ca-snippet-manage` |
| 시스템 | 개발도구 › 화면·코드 생성 › **문서 정의 편집** | `document-definition-editor` |
| 시스템 | 개발도구 › DB·데이터 › **DB 탐색기** | `ca-db-explorer` |
| 시스템 | 개발도구 › DB·데이터 › **DB 빌더** | `ca-db-builder` |
| 시스템 | 개발도구 › DB·데이터 › **DDL 실행 이력** | `ca-ddl-history` |
| 시스템 | 개발도구 › DB·데이터 › **데이터 표준 사전** | `ca-data-standard` |
| 시스템 | 개발도구 › DB·데이터 › **이력 조회** | `ca-entity-version` |
| 시스템 | 개발도구 › DB·데이터 › **CRUD 매트릭스** | `ca-crud-matrix` |
| 시스템 | 개발도구 › DB·데이터 › **i18n 스캔** | `ca-i18n-scan` |
| 시스템 | 개발도구 › 테스트 › **시험 현황** | `test-dashboard` |
| 시스템 | 개발도구 › 테스트 › **시험 대상** | `test-target` |
| 시스템 | 개발도구 › 테스트 › **시험 사례** | `test-scenario` |
| 시스템 | 개발도구 › 테스트 › **시험 묶음** | `test-suite` |
| 시스템 | 개발도구 › 테스트 › **시험 실행** | `test-run` |
| 시스템 | 개발도구 › 테스트 › **테스트 점검항목** | `test-check-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 대상 서비스** | `test-service-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 커버리지** | `ca-test-coverage` |
| 시스템 | 개발도구 › 테스트 › **테스트 데이터 팩토리** | `ca-test-data-factory` |
| 시스템 | 개발도구 › 테스트 › **API 테스트** | `ca-api-playground` |
| 시스템 | 개발도구 › 운영 도구 › **DevOps 콘솔** | `ca-devops-console` |
| 시스템 | 개발도구 › 운영 도구 › **Hotfix 관리** | `ca-hotfix` |
| 시스템 | 개발도구 › 운영 도구 › **디버그정책 관리** | `ca-debug-policy` |
| 시스템 | 개발도구 › 운영 도구 › **개발모듈오버라이드** | `ca-dev-module-override` |
| 시스템 | 개발도구 › 운영 도구 › **서비스 의존성** | `ca-service-dependency` |
| 시스템 | 개발도구 › 확장 개발 › **페이지 확장** | `ca-page-extension` |
