package simulator.model;

import java.util.Iterator;
import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {

		Iterator<Animal> i = as.iterator();
		double min = -1;
		Animal a1 = null;

		while (i.hasNext()) {
			Animal a2;
			a2 = i.next();
			if (min == -1 || a2.get_age() <= min)
				a1 = a2;
		}

		return a1;
	}

}
