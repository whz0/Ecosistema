package simulator.factories;

import org.json.JSONObject;

import simulator.model.Sheep;
import simulator.model.SelectionStrategy;

public class SheepBuilder extends Builder<Sheep> {

	public SheepBuilder(String type_tag, String desc) {
		super("sheep", desc);
	}

	@Override
	protected Sheep create_instance(JSONObject data) {
		
		SelectionStrategy mate_strategy;
		
		if(data.getBoolean("mate_strategy").equals(null)) {
			
		}
		return new Sheep(data.get("mate_strategy").get,data.get("danger_strategy"),data.get("pos"));
	}
	
	
}
