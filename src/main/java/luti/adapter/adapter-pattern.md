# Adapter Pattern (어댑터 패턴)

## 개요

**한 클래스의 인터페이스를 클라이언트가 기대하는 다른 인터페이스로 변환한다.**
어댑터 패턴을 사용하면 호환되지 않는 인터페이스 때문에 함께 동작할 수 없었던 클래스들을 연결할 수 있다.

---

## 코드 구조

```
adapter/
├── ducks/                              ← Duck / Turkey 어댑터 예제
│   ├── Duck.java (interface)           ← Target: 클라이언트가 기대하는 인터페이스
│   ├── Turkey.java (interface)         ← Adaptee 인터페이스
│   ├── MallardDuck.java               ← Duck 구현체
│   ├── WildTurkey.java                ← Turkey 구현체 (Adaptee)
│   ├── TurkeyAdapter.java             ← Turkey → Duck 어댑터
│   ├── DuckAdapter.java               ← Duck → Turkey 어댑터 (양방향)
│   ├── DuckTestDrive.java
│   ├── TurkeyTestDrive.java
│   └── challenge/
│       ├── Drone.java (interface)      ← 전혀 다른 Adaptee 인터페이스
│       ├── SuperDrone.java            ← Drone 구현체
│       └── DroneAdapter.java          ← Drone → Duck 어댑터
│
└── iterenum/                           ← 실전 예제: Java 레거시 API 어댑터
    ├── EnumerationIterator.java        ← Enumeration → Iterator 어댑터
    ├── IteratorEnumeration.java        ← Iterator → Enumeration 어댑터
    └── EI.java / *TestDrive.java
```

---

## 등장인물

| 역할 | 클래스 | 설명 |
|---|---|---|
| **Target** | `Duck` | 클라이언트가 사용하는 인터페이스 |
| **Adaptee** | `Turkey`, `Drone` | 호환되지 않는 기존 인터페이스 |
| **Adapter** | `TurkeyAdapter`, `DroneAdapter` | Adaptee를 감싸 Target처럼 동작하게 함 |
| **Client** | `DuckTestDrive` | Target 인터페이스만 사용, Adaptee를 모름 |

---

## 동작 흐름

```
Client (DuckTestDrive)
    └─→ testDuck(duck)          ← Duck 인터페이스만 알면 됨

Turkey turkey = new WildTurkey()
Duck adapter  = new TurkeyAdapter(turkey)  ← Adaptee를 Adapter에 주입

testDuck(adapter)
    └─→ adapter.quack()         ← Duck.quack() 호출
            └─→ turkey.gobble() ← 내부적으로 Turkey에 위임
    └─→ adapter.fly()           ← Duck.fly() 호출
            └─→ turkey.fly() × 5  ← 짧은 비행을 5번 반복해 Duck 수준으로 맞춤
```

`testDuck()`은 `Duck` 인터페이스만 알고 있어서, `TurkeyAdapter`가 들어와도 `DroneAdapter`가 들어와도 동일하게 동작한다.

---

## 양방향 어댑터 (DuckAdapter)

`TurkeyAdapter` (Turkey → Duck) 반대 방향도 구현할 수 있다.

```java
// Duck → Turkey 어댑터
public class DuckAdapter implements Turkey {
    Duck duck;

    public void gobble() {
        duck.quack();           // Duck.quack() → Turkey.gobble()
    }

    public void fly() {
        if (rand.nextInt(5) == 0) {
            duck.fly();         // Duck은 멀리 날므로 5분의 1 확률로만 실행
        }
    }
}
```

단순 메서드 위임뿐 아니라 **행동 차이를 보정하는 로직**도 어댑터 안에 담길 수 있다.

---

## 실전 예제: Enumeration → Iterator (iterenum)

Java 초기 컬렉션(`Vector`, `Stack` 등)은 `Enumeration`을 사용했고,
이후 Java 2부터 `Iterator`가 표준이 됐다. 레거시 코드와 신규 코드를 연결할 때 어댑터가 필요하다.

```java
// Enumeration을 Iterator처럼 쓰기
public class EnumerationIterator implements Iterator<Object> {
    Enumeration<?> enumeration;

    public boolean hasNext()  { return enumeration.hasMoreElements(); }
    public Object  next()     { return enumeration.nextElement(); }
    public void    remove()   { throw new UnsupportedOperationException(); }
    //                          ↑ Enumeration에는 remove()가 없으므로 예외 처리
}
```

`remove()`처럼 **Adaptee에 없는 기능은 UnsupportedOperationException**으로 막는 것이 일반적이다.

---

## Object Adapter vs Class Adapter

이 패키지의 모든 어댑터는 **Object Adapter** 방식이다 (컴포지션으로 Adaptee를 감쌈).

| | Object Adapter | Class Adapter |
|---|---|---|
| 구현 방식 | Adaptee를 **필드로 갖는다** (컴포지션) | Adaptee를 **상속**한다 (다중 상속) |
| Java 가능 여부 | 가능 | 불가능 (Java는 단일 상속) |
| 유연성 | Adaptee 구현체를 런타임에 교체 가능 | 특정 구현체에 고정 |

Java에서는 인터페이스 다중 구현은 가능하지만 클래스 다중 상속은 안 되므로, 실질적으로 Object Adapter만 사용한다.

---

## 핵심 설계 원칙

**구현보다는 인터페이스에 맞춰서 프로그래밍하라**

클라이언트(`testDuck`)는 `Duck` 인터페이스만 바라본다.
`TurkeyAdapter`든 `DroneAdapter`든 `Duck`을 구현하기만 하면 클라이언트 코드 변경 없이 연결된다.
어댑터가 두 인터페이스 사이의 변환을 오롯이 책임지므로, 클라이언트와 Adaptee 양쪽 모두 수정이 필요 없다.

---

## 요약

| | 설명 |
|---|---|
| **ducks** | Turkey/Drone → Duck 어댑터. 양방향 어댑터와 행동 보정 로직 예시 포함 |
| **iterenum** | 레거시 Enumeration ↔ Iterator 상호 어댑터. 실전에서 어댑터가 필요한 이유를 보여줌 |
