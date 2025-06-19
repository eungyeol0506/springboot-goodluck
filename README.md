# springboot-goodluck
> 본 프로젝트는 스프링부트의 활용 및 실습 목적으로 진행하였습니다.
> 
![home image](image.png)
## Project Overview
이 프로젝트는 스프링부트의 예외처리 및 테스트 기반의 개발을 목적으로 개인으로 진행되었습니다. 

[행운 수집 다이어리](http://www.10x10.co.kr/shopping/category_prd.asp?itemid=4783393) 의 아이디어를 벤치마킹하여 제작한 웹 서비스로, 행운을 기록하며 모으는 기능을 제공합니다.

`multipartFile`, `SpringSecurity` 기능을 포함하여 `Test` 코드 작성을 중점으로 `Thymeleaf`를 사용하여 `WebMvc`를 구현하는 것을 목표로 하였습니다.

## Tech Stack

#### Architecture
> 

#### Backend
<div align=""> 
  <img src="https://img.shields.io/badge/Java17-007396?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Apache%20Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white"/>
  <img src="https://img.shields.io/badge/JdbcTemplate-007396?style=for-the-badge&logoColor=white"/>
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"/>
</div>

#### WebMvc
<div align="">
  <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"/>
  <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"/>
</div>

#### Database
<div align="">
  <img src="https://img.shields.io/badge/Oracle%20Cloud-DA291C?style=for-the-badge&logoColor=white"/> 
</div>

## Project Structure
```
├── GOODLUCK/
├── .github/workflows/                    # GitHub Actions CI/CD 설정
│   └── libs/                             # oracle-cloud 의존성 라이브러리 
│   └── src/main/java/com/example/goodluck/
│       ├── GoodluckApplication.java      # 스프링 부트 앱
│       ├── api/                          # REST API (예정)
│       ├── config/                       # 스프링 설정
│       ├── controller/                   # WebMVC controller
│       ├── domain/                       # 도메인, Repository 
│       ├── global/                       # 예외 핸들러 및 Helper, Interface 등
│       └── service/                      # 트랜잭션 기반 핵심 로직
│             ├── board/                  # 게시글 관련련 컴포넌트
│             └── user/                   # 사용자 관련 컴포넌트
│   └── src/main/resources/
│       ├── static/                       # static 이미지 파일 & js 파일
│       ├── templates/                    # thymeleaf 템플릿
│       └── application.properties        # 변수 설정
│   └── src/test/                         # 테스트 관련 코드
│   └── uploads/                          # 첨부 이미지 저장소 
└── README.md                             # 프로젝트 문서
```
## Document
### 일반 사용자(ROLE_USER) 권한 인증/인가 테이블
|구분|경로(URL)|설명|인증 필요|작성자만 접근|메서드| 
|---|------------|------------|---|---|--------|
|USER|`/`|홈|❌| - |GET|
|USER|`/login`|로그인|❌| - |GET/POST|
|USER|`/logout`|로그아웃| - | - |GET|
|USER|`/regist`|회원가입|❌| - |GET/POST|
|USER|`/profile`|마이 페이지|✅|✅|GET|
|USER|`/profile/form`|내 정보 수정|✅|✅|GET/POST|
|USER|`/password-change`|비밀번호 변경|✅|✅|GET/POST|
|BOARD|`/list/{page}`|게시글 목록 조회|❌| - |GET|
|BOARD|`/board/{boardNo}`|게시글 조회|❌| - |GET|
|BOARD|`/board/write`|게시글 작성|✅|❌|GET/POST|
|BOARD|`/board/delete/{boardNo}`|게시글 삭제|✅|✅|POST|
|BOARD|`/board/modify/{boardNo}`|게시글 수정|✅|✅|GET/POST|
|COMMENT|`/board/{boardNo}/comment`|댓글 작성|✅|❌|POST|

### 참고 자료
- [타임리프 문서](https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html)
- [부트워치](https://bootswatch.com/minty/)
- [서블릿 웹 애플리케이션](https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.static-content)