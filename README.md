# Board Project
일상생활을 공유하는 자유로운 게시판입니다. <br/>
이 프로젝트는 Java와 Vue.js에 대한 이해를 높이고, 관련 기술을 학습한 후 실제로 적용해보고자 시작하게 되었습니다. 

![Image](https://github.com/user-attachments/assets/53073f40-7ec9-44a9-ac43-ca7f690ca894)


## 📚 목차
- [사용 기술](#1-사용-기술)
  - [백엔드](#1-1-백엔드)
  - [프론트엔드](#1-2-프론트엔드)
- [구조 및 설계](#2-구조-및-설계)
  - [인프라 구조](#2-1-인프라-구조)
  - [DB 설계](#2-2-DB-설계)
- [실행 화면](#3-실행-화면)
- [느낀점](#4-느낀점)
<br/>

## 📖 들어가며
### 1. 사용 기술
#### 1-1. 백엔드 
- 주요 프레임워크/라이브러리
  - Java 21
  - Spring Boot 3.4.0
  - Spring Data JPA
  - QueryDSL 5.0.0
  - Spring Security
  - JWT (java-jwt)
  - WebSocket
  - Lombok
  - AWS SDK
- Build Tool
  - gradle 8.11.1
- Database
  - Mysql
  - Redis
#### 1-2. 프론트엔드  
- Vue 3.2.13
- Pinia 4.0.3
- Element Plus 2.8.8
- jsonwebtoken 9.0.2 / jwt-decode 4.0.0  
<br/>

### 2. 구조 및 설계
#### 2-1. 인프라 구조
![인프라구조](https://github.com/user-attachments/assets/cf7ed342-2e43-45b8-8143-4c069cc88963)

#### 2-2. DB 설계 
![erd](https://github.com/user-attachments/assets/633ef676-0046-4201-bc9d-01e4eabba831)

**board(게시글)**
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| system_no | varchar(100) | PK | 게시글 System No. |
| title | varchar(100) | Not Null | 제목 |
| content | text | Not Null | 내용 |
| view | int unsigned | default 0 | 조회수 |
| img_path | text |  | 이미지 경로 |
| id | varchar(100) | Not Null | 작성자 Id |
| user_system_no | varchar(100) | FK | 작성자 System No. |
| create_date | datetime | Not Null | 생성일자 |
| modify_date | datetime | Not Null | 수정일자 |

**role(역할)**
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| system_no | varchar(100) | PK | 역할 System No. |
| role | varchar(100) | Not Null | 역할 |
| create_date | datetime | Not Null | 생성일자 |
| modify_date | datetime | Not Null | 수정일자 |

**user(회원)**
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| system_no | varchar(100) | PK | 회원 System No. |
| name | varchar(20) | Not Null | 이름 |
| email | varchar(200) | Not Null | 이메일 |
| phone | varchar(100) |  | 전화번호 |
| id | varchar(100) | Not Null | 회원 Id |
| password | varchar(100) | Not Null | 비밀번호 |
| role_sysem_no | varchar(100) | FK | 역할 System No. |
| create_date | datetime | Not Null | 생성일자 |
| modify_date | datetime | Not Null | 수정일자 |

**comment(댓글)**
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| system_no | varchar(100) | PK | 댓글 System No. |
| comment | text | Not Null | 댓글 |
| id | varchar(100) | Not Null | 작성자 Id |
| user_system_no | varchar(100) | FK | 작성자 System no. |
| parent_system_no | varchar(100) |  | 상위 댓글 System No. |
| board_system_no | varchar(100) | FK | 게시글 System No. |
| create_date | datetime | Not Null | 생성일자 |
| modify_date | datetime | Not Null | 수정일자 |

**likelog(좋아요 로그)**
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| system_no | varchar(100) | PK | 좋아요 System No. |
| id | varchar(100) | Not Null | 작성자 Id |
| user_system_no | varchar(100) | FK | 작성자 System no. |
| board_system_no | varchar(100) | FK | 게시글 System No. |
| create_date | datetime | Not Null | 생성일자 |
| modify_date | datetime | Not Null | 수정일자 |

**notification(알림)**
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| system_no | varchar(100) | PK | 알림 System No. |
| read_flag | tinyint(1) | default 0 |  |
| id | varchar(100) | Not Null | 알림 대상자 Id |
| user_system_no | varchar(100) | FK | 알림 대상자 System no. |
| board_system_no | varchar(100) | FK | 게시글 System No. |
| title | varchar(100) | Not Null | 게시글 제목 |
| create_date | datetime | Not Null | 생성일자 |
| modify_date | datetime | Not Null | 수정일자 |
<br/>

### 3. 실행 화면
<!-- 게시글 토글 -->
<details>
<summary>게시글 관련 화면</summary>
  
**1. 게시글 목록**
   - 제목, 내용, 작성자는 돋보기🔍 아이콘을 이용하여 검색할 수 있다.
   - 한 페이지 당 10개씩 조회된다.<br/>

  *1-1. 전체 게시글 목록*
  ![전체 게시글 목록](https://github.com/user-attachments/assets/53073f40-7ec9-44a9-ac43-ca7f690ca894)
  - 최신 생성 순서대로 전체 게시글 목록을 조회한다. <br>
  
  *1-2. 조회수 Top 게시글 목록*
  ![좋아요Top게시글목록](https://github.com/user-attachments/assets/b4cbb4d4-2ab1-4c34-819d-062849028068)
  - 상단 [조회수 TOP] 메뉴 클릭
  - 조회수가 높은 순서대로 게시글 목록을 조회한다.

  *1-3. 좋아요 Top 게시글 목록*
  ![조회수Top게시글목록](https://github.com/user-attachments/assets/effba0b1-a093-49be-8a3c-e9d488ea2ad0)
  - 상단 [좋아요 TOP] 메뉴 클릭
  - 좋아요 수가 높은 순서대로 게시글 목록을 조회한다.

  *1-4. 내 게시글 목록*
  ![profile](https://github.com/user-attachments/assets/c57f9285-ec72-4c8c-a534-9ee4c89aaf67)
  ![내게시글목록](https://github.com/user-attachments/assets/89228019-831b-470e-83eb-3f2f9baa0862)
  - 상단 [ID] 메뉴 클릭 → [내 게시글 관리] 클릭
  - 계정 사용자가 작성한 게시글 목록이 조회된다.

  *1-5. 내 좋아요 게시글 목록*
  ![profile](https://github.com/user-attachments/assets/c57f9285-ec72-4c8c-a534-9ee4c89aaf67)
  ![내좋아요게시글목록](https://github.com/user-attachments/assets/630d26d4-2b19-48bf-ac0b-52afa8b88fe3)
  - 상단 [ID] 메뉴 클릭 → [내 좋아요 관리] 클릭
  - 계정 사용자가 좋아요를 누른 게시글 목록이 조회된다.

**2. 게시글 생성**
<p align="left">
  <img src="https://github.com/user-attachments/assets/d83a6500-73c8-4273-a8ce-67efe1f91a0f" width="750" alt="게시글생성">
</p>

- 이미지는 AWS S3에 저장된다.

**3. 게시글 상세보기**
<p align="left">
  <img src="https://github.com/user-attachments/assets/38c49ee3-abc4-480c-af10-d668547d31ae" width="750" alt="게시글상세보기">
</p>

- 게시글에 좋아요 버튼을 누를 수 있다.
- 게시글 작성자인 경우, 수정 버튼을 통해 게시글을 수정할 수 있다. (작성자가 아닌 경우, ‘작성자와 일치하지 않습니다.’ 창이 뜬다.)

**4. 게시글 수정**
<p align="left">
  <img src="https://github.com/user-attachments/assets/c01655d2-c658-43fa-b8ec-9cc2b9ef7b8f" width="750" alt="게시글수정">
</p>

**5. 게시글 삭제**
<p align="left">
  <img src="https://github.com/user-attachments/assets/50e37fcc-8c23-47da-a838-fc05658e5302" width="750" alt="게시글삭제">
</p>

- [내 게시글 목록]에서 CheckBox를 이용해 게시글을 다중 삭제할 수 있다.

**6. 좋아요 취소**
<p align="left">
  <img src="https://github.com/user-attachments/assets/63a25bee-d8b3-4ddb-b954-1f517ee80267" width="750" alt="좋아요취소">
</p>

- [내 좋아요 목록]에서 CheckBox를 이용해 좋아요를 다중 취소할 수 있다. 
</details>

<!-- 회원 토글 -->
<details>
<summary>회원 관련 화면</summary>
  
  **1. 회원가입**
  <p align="left">
  <img src="https://github.com/user-attachments/assets/ccfc9130-7ca0-42ca-bba6-22cdc8ad27c5" width="400" alt="회원가입">
  </p>
  
  - 이메일은 중복될 수 없다.
  
  **2. 로그인**
  <p align="left">
  <img src="https://github.com/user-attachments/assets/f1a3a629-0a2a-44f0-b345-5811cdd28a9e" width="400" alt="로그인">
  </p>
  
  - JWT와 Spring Security를 사용하여 로그인을 검증한다.

  **3. 아이디/비밀번호 찾기**
  <p align="left">
  <img src="https://github.com/user-attachments/assets/b1394d26-1144-451f-abe4-5773b95f94f0" width="400" alt="비밀번호찾기"><br/>
  <img src="https://github.com/user-attachments/assets/79861a59-cac9-4b6d-835e-30ce8a0eb819" width="400" alt="아이디찾기">
  </p>

   **4. 회원 정보 상세보기/수정**
  <p align="left">
  <img src="https://github.com/user-attachments/assets/bcc7dbd6-3bac-4b9a-9001-ba2c302d7460" width="400" alt="회원정보상세보기">
  </p>

   **5. 비밀번호 변경**
  <p align="left">
  <img src="https://github.com/user-attachments/assets/1e7d20ea-8b16-42a6-9de6-19ccfec69690" width="400" alt="비밀번호변경">
  </p>
</details>

<!-- 댓글 토글 -->
<details>
<summary>댓글 관련 화면</summary>
  
  **1. 댓글 조회/생성**
  <p align="left">
  <img src="https://github.com/user-attachments/assets/60f237ea-cd55-48e8-a093-5befb0e2179e" width="700" alt="댓글조회및생성">
  </p>

  - [게시글 상세보기] 하단에서 댓글을 등록할 수 있다.
  - 댓글에 대한 답글을 등록할 수 있다.이메일은 중복될 수 없다. 
</details>

<!-- 알림 토글 -->
<details>
<summary>알림 관련 화면</summary>
  
  **1. 알림 목록**
  ![알림 목록](https://github.com/user-attachments/assets/8a250a1c-dd98-489a-aaa9-e8c93126e5cf)

  - 다른 사용자가 게시글에 댓글을 남기면, 게시글 작성자에게 알림이 전송된다.
  - 알림을 클릭하면 해당 [게시글 상세보기]로 이동한다.
</details>
<br/>

## 💡 마치며
### 4. 느낀점
Spring Boot와 Vue를 사용해 웹 애플리케이션을 개발을 통해, JPA 기반의 데이터 처리 방식에 대해 고민할 수 있었습니다.

특히 “어디까지 DTO로 분리할 것인가”에 대한 고민이 많았습니다. Mybatis를 사용할 때는 SQL을 중심으로 데이터를 처리했기 때문에 DTO는 요청과 응답에서만 사용하는 구조였고, 역할의 경계도 명확했습니다. 반면, JPA는 Entity를 중심으로 동작하다 보니, 어떤 데이터를 Entity로 처리하고 어떤 시점에 DTO로 변환할지에 대한 기준을 명확히 세워야 했습니다. 간단한 로직에서는 Entity로 데이터를 조회한 뒤 DTO로 변환하여 반환했고, 특정 필드만 필요하거나 계산이 포함된 경우에는 QueryDSL의 Projections 기능으로 DTO를 직접 조회하였습니다. 이와 같이 요청과 응답에는 DTO를 사용하고, 내부 로직에서는 Entity를 활용하는 식으로 책임을 분리하며, 코드의 재사용성과 유지보수성을 높이고자 노력했습니다.

또한, 체이닝 방식으로 동적 쿼리를 구성해야 하다 보니 쿼리 재사용성과 중복을 어떻게 줄일지에 대한 고민이 많았습니다. 페이징 처리를 위해 count 쿼리와 실제 데이터 조회 쿼리를 나누는 과정에서 조건을 재활용하고자 QueryDSL의 clone()을 사용했지만, clone()이 내부적으로 join 조건을 복사하지 않아 예상과 달리 조인이 적용되지 않은 쿼리가 실행되었습니다. 이러한 문제를 해결하기 위해 공통 조건을 Base 쿼리 메서드로  분리하고, 각 쿼리에서는 필요한 select나 페이징 조건만 추가하여 중복을 줄이고 유지보수성을 높였습니다.

이번 프로젝트를 통해 ORM의 특성과 동작 방식을 이해할 수 있게 되었고, 재사용성과 성능 최적화를 고려하며 구조를 설계하는 경험을 쌓을 수 있는 좋은 경험이었습니다.
