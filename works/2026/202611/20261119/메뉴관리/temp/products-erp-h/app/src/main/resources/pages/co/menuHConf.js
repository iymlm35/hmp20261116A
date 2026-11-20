/**
 * 메뉴설정 화면 — 메뉴 트리(HMP.service) 편집. 메뉴트리관리(core pages/role/service.js)에서 좌측 프로그램 목록·이동 버튼을 뺀 판.
 *
 * 서버 = erp-h 신규 /mnu/*MenuHConf* (MnuHConfController). core /sys/service-manage/* 는 부르지 않는다.
 * 행 추가 = 선택한 메뉴 아래에 새 행(프로그램은 행의 프로그램 칸 고르기로 지정). 삭제 = 삭제 단추 후 저장.
 * 권한(역할) 부여는 「권한/메뉴매핑」 화면 몫이다 — 이 화면은 메뉴 트리 하나만 둔다.
 *
 * @file menuHConf.js
 * @description 메뉴설정 — 메뉴 트리 인라인 편집(엑셀 내보내기·가져오기 · 레벨 펼치기 · 체크 필터 · 폴더 추가 · 아이콘 고르기)
 * @author Claude Code
 * @since 2026-11-20
 * @see 메뉴설정-계획서.md (작업기준서 대용) · 원본 core pages/role/service.js(복제 기준) · pages/co/mnuMng.js(행 추가 패턴)
 * @copyright Copyright (c) Human Plus Co., Ltd. All rights reserved.
 */
({
    /****************************************************************************
     * pageProps
     ****************************************************************************/
    require: [],
    title: '',
    dirtyCheck: true,
    searchOnLoad: true,

    /****************************************************************************
     * ViewModel
     ****************************************************************************/
    viewModel: {
        // 메뉴 트리 (inline edit) — 전량
        serviceTreeStore: {
            fields: [
                'systemId', 'serviceId', 'serviceName', 'serviceType',
                'parentServiceId', 'programId', 'level',
                'isMenu', 'isPublic', 'openType',
                { name: 'sortOrder', convert: 'number', defaultValue: 0 },
                'icon', 'linkUrl', 'params', 'popupWidth', 'popupHeight', 'logExcludeYn', 'useYn',
                // 읽기 전용 표시 — AS-IS 메뉴 종류(MNU_KND)를 service_type·is_menu 로 환산(methods.toAsisMnuKnd · 저장 칼럼 아님)
                'asisMnuKnd',
                // 모바일 지원 — 비움 = PC 전용 · VIEW · FULL · ONLY
                'mobileMode'
            ],
            tree: { keyField: 'serviceId', parentField: 'parentServiceId' },
            defaults: {
                serviceType: 'SCREEN', sortOrder: 0, useYn: 'Y',
                isMenu: 'Y', isPublic: 'N', openType: 'TAB', logExcludeYn: 'N'
            }
        },
        search: { useYn: '' },
        // 체크 필터(메뉴여부/사용여부/서버) — 검색어는 그리드 자체 검색(searchable)이 맡는다.
        flt: { svcMenu: false, svcUse: false, svcServer: false },
        // 아이콘 고르기 대화상자
        ui: { iconDlg: false, iconVal: '' },
        // onInit 이 선로딩한 프로그램 목록 — 첫 조회가 한 번 이어 쓴다(같은 요청을 또 보내지 않게)
        programRowsInit: null,
        lookup: {
            useYnSearchList: { codeGroup: 'CMN_USE_YN', add: { all: '' } }
        }
    },

    /****************************************************************************
     * Lifecycle
     ****************************************************************************/
    // 🔴 프로그램 고르기 목록은 mount **전에** 확정돼야 한다 — 그리드가 컬럼 생성 시 선택 목록을 1회만 읽는다.
    async onInit() {
        const rows = (await app.post('/mnu/getGrd_MenuHConfProgramList', {}, { showLoading: false, showMessage: false })) || [];
        page.programAll = rows;
        vm.programRowsInit = rows;
    },
    onLoad() {},

    /****************************************************************************
     * Events
     ****************************************************************************/
    events: {
        async onSearch() {
            const search = vm.getData('search');
            // 전량 조회다 — 그리드 자체 검색(searchable)이 로드된 것만 거르므로 lazy 로 받지 않는다.
            const pgmInit = vm.programRowsInit;
            vm.programRowsInit = null;
            const reqs = [{ url: '/mnu/getGrd_MenuHConf', data: { useYn: '' } }];
            if (!pgmInit) reqs.push({ url: '/mnu/getGrd_MenuHConfProgramList', data: {} });
            const [svcRes, pgmLoaded] = await app.postAll(reqs);
            page.programAll = pgmInit || pgmLoaded || [];

            const svcStore = vm.serviceTreeStore;
            svcStore.clearFilter();
            svcStore.loadFlat((svcRes || []).map(s => Object.assign({}, s, { asisMnuKnd: page.methods.toAsisMnuKnd(s) })));
            svcStore.expandToLevel(2);   // AS-IS onRenderStart showTreeLevel(2,0,1) — 2레벨까지 펼친 채 시작
            // 상단 검색창(사용여부) — 값이 있으면(Y/N) 트리에 적용, 빈값('전체')이면 그대로
            if (search.useYn) {
                // 🔴 시트 적재(loadSearchData)는 비동기라 loadFlat 직후엔 새 행이 없다 — 그때 건 필터는 적재에 덮인다.
                await new Promise(function (r) { setTimeout(r, 300); });
                svcStore.setFilter(r => r.useYn === search.useYn);
            }

            const flt = vm.flt;
            flt.svcMenu = false; flt.svcUse = false; flt.svcServer = false;
        },

        // 행 추가 — 선택한 메뉴 아래에 새 행. 상위 메뉴를 먼저 골라야 한다(AS-IS write() 가드 · mnuMng.js onAdd 와 같은 관용구).
        // 🔴 이 훅은 ctx.row 를 변형만 한다(엔진이 트리 배치를 한다) — 직접 addRow 하고 false 를 돌려주면 배치를 우회한다.
        onBeforeAdd(ctx) {
            const grid = view.refs.serviceTree;
            const sel = grid ? grid.getSelectedRows() : [];
            const parent = sel.length ? sel[0] : null;
            let msg = '';
            if (!parent) msg = msgs('MSG_MNU_SELECT_PARENT');
            else if (parent.rowStatus === 'C') msg = msgs('MSG_MNU_SAVE_NEW_FIRST');
            else if ((parent.level || 1) >= 5) msg = msgs('MSG_MNU_LEVEL_EXCEED');
            else if (parent.serviceType === 'SECTION') msg = msgs('MSG_MNU_SECTION_NO_CHILD');
            else if (parent.serviceType !== 'FOLDER' && parent.isMenu === 'N') msg = msgs('MSG_MNU_HIDDEN_NO_CHILD');
            if (msg) {
                app.toast(msg, 'warn');
                return false;
            }
            // 부모가 메뉴(폴더·구분선 아님 · 메뉴 Y)면 상속(is_menu N)으로 시작 — core 메뉴트리관리와 같다
            const inheritInit = parent.serviceType !== 'FOLDER' && parent.isMenu === 'Y';
            ctx.row.systemId = app.system.systemId;
            ctx.row.parentServiceId = parent.serviceId;
            ctx.row.sortOrder = parent.sortOrder || 0;
            ctx.row.isMenu = inheritInit ? 'N' : 'Y';
        },

        async onAddFolder() {
            const svcGrid = view.refs.serviceTree;
            const sel = svcGrid ? svcGrid.getSelectedRows() : [];
            const cur = sel.length ? sel[0] : null;
            // 선택 노드가 폴더면 그 아래 · 화면·서버 노드면 그 노드와 같은 부모 아래(형제) · 선택 없으면 맨 위 단계
            // 🔴 부모 키 = serviceId — rowKey 를 넘기면 IBSheet 가 부모를 못 찾아 맨 아래(루트)에 붙는다
            const parentId = !cur ? null : (cur.serviceType === 'FOLDER' ? cur.serviceId : (cur.parentServiceId || null));
            const parentRow = !cur ? null : (cur.serviceType === 'FOLDER' ? cur : (vm.serviceTreeStore.getFlatData().find(r => r.serviceId === cur.parentServiceId) || null));
            const systemId = app.system.systemId;
            // 폴더 메뉴 ID 자동 채번(MENU_F####) — 아직 저장 안 한 새 행 ID 는 건너뛴다 · 칸은 새 행에서 고쳐 쓸 수 있다
            const pendingIds = (vm.serviceTreeStore.getChangedData().inserted || []).map(r => r.serviceId).filter(Boolean);
            const serviceId = await app.post('/mnu/getFrm_MenuHConfFolderId', { pendingIds }, { showLoading: false });
            vm.serviceTreeStore.addNode(parentId, {
                systemId, serviceId: serviceId || null, serviceType: 'FOLDER',
                parentServiceId: parentId,
                isMenu: 'Y', isPublic: 'N', openType: 'TAB',
                sortOrder: parentRow ? (parentRow.sortOrder || 0) : 0, logExcludeYn: 'N', useYn: 'Y',
                asisMnuKnd: page.methods.toAsisMnuKnd({ serviceType: 'FOLDER' })
            });
        },

        // 상위를 정할 때 부모가 폴더인데 이 행이 상속(메뉴 N · 폴더·구분선 아님)이면 거절한다.
        onBeforeChange(ctx) {
            if (!ctx || ctx.field !== 'parentServiceId' || !ctx.row) return;
            const parent = vm.serviceTreeStore.getFlatData().find(r => r.serviceId === ctx.value);
            if (!parent || parent.serviceType !== 'FOLDER') return;
            if (ctx.row.serviceType === 'FOLDER' || ctx.row.serviceType === 'SECTION' || ctx.row.isMenu !== 'N') return;
            app.toast(msgs('MSG_SERVICE_INHERIT_NO_GROUP_CHILD'), 'warn');
            return false;
        },

        // 체크 필터(메뉴여부/사용여부/서버) — 전량 로드라 서버 재질의 없이 트리에서 걸러낸다.
        onToggleSvcMenu(val) { vm.flt.svcMenu = !!val; page.methods.applySvcFilter(); },
        onToggleSvcUse(val) { vm.flt.svcUse = !!val; page.methods.applySvcFilter(); },
        onToggleSvcServer(val) { vm.flt.svcServer = !!val; page.methods.applySvcFilter(); },

        // 아이콘 칸 클릭 → IconPicker 패널을 대화상자로 연다
        onServiceCellClick(e) {
            if (!e || e.field !== 'icon') return;
            if (app.page.authLevel < 2) return;
            const grid = view.refs.serviceTree;
            const sel = grid ? grid.getSelectedRows() : [];
            page.iconTarget = sel.length ? sel[0] : null;
            if (!page.iconTarget) return;
            vm.ui.iconVal = (e.row && e.row.icon) || '';
            vm.ui.iconDlg = true;
        },
        onIconPicked(cls) {
            if (page.iconTarget) page.iconTarget.icon = cls || null;
            page.iconTarget = null;
            vm.ui.iconDlg = false;
        },
        onIconClear() { page.events.onIconPicked(''); },

        async onSaveService() {
            // 🔴 변경된 행(신규·수정)만 검증한다 — 서버가 같은 검사를 다시 한다(TB-J12).
            const ch = vm.serviceTreeStore.getChangedData() || {};
            const rows = [].concat(ch.inserted || [], ch.updated || []);
            const errs = [];
            rows.forEach(r => {
                const nm = (r.serviceName || '').trim();
                if (!nm) { errs.push(msgs('MSG_SERVICE_NAME_REQ')); return; }
                if (!r.serviceId || !r.serviceId.trim()) {
                    errs.push(nm + ': ' + msgs('MSG_SERVICE_ID_REQ'));
                }
                if ((r.serviceType === 'SCREEN' || r.serviceType === 'SERVER') && (!r.programId || !r.programId.trim())) {
                    errs.push(nm + ': ' + msgs('MSG_SERVICE_PROGRAM_REQ'));
                }
            });
            if (errs.length) { app.toast([...new Set(errs)].slice(0, 6).join('\n'), 'warn'); return; }
            // TB-P17 저장 뒤 새 행 재선택 — 메뉴 ID 는 사용자 입력 키(editable="new") · 수정 저장은 keep-selection 이 보던 행을 잡는다
            const ins = ch.inserted || [];
            const pinRow = ins[ins.length - 1];
            if (pinRow) vm.serviceTreeStore.pinSelection({ serviceId: pinRow.serviceId });
            await app.post('/mnu/setGrd_MenuHConf', 'serviceTreeStore');
            await page.events.onSearch();
        }
    },

    /****************************************************************************
     * Methods
     ****************************************************************************/
    methods: {
        // 프로그램 칸 고르기 — 행의 메뉴 종류에 맞는 프로그램만(SERVER 행 = 서버 API 프로그램 · 화면류 = 서버 아닌 프로그램 · 폴더·구분선 = 없음)
        programFinder(row) {
            const t = row && row.serviceType;
            if (t === 'FOLDER' || t === 'SECTION') return null;
            const server = t === 'SERVER';
            const data = (page.programAll || []).filter(p => server ? p.programType === 'SERVER' : p.programType !== 'SERVER');
            return {
                title: labels.PROGRAM, data, filter: true, valueField: 'programId', displayField: 'programName',
                columns: [
                    { field: 'programName', header: 'PROGRAM_NAME', width: '220px' },
                    { field: 'programId', header: 'PROGRAM_ID', width: '160px' },
                    { field: 'programType', header: 'PROGRAM_TYPE', width: '90px' },
                    { field: 'programPath', header: 'PROGRAM_PATH' }
                ]
            };
        },
        programLabel(v) {
            if (!v) return '';
            const p = (page.programAll || []).find(x => x.programId === v);
            return p ? p.programName + '(' + v + ')' : v;
        },

        // 체크 필터 적용 — 모두 꺼져 있으면 필터 자체를 푼다(전체 표시).
        applySvcFilter() {
            const flt = vm.flt;
            const store = vm.serviceTreeStore;
            if (!flt.svcMenu && !flt.svcUse && !flt.svcServer) { store.clearFilter(); return; }
            // 트리 필터는 매칭 노드의 조상(폴더)까지 유지·펼침 — 엔진 배선이 처리한다.
            store.setFilter(r => (!flt.svcMenu || r.isMenu === 'Y') && (!flt.svcUse || r.useYn === 'Y')
                && (!flt.svcServer || r.serviceType === 'SERVER'));
        },

        // AS-IS 메뉴 종류(MNU_KND) 되읽기 — co mnuMng.js toAsisMnuKnd 와 같은 식.
        //   SECTION → 구분선 · FOLDER → 그룹 · is_menu='Y' → 메뉴 · is_menu='N' → 숨김·상속(한 덩이)
        toAsisMnuKnd(row) {
            if (row.serviceType === 'SECTION') return labels.MNU_KND_SEPARATOR;
            if (row.serviceType === 'FOLDER') return labels.GROUP;
            return row.isMenu === 'N' ? labels.MNU_KND_HIDE_INHERIT : labels.MENU;
        }
    },

    /****************************************************************************
     * Template
     ****************************************************************************/
    template: `
<VBox>
    <!-- title 미지정 = 메뉴명 자동(app.page.serviceName · 메뉴 seed 「메뉴설정」) -->
    <TitleBar search save @search="onSearch" @save="onSaveService" />

    <SearchBar :model="search" @search="onSearch">
        <Field :label="labels.USE_YN" name="useYn">
            <Select :options="lookup.useYnSearchList" />
        </Field>
    </SearchBar>

    <!-- 메뉴 트리 하나 (전량 로드, inline edit) — excelExport·treeLevels = 엑셀·레벨 펼치기(전체 버튼 + 2~5레벨) · excelImport = 내려받은 양식을 고쳐 올린다 -->
    <GridPanel ref="serviceTree" flex="1"
               :title="labels.SERVICE_LIST" icon="pi pi-sitemap"
               :store="serviceTreeStore" selection="multiple" editable searchable keep-selection="serviceId"
               add delete deleteUi="button"
               excelExport excelImport :importOptions="{ matchKey: 'serviceId' }" :treeLevels="[2, 3, 4, 5]"
               @before-add="onBeforeAdd" @cell-click="onServiceCellClick" @before-change="onBeforeChange"
               columnToggle>
        <Tools>
            <Checkbox inputId="svcMenuFlt" :binary="true" :checked="flt.svcMenu"
                      @update:modelValue="onToggleSvcMenu" />
            <label for="svcMenuFlt">{{ labels.IS_MENU }}</label>
            <Checkbox inputId="svcUseFlt" :binary="true" :checked="flt.svcUse"
                      @update:modelValue="onToggleSvcUse" />
            <label for="svcUseFlt">{{ labels.USE_YN }}</label>
            <Checkbox inputId="svcServerFlt" :binary="true" :checked="flt.svcServer"
                      @update:modelValue="onToggleSvcServer" />
            <label for="svcServerFlt">{{ labels.SERVER }}</label>
            <Button :label="labels.ADD_FOLDER" size="small" severity="secondary" authLevel="2" @click="onAddFolder" />
        </Tools>
        <Column field="serviceName" :header="labels.SERVICE_NAME" width="260" expander editable />
        <Column field="serviceId" :header="labels.SERVICE_ID" width="170" editable="new" />
        <!-- 상위 메뉴 ID — 엑셀 양식 왕복용(새 행이 저장 뒤 이 상위 아래로 간다) -->
        <Column field="parentServiceId" :header="labels.PARENT_SERVICE_ID" width="150" editable="new" :visible="false" />
        <Column field="serviceType" :header="labels.SERVICE_TYPE" width="100" editable type="select" codeGroup="CMN_SERVICE_TYPE" clearOnChange="programId" />
        <!-- AS-IS 종류(MNU_KND) — 읽기 전용 · 조회 때 환산 -->
        <Column field="asisMnuKnd" :header="labels.MNU_KND_ASIS" width="100" align="center" editable="false" :visible="false" />
        <Column field="sortOrder" :header="labels.SORT_ORDER" width="70" type="number" editable />
        <!-- 아이콘 = 모양으로 보이고 클릭하면 아이콘 패널(onServiceCellClick) -->
        <Column field="icon" :header="labels.ICON" width="60" type="icon" />
        <!-- 프로그램 = 검색되는 고르기 창 · 목록은 행의 메뉴 종류로 바뀐다(programFinder) · 종류를 바꾸면 비운다(clearOnChange) -->
        <Column field="programId" :header="labels.PROGRAM" width="200" editable type="finder"
                :finder="{ resolve: (row) => programFinder(row) }" :display="(v) => programLabel(v)" />
        <!-- 드물게 쓰는 칸(파라미터·링크·모바일·열기·로그제외)은 기본 숨김 — 툴바 칼럼 표시(columnToggle)로 꺼내 쓴다 -->
        <Column field="params" :header="labels.PARAMS" width="150" editable :visible="false" />
        <Column field="linkUrl" :header="labels.LINK_URL" width="160" editable :visible="false" />
        <Column field="mobileMode" :header="labels.MOBILE_MODE" width="110" editable type="select" codeGroup="CMN_MOBILE_MODE" :visible="false" />
        <Column field="isPublic" :header="labels.IS_PUBLIC" width="70" type="check" editable />
        <Column field="isMenu" :header="labels.IS_MENU" width="70" type="check" editable />
        <Column field="openType" :header="labels.OPEN_TYPE" width="90" editable type="select" codeGroup="CMN_OPEN_TYPE" :visible="false" />
        <Column field="logExcludeYn" :header="labels.LOG_EXCLUDE_YN" width="90" type="check" editable :visible="false" />
        <Column field="useYn" :header="labels.USE_YN" width="70" type="check" editable />
    </GridPanel>

    <Dialog :visible="ui.iconDlg" modal :header="labels.ICON" :style="{ width: '420px' }">
        <IconPicker inline :value="ui.iconVal" @select="onIconPicked" />
        <Footer>
            <Button :label="labels.CLEAR" size="small" severity="secondary" @click="onIconClear" />
            <CloseButton @click="ui.iconDlg = false" />
        </Footer>
    </Dialog>
</VBox>`
})
