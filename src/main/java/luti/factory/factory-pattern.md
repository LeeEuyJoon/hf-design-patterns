# Factory Pattern (팩토리 패턴)

## 개요

**객체 생성 코드를 별도의 클래스/메서드로 캡슐화하여, 클라이언트가 구체 클래스에 의존하지 않도록 한다.**

이 패키지에는 두 가지 팩토리 패턴이 함께 구현되어 있다.

- **Factory Method Pattern**: 서브클래스가 어떤 클래스를 인스턴스화할지 결정한다.
- **Abstract Factory Pattern**: 관련 객체들의 집합(제품군)을 생성하는 인터페이스를 제공한다.

---

## 코드 구조

```
factory/
├── Pizza.java (abstract)              ← 피자 베이스 클래스
├── PizzaStore.java (abstract)         ← 팩토리 메서드 패턴의 Creator
├── PizzaIngredientFactory.java        ← 추상 팩토리 인터페이스
├── PizzaTestDrive.java                ← 실행 진입점
│
├── store/
│   ├── NYPizzaStore.java              ← createPizza() 구현 (NY 스타일)
│   └── ChicagoPizzaStore.java         ← createPizza() 구현 (Chicago 스타일)
│
├── pizza/
│   ├── NYStyleCheesePizza.java        ← Factory Method용 구체 피자
│   ├── NYStyleClamPizza.java
│   ├── NYStylePepperoniPizza.java
│   ├── NYStyleVeggiePizza.java
│   ├── ChicagoStyleCheesePizza.java
│   ├── ChicagoStyleClamPizza.java
│   ├── ChicagoStylePepperoniPizza.java
│   ├── ChicagoStyleVeggiePizza.java
│   ├── CheesePizza.java               ← Abstract Factory용 피자 (재료를 팩토리에서 받음)
│   └── ClamPizza.java
│
└── ingredient/
    ├── Dough.java (interface)         ← 재료 인터페이스들
    ├── Sauce.java (interface)
    ├── Cheese.java (interface)
    ├── Veggies.java (interface)
    ├── Pepperoni.java (interface)
    ├── Clams.java (interface)
    ├── NYPizzaIngredientFactory.java   ← 추상 팩토리 구현체 (NY 재료)
    ├── ChicagoPizzaIngredientFactory.java ← 추상 팩토리 구현체 (Chicago 재료)
    │
    ├── ny/
    │   ├── ThinCrustDough.java        ← NY 스타일 재료 구현체들
    │   ├── MarinaraSauce.java
    │   ├── ReggianoCheese.java
    │   ├── Garlic.java / Onion.java / Mushroom.java / RedPepper.java
    │   ├── SlicedPepperoni.java
    │   └── FreshClams.java
    │
    └── chicago/
        ├── ThickCrustDough.java       ← Chicago 스타일 재료 구현체들
        ├── PlumTomatoSauce.java
        ├── MozzarellaCheese.java
        ├── BlackOlives.java / Spinach.java / EggPlant.java
        └── FrozenClams.java
```

---

## Factory Method Pattern 동작 흐름

```
nyStore.orderPizza("cheese")
    └─→ PizzaStore.orderPizza("cheese")          ← 템플릿 메서드처럼 흐름을 제어
            └─→ createPizza("cheese")            ← 서브클래스(NYPizzaStore)가 구현
                    └─→ new NYStyleCheesePizza() ← 구체 클래스 결정은 서브클래스 담당
            └─→ pizza.prepare()
            └─→ pizza.bake()
            └─→ pizza.cut()
            └─→ pizza.box()
```

`PizzaStore`는 피자를 만드는 흐름(`orderPizza`)을 정의하지만,
**어떤 피자를 만들지**는 `createPizza()`를 통해 서브클래스에게 위임한다.

---

## Abstract Factory Pattern 동작 흐름

```
NYPizzaIngredientFactory factory = new NYPizzaIngredientFactory()
CheesePizza pizza = new CheesePizza(factory)

pizza.prepare()
    └─→ dough  = factory.createDough()   → ThinCrustDough
    └─→ sauce  = factory.createSauce()   → MarinaraSauce
    └─→ cheese = factory.createCheese()  → ReggianoCheese
```

피자 클래스(`CheesePizza`)는 재료를 직접 알지 못한다.
`PizzaIngredientFactory` 인터페이스를 통해서만 재료를 요청하고,
**어떤 재료가 오는지는 주입된 팩토리가 결정**한다.

---

## 왜 `new`로 직접 생성하면 안 될까?

직관적으로는 이렇게 해도 되지 않나 싶다.

```java
// PizzaStore 안에서 직접 생성
public Pizza orderPizza(String type) {
    Pizza pizza;
    if (type.equals("cheese")) {
        pizza = new NYStyleCheesePizza();
    } else if (type.equals("veggie")) {
        pizza = new NYStyleVeggiePizza();
    }
    // ...
}
```

이 방식의 문제는:

### 문제 1: 구체 클래스에 강하게 결합된다

`PizzaStore`가 `NYStyleCheesePizza`, `ChicagoStyleVeggiePizza` 등 구체 클래스를 직접 알고 있어야 한다.
피자 종류가 늘거나, 지역별 스타일이 생길 때마다 `PizzaStore` 코드를 열어야 한다.

### 문제 2: 지역별 변형을 표현하기 어렵다

NY 스토어와 Chicago 스토어는 같은 메뉴(`cheese`, `veggie`, ...)를 팔지만 피자가 다르다.
직접 생성 방식에서는 이 차이를 if-else 분기로 처리해야 하고, 지역이 늘수록 분기가 터진다.

팩토리 메서드는 **"어떤 피자를 만드는가"를 서브클래스로 분리**해서 이 문제를 해결한다.

```java
// NYPizzaStore.java
protected Pizza createPizza(String type) {
    if (type.equals("cheese")) return new NYStyleCheesePizza();
    // ...
}

// ChicagoPizzaStore.java
protected Pizza createPizza(String type) {
    if (type.equals("cheese")) return new ChicagoStyleCheesePizza();
    // ...
}
```

`PizzaStore`(부모)는 구체 피자 클래스를 전혀 모른다.

---

## Factory Method vs Abstract Factory

두 패턴 모두 "객체 생성을 캡슐화"하지만 목적과 규모가 다르다.

### Factory Method: 단일 제품, 서브클래스로 결정

```
PizzaStore (abstract)
    createPizza() ← 이 메서드 하나를 서브클래스가 오버라이드
```

어떤 종류의 피자 객체 하나를 만들지를 서브클래스에 맡긴다.
피자 하나를 생성하는 방법이 변형 포인트다.

### Abstract Factory: 제품군 전체를 팩토리 객체로 교체

```
PizzaIngredientFactory (interface)
    createDough() / createSauce() / createCheese() / ...
```

피자 재료 전체(도우, 소스, 치즈, 야채...)를 하나의 팩토리 객체가 담당한다.
팩토리 객체 자체를 갈아끼우면 제품군 전체가 바뀐다.

```java
// 팩토리만 바꾸면 피자의 모든 재료가 Chicago 스타일로 바뀜
CheesePizza pizza = new CheesePizza(new ChicagoPizzaIngredientFactory());
```

---

## 핵심 설계 원칙

**구체 클래스가 아닌 추상화에 의존하라 (Dependency Inversion Principle)**

`PizzaStore`는 `Pizza`(추상 클래스)에만 의존한다. `NYStyleCheesePizza` 같은 구체 클래스를 전혀 모른다.
`CheesePizza`는 `PizzaIngredientFactory`(인터페이스)에만 의존한다. `NYPizzaIngredientFactory`를 전혀 모른다.

**변하는 것과 변하지 않는 것을 분리하라**

변하는 것: 어떤 피자를 만드는가 (`createPizza`), 어떤 재료를 쓰는가 (각 `create*` 메서드)
변하지 않는 것: 피자를 만드는 과정 (`prepare → bake → cut → box`)

---

## 요약

| | Factory Method | Abstract Factory |
|---|---|---|
| 목적 | 단일 객체 생성을 서브클래스에 위임 | 관련 객체 집합(제품군) 생성 캡슐화 |
| 변형 방법 | 서브클래스 오버라이드 | 팩토리 객체 교체 |
| 코드 예시 | `NYPizzaStore.createPizza()` | `NYPizzaIngredientFactory` |
| 새 지역 추가 | `PizzaStore` 서브클래스 하나 추가 | `PizzaIngredientFactory` 구현체 하나 추가 |
| 클라이언트 코드 변경 | 불필요 | 불필요 |
