package luti.templatemethod;

public class BeverageTestDrive {
	public static void main(String[] args) {

		TeaWithHook teaHook = new TeaWithHook();
		CoffeeWithHook coffeeHook = new CoffeeWithHook();

		System.out.println("\n 홍차 준비 중 ...");
		teaHook.prepareRecipe();

		System.out.println("\n 커피 준비 중 ...");
		coffeeHook.prepareRecipe();
	}
}
