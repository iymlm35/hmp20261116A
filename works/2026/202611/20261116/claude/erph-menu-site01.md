# erp-h 상단메뉴별 left메뉴 트리 — 로그인 계정 「현장담당 (봉덕2차 휴먼아파트 신축공사)」

- 조회: 2026-11-16 · `http://localhost:8080/` · 화면 우상단 사용자 표시 「현장담당 (봉덕2차 휴먼아파트 신축공사)」 (login id 는 화면에 안 나와 확인하지 못함 · `TEST-ACCOUNTS-20261112.md` 기준 `h.site01`(현장담당)으로 추정)
- 방법: Aside 브라우저로 상단 탭을 눌러 `.menu-tree` DOM 을 읽고, 메뉴 API `POST /sys/menu/list` 의 `serviceId` 와 화면 이름·순서를 대조했다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>`. 폴더는 `이름/`. 화면 주소는 규칙에 따른 것이며 주소창과 직접 대조하지 않았다.
- 다른 계정 결과: 같은 폴더의 `erph-menu-*.md`

| 상단 탭 | 폴더 | 화면 | DOM ↔ API 대조 |
|---|---:|---:|---|
| 하자 | 12 | 30 | DOM 30 · API 30 · 이름·순서 일치 |
| 개인 | 7 | 25 | 상단 탭에 안 나옴 (API 만) |

## 1. 트리

```text
상단메뉴
├── [하자]  key=MENU_CS (폴더 12 · 화면 30)
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
│   │   │   └── 모바일사용자  `cs_mobileUsr` → `/page/cs_mobileUsr`
│   │   ├── 기초자료/
│   │   │   ├── 점검일정  `cs_chkSch` → `/page/cs_chkSch`
│   │   │   ├── 공종별협력업체  `cs_bctyCopCo` → `/page/cs_bctyCopCo`
│   │   │   └── 세대정보  `cs_hhldInfo` → `/page/cs_hhldInfo`
│   │   ├── 준공전품질점검/
│   │   │   ├── 품질점검  `cs_csChkMng_qlt` → `/page/cs_csChkMng_qlt`
│   │   │   └── 주부품질점검  `cs_csChkMng_hwf` → `/page/cs_csChkMng_hwf`
│   │   ├── 입주자사전점검/
│   │   │   ├── 사전점검  `cs_csChkMng_bf` → `/page/cs_csChkMng_bf`
│   │   │   ├── 사전방문 예약  `cs_visitRsv` → `/page/cs_visitRsv`
│   │   │   └── 입주 지원  `cs_mvinMng` → `/page/cs_mvinMng`
│   │   ├── 인수인계/
│   │   │   ├── 표준인수인계파일등록  `cs_tnhFile` → `/page/cs_tnhFile`
│   │   │   └── 현장인수인계요청  `cs_siteTnhReq` → `/page/cs_siteTnhReq`
│   │   ├── AS관리/
│   │   │   ├── A/S관리  `cs_csChkMng` → `/page/cs_csChkMng`
│   │   │   ├── 작업관리  `cs_wrkCmplMng` → `/page/cs_wrkCmplMng`
│   │   │   └── 하자 문자발송  `cs_csSendSms` → `/page/cs_csSendSms`
│   │   ├── 점검내역대량등록/
│   │   │   └── 점검내역대량등록  `cs_chkDtlsBlkReg` → `/page/cs_chkDtlsBlkReg`
│   │   └── 현황조회/
│   │       ├── 하자처리현황  `cs_disComCurst` → `/page/cs_disComCurst`
│   │       ├── 하자 접수 및 처리대장  `cs_dealAcp` → `/page/cs_dealAcp`
│   │       ├── 공간/부위별 접수처리 현황  `cs_spaceCurst` → `/page/cs_spaceCurst`
│   │       ├── 현장별 AS접수 통계  `cs_bySiteAcpStats` → `/page/cs_bySiteAcpStats`
│   │       ├── 장기 미처리 하자 관리  `cs_ltrmDefect` → `/page/cs_ltrmDefect`
│   │       └── 확인점검 결과 대시보드  `cs_chkDashboard` → `/page/cs_chkDashboard`
│   ├── 하자처리보고/
│   │   └── 하자처리보고서  `cs_dealRep` → `/page/cs_dealRep`
│   └── 발주 내역  `cs_ordDtls` → `/page/cs_ordDtls`
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
| 하자 | 일정관리 › **일정관리** | `cs_schMng` | `http://localhost:8080/page/cs_schMng` |
| 하자 | 기본정보 › **현장정보** | `cs_siteInfo` | `http://localhost:8080/page/cs_siteInfo` |
| 하자 | 기본정보 › **협력업체정보** | `cs_copCoInfo` | `http://localhost:8080/page/cs_copCoInfo` |
| 하자 | 하자 등록 및 처리 › 공통 › **공종코드** | `cs_ctpCd` | `http://localhost:8080/page/cs_ctpCd` |
| 하자 | 하자 등록 및 처리 › 공통 › **현장별 공간 부위 코드** | `cs_sitSpacePartCd` | `http://localhost:8080/page/cs_sitSpacePartCd` |
| 하자 | 하자 등록 및 처리 › 공통 › **담당현장** | `cs_chrSite` | `http://localhost:8080/page/cs_chrSite` |
| 하자 | 하자 등록 및 처리 › 공통 › **기준정보 등록 요청** | `cs_baseCdReq` | `http://localhost:8080/page/cs_baseCdReq` |
| 하자 | 하자 등록 및 처리 › 공통 › **모바일사용자** | `cs_mobileUsr` | `http://localhost:8080/page/cs_mobileUsr` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **점검일정** | `cs_chkSch` | `http://localhost:8080/page/cs_chkSch` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **공종별협력업체** | `cs_bctyCopCo` | `http://localhost:8080/page/cs_bctyCopCo` |
| 하자 | 하자 등록 및 처리 › 기초자료 › **세대정보** | `cs_hhldInfo` | `http://localhost:8080/page/cs_hhldInfo` |
| 하자 | 하자 등록 및 처리 › 준공전품질점검 › **품질점검** | `cs_csChkMng_qlt` | `http://localhost:8080/page/cs_csChkMng_qlt` |
| 하자 | 하자 등록 및 처리 › 준공전품질점검 › **주부품질점검** | `cs_csChkMng_hwf` | `http://localhost:8080/page/cs_csChkMng_hwf` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **사전점검** | `cs_csChkMng_bf` | `http://localhost:8080/page/cs_csChkMng_bf` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **사전방문 예약** | `cs_visitRsv` | `http://localhost:8080/page/cs_visitRsv` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **입주 지원** | `cs_mvinMng` | `http://localhost:8080/page/cs_mvinMng` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **표준인수인계파일등록** | `cs_tnhFile` | `http://localhost:8080/page/cs_tnhFile` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **현장인수인계요청** | `cs_siteTnhReq` | `http://localhost:8080/page/cs_siteTnhReq` |
| 하자 | 하자 등록 및 처리 › AS관리 › **A/S관리** | `cs_csChkMng` | `http://localhost:8080/page/cs_csChkMng` |
| 하자 | 하자 등록 및 처리 › AS관리 › **작업관리** | `cs_wrkCmplMng` | `http://localhost:8080/page/cs_wrkCmplMng` |
| 하자 | 하자 등록 및 처리 › AS관리 › **하자 문자발송** | `cs_csSendSms` | `http://localhost:8080/page/cs_csSendSms` |
| 하자 | 하자 등록 및 처리 › 점검내역대량등록 › **점검내역대량등록** | `cs_chkDtlsBlkReg` | `http://localhost:8080/page/cs_chkDtlsBlkReg` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자처리현황** | `cs_disComCurst` | `http://localhost:8080/page/cs_disComCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자 접수 및 처리대장** | `cs_dealAcp` | `http://localhost:8080/page/cs_dealAcp` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **공간/부위별 접수처리 현황** | `cs_spaceCurst` | `http://localhost:8080/page/cs_spaceCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **현장별 AS접수 통계** | `cs_bySiteAcpStats` | `http://localhost:8080/page/cs_bySiteAcpStats` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **장기 미처리 하자 관리** | `cs_ltrmDefect` | `http://localhost:8080/page/cs_ltrmDefect` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **확인점검 결과 대시보드** | `cs_chkDashboard` | `http://localhost:8080/page/cs_chkDashboard` |
| 하자 | 하자처리보고 › **하자처리보고서** | `cs_dealRep` | `http://localhost:8080/page/cs_dealRep` |
| 하자 | (바로 아래) › **발주 내역** | `cs_ordDtls` | `http://localhost:8080/page/cs_ordDtls` |
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
