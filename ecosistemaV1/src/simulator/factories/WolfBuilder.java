package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;

public class WolfBuilder extends Builder<Animal> {

	BuilderBasedFactory<SelectionStrategy> factory_builder;

	public WolfBuilder(BuilderBasedFactory<SelectionStrategy> factory_builder) {
		super("wolf", "it's a builder for wolf");
		this.factory_builder = factory_builder;
	}

	@Override
	protected Wolf create_instance(JSONObject data) {
		SelectionStrategy mate_strategy;
		SelectionStrategy hunt_strategy;
		Vector2D pos = null;
		if (data.has("mate_strategy") && !data.isNull("mate_strategy"))
			mate_strategy = factory_builder.create_instance(data.getJSONObject("mate_strategy"));
		else
			mate_strategy = factory_builder.create_instance(new JSONObject("select_first"));
		if (data.has("hunt_strategy") && !data.isNull("hunt_strategy"))
			hunt_strategy = factory_builder.create_instance(data.getJSONObject("hunt_strategy"));
		else
			hunt_strategy = factory_builder.create_instance(new JSONObject("select_first"));
		if (data.has("pos") && !data.isNull("pos")) {
			JSONArray jaX = data.getJSONObject("pos").getJSONArray("x_range");
			JSONArray jaY = data.getJSONObject("pos").getJSONArray("y_range");
			pos = new Vector2D(Vector2D.get_random_pos(jaX.getDouble(0), jaX.getDouble(1)),
					Vector2D.get_random_pos(jaY.getDouble(0), jaY.getDouble(1)));
		}

		return new Wolf(mate_strategy, hunt_strategy, pos);
	}

}
