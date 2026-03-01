# Strategy Pattern (전략 패턴)

## 개요

**알고리즘군을 정의하고, 각각을 캡슐화하여 교환 가능하게 만든다.**
전략 패턴을 사용하면 클라이언트로부터 알고리즘을 독립적으로 변경할 수 있다.

---

## 코드 구조

```
Duck (abstract)
├── flyBehavior: FlyBehavior       ← 전략 인터페이스를 "has-a"로 보유
├── quackBehavior: QuackBehavior
├── performFly()                   ← 전략에 위임
├── performQuack()
└── setFlyBehavior() / setQuackBehavior()  ← 런타임 교체 가능

FlyBehavior (interface)
├── FlyWithWings    → "날고 있어요!"
├── FlyNoWay        → "저는 못 날아요"
└── FlyRocketPowered → "로켓 추진으로 날아갑니다!"

QuackBehavior (interface)
├── Quack   → "꽥꽥"
├── Squeak  → "삑"
└── MuteQuack → "<< 조용~ >>"

Duck 구현체
├── MallarDuck  → FlyWithWings + Quack
└── ModelDuck   → FlyNoWay + Quack  (런타임에 FlyRocketPowered로 교체)
```

---

## 왜 상속만으로는 부족한가?

"Duck을 계속 상속하면 되는 거 아닌가?" 라고 생각할 수 있다.
그러나 오리의 종류와 행동 방식이 늘어날수록 상속은 빠르게 무너진다.

### 문제 1 : 코드 중복

날개로 나는 오리가 여러 종이라고 가정하자.

```
MallarDuck extends Duck { void fly() { "날고 있어요!" } }
GreenDuck  extends Duck { void fly() { "날고 있어요!" } }   // 완전 동일한 코드
WildDuck   extends Duck { void fly() { "날고 있어요!" } }   // 또 동일
```

`fly()` 로직이 바뀔 때 이 모든 클래스를 수정해야 한다.
코드가 여러 곳에 흩어져 있으니 수정 누락이 발생하기 쉽다.

### 문제 2 : 슈퍼클래스 변경이 서브클래스 전체에 영향

`Duck`에 `fly()`를 추가했더니 날면 안 되는 `RubberDuck`까지 날게 되는 상황.
부모를 고치면 모든 자식이 영향을 받는다. 이걸 막으려면 자식마다 일일이 오버라이드해야 한다.

```java
// 날면 안 되는 오리마다 이걸 반복해야 한다
class RubberDuck extends Duck {
    void fly() { /* 아무것도 안 함 */ }
}
class DecoyDuck extends Duck {
    void fly() { /* 아무것도 안 함 */ }
}
```

### 문제 3 : 런타임 행동 변경 불가

상속 구조에서 오리의 행동은 컴파일 타임에 고정된다.
`ModelDuck`은 태어나는 순간부터 죽을 때까지 못 나는 오리다.

```java
// 상속 구조에서는 이게 불가능하다
Duck model = new ModelDuck();
model.fly(); // "못 날아요"
model.upgradeTo(RocketFly);  // ???? 방법이 없다
model.fly(); // "로켓 추진!" ← 이걸 하려면 클래스를 새로 만들어야 한다
```

### 문제 4 : 행동 조합 폭발

나는 방법 3가지 × 우는 방법 3가지 = 서브클래스가 최소 9개 필요.
새로운 행동이 추가될수록 클래스 수가 기하급수적으로 늘어난다.

---

## 전략 패턴이 이 문제를 해결하는 방식

### "has-a"가 "is-a"보다 강하다

상속은 **"~이다(is-a)"** 관계다.
전략 패턴은 **"~을 가진다(has-a)"** 관계(합성, Composition)를 사용한다.

Duck은 나는 방법을 **직접 구현하지 않고**, FlyBehavior 객체에 **위임**한다.

```java
// Duck.java
public void performFly() {
    flyBehavior.fly();  // 구체적인 방법은 전략 객체가 결정
}
```

행동이 Duck 클래스 바깥에 독립적으로 존재하므로:
- 행동을 수정해도 Duck 계층은 건드리지 않아도 된다
- 새 행동을 추가할 때 기존 코드를 전혀 바꾸지 않아도 된다 (OCP)
- 행동 객체는 다른 오리에게도 재사용 가능하다

### 런타임 교체

`MiniDuckSimulator.java`에서 확인할 수 있듯이:

```java
Duck model = new ModelDuck();
model.performFly();                          // "저는 못 날아요"

model.setFlyBehavior(new FlyRocketPowered()); // 런타임에 전략 교체
model.performFly();                          // "로켓 추진으로 날아갑니다!"
```

`ModelDuck` 클래스를 전혀 수정하지 않고, 오리의 행동을 실행 중에 바꿀 수 있다.
상속만으로는 절대로 이런 유연성을 얻을 수 없다.

---

## 핵심 설계 원칙

이 패턴에는 두 가지 중요한 설계 원칙이 녹아 있다.

**1. 변하는 부분을 캡슐화하라**
Duck에서 변하는 것은 `fly()`와 `quack()`이다. 이것만 꺼내 인터페이스 뒤에 숨긴다.

**2. 구현보다 인터페이스에 맞춰 프로그래밍하라**
`Duck`은 `FlyWithWings`를 직접 알지 않는다. `FlyBehavior`라는 인터페이스만 알 뿐이다.
덕분에 어떤 FlyBehavior 구현체가 들어오든 Duck은 신경 쓰지 않아도 된다.

**3. 상속보다 합성을 활용하라**
Duck이 행동을 직접 물려받는 것이 아니라, 행동 객체를 **가짐**으로써 훨씬 유연해진다.

---

## 요약

| | 상속만 사용 | 전략 패턴 |
|---|---|---|
| 행동 수정 시 | 관련 서브클래스 전부 수정 | 해당 전략 클래스만 수정 |
| 새 행동 추가 시 | 새 서브클래스 or 오버라이드 | 새 전략 클래스만 추가 |
| 런타임 행동 변경 | 불가능 | setter로 간단히 가능 |
| 코드 재사용 | 같은 코드가 여러 서브클래스에 중복 | 전략 객체를 여러 클래스가 공유 |
| 클래스 수 | 조합마다 새 클래스 필요 | 행동별로만 클래스 존재 |