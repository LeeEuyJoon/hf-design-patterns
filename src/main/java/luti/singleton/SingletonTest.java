package luti.singleton;

public class SingletonTest {

	public static void main(String[] args) throws InterruptedException {
		Runnable task = () -> {
			Singleton singleton = Singleton.getUniqueInstance();
			System.out.println(Thread.currentThread().getName() + " -> " + singleton);
		};

		Thread t1 = new Thread(task, "thread-1");
		Thread t2 = new Thread(task, "thread-2");

		t1.start();
		t2.start();

		t1.join();
		t2.join();
	}
}