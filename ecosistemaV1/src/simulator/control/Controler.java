package simulator.control;

import java.io.OutputStream;

import org.json.JSONObject;

import simulator.model.Simulator;

public class Controler {
	
	private Simulator _sim;
	
	public Controler(Simulator sim) {
		this._sim = sim;
	}
	
	public void load_data(JSONObject data) {
		
	}
	
	 public void run(double t, double dt, boolean sv, OutputStream out) {
		 
	 }
}
