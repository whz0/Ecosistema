package simulator.model;

import simulator.misc.Vector2D;

public class Sheep extends Animal {

	private final static double INIT_SIGHT = 40.0;
	private final static double INIT_SPEED = 35.0;
	private final static double REACH_DISTANCE = 8.0;
	private final static double MAX_ENERGY = 100.0;
	private final static double MAX_DESIRE = 100.0;
	private final static double MIN_ENERGY = 0.0;
	private final static double MAX_AGE = 8.0;
	private final static double DESIRE = 40.0;
	private final static double ENERGY = 20.0;
	private final static double HORNY = 65.0;
	private final static double RUN_BUFF = 2.0;
	private final static double WASTE = 1.2;

	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super("Sheep", Diet.HERVIBORE, INIT_SIGHT, INIT_SPEED, mate_strategy, pos);
		this._danger_strategy = danger_strategy;
		this._danger_source = null;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this._danger_source = null;
		this._danger_strategy = p1._danger_strategy;
	}

	@Override
	public void update(double dt) {

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
			default:
				break;
			}
			if (this._pos.isOutOfMap()) {
				this._pos.fixPos();
				setStateToNormal();
				if (this._energy != 0.0 || this._age <= MAX_AGE) {
					if (!isDead())
						eat(dt);
				} else
					this._state = State.DEAD;
			}
		}
	}

	private void mateStateUpdate(double dt) {

		if (isInLove() && (this._mate_target.isDead() || isOutOfRange(this._mate_target)))
			this._mate_target = null;
		else {
			lookForMate();
			if (!isInLove())
				advance(dt);
			else {
				towardsToMate();
				specialAdvance(dt);
				if (this._pos.distanceTo(this._mate_target.get_position()) < REACH_DISTANCE) {
					resetDesire();
					this._mate_target.resetDesire();
					if (canHaveBaby())
						this._baby = new Sheep(this, this._mate_target);
					this._mate_target = null;
				}
			}
		}
		if (!isInDanger())
			lookForDanger();
		else {
			setStateToDanger();
			if (!isInDanger() && !isHorny())
				setStateToNormal();
		}
	}

	private void dangerStateUpdate(double dt) {

		if (isInDanger() && this._danger_source.isDead())
			this._danger_source = null;
		if (!isInDanger())
			advance(dt);
		else {
			runFromDanger();
			specialAdvance(dt);
		}
		if (isInDanger() || this._danger_source.isOutOfRange(this))
			lookForDanger();
		else {
			if (!isHorny())
				setStateToNormal();
			else
				setStateToMate();
		}

	}

	private void normalStateUpdate(double dt) {

		advance(dt);
		if (!isInDanger())
			lookForDanger();
		else {
			setStateToDanger();
			if (isHorny())
				setStateToMate();
		}

	}

	private void towardsToMate() {

		this._dest = this._mate_target.get_position();
	}

	private void runFromDanger() {

		this._dest = this._pos.plus(this._pos.minus(this._danger_source.get_position()).direction());
	}

	private void 
	
	private void specialAdvance(double dt) {

		move(velocity(RUN_BUFF * dt));
		getOlder(dt);
		loseEnergy(WASTE * dt);
		gainDesire(dt);
	}

	private void advance(double dt) {

		if (this._pos.distanceTo(this._dest) < REACH_DISTANCE)
			set_RandomDest();
		move(velocity(dt));
		getOlder(dt);
		loseEnergy(dt);
		gainDesire(dt);
	}

	private void getOlder(double dt) {

		this._age += dt;
	}

	private boolean isInDanger() {

		return this._danger_source != null;
	}

	private boolean isInLove() {

		return this._mate_target != null;
	}

	private boolean isHorny() {

		return this._desire > HORNY;
	}

	private void lookForDanger() {

		this._danger_source = search();
	}

	private void lookForMate() {

		this._mate_target = search();
	}

	private void eat(double dt) {

		this._energy += this._region_mngr.get_food(this, dt);
		if (this._energy > MAX_ENERGY)
			this._energy = MAX_ENERGY;
	}

	private void gainDesire(double dt) {

		this._desire += DESIRE * dt;
		if (this._desire > MAX_DESIRE)
			this._desire = MAX_DESIRE;
	}

	private void loseEnergy(double dt) {

		this._energy -= ENERGY * dt;
		if (this._energy < MIN_ENERGY)
			this._energy = MIN_ENERGY;
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

	@Override
	protected void setStateToNormal() {
		super.setStateToNormal();
		this._danger_source = null;
	}

	private void setStateToMate() {

		this._state = State.MATE;
		this._danger_source = null;
	}

	private void setStateToDanger() {

		this._state = State.DANGER;
		this._mate_target = null;
	}

}