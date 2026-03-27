package luti.factory.pizza;

import luti.factory.Pizza;

public class NYStyleCheesePizza extends Pizza {

	public NYStyleCheesePizza() {
		name = "뉴욕 스타일 소스와 치즈 피자";

		toppings.add("잘게 썬 레지아노 치즈");
	}

	@Override
	protected void prepare() {
		System.out.println("준비 중: " + name);
		System.out.println("도우를 돌리는 중...");
		System.out.println("소스를 뿌리는 중...");
		System.out.println("토핑을 올리는 중...");
		for (String topping : toppings) {
			System.out.println("   " + topping);
		}
	}
}
