# erp-h 상단메뉴별 left메뉴 트리 — `h.dev01` 에 role `CS_VIEW_01 + CS_COMMON` 를 함께 부여한 경우 (2026-11-19 조회)

- 조회: 2026-11-19 · `http://localhost:8080/` · 화면 우상단 사용자 표시 「개발자1 (IT팀)」
- 계정·role: `h.dev01` 에 role `CS_VIEW_01 + CS_COMMON` 2개를 설정한 상태의 메뉴다(작성자 확인). 화면에는 login id 가 나오지 않는다.
- 방법: Aside 브라우저로 로그인된 탭의 메뉴 컴포넌트가 화면에 그리는 메뉴 데이터(`moduleList`)의 `label`·`serviceId`·`authLevel` 을 그대로 옮겼다. 같은 시점 left메뉴(`nav.main-menu`)의 최상위 항목(하자 · 일정관리 · 기본정보 · 하자 등록 및 처리 · 하자처리보고 · 하자보수비 · 업무 지도)이 이 데이터와 맞는 것만 확인했고, 하위 폴더를 하나씩 펼쳐 DOM 과 대조하지는 않았다. 메뉴 API(`POST /sys/menu/list`)는 호출하지 않았다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>`, 등급 순서. 폴더는 `이름/`. 화면 주소는 `/page/<serviceId>` 규칙을 따른 것이며 메뉴를 눌러 주소창을 대조하지는 않았다.
- 등급: 1 조회 · 2 저장 · 3 삭제·특수 (`authLevel`).

| 상단 탭 | 폴더 | 화면 | 등급 분포 |
|---|---:|---:|---|
| 하자 | 8 | 29 | 1 28개 · 2 1개 |
| 개인 | 7 | 26 | 1 11개 · 2 13개 · 3 2개 |

- 상단 탭은 「하자」 하나뿐이다. 「공통」·「시스템」 탭은 나오지 않는다. 「개인」은 상단 탭 바에 안 나오고 메뉴 데이터에만 있다.
- left메뉴 아래쪽 고정 버튼 「업무 지도」는 메뉴 트리 데이터에 없다.
- 「하자」 탭 맨 위의 화면 「하자」(`db_dashBoardCS`)는 폴더 없이 탭 바로 아래에 있고, left메뉴에는 폴더들 앞에 「하자」 버튼으로 나온다.
- 하자 화면 29개 중 등급 2(저장)는 1개(일정관리 `cs_schMng`)뿐이고 나머지는 모두 조회(1)다. 기능 허용(`allow*`)은 하자 탭 기준 업로드 6 · 엑셀 가져오기 6 · 다운로드 28 · 인쇄 28 · 엑셀 내보내기 6개 화면이다.

## 1. 트리

```text
상단메뉴
├── [하자]  key=MENU_CS (폴더 8 · 화면 29)
│   ├── 하자  `db_dashBoardCS` → `/page/db_dashBoardCS` · 등급 1
│   ├── 일정관리/
│   │   └── 일정관리  `cs_schMng` → `/page/cs_schMng` · 등급 2
│   ├── 기본정보/
│   │   ├── 현장정보  `cs_siteInfo` → `/page/cs_siteInfo` · 등급 1
│   │   └── 협력업체정보  `cs_copCoInfo` → `/page/cs_copCoInfo` · 등급 1
│   ├── 하자 등록 및 처리/
│   │   ├── AS관리/
│   │   │   └── 분쟁 단지 현황  `cs_dsptMng` → `/page/cs_dsptMng` · 등급 1
│   │   └── 현황조회/
│   │       ├── 하자처리현황  `cs_disComCurst` → `/page/cs_disComCurst` · 등급 1
│   │       ├── 하자 접수 및 처리대장  `cs_dealAcp` → `/page/cs_dealAcp` · 등급 1
│   │       ├── 협력업체별현황  `cs_byCopCoCurst` → `/page/cs_byCopCoCurst` · 등급 1
│   │       ├── 협력업체공종별현황  `cs_byCopCoCtpCurst` → `/page/cs_byCopCoCtpCurst` · 등급 1
│   │       ├── 유형별현황  `cs_byTpCurst` → `/page/cs_byTpCurst` · 등급 1
│   │       ├── 공간/부위별 접수처리 현황  `cs_spaceCurst` → `/page/cs_spaceCurst` · 등급 1
│   │       ├── 현장별 AS접수 통계  `cs_bySiteAcpStats` → `/page/cs_bySiteAcpStats` · 등급 1
│   │       ├── 공종별 처리현황  `cs_ctpDiscomCurst` → `/page/cs_ctpDiscomCurst` · 등급 1
│   │       ├── 업체별 처리현황  `cs_cooDiscomCurst` → `/page/cs_cooDiscomCurst` · 등급 1
│   │       ├── 하자 통합 분석  `cs_defectAnalysis` → `/page/cs_defectAnalysis` · 등급 1
│   │       ├── 협력사 이행 현황  `cs_copCoPerf` → `/page/cs_copCoPerf` · 등급 1
│   │       ├── 중점 관리 단지  `cs_keySite` → `/page/cs_keySite` · 등급 1
│   │       ├── 센터장 일일보고  `cs_ctrDailyRpt` → `/page/cs_ctrDailyRpt` · 등급 1
│   │       ├── 단지별 조직도  `cs_siteOrg` → `/page/cs_siteOrg` · 등급 1
│   │       ├── 재무 연계 하자 분석  `cs_finDefect` → `/page/cs_finDefect` · 등급 1
│   │       ├── 접수 건 추적  `cs_acpTrack` → `/page/cs_acpTrack` · 등급 1
│   │       └── 확인점검 결과 대시보드  `cs_chkDashboard` → `/page/cs_chkDashboard` · 등급 1
│   ├── 하자처리보고/
│   │   └── 하자처리보고서  `cs_dealRep` → `/page/cs_dealRep` · 등급 1
│   └── 하자보수비/
│       └── 현황조회/
│           ├── 현장종합 투입금액  `cs_siteTotCst` → `/page/cs_siteTotCst` · 등급 1
│           ├── 자금집계표  `cs_siteMoneyCst` → `/page/cs_siteMoneyCst` · 등급 1
│           ├── 월별 하자보수비 현황  `cs_bymAsExpIptCurst` → `/page/cs_bymAsExpIptCurst` · 등급 1
│           ├── 일반관리비 실적  `cs_generlCst` → `/page/cs_generlCst` · 등급 1
│           ├── 보증현황  `cs_sponSionCst` → `/page/cs_sponSionCst` · 등급 1
│           └── 전표조회  `cs_asSlpCst` → `/page/cs_asSlpCst` · 등급 1
└── [개인]  key=MENU_COMMON (폴더 7 · 화면 26)
    ├── 마이페이지/
    │   ├── 개인설정  `common-preference` → `/page/common-preference` · 등급 2
    │   ├── 캘린더  `common-calendar` → `/page/common-calendar` · 등급 2
    │   ├── 내 정보  `common-my-page` → `/page/common-my-page` · 등급 1
    │   ├── 사용자 대시보드  `user-dashboard` → `/page/user-dashboard` · 등급 2
    │   ├── 내 프로그램  `my-programs` → `/page/my-programs` · 등급 3
    │   └── 내 예약 보고서  `user-job` → `/page/user-job` · 등급 3
    ├── 결재/
    │   ├── 결재 위임  `common-approval-dlg` → `/page/common-approval-dlg` · 등급 2
    │   ├── 결재선 템플릿  `common-approval-tpl` → `/page/common-approval-tpl` · 등급 2
    │   └── 결재함  `common-approval` → `/page/common-approval` · entryPoint=approval · 등급 1
    ├── 문서/
    │   ├── 내가 만든 문서함  `common-doc-list` → `/page/common-doc-list` · 등급 2
    │   ├── 시트  `common-sheet` → `/page/common-sheet` · 등급 2
    │   ├── 내 문서 이력  `common-doc-version` → `/page/common-doc-version` · 등급 1
    │   ├── 전자 양식  `ca-eform-list` → `/page/ca-eform-list` · 등급 2
    │   └── 원본 확인  `ca-common-doc-verify` → `/page/ca-common-doc-verify` · 등급 1
    ├── 소통/
    │   ├── 공지사항  `common-notice` → `/page/common-notice` · 등급 1
    │   ├── 자료실  `common-library` → `/page/common-library` · 등급 1
    │   ├── 자유게시판  `common-general` → `/page/common-general` · 등급 1
    │   ├── 자주 묻는 질문  `common-faq` → `/page/common-faq` · 등급 1
    │   ├── 주소록  `common-address-book` → `/page/common-address-book` · 등급 1
    │   ├── 조직도  `common-org-chart` → `/page/common-org-chart` · 등급 1
    │   ├── 문의  `common-inquiry` → `/page/common-inquiry` · entryPoint=inquiry · 등급 2
    │   └── 알림 센터  `common-notification` → `/page/common-notification` · entryPoint=notification · 등급 2
    ├── AI/
    │   ├── AI 브리핑  `ai-briefing` → `/page/ai-briefing` · 등급 2
    │   └── AI 어시스턴트  `common-ai-chat` → `/page/common-ai-chat` · entryPoint=aiWindow · 등급 2
    ├── 설문·동의/
    │   └── 동의서  `common-agreement` → `/page/common-agreement` · 등급 1
    └── 내 요청/
        └── 권한·겸직 신청  `ca-role-request` → `/page/ca-role-request` · 등급 2
```

## 2. 화면별 메뉴 경로 표

| 상단 탭 | 메뉴 경로 | serviceId | 등급 | 화면 주소 |
|---|---|---|---:|---|
| 하자 | (바로 아래) › **하자** | `db_dashBoardCS` | 1 | `http://localhost:8080/page/db_dashBoardCS` |
| 하자 | 일정관리 › **일정관리** | `cs_schMng` | 2 | `http://localhost:8080/page/cs_schMng` |
| 하자 | 기본정보 › **현장정보** | `cs_siteInfo` | 1 | `http://localhost:8080/page/cs_siteInfo` |
| 하자 | 기본정보 › **협력업체정보** | `cs_copCoInfo` | 1 | `http://localhost:8080/page/cs_copCoInfo` |
| 하자 | 하자 등록 및 처리 › AS관리 › **분쟁 단지 현황** | `cs_dsptMng` | 1 | `http://localhost:8080/page/cs_dsptMng` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자처리현황** | `cs_disComCurst` | 1 | `http://localhost:8080/page/cs_disComCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자 접수 및 처리대장** | `cs_dealAcp` | 1 | `http://localhost:8080/page/cs_dealAcp` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **협력업체별현황** | `cs_byCopCoCurst` | 1 | `http://localhost:8080/page/cs_byCopCoCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **협력업체공종별현황** | `cs_byCopCoCtpCurst` | 1 | `http://localhost:8080/page/cs_byCopCoCtpCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **유형별현황** | `cs_byTpCurst` | 1 | `http://localhost:8080/page/cs_byTpCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **공간/부위별 접수처리 현황** | `cs_spaceCurst` | 1 | `http://localhost:8080/page/cs_spaceCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **현장별 AS접수 통계** | `cs_bySiteAcpStats` | 1 | `http://localhost:8080/page/cs_bySiteAcpStats` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **공종별 처리현황** | `cs_ctpDiscomCurst` | 1 | `http://localhost:8080/page/cs_ctpDiscomCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **업체별 처리현황** | `cs_cooDiscomCurst` | 1 | `http://localhost:8080/page/cs_cooDiscomCurst` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **하자 통합 분석** | `cs_defectAnalysis` | 1 | `http://localhost:8080/page/cs_defectAnalysis` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **협력사 이행 현황** | `cs_copCoPerf` | 1 | `http://localhost:8080/page/cs_copCoPerf` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **중점 관리 단지** | `cs_keySite` | 1 | `http://localhost:8080/page/cs_keySite` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **센터장 일일보고** | `cs_ctrDailyRpt` | 1 | `http://localhost:8080/page/cs_ctrDailyRpt` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **단지별 조직도** | `cs_siteOrg` | 1 | `http://localhost:8080/page/cs_siteOrg` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **재무 연계 하자 분석** | `cs_finDefect` | 1 | `http://localhost:8080/page/cs_finDefect` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **접수 건 추적** | `cs_acpTrack` | 1 | `http://localhost:8080/page/cs_acpTrack` |
| 하자 | 하자 등록 및 처리 › 현황조회 › **확인점검 결과 대시보드** | `cs_chkDashboard` | 1 | `http://localhost:8080/page/cs_chkDashboard` |
| 하자 | 하자처리보고 › **하자처리보고서** | `cs_dealRep` | 1 | `http://localhost:8080/page/cs_dealRep` |
| 하자 | 하자보수비 › 현황조회 › **현장종합 투입금액** | `cs_siteTotCst` | 1 | `http://localhost:8080/page/cs_siteTotCst` |
| 하자 | 하자보수비 › 현황조회 › **자금집계표** | `cs_siteMoneyCst` | 1 | `http://localhost:8080/page/cs_siteMoneyCst` |
| 하자 | 하자보수비 › 현황조회 › **월별 하자보수비 현황** | `cs_bymAsExpIptCurst` | 1 | `http://localhost:8080/page/cs_bymAsExpIptCurst` |
| 하자 | 하자보수비 › 현황조회 › **일반관리비 실적** | `cs_generlCst` | 1 | `http://localhost:8080/page/cs_generlCst` |
| 하자 | 하자보수비 › 현황조회 › **보증현황** | `cs_sponSionCst` | 1 | `http://localhost:8080/page/cs_sponSionCst` |
| 하자 | 하자보수비 › 현황조회 › **전표조회** | `cs_asSlpCst` | 1 | `http://localhost:8080/page/cs_asSlpCst` |
| 개인 | 마이페이지 › **개인설정** | `common-preference` | 2 | `http://localhost:8080/page/common-preference` |
| 개인 | 마이페이지 › **캘린더** | `common-calendar` | 2 | `http://localhost:8080/page/common-calendar` |
| 개인 | 마이페이지 › **내 정보** | `common-my-page` | 1 | `http://localhost:8080/page/common-my-page` |
| 개인 | 마이페이지 › **사용자 대시보드** | `user-dashboard` | 2 | `http://localhost:8080/page/user-dashboard` |
| 개인 | 마이페이지 › **내 프로그램** | `my-programs` | 3 | `http://localhost:8080/page/my-programs` |
| 개인 | 마이페이지 › **내 예약 보고서** | `user-job` | 3 | `http://localhost:8080/page/user-job` |
| 개인 | 결재 › **결재 위임** | `common-approval-dlg` | 2 | `http://localhost:8080/page/common-approval-dlg` |
| 개인 | 결재 › **결재선 템플릿** | `common-approval-tpl` | 2 | `http://localhost:8080/page/common-approval-tpl` |
| 개인 | 결재 › **결재함** | `common-approval` | 1 | `http://localhost:8080/page/common-approval` |
| 개인 | 문서 › **내가 만든 문서함** | `common-doc-list` | 2 | `http://localhost:8080/page/common-doc-list` |
| 개인 | 문서 › **시트** | `common-sheet` | 2 | `http://localhost:8080/page/common-sheet` |
| 개인 | 문서 › **내 문서 이력** | `common-doc-version` | 1 | `http://localhost:8080/page/common-doc-version` |
| 개인 | 문서 › **전자 양식** | `ca-eform-list` | 2 | `http://localhost:8080/page/ca-eform-list` |
| 개인 | 문서 › **원본 확인** | `ca-common-doc-verify` | 1 | `http://localhost:8080/page/ca-common-doc-verify` |
| 개인 | 소통 › **공지사항** | `common-notice` | 1 | `http://localhost:8080/page/common-notice` |
| 개인 | 소통 › **자료실** | `common-library` | 1 | `http://localhost:8080/page/common-library` |
| 개인 | 소통 › **자유게시판** | `common-general` | 1 | `http://localhost:8080/page/common-general` |
| 개인 | 소통 › **자주 묻는 질문** | `common-faq` | 1 | `http://localhost:8080/page/common-faq` |
| 개인 | 소통 › **주소록** | `common-address-book` | 1 | `http://localhost:8080/page/common-address-book` |
| 개인 | 소통 › **조직도** | `common-org-chart` | 1 | `http://localhost:8080/page/common-org-chart` |
| 개인 | 소통 › **문의** | `common-inquiry` | 2 | `http://localhost:8080/page/common-inquiry` |
| 개인 | 소통 › **알림 센터** | `common-notification` | 2 | `http://localhost:8080/page/common-notification` |
| 개인 | AI › **AI 브리핑** | `ai-briefing` | 2 | `http://localhost:8080/page/ai-briefing` |
| 개인 | AI › **AI 어시스턴트** | `common-ai-chat` | 2 | `http://localhost:8080/page/common-ai-chat` |
| 개인 | 설문·동의 › **동의서** | `common-agreement` | 1 | `http://localhost:8080/page/common-agreement` |
| 개인 | 내 요청 › **권한·겸직 신청** | `ca-role-request` | 2 | `http://localhost:8080/page/ca-role-request` |

## 3. 다른 조회와 비교

| 비교 대상 | 하자 화면 | 개인 화면 | 이번 조회와의 차이 |
|---|---:|---:|---|
| `CS_SITE_01 + CS_COMMON` (2026-11-18) | 29 | 26 | 이번에 없음 21 · 새로 보임 21 · 등급 변경 0 |
| `CS_COMMON 만` | 0 | 26 | 이번에 없음 0 · 새로 보임 29 · 등급 변경 0 |
| 이번 (`CS_VIEW_01 + CS_COMMON`) | 29 | 26 | - |

- `CS_SITE_01 + CS_COMMON` 대비: 이번에 없는 화면 21개, 새로 보이는 화면 21개, 등급이 바뀐 화면 0개.
  - 이번에 없는 화면: 공종코드(`cs_ctpCd`), 현장별 공간 부위 코드(`cs_sitSpacePartCd`), 담당현장(`cs_chrSite`), 기준정보 등록 요청(`cs_baseCdReq`), 모바일사용자(`cs_mobileUsr`), 점검일정(`cs_chkSch`), 공종별협력업체(`cs_bctyCopCo`), 세대정보(`cs_hhldInfo`), 품질점검(`cs_csChkMng_qlt`), 주부품질점검(`cs_csChkMng_hwf`), 사전점검(`cs_csChkMng_bf`), 사전방문 예약(`cs_visitRsv`), 입주 지원(`cs_mvinMng`), 표준인수인계파일등록(`cs_tnhFile`), 현장인수인계요청(`cs_siteTnhReq`), A/S관리(`cs_csChkMng`), 작업관리(`cs_wrkCmplMng`), 하자 문자발송(`cs_csSendSms`), 점검내역대량등록(`cs_chkDtlsBlkReg`), 장기 미처리 하자 관리(`cs_ltrmDefect`), 발주 내역(`cs_ordDtls`)
  - 새로 보이는 화면: 하자(`db_dashBoardCS`), 협력업체정보(`cs_copCoInfo`), 분쟁 단지 현황(`cs_dsptMng`), 협력업체별현황(`cs_byCopCoCurst`), 협력업체공종별현황(`cs_byCopCoCtpCurst`), 유형별현황(`cs_byTpCurst`), 공종별 처리현황(`cs_ctpDiscomCurst`), 업체별 처리현황(`cs_cooDiscomCurst`), 하자 통합 분석(`cs_defectAnalysis`), 협력사 이행 현황(`cs_copCoPerf`), 중점 관리 단지(`cs_keySite`), 센터장 일일보고(`cs_ctrDailyRpt`), 단지별 조직도(`cs_siteOrg`), 재무 연계 하자 분석(`cs_finDefect`), 접수 건 추적(`cs_acpTrack`), 현장종합 투입금액(`cs_siteTotCst`), 자금집계표(`cs_siteMoneyCst`), 월별 하자보수비 현황(`cs_bymAsExpIptCurst`), 일반관리비 실적(`cs_generlCst`), 보증현황(`cs_sponSionCst`), 전표조회(`cs_asSlpCst`)
- `CS_COMMON 만` 대비: 이번에 없는 화면 0개, 새로 보이는 화면 29개, 등급이 바뀐 화면 0개.
  - 새로 보이는 화면: 하자(`db_dashBoardCS`), 일정관리(`cs_schMng`), 현장정보(`cs_siteInfo`), 협력업체정보(`cs_copCoInfo`), 분쟁 단지 현황(`cs_dsptMng`), 하자처리현황(`cs_disComCurst`), 하자 접수 및 처리대장(`cs_dealAcp`), 협력업체별현황(`cs_byCopCoCurst`), 협력업체공종별현황(`cs_byCopCoCtpCurst`), 유형별현황(`cs_byTpCurst`), 공간/부위별 접수처리 현황(`cs_spaceCurst`), 현장별 AS접수 통계(`cs_bySiteAcpStats`), 공종별 처리현황(`cs_ctpDiscomCurst`), 업체별 처리현황(`cs_cooDiscomCurst`), 하자 통합 분석(`cs_defectAnalysis`), 협력사 이행 현황(`cs_copCoPerf`), 중점 관리 단지(`cs_keySite`), 센터장 일일보고(`cs_ctrDailyRpt`), 단지별 조직도(`cs_siteOrg`), 재무 연계 하자 분석(`cs_finDefect`), 접수 건 추적(`cs_acpTrack`), 확인점검 결과 대시보드(`cs_chkDashboard`), 하자처리보고서(`cs_dealRep`), 현장종합 투입금액(`cs_siteTotCst`), 자금집계표(`cs_siteMoneyCst`), 월별 하자보수비 현황(`cs_bymAsExpIptCurst`), 일반관리비 실적(`cs_generlCst`), 보증현황(`cs_sponSionCst`), 전표조회(`cs_asSlpCst`)
- 각 조회 사이에 다른 설정이 바뀌었는지는 확인하지 않았다. 비교 대상은 같은 폴더의 `erph-menu-dev01[CS_SITE_01+CS_COMMON].md` · `erph-menu-dev01[CS_COMMON].md` 에 적힌 값이다(이번에 다시 읽지 않았다).

## 4. 한계

- 화면 이름·순서·serviceId·등급은 화면이 쓰는 메뉴 데이터 그대로다. 메뉴 API 응답과 직접 대조하지는 않았다.
- 메뉴 노출만 본 결과다. 화면 안 버튼, 데이터 범위(법인·현장)는 이 문서에 없다.
- 화면 우상단의 「부서 / 권한 전환」은 열지 않았고, 현재 선택된 부서·권한 기준으로 읽었다.
- API 접근(예: 현장코드 파인더 `/csz/getGrd_CsCodeFind` 호출)은 이번에 시험하지 않았다.
