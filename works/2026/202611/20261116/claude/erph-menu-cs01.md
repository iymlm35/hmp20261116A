# erp-h 상단메뉴별 left메뉴 트리 — 로그인 계정 「CS본사담당 (CS팀)」

- 조회: 2026-11-16 · `http://localhost:8080/` · 화면 우상단 사용자 표시 「CS본사담당 (CS팀)」 (login id 는 화면에 안 나와 확인하지 못함 · `TEST-ACCOUNTS-20261112.md` 기준 `h.cs01`(CS본사담당) 으로 추정)
- 방법: Aside 브라우저로 상단 탭을 눌러 `.menu-tree` DOM 을 읽고, 메뉴 API `POST /sys/menu/list` 의 `serviceId` 와 화면 이름·순서를 대조했다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>`. 폴더는 `이름/`. 화면 주소는 규칙에 따른 것이며 주소창과 직접 대조하지 않았다.
- 다른 계정 결과: `erph-menu-erpadmin.md` · `erph-menu-dev01.md` · `erph-menu-csadm.md`

| 상단 탭 | 폴더 | 화면 | DOM ↔ API 대조 |
|---|---:|---:|---|
| 하자 | 14 | 59 | DOM 59 · API 59 · 이름·순서 일치 |
| 개인 | 7 | 25 | 상단 탭에 안 나옴 (API 만) |

## 1. 트리

```text
상단메뉴
├── [하자]  key=MENU_CS (폴더 14 · 화면 59)
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
│   │   └── 현황조회/
│   │       ├── 현장종합 투입금액  `cs_siteTotCst` → `/page/cs_siteTotCst`
│   │       ├── 자금집계표  `cs_siteMoneyCst` → `/page/cs_siteMoneyCst`
│   │       ├── 월별 하자보수비 현황  `cs_bymAsExpIptCurst` → `/page/cs_bymAsExpIptCurst`
│   │       ├── 일반관리비 실적  `cs_generlCst` → `/page/cs_generlCst`
│   │       ├── 보증현황  `cs_sponSionCst` → `/page/cs_sponSionCst`
│   │       └── 전표조회  `cs_asSlpCst` → `/page/cs_asSlpCst`
│   ├── 협력업체 공지사항  `cs_jcAnncBrdLst` → `/page/cs_jcAnncBrdLst`
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
| 하자 | (바로 아래) › **하자** | `db_dashBoardCS` | `http://localhost:8080/page/db_dashBoardCS` |
| 하자 | 일정관리 › **일정관리** | `cs_schMng` | `http://localhost:8080/page/cs_schMng` |
| 하자 | 기본정보 › **현장정보** | `cs_siteInfo` | `http://localhost:8080/page/cs_siteInfo` |
| 하자 | 기본정보 › **협력업체정보** | `cs_copCoInfo` | `http://localhost:8080/page/cs_copCoInfo` |
| 하자 | 하자 등록 및 처리 › 공통 › **공종코드** | `cs_ctpCd` | `http://localhost:8080/page/cs_ctpCd` |
| 하자 | 하자 등록 및 처리 › 공통 › **현장별 공간 부위 코드** | `cs_sitSpacePartCd` | `http://localhost:8080/page/cs_sitSpacePartCd` |
| 하자 | 하자 등록 및 처리 › 공통 › **담당현장** | `cs_chrSite` | `http://localhost:8080/page/cs_chrSite` |
| 하자 | 하자 등록 및 처리 › 공통 › **기준정보 등록 요청** | `cs_baseCdReq` | `http://localhost:8080/page/cs_baseCdReq` |
| 하자 | 하자 등록 및 처리 › 공통 › **엔지니어 승인관리** | `cs_mobileUsrAprv` | `http://localhost:8080/page/cs_mobileUsrAprv` |
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
| 하자 | 하자보수비 › 현황조회 › **현장종합 투입금액** | `cs_siteTotCst` | `http://localhost:8080/page/cs_siteTotCst` |
| 하자 | 하자보수비 › 현황조회 › **자금집계표** | `cs_siteMoneyCst` | `http://localhost:8080/page/cs_siteMoneyCst` |
| 하자 | 하자보수비 › 현황조회 › **월별 하자보수비 현황** | `cs_bymAsExpIptCurst` | `http://localhost:8080/page/cs_bymAsExpIptCurst` |
| 하자 | 하자보수비 › 현황조회 › **일반관리비 실적** | `cs_generlCst` | `http://localhost:8080/page/cs_generlCst` |
| 하자 | 하자보수비 › 현황조회 › **보증현황** | `cs_sponSionCst` | `http://localhost:8080/page/cs_sponSionCst` |
| 하자 | 하자보수비 › 현황조회 › **전표조회** | `cs_asSlpCst` | `http://localhost:8080/page/cs_asSlpCst` |
| 하자 | (바로 아래) › **협력업체 공지사항** | `cs_jcAnncBrdLst` | `http://localhost:8080/page/cs_jcAnncBrdLst` |
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
