package luti.factory.store;

import luti.factory.*;
import luti.factory.pizza.*;

public class NYPizzaStore extends PizzaStore {

	@Override
	public Pizza createPizza(String type) {
		Pizza pizza = null;

		if (type.equals("cheese")) {
			pizza = new NYStyleCheesePizza();
		} else if (type.equals("veggie")) {
			pizza = new NYStyleVeggiePizza();
		} else if (type.equals("clam")) {
			pizza = new NYStyleClamPizza();
		} else if (type.equals("pepperoni")) {
			pizza = new NYStylePepperoniPizza();
		}

		return pizza;
	}
}
