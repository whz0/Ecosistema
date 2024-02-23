package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;

public class DefaultRegionBuilder extends Builder<DefaultRegion> {

	public DefaultRegionBuilder(String type_tag, String desc) {
		super(type_tag, desc);
		
	}

	@Override
	protected DefaultRegion create_instance(JSONObject data) {
		
		return null;
	}

}
