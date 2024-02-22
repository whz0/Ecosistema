package simulator.model;

import java.util.Iterator;
import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {

		Iterator<Animal> i = as.iterator();
		double min = a.get_sight_range();
		Animal a1 = null;

		while (i.hasNext()) {
			Animal a2;
			a2 = i.next();
			if (a.get_position().distanceTo(a2.get_position()) <= min)
				a1 = a2;
		}

		return a1;
	}

}
