package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;

public class SelectClosestBuilder extends Builder<SelectClosest>{

	public SelectClosestBuilder() {
		super("closest", "it's a builder for select closest");
	}

	@Override
	protected SelectClosest create_instance(JSONObject data) {

		return new SelectClosest();
	}

}
