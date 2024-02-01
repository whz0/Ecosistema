package simulator.model;

import simulator.misc.Vector2D;

public class Wolf extends Animal {

	private final static double INIT_SIGHT = 50.0;
	private final static double INIT_SPEED = 60.0;

	private Animal _hunter_target;
	private SelectionStrategy _hunting_strategy;

	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) {
		super("Wolf", Diet.CARNIVORE, INIT_SIGHT, INIT_SPEED, mate_strategy, pos);
		this._hunting_strategy = hunting_strategy;
	}
	
	protected Wolf(Wolf p1, Animal p2){
		super(p1,p2);
		this._hunting_strategy = p1._hunting_strategy;
		this._hunter_target = null;
	}
}
