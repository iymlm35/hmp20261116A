package hmplus.erp.co.menu.controller;

import static hmplus.erp.common.support.MapSupport.str;

import hmplus.erp.co.menu.dto.MnuHConfFolderIdReq;
import hmplus.erp.co.menu.service.MnuHConfService;
import hmplus.framework.core.annotation.AuthLevel;
import hmplus.framework.core.auth.GridDeleteGuard;
import hmplus.framework.core.dto.GridSaveRequest;
import hmplus.framework.core.dto.Result;
import hmplus.framework.core.dto.ServiceDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 메뉴설정(menuHConf) 컨트롤러 — 화면 pages/co/menuHConf.js 의 서버. 전부 POST.
 *
 * @file MnuHConfController.java
 * @description 메뉴 트리 조회 · 프로그램 고르기 원천 · 폴더 ID 채번 · 변경분 저장.
 *              권한 수준 = 1 조회 · 2 등록·수정·폴더 채번 · 3 삭제(권한 정의표는 계획서 4-2 · 화면과 짝).
 * @author Claude Code
 * @since 2026-11-20
 * @see 메뉴설정-계획서.md (작업기준서 대용) · 화면 pages/co/menuHConf.js
 * @copyright Copyright (c) Human Plus Co., Ltd. All rights reserved.
 */
@RestController
@RequestMapping("/mnu")
@RequiredArgsConstructor
public class MnuHConfController {

    private final MnuHConfService service;

    /**
     * 메뉴 목록.
     *
     * @param p 요청 — useYn(선택 · Y/N)
     * @return 메뉴 행 목록
     */
    @AuthLevel(1)
    @PostMapping("/getGrd_MenuHConf")
    public Result getGrdMenuHConf(@RequestBody(required = false) Map<String, Object> p) {
        return Result.of(service.list(p == null ? null : str(p, "useYn")));
    }

    /**
     * 프로그램 고르기 원천 — 사용 중인 프로그램 전량.
     *
     * @return 프로그램 행 목록
     */
    @AuthLevel(1)
    @PostMapping("/getGrd_MenuHConfProgramList")
    public Result getGrdMenuHConfProgramList() {
        return Result.of(service.programList());
    }

    /**
     * 폴더 메뉴 ID 채번.
     *
     * @param req 요청 — pendingIds(저장 전 새 행 ID)
     * @return 새 폴더 ID(문자열)
     */
    @AuthLevel(2)
    @PostMapping("/getFrm_MenuHConfFolderId")
    public Result getFrmMenuHConfFolderId(@RequestBody(required = false) MnuHConfFolderIdReq req) {
        return Result.of(service.nextFolderId(req == null ? null : req.getPendingIds()));
    }

    /**
     * 변경분 저장 — 추가·수정은 수준 2, 삭제 행이 있으면 수준 3.
     *
     * @param request 변경분(inserted · updated · deleted)
     * @return 저장 완료 안내
     */
    @AuthLevel(2)
    @PostMapping("/setGrd_MenuHConf")
    public Result setGrdMenuHConf(@RequestBody GridSaveRequest<ServiceDto> request) {
        GridDeleteGuard.requireLevel(request, 3);
        service.save(request);
        return Result.<Void>of(null).info("SAVE_COMPLETE");
    }
}
