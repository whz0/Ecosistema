package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {
	
	private final static double init_speed = 0.1;
	
	
	private String _genetic_code;
	private Diet _diet;
	private State _state;
	private Vector2D _pos;
	private Vector2D _dest;
	private double _energy;
	private double _speed;
	private double _age;
	private double _desire;
	private double _sight_range;
	private Animal _mate_target;
	private Animal _baby;
	private AnimalMapView _region_mngr;
	private SelectionStrategy _mate_strategy;
	
	protected Animal(String genetic_code, Diet diet, double sight_range,
			double init_speed, SelectionStrategy mate_strategy, Vector2D pos) {
		
		this._genetic_code = genetic_code;
		this._diet = diet;
		Utils.get_randomized_parameter(this._speed, init_speed);
		
	}
	
	protected Animal(Animal p1, Animal p2) {
		
	}
}
