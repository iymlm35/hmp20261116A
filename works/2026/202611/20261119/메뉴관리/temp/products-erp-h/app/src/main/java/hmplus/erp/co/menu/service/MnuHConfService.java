package hmplus.erp.co.menu.service;

import hmplus.framework.core.dto.GridSaveRequest;
import hmplus.framework.core.dto.ProgramDto;
import hmplus.framework.core.dto.ServiceDto;
import hmplus.framework.core.exception.BizError;
import hmplus.framework.core.service.ProgramService;
import hmplus.framework.core.service.ServiceService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 메뉴설정(menuHConf) 서비스 — 메뉴 트리(HMP.service) 편집 업무.
 *
 * @file MnuHConfService.java
 * @description 메뉴 목록 조회 · 폴더 ID 채번 · 변경분 저장(서버 재검증). 표 쓰기는 core {@link ServiceService} 에 맡긴다
 *              (감사 칼럼 · 메뉴 캐시 갱신은 core 가 한다). 이 서비스는 core 저장 앞에서 메뉴 규칙만 다시 검사한다.
 * @author Claude Code
 * @since 2026-11-20
 * @see 메뉴설정-계획서.md (작업기준서 대용 · hmpcoms/hmp20261116A/works/2026/202611/20261119/메뉴관리) · 화면 pages/co/menuHConf.js
 * @copyright Copyright (c) Human Plus Co., Ltd. All rights reserved.
 *
 * <p>저장 범위는 service 표뿐이다 — program · biz_group 은 만들지도 고치지도 않는다(계획서 5장 11번 · 결정 전 기본 가정).</p>
 */
@Service
@RequiredArgsConstructor
public class MnuHConfService {

    /** 메뉴 깊이 상한(최상위 = 1) — 화면 레벨 펼치기 2~5 와 같다. */
    private static final int MAX_LEVEL = 5;
    /** 메뉴 ID 형식 — 영숫자 · 밑줄 · 하이픈. */
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9_\\-]+$");
    /** 폴더 ID 자동 채번 접두 — core 메뉴트리관리와 같은 규칙(MENU_F####). */
    private static final String FOLDER_ID_PREFIX = "MENU_F";
    private static final Pattern FOLDER_SEQ = Pattern.compile("^\\d+$");

    private final ServiceService serviceService;
    private final ProgramService programService;

    @Value("${framework.system-id:ERP}")
    private String systemId;

    /**
     * 메뉴 목록 — 로그인 시스템의 메뉴 전량. 화면이 트리로 쌓는다.
     *
     * @param useYn 사용 여부(Y/N). 비면 전체
     * @return 메뉴 행 목록
     */
    public List<ServiceDto> list(String useYn) {
        if (useYn == null || useYn.isBlank()) {
            return serviceService.findBySystemId(systemId);
        }
        return serviceService.findBySystemIdAndUseYn(systemId, useYn);
    }

    /**
     * 프로그램 고르기 원천 — 사용 중인 프로그램 전량.
     *
     * @return 프로그램 행 목록
     */
    public List<ProgramDto> programList() {
        return programService.findBySystemIdAndUseYn(systemId, "Y");
    }

    /**
     * 폴더 메뉴 ID 채번 — {@code MENU_F####} 중 가장 큰 번호 + 1. 아직 저장 전인 새 행의 ID 도 건너뛴다.
     *
     * @param pendingIds 화면에 있는 저장 전 새 행 ID 목록(없으면 null)
     * @return 새 폴더 ID
     */
    public String nextFolderId(List<String> pendingIds) {
        int max = 0;
        List<String> ids = new ArrayList<>();
        for (ServiceDto s : serviceService.findBySystemId(systemId)) {
            ids.add(s.getServiceId());
        }
        if (pendingIds != null) {
            ids.addAll(pendingIds);
        }
        for (String id : ids) {
            if (id == null || !id.startsWith(FOLDER_ID_PREFIX)) {
                continue;
            }
            String seq = id.substring(FOLDER_ID_PREFIX.length());
            if (FOLDER_SEQ.matcher(seq).matches()) {
                max = Math.max(max, Integer.parseInt(seq));
            }
        }
        return String.format("%s%04d", FOLDER_ID_PREFIX, max + 1);
    }

    /**
     * 변경분 저장 — 화면이 보낸 값을 서버가 다시 검사한 뒤 core 저장에 넘긴다(TB-J12).
     *
     * <ul>
     *   <li>추가: 이름 · ID(형식 · 중복) · 프로그램(화면 · 서버 종류) 필수, 상위 존재 · 깊이 · 하위 금지 규칙, level 은 서버가 계산한다.</li>
     *   <li>수정: ID · 상위 · level 은 바꾸지 않는다(손대지 않은 값 보존 · TB-J14).</li>
     *   <li>삭제: 하위 메뉴가 남는 행은 거절한다.</li>
     * </ul>
     *
     * @param request 변경분(inserted · updated · deleted)
     * @throws BizError 규칙 위반(메시지 키)
     */
    @Transactional
    public void save(GridSaveRequest<ServiceDto> request) {
        Map<String, ServiceDto> byId = new HashMap<>();
        for (ServiceDto s : serviceService.findBySystemId(systemId)) {
            byId.put(s.getServiceId(), s);
        }
        Set<String> deletedIds = new HashSet<>();
        for (ServiceDto d : Optional.ofNullable(request.getDeleted()).orElse(List.of())) {
            deletedIds.add(d.getServiceId());
        }
        checkDeleted(byId, deletedIds);
        checkUpdated(byId, Optional.ofNullable(request.getUpdated()).orElse(List.of()));
        checkInserted(byId, Optional.ofNullable(request.getInserted()).orElse(List.of()));
        serviceService.batchSave(request);
    }

    private void checkDeleted(Map<String, ServiceDto> byId, Set<String> deletedIds) {
        for (ServiceDto s : byId.values()) {
            if (s.getParentServiceId() != null && deletedIds.contains(s.getParentServiceId())
                    && !deletedIds.contains(s.getServiceId())) {
                throw new BizError("MSG_MNUHCONF_DELETE_HAS_CHILD", s.getParentServiceId());
            }
        }
    }

    private void checkUpdated(Map<String, ServiceDto> byId, List<ServiceDto> updated) {
        for (ServiceDto u : updated) {
            ServiceDto cur = byId.get(u.getServiceId());
            if (cur == null) {
                continue;
            }
            u.setSystemId(systemId);
            u.setParentServiceId(cur.getParentServiceId());
            u.setLevel(cur.getLevel());
            requireName(u);
            requireProgram(u);
        }
    }

    private void checkInserted(Map<String, ServiceDto> byId, List<ServiceDto> inserted) {
        Map<String, ServiceDto> known = new HashMap<>(byId);
        for (ServiceDto n : inserted) {
            n.setSystemId(systemId);
            requireName(n);
            String id = n.getServiceId();
            if (id == null || id.isBlank()) {
                throw new BizError("MSG_SERVICE_ID_REQ", n.getServiceName());
            }
            if (!ID_PATTERN.matcher(id).matches()) {
                throw new BizError("MSG_MNUHCONF_ID_FORMAT", id);
            }
            if (known.containsKey(id)) {
                throw new BizError("MSG_MNUHCONF_ID_DUP", id);
            }
            requireProgram(n);
            n.setLevel(levelUnder(known, n.getParentServiceId()));
            known.put(id, n);
        }
    }

    /** 상위 행을 확인하고 새 행의 level 을 돌려준다(상위 없음 = 최상위 1). */
    private int levelUnder(Map<String, ServiceDto> known, String parentId) {
        if (parentId == null || parentId.isBlank()) {
            return 1;
        }
        ServiceDto p = known.get(parentId);
        if (p == null) {
            throw new BizError("MSG_MNU_HRNK_CD_NOT_EXIST", parentId);
        }
        if ("SECTION".equals(p.getServiceType())) {
            throw new BizError("MSG_MNU_SECTION_NO_CHILD", parentId);
        }
        if (!"FOLDER".equals(p.getServiceType()) && "N".equals(p.getIsMenu())) {
            throw new BizError("MSG_MNU_HIDDEN_NO_CHILD", parentId);
        }
        int level = (p.getLevel() == null ? 1 : p.getLevel()) + 1;
        if (level > MAX_LEVEL) {
            throw new BizError("MSG_MNU_LEVEL_EXCEED", parentId);
        }
        return level;
    }

    private void requireName(ServiceDto s) {
        if (s.getServiceName() == null || s.getServiceName().isBlank()) {
            throw new BizError("MSG_SERVICE_NAME_REQ", s.getServiceId());
        }
    }

    private void requireProgram(ServiceDto s) {
        boolean needs = "SCREEN".equals(s.getServiceType()) || "SERVER".equals(s.getServiceType());
        if (needs && (s.getProgramId() == null || s.getProgramId().isBlank())) {
            throw new BizError("MSG_SERVICE_PROGRAM_REQ", s.getServiceName());
        }
    }
}
