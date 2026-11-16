# erp-h role 비교 — 저장소(시드 SQL·계획표·문서) 기준

> 작성 2026-11-16 · 대상: 요청한 role 9개 + 같은 시드에 정의된 나머지 role 11개
> **DB(sql-h)는 직접 읽지 않았다.** DB 접속은 권한 거부되어 하지 않았다. 아래는 저장소에 있는 정의·계획·패치를 읽은 것이다. 실제 DB 값과 다를 수 있다(§8 참조).
> 근거 파일: `db/common-admin/P3-04-roles.sql`(role 정의) · `P3-plan-role-service.tsv`(role↔서비스 부여 계획 1,357행 · 생성기 `gen_p3.py`) · 후속 패치 `P3-27`·`P3-28`·`P3-29`·`P16`·`P17`·`P28` · 설계서 `S6-role-codes.md` · 직접 읽은 메뉴(Aside 7계정)와의 대조
> hmp-core 소스(`../hmp`)는 이 작업폴더에 없어 읽지 못했다.

## 1. role 정의 (P3-04 시드 · 20개)

| role_id | 이름 | 유형 | 업무관리자 | 시스템관리자 | 개인정보 보기 | 대상 | 적용 사용자 유형 | 사용 | 설명(시드) |
|---|---|---|:-:|:-:|:-:|---|---|:-:|---|
| **SYS_ADMIN** | 시스템관리자 | ASSIGN | N | Y | N | EMPLOYEE | - | Y | 「시스템」 전체 · 업무 화면 없음(R-4) · 터미널·PBE 복호화 전용(S-7) |
| **DEVELOPER** | 개발자 | ASSIGN | N | N | N | EMPLOYEE | - | Y | S6-role-codes 개발자 코드를 hmp 표준 DEVELOPER 로(코드 4곳이 이름으로 참조 · P2-F §4) · 「시스템 › 개발도구」 전체 + 그 밖 조회(R-3) |
| **BIZ_ADMIN** | 업무 전체 관리자 | ASSIGN | Y | N | N | EMPLOYEE | - | Y | 「공통」 전체(A-1·A-7) · 요청 수신(R-5) |
| BIZ_MNG_01 | 공통 담당(인사·조직) | ASSIGN | N | N | N | EMPLOYEE | - | Y | 「공통 › 사용자·조직」(R-2) |
| BIZ_MNG_02 | 공통 담당(기준정보) | ASSIGN | N | N | N | EMPLOYEE | - | Y | 「공통 › 기준정보」(R-2) |
| **CS_ADMIN** | 하자 업무관리자 | ASSIGN | Y | N | N | EMPLOYEE | - | Y | cs 전체 ALL:3(R-5) |
| **CS_APRV_01** | 하자 비용 승인 | ASSIGN | N | N | N | EMPLOYEE | - | Y | cs 비용 승인 COMPANY:3 · CONFIG approval.cost-approver |
| **CS_MNG_01** | 하자 주관(품질관리팀) | ASSIGN | N | N | N | EMPLOYEE | - | Y | cs 본사 주관 COMPANY(D-6) |
| **CS_MNG_02** | 하자 지원(건축·토목 지원팀) | ASSIGN | N | N | N | EMPLOYEE | - | Y | CS003 범위 COMPANY(D-6) |
| **CS_SITE_01** | 하자 현장담당 | ASSIGN | N | N | N | EMPLOYEE | - | Y | cs 현장 SELF(D-7) |
| CS_VIEW_01 | 하자 조회 | ASSIGN | N | N | N | EMPLOYEE | - | Y | cs 조회 COMPANY:1 |
| CS_PTNR_01 | 하자 협력업체 | ASSIGN | N | N | N | PARTNER_USER | - | Y | cs 협력사 SELF · 담당현장 필수(D-7) |
| **CS_COMMON** | 하자 공통 | ASSIGN | N | N | N | EMPLOYEE | - | Y | cs 공용 팝업·파인더 앵커 · 화면 0 · SELF:1(R-6 · P2-C 켜면 폐지) |
| SYS_COMMON | 공통(전원·플랫폼) | COMMON | N | N | N | EMPLOYEE | - | Y | 개인 서비스·헤더 기능·플랫폼 필수 API · 업무 화면 0(R-1) |
| BIZ_COMMON | 공통(전원·업무) | COMMON | N | N | N | EMPLOYEE | INTERNAL | Y | 업무 공통 조회·권한 신청(A-2)·코드 요청(C-2) · 내부 직원만(협력업체 제외) |
| PTNR_COMMON | 공통(협력업체) | COMMON | N | N | N | PARTNER_USER | PARTNER | Y | 권한 신청(A-2) — 협력업체만 |
| HQ_COMMON | 본사 공통 | RULE | N | N | N | EMPLOYEE | - | Y | 발령 COMPANY=HEAD_OFFICE — 본사 부서 소속마다 자동 부여(AS-IS 10010) |
| SITE_COMMON | 현장 공통 | RULE | N | N | N | EMPLOYEE | - | Y | 발령 COMPANY=SITE — 현장(org-unit PROJECT) 배치마다 자동 부여 · 종료일 = 배치 종료일(협력업체 투입 기간 · AS-IS 10020) |
| MOBILE_USER | (P3-04 에서 정의하지 않음 · 기존 행 유지) | | | | | | | | 시드가 그대로 두는 역할 |
| MCP_SERVICE | (P3-04 에서 정의하지 않음 · 기존 행 유지) | | | | | | | | 시드가 그대로 두는 역할 |

- 굵게 = 요청한 9개. 유형: ASSIGN(직접 부여) · COMMON(전원 자동) · RULE(발령 조건 자동).
- 업무관리자=Y: `BIZ_ADMIN`·`CS_ADMIN`(요청 수신 지정). 시스템관리자=Y: `SYS_ADMIN`.
- `CS_COMMON` 은 유형이 ASSIGN(COMMON 아님)이라 자동으로 붙지 않는다 — cs 역할 보유자에게 함께 주는 역할.

## 2. role ↔ 서비스 부여 규모 (계획표 기준)

「화면」= 메뉴에 나오는 서비스, 「API 앵커」= 화면이 부르는 API 첫 마디. 등급 1 조회 · 2 저장 · 3 삭제·특수. 범위: ALL · COMPANY(법인) · SELF.

| role | 화면 수 | 화면 등급 1 / 2 / 3 | 화면 범위 | API 앵커 수 | 앵커 등급 1 / 2 / 3 | 등록 대기(PENDING) |
|---|---:|---|---|---:|---|---:|
| **SYS_ADMIN** | 145 | 1 / 2 / 142 | ALL 145 | 209 | 3 / 1 / 205 | 139 |
| **DEVELOPER** | 202 | 168 / 0 / 34 | ALL 202 | 210 | 177 / 1 / 32 | 155 |
| **BIZ_ADMIN** | 63 | 5 / 0 / 58 | ALL 63 | 90 | 3 / 3 / 84 | 57 |
| BIZ_MNG_01 | 9 | 1 / 8 / 0 | COMPANY 9 | 34 | 6 / 28 / 0 | 4 |
| BIZ_MNG_02 | 8 | 0 / 8 / 0 | COMPANY 8 | 10 | 0 / 10 / 0 | 1 |
| **CS_ADMIN** | 61 | 1 / 0 / 60 | ALL 61 | 11 | 1 / 0 / 10 | 0 |
| **CS_APRV_01** | 3 | 2 / 0 / 1 | COMPANY 3 | 10 | 0 / 0 / 10 | 0 |
| **CS_MNG_01** | 59 | 15 / 43 / 1 | COMPANY 59 | 11 | 1 / 0 / 10 | 0 |
| **CS_MNG_02** | 22 | 4 / 18 / 0 | COMPANY 22 | 9 | 1 / 8 / 0 | 0 |
| **CS_SITE_01** | 33 | 20 / 13 / 0 | SELF 33 | 10 | 0 / 10 / 0 | 0 |
| CS_VIEW_01 | 27 | 27 / 0 / 0 | COMPANY 27 | 11 | 11 / 0 / 0 | 0 |
| CS_PTNR_01 | 14 | 9 / 5 / 0 | SELF 14 | 10 | 0 / 10 / 0 | 0 |
| **CS_COMMON** | 0 | 0 / 0 / 0 | - | 4 | 4 / 0 / 0 | 0 |
| SYS_COMMON | 27 | 12 / 13 / 2 | SELF 27 | 33 | 32 / 1 / 0 | 22 |
| BIZ_COMMON | 7 | 6 / 1 / 0 | SELF 7 | 7 | 7 / 0 / 0 | 4 |
| PTNR_COMMON | 1 | 0 / 1 / 0 | SELF 1 | 0 | 0 / 0 / 0 | 0 |
| HQ_COMMON | 0 | 0 / 0 / 0 | - | 3 | 3 / 0 / 0 | 0 |
| SITE_COMMON | 0 | 0 / 0 / 0 | - | 4 | 4 / 0 / 0 | 0 |

- 「등록 대기」는 계획표를 만들 때 서비스 행이 아직 없던 것으로, 서비스가 등록된 뒤 부여된다.
- `CS_ADMIN` 범위가 ALL 이고 나머지 cs 역할이 COMPANY(법인)/SELF(현장)인 것이 cs 역할 구분의 핵심이다. 계획표는 COMPANY 를 `data_scope=SELF` + `scope_structure_type=COMPANY` 로 적는다.

## 3. 요청한 9개 role 비교

### 3-1. 화면 집합 겹침 (계획표 화면 기준)

행 role 의 화면 중 열 role 에도 있는 화면 수(대각선 = 그 role 의 화면 수).

| 행 \ 열 | BIZ_ADMIN | CS_ADMIN | CS_APRV_01 | CS_COMMON | CS_MNG_01 | CS_MNG_02 | CS_SITE_01 | DEVELOPER | SYS_ADMIN |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| BIZ_ADMIN | 63 | 0 | 0 | 0 | 0 | 0 | 0 | 63 | 5 |
| CS_ADMIN | 0 | 61 | 3 | 0 | 59 | 22 | 33 | 0 | 0 |
| CS_APRV_01 | 0 | 3 | 3 | 0 | 3 | 1 | 2 | 0 | 0 |
| CS_COMMON | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| CS_MNG_01 | 0 | 59 | 3 | 0 | 59 | 22 | 33 | 0 | 0 |
| CS_MNG_02 | 0 | 22 | 1 | 0 | 22 | 22 | 16 | 0 | 0 |
| CS_SITE_01 | 0 | 33 | 2 | 0 | 33 | 16 | 33 | 0 | 0 |
| DEVELOPER | 63 | 0 | 0 | 0 | 0 | 0 | 0 | 202 | 144 |
| SYS_ADMIN | 5 | 0 | 0 | 0 | 0 | 0 | 0 | 144 | 145 |

### 3-2. 포함 관계와 쌍별 차이

- BIZ_ADMIN ⊂ DEVELOPER (BIZ_ADMIN 63개 · DEVELOPER 202개)
- CS_ADMIN ⊃ CS_APRV_01 (CS_ADMIN 61개 · CS_APRV_01 3개)
- CS_ADMIN ⊃ CS_MNG_01 (CS_ADMIN 61개 · CS_MNG_01 59개)
- CS_ADMIN ⊃ CS_MNG_02 (CS_ADMIN 61개 · CS_MNG_02 22개)
- CS_ADMIN ⊃ CS_SITE_01 (CS_ADMIN 61개 · CS_SITE_01 33개)
- CS_APRV_01 ⊂ CS_MNG_01 (CS_APRV_01 3개 · CS_MNG_01 59개)
- CS_MNG_01 ⊃ CS_MNG_02 (CS_MNG_01 59개 · CS_MNG_02 22개)
- CS_MNG_01 ⊃ CS_SITE_01 (CS_MNG_01 59개 · CS_SITE_01 33개)

- **CS_ADMIN ↔ CS_MNG_01**: 공통 59 · CS_ADMIN에만 2 · CS_MNG_01에만 0 · 공통 중 등급이 다른 화면 57
- **CS_MNG_01 ↔ CS_MNG_02**: 공통 22 · CS_MNG_01에만 37 · CS_MNG_02에만 0 · 공통 중 등급이 다른 화면 2
- **CS_MNG_01 ↔ CS_SITE_01**: 공통 33 · CS_MNG_01에만 26 · CS_SITE_01에만 0 · 공통 중 등급이 다른 화면 15
- **CS_MNG_02 ↔ CS_SITE_01**: 공통 16 · CS_MNG_02에만 6 · CS_SITE_01에만 17 · 공통 중 등급이 다른 화면 5
- **CS_ADMIN ↔ CS_APRV_01**: 공통 3 · CS_ADMIN에만 58 · CS_APRV_01에만 0 · 공통 중 등급이 다른 화면 2
- **SYS_ADMIN ↔ DEVELOPER**: 공통 144 · SYS_ADMIN에만 1 · DEVELOPER에만 58 · 공통 중 등급이 다른 화면 109
- **SYS_ADMIN ↔ BIZ_ADMIN**: 공통 5 · SYS_ADMIN에만 140 · BIZ_ADMIN에만 58 · 공통 중 등급이 다른 화면 5
- **DEVELOPER ↔ BIZ_ADMIN**: 공통 63 · DEVELOPER에만 139 · BIZ_ADMIN에만 0 · 공통 중 등급이 다른 화면 58

### 3-3. 하자 화면별 등급 — cs 역할 비교 (계획표)

`·` = 계획표에 부여 없음. 범위는 CS_ADMIN=ALL · CS_MNG_01/02·CS_APRV_01=법인 · CS_SITE_01=현장.

| 메뉴 경로 | serviceId | CS_ADMIN | CS_APRV_01 | CS_MNG_01 | CS_MNG_02 | CS_SITE_01 |
|---|---|:-:|:-:|:-:|:-:|:-:|
| 하자 등록 및 처리 › 현황조회 › **접수 건 추적** | `cs_acpTrack` | · | · | · | · | · |
| 하자보수비 › **연도별예산관리** | `cs_asByyyBgt` | 3 | · | 1 | 2 | · |
| 하자보수비 › **비율등록** | `cs_asByyyBgtCd` | 3 | · | 1 | · | · |
| 하자보수비 › **집행실적관리** | `cs_asSiteCs` | 3 | · | 1 | · | · |
| 하자보수비 › 현황조회 › **전표조회** | `cs_asSlpCst` | 3 | · | 1 | 1 | · |
| 하자 등록 및 처리 › 공통 › **기준정보 등록 승인** | `cs_baseCdAprv` | 3 | · | · | · | · |
| 하자 등록 및 처리 › 공통 › **기준정보 등록 요청** | `cs_baseCdReq` | 3 | · | 2 | · | 2 |
| 하자 등록 및 처리 › 기초자료 › **공종별협력업체** | `cs_bctyCopCo` | 3 | · | 2 | 2 | 1 |
| 하자 등록 및 처리 › 현황조회 › **협력업체공종별현황** | `cs_byCopCoCtpCurst` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 현황조회 › **협력업체별현황** | `cs_byCopCoCurst` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 현황조회 › **현장별 AS접수 통계** | `cs_bySiteAcpStats` | 3 | · | 2 | · | 1 |
| 하자 등록 및 처리 › 현황조회 › **유형별현황** | `cs_byTpCurst` | 3 | · | 2 | · | · |
| 하자보수비 › 현황조회 › **월별 하자보수비 현황** | `cs_bymAsExpIptCurst` | 3 | · | 1 | · | · |
| 하자 등록 및 처리 › 현황조회 › **확인점검 결과 대시보드** | `cs_chkDashboard` | 3 | · | 2 | · | 1 |
| 하자 등록 및 처리 › 점검내역대량등록 › **점검내역대량등록** | `cs_chkDtlsBlkReg` | 3 | · | 2 | 2 | 2 |
| 하자 등록 및 처리 › 기초자료 › **점검일정** | `cs_chkSch` | 3 | · | 2 | 2 | 2 |
| 하자 등록 및 처리 › 공통 › **담당현장** | `cs_chrSite` | 3 | · | 2 | 2 | 1 |
| 하자 등록 및 처리 › 현황조회 › **업체별 처리현황** | `cs_cooDiscomCurst` | 3 | · | 2 | · | · |
| 기본정보 › **협력업체정보** | `cs_copCoInfo` | 3 | · | 2 | 2 | 1 |
| 하자 등록 및 처리 › 현황조회 › **협력사 이행 현황** | `cs_copCoPerf` | · | · | · | · | · |
| 하자보수비 › **비용 집행** | `cs_costClaim` | 3 | 3 | 2 | · | · |
| 하자 등록 및 처리 › AS관리 › **A/S관리** | `cs_csChkMng` | 3 | · | 2 | 2 | 2 |
| 하자 등록 및 처리 › 입주자사전점검 › **사전점검** | `cs_csChkMng_bf` | 3 | · | 2 | 2 | 2 |
| 하자 등록 및 처리 › 준공전품질점검 › **주부품질점검** | `cs_csChkMng_hwf` | · | · | · | · | · |
| 하자 등록 및 처리 › 준공전품질점검 › **품질점검** | `cs_csChkMng_qlt` | 3 | · | 2 | 2 | 2 |
| 하자 등록 및 처리 › AS관리 › **하자 문자발송** | `cs_csSendSms` | 3 | · | 2 | · | 2 |
| 하자 등록 및 처리 › 공통 › **공종코드** | `cs_ctpCd` | 3 | · | 1 | · | 1 |
| 하자 등록 및 처리 › 현황조회 › **공종별 처리현황** | `cs_ctpDiscomCurst` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 기초자료 › **하자분류 매칭** | `cs_ctpMatch` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 현황조회 › **센터장 일일보고** | `cs_ctrDailyRpt` | · | · | · | · | · |
| 하자 등록 및 처리 › 현황조회 › **하자 접수 및 처리대장** | `cs_dealAcp` | 3 | · | 2 | · | 1 |
| 하자처리보고 › **하자처리보고서** | `cs_dealRep` | 3 | · | 2 | · | 1 |
| 하자 등록 및 처리 › 현황조회 › **하자 통합 분석** | `cs_defectAnalysis` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 현황조회 › **하자처리현황** | `cs_disComCurst` | 3 | · | 2 | · | 1 |
| 하자 등록 및 처리 › AS관리 › **분쟁 단지 현황** | `cs_dsptMng` | 3 | · | 2 | 2 | · |
| 하자 등록 및 처리 › 현황조회 › **재무 연계 하자 분석** | `cs_finDefect` | · | · | · | · | · |
| 하자보수비 › 현황조회 › **일반관리비 실적** | `cs_generlCst` | 3 | · | 1 | · | · |
| 하자보수비 › **보증금 청구 관리** | `cs_gntClm` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 기초자료 › **세대정보** | `cs_hhldInfo` | 3 | · | 2 | 2 | 2 |
|  › **협력업체 공지사항** | `cs_jcAnncBrdLst` | 3 | · | 2 | · | · |
| 하자보수비 › 하자전표관리 › **공동도급안분** | `cs_jonitVenture` | 3 | · | · | · | · |
| 하자 등록 및 처리 › 현황조회 › **중점 관리 단지** | `cs_keySite` | · | · | · | · | · |
| 하자 등록 및 처리 › 현황조회 › **장기 미처리 하자 관리** | `cs_ltrmDefect` | 3 | · | 2 | · | 1 |
| 하자 등록 및 처리 › 공통 › **모바일사용자** | `cs_mobileUsr` | 3 | · | 2 | 2 | 1 |
| 하자 등록 및 처리 › 공통 › **엔지니어 승인관리** | `cs_mobileUsrAprv` | 3 | · | 2 | · | · |
| 하자 등록 및 처리 › 입주자사전점검 › **입주 지원** | `cs_mvinMng` | · | · | · | · | · |
|  › **발주 내역** | `cs_ordDtls` | 3 | 1 | 2 | · | 1 |
| 일정관리 › **일정관리** | `cs_schMng` | 3 | · | 2 | 2 | 2 |
| 하자 등록 및 처리 › 공통 › **현장별 공간 부위 코드** | `cs_sitSpacePartCd` | 3 | · | 2 | · | 2 |
| 기본정보 › **현장정보** | `cs_siteInfo` | 3 | · | 2 | 2 | 1 |
| 하자보수비 › 현황조회 › **자금집계표** | `cs_siteMoneyCst` | 3 | · | 1 | · | · |
| 하자 등록 및 처리 › 현황조회 › **단지별 조직도** | `cs_siteOrg` | · | · | · | · | · |
| 하자 등록 및 처리 › 인수인계 › **현장인수인계승인** | `cs_siteTnhAprv` | 3 | · | 3 | 2 | · |
| 하자 등록 및 처리 › 인수인계 › **현장인수인계요청** | `cs_siteTnhReq` | 3 | · | 2 | 2 | 2 |
| 하자보수비 › 현황조회 › **현장종합 투입금액** | `cs_siteTotCst` | 3 | · | 1 | · | · |
| 하자 등록 및 처리 › 현황조회 › **공간/부위별 접수처리 현황** | `cs_spaceCurst` | 3 | · | 2 | · | 1 |
| 하자보수비 › 현황조회 › **보증현황** | `cs_sponSionCst` | 3 | · | 1 | · | · |
| 하자 등록 및 처리 › 인수인계 › **표준인수인계파일등록** | `cs_tnhFile` | 3 | · | 2 | · | 1 |
| 하자 등록 및 처리 › 입주자사전점검 › **사전방문 예약** | `cs_visitRsv` | 3 | · | 2 | · | 2 |
| 하자 등록 및 처리 › AS관리 › **작업관리** | `cs_wrkCmplMng` | 3 | · | 2 | 2 | 2 |
|  › **하자** | `db_dashBoardCS` | 1 | · | 1 | 1 | · |

### 3-4. 시스템·공통 role 비교 — 메뉴 영역별 (계획표)

| 영역 | 화면 수 | SYS_ADMIN | DEVELOPER | BIZ_ADMIN |
|---|---:|---|---|---|
| 공통 › 게시·알림 | 7 | · | 7개 (1:7) | 7개 (3:7) |
| 공통 › 결재·문서 | 11 | · | 11개 (1:11) | 11개 (3:11) |
| 공통 › 권한 | 9 | · | 8개 (1:8) | 8개 (3:8) |
| 공통 › 기준정보 | 8 | · | 8개 (1:8) | 8개 (3:8) |
| 공통 › 메뉴·업무그룹 | 4 | · | 4개 (1:4) | 4개 (3:4) |
| 공통 › 조직·사용자 | 10 | 1개 (3:1) | 10개 (1:10) | 9개 (3:9) |
| 시스템 › AI | 11 | 11개 (3:11) | 11개 (1:11) | · |
| 시스템 › 개발도구 | 32 | 27개 (3:27) | 27개 (3:27) | · |
| 시스템 › 게시·알림 | 7 | · | · | · |
| 시스템 › 결재·문서 | 11 | · | · | · |
| 시스템 › 권한 | 9 | · | · | · |
| 시스템 › 기준정보 | 8 | · | · | · |
| 시스템 › 메뉴·업무그룹 | 5 | 1개 (3:1) | 1개 (1:1) | · |
| 시스템 › 문서 설정 | 5 | 5개 (3:5) | 5개 (1:5) | · |
| 시스템 › 보안 | 21 | 19개 (3:19) | 21개 (1:21) | 2개 (3:2) |
| 시스템 › 연동 | 15 | 15개 (3:15) | 15개 (1:15) | · |
| 시스템 › 운영·로그 | 22 | 22개 (3:22) | 22개 (1:22) | · |
| 시스템 › 조직·사용자 | 10 | · | · | · |
| 시스템 › 플랫폼 | 16 | 16개 (3:16) | 16개 (1:16) | · |

`n개 (1:a 2:b 3:c)` = 그 영역 화면 중 계획표에 부여된 것이 n개이고 등급 1·2·3 이 각각 a·b·c 개. 같은 메뉴가 「공통」과 「시스템」 탭에 따로 있어 탭별로 나눴다.
「시스템」 탭의 조직·사용자·권한·기준정보·결재·문서·게시·알림 영역이 `·` 인 것은 이 role 들에 부여가 없어서가 아니라, 그 `sys-*` 메뉴가 계획표(P3, 11-09~10) 이후에 추가되어 계획표에 없기 때문이다. 실제 메뉴에서는 erpadmin 이 그 172개 전부 등급 3 으로 본다(`01_계정별메뉴/erph-menu-erpadmin.md`). 11-11 이후 메뉴 재편(P3-13·P3-19·P5-menu-system-full)이 만든 부여는 이 비교에 포함되지 않았다.

## 4. 9개 role 요약

| role | 요약 |
|---|---|
| **BIZ_ADMIN** | 「공통」 전체 3 · 요청 수신(is_biz_admin). P3-28 이 BIZ_ADMIN 의 불필요한 API 첫 마디 51개를 회수(162→111). |
| **CS_ADMIN** | 하자 전체 ALL:3 · 업무관리자(요청 수신). |
| **CS_APRV_01** | 하자 비용 승인 3화면 COMPANY:3 · CONFIG `approval.cost-approver` 가 이 role 을 가리킨다. |
| **CS_COMMON** | 화면 0 · API 앵커 4개(등급 1 SELF) — cs 공용 팝업·파인더용. cs 역할 보유자에게 함께 부여. |
| **CS_MNG_01** | 하자 주관(품질관리팀) — 하자 59화면 · COMPANY · 저장 2 중심. |
| **CS_MNG_02** | 하자 지원(건축·토목 지원팀) — CS003 범위 22화면 · COMPANY · 저장 2. 하자보수비·전표 범위는 폐기(D-6). |
| **CS_SITE_01** | 하자 현장담당 — 33화면 · SELF(현장) · user_role 로 받은 현장 ∪ 담당현장만(D-7). |
| **DEVELOPER** | 개발도구 전체 3 + 그 밖 모든 메뉴 조회 1. 코드 등록 요청(`ca-code-request-form`)은 DEVELOPER 3(P3-29). 설계서의 SYS_DEV 를 hmp 표준 이름 DEVELOPER 로 바꿈(코드 4곳이 이름으로 참조). |
| **SYS_ADMIN** | 「시스템」 전체 · 업무 화면 없음 · 터미널·PBE 복호화는 이 role 전용(S-7). 이후 P3-27 로 SYS_ADMIN 보유자에게 BIZ_ADMIN 도 부여(erpadmin 이 공통도 보는 이유). |

## 5. 계획표 이후에 바뀐 것 (패치)

| 패치 | 날짜 | 영향 role | 내용 |
|---|---|---|---|
| P3-27 | 2026-11-11 | SYS_ADMIN · BIZ_ADMIN | SYS_ADMIN 보유자에게 BIZ_ADMIN 도 부여(user_role). 초안 U-2 「erpadmin = SYS_ADMIN 만」을 대체 |
| P3-28 | 2026-11-11 | BIZ_ADMIN · SYS_COMMON | 불필요한 API 첫 마디 부여 회수(BIZ_ADMIN 51 · SYS_COMMON 1) |
| P3-29 | 2026-11-12 | DEVELOPER · BIZ_COMMON | 코드 등록 요청을 설계자·개발자(DEVELOPER 3) 전용으로. 개인 메뉴에서 빠짐(BIZ_COMMON 2 SELF 회수) |
| P16 | 2026-11-11 | 전체 | 호출 화면 기준 API 판정 켬 — 화면 부여가 있으면 그 화면이 부르는 API 도 허용으로 봄 |
| P17 | 2026-11-16 | 전체 | 표시 없는 API 의 role_service 부여 검사를 LOG → **ENFORCE**(부여 없으면 거부) |
| P28 | 2026-11-16 | SYS_ADMIN | 대행 로그인(impersonate) 가능 role 을 SYS_ADMIN 으로 명시 |

## 6. 직접 읽은 메뉴(Aside 7계정)와 계획표 대조

각 계정의 role 을 문서 기준으로 추정해 계획표의 화면·등급과 비교했다. 자동 role(SYS_COMMON·BIZ_COMMON·HQ/SITE_COMMON)도 합쳤다. 계정↔role 매핑은 추정이다.

| 계정 | 추정 role | 읽은 화면 | 계획표 합 | 둘 다 | 둘 다 있는데 등급이 다름 | 읽은 것에만 | 계획표에만 |
|---|---|---:|---:|---:|---:|---:|---:|
| erpadmin | SYS_ADMIN · BIZ_ADMIN · BIZ_MNG_01 · BIZ_MNG_02 · CS_ADMIN · CS_APRV_01 · CS_COMMON · CS_MNG_01 · CS_MNG_02 · CS_VIEW_01 | 307 | 295 | 244 | 0 | 63 | 51 |
| dev01 | DEVELOPER | 246 | 233 | 191 | 1 | 55 | 42 |
| csadm | CS_ADMIN · CS_APRV_01 · CS_COMMON | 86 | 95 | 78 | 0 | 8 | 17 |
| cs01 | CS_MNG_01 · CS_COMMON | 84 | 93 | 76 | 0 | 8 | 17 |
| cs02 | CS_MNG_01 · CS_COMMON | 84 | 93 | 76 | 0 | 8 | 17 |
| cs03 | CS_MNG_02 · CS_COMMON | 51 | 56 | 44 | 0 | 7 | 12 |
| site01 | CS_SITE_01 · CS_COMMON | 55 | 67 | 53 | 0 | 2 | 14 |

- **둘 다 있는 화면의 등급은 거의 일치한다**(불일치 1건: dev01 `ai-plan-approval`). 계획표의 role↔서비스 설계가 실제 메뉴 등급과 맞는다는 뜻이다.
- 「읽은 것에만」은 계획표 이후 추가된 화면이다. 예: cs_acpTrack · cs_copCoPerf · cs_csChkMng_hwf · cs_ctrDailyRpt · cs_finDefect · cs_keySite(하자 신규 화면), `ca-user-role-by-user`(사용자/권한매핑), `ca-page-source-editor`, 시스템 탭의 `sys-*` 메뉴.
- 「계획표에만」은 메뉴에서 읽히지 않은 서비스다(ai-*, approval, approval-delegate 등). 개인 메뉴이거나 숨은 화면일 수 있다. 확인하지 않았다.

## 7. 이 비교로 알 수 있는 것과 없는 것

- 알 수 있다: role 의 설계(유형·등급·범위)와, 그 설계가 7개 계정의 실제 메뉴와 맞는지.
- 알 수 없다: 실제 DB 의 role_service 행(계획 이후 수동 변경 포함) · 계정별 실제 user_role(조직·기간·부여 방식) · hmp-core 가 role 이름으로 직접 주는 특권.

## 8. 확인하지 못한 것 / 다음에 필요한 것

- **DB 직접 조회**: 시도했으나 접속 정보를 앱 설정에서 꺼내 쓰는 방식이 권한 거부되어 하지 않았다. 읽기 전용 접속 방법(허용된 도구나 계정)을 알려 주면 `HMP.role`·`HMP.role_service`·`HMP.user_role` 을 조회해 이 비교를 실제 값으로 바꿀 수 있다. 또는 `erpadmin` 으로 로그인한 Aside 에서 「권한 관리」·「권한/메뉴매핑」을 읽어도 된다.
- **hmp-core 소스**: 작업폴더에 없어 DEVELOPER·SYS_ADMIN 의 코드 상 특권을 확인하지 못했다. 이전 비교에서 dev01 의 「DevOps 콘솔」 등급 3 이 설계(SYS_ADMIN 전용)와 어긋나는지는 계획표의 부여로만 볼 수 있다(§3-4).
- 계획표(P3)는 2026-11-09~10 시점이다. 11-15 의 sql-dev 판(`P5-sqldev-plan-role-service.tsv`)은 CS_* 를 제외한 새 권한코드만 담아 쓰지 않았다.
