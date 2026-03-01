package luti.strategy.duck;

import luti.strategy.flybehavior.FlyWithWings;
import luti.strategy.quackbehavior.Quack;

public class MallarDuck extends Duck {

	public MallarDuck() {
		quackBehavior = new Quack();
		flyBehavior = new FlyWithWings();
	}

	public void display() {
		System.out.println("저는 물오리입니다.");
	}

}
