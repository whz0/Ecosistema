package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "it's a builder for dynamic region");
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) throws IllegalArgumentException {

		Double factor;
		Double food;

		if (!data.isEmpty()) {
			if ((data.has("factor") && data.isNull("factor")) || (data.isNull("food") && data.has("food")))
				throw new IllegalArgumentException("Incorrect argument the values cant be null");
		}
		try {
			food = data.optDouble("food", 100.0);
			factor = data.optDouble("factor", 2.0);
		} catch (Exception e) {
			throw new IllegalArgumentException("Incorrect type of arguments");
		}
		
		return new DynamicSupplyRegion(food, factor);
	}

	@Override
	protected void fill_in_data(JSONObject o) {

		o.put("food", "initial amount of food (optional, default 100.0)");
		o.put("power", "initial amount of food (optional, default 10.0)");
	}
}
