package luti.observer.subject;

import luti.observer.display.Observer;

public interface Subject {
	void registerObserver(Observer o);	// 옵저버 등록
	void removeObserver(Observer o);	// 옵저버 제거
	void notifyObservers();				// 옵저버들에게 변경 내용을 알림
}
