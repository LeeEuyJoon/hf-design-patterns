# Command Pattern (커맨드 패턴)

## 개요

**요청을 객체로 캡슐화하여, 요청을 보내는 객체와 처리하는 객체를 분리한다.**
커맨드 패턴을 사용하면 요청을 큐에 저장하거나, 로깅하거나, 취소(undo)하는 기능을 쉽게 구현할 수 있다.

---

## 코드 구조

```
command/
├── remote/                             ← 기본 커맨드 패턴
│   ├── Command.java (interface)        ← execute()
│   ├── RemoteControl.java              ← Invoker: 7슬롯 리모컨
│   ├── NoCommand.java                  ← Null Object 패턴
│   ├── Light / CeilingFan / GarageDoor
│   │   Stereo / Hottub                 ← Receiver: 실제 동작 수행
│   └── LightOnCommand / LightOffCommand
│       CeilingFanOnCommand / ...       ← ConcreteCommand: Receiver에 위임
│
├── remoteWL/                           ← 람다 버전
│   ├── Command.java (@FunctionalInterface)
│   └── RemoteControl.java              ← NoCommand 없이 () -> {} 람다 사용
│
└── undo/                               ← Undo 기능 추가
    ├── Command.java (interface)        ← execute() + undo()
    ├── RemoteControlWithUndo.java      ← 마지막 커맨드를 undoCommand로 저장
    ├── CeilingFanHighCommand / ...     ← prevSpeed로 이전 상태 기억
    └── DimmerLightOnCommand / ...      ← 연속값 상태의 undo 예시
```

---

## 등장인물

| 역할 | 클래스 | 설명 |
|---|---|---|
| **Invoker** | `RemoteControl` | 커맨드를 실행시키는 버튼. 누가 처리하는지 모름 |
| **Command** | `Command` (interface) | `execute()` 계약만 정의 |
| **ConcreteCommand** | `LightOnCommand` 등 | Receiver를 감싸고 `execute()`에서 위임 |
| **Receiver** | `Light`, `CeilingFan` 등 | 실제로 일을 하는 객체 |
| **Client** | `RemoteLoader` | Command 객체를 만들어 Invoker에 등록 |

---

## 동작 흐름

```
RemoteLoader (Client)
    └─→ new LightOnCommand(light)       ← Receiver를 Command에 주입
    └─→ remoteControl.setCommand(0, onCmd, offCmd)  ← Invoker에 등록

remoteControl.onButtonWasPushed(0)      ← Invoker가 버튼 실행
    └─→ onCommands[0].execute()         ← Command 인터페이스만 알면 됨
            └─→ light.on()              ← Receiver가 실제 동작 수행
```

`RemoteControl`은 `Light`를 전혀 모른다.
`Command` 인터페이스만 통해 요청하고, **누가 처리하는지는 런타임에 주입된 커맨드가 결정**한다.

---

## Null Object 패턴 (NoCommand)

슬롯에 아무 커맨드도 등록되지 않은 경우, `null` 대신 아무것도 하지 않는 `NoCommand`를 넣는다.

```java
// 초기화 시 모든 슬롯을 NoCommand로 채움
Command noCommand = new NoCommand();
for (int i = 0; i < 7; i++) {
    onCommands[i] = noCommand;
    offCommands[i] = noCommand;
}
```

덕분에 `onCommands[i] != null` 같은 방어 코드 없이 그냥 `execute()`를 호출할 수 있다.

---

## 람다로 Command 구현 (remoteWL)

`Command`가 메서드 하나짜리 인터페이스(함수형 인터페이스)이므로, 람다로 대체할 수 있다.

```java
// 기존 방식
remoteControl.setCommand(0, new LightOnCommand(light), new LightOffCommand(light));

// 람다 방식
remoteControl.setCommand(0, () -> light.on(), () -> light.off());
```

`NoCommand` 객체도 람다로 대체된다.

```java
// 기존
Command noCommand = new NoCommand();

// 람다
onCommands[i] = () -> {};
```

---

## Undo 기능 (undo)

`Command` 인터페이스에 `undo()`를 추가하고, 각 커맨드가 이전 상태를 기억하도록 한다.

```java
// undo/Command.java
public interface Command {
    public void execute();
    public void undo();
}
```

`RemoteControlWithUndo`는 마지막으로 실행된 커맨드를 `undoCommand`로 저장한다.

```java
public void onButtonWasPushed(int slot) {
    onCommands[slot].execute();
    undoCommand = onCommands[slot];   // 마지막 커맨드 기억
}

public void undoButtonWasPushed() {
    undoCommand.undo();
}
```

### 이전 상태를 저장하는 방법

단순 On/Off 토글이라면 반대 커맨드를 실행하면 되지만,
`CeilingFan`처럼 여러 단계(HIGH/MEDIUM/LOW/OFF)가 있는 경우 이전 속도를 필드에 저장한다.

```java
// CeilingFanHighCommand.java
public void execute() {
    prevSpeed = ceilingFan.getSpeed();  // undo를 위해 현재 상태 저장
    ceilingFan.high();
}

public void undo() {
    if (prevSpeed == CeilingFan.HIGH)   ceilingFan.high();
    else if (prevSpeed == CeilingFan.MEDIUM) ceilingFan.medium();
    else if (prevSpeed == CeilingFan.LOW)    ceilingFan.low();
    else if (prevSpeed == CeilingFan.OFF)    ceilingFan.off();
}
```

---

## 핵심 설계 원칙

**요청을 보내는 쪽과 처리하는 쪽을 분리하라**

`RemoteControl`(Invoker)은 `Light`(Receiver)를 전혀 모른다.
`Command` 인터페이스 하나로 연결되어 있을 뿐이다.
덕분에 새 기기(`TV`, `Hottub` 등)가 추가되어도 `RemoteControl` 코드는 변경 없이 그냥 새 커맨드 클래스만 추가하면 된다.

---

## 요약

| | 설명 |
|---|---|
| **remote** | 기본 커맨드 패턴. Invoker/Command/Receiver 분리, NoCommand로 null 방지 |
| **remoteWL** | Command를 함수형 인터페이스로 선언해 람다로 간결하게 사용 |
| **undo** | `undo()` 메서드 추가, 커맨드가 이전 상태를 저장해 되돌리기 지원 |
