package simulator.model;

public interface SelectionStrategy {
	
	Animal select(Animal a, List<Animal> as);
}
