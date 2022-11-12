# 코디네이터 1:1 매칭 패션 플랫폼

> 역할:  Spring Boot 백엔드
>
> 기간: 2021.12 ~ 2022.06
> 
> 깃허브: https://github.com/Sigma-i-2022/backend

---
### [목적]
- 서비스 이용자에게 개인 코디네이터를 1:1 매칭하여 스타일에 대한 피드백, 개별적 요소를 고려한 고객 맞춤형 코디 추천 서비스를 제공해주는 패션 플랫폼 제작

### [인원]
- 7명 - 백엔드 2명, 안드로이드 2명, 디자이너 1명, 기획 2명

### [역할]
- [토스페이먼츠 PG 시스템 API](https://ws-pace.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%ED%86%A0%EC%8A%A4%ED%8E%98%EC%9D%B4%EB%A8%BC%EC%B8%A0%20%EA%B2%B0%EC%A0%9C%20%EC%8B%9C%EC%8A%A4%ED%85%9C%20%EC%A0%81%EC%9A%A9%20%EC%8B%9C%EB%A6%AC%EC%A6%88)를 이용한 결제 시스템 구현
- 금융결제원 오픈뱅킹 OpenAPI를 이용한 [계좌실명조회 기능 구현](https://ws-pace.tistory.com/231?category=1010992)
- 기능 단위 패키지 개발 & Git Branch 전략을 통한 협업
- Junit 5 및 BDD Mockito 를 사용하여 API, DB (JPA), Service 로직 테스트 코드 작성
- 전체 목록 조회 시 JPA의 페이징 기법을 적용하여 DB 쿼리 최적화
- Fetch Join Query문을 통해 Lazy Loading 관계를 포함하는 객체 조회 시 발생하는 N + 1 문제 해결

### [문제 해결 경험]
- 토스페이먼츠 서브몰 등록 API 개발 중 [공식문서 오류 정정기](https://ws-pace.tistory.com/242)

### [사용 기술]
- 언어 : Java
- 서버 : AWS EC2, RDS, S3
- 모니터링 : AWS Cloud Watch
- DB : MySql
- 프레임워크 : Spring Boot, Spring Data JPA, Gradle, Swagger
- 외부 API : 토스페이먼츠 PG, 금융결제원 오픈뱅킹 오픈 API

### [성과]
- 교육부 주관 학생 [창업유망팀 300 경진대회 성장트랙](http://www.u300.or.kr/cms/process/team/view.asp?c_show_no=104&c_check_no=97&c_relation=962&c_relation2=980&c_no=4182) 선정
