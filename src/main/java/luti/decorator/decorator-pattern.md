# Decorator Pattern (데코레이터 패턴)

## 개요

**객체에 추가 요소를 동적으로 더한다.**
데코레이터 패턴을 사용하면 서브클래스를 만들 때보다 훨씬 유연하게 기능을 확장할 수 있다.

---

## 코드 구조

```
Beverage (abstract)                     ← 최상위 컴포넌트
├── getDescription()                    ← 음료 설명 반환
├── cost() (abstract)                   ← 가격 계산
│
├── Espresso    extends Beverage        ← 구체 컴포넌트
├── DarkRoast   extends Beverage
├── HouseBlend  extends Beverage
└── Decaf       extends Beverage

CondimentDecorator (abstract)           ← 데코레이터 베이스
    extends Beverage
    ├── beverage: Beverage              ← 감쌀 객체를 "has-a"로 보유
    └── getDescription() (abstract)

    ├── Mocha   extends CondimentDecorator   ← 구체 데코레이터
    ├── Soy     extends CondimentDecorator
    └── Whip    extends CondimentDecorator
```

---

## 동작 흐름

```java
Beverage beverage2 = new DarkRoast();
// beverage2 → [DarkRoast]                      cost: 0.99

beverage2 = new Mocha(beverage2);
// beverage2 → [Mocha → DarkRoast]              cost: 0.99 + 0.20

beverage2 = new Mocha(beverage2);
// beverage2 → [Mocha → Mocha → DarkRoast]      cost: 0.99 + 0.20 + 0.20

beverage2 = new Whip(beverage2);
// beverage2 → [Whip → Mocha → Mocha → DarkRoast]
```

`cost()` 호출 시 바깥에서 안쪽으로 재귀적으로 타고 들어가며 값이 합산된다.

```
Whip.cost()
  └→ Mocha.cost()
       └→ Mocha.cost()
            └→ DarkRoast.cost()  // 0.99 반환
            ← 0.99 + 0.20
       ← 1.19 + 0.20
  ← 1.39 + 0.10
= 1.49
```

---

## 왜 상속만으로는 부족한가?

"모든 음료+첨가물 조합마다 서브클래스를 만들면 되는 거 아닌가?" 라고 생각할 수 있다.
그러나 첨가물 종류가 늘어날수록 상속은 빠르게 무너진다.

### 문제 1 : 클래스 수 폭발

음료 4종 × 첨가물 조합이라고 하면:

```
DarkRoastWithMocha
DarkRoastWithMochaAndWhip
DarkRoastWithMochaAndMochaAndWhip
DarkRoastWithSoyAndMochaAndWhip
HouseBlendWithMocha
HouseBlendWithSoyAndWhip
...
```

첨가물이 하나 추가될 때마다 조합 수는 기하급수적으로 늘어난다.
클래스 파일만 수십, 수백 개가 생긴다.

### 문제 2 : 같은 첨가물을 두 번 추가하기 어렵다

`DarkRoast`에 `Mocha`를 두 번 넣고 싶다면?
상속 구조에서는 `DarkRoastWithDoubleMocha` 클래스를 별도로 만들어야 한다.
데코레이터 패턴에서는 그냥 두 번 감싸면 된다.

```java
beverage = new Mocha(beverage);
beverage = new Mocha(beverage);  // 두 번 감싸기
```

### 문제 3 : 슈퍼클래스 변경이 서브클래스 전체에 영향

`Beverage`에 메서드 하나가 추가되면 수십 개의 서브클래스 전체에 영향이 간다.
조합마다 만든 서브클래스들은 수정 누락이 발생하기 쉽다.

---

## 데코레이터 패턴이 이 문제를 해결하는 방식

### 핵심: 감싸기 (Wrapping)

데코레이터는 자신이 감싸는 객체와 **동일한 타입**을 상속한다.
덕분에 원본 객체 자리에 데코레이터를 투명하게 끼워 넣을 수 있다.

```java
// CondimentDecorator는 Beverage를 상속하면서, 동시에 Beverage를 내부에 보유한다
public abstract class CondimentDecorator extends Beverage {
    Beverage beverage;  // 감쌀 대상
}
```

`Mocha`는 `Beverage`이기도 하면서, 내부에 `Beverage`를 품고 있다.
그래서 `new Mocha(beverage2)` 의 결과를 다시 `Beverage` 타입 변수에 넣을 수 있고,
그것을 또 다른 데코레이터로 감쌀 수 있다.

### 책임 위임

`Mocha.cost()`는 자신의 가격만 알고, 나머지는 내부 객체에 위임한다.

```java
// Mocha.java
public double cost() {
    return beverage.cost() + 0.20;  // 안쪽 객체에 위임 후 자신의 값만 더함
}

public String getDescription() {
    return beverage.getDescription() + ", 모카";
}
```

각 데코레이터는 자신의 역할만 담당하므로, 추가해도 기존 코드를 전혀 건드리지 않는다.

---

## CondimentDecorator가 Beverage를 상속하는 이유

상속이 타입 맞추기(type matching)를 위해 사용된다는 점이 이 패턴의 핵심 포인트다.
기능 확장을 위해 상속하는 것이 아니다.

- 기능 확장은 내부에 보유한 `beverage` 객체에 **위임**하는 방식으로 이루어진다.
- 상속은 단지 데코레이터가 원본 컴포넌트와 같은 타입으로 취급받기 위해 사용된다.

```
Beverage beverage2 = new Mocha(new DarkRoast());
//  ↑ Beverage 타입으로 받을 수 있는 이유:
//    Mocha → CondimentDecorator → Beverage 를 상속하기 때문
```

---

## 핵심 설계 원칙

**클래스는 확장에는 열려 있고, 변경에는 닫혀 있어야 한다 (OCP)**

새 첨가물(`Oat`, `Vanilla` 등)을 추가할 때 기존 코드를 전혀 수정하지 않는다.
새 데코레이터 클래스 하나를 만들어 끼워 넣으면 된다.

**상속보다 합성을 활용하라**

데코레이터는 기능을 물려받는 것이 아니라, 내부 객체에 **위임**함으로써 기능을 확장한다.
`Mocha`는 나는 방법을 부모에게서 물려받지 않는다. 내부 `beverage`에 요청을 넘길 뿐이다.

---

## 요약

| | 상속만 사용 | 데코레이터 패턴 |
|---|---|---|
| 첨가물 추가 시 | 조합마다 새 서브클래스 | 데코레이터 클래스 하나만 추가 |
| 같은 첨가물 중복 추가 | 별도 클래스 필요 | 동일 데코레이터를 여러 번 감싸면 됨 |
| 런타임 조합 변경 | 불가능 | 감싸는 순서/횟수를 자유롭게 조합 |
| 클래스 수 | 조합 수만큼 폭발적 증가 | 음료 수 + 첨가물 수만 존재 |
| 기존 코드 수정 | 부모 변경 시 전체 영향 | 기존 코드 건드리지 않음 (OCP) |
