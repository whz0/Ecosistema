package simulator.model;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region {

	private double _food;
	private double _factor;

	public DynamicSupplyRegion(double food, double factor) {
		super();
		if (food <= 0 || factor <= 0)
			throw new IllegalArgumentException("food and factor can`t be negative or zero");
		this._food = food;
		this._factor = factor;
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a.get_diet().equals(Diet.CARNIVORE))
			return 0.0;
		else if (a.get_diet().equals(Diet.HERVIBORE)) {
			int n = getHervibore();
			double spendFood = Math.min(this._food, 60.0 * Math.exp(-Math.max(0, n - 5.0) * 2.0) * dt);
			this._food -= spendFood;
			return spendFood;
		} else
			return 0;
	}

	@Override
	public void update(double dt) {

		if (Utils._rand.nextDouble() >= 0.5)
			this._food += dt * this._factor;
	}
}
