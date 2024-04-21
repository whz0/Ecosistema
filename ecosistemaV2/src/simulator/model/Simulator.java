package simulator.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable, Observable<EcoSysObserver> {

	private Factory<Animal> _animal_factory;
	private Factory<Region> _regions_factory;
	private RegionManager _region_mngr;
	private List<Animal> _animals;
	private List<EcoSysObserver> _observers;
	private double _time;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory,
			Factory<Region> regions_factory) {
		if (animals_factory == null || regions_factory == null)
			throw new IllegalArgumentException("Invalid animals_factory/regions_factory");
		this._animal_factory = animals_factory;
		this._regions_factory = regions_factory;
		this._observers = new ArrayList<EcoSysObserver>();
		reset(cols, rows, width, height);
	}

	public void addObserver(EcoSysObserver o) {
		_observers.add(o);
		notify_on_add_observer(o);
	}

	public void removeObserver(EcoSysObserver o) {
		_observers.remove(o);
	}

	private void set_region(int row, int col, Region r) {
		
		this._region_mngr.set_region(row, col, r);
		notify_on_set_region(row, col, r);
	}

	public void set_region(int row, int col, JSONObject r_json) {

		set_region(row, col, this._regions_factory.create_instance(r_json));
	}

	private void add_animal(Animal a) {

		this._animals.add(a);
		this._region_mngr.register_animal(a);
		notify_on_add_animal(a);
	}

	public void add_animal(JSONObject a_json) {

		add_animal(this._animal_factory.create_instance(a_json));
	}

	public MapInfo get_map_info() {

		return _region_mngr;
	}

	public List<? extends AnimalInfo> get_animals() {

		return Collections.unmodifiableList(_animals);
	}

	public double get_time() {

		return _time;
	}

	public void advance(double dt) {

		_time += dt;
		Iterator<Animal> i = this._animals.iterator();
		List<Animal> add = new ArrayList<Animal>();
		List<Animal> delete = new ArrayList<Animal>();
		while (i.hasNext()) {
			Animal a = i.next();
			if (a.isDead()) {
				delete.add(a);
				this._region_mngr.unregister_animal(a);

			}
		}
		this._animals.removeAll(delete);

		i = this._animals.iterator();
		while (i.hasNext()) {
			Animal a = i.next();
			a.update(dt);
			this._region_mngr.update_animal_region(a);
		}

		i = this._animals.iterator();
		while (i.hasNext()) {
			Animal a = i.next();
			if (a.is_pregnant()) {
				Animal baby = a.deliver_baby();
				add.add(baby);
				this._region_mngr.register_animal(baby);
			}
		}

		this._animals.addAll(add);
		this._region_mngr.update_all_regions(dt);
		notify_on_advanced(dt);
	}

	public JSONObject as_JSON() {

		JSONObject jo = new JSONObject();

		jo.put("time", _time);
		jo.put("state", _region_mngr.as_JSON());

		return jo;
	}

	public void reset(int cols, int rows, int width, int height) {
		this._region_mngr = new RegionManager(cols, rows, width, height);
		this._animals = new ArrayList<Animal>();
		this._time = 0.0;
		notify_on_reset();
	}

	private void notify_on_reset() {
		List<AnimalInfo> animals = new ArrayList<>(_animals);
		this._observers.forEach((o) -> o.onReset(_time, _region_mngr, animals));
	}

	private void notify_on_add_animal(Animal animal) {
		List<AnimalInfo> animals = new ArrayList<>(_animals);
		this._observers.forEach((o) -> o.onAnimalAdded(_time, _region_mngr, animals, animal));
	}

	private void notify_on_set_region(int row, int col, Region region) {
		this._observers.forEach((o) -> o.onRegionSet(row, col, _region_mngr, region));
	}

	private void notify_on_add_observer(EcoSysObserver o) {
		List<AnimalInfo> animals = new ArrayList<>(_animals);
		o.onRegister(_time, _region_mngr, animals);
	} 

	private void notify_on_advanced(double dt) {
		List<AnimalInfo> animals = new ArrayList<>(_animals);
		this._observers.forEach((o) -> o.onAvanced(_time, _region_mngr, animals, dt));
	}

}
