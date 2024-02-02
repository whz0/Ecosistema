package simulator.model;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {

	private final static double SPEED = 0.1;
	private final static double ENERGY = 100.0;
	private final static double BSPEED = 0.2;
	private final static double BSIGHT = 0.2;
	private final static double BPOS = 60.0;
	private final static double SPEED_FORCE = 0.007;
	private final static double BABY_CHANCE = 0.9;

	protected String _genetic_code;
	protected Diet _diet;
	protected State _state;
	protected Vector2D _pos;
	protected Vector2D _dest;
	protected double _energy;
	protected double _speed;
	protected double _age;
	protected double _desire;
	protected double _sight_range;
	protected Animal _mate_target;
	protected Animal _baby;
	protected AnimalMapView _region_mngr;
	protected SelectionStrategy _mate_strategy;

	protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed,
			SelectionStrategy mate_strategy, Vector2D pos) {

		this._genetic_code = genetic_code;
		this._diet = diet;
		this._sight_range = sight_range;
		this._pos = pos;
		this._mate_strategy = mate_strategy;
		this._speed = Utils.get_randomized_parameter(init_speed, SPEED);
		this._state = State.NORMAL;
		this._energy = ENERGY;
		this._desire = 0.0;
		this._dest = null;
		this._mate_target = null;
		this._baby = null;
		this._region_mngr = null;
	}

	protected Animal(Animal p1, Animal p2) {
		this._dest = null;
		this._mate_target = null;
		this._baby = null;
		this._region_mngr = null;
		this._state = State.NORMAL;
		this._desire = 0.0;
		this._genetic_code = p1.get_genetic_code();
		this._diet = p1.get_diet();
		this._energy = (p1.get_energy() + p2.get_energy()) / 2;
		this._pos = p1.get_position()
				.plus(Vector2D.get_random_vector(-1, 1).scale(BPOS * (Utils._rand.nextGaussian() + 1)));
		this._sight_range = Utils.get_randomized_parameter((p1.get_sight_range() + p2.get_sight_range()) / 2, BSIGHT);
		this._speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed()) / 2, BSPEED);
	}

	protected void init(AnimalMapView reg_mngr) {

		this._region_mngr = reg_mngr;
		if (this._pos == null) {
			this._pos = new Vector2D(Vector2D.get_random_pos(0, _region_mngr.get_width() - 1),
					Vector2D.get_random_pos(0, _region_mngr.get_height() - 1));
		} else
			this._pos.fixPos();
		set_RandomDest();
	}

	protected void set_RandomDest() {
		this._dest = new Vector2D(Vector2D.get_random_pos(0, _region_mngr.get_width() - 1),
				Vector2D.get_random_pos(0, _region_mngr.get_height() - 1));
	}

	protected Animal deliver_baby() {

		Animal baby = this._baby;
		this._baby = null;
		return baby;
	}

	protected void move(double speed) {

		this._pos = this._pos.plus(this._dest.minus(this._pos).direction().scale(speed));
	}

	public JSONObject as_JSON() {

		JSONObject jo1 = new JSONObject();

		jo1.put("pos:", this._pos.asJSONArray());
		jo1.put("gcode", this._genetic_code);
		jo1.put("diet", this._diet.toString());
		jo1.put("state", this._state.toString());
		return jo1;
	}
	
	protected boolean isDead() {
		
		return _state.equals(State.DEAD);
	}
	
	protected void setStateToNormal() {
		
		this._state = State.NORMAL;
		this._mate_target = null;
	}
	
	protected double velocity(double dt) {
		
		return this._speed*dt*Math.exp((this._energy-ENERGY)*SPEED_FORCE);
	}

	protected boolean isOutOfRange(Animal a) {
		
		return this._pos.distanceTo(a.get_position()) > this._sight_range;
	}
	
	protected void resetDesire() {
		
		this._desire = 0.0;
	}

	protected boolean canHaveBaby() {
		
		return Utils.get_randomized_parameter(1,1) < BABY_CHANCE;
	}
}
