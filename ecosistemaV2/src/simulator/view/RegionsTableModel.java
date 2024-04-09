package simulator.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	private Controller _ctrl;
	private Map<MapInfo.RegionData, Map<Animal.Diet, Integer>> _regions;
	private String[] _colNames = { "Row", "Col", "Desc.", Animal.Diet.values().toString() };

	RegionsTableModel(Controller ctrl) {
		
		this._regions = new TreeMap<>(new Comparator<>() {
			@Override
			public int compare(RegionData o1, RegionData o2) {
				if (o1.row() > o2.row() || (o1.row() == o2.row() && o1.col() > o2.col()))
					return 1;
				else if (o1.row() < o2.row() || (o1.row() == o2.row() && o1.col() < o2.col()))
					return -1;
				else
					return 0;
			}
		});
		this._ctrl = ctrl;
		this._ctrl.addObserver(this);

	}

	@Override
	public int getRowCount() {

		return this._regions.size();
	}

	@Override
	public int getColumnCount() {

		return this._colNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object s = null;
		List<MapInfo.RegionData> regions = new ArrayList<>(this._regions.keySet());
		MapInfo.RegionData r = regions.get(rowIndex);

		switch (columnIndex) {
		case 0:
			s = r.row();
			break;
		case 1:
			s = r.col();
			break;
		case 2:
			s = r.toString();
			break;
		default: {
			Animal.Diet a = Animal.Diet.valueOf(this._colNames[columnIndex]);
			s = this._regions.get(r).get(a);
		}

		}

		return s;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub

	}

	private void update(MapInfo map) {

		map.forEach((r) -> {
			List<AnimalInfo> animal = new ArrayList<>(r.r().getAnimalsInfo());
			Map<Animal.Diet, Integer> animals = new HashMap<>();
			for (AnimalInfo a : animal) {
				if (animals.containsKey(a.get_diet())) {
					int num = animals.get(a.get_diet());
					animals.replace(a.get_diet(), num, ++num);
				} else
					animals.put(a.get_diet(), 1);
			}
			this._regions.put(r, animals);
		});
	}
}
