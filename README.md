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
│       ├── common/                       # 전역에서 공통으로 사용
│       ├── config/                       # 스프링 설정
│       ├── domain/                       # 도메인 계층
│       ├── exception/                    # 예외처리 핸들러
│       ├── myboard/                      # myboard 관련 모듈 (핵심 로직)
│       ├── myuser/                       # myuser 관련 (핵심 로직)
│       └── GoodluckApplication.java      # 스프링 부트 앱
│   └── src/main/resources/
│       ├── static/                       # 이미지 파일 & js 파일
│       ├── templates/                    # thymeleaf 템플릿릿
│       └── application.properties        # 변수 설정
│   └── src/test/                         # 테스트
└── README.md                             # 프로젝트 문서
```
## Document
### 참고 자료