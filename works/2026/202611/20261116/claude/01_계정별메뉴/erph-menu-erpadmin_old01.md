# erp-h 상단메뉴별 left메뉴 트리 (프로그램 경로 포함)

- 조회: 2026-11-16 · `http://localhost:8080/` · 로그인 계정 `erpadmin` · Aside 브라우저로 상단 탭별 `.menu-tree` DOM 확인 + 메뉴 API `POST /sys/menu/list` 의 `serviceId` 대조
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>` 순서. 폴더는 `이름/`.
- 화면 주소는 `S2-screen-inventory.md` 가 적은 규칙 `/page/<서비스ID>` 를 따랐다. 메뉴를 눌러 주소창을 직접 대조하지는 않았다.
- 화면 소스 파일 경로(`pages/...js`)는 메뉴 API 에 없어 넣지 않았다. `module` 값은 모두 `app`.
- 계정 권한에 따라 보이는 메뉴가 다르다. 아래는 `erpadmin` 기준.

| 상단 탭 | 폴더 | 화면 |
|---|---:|---:|
| 공통 | 16 | 49 |
| 하자 | 15 | 61 |
| 시스템 | 43 | 172 |
| 개인 (상단 탭에 안 나옴) | 7 | 25 |

## 1. 트리

```text
상단메뉴
├── [공통]  key=MENU_BIZ_COMMON (폴더 16 · 화면 49)
│   ├── 조직·사용자/
│   │   ├── 조직/
│   │   │   ├── 회사 관리  `ca-company` → `/page/ca-company`
│   │   │   ├── 사업장 관리  `ca-business-place` → `/page/ca-business-place`
│   │   │   ├── 부서/조직 정보  `co_deptMng` → `/page/co_deptMng`
│   │   │   └── 조직 구조 관리  `ca-org-structure` → `/page/ca-org-structure`
│   │   ├── 사용자/
│   │   │   ├── 임직원 관리  `ca-employee` → `/page/ca-employee`
│   │   │   ├── 사용자 관리  `ca-hmp-user-manage` → `/page/ca-hmp-user-manage`
│   │   │   ├── 사용자 법인 소속 관리  `ca-user-company` → `/page/ca-user-company`
│   │   │   └── 사용자 발령 관리  `ca-org-assignment` → `/page/ca-org-assignment`
│   │   └── 협력업체/
│   │       ├── 협력업체 관리  `ca-partner` → `/page/ca-partner`
│   │       └── 협력업체사용자 관리  `ca-partner-user` → `/page/ca-partner-user`
│   ├── 권한/
│   │   ├── 권한 정의/
│   │   │   ├── 권한 관리  `ca-role` → `/page/ca-role`
│   │   │   ├── 공통권한 관리  `ca-role-common` → `/page/ca-role-common`
│   │   │   └── 발령권한 관리  `ca-role-rule` → `/page/ca-role-rule`
│   │   └── 권한 부여/
│   │       ├── 권한/사용자매핑  `ca-user-role` → `/page/ca-user-role`
│   │       ├── 사용자/권한매핑  `ca-user-role-by-user` → `/page/ca-user-role-by-user`
│   │       ├── 지정권한 관리  `ca-role-assign` → `/page/ca-role-assign`
│   │       ├── 권한위임 관리  `ca-role-delegation` → `/page/ca-role-delegation`
│   │       ├── 사용자(모바일)  `co_mobileAuthMapp` → `/page/co_mobileAuthMapp`
│   │       └── 사용자권한 조회  `ca-user-role-inquiry` → `/page/ca-user-role-inquiry`
│   ├── 메뉴·업무그룹/
│   │   ├── 업무그룹 관리  `ca-biz-group` → `/page/ca-biz-group`
│   │   ├── 메뉴트리관리  `ca-service` → `/page/ca-service`
│   │   ├── 권한/메뉴매핑  `ca-role-service` → `/page/ca-role-service`
│   │   └── 매뉴얼  `ca-manual-viewer` → `/page/ca-manual-viewer`
│   ├── 기준정보/
│   │   ├── 코드 관리  `ca-code` → `/page/ca-code`
│   │   ├── 국가코드 관리  `ca-country` → `/page/ca-country`
│   │   ├── 화폐 관리  `ca-currency` → `/page/ca-currency`
│   │   ├── 환율 관리  `ca-exchange-rate` → `/page/ca-exchange-rate`
│   │   ├── 은행 관리  `ca-bank` → `/page/ca-bank`
│   │   ├── 공휴일 관리  `ca-holiday` → `/page/ca-holiday`
│   │   ├── 채번 규칙  `ca-numbering-rule` → `/page/ca-numbering-rule`
│   │   └── 결정표  `ca-decision-table` → `/page/ca-decision-table`
│   ├── 결재·문서/
│   │   ├── 결재/
│   │   │   ├── 결재 현황  `ca-approval-dashboard` → `/page/ca-approval-dashboard`
│   │   │   ├── 결재 양식 관리  `ca-approval-form-template` → `/page/ca-approval-form-template`
│   │   │   └── 문서양식관리  `co_elcDoc` → `/page/co_elcDoc`
│   │   ├── 문서 양식/
│   │   │   ├── 문서 유형 관리  `ca-smart-doc-type` → `/page/ca-smart-doc-type`
│   │   │   ├── 문서 템플릿  `ca-doc-template` → `/page/ca-doc-template`
│   │   │   ├── 문서 템플릿 관리  `ca-smart-doc-template` → `/page/ca-smart-doc-template`
│   │   │   └── 전자 양식 디자이너  `ca-eform-designer` → `/page/ca-eform-designer`
│   │   └── 문서 관리/
│   │       ├── 문서 탐색  `ca-edms-browse` → `/page/ca-edms-browse`
│   │       ├── 문서 등록/편집  `ca-edms-doc-manage` → `/page/ca-edms-doc-manage`
│   │       ├── 문서 보안 처리  `ca-common-doc-protect` → `/page/ca-common-doc-protect`
│   │       └── 보존 정책 관리  `ca-retention-policy` → `/page/ca-retention-policy`
│   └── 게시·알림/
│       ├── 게시/
│       │   ├── 게시판관리  `ca-board-manage` → `/page/ca-board-manage`
│       │   └── 문의 관리  `ca-inquiry` → `/page/ca-inquiry`
│       └── 메시지·알림/
│           ├── 통합 메시지 발송  `ca-msg-send` → `/page/ca-msg-send`
│           ├── 메시지템플릿 관리  `ca-msg-template` → `/page/ca-msg-template`
│           ├── 메시지 이력  `ca-msg-history` → `/page/ca-msg-history`
│           ├── 수신거부 관리  `ca-msg-opt-out` → `/page/ca-msg-opt-out`
│           └── 알림 관리  `ca-notification-manage` → `/page/ca-notification-manage`
├── [하자]  key=MENU_CS (폴더 15 · 화면 61)
│   ├── 하자  `db_dashBoardCS` → `/page/db_dashBoardCS`
│   ├── 일정관리/
│   │   └── 일정관리  `cs_schMng` → `/page/cs_schMng`
│   ├── 기본정보/
│   │   ├── 현장정보  `cs_siteInfo` → `/page/cs_siteInfo`
│   │   └── 협력업체정보  `cs_copCoInfo` → `/page/cs_copCoInfo`
│   ├── 하자 등록 및 처리/
│   │   ├── 공통/
│   │   │   ├── 공종코드  `cs_ctpCd` → `/page/cs_ctpCd`
│   │   │   ├── 현장별 공간 부위 코드  `cs_sitSpacePartCd` → `/page/cs_sitSpacePartCd`
│   │   │   ├── 담당현장  `cs_chrSite` → `/page/cs_chrSite`
│   │   │   ├── 기준정보 등록 요청  `cs_baseCdReq` → `/page/cs_baseCdReq`
│   │   │   ├── 엔지니어 승인관리  `cs_mobileUsrAprv` → `/page/cs_mobileUsrAprv`
│   │   │   ├── 기준정보 등록 승인  `cs_baseCdAprv` → `/page/cs_baseCdAprv`
│   │   │   └── 모바일사용자  `cs_mobileUsr` → `/page/cs_mobileUsr`
│   │   ├── 기초자료/
│   │   │   ├── 점검일정  `cs_chkSch` → `/page/cs_chkSch`
│   │   │   ├── 공종별협력업체  `cs_bctyCopCo` → `/page/cs_bctyCopCo`
│   │   │   ├── 세대정보  `cs_hhldInfo` → `/page/cs_hhldInfo`
│   │   │   └── 하자분류 매칭  `cs_ctpMatch` → `/page/cs_ctpMatch`
│   │   ├── 준공전품질점검/
│   │   │   ├── 품질점검  `cs_csChkMng_qlt` → `/page/cs_csChkMng_qlt`
│   │   │   └── 주부품질점검  `cs_csChkMng_hwf` → `/page/cs_csChkMng_hwf`
│   │   ├── 입주자사전점검/
│   │   │   ├── 사전점검  `cs_csChkMng_bf` → `/page/cs_csChkMng_bf`
│   │   │   ├── 사전방문 예약  `cs_visitRsv` → `/page/cs_visitRsv`
│   │   │   └── 입주 지원  `cs_mvinMng` → `/page/cs_mvinMng`
│   │   ├── 인수인계/
│   │   │   ├── 표준인수인계파일등록  `cs_tnhFile` → `/page/cs_tnhFile`
│   │   │   ├── 현장인수인계요청  `cs_siteTnhReq` → `/page/cs_siteTnhReq`
│   │   │   └── 현장인수인계승인  `cs_siteTnhAprv` → `/page/cs_siteTnhAprv`
│   │   ├── AS관리/
│   │   │   ├── A/S관리  `cs_csChkMng` → `/page/cs_csChkMng`
│   │   │   ├── 작업관리  `cs_wrkCmplMng` → `/page/cs_wrkCmplMng`
│   │   │   ├── 하자 문자발송  `cs_csSendSms` → `/page/cs_csSendSms`
│   │   │   └── 분쟁 단지 현황  `cs_dsptMng` → `/page/cs_dsptMng`
│   │   ├── 점검내역대량등록/
│   │   │   └── 점검내역대량등록  `cs_chkDtlsBlkReg` → `/page/cs_chkDtlsBlkReg`
│   │   └── 현황조회/
│   │       ├── 하자처리현황  `cs_disComCurst` → `/page/cs_disComCurst`
│   │       ├── 하자 접수 및 처리대장  `cs_dealAcp` → `/page/cs_dealAcp`
│   │       ├── 협력업체별현황  `cs_byCopCoCurst` → `/page/cs_byCopCoCurst`
│   │       ├── 협력업체공종별현황  `cs_byCopCoCtpCurst` → `/page/cs_byCopCoCtpCurst`
│   │       ├── 유형별현황  `cs_byTpCurst` → `/page/cs_byTpCurst`
│   │       ├── 공간/부위별 접수처리 현황  `cs_spaceCurst` → `/page/cs_spaceCurst`
│   │       ├── 현장별 AS접수 통계  `cs_bySiteAcpStats` → `/page/cs_bySiteAcpStats`
│   │       ├── 공종별 처리현황  `cs_ctpDiscomCurst` → `/page/cs_ctpDiscomCurst`
│   │       ├── 업체별 처리현황  `cs_cooDiscomCurst` → `/page/cs_cooDiscomCurst`
│   │       ├── 장기 미처리 하자 관리  `cs_ltrmDefect` → `/page/cs_ltrmDefect`
│   │       ├── 하자 통합 분석  `cs_defectAnalysis` → `/page/cs_defectAnalysis`
│   │       ├── 협력사 이행 현황  `cs_copCoPerf` → `/page/cs_copCoPerf`
│   │       ├── 중점 관리 단지  `cs_keySite` → `/page/cs_keySite`
│   │       ├── 센터장 일일보고  `cs_ctrDailyRpt` → `/page/cs_ctrDailyRpt`
│   │       ├── 단지별 조직도  `cs_siteOrg` → `/page/cs_siteOrg`
│   │       ├── 재무 연계 하자 분석  `cs_finDefect` → `/page/cs_finDefect`
│   │       ├── 접수 건 추적  `cs_acpTrack` → `/page/cs_acpTrack`
│   │       └── 확인점검 결과 대시보드  `cs_chkDashboard` → `/page/cs_chkDashboard`
│   ├── 하자처리보고/
│   │   └── 하자처리보고서  `cs_dealRep` → `/page/cs_dealRep`
│   ├── 하자보수비/
│   │   ├── 비율등록  `cs_asByyyBgtCd` → `/page/cs_asByyyBgtCd`
│   │   ├── 연도별예산관리  `cs_asByyyBgt` → `/page/cs_asByyyBgt`
│   │   ├── 집행실적관리  `cs_asSiteCs` → `/page/cs_asSiteCs`
│   │   ├── 비용 집행  `cs_costClaim` → `/page/cs_costClaim`
│   │   ├── 보증금 청구 관리  `cs_gntClm` → `/page/cs_gntClm`
│   │   ├── 하자전표관리/
│   │   │   └── 공동도급안분  `cs_jonitVenture` → `/page/cs_jonitVenture`
│   │   └── 현황조회/
│   │       ├── 현장종합 투입금액  `cs_siteTotCst` → `/page/cs_siteTotCst`
│   │       ├── 자금집계표  `cs_siteMoneyCst` → `/page/cs_siteMoneyCst`
│   │       ├── 월별 하자보수비 현황  `cs_bymAsExpIptCurst` → `/page/cs_bymAsExpIptCurst`
│   │       ├── 일반관리비 실적  `cs_generlCst` → `/page/cs_generlCst`
│   │       ├── 보증현황  `cs_sponSionCst` → `/page/cs_sponSionCst`
│   │       └── 전표조회  `cs_asSlpCst` → `/page/cs_asSlpCst`
│   ├── 협력업체 공지사항  `cs_jcAnncBrdLst` → `/page/cs_jcAnncBrdLst`
│   └── 발주 내역  `cs_ordDtls` → `/page/cs_ordDtls`
├── [시스템]  key=MENU_COMMON_ADMIN (폴더 43 · 화면 172)
│   ├── 조직·사용자/
│   │   ├── 조직/
│   │   │   ├── 회사 관리  `sys-ca-company` → `/page/sys-ca-company`
│   │   │   ├── 사업장 관리  `sys-ca-business-place` → `/page/sys-ca-business-place`
│   │   │   ├── 부서/조직 정보  `sys-co_deptMng` → `/page/sys-co_deptMng`
│   │   │   └── 조직 구조 관리  `sys-ca-org-structure` → `/page/sys-ca-org-structure`
│   │   ├── 사용자/
│   │   │   ├── 임직원 관리  `sys-ca-employee` → `/page/sys-ca-employee`
│   │   │   ├── 사용자 관리  `sys-ca-hmp-user-manage` → `/page/sys-ca-hmp-user-manage`
│   │   │   ├── 사용자 법인 소속 관리  `sys-ca-user-company` → `/page/sys-ca-user-company`
│   │   │   └── 사용자 발령 관리  `sys-ca-org-assignment` → `/page/sys-ca-org-assignment`
│   │   └── 협력업체/
│   │       ├── 협력업체 관리  `sys-ca-partner` → `/page/sys-ca-partner`
│   │       └── 협력업체사용자 관리  `sys-ca-partner-user` → `/page/sys-ca-partner-user`
│   ├── 권한/
│   │   ├── 권한 정의/
│   │   │   ├── 권한 관리  `sys-ca-role` → `/page/sys-ca-role`
│   │   │   ├── 공통권한 관리  `sys-ca-role-common` → `/page/sys-ca-role-common`
│   │   │   └── 발령권한 관리  `sys-ca-role-rule` → `/page/sys-ca-role-rule`
│   │   └── 권한 부여/
│   │       ├── 권한/사용자매핑  `sys-ca-user-role` → `/page/sys-ca-user-role`
│   │       ├── 사용자/권한매핑  `sys-ca-user-role-by-user` → `/page/sys-ca-user-role-by-user`
│   │       ├── 지정권한 관리  `sys-ca-role-assign` → `/page/sys-ca-role-assign`
│   │       ├── 권한위임 관리  `sys-ca-role-delegation` → `/page/sys-ca-role-delegation`
│   │       ├── 사용자(모바일)  `sys-co_mobileAuthMapp` → `/page/sys-co_mobileAuthMapp`
│   │       └── 사용자권한 조회  `sys-ca-user-role-inquiry` → `/page/sys-ca-user-role-inquiry`
│   ├── 메뉴·업무그룹/
│   │   ├── 업무그룹 관리  `sys-ca-biz-group` → `/page/sys-ca-biz-group`
│   │   ├── 프로그램관리  `ca-program` → `/page/ca-program`
│   │   ├── 메뉴트리관리  `sys-ca-service` → `/page/sys-ca-service`
│   │   ├── 권한/메뉴매핑  `sys-ca-role-service` → `/page/sys-ca-role-service`
│   │   └── 매뉴얼  `sys-ca-manual-viewer` → `/page/sys-ca-manual-viewer`
│   ├── 기준정보/
│   │   ├── 코드 관리  `sys-ca-code` → `/page/sys-ca-code`
│   │   ├── 국가코드 관리  `sys-ca-country` → `/page/sys-ca-country`
│   │   ├── 화폐 관리  `sys-ca-currency` → `/page/sys-ca-currency`
│   │   ├── 환율 관리  `sys-ca-exchange-rate` → `/page/sys-ca-exchange-rate`
│   │   ├── 은행 관리  `sys-ca-bank` → `/page/sys-ca-bank`
│   │   ├── 공휴일 관리  `sys-ca-holiday` → `/page/sys-ca-holiday`
│   │   ├── 채번 규칙  `sys-ca-numbering-rule` → `/page/sys-ca-numbering-rule`
│   │   └── 결정표  `sys-ca-decision-table` → `/page/sys-ca-decision-table`
│   ├── 결재·문서/
│   │   ├── 결재/
│   │   │   ├── 결재 현황  `sys-ca-approval-dashboard` → `/page/sys-ca-approval-dashboard`
│   │   │   ├── 결재 양식 관리  `sys-ca-approval-form-template` → `/page/sys-ca-approval-form-template`
│   │   │   └── 문서양식관리  `sys-co_elcDoc` → `/page/sys-co_elcDoc`
│   │   ├── 문서 양식/
│   │   │   ├── 문서 유형 관리  `sys-ca-smart-doc-type` → `/page/sys-ca-smart-doc-type`
│   │   │   ├── 문서 템플릿  `sys-ca-doc-template` → `/page/sys-ca-doc-template`
│   │   │   ├── 문서 템플릿 관리  `sys-ca-smart-doc-template` → `/page/sys-ca-smart-doc-template`
│   │   │   └── 전자 양식 디자이너  `sys-ca-eform-designer` → `/page/sys-ca-eform-designer`
│   │   └── 문서 관리/
│   │       ├── 문서 탐색  `sys-ca-edms-browse` → `/page/sys-ca-edms-browse`
│   │       ├── 문서 등록/편집  `sys-ca-edms-doc-manage` → `/page/sys-ca-edms-doc-manage`
│   │       ├── 문서 보안 처리  `sys-ca-common-doc-protect` → `/page/sys-ca-common-doc-protect`
│   │       └── 보존 정책 관리  `sys-ca-retention-policy` → `/page/sys-ca-retention-policy`
│   ├── 게시·알림/
│   │   ├── 게시/
│   │   │   ├── 게시판관리  `sys-ca-board-manage` → `/page/sys-ca-board-manage`
│   │   │   └── 문의 관리  `sys-ca-inquiry` → `/page/sys-ca-inquiry`
│   │   └── 메시지·알림/
│   │       ├── 통합 메시지 발송  `sys-ca-msg-send` → `/page/sys-ca-msg-send`
│   │       ├── 메시지템플릿 관리  `sys-ca-msg-template` → `/page/sys-ca-msg-template`
│   │       ├── 메시지 이력  `sys-ca-msg-history` → `/page/sys-ca-msg-history`
│   │       ├── 수신거부 관리  `sys-ca-msg-opt-out` → `/page/sys-ca-msg-opt-out`
│   │       └── 알림 관리  `sys-ca-notification-manage` → `/page/sys-ca-notification-manage`
│   ├── 플랫폼/
│   │   ├── 시스템·프로그램/
│   │   │   ├── 시스템 관리  `ca-system` → `/page/ca-system`
│   │   │   ├── 기능모듈 관리  `ca-feature-module` → `/page/ca-feature-module`
│   │   │   ├── 기능 플래그  `ca-feature-flag` → `/page/ca-feature-flag`
│   │   │   └── 시스템 가이드  `ca-system-guide` → `/page/ca-system-guide`
│   │   ├── 설정·정책/
│   │   │   ├── 설정 관리  `ca-config` → `/page/ca-config`
│   │   │   ├── 데이터소스 관리  `ca-datasource` → `/page/ca-datasource`
│   │   │   ├── 데이터캐시정책 관리  `ca-data-cache-policy` → `/page/ca-data-cache-policy`
│   │   │   ├── HTTP캐시정책 관리  `ca-http-cache-policy` → `/page/ca-http-cache-policy`
│   │   │   ├── HTTP정책 관리  `ca-http-policy` → `/page/ca-http-policy`
│   │   │   ├── 페이지정책 관리  `ca-page-policy` → `/page/ca-page-policy`
│   │   │   └── 홈 화면 규칙  `ca-home-service-rule` → `/page/ca-home-service-rule`
│   │   └── 다국어/
│   │       ├── 언어코드 관리  `ca-language` → `/page/ca-language`
│   │       ├── 시스템언어 관리  `ca-system-language` → `/page/ca-system-language`
│   │       ├── 라벨 관리  `ca-label` → `/page/ca-label`
│   │       ├── 메시지 관리  `ca-message` → `/page/ca-message`
│   │       └── 포맷정책 관리  `ca-format-policy` → `/page/ca-format-policy`
│   ├── 보안/
│   │   ├── 인증/
│   │   │   ├── 인증정책 관리  `ca-auth-policy` → `/page/ca-auth-policy`
│   │   │   ├── 통합 인증 공급자  `ca-auth-provider` → `/page/ca-auth-provider`
│   │   │   ├── SCIM 2.0 공급자  `ca-scim-provider` → `/page/ca-scim-provider`
│   │   │   ├── 인증항목패턴 관리  `ca-auth-item-pattern` → `/page/ca-auth-item-pattern`
│   │   │   ├── 사용자 세션 관리  `ca-user-session` → `/page/ca-user-session`
│   │   │   └── 외부 사용자 초대·승인·만료  `ca-external-access` → `/page/ca-external-access`
│   │   ├── 접근 통제/
│   │   │   ├── 허용IP 관리  `ca-allowed-ip` → `/page/ca-allowed-ip`
│   │   │   ├── 내부IP 관리  `ca-internal-ip` → `/page/ca-internal-ip`
│   │   │   ├── CORS 허용도메인 관리  `ca-cors-allowed-origin` → `/page/ca-cors-allowed-origin`
│   │   │   ├── 접근정책 관리  `ca-network-access-policy` → `/page/ca-network-access-policy`
│   │   │   ├── 서비스접근 관리  `ca-service-access` → `/page/ca-service-access`
│   │   │   ├── 접근 정책 규칙 관리  `ca-access-rule` → `/page/ca-access-rule`
│   │   │   └── 보안정책 관리  `ca-security-policy` → `/page/ca-security-policy`
│   │   ├── 권한 정책/
│   │   │   ├── ABAC 접근 정책 관리  `ca-access-policy` → `/page/ca-access-policy`
│   │   │   ├── ReBAC 권한 튜플 관리  `ca-permission-tuple` → `/page/ca-permission-tuple`
│   │   │   ├── 권한 매트릭스  `ca-auth-matrix` → `/page/ca-auth-matrix`
│   │   │   └── 권한조절 공급자  `ca-auth-adjust-provider` → `/page/ca-auth-adjust-provider`
│   │   └── 개인정보/
│   │       ├── 개인정보컬럼 관리  `ca-privacy-column` → `/page/ca-privacy-column`
│   │       ├── 개인정보 보존/파기  `ca-privacy-retention` → `/page/ca-privacy-retention`
│   │       ├── 개인정보 로그  `ca-log-privacy` → `/page/ca-log-privacy`
│   │       └── 암호화 도구  `ca-crypto-tool` → `/page/ca-crypto-tool`
│   ├── 운영·로그/
│   │   ├── 로그/
│   │   │   ├── 오류 로그  `ca-log-error` → `/page/ca-log-error`
│   │   │   ├── 로그인 로그  `ca-log-login` → `/page/ca-log-login`
│   │   │   ├── 페이지 로그  `ca-log-page` → `/page/ca-log-page`
│   │   │   ├── API 로그  `ca-log-service` → `/page/ca-log-service`
│   │   │   ├── 파일다운로드 이력  `ca-log-file-download` → `/page/ca-log-file-download`
│   │   │   ├── 권한메뉴 로그  `ca-log-role-service` → `/page/ca-log-role-service`
│   │   │   ├── 권한부여 로그  `ca-log-role-grant` → `/page/ca-log-role-grant`
│   │   │   ├── 로그 대시보드  `ca-log-dashboard` → `/page/ca-log-dashboard`
│   │   │   ├── 로그설정 관리  `ca-log-config` → `/page/ca-log-config`
│   │   │   └── 로그 파일 뷰어  `ca-log-file-viewer` → `/page/ca-log-file-viewer`
│   │   ├── 모니터링/
│   │   │   ├── 모니터링 대시보드  `ca-monitoring-dashboard` → `/page/ca-monitoring-dashboard`
│   │   │   ├── 성능 인사이트  `ca-performance-insight` → `/page/ca-performance-insight`
│   │   │   ├── 모듈 상태  `ca-module-health` → `/page/ca-module-health`
│   │   │   └── 화면 모니터링  `ca-screen-viewer` → `/page/ca-screen-viewer`
│   │   └── 작업·파일/
│   │       ├── 스케줄러 관리  `ca-scheduler` → `/page/ca-scheduler`
│   │       ├── 스케줄러 실행이력  `ca-scheduler-history` → `/page/ca-scheduler-history`
│   │       ├── AI 스케줄러 운영  `ca-ai-scheduler` → `/page/ca-ai-scheduler`
│   │       ├── 대량처리 작업  `ca-excel-job` → `/page/ca-excel-job`
│   │       ├── 점검 모드  `ca-maintenance-mode` → `/page/ca-maintenance-mode`
│   │       ├── 파일 관리  `ca-attached-file` → `/page/ca-attached-file`
│   │       ├── 파일그룹 관리  `ca-file-group` → `/page/ca-file-group`
│   │       └── 서버파일 관리  `ca-server-file` → `/page/ca-server-file`
│   ├── 연동/
│   │   ├── EAI/
│   │   │   ├── EAI 어댑터 설정  `ca-eai-adapter` → `/page/ca-eai-adapter`
│   │   │   ├── 연계 커넥터  `ca-eai-connector` → `/page/ca-eai-connector`
│   │   │   ├── EAI 인터페이스 관리  `ca-eai-interface` → `/page/ca-eai-interface`
│   │   │   ├── EAI 실행 이력  `ca-eai-log` → `/page/ca-eai-log`
│   │   │   ├── EAI 모니터링  `ca-eai-monitor` → `/page/ca-eai-monitor`
│   │   │   └── 외부API 관리  `ca-eai-system` → `/page/ca-eai-system`
│   │   ├── 외부 동기화/
│   │   │   ├── 외부 동기화 매핑  `ca-sync-mapping` → `/page/ca-sync-mapping`
│   │   │   ├── 외부 동기화 스케줄  `ca-sync-schedule` → `/page/ca-sync-schedule`
│   │   │   └── 외부 동기화 로그  `ca-sync-log` → `/page/ca-sync-log`
│   │   └── 결재·메시지 연동/
│   │       ├── GW결재 목록  `ca-gw-approval-list` → `/page/ca-gw-approval-list`
│   │       ├── GW결재 콜백 관리  `ca-gw-approval-callback` → `/page/ca-gw-approval-callback`
│   │       ├── 결재 체이닝  `ca-approval-chain-manage` → `/page/ca-approval-chain-manage`
│   │       ├── 메시지채널 관리  `ca-msg-channel` → `/page/ca-msg-channel`
│   │       ├── 웹훅  `ca-webhook` → `/page/ca-webhook`
│   │       └── 단축 URL  `ca-short-url` → `/page/ca-short-url`
│   ├── 문서 설정/
│   │   ├── 문서 편집기 설정  `ca-smart-doc-editor-config` → `/page/ca-smart-doc-editor-config`
│   │   ├── 문서 접근 로그  `ca-smart-doc-access-log` → `/page/ca-smart-doc-access-log`
│   │   ├── 폐기/아카이브 이력  `ca-smart-doc-disposal-log` → `/page/ca-smart-doc-disposal-log`
│   │   ├── 워터마크 설정  `ca-watermark-config` → `/page/ca-watermark-config`
│   │   └── DRM설정 관리  `ca-drm-config` → `/page/ca-drm-config`
│   ├── AI/
│   │   ├── AI 설정/
│   │   │   ├── AI 연결  `ca-ai-provider` → `/page/ca-ai-provider`
│   │   │   ├── AI 가 볼 수 있는 데이터  `ca-ai-admin` → `/page/ca-ai-admin`
│   │   │   ├── 프롬프트 규칙  `ca-ai-prompt-template` → `/page/ca-ai-prompt-template`
│   │   │   ├── 스키마 컨텍스트  `ca-ai-schema-context` → `/page/ca-ai-schema-context`
│   │   │   ├── 외부 컬럼 매핑  `ca-ai-external-column-map` → `/page/ca-ai-external-column-map`
│   │   │   ├── 이상감지 규칙  `ca-ai-anomaly-rule` → `/page/ca-ai-anomaly-rule`
│   │   │   ├── AI 사용량·한도  `ca-ai-usage-dashboard` → `/page/ca-ai-usage-dashboard`
│   │   │   └── AI 권한 설명기  `ca-ai-access-explainer` → `/page/ca-ai-access-explainer`
│   │   └── 에이전트/
│   │       ├── AI 계획 승인  `ai-plan-approval` → `/page/ai-plan-approval`
│   │       ├── 개선안 보고서  `ai-improvement` → `/page/ai-improvement`
│   │       └── 사용자 프로그램 관리  `ca-user-program-admin` → `/page/ca-user-program-admin`
│   └── 개발도구/
│       ├── 화면·코드 생성/
│       │   ├── 화면 등록  `ca-screen-registration` → `/page/ca-screen-registration`
│       │   ├── 화면 빌더  `ca-screen-builder` → `/page/ca-screen-builder`
│       │   ├── 서버 빌더  `ca-server-builder` → `/page/ca-server-builder`
│       │   ├── 화면 소스 편집  `ca-page-source-editor` → `/page/ca-page-source-editor`
│       │   ├── 생성 마법사  `ca-scaffold-wizard` → `/page/ca-scaffold-wizard`
│       │   ├── 커스텀 컴포넌트 빌더  `ca-custom-component` → `/page/ca-custom-component`
│       │   ├── 커스텀 필드 관리  `ca-custom-field` → `/page/ca-custom-field`
│       │   ├── 스니펫관리  `ca-snippet-manage` → `/page/ca-snippet-manage`
│       │   └── 문서 정의 편집  `document-definition-editor` → `/page/document-definition-editor`
│       ├── DB·데이터/
│       │   ├── DB 탐색기  `ca-db-explorer` → `/page/ca-db-explorer`
│       │   ├── DB 빌더  `ca-db-builder` → `/page/ca-db-builder`
│       │   ├── DDL 실행 이력  `ca-ddl-history` → `/page/ca-ddl-history`
│       │   ├── 데이터 표준 사전  `ca-data-standard` → `/page/ca-data-standard`
│       │   ├── 이력 조회  `ca-entity-version` → `/page/ca-entity-version`
│       │   ├── CRUD 매트릭스  `ca-crud-matrix` → `/page/ca-crud-matrix`
│       │   └── i18n 스캔  `ca-i18n-scan` → `/page/ca-i18n-scan`
│       ├── 테스트/
│       │   ├── 시험 현황  `test-dashboard` → `/page/test-dashboard`
│       │   ├── 시험 대상  `test-target` → `/page/test-target`
│       │   ├── 시험 사례  `test-scenario` → `/page/test-scenario`
│       │   ├── 시험 묶음  `test-suite` → `/page/test-suite`
│       │   ├── 시험 실행  `test-run` → `/page/test-run`
│       │   ├── 테스트 점검항목  `test-check-item` → `/page/test-check-item`
│       │   ├── 테스트 대상 서비스  `test-service-item` → `/page/test-service-item`
│       │   ├── 테스트 커버리지  `ca-test-coverage` → `/page/ca-test-coverage`
│       │   ├── 테스트 데이터 팩토리  `ca-test-data-factory` → `/page/ca-test-data-factory`
│       │   └── API 테스트  `ca-api-playground` → `/page/ca-api-playground`
│       ├── 운영 도구/
│       │   ├── DevOps 콘솔  `ca-devops-console` → `/page/ca-devops-console`
│       │   ├── Hotfix 관리  `ca-hotfix` → `/page/ca-hotfix`
│       │   ├── 디버그정책 관리  `ca-debug-policy` → `/page/ca-debug-policy`
│       │   ├── 개발모듈오버라이드  `ca-dev-module-override` → `/page/ca-dev-module-override`
│       │   └── 서비스 의존성  `ca-service-dependency` → `/page/ca-service-dependency`
│       └── 확장 개발/
│           └── 페이지 확장  `ca-page-extension` → `/page/ca-page-extension`
└── [개인]  key=MENU_COMMON (폴더 7 · 화면 25)
    ├── 마이페이지/
    │   ├── 개인설정  `common-preference` → `/page/common-preference`
    │   ├── 캘린더  `common-calendar` → `/page/common-calendar`
    │   ├── 내 정보  `common-my-page` → `/page/common-my-page`
    │   ├── 사용자 대시보드  `user-dashboard` → `/page/user-dashboard`
    │   ├── 내 프로그램  `my-programs` → `/page/my-programs`
    │   └── 내 예약 보고서  `user-job` → `/page/user-job`
    ├── 결재/
    │   ├── 결재 위임  `common-approval-dlg` → `/page/common-approval-dlg`
    │   ├── 결재선 템플릿  `common-approval-tpl` → `/page/common-approval-tpl`
    │   └── 결재함  `common-approval` → `/page/common-approval` · entryPoint=approval
    ├── 문서/
    │   ├── 내가 만든 문서함  `common-doc-list` → `/page/common-doc-list`
    │   ├── 내 문서 이력  `common-doc-version` → `/page/common-doc-version`
    │   ├── 전자 양식  `ca-eform-list` → `/page/ca-eform-list`
    │   └── 원본 확인  `ca-common-doc-verify` → `/page/ca-common-doc-verify`
    ├── 소통/
    │   ├── 공지사항  `common-notice` → `/page/common-notice`
    │   ├── 자료실  `common-library` → `/page/common-library`
    │   ├── 자유게시판  `common-general` → `/page/common-general`
    │   ├── 자주 묻는 질문  `common-faq` → `/page/common-faq`
    │   ├── 주소록  `common-address-book` → `/page/common-address-book`
    │   ├── 조직도  `common-org-chart` → `/page/common-org-chart`
    │   ├── 문의  `common-inquiry` → `/page/common-inquiry` · entryPoint=inquiry
    │   └── 알림 센터  `common-notification` → `/page/common-notification` · entryPoint=notification
    ├── AI/
    │   ├── AI 브리핑  `ai-briefing` → `/page/ai-briefing`
    │   └── AI 어시스턴트  `common-ai-chat` → `/page/common-ai-chat` · entryPoint=aiWindow
    ├── 설문·동의/
    │   └── 동의서  `common-agreement` → `/page/common-agreement`
    └── 내 요청/
        └── 권한·겸직 신청  `ca-role-request` → `/page/ca-role-request`
```

## 2. 화면별 메뉴 경로 표

| 상단 탭 | 메뉴 경로 | serviceId | 화면 주소 |
|---|---|---|---|
| 공통 | 조직·사용자 › 조직 › **회사 관리** | `ca-company` | `http://localhost:8080/page/ca-company` |
| 공통 | 조직·사용자 › 조직 › **사업장 관리** | `ca-business-place` | `http://localhost:8080/page/ca-business-place` |
| 공통 | 조직·사용자 › 조직 › **부서/조직 정보** | `co_deptMng` | `http://localhost:8080/page/co_deptMng` |
| 공통 | 조직·사용자 › 조직 › **조직 구조 관리** | `ca-org-structure` | `http://localhost:8080/page/ca-org-structure` |
| 공통 | 조직·사용자 › 사용자 › **임직원 관리** | `ca-employee` | `http://localhost:8080/page/ca-employee` |
| 공통 | 조직·사용자 › 사용자 › **사용자 관리** | `ca-hmp-user-manage` | `http://localhost:8080/page/ca-hmp-user-manage` |
| 공통 | 조직·사용자 › 사용자 › **사용자 법인 소속 관리** | `ca-user-company` | `http://localhost:8080/page/ca-user-company` |
| 공통 | 조직·사용자 › 사용자 › **사용자 발령 관리** | `ca-org-assignment` | `http://localhost:8080/page/ca-org-assignment` |
| 공통 | 조직·사용자 › 협력업체 › **협력업체 관리** | `ca-partner` | `http://localhost:8080/page/ca-partner` |
| 공통 | 조직·사용자 › 협력업체 › **협력업체사용자 관리** | `ca-partner-user` | `http://localhost:8080/page/ca-partner-user` |
| 공통 | 권한 › 권한 정의 › **권한 관리** | `ca-role` | `http://localhost:8080/page/ca-role` |
| 공통 | 권한 › 권한 정의 › **공통권한 관리** | `ca-role-common` | `http://localhost:8080/page/ca-role-common` |
| 공통 | 권한 › 권한 정의 › **발령권한 관리** | `ca-role-rule` | `http://localhost:8080/page/ca-role-rule` |
| 공통 | 권한 › 권한 부여 › **권한/사용자매핑** | `ca-user-role` | `http://localhost:8080/page/ca-user-role` |
| 공통 | 권한 › 권한 부여 › **사용자/권한매핑** | `ca-user-role-by-user` | `http://localhost:8080/page/ca-user-role-by-user` |
| 공통 | 권한 › 권한 부여 › **지정권한 관리** | `ca-role-assign` | `http://localhost:8080/page/ca-role-assign` |
| 공통 | 권한 › 권한 부여 › **권한위임 관리** | `ca-role-delegation` | `http://localhost:8080/page/ca-role-delegation` |
| 공통 | 권한 › 권한 부여 › **사용자(모바일)** | `co_mobileAuthMapp` | `http://localhost:8080/page/co_mobileAuthMapp` |
| 공통 | 권한 › 권한 부여 › **사용자권한 조회** | `ca-user-role-inquiry` | `http://localhost:8080/page/ca-user-role-inquiry` |
| 공통 | 메뉴·업무그룹 › **업무그룹 관리** | `ca-biz-group` | `http://localhost:8080/page/ca-biz-group` |
| 공통 | 메뉴·업무그룹 › **메뉴트리관리** | `ca-service` | `http://localhost:8080/page/ca-service` |
| 공통 | 메뉴·업무그룹 › **권한/메뉴매핑** | `ca-role-service` | `http://localhost:8080/page/ca-role-service` |
| 공통 | 메뉴·업무그룹 › **매뉴얼** | `ca-manual-viewer` | `http://localhost:8080/page/ca-manual-viewer` |
| 공통 | 기준정보 › **코드 관리** | `ca-code` | `http://localhost:8080/page/ca-code` |
| 공통 | 기준정보 › **국가코드 관리** | `ca-country` | `http://localhost:8080/page/ca-country` |
| 공통 | 기준정보 › **화폐 관리** | `ca-currency` | `http://localhost:8080/page/ca-currency` |
| 공통 | 기준정보 › **환율 관리** | `ca-exchange-rate` | `http://localhost:8080/page/ca-exchange-rate` |
| 공통 | 기준정보 › **은행 관리** | `ca-bank` | `http://localhost:8080/page/ca-bank` |
| 공통 | 기준정보 › **공휴일 관리** | `ca-holiday` | `http://localhost:8080/page/ca-holiday` |
| 공통 | 기준정보 › **채번 규칙** | `ca-numbering-rule` | `http://localhost:8080/page/ca-numbering-rule` |
| 공통 | 기준정보 › **결정표** | `ca-decision-table` | `http://localhost:8080/page/ca-decision-table` |
| 공통 | 결재·문서 › 결재 › **결재 현황** | `ca-approval-dashboard` | `http://localhost:8080/page/ca-approval-dashboard` |
| 공통 | 결재·문서 › 결재 › **결재 양식 관리** | `ca-approval-form-template` | `http://localhost:8080/page/ca-approval-form-template` |
| 공통 | 결재·문서 › 결재 › **문서양식관리** | `co_elcDoc` | `http://localhost:8080/page/co_elcDoc` |
| 공통 | 결재·문서 › 문서 양식 › **문서 유형 관리** | `ca-smart-doc-type` | `http://localhost:8080/page/ca-smart-doc-type` |
| 공통 | 결재·문서 › 문서 양식 › **문서 템플릿** | `ca-doc-template` | `http://localhost:8080/page/ca-doc-template` |
| 공통 | 결재·문서 › 문서 양식 › **문서 템플릿 관리** | `ca-smart-doc-template` | `http://localhost:8080/page/ca-smart-doc-template` |
| 공통 | 결재·문서 › 문서 양식 › **전자 양식 디자이너** | `ca-eform-designer` | `http://localhost:8080/page/ca-eform-designer` |
| 공통 | 결재·문서 › 문서 관리 › **문서 탐색** | `ca-edms-browse` | `http://localhost:8080/page/ca-edms-browse` |
| 공통 | 결재·문서 › 문서 관리 › **문서 등록/편집** | `ca-edms-doc-manage` | `http://localhost:8080/page/ca-edms-doc-manage` |
| 공통 | 결재·문서 › 문서 관리 › **문서 보안 처리** | `ca-common-doc-protect` | `http://localhost:8080/page/ca-common-doc-protect` |
| 공통 | 결재·문서 › 문서 관리 › **보존 정책 관리** | `ca-retention-policy` | `http://localhost:8080/page/ca-retention-policy` |
| 공통 | 게시·알림 › 게시 › **게시판관리** | `ca-board-manage` | `http://localhost:8080/page/ca-board-manage` |
| 공통 | 게시·알림 › 게시 › **문의 관리** | `ca-inquiry` | `http://localhost:8080/page/ca-inquiry` |
| 공통 | 게시·알림 › 메시지·알림 › **통합 메시지 발송** | `ca-msg-send` | `http://localhost:8080/page/ca-msg-send` |
| 공통 | 게시·알림 › 메시지·알림 › **메시지템플릿 관리** | `ca-msg-template` | `http://localhost:8080/page/ca-msg-template` |
| 공통 | 게시·알림 › 메시지·알림 › **메시지 이력** | `ca-msg-history` | `http://localhost:8080/page/ca-msg-history` |
| 공통 | 게시·알림 › 메시지·알림 › **수신거부 관리** | `ca-msg-opt-out` | `http://localhost:8080/page/ca-msg-opt-out` |
| 공통 | 게시·알림 › 메시지·알림 › **알림 관리** | `ca-notification-manage` | `http://localhost:8080/page/ca-notification-manage` |
| 하자 | (바로 아래) › **하자** | `db_dashBoardCS` | `http://localhost:8080/page/db_dashBoardCS` |
| 하자 | 일정관리 › **일정관리** | `cs_schMng` | `http://localhost:8080/page/cs_schMng` |
| 하자 | 기본정보 › **현장정보** | `cs_siteInfo` | `http://localhost:8080/page/cs_siteInfo` |
| 하자 | 기본정보 › **협력업체정보** | `cs_copCoInfo` | `http://localhost:8080/page/cs_copCoInfo` |
| 하자 | 하자 등록 및 처리 › 공통 › **공종코드** | `cs_ctpCd` | `http://localhost:8080/page/cs_ctpCd` |
| 하자 | 하자 등록 및 처리 › 공통 › **현장별 공간 부위 코드** | `cs_sitSpacePartCd` | `http://localhost:8080/page/cs_sitSpacePartCd` |
| 하자 | 하자 등록 및 처리 › 공통 › **담당현장** | `cs_chrSite` | `http://localhost:8080/page/cs_chrSite` |
| 하자 | 하자 등록 및 처리 › 공통 › **기준정보 등록 요청** | `cs_baseCdReq` | `http://localhost:8080/page/cs_baseCdReq` |
| 하자 | 하자 등록 및 처리 › 공통 › **엔지니어 승인관리** | `cs_mobileUsrAprv` | `http://localhost:8080/page/cs_mobileUsrAprv` |
| 하자 | 하자 등록 및 처리 › 공통 › **기준정보 등록 승인** | `cs_baseCdAprv` | `http://localhost:8080/page/cs_baseCdAprv` |
| 하자 | 하자 등록 및 처리 › 공통 › **모바일사용자** | `cs_mobileUsr` | `http://localhost:8080/page/cs_mobileUsr` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **점검일정** | `cs_chkSch` | `http://localhost:8080/page/cs_chkSch` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **공종별협력업체** | `cs_bctyCopCo` | `http://localhost:8080/page/cs_bctyCopCo` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **세대정보** | `cs_hhldInfo` | `http://localhost:8080/page/cs_hhldInfo` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **하자분류 매칭** | `cs_ctpMatch` | `http://localhost:8080/page/cs_ctpMatch` |
| 하자 | 하자 등록 및 처리 › 준공전품질점검 › **품질점검** | `cs_csChkMng_qlt` | `http://localhost:8080/page/cs_csChkMng_qlt` |
| 하자 | 하자 등록 및 처리 › 준공전품질점검 › **주부품질점검** | `cs_csChkMng_hwf` | `http://localhost:8080/page/cs_csChkMng_hwf` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **사전점검** | `cs_csChkMng_bf` | `http://localhost:8080/page/cs_csChkMng_bf` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **사전방문 예약** | `cs_visitRsv` | `http://localhost:8080/page/cs_visitRsv` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **입주 지원** | `cs_mvinMng` | `http://localhost:8080/page/cs_mvinMng` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **표준인수인계파일등록** | `cs_tnhFile` | `http://localhost:8080/page/cs_tnhFile` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **현장인수인계요청** | `cs_siteTnhReq` | `http://localhost:8080/page/cs_siteTnhReq` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **현장인수인계승인** | `cs_siteTnhAprv` | `http://localhost:8080/page/cs_siteTnhAprv` |
| 하자 | 하자 등록 및 처리 › AS관리 › **A/S관리** | `cs_csChkMng` | `http://localhost:8080/page/cs_csChkMng` |
| 하자 | 하자 등록 및 처리 › AS관리 › **작업관리** | `cs_wrkCmplMng` | `http://localhost:8080/page/cs_wrkCmplMng` |
| 하자 | 하자 등록 및 처리 › AS관리 › **하자 문자발송** | `cs_csSendSms` | `http://localhost:8080/page/cs_csSendSms` |
| 하자 | 하자 등록 및 처리 › AS관리 › **분쟁 단지 현황** | `cs_dsptMng` | `http://localhost:8080/page/cs_dsptMng` |
| 하자 | 하자 등록 및 처리 › 점검내역대량등록 › **점검내역대량등록** | `cs_chkDtlsBlkReg` | `http://localhost:8080/page/cs_chkDtlsBlkReg` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자처리현황** | `cs_disComCurst` | `http://localhost:8080/page/cs_disComCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자 접수 및 처리대장** | `cs_dealAcp` | `http://localhost:8080/page/cs_dealAcp` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **협력업체별현황** | `cs_byCopCoCurst` | `http://localhost:8080/page/cs_byCopCoCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **협력업체공종별현황** | `cs_byCopCoCtpCurst` | `http://localhost:8080/page/cs_byCopCoCtpCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **유형별현황** | `cs_byTpCurst` | `http://localhost:8080/page/cs_byTpCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **공간/부위별 접수처리 현황** | `cs_spaceCurst` | `http://localhost:8080/page/cs_spaceCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **현장별 AS접수 통계** | `cs_bySiteAcpStats` | `http://localhost:8080/page/cs_bySiteAcpStats` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **공종별 처리현황** | `cs_ctpDiscomCurst` | `http://localhost:8080/page/cs_ctpDiscomCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **업체별 처리현황** | `cs_cooDiscomCurst` | `http://localhost:8080/page/cs_cooDiscomCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **장기 미처리 하자 관리** | `cs_ltrmDefect` | `http://localhost:8080/page/cs_ltrmDefect` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자 통합 분석** | `cs_defectAnalysis` | `http://localhost:8080/page/cs_defectAnalysis` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **협력사 이행 현황** | `cs_copCoPerf` | `http://localhost:8080/page/cs_copCoPerf` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **중점 관리 단지** | `cs_keySite` | `http://localhost:8080/page/cs_keySite` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **센터장 일일보고** | `cs_ctrDailyRpt` | `http://localhost:8080/page/cs_ctrDailyRpt` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **단지별 조직도** | `cs_siteOrg` | `http://localhost:8080/page/cs_siteOrg` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **재무 연계 하자 분석** | `cs_finDefect` | `http://localhost:8080/page/cs_finDefect` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **접수 건 추적** | `cs_acpTrack` | `http://localhost:8080/page/cs_acpTrack` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **확인점검 결과 대시보드** | `cs_chkDashboard` | `http://localhost:8080/page/cs_chkDashboard` |
| 하자 | 하자처리보고 › **하자처리보고서** | `cs_dealRep` | `http://localhost:8080/page/cs_dealRep` |
| 하자 | 하자보수비 › **비율등록** | `cs_asByyyBgtCd` | `http://localhost:8080/page/cs_asByyyBgtCd` |
| 하자 | 하자보수비 › **연도별예산관리** | `cs_asByyyBgt` | `http://localhost:8080/page/cs_asByyyBgt` |
| 하자 | 하자보수비 › **집행실적관리** | `cs_asSiteCs` | `http://localhost:8080/page/cs_asSiteCs` |
| 하자 | 하자보수비 › **비용 집행** | `cs_costClaim` | `http://localhost:8080/page/cs_costClaim` |
| 하자 | 하자보수비 › **보증금 청구 관리** | `cs_gntClm` | `http://localhost:8080/page/cs_gntClm` |
| 하자 | 하자보수비 › 하자전표관리 › **공동도급안분** | `cs_jonitVenture` | `http://localhost:8080/page/cs_jonitVenture` |
| 하자 | 하자보수비 › 현황조회 › **현장종합 투입금액** | `cs_siteTotCst` | `http://localhost:8080/page/cs_siteTotCst` |
| 하자 | 하자보수비 › 현황조회 › **자금집계표** | `cs_siteMoneyCst` | `http://localhost:8080/page/cs_siteMoneyCst` |
| 하자 | 하자보수비 › 현황조회 › **월별 하자보수비 현황** | `cs_bymAsExpIptCurst` | `http://localhost:8080/page/cs_bymAsExpIptCurst` |
| 하자 | 하자보수비 › 현황조회 › **일반관리비 실적** | `cs_generlCst` | `http://localhost:8080/page/cs_generlCst` |
| 하자 | 하자보수비 › 현황조회 › **보증현황** | `cs_sponSionCst` | `http://localhost:8080/page/cs_sponSionCst` |
| 하자 | 하자보수비 › 현황조회 › **전표조회** | `cs_asSlpCst` | `http://localhost:8080/page/cs_asSlpCst` |
| 하자 | (바로 아래) › **협력업체 공지사항** | `cs_jcAnncBrdLst` | `http://localhost:8080/page/cs_jcAnncBrdLst` |
| 하자 | (바로 아래) › **발주 내역** | `cs_ordDtls` | `http://localhost:8080/page/cs_ordDtls` |
| 시스템 | 조직·사용자 › 조직 › **회사 관리** | `sys-ca-company` | `http://localhost:8080/page/sys-ca-company` |
| 시스템 | 조직·사용자 › 조직 › **사업장 관리** | `sys-ca-business-place` | `http://localhost:8080/page/sys-ca-business-place` |
| 시스템 | 조직·사용자 › 조직 › **부서/조직 정보** | `sys-co_deptMng` | `http://localhost:8080/page/sys-co_deptMng` |
| 시스템 | 조직·사용자 › 조직 › **조직 구조 관리** | `sys-ca-org-structure` | `http://localhost:8080/page/sys-ca-org-structure` |
| 시스템 | 조직·사용자 › 사용자 › **임직원 관리** | `sys-ca-employee` | `http://localhost:8080/page/sys-ca-employee` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 관리** | `sys-ca-hmp-user-manage` | `http://localhost:8080/page/sys-ca-hmp-user-manage` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 법인 소속 관리** | `sys-ca-user-company` | `http://localhost:8080/page/sys-ca-user-company` |
| 시스템 | 조직·사용자 › 사용자 › **사용자 발령 관리** | `sys-ca-org-assignment` | `http://localhost:8080/page/sys-ca-org-assignment` |
| 시스템 | 조직·사용자 › 협력업체 › **협력업체 관리** | `sys-ca-partner` | `http://localhost:8080/page/sys-ca-partner` |
| 시스템 | 조직·사용자 › 협력업체 › **협력업체사용자 관리** | `sys-ca-partner-user` | `http://localhost:8080/page/sys-ca-partner-user` |
| 시스템 | 권한 › 권한 정의 › **권한 관리** | `sys-ca-role` | `http://localhost:8080/page/sys-ca-role` |
| 시스템 | 권한 › 권한 정의 › **공통권한 관리** | `sys-ca-role-common` | `http://localhost:8080/page/sys-ca-role-common` |
| 시스템 | 권한 › 권한 정의 › **발령권한 관리** | `sys-ca-role-rule` | `http://localhost:8080/page/sys-ca-role-rule` |
| 시스템 | 권한 › 권한 부여 › **권한/사용자매핑** | `sys-ca-user-role` | `http://localhost:8080/page/sys-ca-user-role` |
| 시스템 | 권한 › 권한 부여 › **사용자/권한매핑** | `sys-ca-user-role-by-user` | `http://localhost:8080/page/sys-ca-user-role-by-user` |
| 시스템 | 권한 › 권한 부여 › **지정권한 관리** | `sys-ca-role-assign` | `http://localhost:8080/page/sys-ca-role-assign` |
| 시스템 | 권한 › 권한 부여 › **권한위임 관리** | `sys-ca-role-delegation` | `http://localhost:8080/page/sys-ca-role-delegation` |
| 시스템 | 권한 › 권한 부여 › **사용자(모바일)** | `sys-co_mobileAuthMapp` | `http://localhost:8080/page/sys-co_mobileAuthMapp` |
| 시스템 | 권한 › 권한 부여 › **사용자권한 조회** | `sys-ca-user-role-inquiry` | `http://localhost:8080/page/sys-ca-user-role-inquiry` |
| 시스템 | 메뉴·업무그룹 › **업무그룹 관리** | `sys-ca-biz-group` | `http://localhost:8080/page/sys-ca-biz-group` |
| 시스템 | 메뉴·업무그룹 › **프로그램관리** | `ca-program` | `http://localhost:8080/page/ca-program` |
| 시스템 | 메뉴·업무그룹 › **메뉴트리관리** | `sys-ca-service` | `http://localhost:8080/page/sys-ca-service` |
| 시스템 | 메뉴·업무그룹 › **권한/메뉴매핑** | `sys-ca-role-service` | `http://localhost:8080/page/sys-ca-role-service` |
| 시스템 | 메뉴·업무그룹 › **매뉴얼** | `sys-ca-manual-viewer` | `http://localhost:8080/page/sys-ca-manual-viewer` |
| 시스템 | 기준정보 › **코드 관리** | `sys-ca-code` | `http://localhost:8080/page/sys-ca-code` |
| 시스템 | 기준정보 › **국가코드 관리** | `sys-ca-country` | `http://localhost:8080/page/sys-ca-country` |
| 시스템 | 기준정보 › **화폐 관리** | `sys-ca-currency` | `http://localhost:8080/page/sys-ca-currency` |
| 시스템 | 기준정보 › **환율 관리** | `sys-ca-exchange-rate` | `http://localhost:8080/page/sys-ca-exchange-rate` |
| 시스템 | 기준정보 › **은행 관리** | `sys-ca-bank` | `http://localhost:8080/page/sys-ca-bank` |
| 시스템 | 기준정보 › **공휴일 관리** | `sys-ca-holiday` | `http://localhost:8080/page/sys-ca-holiday` |
| 시스템 | 기준정보 › **채번 규칙** | `sys-ca-numbering-rule` | `http://localhost:8080/page/sys-ca-numbering-rule` |
| 시스템 | 기준정보 › **결정표** | `sys-ca-decision-table` | `http://localhost:8080/page/sys-ca-decision-table` |
| 시스템 | 결재·문서 › 결재 › **결재 현황** | `sys-ca-approval-dashboard` | `http://localhost:8080/page/sys-ca-approval-dashboard` |
| 시스템 | 결재·문서 › 결재 › **결재 양식 관리** | `sys-ca-approval-form-template` | `http://localhost:8080/page/sys-ca-approval-form-template` |
| 시스템 | 결재·문서 › 결재 › **문서양식관리** | `sys-co_elcDoc` | `http://localhost:8080/page/sys-co_elcDoc` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 유형 관리** | `sys-ca-smart-doc-type` | `http://localhost:8080/page/sys-ca-smart-doc-type` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 템플릿** | `sys-ca-doc-template` | `http://localhost:8080/page/sys-ca-doc-template` |
| 시스템 | 결재·문서 › 문서 양식 › **문서 템플릿 관리** | `sys-ca-smart-doc-template` | `http://localhost:8080/page/sys-ca-smart-doc-template` |
| 시스템 | 결재·문서 › 문서 양식 › **전자 양식 디자이너** | `sys-ca-eform-designer` | `http://localhost:8080/page/sys-ca-eform-designer` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 탐색** | `sys-ca-edms-browse` | `http://localhost:8080/page/sys-ca-edms-browse` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 등록/편집** | `sys-ca-edms-doc-manage` | `http://localhost:8080/page/sys-ca-edms-doc-manage` |
| 시스템 | 결재·문서 › 문서 관리 › **문서 보안 처리** | `sys-ca-common-doc-protect` | `http://localhost:8080/page/sys-ca-common-doc-protect` |
| 시스템 | 결재·문서 › 문서 관리 › **보존 정책 관리** | `sys-ca-retention-policy` | `http://localhost:8080/page/sys-ca-retention-policy` |
| 시스템 | 게시·알림 › 게시 › **게시판관리** | `sys-ca-board-manage` | `http://localhost:8080/page/sys-ca-board-manage` |
| 시스템 | 게시·알림 › 게시 › **문의 관리** | `sys-ca-inquiry` | `http://localhost:8080/page/sys-ca-inquiry` |
| 시스템 | 게시·알림 › 메시지·알림 › **통합 메시지 발송** | `sys-ca-msg-send` | `http://localhost:8080/page/sys-ca-msg-send` |
| 시스템 | 게시·알림 › 메시지·알림 › **메시지템플릿 관리** | `sys-ca-msg-template` | `http://localhost:8080/page/sys-ca-msg-template` |
| 시스템 | 게시·알림 › 메시지·알림 › **메시지 이력** | `sys-ca-msg-history` | `http://localhost:8080/page/sys-ca-msg-history` |
| 시스템 | 게시·알림 › 메시지·알림 › **수신거부 관리** | `sys-ca-msg-opt-out` | `http://localhost:8080/page/sys-ca-msg-opt-out` |
| 시스템 | 게시·알림 › 메시지·알림 › **알림 관리** | `sys-ca-notification-manage` | `http://localhost:8080/page/sys-ca-notification-manage` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **시스템 관리** | `ca-system` | `http://localhost:8080/page/ca-system` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **기능모듈 관리** | `ca-feature-module` | `http://localhost:8080/page/ca-feature-module` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **기능 플래그** | `ca-feature-flag` | `http://localhost:8080/page/ca-feature-flag` |
| 시스템 | 플랫폼 › 시스템·프로그램 › **시스템 가이드** | `ca-system-guide` | `http://localhost:8080/page/ca-system-guide` |
| 시스템 | 플랫폼 › 설정·정책 › **설정 관리** | `ca-config` | `http://localhost:8080/page/ca-config` |
| 시스템 | 플랫폼 › 설정·정책 › **데이터소스 관리** | `ca-datasource` | `http://localhost:8080/page/ca-datasource` |
| 시스템 | 플랫폼 › 설정·정책 › **데이터캐시정책 관리** | `ca-data-cache-policy` | `http://localhost:8080/page/ca-data-cache-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **HTTP캐시정책 관리** | `ca-http-cache-policy` | `http://localhost:8080/page/ca-http-cache-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **HTTP정책 관리** | `ca-http-policy` | `http://localhost:8080/page/ca-http-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **페이지정책 관리** | `ca-page-policy` | `http://localhost:8080/page/ca-page-policy` |
| 시스템 | 플랫폼 › 설정·정책 › **홈 화면 규칙** | `ca-home-service-rule` | `http://localhost:8080/page/ca-home-service-rule` |
| 시스템 | 플랫폼 › 다국어 › **언어코드 관리** | `ca-language` | `http://localhost:8080/page/ca-language` |
| 시스템 | 플랫폼 › 다국어 › **시스템언어 관리** | `ca-system-language` | `http://localhost:8080/page/ca-system-language` |
| 시스템 | 플랫폼 › 다국어 › **라벨 관리** | `ca-label` | `http://localhost:8080/page/ca-label` |
| 시스템 | 플랫폼 › 다국어 › **메시지 관리** | `ca-message` | `http://localhost:8080/page/ca-message` |
| 시스템 | 플랫폼 › 다국어 › **포맷정책 관리** | `ca-format-policy` | `http://localhost:8080/page/ca-format-policy` |
| 시스템 | 보안 › 인증 › **인증정책 관리** | `ca-auth-policy` | `http://localhost:8080/page/ca-auth-policy` |
| 시스템 | 보안 › 인증 › **통합 인증 공급자** | `ca-auth-provider` | `http://localhost:8080/page/ca-auth-provider` |
| 시스템 | 보안 › 인증 › **SCIM 2.0 공급자** | `ca-scim-provider` | `http://localhost:8080/page/ca-scim-provider` |
| 시스템 | 보안 › 인증 › **인증항목패턴 관리** | `ca-auth-item-pattern` | `http://localhost:8080/page/ca-auth-item-pattern` |
| 시스템 | 보안 › 인증 › **사용자 세션 관리** | `ca-user-session` | `http://localhost:8080/page/ca-user-session` |
| 시스템 | 보안 › 인증 › **외부 사용자 초대·승인·만료** | `ca-external-access` | `http://localhost:8080/page/ca-external-access` |
| 시스템 | 보안 › 접근 통제 › **허용IP 관리** | `ca-allowed-ip` | `http://localhost:8080/page/ca-allowed-ip` |
| 시스템 | 보안 › 접근 통제 › **내부IP 관리** | `ca-internal-ip` | `http://localhost:8080/page/ca-internal-ip` |
| 시스템 | 보안 › 접근 통제 › **CORS 허용도메인 관리** | `ca-cors-allowed-origin` | `http://localhost:8080/page/ca-cors-allowed-origin` |
| 시스템 | 보안 › 접근 통제 › **접근정책 관리** | `ca-network-access-policy` | `http://localhost:8080/page/ca-network-access-policy` |
| 시스템 | 보안 › 접근 통제 › **서비스접근 관리** | `ca-service-access` | `http://localhost:8080/page/ca-service-access` |
| 시스템 | 보안 › 접근 통제 › **접근 정책 규칙 관리** | `ca-access-rule` | `http://localhost:8080/page/ca-access-rule` |
| 시스템 | 보안 › 접근 통제 › **보안정책 관리** | `ca-security-policy` | `http://localhost:8080/page/ca-security-policy` |
| 시스템 | 보안 › 권한 정책 › **ABAC 접근 정책 관리** | `ca-access-policy` | `http://localhost:8080/page/ca-access-policy` |
| 시스템 | 보안 › 권한 정책 › **ReBAC 권한 튜플 관리** | `ca-permission-tuple` | `http://localhost:8080/page/ca-permission-tuple` |
| 시스템 | 보안 › 권한 정책 › **권한 매트릭스** | `ca-auth-matrix` | `http://localhost:8080/page/ca-auth-matrix` |
| 시스템 | 보안 › 권한 정책 › **권한조절 공급자** | `ca-auth-adjust-provider` | `http://localhost:8080/page/ca-auth-adjust-provider` |
| 시스템 | 보안 › 개인정보 › **개인정보컬럼 관리** | `ca-privacy-column` | `http://localhost:8080/page/ca-privacy-column` |
| 시스템 | 보안 › 개인정보 › **개인정보 보존/파기** | `ca-privacy-retention` | `http://localhost:8080/page/ca-privacy-retention` |
| 시스템 | 보안 › 개인정보 › **개인정보 로그** | `ca-log-privacy` | `http://localhost:8080/page/ca-log-privacy` |
| 시스템 | 보안 › 개인정보 › **암호화 도구** | `ca-crypto-tool` | `http://localhost:8080/page/ca-crypto-tool` |
| 시스템 | 운영·로그 › 로그 › **오류 로그** | `ca-log-error` | `http://localhost:8080/page/ca-log-error` |
| 시스템 | 운영·로그 › 로그 › **로그인 로그** | `ca-log-login` | `http://localhost:8080/page/ca-log-login` |
| 시스템 | 운영·로그 › 로그 › **페이지 로그** | `ca-log-page` | `http://localhost:8080/page/ca-log-page` |
| 시스템 | 운영·로그 › 로그 › **API 로그** | `ca-log-service` | `http://localhost:8080/page/ca-log-service` |
| 시스템 | 운영·로그 › 로그 › **파일다운로드 이력** | `ca-log-file-download` | `http://localhost:8080/page/ca-log-file-download` |
| 시스템 | 운영·로그 › 로그 › **권한메뉴 로그** | `ca-log-role-service` | `http://localhost:8080/page/ca-log-role-service` |
| 시스템 | 운영·로그 › 로그 › **권한부여 로그** | `ca-log-role-grant` | `http://localhost:8080/page/ca-log-role-grant` |
| 시스템 | 운영·로그 › 로그 › **로그 대시보드** | `ca-log-dashboard` | `http://localhost:8080/page/ca-log-dashboard` |
| 시스템 | 운영·로그 › 로그 › **로그설정 관리** | `ca-log-config` | `http://localhost:8080/page/ca-log-config` |
| 시스템 | 운영·로그 › 로그 › **로그 파일 뷰어** | `ca-log-file-viewer` | `http://localhost:8080/page/ca-log-file-viewer` |
| 시스템 | 운영·로그 › 모니터링 › **모니터링 대시보드** | `ca-monitoring-dashboard` | `http://localhost:8080/page/ca-monitoring-dashboard` |
| 시스템 | 운영·로그 › 모니터링 › **성능 인사이트** | `ca-performance-insight` | `http://localhost:8080/page/ca-performance-insight` |
| 시스템 | 운영·로그 › 모니터링 › **모듈 상태** | `ca-module-health` | `http://localhost:8080/page/ca-module-health` |
| 시스템 | 운영·로그 › 모니터링 › **화면 모니터링** | `ca-screen-viewer` | `http://localhost:8080/page/ca-screen-viewer` |
| 시스템 | 운영·로그 › 작업·파일 › **스케줄러 관리** | `ca-scheduler` | `http://localhost:8080/page/ca-scheduler` |
| 시스템 | 운영·로그 › 작업·파일 › **스케줄러 실행이력** | `ca-scheduler-history` | `http://localhost:8080/page/ca-scheduler-history` |
| 시스템 | 운영·로그 › 작업·파일 › **AI 스케줄러 운영** | `ca-ai-scheduler` | `http://localhost:8080/page/ca-ai-scheduler` |
| 시스템 | 운영·로그 › 작업·파일 › **대량처리 작업** | `ca-excel-job` | `http://localhost:8080/page/ca-excel-job` |
| 시스템 | 운영·로그 › 작업·파일 › **점검 모드** | `ca-maintenance-mode` | `http://localhost:8080/page/ca-maintenance-mode` |
| 시스템 | 운영·로그 › 작업·파일 › **파일 관리** | `ca-attached-file` | `http://localhost:8080/page/ca-attached-file` |
| 시스템 | 운영·로그 › 작업·파일 › **파일그룹 관리** | `ca-file-group` | `http://localhost:8080/page/ca-file-group` |
| 시스템 | 운영·로그 › 작업·파일 › **서버파일 관리** | `ca-server-file` | `http://localhost:8080/page/ca-server-file` |
| 시스템 | 연동 › EAI › **EAI 어댑터 설정** | `ca-eai-adapter` | `http://localhost:8080/page/ca-eai-adapter` |
| 시스템 | 연동 › EAI › **연계 커넥터** | `ca-eai-connector` | `http://localhost:8080/page/ca-eai-connector` |
| 시스템 | 연동 › EAI › **EAI 인터페이스 관리** | `ca-eai-interface` | `http://localhost:8080/page/ca-eai-interface` |
| 시스템 | 연동 › EAI › **EAI 실행 이력** | `ca-eai-log` | `http://localhost:8080/page/ca-eai-log` |
| 시스템 | 연동 › EAI › **EAI 모니터링** | `ca-eai-monitor` | `http://localhost:8080/page/ca-eai-monitor` |
| 시스템 | 연동 › EAI › **외부API 관리** | `ca-eai-system` | `http://localhost:8080/page/ca-eai-system` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 매핑** | `ca-sync-mapping` | `http://localhost:8080/page/ca-sync-mapping` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 스케줄** | `ca-sync-schedule` | `http://localhost:8080/page/ca-sync-schedule` |
| 시스템 | 연동 › 외부 동기화 › **외부 동기화 로그** | `ca-sync-log` | `http://localhost:8080/page/ca-sync-log` |
| 시스템 | 연동 › 결재·메시지 연동 › **GW결재 목록** | `ca-gw-approval-list` | `http://localhost:8080/page/ca-gw-approval-list` |
| 시스템 | 연동 › 결재·메시지 연동 › **GW결재 콜백 관리** | `ca-gw-approval-callback` | `http://localhost:8080/page/ca-gw-approval-callback` |
| 시스템 | 연동 › 결재·메시지 연동 › **결재 체이닝** | `ca-approval-chain-manage` | `http://localhost:8080/page/ca-approval-chain-manage` |
| 시스템 | 연동 › 결재·메시지 연동 › **메시지채널 관리** | `ca-msg-channel` | `http://localhost:8080/page/ca-msg-channel` |
| 시스템 | 연동 › 결재·메시지 연동 › **웹훅** | `ca-webhook` | `http://localhost:8080/page/ca-webhook` |
| 시스템 | 연동 › 결재·메시지 연동 › **단축 URL** | `ca-short-url` | `http://localhost:8080/page/ca-short-url` |
| 시스템 | 문서 설정 › **문서 편집기 설정** | `ca-smart-doc-editor-config` | `http://localhost:8080/page/ca-smart-doc-editor-config` |
| 시스템 | 문서 설정 › **문서 접근 로그** | `ca-smart-doc-access-log` | `http://localhost:8080/page/ca-smart-doc-access-log` |
| 시스템 | 문서 설정 › **폐기/아카이브 이력** | `ca-smart-doc-disposal-log` | `http://localhost:8080/page/ca-smart-doc-disposal-log` |
| 시스템 | 문서 설정 › **워터마크 설정** | `ca-watermark-config` | `http://localhost:8080/page/ca-watermark-config` |
| 시스템 | 문서 설정 › **DRM설정 관리** | `ca-drm-config` | `http://localhost:8080/page/ca-drm-config` |
| 시스템 | AI › AI 설정 › **AI 연결** | `ca-ai-provider` | `http://localhost:8080/page/ca-ai-provider` |
| 시스템 | AI › AI 설정 › **AI 가 볼 수 있는 데이터** | `ca-ai-admin` | `http://localhost:8080/page/ca-ai-admin` |
| 시스템 | AI › AI 설정 › **프롬프트 규칙** | `ca-ai-prompt-template` | `http://localhost:8080/page/ca-ai-prompt-template` |
| 시스템 | AI › AI 설정 › **스키마 컨텍스트** | `ca-ai-schema-context` | `http://localhost:8080/page/ca-ai-schema-context` |
| 시스템 | AI › AI 설정 › **외부 컬럼 매핑** | `ca-ai-external-column-map` | `http://localhost:8080/page/ca-ai-external-column-map` |
| 시스템 | AI › AI 설정 › **이상감지 규칙** | `ca-ai-anomaly-rule` | `http://localhost:8080/page/ca-ai-anomaly-rule` |
| 시스템 | AI › AI 설정 › **AI 사용량·한도** | `ca-ai-usage-dashboard` | `http://localhost:8080/page/ca-ai-usage-dashboard` |
| 시스템 | AI › AI 설정 › **AI 권한 설명기** | `ca-ai-access-explainer` | `http://localhost:8080/page/ca-ai-access-explainer` |
| 시스템 | AI › 에이전트 › **AI 계획 승인** | `ai-plan-approval` | `http://localhost:8080/page/ai-plan-approval` |
| 시스템 | AI › 에이전트 › **개선안 보고서** | `ai-improvement` | `http://localhost:8080/page/ai-improvement` |
| 시스템 | AI › 에이전트 › **사용자 프로그램 관리** | `ca-user-program-admin` | `http://localhost:8080/page/ca-user-program-admin` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 등록** | `ca-screen-registration` | `http://localhost:8080/page/ca-screen-registration` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 빌더** | `ca-screen-builder` | `http://localhost:8080/page/ca-screen-builder` |
| 시스템 | 개발도구 › 화면·코드 생성 › **서버 빌더** | `ca-server-builder` | `http://localhost:8080/page/ca-server-builder` |
| 시스템 | 개발도구 › 화면·코드 생성 › **화면 소스 편집** | `ca-page-source-editor` | `http://localhost:8080/page/ca-page-source-editor` |
| 시스템 | 개발도구 › 화면·코드 생성 › **생성 마법사** | `ca-scaffold-wizard` | `http://localhost:8080/page/ca-scaffold-wizard` |
| 시스템 | 개발도구 › 화면·코드 생성 › **커스텀 컴포넌트 빌더** | `ca-custom-component` | `http://localhost:8080/page/ca-custom-component` |
| 시스템 | 개발도구 › 화면·코드 생성 › **커스텀 필드 관리** | `ca-custom-field` | `http://localhost:8080/page/ca-custom-field` |
| 시스템 | 개발도구 › 화면·코드 생성 › **스니펫관리** | `ca-snippet-manage` | `http://localhost:8080/page/ca-snippet-manage` |
| 시스템 | 개발도구 › 화면·코드 생성 › **문서 정의 편집** | `document-definition-editor` | `http://localhost:8080/page/document-definition-editor` |
| 시스템 | 개발도구 › DB·데이터 › **DB 탐색기** | `ca-db-explorer` | `http://localhost:8080/page/ca-db-explorer` |
| 시스템 | 개발도구 › DB·데이터 › **DB 빌더** | `ca-db-builder` | `http://localhost:8080/page/ca-db-builder` |
| 시스템 | 개발도구 › DB·데이터 › **DDL 실행 이력** | `ca-ddl-history` | `http://localhost:8080/page/ca-ddl-history` |
| 시스템 | 개발도구 › DB·데이터 › **데이터 표준 사전** | `ca-data-standard` | `http://localhost:8080/page/ca-data-standard` |
| 시스템 | 개발도구 › DB·데이터 › **이력 조회** | `ca-entity-version` | `http://localhost:8080/page/ca-entity-version` |
| 시스템 | 개발도구 › DB·데이터 › **CRUD 매트릭스** | `ca-crud-matrix` | `http://localhost:8080/page/ca-crud-matrix` |
| 시스템 | 개발도구 › DB·데이터 › **i18n 스캔** | `ca-i18n-scan` | `http://localhost:8080/page/ca-i18n-scan` |
| 시스템 | 개발도구 › 테스트 › **시험 현황** | `test-dashboard` | `http://localhost:8080/page/test-dashboard` |
| 시스템 | 개발도구 › 테스트 › **시험 대상** | `test-target` | `http://localhost:8080/page/test-target` |
| 시스템 | 개발도구 › 테스트 › **시험 사례** | `test-scenario` | `http://localhost:8080/page/test-scenario` |
| 시스템 | 개발도구 › 테스트 › **시험 묶음** | `test-suite` | `http://localhost:8080/page/test-suite` |
| 시스템 | 개발도구 › 테스트 › **시험 실행** | `test-run` | `http://localhost:8080/page/test-run` |
| 시스템 | 개발도구 › 테스트 › **테스트 점검항목** | `test-check-item` | `http://localhost:8080/page/test-check-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 대상 서비스** | `test-service-item` | `http://localhost:8080/page/test-service-item` |
| 시스템 | 개발도구 › 테스트 › **테스트 커버리지** | `ca-test-coverage` | `http://localhost:8080/page/ca-test-coverage` |
| 시스템 | 개발도구 › 테스트 › **테스트 데이터 팩토리** | `ca-test-data-factory` | `http://localhost:8080/page/ca-test-data-factory` |
| 시스템 | 개발도구 › 테스트 › **API 테스트** | `ca-api-playground` | `http://localhost:8080/page/ca-api-playground` |
| 시스템 | 개발도구 › 운영 도구 › **DevOps 콘솔** | `ca-devops-console` | `http://localhost:8080/page/ca-devops-console` |
| 시스템 | 개발도구 › 운영 도구 › **Hotfix 관리** | `ca-hotfix` | `http://localhost:8080/page/ca-hotfix` |
| 시스템 | 개발도구 › 운영 도구 › **디버그정책 관리** | `ca-debug-policy` | `http://localhost:8080/page/ca-debug-policy` |
| 시스템 | 개발도구 › 운영 도구 › **개발모듈오버라이드** | `ca-dev-module-override` | `http://localhost:8080/page/ca-dev-module-override` |
| 시스템 | 개발도구 › 운영 도구 › **서비스 의존성** | `ca-service-dependency` | `http://localhost:8080/page/ca-service-dependency` |
| 시스템 | 개발도구 › 확장 개발 › **페이지 확장** | `ca-page-extension` | `http://localhost:8080/page/ca-page-extension` |
| 개인 | 마이페이지 › **개인설정** | `common-preference` | `http://localhost:8080/page/common-preference` |
| 개인 | 마이페이지 › **캘린더** | `common-calendar` | `http://localhost:8080/page/common-calendar` |
| 개인 | 마이페이지 › **내 정보** | `common-my-page` | `http://localhost:8080/page/common-my-page` |
| 개인 | 마이페이지 › **사용자 대시보드** | `user-dashboard` | `http://localhost:8080/page/user-dashboard` |
| 개인 | 마이페이지 › **내 프로그램** | `my-programs` | `http://localhost:8080/page/my-programs` |
| 개인 | 마이페이지 › **내 예약 보고서** | `user-job` | `http://localhost:8080/page/user-job` |
| 개인 | 결재 › **결재 위임** | `common-approval-dlg` | `http://localhost:8080/page/common-approval-dlg` |
| 개인 | 결재 › **결재선 템플릿** | `common-approval-tpl` | `http://localhost:8080/page/common-approval-tpl` |
| 개인 | 결재 › **결재함** | `common-approval` | `http://localhost:8080/page/common-approval` |
| 개인 | 문서 › **내가 만든 문서함** | `common-doc-list` | `http://localhost:8080/page/common-doc-list` |
| 개인 | 문서 › **내 문서 이력** | `common-doc-version` | `http://localhost:8080/page/common-doc-version` |
| 개인 | 문서 › **전자 양식** | `ca-eform-list` | `http://localhost:8080/page/ca-eform-list` |
| 개인 | 문서 › **원본 확인** | `ca-common-doc-verify` | `http://localhost:8080/page/ca-common-doc-verify` |
| 개인 | 소통 › **공지사항** | `common-notice` | `http://localhost:8080/page/common-notice` |
| 개인 | 소통 › **자료실** | `common-library` | `http://localhost:8080/page/common-library` |
| 개인 | 소통 › **자유게시판** | `common-general` | `http://localhost:8080/page/common-general` |
| 개인 | 소통 › **자주 묻는 질문** | `common-faq` | `http://localhost:8080/page/common-faq` |
| 개인 | 소통 › **주소록** | `common-address-book` | `http://localhost:8080/page/common-address-book` |
| 개인 | 소통 › **조직도** | `common-org-chart` | `http://localhost:8080/page/common-org-chart` |
| 개인 | 소통 › **문의** | `common-inquiry` | `http://localhost:8080/page/common-inquiry` |
| 개인 | 소통 › **알림 센터** | `common-notification` | `http://localhost:8080/page/common-notification` |
| 개인 | AI › **AI 브리핑** | `ai-briefing` | `http://localhost:8080/page/ai-briefing` |
| 개인 | AI › **AI 어시스턴트** | `common-ai-chat` | `http://localhost:8080/page/common-ai-chat` |
| 개인 | 설문·동의 › **동의서** | `common-agreement` | `http://localhost:8080/page/common-agreement` |
| 개인 | 내 요청 › **권한·겸직 신청** | `ca-role-request` | `http://localhost:8080/page/ca-role-request` |
