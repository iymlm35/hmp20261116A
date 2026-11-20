package hmplus.erp.co.menu.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 메뉴설정 폴더 ID 채번 요청.
 *
 * @file MnuHConfFolderIdReq.java
 * @description 화면에 있는 저장 전 새 행의 메뉴 ID 목록. 채번이 이 ID 를 건너뛴다.
 * @author Claude Code
 * @since 2026-11-20
 * @see 메뉴설정-계획서.md 4-0
 * @copyright Copyright (c) Human Plus Co., Ltd. All rights reserved.
 */
@Getter
@Setter
public class MnuHConfFolderIdReq {

    /** 저장 전 새 행의 메뉴 ID 목록(없으면 null). */
    private List<String> pendingIds;
}
