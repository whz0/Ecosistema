package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Sheep;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectionStrategy;
import simulator.model.SelectFirst;

public class SheepBuilder extends Builder<Animal> {

	private Factory<SelectionStrategy> factory_builder;

	public SheepBuilder(Factory<SelectionStrategy> _selection) {
		super("sheep", "it's a builder for sheep");
		if (_selection == null)
			throw new IllegalArgumentException("Invalid factory_builder");
		this.factory_builder = _selection;
	}

	@Override 
	protected Sheep create_instance(JSONObject data) throws IllegalArgumentException {

		SelectionStrategy mate_strategy;
		SelectionStrategy danger_strategy;
		Vector2D pos = null;
		if (data.has("mate_strategy") && !data.isNull("mate_strategy")) {
			try {
				mate_strategy = factory_builder.create_instance(data.getJSONObject("mate_strategy"));
			} catch (Exception e) {
				throw new IllegalArgumentException("Incorrect argument in SheepBuilder mate_strategy");
			}
		} else
			mate_strategy = new SelectFirst();
		
		if (data.has("danger_strategy") && !data.isNull("danger_strategy")) {
			try {
				danger_strategy = factory_builder.create_instance(data.getJSONObject("danger_strategy"));
			} catch (Exception e) {
				throw new IllegalArgumentException("Incorrect argument in SheepBuilder danger_strategy");
			}
		} else
			danger_strategy = new SelectFirst();
		
		if (data.has("pos") && !data.isNull("pos")) {
			JSONArray jaX = data.getJSONObject("pos").getJSONArray("x_range");
			JSONArray jaY = data.getJSONObject("pos").getJSONArray("y_range");
			Double jaX1 = jaX.optDouble(0);
			Double jaX2 = jaX.optDouble(1);
			Double jaY1 = jaY.optDouble(0);
			Double jaY2 = jaY.optDouble(1);
			if (jaX1.isNaN() || jaX2.isNaN() || jaY1.isNaN() || jaX2.isNaN())
				throw new IllegalArgumentException("Incorrect argument in SheepBuilder pos");
			pos = new Vector2D(Vector2D.get_random_pos(jaX1, jaX2), Vector2D.get_random_pos(jaY1, jaY2));
		}
		return new Sheep(mate_strategy, danger_strategy, pos);
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
		o.put("danger_strategy", strategy);
		o.put("pos", pos);

	}

}
