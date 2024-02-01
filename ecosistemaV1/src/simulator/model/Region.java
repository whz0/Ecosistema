package simulator.model;

import java.util.List;

import org.json.JSONObject;

public class Region implements Entity, RegionInfo, FoodSupplier{

	protected List<Animal> animales;
	
	public Region() {
		
	}
	
	@Override
	public double get_food(Animal a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
	}
	
	final void add_animal(Animal a) {
		
	}
	
	final void remove_animal(Animal a) {
		
	}
	
	final List<Animal> getAnimals(){
		
	}
	
	public JSONObject as_JSON()
	{
	"animals":[]
	}


}
