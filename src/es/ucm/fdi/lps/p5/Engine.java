package es.ucm.fdi.lps.p5;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;

import es.ucm.fdi.lps.p5.command.Command;

/**
 * Represents the game engine that controls the execution of the game since the
 * beginning to the end, dealing with the configuration, the input stream and
 * the output stream.
 */
public class Engine {

	/**
	 * Platform-independent line separator
	 */
	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Message of prompt for the player (the name of the property).
	 * <ul>
	 * <li>This property is called "message.prompt" and its default value is
	 * "> ".</li>
	 * </ul>
	 */
	private static final String MESSAGE_PROMPT = "message.prompt";
	private String msgPrompt;

	/**
	 * Message of unknown command (the name of the property).
	 * <ul>
	 * <li>This property is called "message.unknownCommand" and its default
	 * value is "Pardon?".</li>
	 * </ul>
	 */
	private static final String MESSAGE_UNKNOWNCOMMAND = "message.unknownCommand";
	private String msgUnknownCommand;

	/**
	 * Message of location with items (the name of the property).
	 * <ul>
	 * <li>This property is called "message.locationWithItems" and its default
	 * value is "This location contains the following items: ".</li>
	 * </ul>
	 */
	private static final String MESSAGE_LOCATIONWITHITEMS = "message.locationWithItems";
	private String msgLocationWithItems;

	/**
	 * Message of location without items (the name of the property).
	 * <ul>
	 * <li>This property is called "message.locationWithoutItems" and its
	 * default value is "This location has no items.".</li>
	 * </ul>
	 */
	private static final String MESSAGE_LOCATIONWITHOUTITEMS = "message.locationWithoutItems";
	private String msgLocationWithoutItems;

	/**
	 * Message of the player score (the name of the property).
	 * <ul>
	 * <li>This property is called "message.playerScore" and its default value
	 * is "Player score: ".</li>
	 * </ul>
	 */
	private static final String MESSAGE_PLAYERSCORE = "message.playerScore";
	private String msgPlayerScore;

	/**
	 * Message of game over (the name of the property).
	 * <ul>
	 * <li>This property is called "message.gameOver" and its default value is
	 * "GAME OVER".</li>
	 * </ul>
	 */
	private static final String MESSAGE_GAMEOVER = "message.gameOver";
	private String msgGameOver;

	/**
	 * Flag for showing engine information (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showEngineInfo" and its default value
	 * is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWENGINEINFO = "flag.showEngineInfo";
	private boolean flagShowEngineInfo;

	/**
	 * Flag for showing game information (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showGameInfo" and its default value is
	 * "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWGAMEINFO = "flag.showGameInfo";
	private boolean flagShowGameInfo;

	/**
	 * Flag for auto-describing first location (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.autodescribeFirstLocation" and its
	 * default value is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_AUTODESCRIBEFIRSTLOCATION = "flag.autodescribeFirstLocation";
	private boolean flagAutodescribeFirstLocation;

	/**
	 * Flag for showing the items of each location (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showLocationItems" and its default
	 * value is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWLOCATIONITEMS = "flag.showLocationItems";
	private boolean flagShowLocationItems;

	/**
	 * Flag for showing the numerical value of the game items (the name of the
	 * property).
	 * <ul>
	 * <li>This property is called "flag.showItemsValue" and its default value
	 * is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWITEMSVALUE = "flag.showItemsValue";
	private boolean flagShowItemsValue;

	/**
	 * Flag for showing the weight of the game items (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showItemsWeight" and its default value
	 * is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWITEMSWEIGHT = "flag.showItemsWeight";
	private boolean flagShowItemsWeight;

	/**
	 * Limit of command history size for the undo command (the name of the
	 * property).
	 * <ul>
	 * <li>This property is called "limit.commandHistorySize" and its default
	 * value is "1".</li>
	 * </ul>
	 */
	private static final String LIMIT_COMMANDHISTORYSIZE = "limit.commandHistorySize";
	private int limitCommandHistorySize;

	/**
	 * Flag for showing available direction moves (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showConnections" and its default value
	 * is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWCONNECTIONS = "flag.showConnections";
	private boolean flagShowConnections;

	/**
	 * Flag for showing connections status (clear, open or closed) (the name of
	 * the property).
	 * <ul>
	 * <li>This property is called "flag.showConnectiosState" and its default
	 * value is "true".</li>
	 * </ul>
	 */
	private static final String FLAG_SHOWCONNECTIONSSTATE = "flag.showConnectionsState";
	private boolean flagShowConnectionsState;

	/**
	 * A reference to the game that created the Interpreter
	 */
	private Game game;

	/**
	 * The engine configuration
	 */
	private Properties config;

	/**
	 * The engine description
	 */
	private String engineInfo = "GAME ENGINE FOR TEXT ADVENTURES"
			+ LINE_SEPARATOR + "Version 1.0 (January 2011)" + LINE_SEPARATOR
			+ "Designed by Guillermo Jiménez and Federico Peinado";

	/**
	 * Print stream
	 */
	private PrintStream ps;

	/**
	 * The parser
	 */
	private Parser parser;
	
	/**
	 * The file input stream
	 */
	private InputStream input;
	
	/**
	 * Constructs the game engine using a given game. Default configuration,
	 * standard input and standard output are assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public Engine(Game game) {
		if (game == null)
			throw new IllegalArgumentException();
		this.game = game;
		setInput(System.in);
		setOutput(System.out);
		parser = new Parser(this.input, this.game);
		setDefaultConfiguration();
	}

	/**
	 * Constructs the game engine using a given game and a given configuration.
	 * The properties defined in the given configuration override those of the
	 * default configuration. Standard input and standard output are assumed at
	 * this moment.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The arguments 'game' and 'config' cannot be null.
	 */
	public Engine(Game game, Properties config) {
		if (config == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		this.config = config;
		setInput(System.in);
		setOutput(System.out);
		setDefaultConfiguration();
		setConfig(this.config);
		parser = new Parser(this.input, this.game, this.config);
	}

	/**
	 * Constructs the game engine using a given game and a given input stream.
	 * Default configuration and standard output are assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @param input
	 *            The input.
	 * @throws IllegalArgumentException
	 *             The arguments 'game' and 'input' cannot be null.
	 */
	public Engine(Game game, InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setInput(input);
		setOutput(System.out);
		setDefaultConfiguration();
		parser = new Parser(this.input, this.game);
	}

	/**
	 * Constructs the game engine using a given game and a given output stream.
	 * Default configuration and standard input are assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @param output
	 *            The output.
	 * @throws IllegalArgumentException
	 *             The arguments 'game' and 'output' cannot be null.
	 */
	public Engine(Game game, OutputStream output) {
		if (output == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setInput(System.in);
		setOutput(output);
		setDefaultConfiguration();
		parser = new Parser(this.input, this.game);
	}

	/**
	 * Constructs the game engine using a given game, a given input stream and a
	 * given output stream. Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @param input
	 *            The input.
	 * @param output
	 *            The output.
	 * @throws IllegalArgumentException
	 *             The arguments 'game', 'input' and 'output' cannot be null.
	 */
	public Engine(Game game, InputStream input, OutputStream output) {
		if ((input == null) || (output == null))
			throw new IllegalArgumentException();
		this.game = game;
		setInput(input);
		setOutput(output);
		setDefaultConfiguration();
		parser = new Parser(this.input, this.game);
	}

	/**
	 * Constructs the game engine using a given game, a given configuration and
	 * a given output stream. Standard output is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @param input
	 *            The input.
	 * @throws IllegalArgumentException
	 *             The arguments 'game', 'config' and 'input' cannot be null.
	 */
	public Engine(Game game, Properties config, InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		this.config = config;
		setInput(input);
		setOutput(System.out);
		setDefaultConfiguration();
		setConfig(this.config);
		parser = new Parser(this.input, this.game, this.config);
	}

	/**
	 * Constructs the game engine using a given game, a given configuration and
	 * a given input stream. Standard input is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @param output
	 *            The output.
	 * @throws IllegalArgumentException
	 *             The arguments 'game', 'config' and 'output' cannot be null.
	 */
	public Engine(Game game, Properties config, OutputStream output) {
		if (output == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		this.config = config;
		setInput(System.in);
		setOutput(output);
		setDefaultConfiguration();
		setConfig(this.config);
		parser = new Parser(this.input, this.game, this.config);
	}

	/**
	 * Constructs the game engine using a given game, a given configuration, a
	 * given input stream and a given output stream.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @param input
	 *            The input.
	 * @param output
	 *            The output.
	 * @throws IllegalArgumentException
	 *             The arguments 'game', 'config', 'input' and 'output' cannot
	 *             be null.
	 */
	public Engine(Game game, Properties config, InputStream input,
			OutputStream output) {
		if ((input == null) || (output == null))
			throw new IllegalArgumentException();
		this.game = game;
		this.config = config;
		setInput(input);
		setOutput(output);
		setDefaultConfiguration();
		setConfig(this.config);
		parser = new Parser(this.input, this.game, this.config);
	}

	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		msgPrompt = "> ";
		msgUnknownCommand = "Pardon?";
		msgLocationWithItems = "This location contains the following items: ";
		msgLocationWithoutItems = "This location has no items.";
		msgPlayerScore = "Player score: ";
		msgGameOver = "GAME OVER";

		flagShowEngineInfo = true;
		flagShowGameInfo = true;
		flagAutodescribeFirstLocation = true;
		flagShowLocationItems = true;
		flagShowItemsValue = true;
		flagShowItemsWeight = true;

		limitCommandHistorySize = 1;
		flagShowConnections = true;
		flagShowConnectionsState = true;
	}

	/**
	 * Sets a new configuration. Properties that are not overridden maintain
	 * their previous values.
	 * 
	 * @param config
	 *            The config.
	 * @throws IllegalArgumentException
	 *             The argument 'config' cannot be null.
	 */
	public void setConfig(Properties config) {
		if (config == null) {
			throw new IllegalArgumentException();
		}

		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(MESSAGE_PROMPT))
				msgPrompt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_UNKNOWNCOMMAND))
				msgUnknownCommand = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_LOCATIONWITHITEMS))
				msgLocationWithItems = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_LOCATIONWITHOUTITEMS))
				msgLocationWithoutItems = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_PLAYERSCORE))
				msgPlayerScore = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_GAMEOVER))
				msgGameOver = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWENGINEINFO))
				flagShowEngineInfo = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWGAMEINFO))
				flagShowGameInfo = Boolean.parseBoolean(config.getProperty(obj
						.toString()));
			else if (obj.toString().equalsIgnoreCase(
					FLAG_AUTODESCRIBEFIRSTLOCATION))
				flagAutodescribeFirstLocation = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWLOCATIONITEMS))
				flagShowLocationItems = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSVALUE))
				flagShowItemsValue = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSWEIGHT))
				flagShowItemsWeight = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(LIMIT_COMMANDHISTORYSIZE))
				limitCommandHistorySize = Integer.parseInt(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWCONNECTIONS))
				flagShowConnections = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWCONNECTIONSSTATE))
				flagShowConnectionsState = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
		}
	}

	/**
	 * Sets the input stream for the game engine.
	 * 
	 * @param input
	 *            The input.
	 * @throws IllegalArgumentException
	 *             The argument 'input' cannot be null.
	 */
	public void setInput(InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		this.input = input;
	}

	/**
	 * Sets the output stream for the game engine.
	 * 
	 * @param output
	 *            The output.
	 * @throws IllegalArgumentException
	 *             The argument 'output' cannot be null.
	 */
	public void setOutput(OutputStream output) {
		if (output == null) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		ps = new PrintStream(output);
	}

	/**
	 * Print the game events in the output
	 */
	private void printEvents() {
		if (flagShowItemsValue) {
			ps.println(LINE_SEPARATOR + msgPlayerScore
					+ game.reportInventoryValue() + LINE_SEPARATOR
					+ msgGameOver);
		} else {
			ps.println(msgGameOver);
		}
	}

	/**
	 * Runs the main loop of the game execution. Firstly (before entering into
	 * the loop) the description of the engine and the description of the game
	 * can be shown (depending on the configuration). Then the description of
	 * the initial location or the description plus the items that are in that
	 * location can be shown (depending on the configuration). Finally three
	 * steps are repeated until the game ends:
	 * <ol type=”1” start=”1”>
	 * <li>Parsing the input (i.e. identifying a valid next command),</li>
	 * <li>Trying to execute the next command (recording it if it was executed
	 * successfully) and</li>
	 * <li>Reporting the result of the command (and the events that have ocurred
	 * in the game after the command execution) to the output stream.</li>
	 * </ol>
	 * At the end of the game the player score can be shown (depending on the
	 * configuration).
	 */
	public void run() {
		Command command = null;
		boolean executed = false;
		game.clearExecutedCommands();
		game.clearEvents();

		if (flagShowEngineInfo) {
			ps.println(engineInfo);
			ps.println();
		}

		if (flagShowGameInfo) {
			ps.println(game.reportInformation());
			ps.println();
		}

		if (flagAutodescribeFirstLocation) {
			ps.println(game.reportLocationName());
			ps.println(game.reportLocationDescription());
			ps.println();
		}

		if ((game.reportAllLocationItems().isEmpty())
				&& (flagShowLocationItems)) {
			ps.println(msgLocationWithoutItems);
			ps.println();
		} else if ((!game.reportAllLocationItems().isEmpty())
				&& (flagShowLocationItems)) {
			ps.println(msgLocationWithItems
					+ game.setToString(game.reportAllLocationItems(),
							flagShowItemsValue, flagShowItemsWeight));
		}

		if (flagShowConnections) {
			ps.println(game.getCurrentLocation().showConnections(
					flagShowConnectionsState));
		}

		while (!game.isEnded()) {
			ps.println(msgPrompt);
			command = parser.parseNextCommand();

			if (command != null) {
				executed = command.execute();
				if (executed) {
					game.commandCount(command);
					if (game.getNumberOfExecutedCommands() < limitCommandHistorySize) {
						game.addExecutedCommand(command);
					} else {
						game.removeOldestExecutedCommand();
						game.addExecutedCommand(command);
					}
				}
				if (command.hasResult())
					ps.println(command.getResult());
				if (game.hasEvents()) {
					ps.println(game.reportEvents());
					printEvents();
				}
			} else
				ps.println(msgUnknownCommand + LINE_SEPARATOR);
		}
	}
}
