package simulator.model;

import java.util.Iterator;
import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {

		if (!as.isEmpty()) {
			Iterator<Animal> i = as.iterator();
			double min = a.get_sight_range();
			Animal a1 = null;
			while (i.hasNext()) { 
				a1 = i.next();
				if (a.get_position().distanceTo(a1.get_position()) <= min)
					a1 = a;
			}
			return a1;
		}
		return null;
	}

}