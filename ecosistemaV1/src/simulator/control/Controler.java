package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Simulator;

public class Controler {

	private Simulator _sim;

	public Controler(Simulator sim) {
		this._sim = sim;
	}

	public void load_data(JSONObject data) {

		if (data.has("regions")) {
			JSONArray regiones = data.getJSONArray("animals");
			int n = regiones.length();
			for (int i = 0; i < n; i++) {
				JSONObject jo = regiones.getJSONObject(i);
				JSONArray row = data.getJSONArray("row");
				int rowIni = row.getInt(0);
				int rowEnd = row.getInt(1);
				JSONArray col = data.getJSONArray("col");
				int colIni = col.getInt(0);
				int colEnd = col.getInt(1);
				JSONObject region = jo.getJSONObject("spec");
				for (int j = rowIni; j < rowEnd; j++) {
					for (int k = colIni; k < colEnd; k++) {
						this._sim.set_region(j, k, region);
					}
				}
			}
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

	public void run(double t, double dt, boolean sv, OutputStream out) {

		JSONObject jo = new JSONObject();
		jo.put("in", this._sim.as_JSON());
		while (this._sim.get_time() < t) {
			this._sim.advance(dt);
			if(sv)this._sim.notify();
		}
		jo.put("out", this._sim.as_JSON());
		PrintStream p = new PrintStream(out);
		p.println(jo);

	}
}
