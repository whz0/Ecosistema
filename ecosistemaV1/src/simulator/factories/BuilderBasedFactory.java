package simulator.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import org.json.JSONObject;

import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.SelectionStrategy;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _builders_info;

	public BuilderBasedFactory() {
		this._builders = new HashMap<String, Builder<T>>();
		this._builders_info = new LinkedList<JSONObject>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {

		this();
		for (Builder<T> b : builders) {
			add_builder(b);
		}
	}

	public void add_builder(Builder<T> b) {

		this._builders.put(b.get_type_tag(), b);
		this._builders_info.add(b.get_info());
	}

	@Override
	public T create_instance(JSONObject info) {
		if (info == null) {
			throw new IllegalArgumentException("’info’ cannot be null");
		}
		if (this._builders.containsKey(info.getString("type"))) {
			Builder<T> builder = this._builders.get(info.getString("type"));
			if (info.has("data")) {
				return builder.create_instance(info.getJSONObject("data"));
			} else {
				return builder.create_instance(new JSONObject());
			}
		} else
			throw new IllegalArgumentException("Unrecognized ‘info’:" + info.toString());
	}

	public static Factory<SelectionStrategy> initialize_selection_strategy_builders() {

		List<Builder<SelectionStrategy>> selection_strategy_builders;
		selection_strategy_builders = new ArrayList<>();
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		selection_strategy_builders.add(new SelectFirstBuilder());
		Factory<SelectionStrategy> selection_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(
				selection_strategy_builders);
		return selection_strategy_factory;
	}

	public static Factory<Animal> initialize_animals_builders() {

		List<Builder<Animal>> animals_builders = new ArrayList<>();
		animals_builders.add(new SheepBuilder(new BuilderBasedFactory<SelectionStrategy>()));
		animals_builders.add(new WolfBuilder(new BuilderBasedFactory<SelectionStrategy>()));
		Factory<Animal> animal_factory = new BuilderBasedFactory<Animal>(animals_builders);
		return animal_factory;
	}

	public static Factory<Region> initialize_regions_builder() {

		List<Builder<Region>> regions_builders = new ArrayList<>();
		regions_builders.add(new DynamicSupplyRegionBuilder());
		regions_builders.add(new DefaultRegionBuilder());
		Factory<Region> region_factory = new BuilderBasedFactory<Region>(regions_builders);
		return region_factory;
	}

	@Override
	public List<JSONObject> get_info() {
		return Collections.unmodifiableList(_builders_info);
	}

}
