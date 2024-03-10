package simulator.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable {

	private Factory<Animal> animal_factory;
	private Factory<Region> regions_factory;
	private RegionManager manager;
	private List<Animal> animales;
	private double time;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory,
			Factory<Region> regions_factory) {
		this.manager = new RegionManager(cols, rows, width, height);
		this.animales = new ArrayList<Animal>();
		this.animal_factory = animals_factory;
		this.regions_factory = regions_factory;
		this.time = 0.0;
	}

	private void set_region(int row, int col, Region r) {

		this.manager.set_region(row, col, r);
	}

	public void set_region(int row, int col, JSONObject r_json) {

		set_region(row, col, this.regions_factory.create_instance(r_json));
	}

	private void add_animal(Animal a) {

		this.animales.add(a);
		this.manager.register_animal(a);
	}

	public void add_animal(JSONObject a_json) {

		add_animal(this.animal_factory.create_instance(a_json));
	}

	public MapInfo get_map_info() {

		return manager;
	}

	public List<? extends AnimalInfo> get_animals() {

		return Collections.unmodifiableList(animales);
	}

	public double get_time() {

		return time;
	}

	public void advance(double dt) {

		time += dt;
		Iterator<Animal> i = this.animales.iterator();
		List<Animal> add = new ArrayList<Animal>();
		List<Animal> delete = new ArrayList<Animal>();
		while (i.hasNext()) {
			Animal a = i.next();
			if (a.isDead()) {
				delete.add(a);
				this.manager.unregister_animal(a);

			} else {
				a.update(dt);
				this.manager.update_animal_region(a);
				if (a.is_pregnant()) {
					Animal baby = a.deliver_baby();
					add.add(baby);
					this.manager.register_animal(baby);
				}
			}
		}
		this.manager.update_all_regions(dt);
		this.animales.addAll(add);
		this.animales.removeAll(delete);
	}

	public JSONObject as_JSON() {

		JSONObject jo = new JSONObject();

		jo.put("time", time);
		jo.put("state", manager.as_JSON());

		return jo;
	}
}
