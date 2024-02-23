package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectYoungest;

public class SelectYoungestBuilder extends Builder<SelectYoungest> {

	public SelectYoungestBuilder() {
		super("youngest", "it's a builder for select youngest");
		
	}

	@Override
	protected SelectYoungest create_instance(JSONObject data) {

		return new SelectYoungest();
	}

}
