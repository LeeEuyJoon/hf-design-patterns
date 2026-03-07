### Head First Design Patterns 학습 기록

<br>

| # | 패턴                                                                          | 설명 |
|---|-----------------------------------------------------------------------------|------|
| 1 | [Strategy Pattern (전략 패턴)](src/main/java/luti/strategy/strategy-pattern.md) | 알고리즘군을 캡슐화하고, 이를 사용할 주체가 composition으로 갖도록 하여 런타임에 교체 가능하게 만든다 |
| 2 | [Observer Pattern (옵저버 패턴)](src/main/java/luti/observer/observer-pattern.md) | Subject의 상태 변화를 구독한 Observer들에게 자동으로 전파한다. 인터페이스를 통한 느슨한 결합으로 Subject와 Observer가 독립적으로 확장된다 |

<br>

**객체지향 디자인 원칙**
1. 애플리케이션에서 달라지는 부분을 찾아내고, 달라지지 않는 부분과 분리한다.
2. 구현보다는 인터페이스에 맞춰서 프로그래밍한다.
3. 상속보다는 구성을 활용한다.