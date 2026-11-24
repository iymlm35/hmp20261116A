# erp-h 상단메뉴 '공통' left메뉴 트리 (DOM 기준 · 경로 포함)

- 조회: 2026-11-23 · `http://localhost:8080/` · 로그인 계정 `erpadmin` · Aside 브라우저에서 상단 탭 '공통'을 선택한 상태로 사이드바 `.menu-tree` DOM을 재귀로 읽었다(접힌 폴더도 DOM에 있어 펼치지 않고 읽음).
- 경로 칸은 같은 세션의 메뉴 API `POST /sys/menu/list` 응답(공통 트리)과 이름·순서로 맞춰 붙였다. DOM 화면 40개 = API 화면 40개, 이름·순서 모두 일치.
- 트리 표기: 폴더는 「이름/  폴더키」, 화면은 「이름  serviceId → 화면 주소 · 소스 파일」 순서.
- **화면 주소** `/page/<serviceId>`: 40개 모두 HEAD 요청에 200 응답을 확인했다. 다만 이 셸은 화면 존재와 무관하게 200을 줄 수 있어 「주소가 열린다」는 뜻이지 「화면이 있다」는 보증은 아니다. 메뉴를 눌러도 주소창은 `/` 그대로(탭형 SPA)라 주소창으로 확인한 것은 아니다.
- **소스**: 메뉴 API에는 소스 경로가 없어 `docs/work/common-asis/71-매핑-화면-API.tsv`의 「목표_page」(예 `/co/comCode`)를 `products-erp-h/app/src/main/resources/pages/<목표_page>.js`로 풀고 **파일이 실제 있는 것만** 적었다. 브라우저가 런타임에 실제로 불러오는 파일을 직접 확인한 것은 아니다.
- 폴더 8 · 화면 40. 11/20 문서(`erph-menu-erpadmin.md`)의 serviceId(`ca-code` 등)는 이번 응답에서 AS-IS 코드(`COA01` 등)로 바뀌어 있다 — 「공통」 화면 이름은 같고 ID만 다르다.

## 참조 문서(11/20)와의 차이

- 추가: `기초정보관리/법인/회사정보` (참조 문서는 화면 39개, 이번 DOM은 40개)
- serviceId 표기: 11/20 문서의 `ca-*`·`co_*` → 이번 응답의 AS-IS 코드(`CO*`). 그 외 폴더·화면 이름과 순서는 일치.
- 같은 폴더의 코드 폴더 키는 `CO`(상단), `COA`·`COB`·`COD`·`COC`·`COG`·`COZ00`, 하위 `COA10`·`COZ90`.

## 1. 트리

```text
공통  `CO`
├── 기초정보관리/  `COA`
│   ├── 공통코드  `COA01` → `/page/COA01` · 소스 `pages/co/comCode.js`
│   ├── 사용자정보  `COA02` → `/page/COA02` · 소스 `pages/co/usrMng.js`
│   ├── 부서/현장정보  `COA03` → `/page/COA03` · 소스 `pages/co/deptMng.js`
│   ├── 법인/회사정보  `COA08` → `/page/COA08` · 소스 (erp-h 저장소에 `/co/corpMng` 파일 없음)
│   ├── 거래처관리  `COA05` → `/page/COA05` · 소스 `pages/co/tradeInfo.js`
│   ├── 휴일관리  `COA20` → `/page/COA20` · 소스 `pages/co/holidayMng.js`
│   └── 코드관리/  `COA10`
│       ├── 국가코드관리  `COA07` → `/page/COA07` · 소스 `pages/co/ctryCode.js`
│       ├── 통화코드관리  `COA06` → `/page/COA06` · 소스 `pages/co/curyInfo.js`
│       ├── 환율정보관리  `COA09` → `/page/COA09` · 소스 `pages/co/fertInfo.js`
│       └── 은행정보관리  `COA11` → `/page/COA11` · 소스 `pages/co/bnkInfo.js`
├── 권한관리/  `COB`
│   ├── 권한그룹  `COB01` → `/page/COB01` · 소스 `pages/co/authGrp.js`
│   ├── 역할그룹  `COB02` → `/page/COB02` · 소스 `pages/co/roleGrp.js`
│   ├── 메뉴권한  `COB05` → `/page/COB05` · 소스 `pages/co/authMnu.js`
│   ├── 직무연동  `COB08` → `/page/COB08` · 소스 `pages/co/authGrpMapp.js`
│   ├── 사용자겸직정보  `COA04` → `/page/COA04` · 소스 `pages/co/deptCrent.js`
│   ├── 부서별겸직정보  `COB00` → `/page/COB00` · 소스 `pages/co/deptCriteriaCrent.js`
│   ├── 대시보드  `COB07` → `/page/COB07` · 소스 (erp-h 저장소에 `/co/dashbrdAuth` 파일 없음)
│   ├── 사용자  `COB03` → `/page/COB03` · 소스 `pages/co/authMapp.js`
│   └── 사용자(모바일)  `COB06` → `/page/COB06` · 소스 `pages/co/mobileAuthMapp.js`
├── 권한이력/  `COD`
│   ├── (권한현황) 권한그룹-메뉴  `COD30` → `/page/COD30` · 소스 `pages/co/authGrpInfo.js`
│   ├── (권한현황) 권한그룹-사용자  `COD10` → `/page/COD10` · 소스 `pages/co/authUsrInfo.js`
│   ├── (권한현황) 사용자-권한그룹  `COD20` → `/page/COD20` · 소스 `pages/co/usrAuthInfo.js`
│   ├── (변경이력) 사용자  `COD40` → `/page/COD40` · 소스 `pages/co/chgAuthUsrInfo.js`
│   ├── (변경이력) 권한그룹  `COD50` → `/page/COD50` · 소스 `pages/co/chgAuthGrpInfo.js`
│   └── (접속이력) 개인정보  `COD60` → `/page/COD60` · 소스 `pages/co/privCnct.js`
├── 커뮤니티/  `COC`
│   └── 공지사항  `COC01` → `/page/COC01` · 소스 `pages/co/noticeList.js`
├── 공통기능/  `COG`
│   ├── 시스템승인-Sample  `COG01` → `/page/COG01` · 소스 `pages/co/sysAprvTest.js`
│   ├── 시스템승인-Dialog  `COG02` → `/page/COG02` · 소스 (erp-h 저장소에 `/co/sysAprv` 파일 없음) · 화면 파일 없음(pageMissing)
│   ├── 전자메일·SMS-Template  `COG03` → `/page/COG03` · 소스 `pages/co/mailSmsfmt.js`
│   ├── 문서양식관리  `COG10` → `/page/COG10` · 소스 `pages/co/elcDoc.js`
│   ├── 전자서명검증  `COG20` → `/page/COG20` · 소스 `pages/co/signValidation.js`
│   └── 인증서유효기간  `COG30` → `/page/COG30` · 소스 `pages/co/signValidityCheck.js`
└── 시스템관리/  `COZ00`
    ├── 도움말  `COZ05` → `/page/COZ05` · 소스 `pages/co/mnuHelpMng.js`
    ├── 메뉴정보  `COZ04` → `/page/COZ04` · 소스 `pages/co/mnuMng.js`
    ├── 에러로그  `COZ09` → `/page/COZ09` · 소스 `pages/co/errorLog.js`
    ├── 결재로그  `COZ07` → `/page/COZ07` · 소스 `pages/co/aprvLog.js`
    ├── 시스템접속로그  `COZ80` → `/page/COZ80` · 소스 `pages/co/sysCnct.js`
    ├── 시스템접근통제  `COZ81` → `/page/COZ81` · 소스 `pages/co/usrAccessCtrl.js`
    ├── 세션모니터링  `COZ82` → `/page/COZ82` · 소스 `pages/co/userSessionMonitor.js`
    └── 체크리스트/  `COZ90`
        └── DB객체관리  `COZ9001` → `/page/COZ9001` · 소스 `pages/co/dbObject.js`
```

## 2. 화면별 메뉴 경로 표

| 메뉴 경로 | serviceId | 화면 주소 | erp-h 소스(pages) | hmp 원본 program 경로 |
|---|---|---|---|---|
| 공통 › 기초정보관리 › **공통코드** | `COA01` | `http://localhost:8080/page/COA01` | `pages/co/comCode.js` | `code/code` |
| 공통 › 기초정보관리 › **사용자정보** | `COA02` | `http://localhost:8080/page/COA02` | `pages/co/usrMng.js` | `organization/employee` |
| 공통 › 기초정보관리 › **부서/현장정보** | `COA03` | `http://localhost:8080/page/COA03` | `pages/co/deptMng.js` | `co/deptMng` |
| 공통 › 기초정보관리 › **법인/회사정보** | `COA08` | `http://localhost:8080/page/COA08` | — | `organization/company-group` |
| 공통 › 기초정보관리 › **거래처관리** | `COA05` | `http://localhost:8080/page/COA05` | `pages/co/tradeInfo.js` | `organization/partner` |
| 공통 › 기초정보관리 › **휴일관리** | `COA20` | `http://localhost:8080/page/COA20` | `pages/co/holidayMng.js` | `common/holiday` |
| 공통 › 기초정보관리 › 코드관리 › **국가코드관리** | `COA07` | `http://localhost:8080/page/COA07` | `pages/co/ctryCode.js` | `common/country` |
| 공통 › 기초정보관리 › 코드관리 › **통화코드관리** | `COA06` | `http://localhost:8080/page/COA06` | `pages/co/curyInfo.js` | `common/currency` |
| 공통 › 기초정보관리 › 코드관리 › **환율정보관리** | `COA09` | `http://localhost:8080/page/COA09` | `pages/co/fertInfo.js` | `common/exchange-rate` |
| 공통 › 기초정보관리 › 코드관리 › **은행정보관리** | `COA11` | `http://localhost:8080/page/COA11` | `pages/co/bnkInfo.js` | `common/bank` |
| 공통 › 권한관리 › **권한그룹** | `COB01` | `http://localhost:8080/page/COB01` | `pages/co/authGrp.js` | `role/role` |
| 공통 › 권한관리 › **역할그룹** | `COB02` | `http://localhost:8080/page/COB02` | `pages/co/roleGrp.js` | `role/role-assign` |
| 공통 › 권한관리 › **메뉴권한** | `COB05` | `http://localhost:8080/page/COB05` | `pages/co/authMnu.js` | `role/role-service` |
| 공통 › 권한관리 › **직무연동** | `COB08` | `http://localhost:8080/page/COB08` | `pages/co/authGrpMapp.js` | `role/role-rule` |
| 공통 › 권한관리 › **사용자겸직정보** | `COA04` | `http://localhost:8080/page/COA04` | `pages/co/deptCrent.js` | `system/org-assignment` |
| 공통 › 권한관리 › **부서별겸직정보** | `COB00` | `http://localhost:8080/page/COB00` | `pages/co/deptCriteriaCrent.js` | `co/deptCriteriaCrent` |
| 공통 › 권한관리 › **대시보드** | `COB07` | `http://localhost:8080/page/COB07` | — | `dashboard/home-service-rule` |
| 공통 › 권한관리 › **사용자** | `COB03` | `http://localhost:8080/page/COB03` | `pages/co/authMapp.js` | `role/user-role` |
| 공통 › 권한관리 › **사용자(모바일)** | `COB06` | `http://localhost:8080/page/COB06` | `pages/co/mobileAuthMapp.js` | `co/mobileAuthMapp` |
| 공통 › 권한이력 › **(권한현황) 권한그룹-메뉴** | `COD30` | `http://localhost:8080/page/COD30` | `pages/co/authGrpInfo.js` | `role/user-role-inquiry` |
| 공통 › 권한이력 › **(권한현황) 권한그룹-사용자** | `COD10` | `http://localhost:8080/page/COD10` | `pages/co/authUsrInfo.js` | `co/authUsrInfo` |
| 공통 › 권한이력 › **(권한현황) 사용자-권한그룹** | `COD20` | `http://localhost:8080/page/COD20` | `pages/co/usrAuthInfo.js` | `co/usrAuthInfo` |
| 공통 › 권한이력 › **(변경이력) 사용자** | `COD40` | `http://localhost:8080/page/COD40` | `pages/co/chgAuthUsrInfo.js` | `log/log-role-grant` |
| 공통 › 권한이력 › **(변경이력) 권한그룹** | `COD50` | `http://localhost:8080/page/COD50` | `pages/co/chgAuthGrpInfo.js` | `log/log-role-service` |
| 공통 › 권한이력 › **(접속이력) 개인정보** | `COD60` | `http://localhost:8080/page/COD60` | `pages/co/privCnct.js` | `log/log-privacy` |
| 공통 › 커뮤니티 › **공지사항** | `COC01` | `http://localhost:8080/page/COC01` | `pages/co/noticeList.js` | `co/noticeList` |
| 공통 › 공통기능 › **시스템승인-Sample** | `COG01` | `http://localhost:8080/page/COG01` | `pages/co/sysAprvTest.js` | — |
| 공통 › 공통기능 › **시스템승인-Dialog** | `COG02` | `http://localhost:8080/page/COG02` | — | `co/sysAprvPop` |
| 공통 › 공통기능 › **전자메일·SMS-Template** | `COG03` | `http://localhost:8080/page/COG03` | `pages/co/mailSmsfmt.js` | `co/mailSmsfmt` |
| 공통 › 공통기능 › **문서양식관리** | `COG10` | `http://localhost:8080/page/COG10` | `pages/co/elcDoc.js` | `co/elcDoc` |
| 공통 › 공통기능 › **전자서명검증** | `COG20` | `http://localhost:8080/page/COG20` | `pages/co/signValidation.js` | `co/signValidation` |
| 공통 › 공통기능 › **인증서유효기간** | `COG30` | `http://localhost:8080/page/COG30` | `pages/co/signValidityCheck.js` | `co/signValidityCheck` |
| 공통 › 시스템관리 › **도움말** | `COZ05` | `http://localhost:8080/page/COZ05` | `pages/co/mnuHelpMng.js` | `co/mnuHelpMng` |
| 공통 › 시스템관리 › **메뉴정보** | `COZ04` | `http://localhost:8080/page/COZ04` | `pages/co/mnuMng.js` | `role/service` |
| 공통 › 시스템관리 › **에러로그** | `COZ09` | `http://localhost:8080/page/COZ09` | `pages/co/errorLog.js` | `co/errorLog` |
| 공통 › 시스템관리 › **결재로그** | `COZ07` | `http://localhost:8080/page/COZ07` | `pages/co/aprvLog.js` | `co/aprvLog` |
| 공통 › 시스템관리 › **시스템접속로그** | `COZ80` | `http://localhost:8080/page/COZ80` | `pages/co/sysCnct.js` | `co/sysCnct` |
| 공통 › 시스템관리 › **시스템접근통제** | `COZ81` | `http://localhost:8080/page/COZ81` | `pages/co/usrAccessCtrl.js` | `system/allowed-ip` |
| 공통 › 시스템관리 › **세션모니터링** | `COZ82` | `http://localhost:8080/page/COZ82` | `pages/co/userSessionMonitor.js` | `co/userSessionMonitor` |
| 공통 › 시스템관리 › 체크리스트 › **DB객체관리** | `COZ9001` | `http://localhost:8080/page/COZ9001` | `pages/co/dbObject.js` | `co/dbObject` |

> 「hmp 원본 program 경로」는 `app/src/main/resources/db/module/erp/install/common/co-75-asis-rename.sql`의 `program.program_path`다(hmp 플랫폼 쪽 화면 경로). 플랫폼 저장소는 이 작업폴더 밖이라 해당 파일은 열어 보지 않았다. `COG01`은 program 행이 없다.
