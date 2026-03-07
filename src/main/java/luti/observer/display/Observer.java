package luti.observer.display;

public interface Observer {
	void update(float temp, float humidity, float pressure);	// Subject에서 변경된 내용을 전달받아 업데이트하는 메서드
}
