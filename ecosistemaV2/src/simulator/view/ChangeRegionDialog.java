package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JLabel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
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

	private JTextPane _help_text;
	private int _status;

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JPanel text_panel = new JPanel();
		mainPanel.add(text_panel);
		JPanel table_panel = new JPanel();
		mainPanel.add(table_panel);
		JPanel combobox_panel = new JPanel();
		mainPanel.add(combobox_panel);
		JPanel button_panel = new JPanel();
		mainPanel.add(button_panel);
		// TODO crear el texto de ayuda que aparece en la parte superior del diálogo y
		// añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)
		// _regionsInfo se usará para establecer la información en la tabla
		this._help_text = new JTextPane();
		this._help_text.setContentType("text/html");
		this._help_text.setText(
				"<html><body><p>Select a region type, the rows/cols interval, and provide values for the <br>parameters in the <strong>Value Column</strong> (default values are used for parameters with no value)</p></body></html>");
		this._help_text.setEditable(false);
		this._help_text.setOpaque(false);
		text_panel.add(this._help_text);

		_regionsInfo = Main._regions_factory.get_info();

		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {

				return column == 1;
			}

		};
		_dataTableModel.setColumnIdentifiers(_headers);
		_dataTableModel.setRowCount(this._regionsInfo.size());

		JTable data = new JTable(this._dataTableModel);
		data.setPreferredSize(new Dimension(700, 300));
		table_panel.add(data);
		
		_regionsModel = new DefaultComboBoxModel<>();
		// TODO añadir la descripción de todas las regiones a _regionsModel, para eso
		// usa la clave “desc” o “type” de los JSONObject en _regionsInfo,
		for (JSONObject r : this._regionsInfo) {
			String s = r.getString("type");
			this._regionsModel.addElement(s);
		}
		// ya que estos nos dan información sobre lo que puede crear la factoría.
		// TODO crear un combobox que use _regionsModel y añadirlo al diálogo.
		JLabel type_label = new JLabel(" Region type: ");
		combobox_panel.add(type_label);
		JComboBox regions_box = new JComboBox(this._regionsModel);
		combobox_panel.add(regions_box);
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel,
		// _fromColModel y _toColModel.
		this._fromColModel = new DefaultComboBoxModel<String>();
		this._toColModel = new DefaultComboBoxModel<String>();
		this._toRowModel = new DefaultComboBoxModel<String>();
		this._fromRowModel = new DefaultComboBoxModel<String>();

		JLabel row_label = new JLabel(" Row from/to: ");
		combobox_panel.add(row_label);
		JComboBox fromRow_box = new JComboBox(this._fromRowModel);
		combobox_panel.add(fromRow_box);
		JComboBox toRow_box = new JComboBox(this._toRowModel);
		combobox_panel.add(toRow_box);

		JLabel col_label = new JLabel(" Col from/to: ");
		combobox_panel.add(col_label);
		JComboBox fromCol_box = new JComboBox(this._fromColModel);
		combobox_panel.add(fromCol_box);
		JComboBox toCol_box = new JComboBox(this._toColModel);
		combobox_panel.add(toCol_box);

		JButton cancel_button = new JButton("Cancel");
		cancel_button.addActionListener((e) -> {
			this._status = 0;
			ChangeRegionsDialog.this.setVisible(false);
		});
		button_panel.add(cancel_button);

		JButton ok_button = new JButton("OK");
		ok_button.addActionListener((e) -> {
			if (this._regionsModel.getSelectedItem() != null) {
				this._status = 1;
				ChangeRegionsDialog.this.setVisible(true);
			}
		});
		button_panel.add(ok_button);

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

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

		for (int i = 0; i < map.get_cols(); i++) {
			_toColModel.addElement("" + i);
			_fromColModel.addElement("" + i);
		}
		for (int i = 0; i < map.get_rows(); i++) {
			_toRowModel.addElement("" + i);
			_fromRowModel.addElement("" + i);
		}
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._toRowModel.removeAllElements();
		this._fromRowModel.removeAllElements();
		this._toColModel.removeAllElements();
		this._fromColModel.removeAllElements();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		_toColModel.addElement("" + col);
		_fromColModel.addElement("" + col);

		_toRowModel.addElement("" + row);
		_fromRowModel.addElement("" + row);
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}
	
	private void update(int i) {
		JSONObject info = this._regionsInfo.get(i);
		JSONObject data = info.getJSONObject("data");
		data.keySet().forEach((s)->{
			this._dataTableModel.setValueAt(data, i, i);
		});
	}
}
