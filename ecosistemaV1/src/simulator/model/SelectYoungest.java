package simulator.model;

import java.util.Comparator;
import java.util.List;

public class SelectYoungest implements SelectionStrategy, Comparator<Animal> {

	@Override
	public Animal select(Animal a, List<Animal> as) {

	
			return null;
	}

	@Override
	public int compare(Animal o1, Animal o2) {

		if (o1.get_age() < o2.get_age())
			return -1;
		else if (o1.get_age() == o2.get_age())
			return 0;
		else
			return 1;
	}

}
