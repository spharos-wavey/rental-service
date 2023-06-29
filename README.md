# rental-service

## 기술 스택

---

* ### Java 17
* ### Spring Boot 3.0.6
* ### Gradle - Groovy
* ### Redis
  * 결제 과정에서 카카오페이를 사용하였는데 이 때 발생하는 보안상 이슈를 해결하기 위해서 사용하였음
* ### MySQL
* ### Spring Data JPA
* ### Spring Netflix Eureka Client
* ### Apache Kafka
  * 결제 시 포인트를 사용한 경우 유저의 보유 포인트에서 사용한만큼 차감하는 요청을 비동기 방식으로 처리하기 위해서 사용하였음

## 프로젝트 구성(디렉토리 구조)

---

### 계층형 디렉토리 구조 선택
```text
└─rentalservice
    │  RentalServiceApplication.java
    ├─base
    │  │  BaseTimeEntity.java
    │  ├─config // 각종 컨피그 및 기본 설정(DB, Redis, OpenFeign, ...)
    │  └─exception  // 예외 처리
    ├─controller
    ├─messageQueue  // 카프카 설정
    ├─model
    ├─repository
    ├─service
    └─vo
        ├─request
        └─response
```
### 추가 설명
* base
  * BaseTimeEntity.java
    * 데이터 삽입, 수정 시각을 자동으로 조회 및 저장 해주는 클래스
  * config
    * 프로젝트에 적용된 기술에 대한 컨피크 디렉토리(DB Replication, Redis, ...)
  * exception
    * 비즈니스 로직에서 발생할 수 있는 오류들에 대한 에러코드 정의 및 반환 매서드 작성


## ERD

---

<img src="https://user-images.githubusercontent.com/90381800/243152568-35d1dab4-93c1-4a46-a69a-7b7248529149.png" alt="rental-service erd">

## 개발 컨벤션

---

### 디렉토리명, 클래스명 규칙
* 패키지명은 소문자로 시작하여 카멜표기법 사용
* 자바 파일명은 대문자로 시작하여 카멜표기법 사용
  * repository의 경우 서비스명+Repo
  * service의 경우 인터페이스는 서비스명+service, 클래스는 서비스명+serviceImpl
  * VO의 경우 첫 단어로 Request, Response 표기

### 깃 규칙
1. 기본적으로 git flow를 사용함
2. feature 브랜치 생성시 이름은 개발하려는 서비스명(ex. login, singup, vehicle)  
  2-1. commit message 작성 시 "[Create, Update, Delete 중 택 1] 가장 중요한 정보" 로 작성
3. 상세 정보는 PR에 작성
4. 일간 작업 결과물은 delvop 브랜치에 PR
5. 주간 단위로 develop에서 main으로 PR(백업용)

## 실행

---

### application.yml
```yaml
spring:
  application:
    name: rental-service
    
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}

  datasource:
    master:
      username: ${DATABASE_WRITE_ONLY_USERNAME}
      password: ${DATABASE_WRITE_ONLY_PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://${DATABASE_WRITE_ONLY_HOST}:${DATABASE_WRITE_ONLY_PORT}/${DATABASE_WRITE_ONLY_SCHEMA}?useSSL=false

    slave:
      username: ${DATABASE_READ_ONLY_USERNAME}
      password: ${DATABASE_READ_ONLY_PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
      jdbc-url: jdbc:mysql://${DATABASE_READ_ONLY_HOST}:${DATABASE_READ_ONLY_PORT}/${DATABASE_READ_ONLY_SCHEMA}?useSSL=false
  
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL5Dialect
        format_sql: true
    hibernate:
      ddl-auto: update  # create

eureka:
  client:
    service-url:
      defaultZone: http://${DISCOVERY_SERVICE_HOST}:${DISCOVERY_SERVICE_PORT}/eureka/
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}

kakao:
  content-type: application/x-www-form-urlencoded;charset=utf-8
  key:
    admin: ${KAKAO_ADMIN_KEY}
  pay:
    cid: TC0ONETIME
    approval_url: ${PURCHASE_APPROVE_URL}
    cancel_url: ${PURCHASE_CANCEL_URL}
    fail_url: ${PURCHASE_FAIL_URL}
    
kafka:
  bootstrap-server: http://${KAFKA_HOST}:9092
```
* spring.data.redis
  * REDIS_HOST: 레디스 서버가 설치되어있는 서버의 ip
  * REDIS_PORT: 레디스 서버의 리스닝 포트
    * 따로 설정해주지 않을 경우 기본포트 6379로 설정됨
  * REDIS_PASSWORD: 레디스 서버의 비밀번호
    * 접속하려는 레디스 서버에 비밀번호 설정이 되어있지 않다면 삭제해도 무관
    * 대신 삭제 후 해당 프로젝트의 RedisConfig.java 파일에서 수정 필요
* spring.datasource
  * DB Replication을 완료했다면 그에 맞춰서 각각의 host, port, schema 정보를 입력
  * 단일 DB로 실행하는 경우 master, slave 정보를 동일하게 입력
  * DATABASE_USERNAME: MySQL 서버에 접근하기 위한 유저 이름
  * DATABASE_PASSWORD: DATABASE_USERNAME의 비밀번호
  * DATABASE_HOST: MySQL server가 설치된 서버의 ip
  * DATABASE_PORT: MySQL server의 리스닝 포트
    * MySQL server 설치 시 별도의 설정을 하지 않았으면 기본 포트 3306 입력
  * DATABASE_SCHEMA: 해당 프로젝트와 연결되는 스키마 이름
* eureka.client.service-url.defaultZone
  * DISCOVERY_SERVICE_HOST: 디스커버리 서비스가 실행되는 서버의 ip
  * DISCOVERY_SERVICE_PORT: 디스커버리 서비스의 리스닝 포트
    * 스프링 클라우드 유레카 서버의 경우 기본 포트는 8761
* kakao
  * 카카오 공식문서 참고 바람
  * https://developers.kakao.com/docs/latest/ko/kakaopay/single-payment
* kafka.bootstrap-server
  * KAFKA_HOST: 아파치 카프카가 설치된 서버의 ip

### 실행 커맨드
```text
# build
./gradlew bootjar

# run
./gradlew bootrun
```