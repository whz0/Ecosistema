package simulator.factories;

public interface Factory<T> {
	public T create_instance(JSONObject info);

	public List<JSONObject> get_info();
}
