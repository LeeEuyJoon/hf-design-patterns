# Facade Pattern (퍼사드 패턴)

## 개요

**복잡한 서브시스템을 단순한 인터페이스 하나로 감싸 외부에 제공한다.**
클라이언트는 내부 서브시스템의 복잡한 구조를 알 필요 없이 퍼사드만 통해 원하는 작업을 수행할 수 있다.

---

## 코드 구조

```
facade/
└── hometheater/
    ├── HomeTheaterFacade.java     ← Facade: 단순화된 인터페이스 제공
    ├── HomeTheaterTestDrive.java  ← 클라이언트
    │
    └── (서브시스템 클래스들)
        ├── Amplifier.java         ← 앰프
        ├── Tuner.java             ← 라디오 튜너
        ├── StreamingPlayer.java   ← 스트리밍 플레이어
        ├── CdPlayer.java          ← CD 플레이어
        ├── Projector.java         ← 프로젝터
        ├── TheaterLights.java     ← 조명
        ├── Screen.java            ← 스크린
        └── PopcornPopper.java     ← 팝콘 기계
```

---

## 동작 흐름

퍼사드 없이 영화를 보려면 클라이언트가 서브시스템 8개를 직접 순서대로 제어해야 한다.

```java
// 퍼사드 없이 직접 제어
popper.on();
popper.pop();
lights.dim(10);
screen.down();
projector.on();
projector.wideScreenMode();
amp.on();
amp.setStreamingPlayer(player);
amp.setSurroundSound();
amp.setVolume(5);
player.on();
player.play(movie);
```

퍼사드를 사용하면 이 모든 흐름이 메서드 하나로 줄어든다.

```java
// 퍼사드 사용
homeTheater.watchMovie("Raiders of the Lost Ark");
homeTheater.endMovie();
```

`HomeTheaterFacade`가 제공하는 메서드는 4개다.

| 메서드 | 동작 |
|---|---|
| `watchMovie(movie)` | 팝콘 기계 → 조명 어둡게 → 스크린 내리기 → 프로젝터 → 앰프 → 플레이어 순으로 켜고 재생 |
| `endMovie()` | 역순으로 모두 끄기 |
| `listenToRadio(frequency)` | 튜너 켜고 주파수 맞추기 |
| `endRadio()` | 튜너와 앰프 끄기 |

---

## 퍼사드는 서브시스템을 숨기지 않는다

퍼사드는 서브시스템을 **캡슐화하지 않는다.**
클라이언트가 원하면 여전히 서브시스템 클래스에 직접 접근할 수 있다.

```java
// 퍼사드를 통한 접근
homeTheater.watchMovie("...");

// 서브시스템 직접 접근도 여전히 가능
amp.setStereoSound();
player.setSurroundAudio();
```

퍼사드는 **편의를 위한 단순화 레이어**이지, 서브시스템을 잠그는 것이 아니다.

---

## 최소 지식 원칙 (Principle of Least Knowledge)

퍼사드 패턴은 이 원칙을 잘 보여준다.

> 객체는 자신과 친밀한 클래스만 알아야 한다.

퍼사드 없이 클라이언트가 `Amplifier`, `StreamingPlayer`, `Projector` 등 8개 클래스를 직접 알아야 한다면, 서브시스템 중 하나라도 바뀌면 클라이언트 코드가 영향을 받는다.

퍼사드를 두면 클라이언트는 `HomeTheaterFacade` 하나만 알면 된다.
서브시스템 내부가 바뀌어도 퍼사드 인터페이스가 유지되는 한 클라이언트는 바꿀 필요가 없다.

---

## 어댑터 패턴과의 차이

| | Adapter | Facade |
|---|---|---|
| **목적** | 인터페이스 변환 (호환성) | 인터페이스 단순화 (편의성) |
| **대상** | 단일 클래스/인터페이스 | 여러 클래스로 이루어진 서브시스템 |
| **변환 여부** | 기존 인터페이스를 다른 인터페이스로 변환 | 복잡한 여러 호출을 하나로 묶음 |

---

## 요약

서브시스템 클래스들을 하나씩 직접 다루는 대신 `HomeTheaterFacade`가 올바른 순서로 올바른 컴포넌트를 호출해준다.
클라이언트는 복잡한 홈시어터 시스템의 세부 구조를 몰라도 된다.
