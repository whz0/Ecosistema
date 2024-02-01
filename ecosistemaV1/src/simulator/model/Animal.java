package simulator.model;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {

	private final static double SPEED = 0.1;
	private final static double ENERGY = 100;
	private final static double BSPEED = 0.2;
	private final static double BSIGHT = 0.2;
	private final static double BPOS = 60.0;

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

		} else
			this._pos.ajustar();

	}

	protected Animal deliver_baby() {

		// TODO
		this._baby = null;
	}

	protected void move(double speed) {

		this._pos = this._pos.plus(this._dest.minus(this._pos).direction().scale(speed));
	}

	public JSONObject as_JSON(){
	{
	"pos": [28.90696391797469,22.009772194487613],
	"gcode": "Sheep",
	"diet": "HERBIVORE",
	"state": "NORMAL";
	}
	}
}
