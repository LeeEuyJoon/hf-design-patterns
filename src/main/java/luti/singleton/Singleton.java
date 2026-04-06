package luti.singleton;

public class Singleton {

	private static Singleton uniqueInstance;

	private Singleton() {}

	public static Singleton getUniqueInstance() {
		if (uniqueInstance == null) {
			try {
				Thread.sleep(100); // 일부러 생성 타이밍 겹치게 함
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			uniqueInstance = new Singleton();
		}
		return uniqueInstance;
	}
}
