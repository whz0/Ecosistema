package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {

	private Simulator _sim;

	public Controller(Simulator sim) {
		this._sim = sim;
	}

	public void load_data(JSONObject data) {

		if (data.has("regions")) {
			set_regions(data.getJSONObject("regions"));
		}

		JSONArray animales = data.getJSONArray("animals");
		int n = animales.length();
		for (int i = 0; i < n; i++) {
			JSONObject jo = animales.getJSONObject(i);
			int N = jo.getInt("amount");
			JSONObject animal = jo.getJSONObject("spec");
			for (int j = 0; j < N; j++) {
				this._sim.add_animal(animal);
			}
		}

	}

	private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.get_genetic_code(), (int) a.get_position().getX(), (int) a.get_position().getY(),
					(int) Math.round(a.get_age())));
		return ol;
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {

		SimpleObjectViewer view = null;
		if (sv) {
			MapInfo m = _sim.get_map_info();
			view = new SimpleObjectViewer("[ECOSYSTEM]", m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
			view.update(to_animals_info(_sim.get_animals()), _sim.get_time(), dt);
		}

		JSONObject jo = new JSONObject();
		jo.put("in", this._sim.as_JSON());
		while (this._sim.get_time() < t) {
			this._sim.advance(dt);
			if (sv)
				view.update(to_animals_info(_sim.get_animals()), _sim.get_time(), dt);
		}
		jo.put("out", this._sim.as_JSON());
		PrintStream p = new PrintStream(out);
		p.println(jo.toString());
		if (sv)
			view.close();

	}

	public void reset(int cols, int rows, int width, int height) {
		this._sim.reset(cols, rows, width, height);
	}

	public void set_regions(JSONObject rs) {
		JSONArray regiones = rs.getJSONArray("regions");
		int n = regiones.length();
		for (int i = 0; i < n; i++) {
			JSONObject jo = regiones.getJSONObject(i);
			JSONArray row = jo.getJSONArray("row");
			int rowIni = row.getInt(0);
			int rowEnd = row.getInt(1);
			JSONArray col = jo.getJSONArray("col");
			int colIni = col.getInt(0);
			int colEnd = col.getInt(1);
			JSONObject region = jo.getJSONObject("spec");
			for (int j = rowIni; j <= rowEnd; j++) {
				for (int k = colIni; k <= colEnd; k++) {
					this._sim.set_region(j, k, region);
				}
			}
		}
	}

	public void advance(double dt) {
		this._sim.advance(dt);
	}

	public void addObserver(EcoSysObserver o) {
		this._sim.addObserver(o);
	}

	public void removeObserver(EcoSysObserver o) {
		this._sim.removeObserver(o);
	}
}
