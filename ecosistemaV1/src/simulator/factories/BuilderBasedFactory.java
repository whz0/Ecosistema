package simulator.factories;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _builders_info;

	public BuilderBasedFactory() {
		this._builders = new HashMap<String, Builder<T>>();
		this._builders_info = new LinkedList<JSONObject>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {

		this();
		for (Builder<T> b : builders) {
			add_builder(b);
		}
	}

	public void add_builder(Builder<T> b) {

		this._builders.put(b.get_type_tag(), b);
		this._builders_info.add(b.get_info());
	}

	@Override
	public T create_instance(JSONObject info) {
		if (info == null) {
			throw new IllegalArgumentException("’info’ cannot be null");
		}
		if (this._builders.containsKey(info.getString("type"))) {
			Builder<T> builder = this._builders.get(info.getString("type"));
			if (info.has("data") && !info.isNull("data")) {
				return builder.create_instance(info.getJSONObject("data"));
			} else {
				return builder.create_instance(new JSONObject());
			}
		} else
			throw new IllegalArgumentException("Unrecognized ‘info’:" + info.toString());
	}

	@Override
	public List<JSONObject> get_info() {
		return Collections.unmodifiableList(_builders_info);
	}

}
