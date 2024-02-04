package simulator.model;

import java.util.List;
import java.util.Iterator;

import org.json.JSONObject;

public abstract class Region implements Entity, RegionInfo, FoodSupplier {

	protected List<Animal> animales;

	public Region(JSONObject j) {

	}

	@Override
	public abstract double get_food(Animal a, double dt);

	@Override
	public abstract void update(double dt);

	final void add_animal(Animal a) {

		animales.add(a);
	}

	final void remove_animal(Animal a) {

		animales.remove(a);
	}

	final List<Animal> getAnimals() {

		return animales;
	}

	protected int getHervibore() {

		int n = 0;
		Iterator<Animal> i = animales.iterator();
		while (i.hasNext()) {
			Animal a = i.next();
			if (a.get_diet().equals(Diet.HERVIBORE))
				n++;
		}
		return n;
	}

	public JSONObject as_JSON() {

		JSONObject jo = new JSONObject();
		Iterator<Animal> i = animales.iterator();
		while (i.hasNext()) {
			Animal a = i.next();
			jo.put("queso", a.as_JSON());
		}

		return jo;
	}

}
