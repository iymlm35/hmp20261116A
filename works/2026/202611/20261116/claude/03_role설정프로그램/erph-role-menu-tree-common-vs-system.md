# erp-h 「공통 › 권한」 ↔ 「시스템 › 권한」 메뉴트리 비교

> 작성 2026-11-16 · 로그인 계정 erpadmin 으로 읽은 메뉴 API(`POST /sys/menu/list`)의 트리 + dev01 의 같은 트리 · 폴더 키는 `[ ]` 안, 화면은 serviceId
> 범위: 「권한」 폴더만. 「메뉴·업무그룹」 폴더는 다루지 않는다.
> 화면을 열어 본 것은 아니다. DB 를 읽지도 않았다.

## 1. 「권한」 폴더

### 트리 (나란히)

**공통**

```text
권한/  [MENU_BC_ROLE_GRP]
├── 권한 정의/  [MENU_CA_ROLE]
│   ├── 권한 관리  `ca-role`
│   ├── 공통권한 관리  `ca-role-common`
│   └── 발령권한 관리  `ca-role-rule`
└── 권한 부여/  [MENU_BC_ROLE_OPS]
    ├── 권한/사용자매핑  `ca-user-role`
    ├── 사용자/권한매핑  `ca-user-role-by-user`
    ├── 지정권한 관리  `ca-role-assign`
    ├── 권한위임 관리  `ca-role-delegation`
    ├── 사용자(모바일)  `co_mobileAuthMapp`
    └── 사용자권한 조회  `ca-user-role-inquiry`
```

**시스템**

```text
권한/  [MENU_SC_ROLE]
├── 권한 정의/  [MENU_SC_ROLE_DEF]
│   ├── 권한 관리  `sys-ca-role`
│   ├── 공통권한 관리  `sys-ca-role-common`
│   └── 발령권한 관리  `sys-ca-role-rule`
└── 권한 부여/  [MENU_SC_ROLE_GRANT]
    ├── 권한/사용자매핑  `sys-ca-user-role`
    ├── 사용자/권한매핑  `sys-ca-user-role-by-user`
    ├── 지정권한 관리  `sys-ca-role-assign`
    ├── 권한위임 관리  `sys-ca-role-delegation`
    ├── 사용자(모바일)  `sys-co_mobileAuthMapp`
    └── 사용자권한 조회  `sys-ca-user-role-inquiry`
```

### 노드 대응표

| 깊이 | 공통 (이름 · 키) | 시스템 (이름 · 키) | 서로 다른 점 |
|---:|---|---|---|
| 0 | 권한 · `MENU_BC_ROLE_GRP` | 권한 · `MENU_SC_ROLE` | 폴더 키만 다름 |
| 1 | 권한 정의 · `MENU_CA_ROLE` | 권한 정의 · `MENU_SC_ROLE_DEF` | 폴더 키만 다름 |
| 2 | 권한 관리 · `ca-role` | 권한 관리 · `sys-ca-role` | 키만 다름(`sys-` 별칭) |
| 2 | 공통권한 관리 · `ca-role-common` | 공통권한 관리 · `sys-ca-role-common` | 키만 다름(`sys-` 별칭) |
| 2 | 발령권한 관리 · `ca-role-rule` | 발령권한 관리 · `sys-ca-role-rule` | 키만 다름(`sys-` 별칭) |
| 1 | 권한 부여 · `MENU_BC_ROLE_OPS` | 권한 부여 · `MENU_SC_ROLE_GRANT` | 폴더 키만 다름 |
| 2 | 권한/사용자매핑 · `ca-user-role` | 권한/사용자매핑 · `sys-ca-user-role` | 키만 다름(`sys-` 별칭) |
| 2 | 사용자/권한매핑 · `ca-user-role-by-user` | 사용자/권한매핑 · `sys-ca-user-role-by-user` | 키만 다름(`sys-` 별칭) |
| 2 | 지정권한 관리 · `ca-role-assign` | 지정권한 관리 · `sys-ca-role-assign` | 키만 다름(`sys-` 별칭) |
| 2 | 권한위임 관리 · `ca-role-delegation` | 권한위임 관리 · `sys-ca-role-delegation` | 키만 다름(`sys-` 별칭) |
| 2 | 사용자(모바일) · `co_mobileAuthMapp` | 사용자(모바일) · `sys-co_mobileAuthMapp` | 키만 다름(`sys-` 별칭) |
| 2 | 사용자권한 조회 · `ca-user-role-inquiry` | 사용자권한 조회 · `sys-ca-user-role-inquiry` | 키만 다름(`sys-` 별칭) |

- 화면: 공통 9개 · 시스템 9개 / 하위 폴더: 공통 2개 · 시스템 2개

## 2. 비교 결과 정리

| 비교 항목 | 결과 |
|---|---|
| 폴더 구조 | **같다** — 「권한」 아래 「권한 정의」(3화면)와 「권한 부여」(6화면), 같은 깊이·같은 순서 |
| 화면 구성 | **같다** — 9개 화면이 같은 이름·같은 순서로 대응 |
| 폴더 키 | 다르다 — 공통 `MENU_BC_ROLE_GRP` · `MENU_CA_ROLE` · `MENU_BC_ROLE_OPS` ↔ 시스템 `MENU_SC_ROLE` · `MENU_SC_ROLE_DEF` · `MENU_SC_ROLE_GRANT` (공통은 `BC`/`CA`, 시스템은 `SC` 접두) |
| 화면 serviceId | 다르다 — 시스템은 공통 id 앞에 `sys-` (`ca-role` ↔ `sys-ca-role`) |
| 등급 (erpadmin · dev01) | 같다 — erpadmin 3 · dev01 1 |
| 기능 허용 (업로드·엑셀·다운로드·인쇄) | 같다 — 9개 화면 모두 양쪽 동일 |

### 알아 둘 것
- **공통 「권한 정의」의 폴더 키 `MENU_CA_ROLE`** 는 이전 설계서(`S6-menu`)에서 「권한 운영」 폴더 이름이었다. 지금은 「권한 정의」로 쓰이고, 이전 안내에 적은 「공통 › 권한 운영」은 현재 메뉴에 없다. 이름과 키가 일치하지 않는 이유는 확인하지 않았다.
- **「권한」 폴더에 「권한·겸직 신청」(`ca-role-request`)은 없다.** 개인 메뉴(`개인 › 내 요청`)로 옮겨져 있다.
- 두 트리는 같은 화면 소스를 가리키는 별칭이라(`sharedProgram`) 메뉴 구조를 바꿀 때 두 쪽을 따로 고쳐야 한다. 한쪽만 바꾸면 트리가 어긋난다.

## 3. 확인하지 못한 것

- 폴더(`MENU_*`)도 서비스 행이라 폴더 자체에 부여가 필요한지, 하위 화면 부여만으로 폴더가 보이는지는 확인하지 못했다.
- 두 탭의 같은 화면을 열어 동작을 비교하지 않았다.
- dev01 로 읽은 트리와 erpadmin 트리는 같은 구조였다(등급만 다름). 다른 계정(csadm 등)에는 두 트리가 모두 없다.
