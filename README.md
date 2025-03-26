# Inflearn Java to Kotlin Expansion Pack

- [[2-3] Configure a Gradle project](https://kotlinlang.org/docs/gradle-configure-project.html): 자바와 코틀린 함께 사용하기
- 2.6 JUnit5 사용법과 테스트 코드 리팩터링
    - 자주 사용하는 5가지 어노테이션
        - `@Test`, `@BeforeEeach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`
    - 자주 사용되는 단언문
    - [Test code using JUnit in JVM - tutorial](https://kotlinlang.org/docs/jvm-test-using-junit.html): JUnit 사용하기
    - [Object declarations and expressions](https://kotlinlang.org/docs/object-declarations.html): 정적 메서드
    - [Scope functions](https://kotlinlang.org/docs/scope-functions.html): `apply`
- 2.7 JUnit5로 Spring Boot 테스트하기
- 2.8 유저 관련 기능 테스트 작성하기
- 2.9 책 관련 기능 테스트 작성하기
- 2.10 테스트 작성 끝! 다음으로!

## 2.7 JUnit5로 Spring Boot 테스트 하기

### POJO(Plain Old Java Object)

- 구조에 종속되지 않는 순수한 자바 객체

1. Can't Extend anything
2. Can't Implement anything
3. No outside annotations

- [P O J O](https://martinfowler.com/bliki/POJO.html)
- [What is a POJO in Java? Almost EVERYONE Gets This Wrong](https://www.youtube.com/watch?v=oqPiEc2zNb0&t=25s)

### 2.7.1 @Autowired constructor

- [Using @Autowired](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired.html)

### 플랫폼 타입(Platform Type)

```kotlin
results[0].age must not be null
```

- 코틀린은 자바와 상호 운용성을 고려하여 설계되었기 때문에 자바 코드를 호출할 수 있다.
- 차이에서 발생하는 느슨함을 표현하는 타입이다.

## 2.8 유저 관련 기능 테스트 작성하기

### 2.8.1 테스트 독립성 유지하기

`@AfterEach`와 인메모리 데이터베이스, `@Transactional` 외에 독립성을 유지하는 방법을 알아보자.

- [Testcontainers를 이용한 테스트 환경 구축하기](https://dealicious-inc.github.io/2022/01/10/test-containers.html)

### 2.8.2 AssertJ Assertions

```kotlin
assertThat(results).extracting("name").containsExactlyInAnyOrder("김동영", "최용준")
assertThat(results).extracting("age").containsExactlyInAnyOrder(29, null)
```

- `containsExactlyInAnyOrder`: 순서와 상관없이 컬렉션에 정확히 포함하고 있는지 검증한다.

- [AssertJ - fluent assertions java library](https://assertj.github.io/doc/#assertj-core-assertions-guide())