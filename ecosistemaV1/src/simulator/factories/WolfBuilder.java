package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;

public class WolfBuilder extends Builder<Animal> {

	private Factory<SelectionStrategy> factory_builder;

	public WolfBuilder(Factory<SelectionStrategy> _selection) {
		super("wolf", "it's a builder for wolf");
		this.factory_builder = _selection;
	}

	@Override
	protected Wolf create_instance(JSONObject data) throws IllegalArgumentException {
		SelectionStrategy mate_strategy;
		SelectionStrategy hunt_strategy;
		Vector2D pos = null;

		if (data.has("mate_strategy") && !data.isNull("mate_strategy")) {
			try {
				mate_strategy = factory_builder.create_instance(data.getJSONObject("mate_strategy"));
			} catch (Exception e) {
				throw new IllegalArgumentException("Incorrect argument in WolfBuilder mate_strategy");
			}
		} else
			mate_strategy = new SelectFirst();

		if (data.has("hunt_strategy") && !data.isNull("hunt_strategy")) {

			try {
				hunt_strategy = factory_builder.create_instance(data.getJSONObject("hunt_strategy"));
			} catch (Exception e) {
				throw new IllegalArgumentException("Incorrect argument in WolfBuilder hunt_strategy");
			}
		} else
			hunt_strategy = new SelectFirst();
		if (data.has("pos") && !data.isNull("pos")) {
			JSONArray jaX = data.getJSONObject("pos").getJSONArray("x_range");
			JSONArray jaY = data.getJSONObject("pos").getJSONArray("y_range");
			Double jaX1 = jaX.optDouble(0);
			Double jaX2 = jaX.optDouble(1);
			Double jaY1 = jaY.optDouble(0);
			Double jaY2 = jaY.optDouble(1);
			if (jaX1.isNaN() || jaX2.isNaN() || jaY1.isNaN() || jaX2.isNaN())
				throw new IllegalArgumentException("Incorrect argument in WolfBuilder pos");
			pos = new Vector2D(Vector2D.get_random_pos(jaX1, jaX2), Vector2D.get_random_pos(jaY1, jaY2));
		}
		return new Wolf(mate_strategy, hunt_strategy, pos);
	}

	protected void fill_in_data(JSONObject o) {
		JSONObject posy = new JSONObject();
		JSONArray range = new JSONArray();
		range.put(100.0);
		range.put(200.0);
		posy.put("y_range", range);
		JSONObject posx = new JSONObject();
		posx.put("x_range", range);
		JSONArray pos = new JSONArray();
		pos.put(posy);
		pos.put(posx);
		JSONObject strategy = new JSONObject();
		o.put("mate_strategy", strategy);
		o.put("hunt_strategy", strategy);
		o.put("pos", pos);
	}

}
