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

	public RegionManager(int cols, int rows, int width, int height) throws IllegalArgumentException {
		if (cols <= 0 || rows <= 0 || width <= 0 || height <= 0)
			throw new IllegalArgumentException("Regions size can`t zero or negative");
		if (width % cols != 0 || height % rows != 0)
			throw new IllegalArgumentException("Region sizes should be divisible");
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		this._region_width = width / cols;
		this._region_height = height / rows;
		this._region = new Region[this._rows][this._cols];
		for (int i = 0; i < this._rows; i++)
			for (int j = 0; j < this._cols; j++)
				_region[i][j] = new DefaultRegion();
		this._animal_region = new HashMap<Animal, Region>();

	}

	void set_region(int row, int col, Region r) {

		this._region[row][col] = r;
	}

	void register_animal(Animal a) {

		a.init(this);
		Region r = searchRegion(a.get_position());
		this._animal_region.put(a, r);
		r.add_animal(a);
	}

	private Region searchRegion(Vector2D pos) {

		int col = (int) (pos.getX() / this._region_width);
		int row = (int) (pos.getY() / this._region_height);
		return this._region[row][col];
	}

	void unregister_animal(Animal a) {

		Region r = this._animal_region.get(a);
		this._animal_region.remove(a);
		r.remove_animal(a);
	}

	void update_animal_region(Animal a) {

		Region r = searchRegion(a.get_position());
		if (r != this._animal_region.get(a)) {
			unregister_animal(a);
			this._animal_region.put(a, r);
			r.add_animal(a);
		}
	}

	public double get_food(Animal a, double dt) {

		return _animal_region.get(a).get_food(a, dt);
	}

	void update_all_regions(double dt) {

		for (Region[] re : this._region)
			for (Region r : re)
				r.update(dt);
	}

	public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {

		List<Animal> animalList = new ArrayList<Animal>();

		Vector2D range1 = a.get_position().plus(new Vector2D(-a.get_sight_range(), a.get_sight_range()));
		Vector2D range2 = a.get_position().plus(new Vector2D(a.get_sight_range(), -a.get_sight_range()));

		int col1 = (int) Math.floor(range1.getX() / this._region_width);
		if (col1 >= _cols)
			col1 = _cols - 1;
		else if (col1 < 0)
			col1 = 0;
		int row1 = (int) Math.floor(range1.getY() / this._region_height);
		if (row1 >= _rows)
			row1 = _rows - 1;
		else if (row1 < 0)
			row1 = 0;

		int col2 = (int) Math.floor(range2.getX() / this._region_width);
		if (col2 >= _cols)
			col2 = _cols - 1;
		else if (col2 < 0)
			col2 = 0;
		int row2 = (int) Math.floor(range2.getY() / this._region_height);
		if (row2 >= _rows)
			row2 = _rows - 1;
		else if (row2 < 0)
			row2 = 0;

		for (int i = col1; i <= col2; i++) {
			for (int j = row1; j >= row2; j--) {
				Iterator<Animal> iterator = this._region[j][i].getAnimals().iterator();
				while (iterator.hasNext()) {
					Animal animalIterator = iterator.next();
					if (!a.isOutOfRange(animalIterator) && filter.test(animalIterator) && animalIterator != a) {
						animalList.add(animalIterator);
					}
				}
			}
		}

		return animalList;
	}

	public JSONObject as_JSON() {

		JSONArray ja = new JSONArray();

		for (int i = 0; i < this._rows; i++) {
			for (int j = 0; j < this._cols; j++) {
				JSONObject jo = new JSONObject();
				jo.put("row", this._rows);
				jo.put("col", this._cols);
				jo.put("data", this._region[i][j].as_JSON());
				ja.put(jo);
			}
		}
		JSONObject j = new JSONObject();

		j.put("regions", ja);

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

	@Override
	public Iterator<RegionData> iterator() {

		return new Iterator<RegionData>() {

			int i = 0;
			int j = 0;

			@Override
			public boolean hasNext() {

				return this.i < RegionManager.this._rows - 1 || (this.i==RegionManager.this._rows-1 && this.j < RegionManager.this._cols);
			}

			@Override
			public RegionData next() {

				RegionData region = new RegionData(i, j, RegionManager.this._region[i][j]);
				if (this.j == RegionManager.this._cols - 1) {
					i++;
					j = 0;
				} else
					j++;
				if (region == null)
					return next();
				return region;
			}

			public void remove() {

				RegionManager.this._region[i][j] = null;
			}

		};
	}

}
