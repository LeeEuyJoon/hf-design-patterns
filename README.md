### Head First Design Patterns 학습 기록

<br>

| # | 패턴                                                                          | 설명 |
|---|-----------------------------------------------------------------------------|------|
| 1 | [Strategy Pattern (전략 패턴)](src/main/java/luti/strategy/strategy-pattern.md) | 알고리즘군을 캡슐화하고, 이를 사용할 주체가 composition으로 갖도록 하여 런타임에 교체 가능하게 만든다 |
| 2 | [Observer Pattern (옵저버 패턴)](src/main/java/luti/observer/observer-pattern.md) | Subject의 상태 변화를 구독한 Observer들에게 자동으로 전파한다. 인터페이스를 통한 느슨한 결합으로 Subject와 Observer가 독립적으로 확장된다 |
| 3 | [Decorator Pattern (데코레이터 패턴)](src/main/java/luti/decorator/decorator-pattern.md) | 객체를 감싸는 방식으로 기능을 동적으로 추가한다. 상속 없이 조합만으로 유연하게 확장하며, 기존 코드를 변경하지 않는다 (OCP) |
| 4 | [Factory Pattern (팩토리 패턴)](src/main/java/luti/factory/factory-pattern.md) | 객체 생성 코드를 캡슐화한다. Factory Method는 서브클래스가 어떤 객체를 만들지 결정하고, Abstract Factory는 관련 객체들의 제품군 전체를 팩토리 객체로 교체한다 |
| 5 | Singleton Pattern (싱글턴 패턴) | 클래스 인스턴스가 하나뿐임을 보장하고, 어디서든 그 인스턴스에 접근할 수 있는 전역 접근점을 제공한다 |
| 6 | [Command Pattern (커맨드 패턴)](src/main/java/luti/command/command-pattern.md) | 요청을 객체로 캡슐화하여 요청을 보내는 쪽과 처리하는 쪽을 분리한다. |

<br>

**객체지향 디자인 원칙**
1. 바뀌는 부분은 캡슐화한다.
2. 상속보다는 구성을 활용한다.
3. 구현보다는 인터페이스에 맞춰서 프로그래밍한다.
4. 상호작용하는 객체 사이에서는 가능하면 느슨한 결합을 사용해야 한다.
5. 클래스는 확장에는 열려 있어야 하지만 변경에는 닫혀 있어야 한다 (OCP).
6. 추상화에 의존하라. 구체 클래스에 의존하면 안 된다 (DIP).
