package luti.decorator.condiment;

import luti.decorator.beverage.Beverage;

public abstract class CondimentDecorator extends Beverage {

	Beverage beverage;

	public abstract String getDescription();
}
