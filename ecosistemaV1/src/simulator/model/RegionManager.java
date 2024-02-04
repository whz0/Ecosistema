package simulator.model;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONObject;

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
		this._animal_region.put(null, null);
	}

	void set_region(int row, int col, Region r) {

		this._region[col][row] = r;
	}

	void register_animal(Animal a) {

		
		this._animal_region.put(a, searchRegion(a));
	}

	private Region searchRegion(Animal a) {
		
		return null;
	}

	void unregister_animal(Animal a) {

	}

	void update_animal_region(Animal a) {

	}

	public double get_food(Animal a, double dt) {

		return 0;
	}

	void update_all_regions(double dt) {

	}

	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
		return null;

	}

	public JSONObject as_JSON() {
		return null;
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
