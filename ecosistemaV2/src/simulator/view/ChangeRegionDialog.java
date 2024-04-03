package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

	private DefaultComboBoxModel<String> _regionsModel;
	private DefaultComboBoxModel<String> _fromRowModel;
	private DefaultComboBoxModel<String> _toRowModel;
	private DefaultComboBoxModel<String> _fromColModel;
	private DefaultComboBoxModel<String> _toColModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _regionsInfo;
	private String[] _headers = { "Key", "Value", "Description" };

	// TODO en caso de ser necesario, añadir los atributos aquí…
	private int _status;

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		this._ctrl.addObserver(this);
		initGUI();
		// TODO registrar this como observer;
	}

	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		// TODO crea varios paneles para organizar los componentes visuales en el
		// dialogo, y añadelos al mainpanel. P.ej., uno para el texto de ayuda,
		// uno para la tabla, uno para los combobox, y uno para los botones.
		// TODO crear el texto de ayuda que aparece en la parte superior del diálogo y
		// añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)
		// _regionsInfo se usará para establecer la información en la tabla
		_regionsInfo = Main._regions_factory.get_info();
		// _dataTableModel es un modelo de tabla que incluye todos los parámetros de
		// la region
		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO hacer editable solo la columna 1
				return column == 1;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		// TODO crear un JTable que use _dataTableModel, y añadirlo al diálogo
		// _regionsModel es un modelo de combobox que incluye los tipos de regiones
		JTable data = new JTable(this._dataTableModel);
		_regionsModel = new DefaultComboBoxModel<>();
		// TODO añadir la descripción de todas las regiones a _regionsModel, para eso
		// usa la clave “desc” o “type” de los JSONObject en _regionsInfo,
		for (JSONObject r : this._regionsInfo) {
			String s = r.getString("type");
			this._regionsModel.addElement(s);
		}
		// ya que estos nos dan información sobre lo que puede crear la factoría.
		// TODO crear un combobox que use _regionsModel y añadirlo al diálogo.
		JComboBox regions_box = new JComboBox(this._regionsModel);
		this.add(regions_box);
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel,
		// _fromColModel y _toColModel.
		JComboBox fromRow_box = new JComboBox(this._fromRowModel);
		this.add(fromRow_box);
		JComboBox toRow_box = new JComboBox(this._toRowModel);
		this.add(toRow_box);
		JComboBox fromCol_box = new JComboBox(this._fromColModel);
		this.add(fromCol_box);
		JComboBox toCol_box = new JComboBox(this._toColModel);
		this.add(toCol_box);
		// TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
		// TODO crear los botones OK y Cancel y añadirlos al diálogo.
		JButton ok = new JButton("OK");
		ok.addActionListener((e) -> {
			if (this._regionsModel.getSelectedItem() != null) {
				this._status = 1;
				ChangeRegionsDialog.this.setVisible(true);
			}
		});
		this.add(ok);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((e) -> {
			this._status = 0;
			this.setVisible(false);
		});
		this.add(cancel);

		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open(Frame parent) {
		setLocation(//
				parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, //
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}
	// TODO el resto de métodos van aquí…

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._toRowModel.removeAllElements();
		this._fromRowModel.removeAllElements();
		this._toColModel.removeAllElements();
		this._fromColModel.removeAllElements();
		this._regionsModel.removeAllElements();
		for (int i = 0; i < map.get_cols(); i++) {
			this._toColModel.addElement("" + i);
			this._fromColModel.addElement("" + i);
		}
		for (int i = 0; i < map.get_rows(); i++) {
			this._toRowModel.addElement("" + i);
			this._fromRowModel.addElement("" + i);
		}
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}
}
