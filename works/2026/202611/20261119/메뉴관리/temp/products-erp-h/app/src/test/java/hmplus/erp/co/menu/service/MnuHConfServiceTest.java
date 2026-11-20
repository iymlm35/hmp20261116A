package hmplus.erp.co.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hmplus.framework.core.dto.GridSaveRequest;
import hmplus.framework.core.dto.ServiceDto;
import hmplus.framework.core.exception.BizError;
import hmplus.framework.core.service.ProgramService;
import hmplus.framework.core.service.ServiceService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 메뉴설정 저장 규칙 단위시험 — core ServiceService 는 목(mock)으로 두고 저장 앞의 서버 검증만 본다.
 * 기본 트리: CM(폴더 · level 1) › CMA(폴더 · 2) › CMA010(화면 · 메뉴 Y · 3) · CMA011(화면 · 메뉴 N · 3) · SEC(구분선 · 2).
 */
class MnuHConfServiceTest {

    private ServiceService core;
    private MnuHConfService service;
    private final List<ServiceDto> table = new ArrayList<>();

    @BeforeEach
    void setUp() {
        core = mock(ServiceService.class);
        ProgramService programs = mock(ProgramService.class);
        service = new MnuHConfService(core, programs);
        ReflectionTestUtils.setField(service, "systemId", "ERP");
        table.clear();
        table.add(row("CM", null, 1, "FOLDER", "Y", null));
        table.add(row("CMA", "CM", 2, "FOLDER", "Y", null));
        table.add(row("CMA010", "CMA", 3, "SCREEN", "Y", "CMA010"));
        table.add(row("CMA011", "CMA", 3, "SCREEN", "N", "CMA011"));
        table.add(row("SEC", "CMA", 2, "SECTION", "Y", null));
        when(core.findBySystemId("ERP")).thenReturn(table);
    }

    private static ServiceDto row(String id, String parent, int level, String type, String isMenu, String programId) {
        ServiceDto d = new ServiceDto();
        d.setSystemId("ERP");
        d.setServiceId(id);
        d.setParentServiceId(parent);
        d.setLevel(level);
        d.setServiceType(type);
        d.setIsMenu(isMenu);
        d.setProgramId(programId);
        d.setServiceName(id + " 이름");
        return d;
    }

    private static GridSaveRequest<ServiceDto> inserted(ServiceDto... rows) {
        GridSaveRequest<ServiceDto> r = new GridSaveRequest<>();
        r.setInserted(new ArrayList<>(List.of(rows)));
        return r;
    }

    @Test
    void 추가_정상이면_level_을_상위_다음으로_계산해_core_저장에_넘긴다() {
        ServiceDto n = row("CMA020", "CMA", 99, "SCREEN", "Y", "CMA020");
        GridSaveRequest<ServiceDto> req = inserted(n);
        service.save(req);
        assertThat(n.getLevel()).isEqualTo(3);
        verify(core).batchSave(req);
    }

    @Test
    void 추가_이름이_없으면_거절한다() {
        ServiceDto n = row("CMA020", "CMA", 3, "SCREEN", "Y", "CMA020");
        n.setServiceName(" ");
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_SERVICE_NAME_REQ");
        verify(core, never()).batchSave(any());
    }

    @Test
    void 추가_ID_형식이_틀리면_거절한다() {
        ServiceDto n = row("CMA 020", "CMA", 3, "SCREEN", "Y", "CMA020");
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNUHCONF_ID_FORMAT");
    }

    @Test
    void 추가_ID_가_이미_있으면_거절한다() {
        ServiceDto n = row("CMA010", "CMA", 3, "SCREEN", "Y", "CMA010");
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNUHCONF_ID_DUP");
    }

    @Test
    void 같은_요청_안에서_ID_가_겹치면_거절한다() {
        ServiceDto a = row("CMA020", "CMA", 3, "SCREEN", "Y", "CMA020");
        ServiceDto b = row("CMA020", "CMA", 3, "SCREEN", "Y", "CMA020");
        assertThatThrownBy(() -> service.save(inserted(a, b))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNUHCONF_ID_DUP");
    }

    @Test
    void 화면_행에_프로그램이_없으면_거절한다() {
        ServiceDto n = row("CMA020", "CMA", 3, "SCREEN", "Y", null);
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_SERVICE_PROGRAM_REQ");
    }

    @Test
    void 상위가_없으면_거절한다() {
        ServiceDto n = row("CMA020", "NOPE", 3, "FOLDER", "Y", null);
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNU_HRNK_CD_NOT_EXIST");
    }

    @Test
    void 구분선_아래에는_추가할_수_없다() {
        ServiceDto n = row("CMA020", "SEC", 3, "FOLDER", "Y", null);
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNU_SECTION_NO_CHILD");
    }

    @Test
    void 숨김_메뉴_아래에는_추가할_수_없다() {
        ServiceDto n = row("CMA020", "CMA011", 4, "SCREEN", "N", "CMA020");
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNU_HIDDEN_NO_CHILD");
    }

    @Test
    void 깊이가_5를_넘기면_거절한다() {
        table.add(row("L4", "CMA010", 4, "FOLDER", "Y", null));
        table.add(row("L5", "L4", 5, "FOLDER", "Y", null));
        ServiceDto n = row("L6", "L5", 6, "FOLDER", "Y", null);
        assertThatThrownBy(() -> service.save(inserted(n))).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNU_LEVEL_EXCEED");
    }

    @Test
    void 같은_요청의_새_폴더_아래에_추가할_수_있다() {
        ServiceDto folder = row("MENU_F0001", "CM", 9, "FOLDER", "Y", null);
        ServiceDto child = row("CMX010", "MENU_F0001", 9, "SCREEN", "Y", "CMX010");
        service.save(inserted(folder, child));
        assertThat(folder.getLevel()).isEqualTo(2);
        assertThat(child.getLevel()).isEqualTo(3);
    }

    @Test
    void 수정은_ID_상위_level_을_바꾸지_않는다() {
        ServiceDto u = row("CMA010", "CM", 1, "SCREEN", "Y", "CMA010");
        u.setServiceName("새 이름");
        GridSaveRequest<ServiceDto> req = new GridSaveRequest<>();
        req.setUpdated(new ArrayList<>(List.of(u)));
        service.save(req);
        assertThat(u.getParentServiceId()).isEqualTo("CMA");
        assertThat(u.getLevel()).isEqualTo(3);
        verify(core).batchSave(req);
    }

    @Test
    void 하위가_남는_행_삭제는_거절한다() {
        ServiceDto d = row("CMA", "CM", 2, "FOLDER", "Y", null);
        GridSaveRequest<ServiceDto> req = new GridSaveRequest<>();
        req.setDeleted(new ArrayList<>(List.of(d)));
        assertThatThrownBy(() -> service.save(req)).isInstanceOf(BizError.class)
                .hasMessageContaining("MSG_MNUHCONF_DELETE_HAS_CHILD");
        verify(core, never()).batchSave(any());
    }

    @Test
    void 하위까지_함께_삭제하면_통과한다() {
        GridSaveRequest<ServiceDto> req = new GridSaveRequest<>();
        req.setDeleted(new ArrayList<>(List.of(
                row("CMA", "CM", 2, "FOLDER", "Y", null),
                row("CMA010", "CMA", 3, "SCREEN", "Y", "CMA010"),
                row("CMA011", "CMA", 3, "SCREEN", "N", "CMA011"),
                row("SEC", "CMA", 2, "SECTION", "Y", null))));
        service.save(req);
        verify(core).batchSave(req);
    }

    @Test
    void 폴더_ID_는_가장_큰_번호_다음을_돌려준다() {
        table.add(row("MENU_F0003", "CM", 2, "FOLDER", "Y", null));
        table.add(row("MENU_FX", "CM", 2, "FOLDER", "Y", null));
        assertThat(service.nextFolderId(null)).isEqualTo("MENU_F0004");
        assertThat(service.nextFolderId(List.of("MENU_F0007"))).isEqualTo("MENU_F0008");
    }

    @Test
    void 폴더_ID_는_폴더가_없으면_0001_부터다() {
        assertThat(service.nextFolderId(List.of())).isEqualTo("MENU_F0001");
    }
}
