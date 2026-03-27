package luti.factory;

import luti.factory.ingredient.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Pizza {

	protected String name;
	protected Dough dough;
	protected Sauce sauce;
	protected Cheese cheese;
	protected Veggies[] veggies;
	protected Pepperoni pepperoni;
	protected Clams clam;
	protected List<String> toppings = new ArrayList<String>();

	protected abstract void prepare();

	protected void bake() {
		System.out.println("175도에서 25분간 굽기");
	}

	protected void cut() {
		System.out.println("피자를 사선으로 자르기");
	}

	protected void box() {
		System.out.println("피자를 상자에 담기");
	}

	public String getName() {
		return name;
	}

}
