-- =============================================================================
-- P38 메뉴설정(co_menuHConf) 등록 (2026-11-20 · 작성만 · sql-h 에는 적용하지 않았다)
-- 위치 = 공통 › 시스템관리(MENU_CO_COZ) · 메뉴정보(ca-service · sort 50) 바로 아래 · sort 55(에러로그 ca-log-error 60 앞)
-- 화면 pages/co/menuHConf.js · 서버 MnuHConfController(/mnu/*MenuHConf*) · 계획서 메뉴설정-계획서.md
-- 적용 주체 = 리드(메뉴·권한·서비스 등록은 직접 하지 않는다 · docs/vibe/VIBE-CODING.md §2) · 적용 뒤 /cache/reload-menu
-- 부여 role = SYS_ADMIN 3 (BIZ_ADMIN 3 추가 여부는 계획서 5장 3번 결정 전 · 아래 주석 행으로 준비)
-- 되돌림: P38-menu-hconf-mssql-down.sql (ins_user_id = P38 한정)
-- 멱등(WHERE NOT EXISTS) · 방언 중립 · 시스템 ID = ERP
-- =============================================================================
SET NOCOUNT ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_NULLS ON;

-- 1) program — 화면 정의
INSERT INTO HMP.program (system_id,program_id,biz_group_id,program_name,program_type,program_path,description,handle_privacy_yn,use_yn,feature_module_id,auth_max_level,auth_level1_term,auth_level2_term,auth_level3_term,ins_user_id,ins_datetime)
SELECT N'ERP',N'co_menuHConf',N'SYS_PLATFORM',N'메뉴설정',N'SCREEN',N'co/menuHConf',NULL,N'N',N'Y',NULL,3,N'조회',N'수정',N'삭제',N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.program WHERE system_id=N'ERP' AND program_id=N'co_menuHConf');

-- 2) service — 메뉴 노드 (공통 › 시스템관리 하위 · level 3 · sort 55)
INSERT INTO HMP.service (system_id,service_id,parent_service_id,"level",sort_order,service_name,service_type,program_id,link_url,params,is_menu,is_public,log_exclude_yn,open_type,popup_width,popup_height,icon,use_yn,data_scope_org_type,capability_condition,tenant_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'co_menuHConf',N'MENU_CO_COZ',3,55,N'메뉴설정',N'SCREEN',N'co_menuHConf',NULL,NULL,N'Y',N'N',N'N',N'TAB',NULL,NULL,N'pi pi-sitemap',N'Y',N'LEGAL',NULL,N'DEFAULT',N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.service WHERE system_id=N'ERP' AND tenant_id=N'DEFAULT' AND service_id=N'co_menuHConf');

-- 3) service_i18n — 메뉴명 ko · en (나머지 언어는 메뉴 시드 일괄 작업 때)
INSERT INTO HMP.service_i18n (system_id,service_id,language,service_name,tenant_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'co_menuHConf',N'ko',N'메뉴설정',N'DEFAULT',N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.service_i18n WHERE system_id=N'ERP' AND tenant_id=N'DEFAULT' AND service_id=N'co_menuHConf' AND language=N'ko');
INSERT INTO HMP.service_i18n (system_id,service_id,language,service_name,tenant_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'co_menuHConf',N'en',N'Menu Setting',N'DEFAULT',N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.service_i18n WHERE system_id=N'ERP' AND tenant_id=N'DEFAULT' AND service_id=N'co_menuHConf' AND language=N'en');

-- 4) role_service — SYS_ADMIN 등급 3 · 범위 ALL · 엑셀 가져오기·내보내기 Y
--    등급 뜻: 1 조회 · 2 등록·수정·폴더 채번 · 3 삭제 (서버 @AuthLevel 과 짝)
INSERT INTO HMP.role_service (system_id,role_id,service_id,auth_level,allow_file_download,allow_file_upload,allow_excel_export,allow_excel_import,allow_print,data_scope,scope_structure_type,applicable_user_types,tenant_id,use_yn,ins_user_id,ins_datetime)
SELECT r.system_id,r.role_id,N'co_menuHConf',N'3',N'N',N'N',N'Y',N'Y',N'N',N'ALL',NULL,NULL,N'DEFAULT',N'Y',N'P38',CURRENT_TIMESTAMP
FROM HMP.role r
WHERE r.role_id = N'SYS_ADMIN' AND r.system_id = N'ERP' AND r.tenant_id = N'DEFAULT'
  AND EXISTS (SELECT 1 FROM HMP.service a WHERE a.system_id = r.system_id AND a.service_id = N'co_menuHConf')
  AND NOT EXISTS (SELECT 1 FROM HMP.role_service x WHERE x.system_id = r.system_id AND x.tenant_id = N'DEFAULT' AND x.role_id = r.role_id AND x.service_id = N'co_menuHConf');
-- BIZ_ADMIN 3 을 줄 때(결정 후): 위 4) 의 role_id 를 BIZ_ADMIN 으로 바꿔 한 번 더 실행한다.

-- 5) message — 서버 거절 문구 새 키 3종 ko · en (기존 MSG_MNU_* · MSG_SERVICE_* 키는 재사용)
INSERT INTO HMP.message (system_id,message_id,language,biz_group_id,message_text,tenant_id,project_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'MSG_MNUHCONF_ID_FORMAT',N'ko',N'SYS',N'메뉴 ID 는 영문·숫자·밑줄·하이픈만 쓸 수 있습니다: {0}',N'DEFAULT',NULL,N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.message WHERE system_id=N'ERP' AND message_id=N'MSG_MNUHCONF_ID_FORMAT' AND language=N'ko');
INSERT INTO HMP.message (system_id,message_id,language,biz_group_id,message_text,tenant_id,project_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'MSG_MNUHCONF_ID_FORMAT',N'en',N'SYS',N'The menu ID may contain only letters, digits, underscores and hyphens: {0}',N'DEFAULT',NULL,N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.message WHERE system_id=N'ERP' AND message_id=N'MSG_MNUHCONF_ID_FORMAT' AND language=N'en');
INSERT INTO HMP.message (system_id,message_id,language,biz_group_id,message_text,tenant_id,project_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'MSG_MNUHCONF_ID_DUP',N'ko',N'SYS',N'이미 있는 메뉴 ID 입니다: {0}',N'DEFAULT',NULL,N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.message WHERE system_id=N'ERP' AND message_id=N'MSG_MNUHCONF_ID_DUP' AND language=N'ko');
INSERT INTO HMP.message (system_id,message_id,language,biz_group_id,message_text,tenant_id,project_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'MSG_MNUHCONF_ID_DUP',N'en',N'SYS',N'The menu ID already exists: {0}',N'DEFAULT',NULL,N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.message WHERE system_id=N'ERP' AND message_id=N'MSG_MNUHCONF_ID_DUP' AND language=N'en');
INSERT INTO HMP.message (system_id,message_id,language,biz_group_id,message_text,tenant_id,project_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'MSG_MNUHCONF_DELETE_HAS_CHILD',N'ko',N'SYS',N'하위 메뉴가 남아 있어 삭제할 수 없습니다: {0}',N'DEFAULT',NULL,N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.message WHERE system_id=N'ERP' AND message_id=N'MSG_MNUHCONF_DELETE_HAS_CHILD' AND language=N'ko');
INSERT INTO HMP.message (system_id,message_id,language,biz_group_id,message_text,tenant_id,project_id,ins_user_id,ins_datetime)
SELECT N'ERP',N'MSG_MNUHCONF_DELETE_HAS_CHILD',N'en',N'SYS',N'It cannot be deleted because sub-menus remain: {0}',N'DEFAULT',NULL,N'P38',CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM HMP.message WHERE system_id=N'ERP' AND message_id=N'MSG_MNUHCONF_DELETE_HAS_CHILD' AND language=N'en');

-- 확인
SELECT N'AFTER' k,
       (SELECT COUNT(*) FROM HMP.program WHERE system_id=N'ERP' AND program_id=N'co_menuHConf') program_n,
       (SELECT COUNT(*) FROM HMP.service WHERE system_id=N'ERP' AND service_id=N'co_menuHConf') service_n,
       (SELECT COUNT(*) FROM HMP.service_i18n WHERE system_id=N'ERP' AND service_id=N'co_menuHConf') i18n_n,
       (SELECT COUNT(*) FROM HMP.role_service WHERE system_id=N'ERP' AND service_id=N'co_menuHConf') role_service_n,
       (SELECT COUNT(*) FROM HMP.message WHERE system_id=N'ERP' AND message_id LIKE N'MSG[_]MNUHCONF[_]%') message_n;
