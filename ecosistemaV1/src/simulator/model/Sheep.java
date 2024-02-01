package simulator.model;

import simulator.misc.Vector2D;

public class Sheep extends Animal {

	private final static double INIT_SIGHT = 40.0;
	private final static double INIT_SPEED = 35.0;
	
	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super("Sheep", Diet.HERVIBORE , INIT_SIGHT, INIT_SPEED, mate_strategy, pos);
		this._danger_strategy = danger_strategy;
		
	}
	
	protected Sheep(Sheep p1, Animal p2) {
		super(p1,p2);
		this._danger_source = null;
		this._danger_strategy = p1._danger_strategy;
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public State get_state() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2D get_position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get_genetic_code() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Diet get_diet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double get_speed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double get_sight_range() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double get_energy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double get_age() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2D get_destination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean is_pregnant() {
		// TODO Auto-generated method stub
		return false;
	}

}
