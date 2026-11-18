# erp-h 상단메뉴별 left메뉴 트리 — `h.dev01` 에 role `CS_COMMON` 만 부여한 경우 (2026-11-18 조회)

- 조회: 2026-11-18 · `http://localhost:8080/` · 화면 우상단 사용자 표시 「개발자1 (IT팀)」
- 계정·role: `h.dev01` 에 role `CS_COMMON`(하자 공통) 1개를 설정한 상태의 메뉴다(작성자 확인). 화면에는 login id 가 나오지 않는다.
- 방법: Aside 브라우저로 로그인된 탭의 메뉴 컴포넌트가 화면에 그리는 메뉴 데이터(`moduleList`)의 `label`·`serviceId`·`authLevel` 을 그대로 옮겼다. 메뉴 API(`POST /sys/menu/list`)는 호출하지 않았다.
- 트리 표기: 화면 이름 뒤에 serviceId, 화면 주소 `/page/<serviceId>`, 등급 순서. 폴더는 `이름/`. 화면 주소는 `/page/<serviceId>` 규칙을 따른 것이며 메뉴를 눌러 주소창을 대조하지는 않았다.
- 등급: 1 조회 · 2 저장 · 3 삭제·특수 (`authLevel`).

| 상단 탭 | 폴더 | 화면 | 등급 분포 |
|---|---:|---:|---|
| 개인 | 7 | 26 | 1 11개 · 2 13개 · 3 2개 |

- 상단 탭 바에 탭이 하나도 나오지 않는다(「하자」·「공통」·「시스템」 없음). left메뉴에는 「개인」 트리만 있다.
- 「하자」 탭이 없는 것은 `CS_COMMON` 에 하자 메뉴 부여가 없다는 뜻으로 읽을 수 있다. 「공통」·「시스템」 탭도 없다.
- left메뉴 아래쪽 고정 버튼 「업무 지도」는 메뉴 트리 데이터에 없다.

## 1. 트리

```text
상단메뉴
└── [개인]  key=MENU_COMMON (폴더 7 · 화면 26)
    ├── 마이페이지/
    │   ├── 개인설정  `common-preference` → `/page/common-preference` · 등급 2
    │   ├── 캘린더  `common-calendar` → `/page/common-calendar` · 등급 2
    │   ├── 내 정보  `common-my-page` → `/page/common-my-page` · 등급 1
    │   ├── 사용자 대시보드  `user-dashboard` → `/page/user-dashboard` · 등급 2
    │   ├── 내 프로그램  `my-programs` → `/page/my-programs` · 등급 3
    │   └── 내 예약 보고서  `user-job` → `/page/user-job` · 등급 3
    ├── 결재/
    │   ├── 결재 위임  `common-approval-dlg` → `/page/common-approval-dlg` · 등급 2
    │   ├── 결재선 템플릿  `common-approval-tpl` → `/page/common-approval-tpl` · 등급 2
    │   └── 결재함  `common-approval` → `/page/common-approval` · entryPoint=approval · 등급 1
    ├── 문서/
    │   ├── 내가 만든 문서함  `common-doc-list` → `/page/common-doc-list` · 등급 2
    │   ├── 시트  `common-sheet` → `/page/common-sheet` · 등급 2
    │   ├── 내 문서 이력  `common-doc-version` → `/page/common-doc-version` · 등급 1
    │   ├── 전자 양식  `ca-eform-list` → `/page/ca-eform-list` · 등급 2
    │   └── 원본 확인  `ca-common-doc-verify` → `/page/ca-common-doc-verify` · 등급 1
    ├── 소통/
    │   ├── 공지사항  `common-notice` → `/page/common-notice` · 등급 1
    │   ├── 자료실  `common-library` → `/page/common-library` · 등급 1
    │   ├── 자유게시판  `common-general` → `/page/common-general` · 등급 1
    │   ├── 자주 묻는 질문  `common-faq` → `/page/common-faq` · 등급 1
    │   ├── 주소록  `common-address-book` → `/page/common-address-book` · 등급 1
    │   ├── 조직도  `common-org-chart` → `/page/common-org-chart` · 등급 1
    │   ├── 문의  `common-inquiry` → `/page/common-inquiry` · entryPoint=inquiry · 등급 2
    │   └── 알림 센터  `common-notification` → `/page/common-notification` · entryPoint=notification · 등급 2
    ├── AI/
    │   ├── AI 브리핑  `ai-briefing` → `/page/ai-briefing` · 등급 2
    │   └── AI 어시스턴트  `common-ai-chat` → `/page/common-ai-chat` · entryPoint=aiWindow · 등급 2
    ├── 설문·동의/
    │   └── 동의서  `common-agreement` → `/page/common-agreement` · 등급 1
    └── 내 요청/
        └── 권한·겸직 신청  `ca-role-request` → `/page/ca-role-request` · 등급 2
```

## 2. 화면별 메뉴 경로 표

| 상단 탭 | 메뉴 경로 | serviceId | 등급 | 화면 주소 |
|---|---|---|---:|---|
| 개인 | 마이페이지 › **개인설정** | `common-preference` | 2 | `http://localhost:8080/page/common-preference` |
| 개인 | 마이페이지 › **캘린더** | `common-calendar` | 2 | `http://localhost:8080/page/common-calendar` |
| 개인 | 마이페이지 › **내 정보** | `common-my-page` | 1 | `http://localhost:8080/page/common-my-page` |
| 개인 | 마이페이지 › **사용자 대시보드** | `user-dashboard` | 2 | `http://localhost:8080/page/user-dashboard` |
| 개인 | 마이페이지 › **내 프로그램** | `my-programs` | 3 | `http://localhost:8080/page/my-programs` |
| 개인 | 마이페이지 › **내 예약 보고서** | `user-job` | 3 | `http://localhost:8080/page/user-job` |
| 개인 | 결재 › **결재 위임** | `common-approval-dlg` | 2 | `http://localhost:8080/page/common-approval-dlg` |
| 개인 | 결재 › **결재선 템플릿** | `common-approval-tpl` | 2 | `http://localhost:8080/page/common-approval-tpl` |
| 개인 | 결재 › **결재함** | `common-approval` | 1 | `http://localhost:8080/page/common-approval` |
| 개인 | 문서 › **내가 만든 문서함** | `common-doc-list` | 2 | `http://localhost:8080/page/common-doc-list` |
| 개인 | 문서 › **시트** | `common-sheet` | 2 | `http://localhost:8080/page/common-sheet` |
| 개인 | 문서 › **내 문서 이력** | `common-doc-version` | 1 | `http://localhost:8080/page/common-doc-version` |
| 개인 | 문서 › **전자 양식** | `ca-eform-list` | 2 | `http://localhost:8080/page/ca-eform-list` |
| 개인 | 문서 › **원본 확인** | `ca-common-doc-verify` | 1 | `http://localhost:8080/page/ca-common-doc-verify` |
| 개인 | 소통 › **공지사항** | `common-notice` | 1 | `http://localhost:8080/page/common-notice` |
| 개인 | 소통 › **자료실** | `common-library` | 1 | `http://localhost:8080/page/common-library` |
| 개인 | 소통 › **자유게시판** | `common-general` | 1 | `http://localhost:8080/page/common-general` |
| 개인 | 소통 › **자주 묻는 질문** | `common-faq` | 1 | `http://localhost:8080/page/common-faq` |
| 개인 | 소통 › **주소록** | `common-address-book` | 1 | `http://localhost:8080/page/common-address-book` |
| 개인 | 소통 › **조직도** | `common-org-chart` | 1 | `http://localhost:8080/page/common-org-chart` |
| 개인 | 소통 › **문의** | `common-inquiry` | 2 | `http://localhost:8080/page/common-inquiry` |
| 개인 | 소통 › **알림 센터** | `common-notification` | 2 | `http://localhost:8080/page/common-notification` |
| 개인 | AI › **AI 브리핑** | `ai-briefing` | 2 | `http://localhost:8080/page/ai-briefing` |
| 개인 | AI › **AI 어시스턴트** | `common-ai-chat` | 2 | `http://localhost:8080/page/common-ai-chat` |
| 개인 | 설문·동의 › **동의서** | `common-agreement` | 1 | `http://localhost:8080/page/common-agreement` |
| 개인 | 내 요청 › **권한·겸직 신청** | `ca-role-request` | 2 | `http://localhost:8080/page/ca-role-request` |

## 3. 직전 조회(`CS_MNG_01` · `CS_COMMON` 2개)와 다른 점

| 항목 | 직전(2개 role) | 이번(CS_COMMON 만) |
|---|---:|---:|
| 하자 화면 | 59 | 0 |
| 개인 화면 | 26 | 26 |

- 이번에 안 보이는 화면: 하자 탭 59개 전부, 개인 탭 0개
- 이번에 새로 보이는 화면: 없음
- 개인 탭 등급이 달라진 화면: 없음
- 같은 `h.dev01` 에서 `CS_MNG_01` 을 뺀 것이 유일한 변경이라면 하자 탭 59개는 `CS_MNG_01` 이 열어 주던 몫으로 읽을 수 있다. 그 사이 다른 설정이 바뀌었는지는 확인하지 않았다.

## 4. 한계

- 화면 이름·순서·serviceId·등급은 화면이 쓰는 메뉴 데이터 그대로다. 메뉴 API 응답과 직접 대조하지는 않았다.
- 메뉴 노출만 본 결과다. 화면 안 버튼, 데이터 범위(법인·현장)는 이 문서에 없다.
- 화면 우상단의 「부서 / 권한 전환」은 열지 않았고, 현재 선택된 부서·권한 기준으로 읽었다.
