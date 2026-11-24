# ERP → ERPH202611A 복제 결과 리포트

- 작업일: 2026-11-24 (18:13 ~ 18:47, 약 34분)
- 원본: 192.168.1.170,1434 / ERP (SQL Server 2022 CU27, Linux)
- 대상: 192.168.1.21,23302 / ERPH202611A (SQL Server 2022 RTM)
- 관련 문서: [ERP_복제계획서.md](ERP_복제계획서.md), [todo.md](todo.md), [note.md](note.md)

---

## 1. 종합 결과

| 판정 | 내용 |
|---|---|
| **성공** | 테이블 1,418/1,418 + 데이터 6,145,063행 (실패 0건), 추출 가능한 모든 코드 개체 원문 동일 복제 |
| **부분 제한** | 암호화(WITH ENCRYPTION) 모듈 116개 복제 불가(원본에서 정의 추출 자체가 불가능), DEFAULT 제약 30건 생성 불가(원본 메타데이터 이상) |
| **원본 영향** | 없음 — 전 과정에서 원본에는 SELECT(읽기)만 수행 |

계획서의 B안(백업/복원)은 서버 OS 관리자 협조가 필요하여, 보유한 SQL 계정만으로 가능한 **스키마 스크립팅 + 대량 데이터 복사** 방식으로 수행했다.

## 2. 항목별 복제 결과

| 항목 | 원본 | 타깃 생성 | 판정 |
|---|---|---|---|
| 스키마 (HMP, VPLUS40, hmp_ai) | 3 | 3 (+소유자 사용자 hmp, vplus40을 WITHOUT LOGIN으로 생성) | ✅ |
| 테이블 | 1,418 | 1,418 | ✅ |
| 테이블 데이터 | 6,145,063행 (복사 시점) | 6,145,063행 | ✅ |
| 인덱스 (PK 포함) | 2,087 | 2,087 | ✅ |
| FK 제약 | 120 (비활성 45) | 120 (비활성 45 — 원본 상태 그대로 재현) | ✅ |
| CHECK 제약 | 85 | 85 | ✅ |
| DEFAULT 제약 | 1,453 | 1,423 | ⚠️ 30건 불가 (4장) |
| 시퀀스 | 390 | 390 (현재값 이후부터 이어지도록 RESTART 반영) | ✅ |
| 시노님 | 3 | 3 | ✅ |
| 뷰 | 16 | 2 | ⚠️ 14건 암호화 (3장) |
| 함수 | 146 | 46 | ⚠️ 100건 암호화 (3장) |
| 저장 프로시저 | 3 | 1 | ⚠️ 2건 암호화 (3장) |
| IDENTITY 열 | 28 | 28 (현재값 전수 점검 — 재조정 필요 0건) | ✅ |
| 복구 모델 | SIMPLE | SIMPLE (원본과 동일하게 전환 완료) | ✅ |

## 3. 검증 결과 (사용자 지정 기준)

### 3.1 모듈 코드 줄 수 비교 (함수/프로시저/뷰)

생성된 49개 모듈 전체를 원본 정의와 비교한 결과, **49/49 모두 바이트 단위 동일(IDENTICAL)** — 줄 수는 물론 코드 원문 자체가 일치한다. 원문 무수정 이식 원칙이 지켜졌음을 의미한다. (상세: verify_modules.csv)

### 3.2 테이블 행 수 비교 (전체 1,418개)

- **1,415개 테이블: 원본 기준값과 완전 일치**
- 3개 테이블은 타깃이 더 많음 — 복사 시점이 기준 집계(추출 시점)보다 늦고, 그 사이 원본에 행이 추가된 로그성 테이블로 정상 편차:

| 테이블 | 기준 집계 | 타깃(복사본) |
|---|---|---|
| HMP.log_service | 605,046 | 605,103 |
| HMP.scheduler_history | 487,517 | 487,980 |
| HMP.metric_snapshot | 44,828 | 44,960 |

복사 자체의 손실은 0건이다 (복사 직후 테이블별 COUNT 검증 전수 수행).

## 4. 실패·제한 사항 (조치 이력 포함)

### 4.1 암호화 모듈 116개 — 복제 불가 (원천 제약)

원본에서 WITH ENCRYPTION으로 생성된 모듈은 `sys.sql_modules.definition`이 NULL로, 현재 보유한 SQL 계정(TDS 접속)으로는 어떤 방법으로도 정의를 추출할 수 없다. 규칙(2회 조치)에 따라 대체 경로 확인 후 복제 불가로 확정했다.

**해소 방법 (둘 중 하나):**
1. 서버 관리자 협조로 **백업/복원(계획서 B안)** 수행 — 암호화 모듈까지 완전 복제됨 (권장)
2. 해당 모듈의 원본 소스 코드(.sql)를 보유하고 있다면 타깃에서 직접 생성

**유형별 내역** (전체 116개, 모두 VPLUS40 스키마):

| 유형 | 수 | 개체명 |
|---|---|---|
| 스칼라 함수 | 67 | ADD_DAYS, ADD_MONTHS8, CMD_LBL, DAYS_BETWEEN, FCMA_CHG_OBJ_YN, FCMA_EBGT_DGR, FCMA_EBGT_NM, FCMA_MAX_CTP, FCMD_DT_DIFF, FCMW_LMT_DT, FCMZ_AMT_UNT, FCMZ_CLOS_YN, FCMZ_CNST_RVW_YN, FCMZ_CONT_AMT, FCMZ_CTL_EXCEED_YN, FCMZ_DEFC_EBGT_AMT, FCMZ_ELAP_YN, FCMZ_END_SIT_YN, FCMZ_HO_TRNSF_CHK, FCMZ_TCTL_SITE_YN, FCOA_WORKING_DT_PICK, FCOF_APRV_CTL_YN, FCOF_APRV_STP, FCOZ_COMMA, FCOZ_GET_CUST_NM, FCOZ_GET_DATE, FCOZ_GET_DEPT_NM, FCOZ_GET_TEL_NO, FCOZ_GET_USR_NM, FCOZ_SYS_USE_YN, FCSA_GETHHLDPRGSPRED, FCSM_CTP_NM, FDSA_GET_FHO_NO, FDSZ_APPHOLIDAY_END_DT, FMMZ_MAT_DTL_TP, FMT_AMT, FMT_AMT_PAD2, FMT_DT8, FMT_DTM, FMT_DTS, FMT_INT, FMT_PERD, FMT_YM, FMT_YM_KO, FMT_YMD, FSMZ_VAT_REG_YN, PARSE_DT8, STR_CAT10, STR_CAT2, STR_CAT3, STR_CAT4, STR_CAT5, STR_CAT6, STR_CAT7, STR_CAT8, STR_DEPT_CLEAN, STR_DIGITS, STR_LEN, STR_LPAD, STR_NOWS, STR_NUM, STR_NUMCHARS, STR_POS, STR_RIGHT, STR_RPAD, STR_SUB, STR_YM |
| 인라인 테이블 함수 | 17 | FCMA_IPT_DTL, FCMA_ORD_DTL, FCMA_RESTM_ID, FCMZ_EBGT_CTL_XCPT, FCMZ_HRNK_CTP_NM, FCMZ_SAFT_DEFC_IF, FCMZ_SIT, FCMZ_STD_CTL_XCPT, FCOF_APRV_OBJ, FCSA_SITE_AUTH, FDSZ_ACNTUNIT_AUTH, FFIA_SLPDETAIL, FMMC_CUST_ORD, FMMZ_AGG_XCPT_ITM, FMMZ_ITM_CTL, FSMC_PRPD_PAY, FSMD_CUR_CONT |
| 다중문 테이블 함수 | 16 | FCMA_BCTY_STD_CTP, FCMA_CTP_JOUR, FCMA_EBGT_GW, FCMA_ICST_RT, FCMA_SITU_RPTING_ID, FCMA_STD_CD, FCMA_STD_CTP, FCMA_USE_CTP, FCMD_HO_TRNSF_CMFI, FCME_IDRT_ID, FCMF_CCNST_SCH_DTL, FCMK_TLIGT_ITV, FCMW_PERD_YM, FCMZ_LOWR_CTP_ID, FCOA_WORKING_DT, SPLITTABLESTR |
| 뷰 | 14 | VCOF_APRV, VCOF_APRV_DTL, VCSM_CONT_TRADE, VCSM_CTP_CNT, VCSZ_SITE_INFO, VDSE_BILLNO, VDSF_CONTRACT, VFIA_DEPTCST_BGT_SO, VFIV_BAROBILL, VQMZ_VENDOR, VSMA_VNDR, VSOD_COLLECT, VSOD_DEMAND, VSOZ_PJT_INFO |
| 프로시저 | 2 | PCMA_EBGT_GW_FN, PCMF_CCNST_SCH_DTL_FN |

### 4.2 DEFAULT 제약 30건 — 원본 메타데이터 이상으로 생성 불가

VPLUS40 스키마 23개 테이블의 DEFAULT 제약 30건은 **원본 데이터베이스 자체에서 정의(definition)가 NULL**이어서 DDL을 구성할 수 없었다 (이 이상 때문에 SMO 스크립팅도 해당 테이블에서 실패했고, 테이블 본체는 수동 DDL로 복구 생성함). 조치 2회(카탈로그 재조회, INFORMATION_SCHEMA 경로) 모두 동일하게 NULL이었고, 원본 무수정·임의 보완 금지 원칙에 따라 실패로 확정했다.

- 영향: 해당 열(대부분 `ins_datetime` 감사 열)에 INSERT 시 기본값이 자동 입력되지 않음. 복사된 기존 데이터에는 영향 없음
- 대상 30건: TCMF_CCNST_SCH_CHG, TCMG_APRS, TCMG_SIT_APRS, TCMH_AMTPER_PROC_CTL, TCMZ_FCLS, TCMZ_FCLS_DTL, TFID_CLOS_CUST, THRA_TECHBASE, TLAB_MONMAGAM(2건: ETC_AMT, ins_datetime), TMMB_EBGT_DTL, TMMB_REQ_CTP, TMMB_REQ_ITM, TMMC_CONT_CTP, TMMC_CONT_ITM, TMMC_CONT_PREPAY, TMMD_EVAL_DDCT, TMMD_GI_FCLS, TMMD_GO_ITM, TMMZ_ITM, TSMB_BD_VNDR, TSMD_EVAL_DDCT, TSMD_EVAL_DTL 각 ins_datetime 1건 + TSOC_JOINT_INFO 7건(LM_YN, SKAT_RT, UNTK_AMT, SPLPRC_AMT, VAT, TXFRE_AMT, INS_DATETIME)
- 참고: 백업/복원(B안)을 수행하더라도 이 30건은 원본과 동일한 이상 상태로 복제된다 (원본 자체의 문제)

### 4.3 기타 참고

- 암호화 뷰 14개가 없으므로 이를 참조하는 애플리케이션 기능은 타깃에서 오류가 날 수 있다. 4.1 해소 전까지는 해당 기능 사용 불가
- 시노님 3개(mock_kakao_send 등)는 원본 정의 그대로 생성했으며 [HMP] 스키마의 미존재 개체를 가리킬 수 있다(원본도 동일 상태)

## 5. 수행 방식 요약

| 단계 | 방법 | 결과 |
|---|---|---|
| 스키마 추출 | dbatools(SMO) 스크립팅 + 실패분 23개 테이블은 카탈로그 기반 수동 DDL 생성 | 1,418개 전체 확보 |
| 모듈 추출 | sys.sql_modules 원문 그대로 추출 (ANSI_NULLS/QUOTED_IDENTIFIER 원본 설정 유지) | 49개 (암호화 116개 불가) |
| 데이터 복사 | SqlBulkCopy (KeepIdentity·KeepNulls·TableLock, 배치 20,000행, 테이블별 최대 3회 시도·30분 타임아웃) | 1,418/1,418, 725초 |
| FK | 데이터 적재 후 생성, untrusted는 WITH NOCHECK·비활성 45건은 NOCHECK CONSTRAINT로 원본 상태 재현 | 120/120 |
| 검증 | 모듈 원문 비교(전수) + 테이블 COUNT 전수 비교 + IDENTITY 전수 점검 | 3장 참조 |

적용 규칙: 실패 시 최대 2회 재시도(총 3회), 30분 무응답 시 다음 진행, 모듈 오류는 2회 조치 한도, 원본 읽기 전용·코드 원문 무수정. 모든 규칙이 준수되었고 재시도 한도 소진으로 중단된 항목은 없다.

## 6. 권고 사항

1. **암호화 모듈 116개**: 서버 관리자 협조 하에 백업/복원(계획서 B안)으로 재복제하면 완전해진다. 그때까지 타깃은 "데이터·테이블 완전 + 코드 일부 결손" 상태
2. 애플리케이션을 ERPH202611A로 연결해 주요 화면·기능 동작 확인 (특히 4.1 모듈을 쓰는 기능)
3. 원본의 DEFAULT 정의 NULL 이상(4.2)은 원본 관리 주체에게 전달할 가치가 있음 (원본에서도 잠재 문제)

## 부록. 산출물 위치

- 검증·작업 증적 (본 폴더에 보존): [repl_evidence/](repl_evidence/)
  - verify_modules.csv (모듈 49개 원문 비교), verify_tables.csv (테이블 1,418개 행수 비교), verify_identity.csv (IDENTITY 28개), verify_objects.csv (개체 수 집계), copy_progress.csv (테이블별 복사 기록·시도 횟수·소요), encrypted_modules.csv (암호화 116개 목록), tablelist.csv (추출 시점 기준 행수), apply03b_errors.log (DEFAULT 30건 오류 원문), extract/copy/modules/verify 로그
- 추출 DDL 원본(.sql)·실행 스크립트: 세션 임시 폴더 `D:\root\services\cld\claude-appData\claude\07bdd7d82b968c18\b03a3245-5542-4a6d-bc0f-881f3f725651\scratchpad\repl\` (재실행 필요 시 사용 가능하나 세션 정리 시 삭제될 수 있음)
