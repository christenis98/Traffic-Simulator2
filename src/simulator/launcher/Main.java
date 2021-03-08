package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
//import simulator.launcher.Main;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static int _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _sTime = null;
	private static String _sMode = null;
	private static int _time = 0;
	private static Factory<Event> _eventsFactory = null;
	//String type,TrafficSimulator sim, Factory<Event> eventsFactory
	
	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTickOption(line);
			
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

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc(
				"Ticks to the simulator, d value = 10. ").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				"Determina el modo de ejecucion del simulador GUI o consola. ").build());
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if(_sMode.equals("console")) {
			if (_inFile == null) {
				throw new ParseException("An events file is missing");
			}
		}
	}
	
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if(!_sMode.equals("gui")) {
			_outFile = line.getOptionValue("o");
		}
	}	
	
	private static void parseTickOption(CommandLine line) throws ParseException {
		if (line.hasOption("t")) {
			_sTime = line.getOptionValue("t");
			_time = Integer.parseInt(_sTime);
		}
		else {
			//Valor por defecto del timer.
			_time = _timeLimitDefaultValue;
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		_sMode = line.getOptionValue("m");
		//_sMode.toLowerCase();
		if(_sMode == null) {
			_sMode.equals("gui");
		}
		if(!_sMode.equals("gui") && !_sMode.equals("console")) {
			throw new ParseException("El modo es gui o consolas");
		}
	}

	private static void initFactories() {

		ArrayList <Builder<LightSwitchStrategy>> light = new ArrayList<>();
		light.add (new RoundRobinStrategyBuilder ("round_robin_lss"));
		light.add(new MostCrowdedStrategyBuilder ("most_crowded_lss"));
		Factory<LightSwitchStrategy> lssFactory = new BuilderBasedFactory<>(light);
		
		ArrayList<Builder<DequeuingStrategy>> deqStrat = new ArrayList<>();
		deqStrat.add(new MoveFirstStrategyBuilder("move_first_dqs"));
		deqStrat.add(new MoveAllStrategyBuilder("move_all_dqs"));
		
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(deqStrat);
		
		ArrayList<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder("new_junction", lssFactory,dqsFactory));
		ebs.add(new NewCityRoadEventBuilder ("new_city_road"));
		ebs.add(new NewInterCityRoadEventBuilder ("new_inter_city_road"));	
		ebs.add(new SetWeatherEventBuilder("set_weather"));
		ebs.add(new SetContClassEventBuilder("set_cont_class"));
		ebs.add(new NewVehicleEventBuilder("new_vehicle"));
		
		Factory<Event> eventsFactory = new BuilderBasedFactory<>(ebs);
		
		_eventsFactory = eventsFactory;
		
	}

	private static void startBatchMode() throws IOException {
		
		InputStream is = new FileInputStream(new File(_inFile));
		OutputStream os = null;
		if(_outFile== null)
			os = System.out;
		else
			os = new FileOutputStream(new File (_outFile));
		TrafficSimulator sim = new TrafficSimulator();
		Controller control = new Controller (sim, _eventsFactory);
		control.loadEvents(is);
		control.run(_time, os);
		is.close();
		
	}
	
	private static void startGUIMode() throws IOException{
		TrafficSimulator simulator = new TrafficSimulator();
		Controller controller = new Controller(simulator, _eventsFactory);
		InputStream is = null;
		
		if(_inFile != null) {
			is = new FileInputStream(new File(_inFile));
			controller.loadEvents(is);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});
		
		if(is != null) {
			is.close();
		}
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_sMode.equals("console")) {
			startBatchMode();
		}
		else if(_sMode.equals("gui")) {
			startGUIMode();
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
