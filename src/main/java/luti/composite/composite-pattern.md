# Composite Pattern (컴포지트 패턴)

## 개요

**개별 객체(Leaf)와 복합 객체(Composite)를 동일한 인터페이스로 다룬다.**
트리 구조로 객체를 구성하고, 클라이언트는 단일 객체든 복합 객체든 같은 방식으로 사용할 수 있다.

---

## 코드 구조

```
composite/
├── menu/                          ← 기본 컴포지트 패턴
│   ├── MenuComponent.java         ← Component: 공통 인터페이스
│   ├── Menu.java                  ← Composite: 자식을 가질 수 있는 노드
│   ├── MenuItem.java              ← Leaf: 자식이 없는 단말 노드
│   ├── Waitress.java              ← 클라이언트
│   └── MenuTestDrive.java
│
└── menuiterator/                  ← 컴포지트 + Iterator
    ├── MenuComponent.java         ← createIterator() 추가
    ├── Menu.java                  ← CompositeIterator 반환
    ├── MenuItem.java              ← NullIterator 반환
    ├── CompositeIterator.java     ← Stack으로 트리 DFS 순회
    ├── NullIterator.java          ← Leaf용 Null Object
    ├── Waitress.java              ← printVegetarianMenu() 추가
    └── MenuTestDrive.java
```

---

## 등장인물

| 역할 | 클래스 | 설명 |
|---|---|---|
| **Component** | `MenuComponent` | Leaf와 Composite 모두가 구현하는 공통 추상 클래스 |
| **Leaf** | `MenuItem` | 자식이 없는 단말 노드. 메뉴 항목 하나 |
| **Composite** | `Menu` | 자식(`MenuComponent`)을 가질 수 있는 노드. 메뉴 전체 또는 서브메뉴 |
| **Client** | `Waitress` | `MenuComponent`만 바라봄. Leaf인지 Composite인지 구분하지 않음 |

---

## 트리 구조

```
ALL MENUS
├── PANCAKE HOUSE MENU (Menu)
│   ├── K&B's Pancake Breakfast (MenuItem)
│   ├── Regular Pancake Breakfast (MenuItem)
│   └── ...
├── DINER MENU (Menu)
│   ├── Vegetarian BLT (MenuItem)
│   ├── ...
│   └── DESSERT MENU (Menu)          ← 메뉴 안에 메뉴 중첩 가능
│       ├── Apple Pie (MenuItem)
│       └── ...
└── CAFE MENU (Menu)
    ├── Veggie Burger (MenuItem)
    ├── ...
    └── COFFEE MENU (Menu)           ← 메뉴 안에 메뉴 중첩 가능
        └── ...
```

`Menu` 안에 `Menu`를 넣을 수 있다. 깊이 제한이 없다.

---

## 재귀 출력 (menu)

`Waitress`는 `allMenus.print()` 한 번만 호출한다.

```java
// Waitress.java
public void printMenu() {
    allMenus.print();   // Menu든 MenuItem이든 같은 메서드
}
```

`Menu.print()`는 자신의 이름을 출력한 뒤 자식들의 `print()`를 재귀 호출한다.

```java
// Menu.java
public void print() {
    System.out.println(getName() + ", " + getDescription());
    Iterator<MenuComponent> iterator = menuComponents.iterator();
    while (iterator.hasNext()) {
        iterator.next().print();   // Leaf면 MenuItem.print(), Composite면 재귀
    }
}
```

`MenuItem.print()`는 자신의 정보만 출력하고 끝낸다.
클라이언트는 트리 구조를 전혀 신경 쓰지 않아도 된다.

---

## UnsupportedOperationException 전략

`MenuComponent`는 Leaf와 Composite의 모든 메서드를 정의하지만,
지원하지 않는 연산은 기본적으로 `UnsupportedOperationException`을 던진다.

```java
// MenuComponent.java
public void add(MenuComponent menuComponent) {
    throw new UnsupportedOperationException();  // MenuItem은 자식 추가 불가
}
public double getPrice() {
    throw new UnsupportedOperationException();  // Menu는 가격이 없음
}
```

`MenuItem`은 `add()`/`remove()`를 오버라이드하지 않고,
`Menu`는 `getPrice()`/`isVegetarian()`을 오버라이드하지 않는다.

이 방식은 **투명성(Transparency)**을 택한 것이다.
클라이언트가 Leaf/Composite 구분 없이 동일하게 다룰 수 있지만,
잘못된 메서드 호출은 런타임에서야 드러난다.

---

## CompositeIterator — 트리 전체 순회 (menuiterator)

`menu` 버전의 `print()`는 재귀 호출로 트리를 순회하지만,
이는 `Menu` 내부에 순회 로직이 묻혀 있는 구조다.

`menuiterator` 버전은 `createIterator()`를 `MenuComponent`에 추가해
**외부에서도 트리 전체를 순회**할 수 있게 한다.

```java
// menuiterator/MenuComponent.java
public abstract Iterator<MenuComponent> createIterator();

// Menu.java → CompositeIterator 반환 (자식 포함 DFS)
public Iterator<MenuComponent> createIterator() {
    return new CompositeIterator(menuComponents.iterator());
}

// MenuItem.java → NullIterator 반환 (자식 없음)
public Iterator<MenuComponent> createIterator() {
    return new NullIterator();
}
```

`CompositeIterator`는 `Stack`을 사용해 트리를 DFS로 순회한다.
`Menu`를 만나면 그 자식 이터레이터를 스택에 쌓고, `MenuItem`을 만나면 그냥 반환한다.

```java
// CompositeIterator.java
public MenuComponent next() {
    Iterator<MenuComponent> iterator = stack.peek();
    MenuComponent component = iterator.next();
    stack.push(component.createIterator());  // 자식 이터레이터를 스택에 추가
    return component;
}
```

이를 통해 `Waitress`는 트리 구조를 몰라도 채식 메뉴만 뽑을 수 있다.

```java
// Waitress.java
public void printVegetarianMenu() {
    Iterator<MenuComponent> iterator = allMenus.createIterator();
    while (iterator.hasNext()) {
        MenuComponent menuComponent = iterator.next();
        try {
            if (menuComponent.isVegetarian()) {
                menuComponent.print();
            }
        } catch (UnsupportedOperationException e) {}  // Menu는 isVegetarian() 없음
    }
}
```

---

## menu vs menuiterator 비교

| | menu | menuiterator |
|---|---|---|
| 순회 방식 | `print()` 재귀 호출 (내부 순회) | `createIterator()`로 외부 순회 |
| 횡단 관심사 | 불가 (print 안에 묶임) | 가능 (채식 필터 등 외부에서 적용) |
| Null Object | 없음 | `NullIterator` (Leaf의 createIterator) |
| 복잡도 | 단순 | CompositeIterator + Stack 필요 |

---

## 요약

컴포지트 패턴의 핵심은 **"단일 객체와 복합 객체를 같은 타입으로 취급"**하는 것이다.
`Waitress`는 `allMenus`가 `MenuItem` 하나인지, 수십 개의 서브메뉴를 가진 트리인지 알 필요가 없다.
`print()` 한 번이면 트리 전체를 순회한다.
