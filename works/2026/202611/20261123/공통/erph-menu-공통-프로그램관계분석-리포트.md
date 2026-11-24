# erp-h '공통' 메뉴 프로그램 간 관계 분석 리포트

- 작성: 2026-11-23 · 대상: 상단메뉴 「공통」 left메뉴 40개 화면(폴더 8) · 계정 `erpadmin` · `http://localhost:8080/`
- 짝 문서: `erph-menu-erpadmin-dom-공통.md`(DOM 트리 · 화면 주소 · 소스 경로)

## 0. 근거와 한계

| 근거 | 무엇을 얻었나 |
|---|---|
| Aside 로 읽은 사이드바 DOM · 메뉴 API(`POST /sys/menu/list`) | 메뉴 계층 · serviceId · 화면 40개 |
| `app/src/main/resources/db/module/erp/install/common/co-75-asis-rename.sql` | 화면 ↔ program(hmp 원본 경로) · 화면 ↔ API 사용(`service_api_usage`) |
| `docs/work/common-asis/71-매핑-화면-API.tsv` · `02-결정표.tsv` | 화면 유형(A·B·C) · 목표 page · TO-BE 대응(hmp 원본 화면) |
| `app/src/main/resources/pages/co/*.js` | 화면에서 다른 화면을 여는 `app.openPopup(...)` 호출 |

한계 — 먼저 읽어 두세요.
- **API 공유 = 같은 백엔드 컨트롤러(URL 첫 토막 `api_service_id`)를 쓴다는 뜻**이다. 같은 표·같은 데이터를 건드린다는 증거는 아니다. 특히 `biz-code`(공통코드 조회)처럼 여러 화면이 쓰는 범용 조회는 관계를 과장한다. 「업무 군집」(§6)은 이 공유와 화면 이름을 함께 본 **해석**이다.
- 화면 코드는 erp-h 저장소의 `pages/co/*.js` 만 읽었다. hmp 플랫폼 저장소(`core` · `feature-*`)는 작업폴더 밖이라 열지 않았다 — 그쪽 화면 안의 호출 관계는 이 리포트에 없다.
- 법인/회사정보(`COA08`) · 대시보드(`COB07`) · 시스템승인-Dialog(`COG02`)는 erp-h 저장소에 목표 page 파일이 없어 호출 관계를 코드로 읽지 못했다. `COG02` 는 메뉴 API 가 `pageMissing=true` 로 표시한다.
- 화면을 눌러 동작을 확인하지는 않았다(메뉴 몇 개를 펼쳐 주소 형식만 확인).

## 1. 핵심 결론

1. **독립 화면이 있고, 얽힌 곳은 권한 영역에 몰려 있다.** 화면 40개 중 다른 화면과 API 서비스를 하나도 공유하지 않는 것은 5개(`COA20 휴일관리` · `COA11 은행정보관리` · `COC01 공지사항` · `COG01` · `COG02`)다. 반대로 권한관리(COB) 화면들과 권한현황 `COD30`은 서로 3~5개 API 서비스를 공유한다.
2. **허브는 `COD30 (권한현황) 권한그룹-메뉴`**(다른 화면 15개와 공유)다. 조회 화면이면서 `biz-group` · `role-manage` · `user-scope` · `company` · `dept` · `org-unit-manage` · `role-service` · `service-manage` · `user` 9개 서비스를 읽는다. 권한 모델이 바뀌면 영향이 가장 먼저 닿는 화면이다.
3. **`COA04 사용자겸직정보`는 메뉴 폴더와 업무 영역이 어긋난다.** 코드 접두는 COA(기초정보)지만 메뉴는 「권한관리」 아래에 있고 program 업무그룹도 `COB`다. 공유 상대는 기초정보(`company`)와 권한(`user-scope` · `user`)에 걸쳐 12개 화면이다.
4. **화면이 직접 다른 화면을 여는 관계는 6개 화면에서 8건**이고, 모두 「목록 → 상세·입력 팝업」 또는 「현황 → 이력 조회」 형태다(§3).
5. **팝업 대상 2건(`co_custDelInfo` · `co_chgAuthGrpInfo`)이 옛 co_* 서비스 ID 를 직접 부른다.** 메뉴 ID 는 AS-IS 코드(COA0502 · COD50)로 바뀌었는데, 이 두 ID 에는 이 SQL 안에 별칭(`serviceAlias`)도 끄기(`use_yn=N`)도 없다. 현재 DB 에서 열리는지는 확인하지 못했다(§7).
6. **구현 방식은 3갈래다.** 이미 co 화면을 쓰는 A형 12개 · hmp 원본 화면을 AS-IS 이름으로 감싼 B형 24개 · co 화면이 없는 C형 4개(71 문서 분류). B·C형은 시스템 메뉴의 hmp 원본과 같은 화면을 둘이 쓰는 관계다(§4).

## 2. 메뉴 계층 관계

| 폴더 | 폴더키 | 화면 수 | 화면 |
|---|---|---:|---|
| 기초정보관리 | `COA` | 6 | 공통코드(`COA01`) · 사용자정보(`COA02`) · 부서/현장정보(`COA03`) · 법인/회사정보(`COA08`) · 거래처관리(`COA05`) · 휴일관리(`COA20`) |
| 기초정보관리 › 코드관리 | `COA10` | 4 | 국가코드관리(`COA07`) · 통화코드관리(`COA06`) · 환율정보관리(`COA09`) · 은행정보관리(`COA11`) |
| 권한관리 | `COB` | 9 | 권한그룹(`COB01`) · 역할그룹(`COB02`) · 메뉴권한(`COB05`) · 직무연동(`COB08`) · 사용자겸직정보(`COA04`) · 부서별겸직정보(`COB00`) · 대시보드(`COB07`) · 사용자(`COB03`) · 사용자(모바일)(`COB06`) |
| 권한이력 | `COD` | 6 | (권한현황) 권한그룹-메뉴(`COD30`) · (권한현황) 권한그룹-사용자(`COD10`) · (권한현황) 사용자-권한그룹(`COD20`) · (변경이력) 사용자(`COD40`) · (변경이력) 권한그룹(`COD50`) · (접속이력) 개인정보(`COD60`) |
| 커뮤니티 | `COC` | 1 | 공지사항(`COC01`) |
| 공통기능 | `COG` | 6 | 시스템승인-Sample(`COG01`) · 시스템승인-Dialog(`COG02`) · 전자메일·SMS-Template(`COG03`) · 문서양식관리(`COG10`) · 전자서명검증(`COG20`) · 인증서유효기간(`COG30`) |
| 시스템관리 | `COZ00` | 7 | 도움말(`COZ05`) · 메뉴정보(`COZ04`) · 에러로그(`COZ09`) · 결재로그(`COZ07`) · 시스템접속로그(`COZ80`) · 시스템접근통제(`COZ81`) · 세션모니터링(`COZ82`) |
| 시스템관리 › 체크리스트 | `COZ90` | 1 | DB객체관리(`COZ9001`) |

- 최대 깊이 3단계(`기초정보관리 › 코드관리 › 국가코드관리` · `시스템관리 › 체크리스트 › DB객체관리`).
- 화면 접두와 폴더가 다른 경우: `COA04` 는 접두 COA 인데 권한관리 폴더 아래에 있다. 나머지는 접두와 폴더가 일치한다(`COZ9001` 은 `COZ90` 폴더).

## 3. 화면이 다른 화면을 여는 관계 (코드 근거)

`pages/co/*.js` 의 `app.openPopup('<서비스ID>')` 호출을 모았다.

| 호출 화면 | → 여는 화면 | 호출 위치 | 성격 |
|---|---|---|---|
| 거래처관리 `COA05` | 거래처등록/수정 `COA0501` | tradeInfo.js:436 | 목록 → 상세·입력 팝업(메뉴에 안 보이는 하위 화면) |
| 거래처관리 `COA05` | 거래처삭제정보관리 `co_custDelInfo`(=COA0502) | tradeInfo.js:369 | 목록 → 삭제정보 조회(옛 ID 로 호출) |
| 공지사항 `COC01` | 공지사항 상세 `COC0101` | noticeList.js:74 | 목록 → 상세 팝업 |
| 공지사항 `COC01` | 공지사항 입력 `COC0102` | noticeList.js:62 | 목록 → 입력 팝업 |
| (권한현황) 권한그룹-메뉴 `COD30` | `co_chgAuthGrpInfo`(=COD50 권한그룹 변경이력) | authGrpInfo.js | 현황 → 변경이력(옛 ID 로 호출) |
| (권한현황) 권한그룹-사용자 `COD10` | `co_chgAuthGrpInfo`(=COD50) | authUsrInfo.js | 현황 → 변경이력(옛 ID 로 호출) |
| 시스템승인-Sample `COG01` | 시스템승인-Dialog `COG02` | sysAprvTest.js | 시험 화면 → 결재 다이얼로그 |
| 결재로그 `COZ07` | 결재로그팝업 `COZ0701` | aprvLog.js:177 | 목록 → 상세 팝업 |

- 메뉴에 안 보이는 하위 화면(`COA0501` · `COA0502` · `COC0101` · `COC0102` · `COZ0701`)은 `11-asis-screens.md` §3 에서 「메뉴에 등록된 하위 화면(사용 7)」으로 분류된 것이다. 다이얼로그형(화면 안에서 연다)은 이 표에 없다 — 예: 권한그룹 「기존권한 복사」 · 휴일관리 4종 · 에러로그 「오류상세」(`queryPop`, 메뉴 자리 `COZ0901`).
- 사용 7개 하위 화면 중 `COA0301 HR정보동기화`(program `co/hrInfoSync`)는 `pages/co/*.js` 어디에서도 `openPopup` 으로 열리지 않는다. 진입 경로를 확인하지 못했다.

## 4. 구현 계층 관계 (erp-h 화면 ↔ hmp 원본 화면)

71 문서의 유형: **A** 이미 co 화면을 연다 · **B** hmp `ca-*` 화면을 열고 옛 co 이식 화면이 있다(AS-IS ID 로 감싼 화면) · **C** co 화면이 없다(감싸기 화면 신규).

| 유형 | 화면 수 | 화면 |
|---|---:|---|
| A | 12 | COA03 · COB00 · COB06 · COD10 · COD20 · COC01 · COG10 · COG20 · COG30 · COZ05 · COZ07 · COZ9001 |
| B | 24 | COA01 · COA02 · COA05 · COA20 · COA07 · COA06 · COA09 · COA11 · COB01 · COB02 · COB05 · COB08 · COA04 · COB07 · COB03 · COD30 · COD40 · COD50 · COD60 · COG03 · COZ04 · COZ09 · COZ80 · COZ82 |
| C | 4 | COA08 · COG01 · COG02 · COZ81 |

- B·C형은 시스템 메뉴의 hmp 원본 화면과 **같은 화면을 둘이 쓰는 관계**다. 결정표(`02-결정표.tsv`)는 이때 시스템 쪽 배치를 `use_yn=N` 으로 끄는 것을 원칙으로 한다(지시 16 · CA-09 「공통에 있는 화면은 시스템에 두지 않음」).
- **1 : N 병합**(공통 화면 하나가 hmp 원본 여러 개를 대체): `COA02 사용자정보` ← `employee` + `hmp-user-manage` + `user-company` · `COB01 권한그룹` ← `role` + `role-common` 외 · `COB08 직무연동` ← `role-rule` + `role-condition` 외 · `COZ04 메뉴정보` ← `service` + `program` + `biz-group` 외 · `COA03 부서/현장정보` ← `dept` + `org-unit` 외. 이 화면들은 hmp 원본이 바뀌면 영향 면적이 넓다. (대응 전체는 §8 마지막 칸)

## 5. API 공유로 본 관계

### 5.1 많이 공유되는 API 서비스 (3개 화면 이상)

| API 서비스 | 쓰는 화면 수 | 화면 |
|---|---:|---|
| `biz-group` | 7 | 권한그룹(COB01) · 역할그룹(COB02) · 메뉴권한(COB05) · 직무연동(COB08) · 사용자(COB03) · (권한현황) 권한그룹-메뉴(COD30) · 메뉴정보(COZ04) |
| `user-scope` | 7 | 권한그룹(COB01) · 역할그룹(COB02) · 메뉴권한(COB05) · 직무연동(COB08) · 사용자겸직정보(COA04) · 사용자(COB03) · (권한현황) 권한그룹-메뉴(COD30) |
| `role-manage` | 6 | 권한그룹(COB01) · 역할그룹(COB02) · 메뉴권한(COB05) · 직무연동(COB08) · 사용자(COB03) · (권한현황) 권한그룹-메뉴(COD30) |
| `user` | 6 | 역할그룹(COB02) · 사용자겸직정보(COA04) · 사용자(COB03) · (권한현황) 권한그룹-메뉴(COD30) · (변경이력) 사용자(COD40) · (접속이력) 개인정보(COD60) |
| `company` | 5 | 사용자정보(COA02) · 법인/회사정보(COA08) · 거래처관리(COA05) · 사용자겸직정보(COA04) · (권한현황) 권한그룹-메뉴(COD30) |
| `dept` | 5 | 사용자정보(COA02) · 직무연동(COB08) · 대시보드(COB07) · 사용자(COB03) · (권한현황) 권한그룹-메뉴(COD30) |
| `service-manage` | 5 | 메뉴권한(COB05) · (권한현황) 권한그룹-메뉴(COD30) · (변경이력) 권한그룹(COD50) · (접속이력) 개인정보(COD60) · 메뉴정보(COZ04) |
| `org-unit-manage` | 4 | 공통코드(COA01) · 직무연동(COB08) · 사용자겸직정보(COA04) · (권한현황) 권한그룹-메뉴(COD30) |
| `role` | 4 | 대시보드(COB07) · (변경이력) 사용자(COD40) · (변경이력) 권한그룹(COD50) · 시스템접근통제(COZ81) |
| `biz-code` | 4 | 전자메일·SMS-Template(COG03) · 문서양식관리(COG10) · 시스템접속로그(COZ80) · DB객체관리(COZ9001) |
| `coz` | 3 | 부서/현장정보(COA03) · 사용자(모바일)(COB06) · 결재로그(COZ07) |
| `currency` | 3 | 국가코드관리(COA07) · 통화코드관리(COA06) · 환율정보관리(COA09) |
| `auth` | 3 | 사용자(모바일)(COB06) · (권한현황) 권한그룹-사용자(COD10) · (권한현황) 사용자-권한그룹(COD20) |

- `biz-code`(공통코드 조회)는 범용 조회라 의존이라기보다 「공통코드를 읽는다」는 표시에 가깝다.
- `biz-group` · `user-scope` · `role-manage` · `company` · `dept` 는 권한 화면들이 공통으로 쓰는 기준 데이터(업무그룹 · 사용자 범위 · 권한그룹 · 법인 · 부서) 조회 계열이다.

### 5.2 화면 쌍 — 공유 서비스 3개 이상

| 화면 A | 화면 B | 공유 수 | 공유 서비스 |
|---|---|---:|---|
| 메뉴권한(COB05) | (권한현황) 권한그룹-메뉴(COD30) | 5 | `biz-group` `role-manage` `role-service` `service-manage` `user-scope` |
| 직무연동(COB08) | (권한현황) 권한그룹-메뉴(COD30) | 5 | `biz-group` `dept` `org-unit-manage` `role-manage` `user-scope` |
| 사용자(COB03) | (권한현황) 권한그룹-메뉴(COD30) | 5 | `biz-group` `dept` `role-manage` `user-scope` `user` |
| 역할그룹(COB02) | 사용자(COB03) | 4 | `biz-group` `role-manage` `user-scope` `user` |
| 역할그룹(COB02) | (권한현황) 권한그룹-메뉴(COD30) | 4 | `biz-group` `role-manage` `user-scope` `user` |
| 직무연동(COB08) | 사용자(COB03) | 4 | `biz-group` `dept` `role-manage` `user-scope` |
| 사용자겸직정보(COA04) | (권한현황) 권한그룹-메뉴(COD30) | 4 | `company` `org-unit-manage` `user-scope` `user` |
| 권한그룹(COB01) | 역할그룹(COB02) | 3 | `biz-group` `role-manage` `user-scope` |
| 권한그룹(COB01) | 메뉴권한(COB05) | 3 | `biz-group` `role-manage` `user-scope` |
| 권한그룹(COB01) | 직무연동(COB08) | 3 | `biz-group` `role-manage` `user-scope` |
| 권한그룹(COB01) | 사용자(COB03) | 3 | `biz-group` `role-manage` `user-scope` |
| 권한그룹(COB01) | (권한현황) 권한그룹-메뉴(COD30) | 3 | `biz-group` `role-manage` `user-scope` |
| 역할그룹(COB02) | 메뉴권한(COB05) | 3 | `biz-group` `role-manage` `user-scope` |
| 역할그룹(COB02) | 직무연동(COB08) | 3 | `biz-group` `role-manage` `user-scope` |
| 메뉴권한(COB05) | 직무연동(COB08) | 3 | `biz-group` `role-manage` `user-scope` |
| 메뉴권한(COB05) | 사용자(COB03) | 3 | `biz-group` `role-manage` `user-scope` |
| 메뉴권한(COB05) | 메뉴정보(COZ04) | 3 | `biz-group` `program` `service-manage` |

### 5.3 화면별 연결 수 (다른 화면과 API 서비스를 1개 이상 공유하는 화면 수)

| 연결 수 | 화면 |
|---:|---|
| 15 | (권한현황) 권한그룹-메뉴(COD30) |
| 12 | 사용자겸직정보(COA04) |
| 11 | 사용자(COB03) |
| 10 | 직무연동(COB08) |
| 9 | 역할그룹(COB02) · 메뉴권한(COB05) |
| 8 | 사용자정보(COA02) · (변경이력) 사용자(COD40) · (접속이력) 개인정보(COD60) · 메뉴정보(COZ04) |
| 7 | 권한그룹(COB01) · 대시보드(COB07) · (변경이력) 권한그룹(COD50) |
| 5 | 법인/회사정보(COA08) |
| 4 | 거래처관리(COA05) · 사용자(모바일)(COB06) · 문서양식관리(COG10) · 시스템접속로그(COZ80) |
| 3 | 공통코드(COA01) · 국가코드관리(COA07) · 전자메일·SMS-Template(COG03) · 시스템접근통제(COZ81) · DB객체관리(COZ9001) |
| 2 | 부서/현장정보(COA03) · 통화코드관리(COA06) · 환율정보관리(COA09) · 부서별겸직정보(COB00) · (권한현황) 권한그룹-사용자(COD10) · (권한현황) 사용자-권한그룹(COD20) · 결재로그(COZ07) |
| 1 | 전자서명검증(COG20) · 인증서유효기간(COG30) · 도움말(COZ05) · 에러로그(COZ09) · 세션모니터링(COZ82) |
| 0 | 휴일관리(COA20) · 은행정보관리(COA11) · 공지사항(COC01) · 시스템승인-Sample(COG01) · 시스템승인-Dialog(COG02) |

## 6. 업무 군집 (API 공유 + 화면 역할을 함께 본 해석)

| 군집 | 화면 | 연결 근거 |
|---|---|---|
| 권한 설정 체인 | 권한그룹 `COB01` · 역할그룹 `COB02` · 메뉴권한 `COB05` · 직무연동 `COB08` · 사용자 `COB03` · 대시보드 `COB07` | `role-manage` · `biz-group` · `user-scope` 공유(대시보드 `COB07` 은 `dept` · `role` 만 겹침). 권한그룹을 만들고 → 역할·메뉴·직무에 붙이고 → 사용자에게 부여하는 순서와 일치 |
| 권한 조회·이력 | `COD30` · `COD10` · `COD20` · `COD40` · `COD50` · `COD60` | `COD30` 은 설정 체인 전체를 읽는 허브. `COD40` · `COD50` · `COD60` 은 `role` · `user` · `service-manage` 를 읽고 각자 `log-*` 를 씀. `COD10` · `COD20` 은 `auth` 만 쓰는 작은 조회 |
| 조직·사용자 기준정보 | 사용자정보 `COA02` · 부서/현장정보 `COA03` · 법인/회사정보 `COA08` · 거래처관리 `COA05` · 사용자겸직정보 `COA04` · 부서별겸직정보 `COB00` | `company` · `dept` · `employee-dept` · `user` 공유. `COA04` 가 이 군집과 권한 체인을 잇는 다리 |
| 통화·국가 코드 | 국가코드관리 `COA07` · 통화코드관리 `COA06` · 환율정보관리 `COA09` · 법인/회사정보 `COA08` | `currency`(COA06 · COA07 · COA09) · `country`(COA07 · COA08) |
| 메뉴·프로그램 | 메뉴정보 `COZ04` · 메뉴권한 `COB05` | `service-manage` · `program` · `biz-group` 공유. 메뉴 구조가 바뀌면 메뉴권한 화면이 영향받는다 |
| 문서·첨부 | 문서양식관리 `COG10` · 도움말 `COZ05` | `attached-file` 공유 |
| 전자서명 | 전자서명검증 `COG20` · 인증서유효기간 `COG30` | `sign` 공유 |
| 로그·모니터링 | 에러로그 `COZ09` · 시스템접속로그 `COZ80` · 세션모니터링 `COZ82` | `syslog`(COZ09 · COZ80) · `usr`(COZ82 · COB00). 연결은 약하다 |
| 독립 | 휴일관리 `COA20` · 은행정보관리 `COA11` · 공지사항 `COC01` · `COG01` · `COG02` | 공유 API 없음(`COG01`→`COG02` 는 §3 의 팝업 관계뿐) |

## 7. 확인이 필요한 점

1. **옛 서비스 ID 로 여는 팝업 2건**: `co_custDelInfo`(tradeInfo.js:369) · `co_chgAuthGrpInfo`(authGrpInfo.js · authUsrInfo.js). 75 SQL 의 별칭(`serviceAlias.*`, 43종)에는 `co_noticeList` · `co_noticeView` · `co_aprvLog` 등은 있으나 이 두 ID 는 없고, ⑧ 끄기 목록에서도 찾지 못했다(SQL 안에 두 ID 가 0건). 현재 DB 에서 유효한 service 인지 확인이 필요하다.
2. **소스 파일이 없는 화면 3개**: `COA08` · `COB07` · `COG02`. 71 문서의 목표 page(`/co/corpMng` · `/co/dashbrdAuth` · `/co/sysAprv`)에 해당하는 파일이 erp-h 에 없다. 앞의 둘은 hmp 원본 화면(program 경로 `organization/company-group` · `dashboard/home-service-rule`)을 쓰는 것으로 보이나 확인하지 못했다. `COG02` 는 `pageMissing=true` 다.
3. **`COA0301 HR정보동기화`의 진입 경로**를 코드에서 찾지 못했다(§3).
4. **ID 체계가 바뀌었다**: 11/20 조회(`ca-code` 등)와 이번 조회(`COA01` 등)의 serviceId 가 다르다. 이전 문서를 근거로 만든 매핑이나 권한 설정이 있으면 새 ID 로 다시 맞춰야 한다.

## 8. 화면별 관계 요약

| 화면 | serviceId | 유형 | 여는 화면 | 사용 API 서비스(경로 수) | 연결 화면 수 | TO-BE 대응(hmp 원본) |
|---|---|---|---|---|---:|---|
| 공통코드 | `COA01` | B | — | code-group-manage(11) code-i18n(1) code-manage(1) code-request(1) org-unit-manage(1) | 3 | ca-code / code(code/code) |
| 사용자정보 | `COA02` | B | — | code(1) company(1) dept(1) employee-dept(2) employee-user(4) employee(3) | 8 | ca-employee / employee + ca-hmp-user-manage / hmp-user-manage + ca-user-company / user-com… |
| 부서/현장정보 | `COA03` | A | — | coz(1) dpt(4) | 2 | co_deptMng·ca-dept / dept(co/deptMng) + ca-org-unit / org-unit |
| 법인/회사정보 | `COA08` | C | — | company-group(5) company(1) country(1) | 5 | ca-company-group / company-group + ca-company / company + ca-business-place / business-pla… |
| 거래처관리 | `COA05` | B | `co_custDelInfo` `COA0501` | code(1) company(1) partner(3) | 4 | ca-partner / partner |
| 휴일관리 | `COA20` | B | — | holiday(7) | 0 | ca-holiday / holiday(common/holiday) |
| 국가코드관리 | `COA07` | B | — | country(6) currency(1) | 3 | ca-country / country(common/country) |
| 통화코드관리 | `COA06` | B | — | currency(4) | 2 | ca-currency / currency(common/currency) |
| 환율정보관리 | `COA09` | B | — | currency(1) exchange-rate(3) | 2 | ca-exchange-rate / exchange-rate(common/exchange-rate) |
| 은행정보관리 | `COA11` | B | — | bank(4) | 0 | ca-bank / bank(common/bank) |
| 권한그룹 | `COB01` | B | — | biz-group(1) role-manage(4) system(1) user-scope(1) | 7 | ca-role / role + ca-role-common / role-common + ca-role-rule / role-rule(권한 정의부) |
| 역할그룹 | `COB02` | B | — | biz-group(1) role-manage(6) user-scope(2) user(1) | 9 | ca-role-assign / role-assign |
| 메뉴권한 | `COB05` | B | — | biz-group(1) program(1) role-manage(1) role-service(4) service-manage(1) user-scope(2) | 9 | ca-role-service / role-service |
| 직무연동 | `COB08` | B | — | biz-group(1) dept(1) org-unit-manage(2) role-manage(6) user-scope(1) | 10 | ca-role-rule / role-rule + ca-role-condition / role-condition |
| 사용자겸직정보 | `COA04` | B | — | company(1) org-assignment(9) org-unit-manage(1) user-scope(1) user(1) | 12 | co_deptCrent / co_deptCrent(co/deptCrent · erp-h 이식본 보정) |
| 부서별겸직정보 | `COB00` | A | — | employee-dept(1) usr(3) | 2 | co_deptCriteriaCrent / co_deptCriteriaCrent(co/deptCriteriaCrent · erp-h 이식본 보정) |
| 대시보드 | `COB07` | B | — | dept(1) home-service(4) role(1) | 7 | ca-home-service-rule / home-service-rule |
| 사용자 | `COB03` | B | — | biz-group(1) dept(1) role-manage(5) user-scope(1) user(1) | 11 | ca-user-role·ca-user-role-by-user / user-role |
| 사용자(모바일) | `COB06` | A | — | auth(2) coz(1) | 4 | co_mobileAuthMapp / co_mobileAuthMapp |
| (권한현황) 권한그룹-메뉴 | `COD30` | B | `co_chgAuthGrpInfo` | biz-group(1) company(1) dept(1) org-unit-manage(1) role-manage(4) role-service(1) service-manage(1) user-scope(2) user(1) | 15 | ca-user-role-inquiry / user-role-inquiry(권한그룹-메뉴 보기) |
| (권한현황) 권한그룹-사용자 | `COD10` | A | `co_chgAuthGrpInfo` | auth(1) | 2 | 신규(이식본 COD10 을 HMP 데이터로 연결) |
| (권한현황) 사용자-권한그룹 | `COD20` | A | — | auth(1) | 2 | 신규(이식본 COD20 을 HMP 데이터로 연결) |
| (변경이력) 사용자 | `COD40` | B | — | log-role-grant(2) role(1) user(1) | 8 | ca-log-role-grant / log-role-grant |
| (변경이력) 권한그룹 | `COD50` | B | — | log-role-service(3) role(1) service-manage(1) | 7 | ca-log-role-service / log-role-service |
| (접속이력) 개인정보 | `COD60` | B | — | log-privacy(2) service-manage(1) user(1) | 8 | ca-log-privacy / log-privacy |
| 공지사항 | `COC01` | A | `COC0102` `COC0101` | co_notice(1) | 0 | co_noticeList / co_noticeList |
| 시스템승인-Sample | `COG01` | C | `COG02` | — | 0 | 신규(HMP 결재 엔진 호출 시험 화면) |
| 시스템승인-Dialog | `COG02` | C | — | — | 0 | COG02 / co_sysAprvPop(co/sysAprvPop · erp-h 신규 · 52-커뮤니티 §5) |
| 전자메일·SMS-Template | `COG03` | B | — | biz-code(1) mail(2) | 3 | co_mailSmsfmt / co/mailSmsfmt(erp-h 이식본) |
| 문서양식관리 | `COG10` | A | — | attached-file(10) biz-code(1) elcDoc(5) | 4 | co_elcDoc / co_elcDoc |
| 전자서명검증 | `COG20` | A | — | sign(4) | 1 | COG20 / COG20(co/signValidation) |
| 인증서유효기간 | `COG30` | A | — | sign(1) | 1 | COG30 / COG30(co/signValidityCheck) |
| 도움말 | `COZ05` | A | — | attached-file(9) mnu(3) | 1 | co_mnuHelpMng / co_mnuHelpMng |
| 메뉴정보 | `COZ04` | B | — | biz-group(1) program(1) service-manage(3) | 8 | ca-service / service + ca-program / program + ca-biz-group / biz-group + service_api_usage… |
| 에러로그 | `COZ09` | B | — | syslog(1) | 1 | co_errorLog / co/errorLog(erp-h 이식본) |
| 결재로그 | `COZ07` | A | `COZ0701` | aproval(2) coz(1) | 2 | co_aprvLog / co_aprvLog |
| 시스템접속로그 | `COZ80` | B | — | biz-code(1) syslog(1) | 4 | co_sysCnct / co/sysCnct(erp-h 이식본 · log_login·log_page 병합) |
| 시스템접근통제 | `COZ81` | C | — | allowed-ip(3) role(1) | 3 | ca-allowed-ip / allowed-ip |
| 세션모니터링 | `COZ82` | B | — | user-session(1) usr(1) | 1 | co_userSessionMonitor / co/userSessionMonitor(erp-h 이식본) |
| DB객체관리 | `COZ9001` | A | — | biz-code(1) dbobject(2) | 3 | co_dbObject / co_dbObject |
