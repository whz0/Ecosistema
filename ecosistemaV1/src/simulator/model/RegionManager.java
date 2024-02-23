package simulator.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class RegionManager implements AnimalMapView {

	private int _width;
	private int _height;
	private int _cols;
	private int _rows;
	private int _region_width;
	private int _region_height;
	private Region[][] _region;
	private Map<Animal, Region> _animal_region;

	public RegionManager(int cols, int rows, int width, int height) {
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		this._region_width = width / cols;
		this._region_height = height / rows;
		this._region = new DefaultRegion[this._cols][this._rows];
		this._animal_region = new HashMap<Animal, Region>();
	}

	void set_region(int row, int col, Region r) {

		this._region[col][row] = r;
	}

	void register_animal(Animal a) {

		Region r = searchRegion(a.get_position());
		this._animal_region.put(a, r);
		r.add_animal(a);
	}

	private Region searchRegion(Vector2D pos) {

		double col = pos.getX() / this._region_width;
		double row = pos.getY() / this._region_height;
		return this._region[(int) Math.round(col)][(int) Math.round(row)];
	}
	
	void unregister_animal(Animal a) {

		Region r = this._animal_region.get(a);
		this._animal_region.remove(a);
		r.remove_animal(a);
	}

	void update_animal_region(Animal a) {

		Region r1 = searchRegion(a.get_position());
		if (r1 != this._animal_region.get(a)) {
			unregister_animal(a);
			register_animal(a);
		}
	}

	public double get_food(Animal a, double dt) {

		return 0;
	}

	void update_all_regions(double dt) {

		for (Region[] re : this._region)
			for (Region r : re)
				r.update(dt);
	}

	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
		
		List<Animal> animalList = new ArrayList<Animal>();
		
		Vector2D range1 = a.get_position().max_range(a.get_sight_range());
		Vector2D range2 = range1.rotate(180);
		double col1 = range1.getX() / this._region_width;
		double row1 = range1.getY() / this._region_height;
		
		double col2 = range2.getX() / this._region_width;
		double row2 = range2.getY() / this._region_height;
		
		for(double i=col1; i<= col2;i++) {
			for(double j=row1; j<= row2;i++) {
		Iterator<Animal> iterator = _region[(int) Math.round(col1)][(int) Math.round(row1)].getAnimals().iterator();
		while(iterator.hasNext()) {
			Animal animalIterator = iterator.next();
			if(!a.isOutOfRange(animalIterator) && filter.test(animalIterator)) {
				animalList.add(animalIterator);
			}
		}
		}
		}
		
		return animalList;
	}

	public JSONObject as_JSON() {

		JSONArray ja = new JSONArray();

		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				JSONObject jo = new JSONObject();
				jo.put("row:", this._rows);
				jo.put("cols:", this._cols);
				jo.put("data:", this._region[i][j].as_JSON());
				ja.put(jo);
			}
		}
		JSONObject j = new JSONObject();

		j.put("regiones:", ja);

		return j;
	}

	@Override
	public int get_cols() {

		return this._cols;
	}

	@Override
	public int get_rows() {

		return this._rows;
	}

	@Override
	public int get_width() {

		return this._width;
	}

	@Override
	public int get_height() {

		return this._height;
	}

	@Override
	public int get_region_width() {

		return this._region_width;
	}

	@Override
	public int get_region_height() {

		return this._region_height;
	}

}
