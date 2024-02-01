package simulator.model;

import java.util.Comparator;
import java.util.List;

public class SelectFirst implements SelectionStrategy, Comparator{


	public int compare(Object o1, Object o2) {
		
		return 0;
	}

	@Override
	public Animal select(Animal a, List<Animal> as) {
		
		return null;
	}

}
