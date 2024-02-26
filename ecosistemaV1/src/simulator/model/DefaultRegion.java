package simulator.model;

public class DefaultRegion extends Region {

	public DefaultRegion() {
		super();
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a.get_diet().equals(Diet.CARNIVORE))
			return 0.0;
		else if (a.get_diet().equals(Diet.HERVIBORE)) {
			int n = getHervibore();
			return 60.0 * Math.exp(-Math.max(0, n - 5.0) * 2.0) * dt;
		}
		return 0;
	}

	@Override
	public void update(double dt) {
	}

}
