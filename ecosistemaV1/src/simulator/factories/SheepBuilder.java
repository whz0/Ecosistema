package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Sheep;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectionStrategy;
import simulator.model.SelectFirst;

public class SheepBuilder extends Builder<Animal> {

	Factory<SelectionStrategy> factory_builder;

	public SheepBuilder(Factory<SelectionStrategy> _selection) {
		super("sheep", "it's a builder for sheep");
		this.factory_builder = _selection;
	}

	@Override
	protected Sheep create_instance(JSONObject data) {

		SelectionStrategy mate_strategy;
		SelectionStrategy danger_strategy;
		Vector2D pos = null;

		if (data.has("mate_strategy") && !data.isNull("mate_strategy"))
			mate_strategy = factory_builder.create_instance(data.getJSONObject("mate_strategy"));
		else
			mate_strategy = new SelectFirst();
		if (data.has("hunt_strategy") && !data.isNull("hunt_strategy"))
			danger_strategy = factory_builder.create_instance(data.getJSONObject("hunt_strategy"));
		else
			danger_strategy = new SelectFirst();
		if (data.has("pos") && !data.isNull("pos")) {
			JSONArray jaX = data.getJSONObject("pos").getJSONArray("x_range");
			JSONArray jaY = data.getJSONObject("pos").getJSONArray("y_range");
			pos = new Vector2D(Vector2D.get_random_pos(jaX.getDouble(0), jaX.getDouble(1)),
					Vector2D.get_random_pos(jaY.getDouble(0), jaY.getDouble(1)));
		}

		return new Sheep(mate_strategy, danger_strategy, pos);
	}

}
