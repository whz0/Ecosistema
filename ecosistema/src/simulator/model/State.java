package simulator.model;

public enum State {

	NORMAL(0),MATE(1),HUNGER(2),DANGER(3),DEAD(4);
	
	private int state;
	
	private State(int state) {
		this.state = state;
	}
}
