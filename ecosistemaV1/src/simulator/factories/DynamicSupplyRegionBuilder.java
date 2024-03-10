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
		Double food = 1000.0;
		if (!data.isNull("factor")) {
			factor = data.optDouble("factor");
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
		o.put("factor", 100);
		o.put("food", 1000);
	}

}
