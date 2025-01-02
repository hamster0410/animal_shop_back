# KOSTA 3차 프로젝트 : 애니멀핑
<br/>

# 📃목차

- 개요
    - 프로젝트 목적
    - 아이디어 및 배경
    - 프로젝트 플랜
- 팀원
    - 소개 및 역할 분담
- 설계
    - 주요 기능
    - 사용된 기술 스택
    - 프로젝트 구조
    - REST API 설계
    - ERD 설계
    - 흐름도
- 동작 화면
- issue 사항
<br/>


# 📌 프로젝트 목적

- 반려동물 정보를 기반으로한 내 반려동물 맞춤 플랫폼 
- 위치기반 정보, 공공 API를 기반으로한 서비스 제공 
<br/>


# 💡 아이디어 및 배경

- 반려동물을 키우는 사람들이 내가 키우는 반려동물에 대해 더 잘 알고 다양한 생각을 공유하는게 좋다는 생각이 들었습니다. (커뮤니티, 동물백과, 펫 정보 계산기)
- 반려동물을 키우는 사람들이 더 잘 키울 수 있기를 바랍니다. (쇼핑몰 운영 & 관리자와 채팅)
- 반려동물과 함께 가볼만한 곳을 알려주고 싶었습니다. (펫 동반시설 지도 검색)
- 반려동물에게 새 가족을 만들어 주고 싶었습니다. (유기동물 관심상태 등록 및 상태 메일 전송)
  
<br/>

# 📅 프로젝트 플랜
![image](https://github.com/user-attachments/assets/01d58b8b-4d09-4617-a349-842844335a8d)

<br/>

# 🤼 팀 멤버 소개

| **항목**     | **선우**                                                                                      | **소진**                                                                                      | **정아**                                                                                      | **혁주**                                                                                      |
|:------------|:---------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------|
| **사진**     | ![한선우](https://avatars.githubusercontent.com/u/120350053?v=4)                              | ![최소진](https://github.com/user-attachments/assets/646fbee6-a1b8-402e-9b91-93dd2c31a778)   | ![최정아](https://github.com/user-attachments/assets/20aab45b-c93b-4166-9c77-acb1288f47fe)   | ![석혁주](https://avatars.githubusercontent.com/u/140710676?v=4)   |
| **역할**     | BackEnd(팀장)                                                                                     | FrontEnd(부팀장)                                                                                     | FrontEnd                                                                                     | BackEnd                                                                                     |
| **GitHub**   | [한선우 GitHub](https://github.com/hamster0410)                                               | [최소진 GitHub](https://github.com/sosojean)                                                 | [최정아 GitHub](https://github.com/berryicebox)                                              | [석혁주 GitHub](https://github.com/cocoboll0)                                              |
| **주 작업**  | 1. 팀 전체 관리 <br>2. REST API 설계<br>3. ERD 설계 <br>4. 소셜 로그인, 페이, 소켓 채팅 구현<br>5. 쇼핑몰 CRUD 구현 <br>6. 발표 | 1. 프로젝트 기획 <br>2. 백엔드 통신 설계 <br>3. 지도, 채팅 구현 <br>4. 댓글 및 대댓글 로직 구현<br>5. 판매 데이터 분석 툴 구현<br>6. React 프로젝트 관리 | 1. 팀 회의 서기 <br>2. 내 반려동물 정보 기반 계산기 구현 <br>3. 동물 백과 구현<br>4. 유기동물 검색 구현 <br>5. 커스텀 훅 생성 및 적용 | 1. 팀 일정 관리<br> 2. 오픈 API 전처리 <br>3. 쇼핑몰 CRUD 구현 <br>4. API 명세서 작성 <br>5. 코드 리팩토링 & QA <br>6. 발표 자료 작성 |
<br/>


# 🔑 주요 기능

- **커뮤니티**
    - 다양한 카테고리를 가진 게시판을 통해 반려동물을 키우는 사람들이 원하는 주제로 소통합니다. 
- **쇼핑몰**
    - 반려동물을 키우는데 필요한 물건들을 사용자가 직접 사고 판매합니다. 
- **유틸리티**
    - 반려동물을 키우는데 도움이 될만한 기능들을 추가했습니다. 


![image](https://github.com/user-attachments/assets/63dcb502-59a0-4260-86c6-4602f4ed6461)

    
# <img src="https://github.com/user-attachments/assets/c358e165-b991-4930-85b1-cddc0433a5d9" width="50"> 사용된 기술 스택

![제목을-입력해주세요_-001 (1)](https://github.com/user-attachments/assets/18c49e68-3f1c-4d78-9563-f814e80bacf4)

<br/>


# 🌌 프로젝트 구조

```agda
Front End (React)

├─assets //스타일링 요소
│  ├─fonts // 폰트
│  ├─img // 이미지
│  └─styles // scss 파일
│      ├─board
│      ├─comment
│      └─layout
│
├─components // 모듈 컴포넌트
│  ├─board //게시판 관련
│  ├─comment //댓글 관련
│  └─layout //화면 레이아웃 관련
│
├─pages // 페이지 컴포넌트
│
└─utils // 커스텀훅, 전역 함수

----------------------------------------------------------------------------------------

Back End (Spring Boot)

project/
├── community                 //애니멀핑 커뮤니티 도메인
│   ├── comment               //댓글 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── heart_comment         //댓글 좋아요 CRUD
│   │   ├── controller
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── heart_post            //게시글 좋아요 CRUD
│   │   ├── controller
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── member                //회원 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   └── post                   //게시글 CRUD
│       ├── controller
│       ├── dto
│       ├── entity
│       ├── repository
│       └── service
├── global                    // 전체 프로젝트에서 공통적으로 쓰이는 서비스 모음
│   ├── admin                 // 관리자 기능 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── config                // 내부 보안 설정, 소켓 통신 
│   ├── controller            // 파일 입출력 Controller
│   ├── dto    
│   ├── init                  // 프로젝트 실행 시 초기 세팅(디렉토리 생성 등)
│   ├── pay                   // 결제 구현 CRUD
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── security              // spring security, jwt token provider
│   └── service
├── shop                      // 애니멀핑 쇼핑몰 도메인
│   ├── cart                  // 장바구니 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── cart_item             // 장바구니 상품 CRUD
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── delivery              // 배송 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── item                  // 상품 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── item_comment          // 상품 리뷰 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── item_comment_like     // 상품 리뷰 좋아요 CRUD
│   │   ├── controller
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── main                  // 메인페이지 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   └── service
│   ├── order                 // 주문 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── order_item             // 주문 상품 CRUD
│   │   ├── dto
│   │   ├── entity
│   │   └── repository
│   ├── pet                    // 반려동물 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   ├── point                  // 상품 결제 포인트 CRUD
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   └── service
│   └── seller                 // 판매자 기능 CRUD
│       └── controller
└── tools                      // 유틸리티(지도 검색, 유기동물, 동물 백과, 펫 계산기)
    ├── abandoned_animal       // 유기동물 CRUD
    │   ├── controller
    │   ├── dto
    │   ├── entity
    │   ├── repository
    │   └── service
    ├── calculate              // 계산기 CRUD
    │   ├── controller
    │   ├── dto
    │   └── service
    ├── chat                   // 채팅 CRUD
    │   ├── controller
    │   ├── dto
    │   ├── entity
    │   ├── repository
    │   └── service
    ├── map_service            // 지도 CRUD
    │   ├── controller
    │   ├── dto
    │   ├── entity
    │   ├── repository
    │   └── service
    └── wiki_service            // 동물 백과 CRUD 
        ├── controller
        ├── dto
        ├── entity
        ├── repository
        └── service

```
<br/>


# ✉️ REST API 명세서



# 🧱  ERD 설계
![image](https://github.com/user-attachments/assets/a4397905-9aed-4d24-98bd-a0f2a90fd18e)

<br/>

# 🔄  흐름도
![image](https://github.com/user-attachments/assets/1c9fef65-06da-4069-a697-d29406a87bdd)

<br/>


# 🔥 동작 화면

### 회원가입
<p align="center">
  <img src="https://github.com/user-attachments/assets/0674787e-5e62-4c01-9f85-d1e624afc868" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 게시글 작성
<p align="center">
  <img src="https://github.com/user-attachments/assets/8eb5fdf5-8b10-483c-ae50-384ff7e3d4ad" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 게시글 추천
<p align="center">
  <img src="https://github.com/user-attachments/assets/ee1b9dcb-f4bd-41fd-8011-b2c276f399fd" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 댓글 작성 

<p align="center">
  <img src="https://github.com/user-attachments/assets/ecc2d770-b6ed-47cd-9eaf-51b0fc8987b5" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

<br/>


# ❓ Issue 사항
- 선우 : 이슈
   - jwt 인증 토큰 구현 : 라이브러리 선정 및 함수 파악 어려움
   - 대댓글 엔티티 순환참조 : 댓글에 부모를 dto로 리턴하여 에러 해결
   - 이미지 업로드 기능 구현 : 이미지 업로드시 서버에 저장하고 반환하는 방법 선정에서 테이블에 이미지 url컬럼을 생성하는가에 대한 고민이 많았음
   - 컨텐츠 구현 부족 : 내가 좋아요한 게시판, 작성한 게시판 조회 등 구현해야할 컨텐츠가 많았음에도 파이널프로젝트 시작으로 구현하지 못한것에 대한 아쉬움 

- 소진 : 이슈
  - 이미지 업로드 기능 구현: 이미지 파일을 업로드하는 과정에서 어려움이 있었음.
  - 대댓글 정렬: 댓글과 대댓글을 계층 구조로 정렬하는 기능 구현에 어려움이 있었음.
  - 컴포넌트 간 부모-자식 관계 설정: 컴포넌트 간 부모-자식 관계를 설정하고 관리하는 과정에서 어려움을 느꼈음.
  - 컴포넌트 리렌더링: 추천/추천 취소 시 컴포넌트가 의도한 대로 리렌더링되지 않는 문제 발생.
  - JWT 리프레시 토큰 발급: JWT 리프레시 토큰을 발급하여 인증 상태를 갱신하는 기능 구현의 어려움.

- 정아 : 이슈
  - 이미지 업로드 기능: 이미지 업로드 기능 구현에 어려움이 있었음.
  - 추천/추천 취소 리렌더링 문제: 추천/추천 취소 기능 실행 시 컴포넌트가 의도한 대로 리렌더링되지 않는 문제 발생.

# 협업자료
[프로젝트 제안서.pdf](https://github.com/user-attachments/files/17694815/default.pdf)
