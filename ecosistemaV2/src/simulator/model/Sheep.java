package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Vector2D;

public class Sheep extends Animal {

	private final static double INIT_SIGHT = 40.0;
	private final static double INIT_SPEED = 35.0;
	private final static double REACH_DISTANCE = 8.0;
	private final static double MAX_AGE = 8.0;
	private final static double DESIRE = 40.0;
	private final static double ENERGY = 20.0;
	private final static double RUN_BUFF = 2.0;

	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos)
			throws IllegalArgumentException {
		super("Sheep", Diet.HERVIBORE, INIT_SIGHT, INIT_SPEED, mate_strategy, pos);
		if (danger_strategy == null)
			throw new IllegalArgumentException("Invalid danger_strategy");
		this._danger_strategy = danger_strategy;
		this._danger_source = null;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this._danger_source = null;
		this._danger_strategy = p1._danger_strategy;
	}

	protected void mateStateUpdate(double dt) {

		if (isInLove() && (this._mate_target.isDead() || isOutOfRange(this._mate_target)))
			this._mate_target = null;
		if (!isInLove())
			lookForMate();
		if (!isInLove())
			advance(DESIRE, ENERGY, dt);
		else {
			towardsToMate();
			specialAdvance(DESIRE, ENERGY, RUN_BUFF, dt);
			if (this._pos.distanceTo(this._mate_target.get_position()) < REACH_DISTANCE) {
				resetDesire();
				this._mate_target.resetDesire();
				if (canHaveBaby()) {
					this._baby = new Sheep(this, this._mate_target);
					this._mate_target = null;
				}
			}
		}
		if (!isInDanger())
			lookForDanger();
		if (isInDanger())
			setStateToDanger();
		else if (!isInDanger() && !hasDesireToMate())
			setStateToNormal();

	}

	protected void dangerStateUpdate(double dt) {

		if (isInDanger() && this._danger_source.isDead())
			this._danger_source = null;
		if (!isInDanger())
			advance(DESIRE, ENERGY, dt);
		else {
			runFromDanger();
			specialAdvance(DESIRE, ENERGY, RUN_BUFF, dt);
		}
		if (!isInDanger() || this._danger_source.isOutOfRange(this))
			lookForDanger();
		if (!isInDanger()) {
			if (hasDesireToMate())
				setStateToMate();
			else
				setStateToNormal();
		}

	}

	protected void normalStateUpdate(double dt) {

		advance(DESIRE, ENERGY, dt);
		if (!isInDanger())
			lookForDanger();
		if (!isInDanger() && hasDesireToMate())
			setStateToMate();
		else if (isInDanger())
			setStateToDanger();

	}

	private void runFromDanger() {

		this._dest = this._pos.plus(this._pos.minus(this._danger_source.get_position()).direction());
	}

	private boolean isInDanger() {

		return this._danger_source != null;
	}

	private void lookForDanger() {

		List<Animal> list = new ArrayList<Animal>();
		list = this._region_mngr.get_animals_in_range(this, (a) -> a.get_diet().equals(Diet.CARNIVORE));
		this._danger_source = this._danger_strategy.select(this, list);
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

	@Override
	protected boolean toOld() {
		return this._age >= MAX_AGE;
	}

	@Override
	protected void hungerStateUpdate(double dt) {
	}
}