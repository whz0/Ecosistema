package simulator.model;

public interface AnimalMapView extends MapInfo, FoodSupplier {
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter);
}
