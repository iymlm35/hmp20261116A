# erp-h 권한 프로그램 ↔ DB 테이블 관계 다이어그램 (공통 탭 · 시스템 탭)

> 작성 2026-11-16 · 근거: `erph-role-program-db-tables.md`(백엔드 소스 기준 정리) · 소스 `platform-hmp`(hmp-core) · erp-h `co_mobileAuthMapp`
> Mermaid 다이어그램이다. VS Code 는 기본 미리보기에서 그리지 않으므로 Mermaid 지원 확장(예: Markdown Preview Mermaid Support)이나 GitHub·GitLab 에서 본다.
> 소스를 읽어 만든 것이고 DB 를 조회해 확인하지 않았다. 표의 FK 는 확인하지 못해 엔티티 매핑 기준으로 그렸다.

## 1. 메뉴 화면 → 프로그램: 두 탭이 같은 프로그램을 공유한다

공통 탭의 화면 9개와 시스템 탭의 `sys-*` 별칭 9개가 **프로그램 8개**를 함께 쓴다. 사용자권한 매핑 두 화면(`byRole`/`byUser`)은 프로그램 `user-role` 하나다.

```mermaid
flowchart LR
  subgraph C["공통 탭 · 공통 › 권한"]
    direction TB
    c1["ca-role<br/>권한 관리"]
    c2["ca-role-common<br/>공통권한 관리"]
    c3["ca-role-rule<br/>발령권한 관리"]
    c4["ca-user-role<br/>권한/사용자매핑"]
    c5["ca-user-role-by-user<br/>사용자/권한매핑"]
    c6["ca-role-assign<br/>지정권한 관리"]
    c7["ca-role-delegation<br/>권한위임 관리"]
    c8["ca-user-role-inquiry<br/>사용자권한 조회"]
    c9["co_mobileAuthMapp<br/>사용자 모바일"]
  end

  subgraph P["공유 프로그램 · 화면 소스 하나"]
    direction TB
    p1["role"]
    p2["role-common"]
    p3["role-rule"]
    p4["user-role<br/>mode byRole / byUser"]
    p5["role-assign"]
    p6["role-delegation"]
    p7["user-role-inquiry"]
    p8["mobileAuthMapp<br/>erp-h 소스"]
  end

  subgraph S["시스템 탭 · 시스템 › 권한 · sys- 별칭"]
    direction TB
    s1["sys-ca-role"]
    s2["sys-ca-role-common"]
    s3["sys-ca-role-rule"]
    s4["sys-ca-user-role"]
    s5["sys-ca-user-role-by-user"]
    s6["sys-ca-role-assign"]
    s7["sys-ca-role-delegation"]
    s8["sys-ca-user-role-inquiry"]
    s9["sys-co_mobileAuthMapp"]
  end

  c1 --> p1
  c2 --> p2
  c3 --> p3
  c4 --> p4
  c5 --> p4
  c6 --> p5
  c7 --> p6
  c8 --> p7
  c9 --> p8
  s1 --> p1
  s2 --> p2
  s3 --> p3
  s4 --> p4
  s5 --> p4
  s6 --> p5
  s7 --> p6
  s8 --> p7
  s9 --> p8
```

- 별칭은 원본의 `program_id` 를 복사하고 `params` 에 `sharedProgram:true` 를 더한 행이다.
- 같은 프로그램이라 부르는 API · 서비스 · 쿼리 · 표가 모두 같다(아래 2·3절).

## 1-1. 테이블을 가운데 두고 보기: 왼쪽 시스템 탭 · 오른쪽 공통 탭

권한 관련 테이블 9개를 가운데 두고, 왼쪽에 시스템 탭의 `sys-*` 화면, 오른쪽에 공통 탭의 화면을 놓았다. 두 쪽의 같은 화면이 **같은 테이블에 같은 방식으로 연결**된다.

- 굵은 선 `===` : 쓰기(INSERT·UPDATE·DELETE) · 점선 `-.-` : 읽기(SELECT)
- 가운데 표는 이 9개 프로그램이 읽거나 쓰는 권한 관련 표만 골랐다. 기준·설정 표(`biz_group` · `config` · `code` …)와 사용자·조직 표는 4절에 있다.
- `role_service` · `service` 는 이 프로그램들이 읽기만 한다(실효 메뉴 계산). 쓰는 화면은 「권한/메뉴매핑」이다.

```mermaid
flowchart LR
  subgraph SYS["시스템 탭 · 시스템 › 권한 · sys- 별칭"]
    direction TB
    s_role["sys-ca-role<br/>권한 관리"]
    s_common["sys-ca-role-common<br/>공통권한 관리"]
    s_rule["sys-ca-role-rule<br/>발령권한 관리"]
    s_urole["sys-ca-user-role<br/>권한/사용자매핑"]
    s_ubyuser["sys-ca-user-role-by-user<br/>사용자/권한매핑"]
    s_assign["sys-ca-role-assign<br/>지정권한 관리"]
    s_deleg["sys-ca-role-delegation<br/>권한위임 관리"]
    s_inq["sys-ca-user-role-inquiry<br/>사용자권한 조회"]
    s_mob["sys-co_mobileAuthMapp<br/>사용자 모바일"]
  end

  subgraph TBL["권한 관련 테이블 · 스키마 HMP"]
    direction TB
    t_role["role<br/>역할 정의"]
    t_role_i18n["role_i18n<br/>역할 이름 다국어"]
    t_role_condition["role_condition<br/>발령 조건"]
    t_user_role["user_role<br/>사용자 역할 부여"]
    t_role_delegation["role_delegation<br/>권한 위임"]
    t_role_service["role_service<br/>역할 메뉴 부여"]
    t_service["service<br/>메뉴 · API"]
    t_log_role["log_role<br/>역할 정의 이력"]
    t_log_role_grant["log_role_grant<br/>부여 · 회수 이력"]
  end

  subgraph COM["공통 탭 · 공통 › 권한"]
    direction TB
    c_role["ca-role<br/>권한 관리"]
    c_common["ca-role-common<br/>공통권한 관리"]
    c_rule["ca-role-rule<br/>발령권한 관리"]
    c_urole["ca-user-role<br/>권한/사용자매핑"]
    c_ubyuser["ca-user-role-by-user<br/>사용자/권한매핑"]
    c_assign["ca-role-assign<br/>지정권한 관리"]
    c_deleg["ca-role-delegation<br/>권한위임 관리"]
    c_inq["ca-user-role-inquiry<br/>사용자권한 조회"]
    c_mob["co_mobileAuthMapp<br/>사용자 모바일"]
  end

  %% sys
  s_role === t_role
  s_role === t_role_i18n
  s_role === t_log_role
  s_role -.- t_user_role
  s_common === t_role
  s_common === t_role_i18n
  s_common === t_log_role
  s_common -.- t_user_role
  s_rule === t_role
  s_rule === t_role_i18n
  s_rule === t_role_condition
  s_rule === t_log_role
  s_rule -.- t_user_role
  s_urole === t_user_role
  s_urole === t_log_role_grant
  s_urole -.- t_role
  s_urole -.- t_role_service
  s_urole -.- t_service
  s_ubyuser === t_user_role
  s_ubyuser === t_log_role_grant
  s_ubyuser -.- t_role
  s_ubyuser -.- t_role_service
  s_ubyuser -.- t_service
  s_assign === t_role
  s_assign === t_role_i18n
  s_assign === t_log_role
  s_assign === t_user_role
  s_assign === t_log_role_grant
  s_deleg === t_role_delegation
  s_deleg === t_log_role_grant
  s_deleg -.- t_user_role
  s_inq -.- t_user_role
  s_inq -.- t_role
  s_inq -.- t_role_delegation
  s_inq -.- t_role_service
  s_inq -.- t_service
  s_mob === t_user_role
  s_mob === t_log_role_grant
  %% com
  t_role === c_role
  t_role_i18n === c_role
  t_log_role === c_role
  t_user_role -.- c_role
  t_role === c_common
  t_role_i18n === c_common
  t_log_role === c_common
  t_user_role -.- c_common
  t_role === c_rule
  t_role_i18n === c_rule
  t_role_condition === c_rule
  t_log_role === c_rule
  t_user_role -.- c_rule
  t_user_role === c_urole
  t_log_role_grant === c_urole
  t_role -.- c_urole
  t_role_service -.- c_urole
  t_service -.- c_urole
  t_user_role === c_ubyuser
  t_log_role_grant === c_ubyuser
  t_role -.- c_ubyuser
  t_role_service -.- c_ubyuser
  t_service -.- c_ubyuser
  t_role === c_assign
  t_role_i18n === c_assign
  t_log_role === c_assign
  t_user_role === c_assign
  t_log_role_grant === c_assign
  t_role_delegation === c_deleg
  t_log_role_grant === c_deleg
  t_user_role -.- c_deleg
  t_user_role -.- c_inq
  t_role -.- c_inq
  t_role_delegation -.- c_inq
  t_role_service -.- c_inq
  t_service -.- c_inq
  t_user_role === c_mob
  t_log_role_grant === c_mob
```

읽는 법
- 왼쪽과 오른쪽에서 같은 이름의 화면(예: `sys-ca-role` ↔ `ca-role`)은 **선이 닿는 표가 같다.** 이것이 「같은 테이블 · 같은 쿼리」의 그림이다.
- 다른 것은 선이 아니라 화면 쪽 노드가 받은 `role_service` 부여다(2절). 선 자체는 두 쪽이 같아도 계정에 따라 왼쪽만 또는 오른쪽만 열릴 수 있다.
- `user_role` 에 굵은 선이 모이는 화면: `ca-user-role` · `ca-user-role-by-user` · `ca-role-assign` · `co_mobileAuthMapp`. `role` 에 굵은 선이 모이는 화면: `ca-role` · `ca-role-common` · `ca-role-rule` · `ca-role-assign`.
- 가운데 선이 가장 많은 화면은 `ca-role-assign`(쓰기 5)과 `ca-user-role-inquiry`(읽기 5)다.

## 2. 접근 판정: 쿼리는 같고 허용 여부만 serviceId 별로 다르다

```mermaid
sequenceDiagram
  autonumber
  participant U as 로그인 계정
  participant G as 화면 JS 공유
  participant C as RoleManageController
  participant RS as role_service 부여
  participant Q as 서비스 메서드 · 쿼리 공유

  U->>G: 공통 탭 ca-role 또는 시스템 탭 sys-ca-role 을 연다
  G->>C: API 호출 + 헤더 X-Service-Id = 열린 화면의 serviceId
  C->>C: 플랫폼 관리자 · 시스템 관리자면 판정 통과
  C->>RS: 계정의 role 들이 그 serviceId 에 받은 등급 최댓값
  RS-->>C: 예 ca-role 등급 3 / sys-ca-role 부여 없음
  alt 필요한 등급 이상
    C->>Q: 같은 메서드 · 같은 표 호출
    Q-->>G: 결과
  else 등급 부족
    C-->>G: 거부 · 쿼리 실행 안 함
  end
```

- 판정 기준은 **호출한 화면의 serviceId** 이고 쿼리 파라미터가 아니다. 쿼리 파라미터는 로그인 정보(법인 범위 · 시스템 ID)와 화면 `params`(`mode` · `systemScope`)로 정해진다.
- `AL0` 표시가 붙은 읽기 API 는 이 판정 대신 메서드 안의 역할 관리자 판정을 쓴다.

## 3. 프로그램 → 테이블 쓰기 관계

굵은 선은 INSERT·UPDATE·DELETE 이다. 조회 전용 프로그램(`user-role-inquiry`)은 쓰는 표가 없다.

```mermaid
flowchart LR
  subgraph PRG["프로그램 · 두 탭 공유"]
    direction TB
    p1["role · role-common"]
    p3["role-rule"]
    p4["user-role<br/>byRole · byUser"]
    p5["role-assign"]
    p6["role-delegation"]
    p8["mobileAuthMapp"]
    p7["user-role-inquiry<br/>조회 전용"]
  end

  subgraph BG["백그라운드 · 화면 아님"]
    direction TB
    j1["RoleRuleServiceImpl<br/>발령 자동 부여 ROLE_RULE_SYNC"]
    j2["RoleExpiryService<br/>종료일 만료 ROLE_EXPIRY"]
    j3["RoleDelegationExpireJob<br/>위임 만료"]
  end

  subgraph TBL["쓰는 표 · 스키마 HMP"]
    direction TB
    t_role["role"]
    t_i18n["role_i18n"]
    t_cond["role_condition"]
    t_ur["user_role"]
    t_del["role_delegation"]
    t_lr["log_role"]
    t_lg["log_role_grant"]
  end

  p1 ==> t_role
  p1 ==> t_i18n
  p1 ==> t_lr
  p3 ==> t_role
  p3 ==> t_i18n
  p3 ==> t_cond
  p3 ==> t_lr
  p4 ==> t_ur
  p4 ==> t_lg
  p5 ==> t_role
  p5 ==> t_i18n
  p5 ==> t_lr
  p5 ==> t_ur
  p5 ==> t_lg
  p6 ==> t_del
  p6 ==> t_lg
  p8 ==> t_ur
  p8 ==> t_lg
  j1 ==> t_ur
  j1 ==> t_lg
  j2 ==> t_ur
  j3 ==> t_del
```

- `user_role` 에 쓰는 경로는 이 외에 권한 신청 승인(`ca-role-request`, 이 문서 범위 밖)도 있다.
- 부여·회수·위임 이력(`log_role_grant`)과 역할 정의 이력(`log_role`)은 별도 트랜잭션(`logTx`)으로 저장된다.

## 4. 프로그램 → 테이블 읽기 관계 (표를 묶어서)

점선은 SELECT 이다. 쓰는 프로그램도 읽기 대상이 많아 표를 성격별로 묶었다.

```mermaid
flowchart LR
  subgraph PRG["프로그램"]
    direction TB
    r1["role"]
    r2["role-common"]
    r3["role-rule"]
    r4["user-role"]
    r5["role-assign"]
    r6["role-delegation"]
    r7["user-role-inquiry"]
    r8["mobileAuthMapp"]
  end

  subgraph G1["역할 · 부여 표"]
    direction TB
    g1a["role"]
    g1b["user_role"]
    g1c["role_delegation"]
  end

  subgraph G2["메뉴 부여 표 · 실효 메뉴 계산"]
    direction TB
    g2a["role_service"]
    g2b["service"]
  end

  subgraph G3["기준 · 설정 표"]
    direction TB
    g3a["biz_group"]
    g3b["config<br/>SECURITY.role-group.hq · site"]
    g3c["sod_rule"]
    g3d["code · code_group"]
    g3e["system_language"]
  end

  subgraph G4["사용자 · 조직 표"]
    direction TB
    g4a["hmp_user · user_i18n"]
    g4b["dept · org_unit<br/>이름 풀이"]
    g4c["employee · employee_dept<br/>org_assignment · user_company"]
  end

  r1 -.-> g1b
  r1 -.-> g3a
  r1 -.-> g3e
  r2 -.-> g1b
  r2 -.-> g3a
  r3 -.-> g1b
  r3 -.-> g3a
  r3 -.-> g3d
  r3 -.-> g3e
  r3 -.-> g4b
  r4 -.-> g1a
  r4 -.-> g1b
  r4 -.-> g2a
  r4 -.-> g2b
  r4 -.-> g3a
  r4 -.-> g3b
  r4 -.-> g3c
  r4 -.-> g4a
  r4 -.-> g4b
  r5 -.-> g1b
  r5 -.-> g3a
  r5 -.-> g3b
  r5 -.-> g3c
  r5 -.-> g3e
  r5 -.-> g4a
  r5 -.-> g4b
  r6 -.-> g1b
  r6 -.-> g3d
  r7 -.-> g1a
  r7 -.-> g1b
  r7 -.-> g1c
  r7 -.-> g2a
  r7 -.-> g2b
  r7 -.-> g3a
  r7 -.-> g4a
  r7 -.-> g4b
  r8 -.-> g1b
  r8 -.-> g4a
  r8 -.-> g4b
  r8 -.-> g4c
```

## 5. 표 사이의 관계 (엔티티 매핑 기준)

`Role` 엔티티의 `i18ns` · `conditions` · `roleServices` 는 `cascade = ALL, orphanRemoval = true` 이다. `user_role` · `role_delegation` 은 역할 쪽에서 연쇄되지 않고 `role` 을 가리키기만 한다(`@ManyToOne`).

```mermaid
erDiagram
  role ||--o{ role_i18n : "연쇄 삭제"
  role ||--o{ role_condition : "연쇄 삭제"
  role ||--o{ role_service : "연쇄 삭제"
  role ||--o{ user_role : "참조만 · 삭제 시 남음"
  role ||--o{ role_delegation : "참조만"
  service ||--o{ role_service : "메뉴 · API 부여"
  hmp_user ||--o{ user_role : "계정에 부여"
  user_role }o..o{ log_role_grant : "이력 · FK 아님"
  role_delegation }o..o{ log_role_grant : "이력 · FK 아님"
  role }o..o{ log_role : "이력 · FK 아님"

  role {
    string system_id PK
    string tenant_id PK
    string role_id PK
    string role_type "ASSIGN COMMON RULE"
    string is_biz_admin
    string is_system_admin
    string use_yn
  }
  role_i18n {
    string system_id PK
    string role_id PK
    string language PK
  }
  role_condition {
    string system_id PK
    string role_id PK
    int condition_seq PK
  }
  user_role {
    string system_id PK
    string user_id PK
    string role_id PK
    string org_type PK
    string org_id PK
    date start_date PK
    date end_date
    string grant_type "ASSIGN RULE"
  }
  role_service {
    string system_id PK
    string role_id PK
    string service_id PK
    int auth_level "1 2 3"
    string data_scope
  }
  role_delegation {
    string delegation_id PK
    string role_id
    string status
  }
```

- 점선 관계(`log_*`)는 이력 기록이며 DB 가 외래키로 묶는지는 확인하지 못했다.
- `role_service` 는 이 9개 프로그램이 직접 쓰지 않는다. 쓰는 화면은 「메뉴·업무그룹 › 권한/메뉴매핑」(`ca-role-service`)이고, 이 문서의 대상에서 뺐다.
- **역할을 삭제하면** `role_i18n` · `role_condition` · `role_service` 가 함께 지워지고 `user_role` 은 남는다. 삭제 전에 `user_role` 을 정리하는 것이 안전하다.

## 6. 정리

| 질문 | 답 |
|---|---|
| 공통 탭과 시스템 탭이 같은 표를 쓰는가 | 같다. 같은 프로그램 · 같은 API · 같은 쿼리 |
| 두 탭의 차이 | 호출 화면 serviceId 에 받은 `role_service` 부여(허용 판정)와 화면 `params` |
| 쓰기가 가장 많은 프로그램 | `ca-role-assign` — 역할 정의 4표 + 부여 2표 |
| 쓰기가 없는 프로그램 | `ca-user-role-inquiry` |
| `user_role` 을 쓰는 프로그램 | `user-role`(두 화면) · `role-assign` · `mobileAuthMapp` + 백그라운드 `RoleRuleServiceImpl` · `RoleExpiryService` |
| 이 9개가 쓰지 않는 관련 표 | `role_service`(권한/메뉴매핑 화면) · `role_request`·`role_request_target`(권한 신청) · `log_role_service` |

## 7. 확인하지 못한 것

- 실제 DB 의 FK·인덱스. 5절의 관계선은 엔티티의 `@OneToMany`/`@ManyToOne` 매핑이다.
- `role_delegation` 의 컬럼 이름(`delegation_id` 등)은 정확한 PK 칸을 확인하지 않고 단순화했다.
- 별칭 `sys-*` 의 `service.params` 값이 원본과 같은지.
- 4절의 `user_role` · `role` 읽기 중 일부(법인 범위 검사 등)는 하부 쿼리를 따라가지 않아 생략했다.
