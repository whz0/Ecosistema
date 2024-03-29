package simulator.model;

import java.util.Iterator;
import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {

		if (!as.isEmpty()) {
			Iterator<Animal> i = as.iterator();
			Animal a1 = i.next();
			double min = a1.get_age();
			while (i.hasNext()) {
				Animal a2 = i.next();
				if (a2.get_age() <= min && !a.equals(a2))
					a1 = a2;
			}
			return a.equals(a1) ? a1 : null;
		}
		return null;
	}

}
