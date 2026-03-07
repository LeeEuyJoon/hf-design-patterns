# Observer Pattern (옵저버 패턴)

## 개요

**한 객체의 상태가 바뀌면, 그 객체에 의존하는 다른 객체들에게 자동으로 알림이 가고 업데이트된다.**
Subject와 Observer는 서로 인터페이스를 통해서만 알기 때문에, 느슨하게 결합(Loose Coupling)된다.

---

## 코드 구조

```
observer/
├── WeatherStation.java                  ← 실행 진입점
│
├── subject/
│   ├── Subject.java (interface)         ← registerObserver / removeObserver / notifyObservers
│   └── WeatherData.java                 ← Subject 구현체, 데이터 발행자
│
└── display/
    ├── Observer.java (interface)         ← update(temp, humidity, pressure)
    ├── DisplayElement.java (interface)   ← display()
    ├── CurrentConditionsDisplay.java     ← 현재 온도/습도 출력
    ├── StatisticsDisplay.java            ← 평균/최고/최저 온도 출력
    └── ForecastDisplay.java              ← 기압 변화로 날씨 예측 출력
```

---

## 동작 흐름

```
weatherData.setMeasurements(80, 65, 30.4f)
    └─→ measurementsChanged()
            └─→ notifyObservers()
                    ├─→ currentDisplay.update(80, 65, 30.4f)   → display()
                    ├─→ statisticsDisplay.update(80, 65, 30.4f) → display()
                    └─→ forecastDisplay.update(80, 65, 30.4f)  → display()
```

`WeatherData`는 데이터가 바뀌면 `notifyObservers()`를 호출하고, 등록된 모든 Observer가 순서대로 `update()`를 받는다.

---

## 왜 그냥 Display 클래스들을 WeatherData 안에서 직접 호출하면 안 될까?

직관적으로 생각하면 이런 방식도 가능하다.

```java
public void measurementsChanged() {
    currentDisplay.update(temperature, humidity, pressure);
    statisticsDisplay.update(temperature, humidity, pressure);
    forecastDisplay.update(temperature, humidity, pressure);
    // 새 디스플레이 추가할 때마다 여기를 수정해야 한다
}
```

이 방식의 문제는:

### 문제 1 : WeatherData가 Display를 직접 안다 → 강한 결합

`WeatherData`가 `CurrentConditionsDisplay`, `StatisticsDisplay` 같은 구체 클래스를 직접 알고 있어야 한다.
새 디스플레이를 추가하거나 제거할 때마다 WeatherData 코드를 열어서 수정해야 한다.
데이터를 관리하는 클래스가 UI 출력 방식까지 알아야 하는 건 책임 범위를 벗어난다.

### 문제 2 : 런타임 구독/해지가 불가능하다

특정 디스플레이를 상황에 따라 껐다 켰다 하고 싶어도, 하드코딩된 호출 구조에서는 방법이 없다.
옵저버 패턴에서는 `registerObserver()`와 `removeObserver()`로 언제든지 자유롭게 제어할 수 있다.

```java
// WeatherStation.java
CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
// 생성자 안에서 weatherData.registerObserver(this) 호출 → 자동 구독

// 필요 없어지면
weatherData.removeObserver(currentDisplay); // 구독 해지 → WeatherData 코드 건드릴 필요 없음
```

---

## 느슨한 결합 (Loose Coupling)

이 패턴의 핵심은 Subject와 Observer가 **인터페이스를 통해서만 서로를 안다**는 점이다.

`WeatherData`는 Observer 목록을 `List<Observer>`로 들고 있다.
`CurrentConditionsDisplay`가 어떤 클래스인지 전혀 모른다. `update()`를 가진 Observer라는 것만 알 뿐이다.

덕분에:
- Observer를 새로 만들어도 Subject 코드는 변경 없다
- Subject의 데이터 구조가 바뀌어도 Observer 등록/해지 방식은 그대로다
- Subject와 Observer를 독립적으로 재사용할 수 있다

---

## Push vs Pull

현재 코드는 **Push 방식**이다. Subject가 데이터를 직접 밀어넣는다.

```java
// Subject가 필요한 데이터를 파라미터로 밀어넣음
observer.update(temperature, humidity, pressure);
```

Observer 입장에서는 자신이 필요하지 않은 데이터도 강제로 받게 된다.
`ForecastDisplay`는 `pressure`만 필요하지만 `temp`와 `humidity`도 같이 받는다.

**Pull 방식**은 Subject 자신을 넘겨주고, Observer가 필요한 것만 꺼내가는 방법이다.

```java
// Subject 자신을 넘겨줌
observer.update(this);

// Observer가 필요한 것만 꺼내감
public void update(Subject subject) {
    this.pressure = ((WeatherData) subject).getPressure();
}
```

Pull 방식이 Observer의 자율성이 높고, 데이터 항목이 많아질수록 더 유연하다.
현재 코드는 Push 방식이지만, 데이터 종류가 늘어나면 Pull 방식으로 리팩터링을 고려할 수 있다.

---

## 핵심 설계 원칙

**변하는 것과 변하지 않는 것을 분리하라**
변하는 것: Observer 목록, 각 Observer가 데이터를 가공하는 방식
변하지 않는 것: Subject가 변경 시 Observer에게 알린다는 구조 자체

**구현보다 인터페이스에 맞춰 프로그래밍하라**
`WeatherData`는 `Observer` 인터페이스만 알고, Display 구현체들은 `Subject` 인터페이스만 안다.

---

## 요약

| | 직접 호출 방식 | 옵저버 패턴 |
|---|---|---|
| 새 Display 추가 | WeatherData 코드 수정 | registerObserver() 호출만으로 끝 |
| Display 제거 | WeatherData 코드 수정 | removeObserver() 호출만으로 끝 |
| 런타임 구독 제어 | 불가능 | 자유롭게 가능 |
| Subject-Observer 결합도 | 강함 (구체 클래스 직접 참조) | 약함 (인터페이스만 참조) |