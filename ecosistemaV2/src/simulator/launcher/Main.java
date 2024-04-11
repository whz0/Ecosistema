package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.DefaultRegionBuilder;
import simulator.factories.DynamicSupplyRegionBuilder;
import simulator.factories.Factory;
import simulator.factories.SelectClosestBuilder;
import simulator.factories.SelectFirstBuilder;
import simulator.factories.SelectYoungestBuilder;
import simulator.factories.SheepBuilder;
import simulator.factories.WolfBuilder;
import simulator.misc.Utils;
import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.SelectionStrategy;
import simulator.model.Simulator;
import simulator.view.MainWindow;

public class Main {

	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

		private String _tag;
		private String _desc;

		private ExecMode(String modeTag, String modeDesc) {
			_tag = modeTag;
			_desc = modeDesc;
		}

		public String get_tag() {
			return _tag;
		}

		public String get_desc() {
			return _desc;
		}
	}

	// default values for some parameters
	//
	private final static Double _default_time = 10.0; // in seconds
	public final static Double _default_delta_time = 0.03;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = null;
	private static String _in_file = null;
	private static String _out_file = null;
	public static Double _delta_time = null;
	private static boolean _viewer = false;
	private static ExecMode _mode = ExecMode.GUI;
	public static Factory<SelectionStrategy> _selections_factory;
	public static Factory<Region> _regions_factory;
	public static Factory<Animal> _animals_factory;
	private static Controller _controler;
	private static Simulator _simulator;
	private static FileOutputStream _out;

	private static void parse_args(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = build_options();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parse_delta_time_option(line);
			parse_help_option(line, cmdLineOptions);
			parse_in_file_option(line);
			parse_time_option(line);
			parse_out_file_option(line);
			parse_viewer_option(line);
			parse_mode_option(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options build_options() {
		Options cmdLineOptions = new Options();

		cmdLineOptions
				.addOption(
						Option.builder("dt").longOpt("delta-time").hasArg()
								.desc("A double representing actual time, in\n"
										+ "seconds, per simulation step. Default value: " + _default_delta_time + ".")
								.build());
		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg()
				.desc("A configuration file (optional in GUI mode).").build());

		// steps
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
				.desc("time in seconds. Default value: " + _default_time + ". (only for BATCH mode).").build());

		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("A file where output is written (only for BATCH mode).").build());

		cmdLineOptions.addOption(Option.builder("sv").longOpt("simple-viewer").desc("Viewer mode in console.").build());

		cmdLineOptions
				.addOption(
						Option.builder("m").longOpt("mode").hasArg()
								.desc("Execution Mode. Possible values: 'batch' (Batch\n"
										+ "mode), 'gui' (Graphical User Interface mode).\n" + "Default value: 'gui'.")
								.build());

		return cmdLineOptions;
	}

	private static void parse_out_file_option(CommandLine line) throws ParseException {
		_out_file = line.getOptionValue("o");
		if (_mode == ExecMode.BATCH && _out_file == null) {
			throw new ParseException("In batch mode an output configuration file is required");
		}
	}

	private static void parse_delta_time_option(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _default_delta_time.toString());
		try {
			_delta_time = Double.parseDouble(dt);
			assert (_delta_time >= 0 && _delta_time < _time);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + dt);
		}
	}

	private static void parse_viewer_option(CommandLine line) {

		if (line.hasOption("sv"))
			_viewer = true;
	}

	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parse_in_file_option(CommandLine line) throws ParseException {
		_in_file = line.getOptionValue("i");
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}

	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _default_time.toString());
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}

	private static void parse_mode_option(CommandLine line) throws ParseException {
		String mode = line.getOptionValue("m");
		if (mode.equalsIgnoreCase("batch"))
			_mode = ExecMode.BATCH;
		else if (mode.equalsIgnoreCase("gui"))
			_mode = ExecMode.GUI;
		else
			throw new ParseException("Invalid mode for execute");
	}

	private static void init_factories() {

		_selections_factory = initialize_selection_strategy_builders();
		_regions_factory = initialize_regions_builder();
		_animals_factory = initialize_animals_builders();
	}

	private static Factory<SelectionStrategy> initialize_selection_strategy_builders() {

		List<Builder<SelectionStrategy>> selection_strategy_builders;
		selection_strategy_builders = new ArrayList<>();
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		selection_strategy_builders.add(new SelectFirstBuilder());
		Factory<SelectionStrategy> selection_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(
				selection_strategy_builders);
		return selection_strategy_factory;
	}

	private static Factory<Animal> initialize_animals_builders() {

		List<Builder<Animal>> animals_builders = new ArrayList<>();
		animals_builders.add(new SheepBuilder(_selections_factory));
		animals_builders.add(new WolfBuilder(_selections_factory));
		Factory<Animal> animal_factory = new BuilderBasedFactory<Animal>(animals_builders);
		return animal_factory;
	}

	private static Factory<Region> initialize_regions_builder() {

		List<Builder<Region>> regions_builders = new ArrayList<>();
		regions_builders.add(new DynamicSupplyRegionBuilder());
		regions_builders.add(new DefaultRegionBuilder());
		Factory<Region> region_factory = new BuilderBasedFactory<Region>(regions_builders);
		return region_factory;
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static void start_batch_mode() throws Exception {
		try (InputStream is = new FileInputStream(new File(_in_file))) {
			_out = new FileOutputStream(new File(_out_file));
			JSONObject j = load_JSON_file(is);
			_simulator = new Simulator(j.getInt("cols"), j.getInt("rows"), j.getInt("width"), j.getInt("height"),
					_animals_factory, _regions_factory);
			_controler = new Controller(_simulator);
			_controler.load_data(j);
			_controler.run(_time, _delta_time, _viewer, _out);
			_out.close();
		}
	}

	private static void start_GUI_mode() throws Exception {

		if (_in_file == null)
			_simulator = new Simulator(800, 600, 15, 20, _animals_factory, _regions_factory);
		else
			try (InputStream is = new FileInputStream(new File(_in_file))) {
				JSONObject j = load_JSON_file(is);
				_simulator = new Simulator(j.getInt("cols"), j.getInt("rows"), j.getInt("width"), j.getInt("height"),
						_animals_factory, _regions_factory);
				_controler = new Controller(_simulator);
				_controler.load_data(j);
			}
		SwingUtilities.invokeAndWait(() -> new MainWindow(_controler));
		
	}

	private static void start(String[] args) throws Exception {
		init_factories();
		parse_args(args);
		switch (_mode) {
		case BATCH:
			start_batch_mode();
			break;
		case GUI:
			start_GUI_mode();
			break;
		}
	}

	public static void main(String[] args) {
		Utils._rand.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
