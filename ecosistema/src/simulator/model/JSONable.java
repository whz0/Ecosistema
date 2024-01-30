package simulator.model;

public interface JSONable {
	default public JSONObject as_JSON() {
		return new JSONObject();
	}
}
