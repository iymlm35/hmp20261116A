# erp-h 상단메뉴별 left메뉴 트리 (프로그램 경로 포함)

- 조회: 2026-11-20 · `http://localhost:8080/` · 로그인 계정 `erpadmin` · Aside 브라우저로 상단 탭(공통·시스템)을 눌러 사이드바 `.menu-tree` DOM을 읽고, 같은 세션의 메뉴 API `POST /sys/menu/list` 응답과 대조했다(DOM 항목 수 = API 노드 수 일치). 하자·개인·공사는 API 응답 기준이다.
- 이전 조회(2026-11-16)는 `erph-menu-erpadmin_old01.md`에 그대로 두었다. 이 문서는 현재(11/20) 결과이고, 두 문서의 차이는 「3. 이전 문서(11/16)와의 차이」에 정리했다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>` 순서. 폴더는 `이름/`.
- 화면 주소는 `S2-screen-inventory.md` 가 적은 규칙 `/page/<서비스ID>` 를 따랐다. 메뉴를 눌러 주소창을 직접 대조하지는 않았다.
- 화면 소스 파일 경로(`pages/...js`)는 메뉴 API 에 없어 넣지 않았다. `module` 값은 표기가 없으면 `app`, 다르면 줄 끝에 적었다.
- 계정 권한에 따라 보이는 메뉴가 다르다. 아래는 `erpadmin` 기준. 이 조회의 로그인 세션 권한이 11/16 조회 때와 같은지는 확인하지 못했다.
- 이전 문서에 없던 **공사**(`CM`) 탭이 이번 응답에 있어 함께 적었다.

| 상단 탭 | 폴더 | 화면 |
|---|---:|---:|
| 공사 | 26 | 80 |
| 공통 | 8 | 39 |
| 하자 | 14 | 57 |
| 시스템 | 37 | 133 |
| 개인 (상단 탭에 안 나옴) | 7 | 26 |

## 1. 트리

```text
상단메뉴
├── [공사]  key=CM (폴더 26 · 화면 80)
│   ├── 업무현황/
│   │   ├── ERP시스템승인  `CMA010` → `/page/CMA010`
│   │   ├── 진행대상업무현황  `CMA020` → `/page/CMA020` · 화면 파일 없음(pageMissing)
│   │   ├── 현장업무수행현황  `CMA030` → `/page/CMA030`
│   │   └── 필수첨부문서관리  `CMA040` → `/page/CMA040`
│   ├── 현장개요/
│   │   ├── 공사규모  `CMA20` → `/page/CMA20`
│   │   ├── 현장관리  `CMA10` → `/page/CMA10`
│   │   ├── 공사비분석  `CMA30` → `/page/CMA30`
│   │   └── 시설물관리  `CMA40` → `/page/CMA40`
│   ├── 공사실행/
│   │   ├── 실행예산/
│   │   │   ├── 실행예산편성  `CMB1010` → `/page/CMB1010`
│   │   │   ├── 실행내역  `CMB1020` → `/page/CMB1020`
│   │   │   └── 실행예산승인  `CMB1030` → `/page/CMB1030`
│   │   ├── 시행예산/
│   │   │   ├── 시행예산편성  `CMB2010` → `/page/CMB2010`
│   │   │   ├── 시행예산승인  `CMB2020` → `/page/CMB2020`
│   │   │   ├── 시행예산반영현황  `CMB2030` → `/page/CMB2030`
│   │   │   └── 시행예산모니터링  `CMB2040` → `/page/CMB2040`
│   │   ├── 최종공사실행  `CMB30` → `/page/CMB30`
│   │   ├── 실행내역현황  `CMB25` → `/page/CMB25`
│   │   ├── 실행예외현황  `CMB40` → `/page/CMB40`
│   │   ├── 예산단계별대비  `CMB60` → `/page/CMB60`
│   │   ├── 예산외발주현황  `CMB50` → `/page/CMB50`
│   │   └── 내역변경현황  `CMB99` → `/page/CMB99`
│   ├── 공사원가/
│   │   ├── 이체원가조정  `CMD05` → `/page/CMD05`
│   │   ├── 공사원가현황  `CMD10` → `/page/CMD10`
│   │   ├── 공사실행대비  `CMD20` → `/page/CMD20`
│   │   ├── 회계원가등록  `CMD25` → `/page/CMD25`
│   │   ├── 회계원가대비  `CMD30` → `/page/CMD30`
│   │   └── 공정·원가집행률대비  `CMD40` → `/page/CMD40`
│   ├── 공사마감/
│   │   ├── 공사월마감  `CMH10` → `/page/CMH10`
│   │   ├── 월마감현황  `CMH20` → `/page/CMH20`
│   │   └── 월마감모니터링  `CMH30` → `/page/CMH30`
│   ├── 실행기성/
│   │   ├── 실행기성내역  `CME10` → `/page/CME10`
│   │   └── 실행기성현황  `CME20` → `/page/CME20`
│   ├── 준공예정/
│   │   ├── 준공예정내역  `CMF10` → `/page/CMF10`
│   │   └── 준공예정현황  `CMF20` → `/page/CMF20`
│   ├── 도급기성·원가/
│   │   ├── 계획관리/
│   │   │   ├── 도급기성·원가계획등록  `CMG1010` → `/page/CMG1010`
│   │   │   └── 도급기성·원가계획승인  `CMG1020` → `/page/CMG1020`
│   │   ├── 손익추정/
│   │   │   ├── 표준분포도  `CMG2010` → `/page/CMG2010`
│   │   │   ├── 표준분포도등록(팝업)  `CMG2020` → `/page/CMG2020`
│   │   │   ├── 추정원가배분  `CMG2030` → `/page/CMG2030`
│   │   │   └── 도급기성대비  `CMG2040` → `/page/CMG2040`
│   │   └── 실적관리/
│   │       ├── 도급기성·원가실적등록  `CMG3010` → `/page/CMG3010`
│   │       └── 도급기성·원가실적승인  `CMG3020` → `/page/CMG3020`
│   ├── 마일스톤/
│   │   ├── 표준마일스톤  `CMH210` → `/page/CMH210`
│   │   ├── 계획관리/
│   │   │   ├── 계획마일스톤등록  `CMH22010` → `/page/CMH22010`
│   │   │   └── 계획마일스톤승인  `CMH22020` → `/page/CMH22020`
│   │   ├── 실적관리/
│   │   │   ├── 실적마일스톤등록  `CMH23010` → `/page/CMH23010`
│   │   │   └── 실적마일스톤승인  `CMH23020` → `/page/CMH23020`
│   │   ├── 공정률달성현황  `CMH250` → `/page/CMH250`
│   │   ├── 공정계획대비실적  `CMH240` → `/page/CMH240`
│   │   ├── 월별공정진행현황  `CMH270` → `/page/CMH270`
│   │   ├── 연도별공정진행현황  `CMH260` → `/page/CMH260`
│   │   └── 현장경비집행현황  `CMH280` → `/page/CMH280`
│   ├── 현황보고/
│   │   ├── 공사현황보고  `CMI10` → `/page/CMI10`
│   │   ├── 자금청구(현장)  `CMI40` → `/page/CMI40`
│   │   └── 자금청구(본사)  `CMI50` → `/page/CMI50`
│   ├── 공사손익/
│   │   ├── 경영실적현황  `CMJ10` → `/page/CMJ10`
│   │   ├── 손익추정현황  `CMJ20` → `/page/CMJ20`
│   │   ├── 청구수금현황  `CMJ30` → `/page/CMJ30`
│   │   ├── 현장실적비교  `CMJ40` → `/page/CMJ40`
│   │   ├── 공사손익현황  `CMJ50` → `/page/CMJ50`
│   │   ├── 손익현황상세Ⅰ  `CMJ60` → `/page/CMJ60`
│   │   └── 손익현황상세Ⅱ  `CMJ70` → `/page/CMJ70`
│   ├── 공사일지/
│   │   ├── 기초코드등록  `CMK10` → `/page/CMK10`
│   │   ├── 공사일지등록  `CMK20` → `/page/CMK20`
│   │   └── 공사일지승인  `CMK30` → `/page/CMK30`
│   ├── 현장점검/
│   │   └── 현장점검일정  `CMM10` → `/page/CMM10` · 화면 파일 없음(pageMissing)
│   ├── 완료보고/
│   │   └── 공사완료보고서  `CMN20` → `/page/CMN20`
│   ├── 준공평가/
│   │   ├── 평가기준관리  `CMO10` → `/page/CMO10` · 화면 파일 없음(pageMissing)
│   │   ├── 준공현장평가  `CMO20` → `/page/CMO20` · 화면 파일 없음(pageMissing)
│   │   └── 준공평가현황  `CMO30` → `/page/CMO30`
│   ├── 표준공종/
│   │   ├── 표준공종관리  `CMZ10` → `/page/CMZ10`
│   │   ├── 공사비대비표  `CMZ20` → `/page/CMZ20`
│   │   ├── 표준공종별투입현황  `CMZ30` → `/page/CMZ30`
│   │   ├── 표준내역별실적단가  `CMZ40` → `/page/CMZ40`
│   │   ├── 실행내역별실적단가Ⅰ  `CMZ50` → `/page/CMZ50`
│   │   └── 실행내역별실적단가Ⅱ  `CMZ60` → `/page/CMZ60`
│   ├── Data이관/
│   │   ├── DataConversion  `CMZ110` → `/page/CMZ110`
│   │   └── 공사원가등록  `CMZ120` → `/page/CMZ120`
│   ├── 공사공통/
│   │   └── 슬라이드사진미리보기(팝업)  `CMZ900010` → `/page/CMZ900010`
│   └── 공사관리/
│       └── 회계원가등록  `migcst` → `/page/migcst` · module=sys
├── [공통]  key=MENU_BIZ_COMMON (폴더 8 · 화면 39)
│   ├── 기초정보관리/
│   │   ├── 공통코드  `ca-code` → `/page/ca-code`
│   │   ├── 사용자정보  `ca-employee` → `/page/ca-employee`
│   │   ├── 부서/현장정보  `co_deptMng` → `/page/co_deptMng`
│   │   ├── 거래처관리  `ca-partner` → `/page/ca-partner`
│   │   ├── 휴일관리  `ca-holiday` → `/page/ca-holiday`
│   │   └── 코드관리/
│   │       ├── 국가코드관리  `ca-country` → `/page/ca-country`
│   │       ├── 통화코드관리  `ca-currency` → `/page/ca-currency`
│   │       ├── 환율정보관리  `ca-exchange-rate` → `/page/ca-exchange-rate`
│   │       └── 은행정보관리  `ca-bank` → `/page/ca-bank`
│   ├── 권한관리/
│   │   ├── 권한그룹  `ca-role` → `/page/ca-role`
│   │   ├── 역할그룹  `ca-role-assign` → `/page/ca-role-assign`
│   │   ├── 메뉴권한  `ca-role-service` → `/page/ca-role-service`
│   │   ├── 직무연동  `ca-role-rule` → `/page/ca-role-rule`
│   │   ├── 사용자겸직정보  `ca-org-assignment` → `/page/ca-org-assignment`
│   │   ├── 부서별겸직정보  `co_deptCriteriaCrent` → `/page/co_deptCriteriaCrent`
│   │   ├── 대시보드  `ca-home-service-rule` → `/page/ca-home-service-rule`
│   │   ├── 사용자  `ca-user-role` → `/page/ca-user-role`
│   │   └── 사용자(모바일)  `co_mobileAuthMapp` → `/page/co_mobileAuthMapp`
│   ├── 권한이력/
│   │   ├── (권한현황) 권한그룹-메뉴  `ca-user-role-inquiry` → `/page/ca-user-role-inquiry`
│   │   ├── (권한현황) 권한그룹-사용자  `COD10` → `/page/COD10`
│   │   ├── (권한현황) 사용자-권한그룹  `COD20` → `/page/COD20`
│   │   ├── (변경이력) 사용자  `ca-log-role-grant` → `/page/ca-log-role-grant`
│   │   ├── (변경이력) 권한그룹  `ca-log-role-service` → `/page/ca-log-role-service`
│   │   └── (접속이력) 개인정보  `ca-log-privacy` → `/page/ca-log-privacy`
│   ├── 커뮤니티/
│   │   └── 공지사항  `co_noticeList` → `/page/co_noticeList`
│   ├── 공통기능/
│   │   ├── 시스템승인-Sample  `COG01` → `/page/COG01` · 화면 파일 없음(pageMissing) · module=sys
│   │   ├── 시스템승인-Dialog  `COG02` → `/page/COG02` · 화면 파일 없음(pageMissing)
│   │   ├── 전자메일·SMS-Template  `ca-msg-template` → `/page/ca-msg-template`
│   │   ├── 문서양식관리  `co_elcDoc` → `/page/co_elcDoc`
│   │   ├── 전자서명검증  `COG20` → `/page/COG20`
│   │   └── 인증서유효기간  `COG30` → `/page/COG30`
│   └── 시스템관리/
│       ├── 도움말  `co_mnuHelpMng` → `/page/co_mnuHelpMng`
│       ├── 메뉴정보  `ca-service` → `/page/ca-service`
│       ├── 에러로그  `ca-log-error` → `/page/ca-log-error`
│       ├── 결재로그  `co_aprvLog` → `/page/co_aprvLog`
│       ├── 시스템접속로그  `ca-log-login` → `/page/ca-log-login`
│       ├── 시스템접근통제  `ca-allowed-ip` → `/page/ca-allowed-ip`
│       ├── 세션모니터링  `ca-user-session` → `/page/ca-user-session`
│       └── 체크리스트/
│           └── DB객체관리  `co_dbObject` → `/page/co_dbObject`
├── [하자]  key=MENU_CS (폴더 14 · 화면 57)
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
│   │   ├── 입주자사전점검/
│   │   │   ├── 사전방문 예약  `cs_visitRsv` → `/page/cs_visitRsv`
│   │   │   └── 입주 지원  `cs_mvinMng` → `/page/cs_mvinMng`
│   │   ├── 인수인계/
│   │   │   ├── 표준인수인계파일등록  `cs_tnhFile` → `/page/cs_tnhFile`
│   │   │   ├── 현장인수인계요청  `cs_siteTnhReq` → `/page/cs_siteTnhReq`
│   │   │   └── 현장인수인계승인  `cs_siteTnhAprv` → `/page/cs_siteTnhAprv`
│   │   ├── AS관리/
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
├── [시스템]  key=MENU_COMMON_ADMIN (폴더 37 · 화면 133)
│   ├── 권한그룹/
│   │   └── 권한 부여/
│   │       └── 권한위임 관리  `sys-ca-role-delegation` → `/page/sys-ca-role-delegation`
│   ├── 기준정보/
│   │   ├── 채번 규칙  `sys-ca-numbering-rule` → `/page/sys-ca-numbering-rule`
│   │   ├── 코드 등록 요청 관리  `sys-ca-code-request` → `/page/sys-ca-code-request`
│   │   └── 결정표  `sys-ca-decision-table` → `/page/sys-ca-decision-table`
│   ├── 결재·문서/
│   │   ├── 결재/
│   │   │   └── 결재 양식 관리  `sys-ca-approval-form-template` → `/page/sys-ca-approval-form-template`
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
│   │   │   └── 페이지정책 관리  `ca-page-policy` → `/page/ca-page-policy`
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
│   │   │   └── 외부 사용자 초대·승인·만료  `ca-external-access` → `/page/ca-external-access`
│   │   ├── 접근 통제/
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
│   │       └── 암호화 도구  `ca-crypto-tool` → `/page/ca-crypto-tool`
│   ├── 운영·로그/
│   │   ├── 로그/
│   │   │   ├── 페이지 로그  `ca-log-page` → `/page/ca-log-page`
│   │   │   ├── API 로그  `ca-log-service` → `/page/ca-log-service`
│   │   │   ├── 파일다운로드 이력  `ca-log-file-download` → `/page/ca-log-file-download`
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
│       │   ├── 코드 등록 요청 관리  `code-request` → `/page/code-request`
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
└── [개인]  key=MENU_COMMON (폴더 7 · 화면 26)
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
    │   ├── 시트  `common-sheet` → `/page/common-sheet`
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
| 공사 | 업무현황 › **ERP시스템승인** | `CMA010` | `http://localhost:8080/page/CMA010` |
| 공사 | 업무현황 › **진행대상업무현황** | `CMA020` | `http://localhost:8080/page/CMA020` |
| 공사 | 업무현황 › **현장업무수행현황** | `CMA030` | `http://localhost:8080/page/CMA030` |
| 공사 | 업무현황 › **필수첨부문서관리** | `CMA040` | `http://localhost:8080/page/CMA040` |
| 공사 | 현장개요 › **공사규모** | `CMA20` | `http://localhost:8080/page/CMA20` |
| 공사 | 현장개요 › **현장관리** | `CMA10` | `http://localhost:8080/page/CMA10` |
| 공사 | 현장개요 › **공사비분석** | `CMA30` | `http://localhost:8080/page/CMA30` |
| 공사 | 현장개요 › **시설물관리** | `CMA40` | `http://localhost:8080/page/CMA40` |
| 공사 | 공사실행 › 실행예산 › **실행예산편성** | `CMB1010` | `http://localhost:8080/page/CMB1010` |
| 공사 | 공사실행 › 실행예산 › **실행내역** | `CMB1020` | `http://localhost:8080/page/CMB1020` |
| 공사 | 공사실행 › 실행예산 › **실행예산승인** | `CMB1030` | `http://localhost:8080/page/CMB1030` |
| 공사 | 공사실행 › 시행예산 › **시행예산편성** | `CMB2010` | `http://localhost:8080/page/CMB2010` |
| 공사 | 공사실행 › 시행예산 › **시행예산승인** | `CMB2020` | `http://localhost:8080/page/CMB2020` |
| 공사 | 공사실행 › 시행예산 › **시행예산반영현황** | `CMB2030` | `http://localhost:8080/page/CMB2030` |
| 공사 | 공사실행 › 시행예산 › **시행예산모니터링** | `CMB2040` | `http://localhost:8080/page/CMB2040` |
| 공사 | 공사실행 › **최종공사실행** | `CMB30` | `http://localhost:8080/page/CMB30` |
| 공사 | 공사실행 › **실행내역현황** | `CMB25` | `http://localhost:8080/page/CMB25` |
| 공사 | 공사실행 › **실행예외현황** | `CMB40` | `http://localhost:8080/page/CMB40` |
| 공사 | 공사실행 › **예산단계별대비** | `CMB60` | `http://localhost:8080/page/CMB60` |
| 공사 | 공사실행 › **예산외발주현황** | `CMB50` | `http://localhost:8080/page/CMB50` |
| 공사 | 공사실행 › **내역변경현황** | `CMB99` | `http://localhost:8080/page/CMB99` |
| 공사 | 공사원가 › **이체원가조정** | `CMD05` | `http://localhost:8080/page/CMD05` |
| 공사 | 공사원가 › **공사원가현황** | `CMD10` | `http://localhost:8080/page/CMD10` |
| 공사 | 공사원가 › **공사실행대비** | `CMD20` | `http://localhost:8080/page/CMD20` |
| 공사 | 공사원가 › **회계원가등록** | `CMD25` | `http://localhost:8080/page/CMD25` |
| 공사 | 공사원가 › **회계원가대비** | `CMD30` | `http://localhost:8080/page/CMD30` |
| 공사 | 공사원가 › **공정·원가집행률대비** | `CMD40` | `http://localhost:8080/page/CMD40` |
| 공사 | 공사마감 › **공사월마감** | `CMH10` | `http://localhost:8080/page/CMH10` |
| 공사 | 공사마감 › **월마감현황** | `CMH20` | `http://localhost:8080/page/CMH20` |
| 공사 | 공사마감 › **월마감모니터링** | `CMH30` | `http://localhost:8080/page/CMH30` |
| 공사 | 실행기성 › **실행기성내역** | `CME10` | `http://localhost:8080/page/CME10` |
| 공사 | 실행기성 › **실행기성현황** | `CME20` | `http://localhost:8080/page/CME20` |
| 공사 | 준공예정 › **준공예정내역** | `CMF10` | `http://localhost:8080/page/CMF10` |
| 공사 | 준공예정 › **준공예정현황** | `CMF20` | `http://localhost:8080/page/CMF20` |
| 공사 | 도급기성·원가 › 계획관리 › **도급기성·원가계획등록** | `CMG1010` | `http://localhost:8080/page/CMG1010` |
| 공사 | 도급기성·원가 › 계획관리 › **도급기성·원가계획승인** | `CMG1020` | `http://localhost:8080/page/CMG1020` |
| 공사 | 도급기성·원가 › 손익추정 › **표준분포도** | `CMG2010` | `http://localhost:8080/page/CMG2010` |
| 공사 | 도급기성·원가 › 손익추정 › **표준분포도등록(팝업)** | `CMG2020` | `http://localhost:8080/page/CMG2020` |
| 공사 | 도급기성·원가 › 손익추정 › **추정원가배분** | `CMG2030` | `http://localhost:8080/page/CMG2030` |
| 공사 | 도급기성·원가 › 손익추정 › **도급기성대비** | `CMG2040` | `http://localhost:8080/page/CMG2040` |
| 공사 | 도급기성·원가 › 실적관리 › **도급기성·원가실적등록** | `CMG3010` | `http://localhost:8080/page/CMG3010` |
| 공사 | 도급기성·원가 › 실적관리 › **도급기성·원가실적승인** | `CMG3020` | `http://localhost:8080/page/CMG3020` |
| 공사 | 마일스톤 › **표준마일스톤** | `CMH210` | `http://localhost:8080/page/CMH210` |
| 공사 | 마일스톤 › 계획관리 › **계획마일스톤등록** | `CMH22010` | `http://localhost:8080/page/CMH22010` |
| 공사 | 마일스톤 › 계획관리 › **계획마일스톤승인** | `CMH22020` | `http://localhost:8080/page/CMH22020` |
| 공사 | 마일스톤 › 실적관리 › **실적마일스톤등록** | `CMH23010` | `http://localhost:8080/page/CMH23010` |
| 공사 | 마일스톤 › 실적관리 › **실적마일스톤승인** | `CMH23020` | `http://localhost:8080/page/CMH23020` |
| 공사 | 마일스톤 › **공정률달성현황** | `CMH250` | `http://localhost:8080/page/CMH250` |
| 공사 | 마일스톤 › **공정계획대비실적** | `CMH240` | `http://localhost:8080/page/CMH240` |
| 공사 | 마일스톤 › **월별공정진행현황** | `CMH270` | `http://localhost:8080/page/CMH270` |
| 공사 | 마일스톤 › **연도별공정진행현황** | `CMH260` | `http://localhost:8080/page/CMH260` |
| 공사 | 마일스톤 › **현장경비집행현황** | `CMH280` | `http://localhost:8080/page/CMH280` |
| 공사 | 현황보고 › **공사현황보고** | `CMI10` | `http://localhost:8080/page/CMI10` |
| 공사 | 현황보고 › **자금청구(현장)** | `CMI40` | `http://localhost:8080/page/CMI40` |
| 공사 | 현황보고 › **자금청구(본사)** | `CMI50` | `http://localhost:8080/page/CMI50` |
| 공사 | 공사손익 › **경영실적현황** | `CMJ10` | `http://localhost:8080/page/CMJ10` |
| 공사 | 공사손익 › **손익추정현황** | `CMJ20` | `http://localhost:8080/page/CMJ20` |
| 공사 | 공사손익 › **청구수금현황** | `CMJ30` | `http://localhost:8080/page/CMJ30` |
| 공사 | 공사손익 › **현장실적비교** | `CMJ40` | `http://localhost:8080/page/CMJ40` |
| 공사 | 공사손익 › **공사손익현황** | `CMJ50` | `http://localhost:8080/page/CMJ50` |
| 공사 | 공사손익 › **손익현황상세Ⅰ** | `CMJ60` | `http://localhost:8080/page/CMJ60` |
| 공사 | 공사손익 › **손익현황상세Ⅱ** | `CMJ70` | `http://localhost:8080/page/CMJ70` |
| 공사 | 공사일지 › **기초코드등록** | `CMK10` | `http://localhost:8080/page/CMK10` |
| 공사 | 공사일지 › **공사일지등록** | `CMK20` | `http://localhost:8080/page/CMK20` |
| 공사 | 공사일지 › **공사일지승인** | `CMK30` | `http://localhost:8080/page/CMK30` |
| 공사 | 현장점검 › **현장점검일정** | `CMM10` | `http://localhost:8080/page/CMM10` |
| 공사 | 완료보고 › **공사완료보고서** | `CMN20` | `http://localhost:8080/page/CMN20` |
| 공사 | 준공평가 › **평가기준관리** | `CMO10` | `http://localhost:8080/page/CMO10` |
| 공사 | 준공평가 › **준공현장평가** | `CMO20` | `http://localhost:8080/page/CMO20` |
| 공사 | 준공평가 › **준공평가현황** | `CMO30` | `http://localhost:8080/page/CMO30` |
| 공사 | 표준공종 › **표준공종관리** | `CMZ10` | `http://localhost:8080/page/CMZ10` |
| 공사 | 표준공종 › **공사비대비표** | `CMZ20` | `http://localhost:8080/page/CMZ20` |
| 공사 | 표준공종 › **표준공종별투입현황** | `CMZ30` | `http://localhost:8080/page/CMZ30` |
| 공사 | 표준공종 › **표준내역별실적단가** | `CMZ40` | `http://localhost:8080/page/CMZ40` |
| 공사 | 표준공종 › **실행내역별실적단가Ⅰ** | `CMZ50` | `http://localhost:8080/page/CMZ50` |
| 공사 | 표준공종 › **실행내역별실적단가Ⅱ** | `CMZ60` | `http://localhost:8080/page/CMZ60` |
| 공사 | Data이관 › **DataConversion** | `CMZ110` | `http://localhost:8080/page/CMZ110` |
| 공사 | Data이관 › **공사원가등록** | `CMZ120` | `http://localhost:8080/page/CMZ120` |
| 공사 | 공사공통 › **슬라이드사진미리보기(팝업)** | `CMZ900010` | `http://localhost:8080/page/CMZ900010` |
| 공사 | 공사관리 › **회계원가등록** | `migcst` | `http://localhost:8080/page/migcst` |
| 공통 | 기초정보관리 › **공통코드** | `ca-code` | `http://localhost:8080/page/ca-code` |
| 공통 | 기초정보관리 › **사용자정보** | `ca-employee` | `http://localhost:8080/page/ca-employee` |
| 공통 | 기초정보관리 › **부서/현장정보** | `co_deptMng` | `http://localhost:8080/page/co_deptMng` |
| 공통 | 기초정보관리 › **거래처관리** | `ca-partner` | `http://localhost:8080/page/ca-partner` |
| 공통 | 기초정보관리 › **휴일관리** | `ca-holiday` | `http://localhost:8080/page/ca-holiday` |
| 공통 | 기초정보관리 › 코드관리 › **국가코드관리** | `ca-country` | `http://localhost:8080/page/ca-country` |
| 공통 | 기초정보관리 › 코드관리 › **통화코드관리** | `ca-currency` | `http://localhost:8080/page/ca-currency` |
| 공통 | 기초정보관리 › 코드관리 › **환율정보관리** | `ca-exchange-rate` | `http://localhost:8080/page/ca-exchange-rate` |
| 공통 | 기초정보관리 › 코드관리 › **은행정보관리** | `ca-bank` | `http://localhost:8080/page/ca-bank` |
| 공통 | 권한관리 › **권한그룹** | `ca-role` | `http://localhost:8080/page/ca-role` |
| 공통 | 권한관리 › **역할그룹** | `ca-role-assign` | `http://localhost:8080/page/ca-role-assign` |
| 공통 | 권한관리 › **메뉴권한** | `ca-role-service` | `http://localhost:8080/page/ca-role-service` |
| 공통 | 권한관리 › **직무연동** | `ca-role-rule` | `http://localhost:8080/page/ca-role-rule` |
| 공통 | 권한관리 › **사용자겸직정보** | `ca-org-assignment` | `http://localhost:8080/page/ca-org-assignment` |
| 공통 | 권한관리 › **부서별겸직정보** | `co_deptCriteriaCrent` | `http://localhost:8080/page/co_deptCriteriaCrent` |
| 공통 | 권한관리 › **대시보드** | `ca-home-service-rule` | `http://localhost:8080/page/ca-home-service-rule` |
| 공통 | 권한관리 › **사용자** | `ca-user-role` | `http://localhost:8080/page/ca-user-role` |
| 공통 | 권한관리 › **사용자(모바일)** | `co_mobileAuthMapp` | `http://localhost:8080/page/co_mobileAuthMapp` |
| 공통 | 권한이력 › **(권한현황) 권한그룹-메뉴** | `ca-user-role-inquiry` | `http://localhost:8080/page/ca-user-role-inquiry` |
| 공통 | 권한이력 › **(권한현황) 권한그룹-사용자** | `COD10` | `http://localhost:8080/page/COD10` |
| 공통 | 권한이력 › **(권한현황) 사용자-권한그룹** | `COD20` | `http://localhost:8080/page/COD20` |
| 공통 | 권한이력 › **(변경이력) 사용자** | `ca-log-role-grant` | `http://localhost:8080/page/ca-log-role-grant` |
| 공통 | 권한이력 › **(변경이력) 권한그룹** | `ca-log-role-service` | `http://localhost:8080/page/ca-log-role-service` |
| 공통 | 권한이력 › **(접속이력) 개인정보** | `ca-log-privacy` | `http://localhost:8080/page/ca-log-privacy` |
| 공통 | 커뮤니티 › **공지사항** | `co_noticeList` | `http://localhost:8080/page/co_noticeList` |
| 공통 | 공통기능 › **시스템승인-Sample** | `COG01` | `http://localhost:8080/page/COG01` |
| 공통 | 공통기능 › **시스템승인-Dialog** | `COG02` | `http://localhost:8080/page/COG02` |
| 공통 | 공통기능 › **전자메일·SMS-Template** | `ca-msg-template` | `http://localhost:8080/page/ca-msg-template` |
| 공통 | 공통기능 › **문서양식관리** | `co_elcDoc` | `http://localhost:8080/page/co_elcDoc` |
| 공통 | 공통기능 › **전자서명검증** | `COG20` | `http://localhost:8080/page/COG20` |
| 공통 | 공통기능 › **인증서유효기간** | `COG30` | `http://localhost:8080/page/COG30` |
| 공통 | 시스템관리 › **도움말** | `co_mnuHelpMng` | `http://localhost:8080/page/co_mnuHelpMng` |
| 공통 | 시스템관리 › **메뉴정보** | `ca-service` | `http://localhost:8080/page/ca-service` |
| 공통 | 시스템관리 › **에러로그** | `ca-log-error` | `http://localhost:8080/page/ca-log-error` |
| 공통 | 시스템관리 › **결재로그** | `co_aprvLog` | `http://localhost:8080/page/co_aprvLog` |
| 공통 | 시스템관리 › **시스템접속로그** | `ca-log-login` | `http://localhost:8080/page/ca-log-login` |
| 공통 | 시스템관리 › **시스템접근통제** | `ca-allowed-ip` | `http://localhost:8080/page/ca-allowed-ip` |
| 공통 | 시스템관리 › **세션모니터링** | `ca-user-session` | `http://localhost:8080/page/ca-user-session` |
| 공통 | 시스템관리 › 체크리스트 › **DB객체관리** | `co_dbObject` | `http://localhost:8080/page/co_dbObject` |
| 하자 |  › **하자** | `db_dashBoardCS` | `http://localhost:8080/page/db_dashBoardCS` |
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
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **사전방문 예약** | `cs_visitRsv` | `http://localhost:8080/page/cs_visitRsv` |
| 하자 | 하자 등록 및 처리 › 입주자사전점검 › **입주 지원** | `cs_mvinMng` | `http://localhost:8080/page/cs_mvinMng` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **표준인수인계파일등록** | `cs_tnhFile` | `http://localhost:8080/page/cs_tnhFile` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **현장인수인계요청** | `cs_siteTnhReq` | `http://localhost:8080/page/cs_siteTnhReq` |
| 하자 | 하자 등록 및 처리 › 인수인계 › **현장인수인계승인** | `cs_siteTnhAprv` | `http://localhost:8080/page/cs_siteTnhAprv` |
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
| 하자 |  › **협력업체 공지사항** | `cs_jcAnncBrdLst` | `http://localhost:8080/page/cs_jcAnncBrdLst` |
| 하자 |  › **발주 내역** | `cs_ordDtls` | `http://localhost:8080/page/cs_ordDtls` |
| 시스템 | 권한그룹 › 권한 부여 › **권한위임 관리** | `sys-ca-role-delegation` | `http://localhost:8080/page/sys-ca-role-delegation` |
| 시스템 | 기준정보 › **채번 규칙** | `sys-ca-numbering-rule` | `http://localhost:8080/page/sys-ca-numbering-rule` |
| 시스템 | 기준정보 › **코드 등록 요청 관리** | `sys-ca-code-request` | `http://localhost:8080/page/sys-ca-code-request` |
| 시스템 | 기준정보 › **결정표** | `sys-ca-decision-table` | `http://localhost:8080/page/sys-ca-decision-table` |
| 시스템 | 결재·문서 › 결재 › **결재 양식 관리** | `sys-ca-approval-form-template` | `http://localhost:8080/page/sys-ca-approval-form-template` |
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
| 시스템 | 플랫폼 › 다국어 › **언어코드 관리** | `ca-language` | `http://localhost:8080/page/ca-language` |
| 시스템 | 플랫폼 › 다국어 › **시스템언어 관리** | `ca-system-language` | `http://localhost:8080/page/ca-system-language` |
| 시스템 | 플랫폼 › 다국어 › **라벨 관리** | `ca-label` | `http://localhost:8080/page/ca-label` |
| 시스템 | 플랫폼 › 다국어 › **메시지 관리** | `ca-message` | `http://localhost:8080/page/ca-message` |
| 시스템 | 플랫폼 › 다국어 › **포맷정책 관리** | `ca-format-policy` | `http://localhost:8080/page/ca-format-policy` |
| 시스템 | 보안 › 인증 › **인증정책 관리** | `ca-auth-policy` | `http://localhost:8080/page/ca-auth-policy` |
| 시스템 | 보안 › 인증 › **통합 인증 공급자** | `ca-auth-provider` | `http://localhost:8080/page/ca-auth-provider` |
| 시스템 | 보안 › 인증 › **SCIM 2.0 공급자** | `ca-scim-provider` | `http://localhost:8080/page/ca-scim-provider` |
| 시스템 | 보안 › 인증 › **인증항목패턴 관리** | `ca-auth-item-pattern` | `http://localhost:8080/page/ca-auth-item-pattern` |
| 시스템 | 보안 › 인증 › **외부 사용자 초대·승인·만료** | `ca-external-access` | `http://localhost:8080/page/ca-external-access` |
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
| 시스템 | 보안 › 개인정보 › **암호화 도구** | `ca-crypto-tool` | `http://localhost:8080/page/ca-crypto-tool` |
| 시스템 | 운영·로그 › 로그 › **페이지 로그** | `ca-log-page` | `http://localhost:8080/page/ca-log-page` |
| 시스템 | 운영·로그 › 로그 › **API 로그** | `ca-log-service` | `http://localhost:8080/page/ca-log-service` |
| 시스템 | 운영·로그 › 로그 › **파일다운로드 이력** | `ca-log-file-download` | `http://localhost:8080/page/ca-log-file-download` |
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
| 시스템 | 개발도구 › DB·데이터 › **코드 등록 요청 관리** | `code-request` | `http://localhost:8080/page/code-request` |
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
| 개인 | 문서 › **시트** | `common-sheet` | `http://localhost:8080/page/common-sheet` |
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

## 3. 이전 문서(11/16)와의 차이

- 비교 기준: `erph-menu-erpadmin_old01.md` 의 트리(1장)와 이번 API 응답을 **serviceId** 와 폴더 경로로 대조. 공사는 이전 문서에 없어 비교하지 않았다.
- 「이름·위치 변경」은 serviceId 가 같은데 폴더 경로나 이름이 다른 화면이다.

### 공통

- 화면 49 → 39 · 폴더 16 → 8 · 사라진 화면 29 · 새 화면 19 · 이름·위치 변경 20

**사라진 화면** (이전 경로 | serviceId)

- 조직·사용자 › 조직 › 회사 관리 | `ca-company`
- 조직·사용자 › 조직 › 사업장 관리 | `ca-business-place`
- 조직·사용자 › 조직 › 조직 구조 관리 | `ca-org-structure`
- 조직·사용자 › 사용자 › 사용자 관리 | `ca-hmp-user-manage`
- 조직·사용자 › 사용자 › 사용자 법인 소속 관리 | `ca-user-company`
- 조직·사용자 › 협력업체 › 협력업체사용자 관리 | `ca-partner-user`
- 권한 › 권한 정의 › 공통권한 관리 | `ca-role-common`
- 권한 › 권한 부여 › 사용자/권한매핑 | `ca-user-role-by-user`
- 권한 › 권한 부여 › 권한위임 관리 | `ca-role-delegation`
- 메뉴·업무그룹 › 업무그룹 관리 | `ca-biz-group`
- 메뉴·업무그룹 › 매뉴얼 | `ca-manual-viewer`
- 기준정보 › 채번 규칙 | `ca-numbering-rule`
- 기준정보 › 결정표 | `ca-decision-table`
- 결재·문서 › 결재 › 결재 현황 | `ca-approval-dashboard`
- 결재·문서 › 결재 › 결재 양식 관리 | `ca-approval-form-template`
- 결재·문서 › 문서 양식 › 문서 유형 관리 | `ca-smart-doc-type`
- 결재·문서 › 문서 양식 › 문서 템플릿 | `ca-doc-template`
- 결재·문서 › 문서 양식 › 문서 템플릿 관리 | `ca-smart-doc-template`
- 결재·문서 › 문서 양식 › 전자 양식 디자이너 | `ca-eform-designer`
- 결재·문서 › 문서 관리 › 문서 탐색 | `ca-edms-browse`
- 결재·문서 › 문서 관리 › 문서 등록/편집 | `ca-edms-doc-manage`
- 결재·문서 › 문서 관리 › 문서 보안 처리 | `ca-common-doc-protect`
- 결재·문서 › 문서 관리 › 보존 정책 관리 | `ca-retention-policy`
- 게시·알림 › 게시 › 게시판관리 | `ca-board-manage`
- 게시·알림 › 게시 › 문의 관리 | `ca-inquiry`
- 게시·알림 › 메시지·알림 › 통합 메시지 발송 | `ca-msg-send`
- 게시·알림 › 메시지·알림 › 메시지 이력 | `ca-msg-history`
- 게시·알림 › 메시지·알림 › 수신거부 관리 | `ca-msg-opt-out`
- 게시·알림 › 메시지·알림 › 알림 관리 | `ca-notification-manage`

**새로 생긴 화면** (현재 경로 | serviceId)

- 권한관리 › 부서별겸직정보 | `co_deptCriteriaCrent`
- 권한관리 › 대시보드 | `ca-home-service-rule`
- 권한이력 › (권한현황) 권한그룹-사용자 | `COD10`
- 권한이력 › (권한현황) 사용자-권한그룹 | `COD20`
- 권한이력 › (변경이력) 사용자 | `ca-log-role-grant`
- 권한이력 › (변경이력) 권한그룹 | `ca-log-role-service`
- 권한이력 › (접속이력) 개인정보 | `ca-log-privacy`
- 커뮤니티 › 공지사항 | `co_noticeList`
- 공통기능 › 시스템승인-Sample | `COG01` · 화면 파일 없음(pageMissing)
- 공통기능 › 시스템승인-Dialog | `COG02` · 화면 파일 없음(pageMissing)
- 공통기능 › 전자서명검증 | `COG20`
- 공통기능 › 인증서유효기간 | `COG30`
- 시스템관리 › 도움말 | `co_mnuHelpMng`
- 시스템관리 › 에러로그 | `ca-log-error`
- 시스템관리 › 결재로그 | `co_aprvLog`
- 시스템관리 › 시스템접속로그 | `ca-log-login`
- 시스템관리 › 시스템접근통제 | `ca-allowed-ip`
- 시스템관리 › 세션모니터링 | `ca-user-session`
- 시스템관리 › 체크리스트 › DB객체관리 | `co_dbObject`

**이름·위치 변경** (serviceId | 이전 → 현재)

- `co_deptMng` | 조직·사용자 › 조직 › 부서/조직 정보 → 기초정보관리 › 부서/현장정보
- `ca-employee` | 조직·사용자 › 사용자 › 임직원 관리 → 기초정보관리 › 사용자정보
- `ca-org-assignment` | 조직·사용자 › 사용자 › 사용자 발령 관리 → 권한관리 › 사용자겸직정보
- `ca-partner` | 조직·사용자 › 협력업체 › 협력업체 관리 → 기초정보관리 › 거래처관리
- `ca-role` | 권한 › 권한 정의 › 권한 관리 → 권한관리 › 권한그룹
- `ca-role-rule` | 권한 › 권한 정의 › 발령권한 관리 → 권한관리 › 직무연동
- `ca-user-role` | 권한 › 권한 부여 › 권한/사용자매핑 → 권한관리 › 사용자
- `ca-role-assign` | 권한 › 권한 부여 › 지정권한 관리 → 권한관리 › 역할그룹
- `co_mobileAuthMapp` | 권한 › 권한 부여 › 사용자(모바일) → 권한관리 › 사용자(모바일)
- `ca-user-role-inquiry` | 권한 › 권한 부여 › 사용자권한 조회 → 권한이력 › (권한현황) 권한그룹-메뉴
- `ca-service` | 메뉴·업무그룹 › 메뉴트리관리 → 시스템관리 › 메뉴정보
- `ca-role-service` | 메뉴·업무그룹 › 권한/메뉴매핑 → 권한관리 › 메뉴권한
- `ca-code` | 기준정보 › 코드 관리 → 기초정보관리 › 공통코드
- `ca-country` | 기준정보 › 국가코드 관리 → 기초정보관리 › 코드관리 › 국가코드관리
- `ca-currency` | 기준정보 › 화폐 관리 → 기초정보관리 › 코드관리 › 통화코드관리
- `ca-exchange-rate` | 기준정보 › 환율 관리 → 기초정보관리 › 코드관리 › 환율정보관리
- `ca-bank` | 기준정보 › 은행 관리 → 기초정보관리 › 코드관리 › 은행정보관리
- `ca-holiday` | 기준정보 › 공휴일 관리 → 기초정보관리 › 휴일관리
- `co_elcDoc` | 결재·문서 › 결재 › 문서양식관리 → 공통기능 › 문서양식관리
- `ca-msg-template` | 게시·알림 › 메시지·알림 › 메시지템플릿 관리 → 공통기능 › 전자메일·SMS-Template

**사라진 폴더**: 게시·알림, 게시·알림 › 게시, 게시·알림 › 메시지·알림, 결재·문서, 결재·문서 › 결재, 결재·문서 › 문서 관리, 결재·문서 › 문서 양식, 권한, 권한 › 권한 부여, 권한 › 권한 정의, 기준정보, 메뉴·업무그룹, 조직·사용자, 조직·사용자 › 사용자, 조직·사용자 › 조직, 조직·사용자 › 협력업체

**새 폴더**: 공통기능, 권한관리, 권한이력, 기초정보관리, 기초정보관리 › 코드관리, 시스템관리, 시스템관리 › 체크리스트, 커뮤니티

### 하자

- 화면 61 → 57 · 폴더 15 → 14 · 사라진 화면 4 · 새 화면 0 · 이름·위치 변경 0

**사라진 화면** (이전 경로 | serviceId)

- 하자 등록 및 처리 › 준공전품질점검 › 품질점검 | `cs_csChkMng_qlt`
- 하자 등록 및 처리 › 준공전품질점검 › 주부품질점검 | `cs_csChkMng_hwf`
- 하자 등록 및 처리 › 입주자사전점검 › 사전점검 | `cs_csChkMng_bf`
- 하자 등록 및 처리 › AS관리 › A/S관리 | `cs_csChkMng`

**사라진 폴더**: 하자 등록 및 처리 › 준공전품질점검

### 시스템

- 화면 172 → 133 · 폴더 43 → 37 · 사라진 화면 41 · 새 화면 2 · 이름·위치 변경 1

**사라진 화면** (이전 경로 | serviceId)

- 조직·사용자 › 조직 › 회사 관리 | `sys-ca-company`
- 조직·사용자 › 조직 › 사업장 관리 | `sys-ca-business-place`
- 조직·사용자 › 조직 › 부서/조직 정보 | `sys-co_deptMng`
- 조직·사용자 › 조직 › 조직 구조 관리 | `sys-ca-org-structure`
- 조직·사용자 › 사용자 › 임직원 관리 | `sys-ca-employee`
- 조직·사용자 › 사용자 › 사용자 관리 | `sys-ca-hmp-user-manage`
- 조직·사용자 › 사용자 › 사용자 법인 소속 관리 | `sys-ca-user-company`
- 조직·사용자 › 사용자 › 사용자 발령 관리 | `sys-ca-org-assignment`
- 조직·사용자 › 협력업체 › 협력업체 관리 | `sys-ca-partner`
- 조직·사용자 › 협력업체 › 협력업체사용자 관리 | `sys-ca-partner-user`
- 권한 › 권한 정의 › 권한 관리 | `sys-ca-role`
- 권한 › 권한 정의 › 공통권한 관리 | `sys-ca-role-common`
- 권한 › 권한 정의 › 발령권한 관리 | `sys-ca-role-rule`
- 권한 › 권한 부여 › 권한/사용자매핑 | `sys-ca-user-role`
- 권한 › 권한 부여 › 사용자/권한매핑 | `sys-ca-user-role-by-user`
- 권한 › 권한 부여 › 지정권한 관리 | `sys-ca-role-assign`
- 권한 › 권한 부여 › 사용자(모바일) | `sys-co_mobileAuthMapp`
- 권한 › 권한 부여 › 사용자권한 조회 | `sys-ca-user-role-inquiry`
- 메뉴·업무그룹 › 업무그룹 관리 | `sys-ca-biz-group`
- 메뉴·업무그룹 › 프로그램관리 | `ca-program`
- 메뉴·업무그룹 › 메뉴트리관리 | `sys-ca-service`
- 메뉴·업무그룹 › 권한/메뉴매핑 | `sys-ca-role-service`
- 메뉴·업무그룹 › 매뉴얼 | `sys-ca-manual-viewer`
- 기준정보 › 코드 관리 | `sys-ca-code`
- 기준정보 › 국가코드 관리 | `sys-ca-country`
- 기준정보 › 화폐 관리 | `sys-ca-currency`
- 기준정보 › 환율 관리 | `sys-ca-exchange-rate`
- 기준정보 › 은행 관리 | `sys-ca-bank`
- 기준정보 › 공휴일 관리 | `sys-ca-holiday`
- 결재·문서 › 결재 › 결재 현황 | `sys-ca-approval-dashboard`
- 결재·문서 › 결재 › 문서양식관리 | `sys-co_elcDoc`
- 게시·알림 › 메시지·알림 › 메시지템플릿 관리 | `sys-ca-msg-template`
- 플랫폼 › 설정·정책 › 홈 화면 규칙 | `ca-home-service-rule`
- 보안 › 인증 › 사용자 세션 관리 | `ca-user-session`
- 보안 › 접근 통제 › 허용IP 관리 | `ca-allowed-ip`
- 보안 › 개인정보 › 개인정보 로그 | `ca-log-privacy`
- 운영·로그 › 로그 › 오류 로그 | `ca-log-error`
- 운영·로그 › 로그 › 로그인 로그 | `ca-log-login`
- 운영·로그 › 로그 › 권한메뉴 로그 | `ca-log-role-service`
- 운영·로그 › 로그 › 권한부여 로그 | `ca-log-role-grant`
- 연동 › 결재·메시지 연동 › GW결재 목록 | `ca-gw-approval-list`

**새로 생긴 화면** (현재 경로 | serviceId)

- 기준정보 › 코드 등록 요청 관리 | `sys-ca-code-request`
- 개발도구 › DB·데이터 › 코드 등록 요청 관리 | `code-request`

**이름·위치 변경** (serviceId | 이전 → 현재)

- `sys-ca-role-delegation` | 권한 › 권한 부여 › 권한위임 관리 → 권한그룹 › 권한 부여 › 권한위임 관리

**사라진 폴더**: 권한, 권한 › 권한 부여, 권한 › 권한 정의, 메뉴·업무그룹, 조직·사용자, 조직·사용자 › 사용자, 조직·사용자 › 조직, 조직·사용자 › 협력업체

**새 폴더**: 권한그룹, 권한그룹 › 권한 부여

### 개인

- 화면 25 → 26 · 폴더 7 → 7 · 사라진 화면 0 · 새 화면 1 · 이름·위치 변경 0

**새로 생긴 화면** (현재 경로 | serviceId)

- 문서 › 시트 | `common-sheet`

### 요약

| 상단 탭 | 화면(이전→현재) | 폴더(이전→현재) | 사라짐 | 새로 생김 | 이름·위치 변경 |
|---|---|---|---:|---:|---:|
| 공통 | 49 → 39 | 16 → 8 | 29 | 19 | 20 |
| 하자 | 61 → 57 | 15 → 14 | 4 | 0 | 0 |
| 시스템 | 172 → 133 | 43 → 37 | 41 | 2 | 1 |
| 개인 | 25 → 26 | 7 → 7 | 0 | 1 | 0 |

### 메모

- 공통 탭은 이전의 조직·사용자/권한/메뉴·업무그룹/기준정보 등 폴더가 사라지고 기초정보관리·권한관리·권한이력·커뮤니티·공통기능·시스템관리 폴더(AS-IS 메뉴 모양)로 바뀌었다.
- 시스템 탭에서는 조직·사용자, 권한, 메뉴·업무그룹 폴더가 없어지고, `권한` 폴더는 `권한그룹`으로 이름이 바뀌어 권한위임 관리만 남았다. 따라서 `MENU_SC_MENU`(메뉴·업무그룹)와 그 아래 프로그램관리(`ca-program`)·메뉴트리관리(`sys-ca-service`)는 현재 메뉴 응답에 없다. 메뉴트리관리 프로그램은 공통 › 시스템관리 › **메뉴정보**(`ca-service`)로 보인다.
- 변경 원인(어느 SQL·화면 편집인지)은 확인하지 못했다. 저장소 `db/common-admin` 에는 「기초정보관리」 문자열이 없다.
