package simulator.model;

import java.util.List;

public class SelectFirst implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {

		if (!as.isEmpty())
			return as.get(0).equals(a) ? as.get(0) : as.size() > 1 ? as.get(1) : null;
		return null;
	}

}
