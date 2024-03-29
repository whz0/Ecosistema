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
 
		Double factor = 2.0;
		Double food = 100.0;
		
	//	factor = data.optDouble("factor",2.0);
		
		if (!data.has("factor")) {
			factor = data.getDouble("factor");
			if (factor.isNaN())
				throw new IllegalArgumentException("Incorrect argument in DynamicSupplyRegion builder factor");
		} 
		if (!data.isNull("food"))
			food = data.optDouble("food");
		if (food.isNaN())
			throw new IllegalArgumentException("Incorrect argument in DynamicSupplyRegion builder food");

		return new DynamicSupplyRegion(food, factor);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		o.put("factor", "food increase factor (optional, default 2.0)");
		o.put("food", "initial amount of food (optional, default 100.0)");
	}
}
