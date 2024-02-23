package simulator.factories;

import org.json.JSONObject;

import simulator.model.Wolf;

public class WolfBuilder extends Builder<Wolf> {

	public WolfBuilder() {
		super("wolf", "it's a builder for wolf");
	}

	@Override
	protected Wolf create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return new Wolf(***);
	}

}
