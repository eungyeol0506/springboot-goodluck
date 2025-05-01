# springboot-goodluck
> 본 프로젝트는 스프링부트의 활용 및 실습 목적으로 진행하였습니다.

## Project Overview
**demo**
> 개발중

이 프로젝트는 스프링부트의 실습 목적으로 개인으로 진행되었습니다. 

[행운 수집 다이어리](http://www.10x10.co.kr/shopping/category_prd.asp?itemid=4783393) 벤치마킹하여 제작한 서비스로, 소소한 행운을 기록하며 행복을 전달합니다.

기본적인 `file handle`, `Test` 기반 단위 검증을 중점으로 `Thymeleaf`를 사용하여 WebMvc를 구현합니다. 

## Tech Stack

#### Architecture
> 개발중

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
│       ├── api/                          # REST API
│       ├── config/                       # 스프링 설정
│       ├── controller/                   # WebMVC controller
│       ├── domain/                       # 도메인, Repository
│       ├── global/                       # 예외 핸들러 및 interface 들들
│       └── service/                      # 트랜잭션 기반 핵심 로직
│             ├── board/                  # 게시글 관련련 컴포넌트
│             └── user/                   # 사용자 관련 컴포넌트
│   └── src/main/resources/
│       ├── static/                       # 이미지 파일 & js 파일
│       ├── templates/                    # thymeleaf 템플릿릿
│       └── application.properties        # 변수 설정
│   └── src/test/                         # 테스트 관련 코드
└── README.md                             # 프로젝트 문서
```
## Document
### 참고 자료