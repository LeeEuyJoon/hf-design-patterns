# Iterator Pattern (반복자 패턴)

## 개요

**컬렉션의 내부 구현을 노출하지 않고, 원소를 순서대로 접근하는 방법을 제공한다.**
클라이언트는 컬렉션이 배열인지, 리스트인지, 트리인지 알 필요 없이 동일한 인터페이스로 순회할 수 있다.

---

## 코드 구조

두 버전을 별도 패키지로 분리해 구현했다.

```
iterator/
├── custom/                        ← 직접 정의한 Iterator 인터페이스 버전
│   ├── Iterator.java              ← 커스텀 반복자 인터페이스 (hasNext, next)
│   ├── MenuItem.java
│   ├── DinerMenu.java             ← 배열(MenuItem[])로 메뉴 저장
│   ├── DinerMenuIterator.java     ← 배열용 Iterator 구현체
│   ├── PancakeHouseMenu.java      ← ArrayList로 메뉴 저장
│   ├── PancakeHouseMenuIterator.java ← ArrayList용 Iterator 구현체
│   ├── Waitress.java
│   └── MenuTestDrive.java
│
└── javastd/                       ← java.util.Iterator 사용 버전
    ├── MenuItem.java
    ├── DinerMenu.java
    ├── DinerMenuIterator.java     ← java.util.Iterator<MenuItem> 구현
    ├── PancakeHouseMenu.java      ← ArrayList.iterator() 직접 반환 (별도 클래스 불필요)
    ├── Waitress.java              ← Iterator<MenuItem> 제네릭 사용
    └── MenuTestDrive.java
```

---

## 문제: 자료구조가 다르면 순회 코드도 달라진다

`DinerMenu`는 배열, `PancakeHouseMenu`는 `ArrayList`를 쓴다.
이터레이터 없이 `Waitress`가 두 메뉴를 출력하려면 자료구조를 직접 꺼내서 각자 다르게 순회해야 한다.

```java
// DinerMenu는 배열이라 인덱스 기반 루프
MenuItem[] dinerItems = dinerMenu.getMenuItems();
for (int i = 0; i < dinerItems.length; i++) {
    System.out.println(dinerItems[i].getName());
}

// PancakeHouseMenu는 ArrayList라 for-each
ArrayList<MenuItem> pancakeItems = pancakeHouseMenu.getMenuItems();
for (MenuItem item : pancakeItems) {
    System.out.println(item.getName());
}
```

`Waitress`가 두 메뉴의 **내부 자료구조를 직접 알아야** 한다.
메뉴가 하나 더 추가되거나 자료구조가 바뀌면 `Waitress` 코드도 같이 수정해야 한다.

---

## 해결: Iterator로 순회 방법을 통일

각 메뉴가 `createIterator()`로 자신에 맞는 이터레이터를 반환한다.

```java
// DinerMenu.java - 배열용 이터레이터 반환
public Iterator createIterator() {
    return new DinerMenuIterator(menuItems);
}

// PancakeHouseMenu.java - ArrayList용 이터레이터 반환
public Iterator createIterator() {
    return new PancakeHouseMenuIterator(menuItems);
}
```

`Waitress`는 `Iterator` 인터페이스만 사용해 순회한다.

```java
// Waitress.java
private void printMenu(Iterator iterator) {
    while (iterator.hasNext()) {
        MenuItem menuItem = iterator.next();
        System.out.print(menuItem.getName() + ", ");
        System.out.print(menuItem.getPrice() + " -- ");
        System.out.println(menuItem.getDescription());
    }
}
```

배열이든 `ArrayList`든 `Waitress` 코드는 동일하다.

---

## 동작 흐름

```
MenuTestDrive
    └─→ new Waitress(pancakeMenu, dinerMenu)
    └─→ waitress.printMenu()
            └─→ pancakeMenu.createIterator()  → PancakeHouseMenuIterator
            └─→ dinerMenu.createIterator()    → DinerMenuIterator
            └─→ printMenu(pancakeIterator)    ← Iterator 인터페이스로만 접근
            └─→ printMenu(dinerIterator)      ← 동일한 코드로 순회
```

---

## Iterator 구현체 비교

두 이터레이터는 내부 자료구조만 다르고 인터페이스는 동일하다.

| | `DinerMenuIterator` | `PancakeHouseMenuIterator` |
|---|---|---|
| 내부 자료구조 | `MenuItem[]` (배열) | `ArrayList<MenuItem>` |
| `hasNext()` | 인덱스 범위 + null 체크 | `ArrayList.size()` 비교 |
| `next()` | `items[position++]` | `ArrayList.get(position++)` |

```java
// DinerMenuIterator - 배열이라 null도 체크
public boolean hasNext() {
    return position < items.length && items[position] != null;
}

// PancakeHouseMenuIterator - ArrayList는 size()로 충분
public boolean hasNext() {
    return position < items.size();
}
```

---

## getMenuItems()가 주석 처리된 이유

`DinerMenu`의 `getMenuItems()`는 주석 처리되어 있다.

```java
// public MenuItem[] getMenuItems() {
//     return menuItems;
// }
```

이터레이터 패턴을 적용한 이상 외부에서 배열을 직접 꺼낼 필요가 없다.
`getMenuItems()`를 열어두면 클라이언트가 배열에 직접 의존하게 되어 패턴의 의미가 사라진다.

---

## 핵심 설계 원칙

**단일 역할 원칙 (Single Responsibility Principle)**

컬렉션 클래스(`DinerMenu`)는 메뉴 항목을 관리하는 역할만 한다.
순회 방법은 이터레이터(`DinerMenuIterator`)가 따로 담당한다.
두 역할을 한 클래스에 넣으면 컬렉션 구조가 바뀔 때 순회 코드도 함께 깨진다.

---

## custom vs javastd 비교

| | `custom` | `javastd` |
|---|---|---|
| Iterator 인터페이스 | 직접 정의 (`Iterator.java`) | `java.util.Iterator<T>` |
| 제네릭 | 없음 | `Iterator<MenuItem>` |
| `PancakeHouseMenuIterator` | 별도 클래스 필요 | **불필요** — `ArrayList.iterator()` 직접 반환 |
| `remove()` | 없음 | Java 8+ default 구현 (UnsupportedOperationException) |

두 버전의 차이는 "학습용 vs 실무용"이 아니다.
**인터페이스를 직접 정의했냐, 자바 표준을 가져다 썼냐**의 차이일 뿐이다.

실제 프로젝트에서는 `Iterator`를 직접 다루는 경우 자체가 드물다.
`Iterable<T>`를 구현해 for-each를 쓰거나, Stream API로 처리하는 방식이 일반적이다.

`javastd` 버전의 `PancakeHouseMenu`는 이터레이터 클래스 없이 한 줄로 끝난다.

```java
// javastd/PancakeHouseMenu.java
public Iterator<MenuItem> createIterator() {
    return menuItems.iterator();  // ArrayList가 이미 java.util.Iterator를 구현
}
```

---

## 요약

| | 이터레이터 적용 전 | 이터레이터 적용 후 |
|---|---|---|
| `Waitress`가 알아야 하는 것 | 각 메뉴의 자료구조 (배열, 리스트 ...) | `Iterator` 인터페이스 하나 |
| 새 메뉴 추가 시 | `Waitress` 코드 수정 필요 | `createIterator()` 구현만 추가 |
| 자료구조 변경 시 | `Waitress` 코드 수정 필요 | 해당 이터레이터만 수정 |
