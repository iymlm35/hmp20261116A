-- P38 되돌리기 — 메뉴설정(co_menuHConf) program · service · service_i18n · role_service · message 를 지운다(ins_user_id = P38 한정)
-- 사람이 따로 만든 같은 이름의 행은 건드리지 않는다. 적용 뒤 /cache/reload-menu
SET NOCOUNT ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_NULLS ON;
DELETE FROM HMP.role_service WHERE system_id = N'ERP' AND service_id = N'co_menuHConf' AND ins_user_id = N'P38';
DELETE FROM HMP.service_i18n WHERE system_id = N'ERP' AND service_id = N'co_menuHConf' AND ins_user_id = N'P38';
DELETE FROM HMP.service WHERE system_id = N'ERP' AND service_id = N'co_menuHConf' AND ins_user_id = N'P38';
DELETE FROM HMP.program WHERE system_id = N'ERP' AND program_id = N'co_menuHConf' AND ins_user_id = N'P38';
DELETE FROM HMP.message WHERE system_id = N'ERP' AND message_id LIKE N'MSG[_]MNUHCONF[_]%' AND ins_user_id = N'P38';
SELECT N'AFTER' k,
       (SELECT COUNT(*) FROM HMP.program WHERE program_id = N'co_menuHConf') program_n,
       (SELECT COUNT(*) FROM HMP.service WHERE service_id = N'co_menuHConf') service_n,
       (SELECT COUNT(*) FROM HMP.message WHERE message_id LIKE N'MSG[_]MNUHCONF[_]%') message_n;
