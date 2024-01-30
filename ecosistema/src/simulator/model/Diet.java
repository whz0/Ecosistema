package simulator.model;

public enum Diet {
	
	HERVIBORE(0), CARNIVORE(1);
	
	private int diet;

	private Diet(int diet) {
		this.diet = diet;
	}
}
