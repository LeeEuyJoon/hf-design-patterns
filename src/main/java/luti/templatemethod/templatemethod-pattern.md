# Template Method Pattern (템플릿 메서드 패턴)

## 개요

**알고리즘의 골격(순서)을 상위 클래스에서 정의하고, 세부 단계는 서브클래스가 채우도록 한다.**
전체 흐름은 변하지 않고, 달라지는 부분만 서브클래스에 위임한다.

---

## 코드 구조

```
templatemethod/
├── CaffeinBeverage.java          ← 기본 Template Method
├── Coffee.java                   ← brew(), addCondiments() 구현
├── Tea.java                      ← brew(), addCondiments() 구현
│
├── CaffeinBeverageWithHook.java  ← Hook 추가 버전
├── CoffeeWithHook.java           ← hook으로 사용자 입력 반영
├── TeaWithHook.java              ← hook으로 사용자 입력 반영
│
└── BeverageTestDrive.java        ← 실행 진입점
```

---

## 템플릿 메서드란

`prepareRecipe()`가 템플릿 메서드다.
알고리즘의 각 단계를 순서대로 호출하되, `final`로 선언해 서브클래스가 순서를 바꾸지 못하게 막는다.

```java
// CaffeinBeverage.java
final void prepareRecipe() {
    boilWater();       // 구체 메서드 (공통)
    brew();            // 추상 메서드 (서브클래스 구현)
    pourInCup();       // 구체 메서드 (공통)
    addCondiments();   // 추상 메서드 (서브클래스 구현)
}
```

| 단계 | 종류 | 설명 |
|---|---|---|
| `boilWater()` | 구체 메서드 | Coffee/Tea 모두 동일 → 상위 클래스가 제공 |
| `brew()` | 추상 메서드 | Coffee는 필터로, Tea는 우려냄 → 서브클래스가 구현 |
| `pourInCup()` | 구체 메서드 | Coffee/Tea 모두 동일 → 상위 클래스가 제공 |
| `addCondiments()` | 추상 메서드 | Coffee는 설탕/우유, Tea는 레몬 → 서브클래스가 구현 |

---

## 동작 흐름

```
BeverageTestDrive
    └─→ tea.prepareRecipe()                    ← 상위 클래스의 템플릿 메서드 호출
            └─→ boilWater()                    ← CaffeinBeverage 공통 구현
            └─→ brew()                         ← Tea.brew() "찻잎을 우려내는 중"
            └─→ pourInCup()                    ← CaffeinBeverage 공통 구현
            └─→ addCondiments()                ← Tea.addCondiments() "레몬을 추가하는 중"
```

---

## Hook 메서드 (CaffeinBeverageWithHook)

Hook은 상위 클래스에 기본 구현(보통 아무것도 안 하거나 `true`)이 있지만,
서브클래스가 필요할 때 선택적으로 오버라이드해 알고리즘 흐름에 개입할 수 있는 메서드다.

```java
// CaffeinBeverageWithHook.java
final void prepareRecipe() {
    boilWater();
    brew();
    pourInCup();
    if (customerWantsCondiments()) {   // hook으로 흐름 제어
        addCondiments();
    }
}

boolean customerWantsCondiments() {    // hook: 기본값 true
    return true;
}
```

`CoffeeWithHook`은 이 hook을 오버라이드해 사용자 입력을 받아 첨가물 추가 여부를 결정한다.

```java
// CoffeeWithHook.java
@Override
public boolean customerWantsCondiments() {
    String answer = getUserInput();   // "커피에 우유와 설탕을 넣을까요? (y/n)"
    return answer.toLowerCase().startsWith("y");
}
```

**추상 메서드 vs Hook 메서드**

| | 추상 메서드 | Hook 메서드 |
|---|---|---|
| 서브클래스 구현 | 필수 | 선택 |
| 용도 | 달라지는 핵심 단계 구현 | 알고리즘 흐름에 선택적으로 개입 |
| 예시 | `brew()`, `addCondiments()` | `customerWantsCondiments()` |

---

## 헐리우드 원칙 (Hollywood Principle)

> **먼저 연락하지 마세요. 저희가 연락할게요.**
> (Don't call us, we'll call you.)

상위 클래스(`CaffeinBeverage`)가 주도권을 갖는다.
서브클래스(`Coffee`, `Tea`)는 직접 흐름을 제어하지 않고,
상위 클래스가 필요한 시점에 서브클래스의 메서드를 호출한다.

```
CaffeinBeverage (고수준)  →  brew() 호출  →  Coffee (저수준) 응답
```

저수준 컴포넌트가 고수준 컴포넌트를 직접 호출하는 의존성을 끊어,
순환 의존이나 강한 결합을 방지한다.

---

## 요약

| | 기본 (`CaffeinBeverage`) | Hook 추가 (`CaffeinBeverageWithHook`) |
|---|---|---|
| 알고리즘 제어 | 상위 클래스가 고정된 순서로 호출 | 동일하되, hook으로 단계 실행 여부 조건 추가 |
| 서브클래스 역할 | 추상 메서드 구현 (필수) | 추상 메서드 구현 + hook 오버라이드 (선택) |
| 유연성 | 흐름 고정 | hook으로 흐름에 부분 개입 가능 |
