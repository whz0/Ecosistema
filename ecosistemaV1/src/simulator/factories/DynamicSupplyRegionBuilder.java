package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "it's a builder for dynamic region");
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {

		Double factor = 2.0;
		Double food = 1000.0;
		if (!data.isNull("factor"))
			data.getDouble("factor");
		if (!data.isNull("food"))
			food = data.getDouble("food");

		return new DynamicSupplyRegion(factor, food);
	}

}
