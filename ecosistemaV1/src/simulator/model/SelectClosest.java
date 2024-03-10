package simulator.model;

import java.util.Iterator;
import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {

		if (!as.isEmpty()) {
			Iterator<Animal> i = as.iterator();
			double min = a.get_sight_range();
			Animal a1 = i.next();
			while (i.hasNext()) {
				Animal a2;
				a2 = i.next();
				if (a.get_position().distanceTo(a2.get_position()) <= min && !a.equals(a2))
					a1 = a2;
			}
			return a1.equals(a) ? a1 : null;
		}
		return null;
	}

}
