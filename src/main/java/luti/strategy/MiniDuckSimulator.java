package luti.strategy;

import luti.strategy.duck.Duck;
import luti.strategy.duck.MallarDuck;
import luti.strategy.duck.ModelDuck;
import luti.strategy.flybehavior.FlyRocketPowered;

public class MiniDuckSimulator {
	public static void main(String[] args) {

		Duck mallard = new MallarDuck();
		mallard.display();
		mallard.performQuack();
		mallard.performFly();

		System.out.println("-----------------------------");

		Duck model = new ModelDuck();
		model.display();
		model.performFly();
		// 모형 오리에게 로켓 추진 날개 장착
		model.setFlyBehavior(new FlyRocketPowered());
		model.performFly();

	}
}
