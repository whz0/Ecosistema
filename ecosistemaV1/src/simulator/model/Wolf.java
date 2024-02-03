package simulator.model;

import simulator.misc.Vector2D;

public class Wolf extends Animal {

	private final static double INIT_SIGHT = 50.0;
	private final static double INIT_SPEED = 60.0;
	private final static double REACH_DISTANCE = 8.0;
	private final static double MAX_ENERGY = 100.0;
	private final static double MIN_ENERGY = 0.0;
	private final static double MAX_AGE = 12.0;
	private final static double DESIRE = 30.0;
	private final static double ENERGY = 18.0;
	private final static double ENERGY_GAIN = 50.0;
	private final static double ENERGY_LOOSE = 50.0;
	private final static double HUNGRY = 50.0;
	private final static double RUN_BUFF = 3.0;

	private Animal _hunter_target;
	private SelectionStrategy _hunting_strategy;

	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) {
		super("Wolf", Diet.CARNIVORE, INIT_SIGHT, INIT_SPEED, mate_strategy, pos);
		this._hunting_strategy = hunting_strategy;
	}

	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		this._hunting_strategy = p1._hunting_strategy;
		this._hunter_target = null;
	}

	@Override
	public void update(double dt) {

		if (!isDead()) {
			switch (this._state) {
			case NORMAL:
				normalStateUpdate(dt);
				break;
			case HUNGER:
				hungerStateUpdate(dt);
				break;
			case MATE:
				mateStateUpdate(dt);
				break;
			default:
				break;
			}
			if (this._pos.isOutOfMap(this._region_mngr)) {
				this._pos.fixPos(this._region_mngr);
				setStateToNormal();
				if (this._energy > MIN_ENERGY || this._age <= MAX_AGE) {
					if (!isDead())
						eat(dt);
				} else
					setStateToDead();
			}
		}
	}

	private void mateStateUpdate(double dt) {

		if (isInLove() && (this._mate_target.isDead() || isOutOfRange(this._mate_target)))
			this._mate_target = null;
		else {
			lookForMate();
			if (!isInLove())
				advance(DESIRE, ENERGY, dt);
			else {
				towardsToMate();
				specialAdvance(DESIRE, ENERGY, RUN_BUFF, dt);
				if (this._pos.distanceTo(this._mate_target.get_position()) < REACH_DISTANCE) {
					resetDesire();
					this._mate_target.resetDesire();
					if (canHaveBaby())
						haveBaby();
				}
			}
		}
		if (isHungry())
			setStateToHunger();
		else {
			if (!isHorny())
				setStateToNormal();
		}
	}

	private void hungerStateUpdate(double dt) {

		if (!isInChase() || this._hunter_target.isDead() || isOutOfRange(_hunter_target))
			lookForFood();
		if (!isInChase())
			advance(DESIRE, ENERGY, dt);
		else {
			runTowardsPrey();
			specialAdvance(DESIRE, ENERGY, RUN_BUFF, dt);
			if (this._pos.distanceTo(this._hunter_target.get_position()) < REACH_DISTANCE)
				hunt();
		}
		if (!isHungry()) {
			if (!isHorny())
				setStateToNormal();
			else
				setStateToMate();
		}

	}

	private void normalStateUpdate(double dt) {

		advance(DESIRE, ENERGY, dt);
		if (isHungry())
			setStateToHunger();
		else {
			if (isHorny())
				setStateToMate();
		}

	}

	private void runTowardsPrey() {

		this._dest = this._hunter_target.get_position();
	}

	private void hunt() {

		this._hunter_target.setStateToDead();
		this._hunter_target = null;
		this._energy += ENERGY_GAIN;
		if (this._energy > MAX_ENERGY)
			this._energy = MAX_ENERGY;
	}

	private boolean isHungry() {

		return this._energy < HUNGRY;
	}

	private boolean isInChase() {

		return this._hunter_target != null;
	}

	private void haveBaby() {

		this._baby = new Wolf(this, this._mate_target);
		this._energy -= ENERGY_LOOSE;
		if (this._energy < MIN_ENERGY)
			this._energy = MIN_ENERGY;
		this._mate_target = null;
	}

	private void lookForFood() {

		this._hunter_target = search();
	}

	private void setStateToHunger() {

		this._state = State.HUNGER;
		this._mate_target = null;
	}

	@Override
	protected void setStateToNormal() {
		super.setStateToNormal();
		this._hunter_target = null;
	}

	protected void setStateToMate() {

		this._state = State.MATE;
		this._hunter_target = null;
	}
}
