### Head First Design Patterns 학습 기록

<br>

| # | 패턴                                                                          | 설명 |
|---|-----------------------------------------------------------------------------|------|
| 1 | [Strategy Pattern (전략 패턴)](src/main/java/luti/strategy/strategy-pattern.md) | 알고리즘군을 캡슐화하고, 이를 사용할 주체가 composition으로 갖도록 하여 런타임에 교체 가능하게 만든다 |
| 2 | [Observer Pattern (옵저버 패턴)](src/main/java/luti/observer/observer-pattern.md) | Subject의 상태 변화를 구독한 Observer들에게 자동으로 전파한다. 인터페이스를 통한 느슨한 결합으로 Subject와 Observer가 독립적으로 확장된다 |
| 3 | [Decorator Pattern (데코레이터 패턴)](src/main/java/luti/decorator/decorator-pattern.md) | 객체를 감싸는 방식으로 기능을 동적으로 추가한다. 상속 없이 조합만으로 유연하게 확장하며, 기존 코드를 변경하지 않는다 (OCP) |
| 4 | [Factory Pattern (팩토리 패턴)](src/main/java/luti/factory/factory-pattern.md) | 객체 생성 코드를 캡슐화한다. Factory Method는 서브클래스가 어떤 객체를 만들지 결정하고, Abstract Factory는 관련 객체들의 제품군 전체를 팩토리 객체로 교체한다 |
| 5 | Singleton Pattern (싱글턴 패턴) | 클래스 인스턴스가 하나뿐임을 보장하고, 어디서든 그 인스턴스에 접근할 수 있는 전역 접근점을 제공한다 |
| 6 | [Command Pattern (커맨드 패턴)](src/main/java/luti/command/command-pattern.md) | 요청을 객체로 캡슐화하여 요청을 보내는 쪽과 처리하는 쪽을 분리한다. Undo, 람다 커맨드까지 단계별로 구현한다 |
| 7 | [Adapter Pattern (어댑터 패턴)](src/main/java/luti/adapter/adapter-pattern.md) | 호환되지 않는 인터페이스를 클라이언트가 기대하는 인터페이스로 변환한다. 기존 코드 수정 없이 함께 동작할 수 없었던 클래스들을 연결한다 |
| 8 | [Facade Pattern (퍼사드 패턴)](src/main/java/luti/facade/facade-pattern.md) | 복잡한 서브시스템을 단순한 인터페이스 하나로 감싼다. 클라이언트는 내부 구조를 몰라도 되고, 서브시스템 변경이 클라이언트에 미치는 영향을 최소화한다 |
| 9 | [Template Method Pattern (템플릿 메서드 패턴)](src/main/java/luti/templatemethod/templatemethod-pattern.md) | 알고리즘의 골격을 상위 클래스에서 정의하고 세부 단계는 서브클래스가 채운다. Hook으로 서브클래스가 선택적으로 알고리즘 흐름에 개입할 수 있다 |
| 10 | [Iterator Pattern (반복자 패턴)](src/main/java/luti/iterator/iterator-pattern.md) | 컬렉션의 내부 자료구조를 노출하지 않고 동일한 인터페이스로 순회한다. 클라이언트는 배열인지 리스트인지 몰라도 되고, 새 컬렉션 추가 시 순회 코드를 수정할 필요가 없다 |
| 11 | [Composite Pattern (컴포지트 패턴)](src/main/java/luti/composite/composite-pattern.md) | 개별 객체와 복합 객체를 동일한 인터페이스로 다룬다. 트리 구조로 객체를 구성하고, 클라이언트는 Leaf인지 Composite인지 구분하지 않아도 된다 |

<br>

**객체지향 디자인 원칙**
1. 바뀌는 부분은 캡슐화한다.
2. 상속보다는 구성을 활용한다.
3. 구현보다는 인터페이스에 맞춰서 프로그래밍한다.
4. 상호작용하는 객체 사이에서는 가능하면 느슨한 결합을 사용해야 한다.
5. 클래스는 확장에는 열려 있어야 하지만 변경에는 닫혀 있어야 한다 (OCP).
6. 추상화에 의존하라. 구체 클래스에 의존하면 안 된다 (DIP).
