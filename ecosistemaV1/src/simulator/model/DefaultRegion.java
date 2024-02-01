package simulator.model;

import simulator.misc.Vector2D;

public class DefaultRegion extends Region{
	
	public DefaultRegion() {
		
	}
	
	double get_food(Animal a, Vector2D dt){
		if(a.get_diet().equals(Diet.CARNIVORE)) return 0.0;
		else return 60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt;
	}
}
