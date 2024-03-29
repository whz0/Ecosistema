package simulator.model;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {

	private final static double SPEED = 0.1;
	private final static double ENERGY = 100.0;
	private final static double MIN_ENERGY = 0.0;
	private final static double MAX_DESIRE = 100.0;
	private final static double BSPEED = 0.2;
	private final static double BSIGHT = 0.2;
	private final static double BPOS = 60.0;
	private final static double REACH_DISTANCE = 0.8;
	private final static double SPEED_FORCE = 0.007;
	private final static double BABY_CHANCE = 0.9;
	private final static double DESIRE_TO_MATE = 65.0;
	private final static double WASTE = 1.2;

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
	
	public static enum State{
		NORMAL, MATE, HUNGER, DANGER, DEAD;

	}
	
	public static enum Diet{
		HERVIBORE, CARNIVORE;
	}

	protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed,
			SelectionStrategy mate_strategy, Vector2D pos) throws IllegalArgumentException {
		if (genetic_code == null || diet == null || sight_range <= 0 || init_speed <= 0 || mate_strategy == null)
			throw new IllegalArgumentException("Invalid genetic_code/diet/sight_rangeinit_speed/mate_strategy");
		this._age = 0.0;
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
		this._mate_strategy = p2._mate_strategy;
	}

	protected void init(AnimalMapView reg_mngr) {

		this._region_mngr = reg_mngr;
		if (this._pos == null) {
			this._pos = new Vector2D(Vector2D.get_random_pos(0, _region_mngr.get_width() - 1),
					Vector2D.get_random_pos(0, _region_mngr.get_height() - 1));
		} else
			this._pos.fixPos(reg_mngr);
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

	@Override
	public void update(double dt) {
		mostrarAtributos();
		if (!isDead()) {
			switch (this._state) {
			case NORMAL:
				normalStateUpdate(dt);
				break;
			case DANGER:
				dangerStateUpdate(dt);
				break;
			case MATE:
				mateStateUpdate(dt);
				break;
			case HUNGER:
				hungerStateUpdate(dt);
			default:
				break;
			}
			if (this._pos.isOutOfMap(this._region_mngr)) {
				this._pos.fixPos(this._region_mngr);
				setStateToNormal();
			}
			if (this._energy == 0.0 || toOld()) {
				setStateToDead();
			}
			if (!isDead()) {
				eat(dt);
			}
		}
	}

	private void mostrarAtributos() {
		System.out.println("codigo genetico: " + this._genetic_code + "--" + this.hashCode());
		System.out.println("energia:" + this._energy);
		System.out.println("edad: " + this._age);
		System.out.println("deseo: " + this._desire);
		System.out.println("estado: " + this._state.toString());
		System.out.println("velocidad: " + this._speed);
		System.out.println("---------------------------");
	}

	protected abstract boolean toOld();

	protected abstract void hungerStateUpdate(double dt);

	protected abstract void mateStateUpdate(double dt);

	protected abstract void dangerStateUpdate(double dt);

	protected abstract void normalStateUpdate(double dt);

	protected void move(double speed) {

		this._pos = this._pos.plus(this._dest.minus(this._pos).direction().scale(speed));
	}

	public JSONObject as_JSON() {

		JSONObject jo1 = new JSONObject();

		jo1.put("pos", this._pos.asJSONArray());
		jo1.put("gcode", this._genetic_code);
		jo1.put("diet", this._diet.toString());
		jo1.put("state", this._state.toString());
		return jo1;
	}

	protected void advance(double desire_gain, double energy_loose, double dt) {

		if (this._pos.distanceTo(this._dest) < REACH_DISTANCE)
			set_RandomDest();
		move(velocity(dt));
		getOlder(dt);
		loseEnergy(energy_loose, dt);
		gainDesire(desire_gain, dt);
	}

	protected void specialAdvance(double desire_gain, double energy_loose, double run_buff, double dt) {

		move(velocity(run_buff * dt));
		getOlder(dt);
		loseEnergy(energy_loose, WASTE * dt);
		gainDesire(desire_gain, dt);
	}

	protected double velocity(double dt) {

		return this._speed * dt * Math.exp((this._energy - ENERGY) * SPEED_FORCE);
	}

	protected void getOlder(double dt) {

		this._age += dt;
	}

	protected boolean isDead() {

		return _state.equals(State.DEAD);
	}

	protected void setStateToNormal() {

		this._state = State.NORMAL;
		this._mate_target = null;
	}

	protected void setStateToDead() {

		this._state = State.DEAD;
	}

	protected boolean isOutOfRange(Animal a) {

		return this._pos.distanceTo(a.get_position()) > this._sight_range;
	}

	protected void lookForMate() {

		List<Animal> animal = new ArrayList<Animal>();
		animal = this._region_mngr.get_animals_in_range(this,
				(a) -> this.get_genetic_code().equals(a.get_genetic_code()));
		this._mate_target = this._mate_strategy.select(this, animal);
	}

	protected boolean hasDesireToMate() {

		return this._desire > DESIRE_TO_MATE;
	}

	protected boolean isInLove() {

		return this._mate_target != null;
	}

	protected void eat(double dt) {

		this._energy += this._region_mngr.get_food(this, dt);
		if (this._energy > ENERGY)
			this._energy = ENERGY;
	}

	protected void gainDesire(double desire_gain, double dt) {

		this._desire += desire_gain * dt;
		if (this._desire > MAX_DESIRE)
			this._desire = MAX_DESIRE;
	}

	protected void loseEnergy(double energy_loose, double dt) {

		this._energy -= energy_loose * dt;
		if (this._energy < MIN_ENERGY)
			this._energy = MIN_ENERGY;
	}

	protected void towardsToMate() {

		this._dest = this._mate_target.get_position();
	}

	protected void resetDesire() {

		this._desire = 0.0;
	}

	protected boolean canHaveBaby() {

		return Utils._rand.nextDouble() < BABY_CHANCE;
	}

	@Override
	public State get_state() {

		return this._state;
	}

	@Override
	public Vector2D get_position() {

		return this._pos;
	}

	@Override
	public String get_genetic_code() {

		return this._genetic_code;
	}

	@Override
	public Diet get_diet() {

		return this._diet;
	}

	@Override
	public double get_speed() {

		return this._speed;
	}

	@Override
	public double get_sight_range() {

		return this._sight_range;
	}

	@Override
	public double get_energy() {

		return this._energy;
	}

	@Override
	public double get_age() {

		return this._age;
	}

	@Override
	public Vector2D get_destination() {

		return this._dest;
	}

	@Override
	public boolean is_pregnant() {

		return this._baby != null;
	}

	public boolean equals(Animal a) {
		return this != a;
	}

}
