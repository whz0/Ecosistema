package simulator.view;

import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.MapInfo;

import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class MapViewer extends AbstractMapViewer {

	// Anchura/altura/ de la simulación -- se supone que siempre van a ser iguales
	// al tamaño del componente
	private int _width;
	private int _height;

	// Número de filas/columnas de la simulación
	private int _rows;
	private int _cols;

	// Anchura/altura de una región
	int _rwidth;
	int _rheight;

	// Mostramos sólo animales con este estado. Los posibles valores de _currState
	// son null, y los valores deAnimal.State.values(). Si es null mostramos todo.
	Animal.State _currState;

	// En estos atributos guardamos la lista de animales y el tiempo que hemos
	// recibido la última vez para dibujarlos.
	volatile private Collection<AnimalInfo> _objs;
	volatile private Double _time;

	// Una clase auxilar para almacenar información sobre una especie
	private static class SpeciesInfo {
		private Integer _count;
		private Color _color;

		SpeciesInfo(Color color) {
			_count = 0;
			_color = color;
		}
	}

	// Un mapa para la información sobre las especies
	Map<String, SpeciesInfo> _kindsInfo = new HashMap<>();

	// El font que usamos para dibujar texto
	private Font _font = new Font("Arial", Font.BOLD, 12);

	// Indica si mostramos el texto la ayuda o no
	private boolean _showHelp;

	public MapViewer() {
		initGUI();
	}

	private void initGUI() {

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'h':
					_showHelp = !_showHelp;
					repaint();
					break;
				case 's':

					int pos;
					if (_currState == null)
						pos = 0;
					else
						pos = _currState.ordinal() + 1;
					if (pos == Animal.State.values().length)
						_currState = null;
					else
						_currState = Animal.State.values()[pos];
					repaint();

				default:
				}
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus(); // Esto es necesario para capturar las teclas cuando el ratón está sobre este
								// componente.
			}
		});

		// Por defecto mostramos todos los animales
		_currState = null;

		// Por defecto mostramos el texto de ayuda
		_showHelp = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Cambiar el font para dibujar texto
		g.setFont(_font);

		// Dibujar fondo blanco
		gr.setBackground(Color.WHITE);
		gr.clearRect(0, 0, _width, _height);

		// Dibujar los animales, el tiempo, etc.
		if (_objs != null)
			drawObjects(gr, _objs, _time);

		g.setColor(Color.red);
		if (_showHelp) {
			g.drawString("h: toggle help", 10, 20);
			g.drawString("s: show animals of a specific state",10,35);
		}

	}

	private boolean visible(AnimalInfo a) {

		return _currState == null || a.get_state().equals(_currState);
	}

	private void drawObjects(Graphics2D g, Collection<AnimalInfo> animals, Double time) {

		// TODO Dibujar el grid de regiones

		for (int i = 0; i < _width; i += _rwidth) {
			g.drawRect(i, 0, _width, _height);
		}
		for (int i = 0; i < _height; i += _rheight) {
			g.drawRect(0, i, _width, _height);
		}

		// Dibujar los animales
		for (AnimalInfo a : animals) {

			// Si no es visible saltamos la iteración
			if (!visible(a))
				continue;

			// La información sobre la especie de 'a'
			SpeciesInfo esp_info = _kindsInfo.get(a.get_genetic_code());

			if (esp_info == null) {
				esp_info = new SpeciesInfo(ViewUtils.get_color(a.get_genetic_code()));
				_kindsInfo.put(a.get_genetic_code(), esp_info);
			}

			esp_info._count++;

			g.setColor(esp_info._color);
			g.fillOval((int) Math.floor(a.get_position().getX()), (int) Math.floor(a.get_position().getY()),
					(int) Math.floor(a.get_age() / 2 + 2), (int) Math.floor(a.get_age() / 2 + 2));

		}

		// TODO Dibujar la etiqueta del estado visible, sin no es null.
		if (_currState != null) {
			g.setColor(ViewUtils.get_color(_currState.toString()));
			switch (_currState) {
			case NORMAL:
				drawStringWithRect(g, 10, _height - 20, "NORMAL");
				break;
			case HUNGER:
				drawStringWithRect(g, 10, _height - 20, "HUNGER");
				break;
			case MATE:
				drawStringWithRect(g, 10, _height - 20, "MATE");
				break;
			case DANGER:
				drawStringWithRect(g, 10, _height - 20, "DANGER");
				break;
			default:
				drawStringWithRect(g, 10, _height - 20, "DEAD");
			}
		}

		g.setColor(Color.MAGENTA);
		drawStringWithRect(g, 10, _height - 40, "Time: " + String.format("%.3f", time));

		int i = 0;
		for (Entry<String, SpeciesInfo> e : _kindsInfo.entrySet()) {
			g.setColor(e.getValue()._color);
			drawStringWithRect(g, 10, _height - 60 - i * 20, e.getKey().toString() + ": " + e.getValue()._count);
			e.getValue()._count = 0;
			i++;
		}
	}

	// Un método que dibujar un texto con un rectángulo
	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {

		this._objs = objs;
		this._time = time;
		repaint();
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {

		this._width = map.get_width();
		this._height = map.get_height();
		this._cols = map.get_cols();
		this._rows = map.get_rows();
		this._rwidth = map.get_region_width();
		this._rheight = map.get_region_height();
		this._time = time;

		setPreferredSize(new Dimension(map.get_width(), map.get_height()));

		update(animals, time);
	}

}
