package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;

public class DynamicSupplyRegionBuilder extends Builder<DynamicSupplyRegion> {

	public DynamicSupplyRegionBuilder(String type_tag, String desc) {
		super(type_tag, desc);
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {
		
		return null;
	}

}
