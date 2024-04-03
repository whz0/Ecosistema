package simulator.view;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class ControlPanel extends JPanel {

	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private MapWindow _viewer;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _openButton;
	private JButton _runButton;
	private JButton _regionsButton;
	private JButton _viewerButton;
	private JButton _stopButton;
	private JTextField _delta_time_text;
	private JSpinner _step_spinner;

// TODO añade más atributos aquí …
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		add(_toolaBar, BorderLayout.PAGE_START);
// TODO crear los diferentes botones/atributos y añadirlos a _toolaBar.
// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
// _toolaBar.addSeparator() para añadir la línea de separación vertical
// entre las componentes que lo necesiten.
// Quit Button
		_openButton = new JButton();
		_openButton.setToolTipText("Open");
		_openButton.setIcon(new ImageIcon("resources/icon/open.png"));
		_openButton.addActionListener((e) -> {
			int v = _fc.showOpenDialog(ViewUtils.getWindow(this));
			if (v == JFileChooser.APPROVE_OPTION) {
				File file = this._fc.getSelectedFile(); // obtenemos el fichero seleccionado
				System.out.println("loading " + file.getName());
				JSONObject jo;
				try {
					jo = new JSONObject(new JSONTokener(new FileReader(file)));
					this._ctrl.reset(jo.getInt("cols"), jo.getInt("rows"), jo.getInt("width"), jo.getInt("height"));
					this._ctrl.load_data(jo);
				} catch (JSONException e1) {
					ViewUtils.showErrorMsg(e1.getMessage());
				} catch (FileNotFoundException e1) {
					ViewUtils.showErrorMsg(e1.getMessage());
				}
			} else
				System.out.println("load cancelled by user");
		});
		_toolaBar.add(_openButton);

		_toolaBar.addSeparator();
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("Viewer");
		_viewerButton.setIcon(new ImageIcon("resources/icon/viewer.png"));
		_viewerButton.addActionListener((e) -> this._viewer = new MapWindow(new JFrame(), this._ctrl));
		_toolaBar.add(_viewerButton);

		_toolaBar.addSeparator();
		_regionsButton = new JButton();
		_regionsButton.setToolTipText("Regions");
		_regionsButton.setIcon(new ImageIcon("resources/icon/regions.png"));
		_regionsButton.addActionListener((e) -> _changeRegionsDialog.open(ViewUtils.getWindow(this)));
		_toolaBar.add(_regionsButton);

		_toolaBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("Run");
		_runButton.setIcon(new ImageIcon("resoucers/icon/run.png"));
		_runButton.addActionListener((e) -> {
			enable_buttons();
			this._stopped = false;
			run_sim((Integer) this._step_spinner.getValue(), Double.valueOf(this._delta_time_text.getText()));
		});
		_toolaBar.add(_runButton);

		_toolaBar.addSeparator();
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stop");
		_stopButton.setIcon(new ImageIcon("resources/icon/stop.png"));
		_stopButton.addActionListener((e) -> this._stopped = true);
		_toolaBar.add(_stopButton);

		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolaBar.add(_quitButton);

// TODO Inicializar _fc con una instancia de JFileChooser. Para que siempre
// abre en la carpeta de ejemplos puedes usar:

// _fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
// TODO Inicializar _changeRegionsDialog con instancias del diálogo de cambio de regiones
		this._fc = new JFileChooser();
		this._fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));

		this._step_spinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 10));

		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
	}

	// TODO el resto de métodos van aquí…
	private void run_sim(int n, double dt) {
		if (n > 0 && !_stopped) {
			try {
				for (int i = 0; i < n; i++)
					_ctrl.advance(dt);
				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				ViewUtils.showErrorMsg(e.getMessage());
				enable_buttons();
				_stopped = true;
			}
		} else {
			enable_buttons();
			_stopped = true;
		}
	}

	private void enable_buttons() {
		this._quitButton.setEnabled(!_stopped);
		this._runButton.setEnabled(!_stopped);
		this._openButton.setEnabled(!_stopped);
		this._viewerButton.setEnabled(!_stopped);
		this._regionsButton.setEnabled(!_stopped);
	}

}
