# KOSTA 3차 프로젝트 : 애니멀핑 🐾 
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
| **주 작업**  | 1. 팀 전체 관리 <br>2. REST API 설계<br>3. ERD 설계 <br>4. 소셜 로그인, 페이, 소켓 채팅 구현<br>5. 쇼핑몰 CRUD 구현 <br>6. 발표 | 1. 프로젝트 기획 <br>2. 백엔드 통신 설계 <br>3. 지도, 채팅 구현 <br>4. 댓글 및 대댓글 로직 구현<br>5. 판매 데이터 분석 툴 구현<br>6. React 프로젝트 관리 | 1. 팀 회의 서기 <br>2. 내 반려동물 정보 기반 계산기 구현 <br>3. 동물 백과 구현<br>4. 유기동물 검색 구현 <br>5. 장바구니 및 판매 상품 구현 | 1. 팀 일정 관리<br> 2. 오픈 API 전처리 <br>3. 쇼핑몰 CRUD 구현 <br>4. API 명세서 작성 <br>5. 코드 리팩토링 & QA <br>6. 발표 자료 작성 |
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

├── App.jsx
├── App.test.js
├── index.css
├── index.jsx
├── assets
│   ├── fonts
│   ├── img
│   └── styles
├── components
│   ├── additional
│   │   ├── adopt
│   │   ├── calc
│   │   └── wiki
│   ├── board
│   ├── chatting
│   ├── comment
│   ├── common
│   ├── layout
│   ├── map
│   ├── member
│   │   ├── myPage
│   │   │   └── items
│   │   ├── password
│   │   └── pet
│   │       └── register
│   └── shop
│       ├── admin
│       │   └── notice
│       ├── order
│       │   ├── delivery
│       ├── product
│       │   ├── QnA
│       │   ├── detail
│       │   ├── option
│       │   └── review
│       └── seller
│           ├── itemList
│           ├── itemRegister
│           └── sellerQna
├── pages
│   ├── additional
│   ├── board
│   ├── chatting
│   ├── map
│   ├── member
│   └── shop
│       ├── admin
│       ├── order
│       ├── product
│       └── seller
└── utils


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
API에 대한 자세한 내용은 아래 링크를 참고하세요:

[REST API 명세서 보기](https://crimson-shingle-d7b.notion.site/api-13ddb7c32eb98022b074dd7562af351e?pvs=74)


# 🧱  ERD 설계
![image](https://github.com/user-attachments/assets/a4397905-9aed-4d24-98bd-a0f2a90fd18e)

<br/>

# 🔄  흐름도
![image](https://github.com/user-attachments/assets/1c9fef65-06da-4069-a697-d29406a87bdd)

<br/>


# 🔥 동작 화면

### 소셜 회원가입 & 로그인
<p align="center">
  <img src="https://github.com/user-attachments/assets/76822a4d-b9d3-4c25-934e-1d607ff7b229" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 비밀번호 수정
<p align="center">
  <img src="https://github.com/user-attachments/assets/7abe7109-5bb3-4c5d-aec5-b5164e310026" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>


### 댓글 대댓글
<p align="center">
  <img src="https://github.com/user-attachments/assets/ff44b9b0-73ff-48b2-bbfd-953ed6a14600" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 배너
<p align="center">
  <img src="https://github.com/user-attachments/assets/dd5195ff-739a-404a-a55e-f48572821e75" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 펫 등록
<p align="center">
  <img src="https://github.com/user-attachments/assets/f37d54ff-6b17-4b40-88b7-56ef19819af7" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 펫 맞춤 상품 추천
<p align="center">
  <img src="https://github.com/user-attachments/assets/1b60c7d8-3bc0-4b7c-b947-e6b740485914" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 장바구니 구매
<p align="center">
  <img src="https://github.com/user-attachments/assets/f79cd5c5-c76a-469b-b841-af75cc162163" style="display: block; margin: auto; border: 2px solid #000;">
</p>

<br/>

### 계산기
<p align="center">
  <img src="https://github.com/user-attachments/assets/9fc17880-08b3-440a-a5c4-2698a88a33ae" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 동물백과
<p align="center">
  <img src="https://github.com/user-attachments/assets/abb956bb-d736-4ca1-8720-96af0bcd2d63" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 위치 기반 지도 검색
<p align="center">
  <img src="https://github.com/user-attachments/assets/592b184c-081a-4444-88a4-903a2cabc92d" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 반려동물 등록
<p align="center">
  <img src="https://github.com/user-attachments/assets/7c4c7325-7c37-49e5-a946-22e48b7c4575" style="display: block; margin: auto; border: 2px solid #000;">
</p>
<br/>

### 판매자 대시보드
<p align="center">
  <img src="https://github.com/user-attachments/assets/28215e52-a457-446b-9c8e-4a7fa091cae8" style="display: block; margin: auto; border: 2px solid #000;">
</p>

<br/>

### 관리자 대시보드
<p align="center">
  <img src="https://github.com/user-attachments/assets/8e016750-576f-4f16-afc9-11f714444c18" style="display: block; margin: auto; border: 2px solid #000;">
</p>

<br/>

### 관리자 채팅
<p align="center">
  <img src="https://github.com/user-attachments/assets/564d294f-4dbc-4c1b-997f-7862ff5ae69f" style="display: block; margin: auto; border: 2px solid #000;">
</p>

<br/>


<br/>


# ❓ Issue 사항

개발 과정에서 특히 공을 들였던 부분은 아래와 같습니다:

1. **내 상품 구매 정보를 각 판매자에게 다르게 전달**  
   - `HashMap`을 활용하여 각 판매자에게 적합한 구매 정보를 분리 전달하였습니다.  
   - 이를 통해 판매자별로 정보를 커스터마이징하고 처리 속도를 최적화할 수 있었습니다.

2. **장바구니 담기 로직 최적화**  
   - 사용자가 장바구니에 물품을 담을 때, 동일한 품목의 옵션이 이미 담겨 있다면 제외하여 출력하는 로직을 구현하였습니다.  
   - 중복 옵션을 제거함으로써 사용자 경험을 개선하고, 데이터 처리의 불필요한 중복을 방지했습니다.

3. **Spring JPA의 `Specification` 활용**  
   - Spring JPA의 `Specification` 기능을 사용하여 동적인 `WHERE` 조건절을 쉽게 생성하고 적용할 수 있었습니다.  
   - 복잡한 쿼리 로직도 간결하고 유지보수 가능한 형태로 구현할 수 있었습니다.


# 협업자료
발표 ppt 
https://www.miricanvas.com/v/142oczu
