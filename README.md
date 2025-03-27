# Inflearn Java to Kotlin Expansion Pack

# 2. 리팩터링 사전 준비

## 목차

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

## 2.9 책 관련 기능 테스트 작성하기

### 2.9.1 후행 쉼표(Trailing comma)

> Settings/Preferences | Editor | Code Style | Kotlin, open the Other tab and select the Use trailing comma option.

자바에서는 후행 쉼표를 사용할 수 없었다. 하지만 코틀린은 후행 쉼표를 사용할 수 있고 권장한다. 작은 배려다.

- [Coding conventions](https://kotlinlang.org/docs/coding-conventions.html#trailing-commas)

### 2.9.2 자식 관계까지 함께 삭제하기

> 구의 증명, 우리는 한 몸이야. 절연하자.

- `JPA`의 `orphanRemoval` 옵션의 기본값은 `false`다. `true`로 변경하여 관계를 맺은 `엔티티(entity)`의 데이터까지 함께 삭제한다.
- 자식을 먼저 삭제하고 부모를 삭제한다.
- [김영한 'orphanRemoval과 cascade의 관계'](https://www.inflearn.com/community/questions/137740)

## 2.10 테스트 작성 끝! 다음으로!

### 2.10.1 전체 테스트 코드 실행 방법

1. 터미널

```shell
./gradlew test
```

- 터미널로 간편하게 테스트 할 수 있다.

2. IDE

> Gralde | Tasks | verification | check

- 에러가 발생했을 때 메시지를 추적하기 용이하다.

- [Getting Started with Gradle](https://www.jetbrains.com/help/idea/getting-started-with-gradle.html)

# 3. Java 서버를 Kotlin 서버로 리팩터링하자!

- 3.1 Kotlin 리팩터링 계획 세우기
- 3.2 도메인 계층을 Kotlin으로 변경하기 - Book.java
- 3.3 도메인 계층을 Kotlin으로 변경하기 - UserLoanHistory.java, User.java
- 3.4 Kotlin과 JPA를 함께 사용할 때 이야기거리 3가지
- 3.5 리포지토리를 Kotlin으로 변경하기

## 3.1 Kotlin 리팩터링 계획 세우기

```kotlin
@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String
)
```

- 앞서, `POJO`를 엄격하게 바라봤다. 하지만 넓게 생각하면 `JPA Entity`와 `POJO`를 모두 만족하는 객체는 존재한다. 실무에서는 다양한 메타데이터가 필요하다.

### 3.1.1 당신의 엔티티는 안전하십니까

> 1. `엔티티`는 `JPA의 엔티티(@Entity)`와 1:1 대응될까?
> 2. `JPA`가 없으면 `엔티티`는 허상인가?

- 추상적이고 보편적인 엔티티를 구체화한 결과가 `JPA 엔티티`라고 생각한다.
- 문맥에 따라 다르지만, 주로 사용하는 엔티티는 도메인 엔티티(비즈니스 객체)를 의미한다.
- `JPA 엔티티`의 뿌리는 `DB 엔티티`에 가깝다. 추상적인 엔티티를 구현 기술(JPA)에 맞춰 구체화한 결과물이다.
- **따라서, `JPA 엔티티`는 `영속성[^1] 객체(PO: persistent object)`다.**

[^1]: 데이터가 영원히 이어지도록 임의의 공간에 저장하고 불러온다.

## 3.2 도메인 계층을 Kotlin으로 변경하기 - Book.java

### 3.2.1 JPA Entity 생성하기

- `선택적 매개변수(default parameter)`는 왜 하단에 위치해야할까?
    - 기술적으로 문제가 되지 않으면 사용성과 가독성을 고려한다.
    - 공식 문서에 정의되어 있지 않지만 필수 파라미터를 먼저 작성하는 방법을 권장한다.
- [제미니의 개발실무 'Kotlin JPA Entity ID 선언 전략'](https://youtu.be/gv9D2i07hNU?si=2eKcnoFhvGdyTYfT)
- [kotlin JPA 에서 entity ID 를 어떻게 선언하는게 좋을까?#1](https://multifrontgarden.tistory.com/304)
- [No-arg compiler plugin](https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support)

### 3.2.2 plugin vs. dependency

- `Gradle` `플러그인(plugin)`과 `의존성(dependency)`의 차이
    - `plugin`: Gradle 빌드 프로세스 확장
    - `dependency`: 프로젝트에서 사용하는 라이브러리, 런타임과 컴파일 시점에 사용하는 모듈

```
Caused by: java.lang.NoClassDefFoundError: kotlin/reflect/full/KClasses
```

- 상황: 코틀린 컴파일러가 `JVM`, `ClassLoader`가 리플랙션을 실행 할 수 없을 때 발생한다.
- 해결: `dependency`를 추가한다.
- [Reflection](https://kotlinlang.org/docs/reflection.html)