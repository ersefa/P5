package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents a Look command from the player that shows the description of the
 * player location.
 * 
 * <ul>
 * <li>It uses a command-specific property called "keyword.lookCommand" which
 * default value is "look".</li>
 * <li>It uses a command-specific property called "keyword.lookCommand.abbrev"
 * which default value is "l".</li>
 * <li>It uses a command-specific property called "keyword.lookCommand.alt"
 * which default value is "search".</li>
 * <li>It uses a command-specific property called "message.lookCommand.help"
 * which default value is "look|l|search".</li>
 * </ul>
 */
public class LookCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Platform-independent line separator
	 */
	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The LookCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.lookCommand" and its default value
	 * is "look".</li>
	 * </ul>
	 */
	private final String KEYWORD_LOOKCOMMAND = "keyword.lookCommand";
	private String keywordLookCommand;

	/**
	 * The LookCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.lookCommand.abbrev" and its default
	 * value is "l".</li>
	 * </ul>
	 */
	private final String KEYWORD_LOOKCOMMAND_ABBREV = "keyword.lookCommand.abbrev";
	private String keywordLookCommandAbbrev;

	/**
	 * The LookCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.lookCommand.alt" and its default
	 * value is "search".</li>
	 * </ul>
	 */
	private final String KEYWORD_LOOKCOMMAND_ALT = "keyword.lookCommand.alt";
	private String keywordLookCommandAlt;

	/**
	 * The LookCommand help message
	 * <ul>
	 * <li>This property is called "message.lookCommand.help" and its default
	 * value is "look|l|search".</li>
	 * </ul>
	 */
	private final String MESSAGE_LOOKCOMMAND_HELP = "message.lookCommand.help";
	private String msgLookCommandHelp;

	/**
	 * Flag for showing the items of each location (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showLocationItems" and its default
	 * value is "true".</li>
	 * </ul>
	 */
	private final String FLAG_TAKECOMMAND_SHOWLOCATIONITEMS = "flag.showLocationItems";
	private boolean flagLookCommandShowLocationItems;

	/**
	 * Message of location with items (the name of the property).
	 * <ul>
	 * <li>This property is called "message.locationWithItems" and its default
	 * value is "This location contains the following items: ".</li>
	 * </ul>
	 */
	private final String MESSAGE_TAKECOMMAND_LOCATIONWITHITEMS = "message.locationWithItems";
	private String msgLookCommandLocationWithItems;

	/**
	 * Message of location without items (the name of the property).
	 * <ul>
	 * <li>This property is called "message.locationWithoutItems" and its
	 * default value is "This location has no items.".</li>
	 * </ul>
	 */
	private final String MESSAGE_TAKECOMMAND_LOCATIONWITHOUTITEMS = "message.locationWithoutItems";
	private String msgLookCommandLocationWithoutItems;

	/**
	 * Flag for showing the numerical value of the game items (the name of the
	 * property).
	 * <ul>
	 * <li>This property is called "flag.showItemValues" and its default value
	 * is "true".</li>
	 * </ul>
	 */
	private final String FLAG_SHOWITEMSVALUES = "flag.showItemsValues";
	private boolean flagShowItemsValues;

	/**
	 * Flag for showing the numerical weight of the game items (the name of the
	 * property).
	 * <ul>
	 * <li>This property is called "flag.showItemWeight" and its default value
	 * is "true".</li>
	 * </ul>
	 */
	private final String FLAG_SHOWITEMSWEIGHT = "flag.showItemsWeight";
	private boolean flagShowItemsWeight;
	
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
	 * Constructs a Look command (as an specific type of Command) that has
	 * access to a given game. Initially the command is unparsed and unexecuted.
	 * Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public LookCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Look command (as an specific type of Command) that has
	 * access to a given game. Initially the command is unparsed and unexecuted.
	 * The properties defined in the given configuration override those of the
	 * default configuration.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The arguments 'game' and 'config' cannot be null.
	 */
	public LookCommand(Game game, Properties config) {
		this(game);
		if (config == null) {
			throw new IllegalArgumentException();
		}
		this.config = config;
		setConfiguration();
	}

	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		keywordLookCommand = "look";
		keywordLookCommandAbbrev = "l";
		keywordLookCommandAlt = "search";

		msgLookCommandHelp = "look|l|search";

		msgLookCommandLocationWithItems = "This location contains the following items: ";
		msgLookCommandLocationWithoutItems = "This location has no items.";
		flagLookCommandShowLocationItems = true;
		flagShowItemsValues = true;
		flagShowItemsWeight = true;
		flagShowConnections = true;
		flagShowConnectionsState = true;
	}

	/**
	 * Parses the configuration from the configuration file
	 */
	private void setConfiguration() {
		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(KEYWORD_LOOKCOMMAND))
				keywordLookCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_LOOKCOMMAND_ABBREV))
				keywordLookCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_LOOKCOMMAND_ALT))
				keywordLookCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_LOOKCOMMAND_HELP))
				msgLookCommandHelp = config.getProperty(obj.toString());

			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_TAKECOMMAND_LOCATIONWITHITEMS))
				msgLookCommandLocationWithItems = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_TAKECOMMAND_LOCATIONWITHOUTITEMS))
				msgLookCommandLocationWithoutItems = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					FLAG_TAKECOMMAND_SHOWLOCATIONITEMS))
				flagLookCommandShowLocationItems = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSVALUES))
				flagShowItemsValues = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSWEIGHT))
				flagShowItemsWeight = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSWEIGHT))
				flagShowItemsWeight = Boolean.parseBoolean(config
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
	 * Executes the Look command, obtaining as a result the description of the
	 * player location. This command may show only the description of the player
	 * location or the description plus the items that are in that location,
	 * depending on the configuration.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		executed = true;
		if (game.reportLocationDescription() != null) {
			result = lookOk();
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Gets the help information about this Look command.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgLookCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Look
	 * command (e.g. "look").
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if (firstCommand.equalsIgnoreCase(keywordLookCommand)
					|| firstCommand.equalsIgnoreCase(keywordLookCommandAbbrev)
					|| firstCommand.equalsIgnoreCase(keywordLookCommandAlt)) {
				parsed = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a String representation for this object: LookCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * String with the result of a well executed look
	 * @return the string with the result
	 */
	private String lookOk() {
		String temp = "";
		if (flagLookCommandShowLocationItems) {
			temp = game.reportLocationName() + LINE_SEPARATOR
					+ game.reportLocationDescription() + LINE_SEPARATOR;

			if (game.reportAllLocationItems().isEmpty()) {
				temp += LINE_SEPARATOR + msgLookCommandLocationWithoutItems
						+ LINE_SEPARATOR;
			} else {
				temp += LINE_SEPARATOR
						+ msgLookCommandLocationWithItems
						+ game.setToString(game.reportAllLocationItems(),
								flagShowItemsValues, flagShowItemsWeight)
						+ LINE_SEPARATOR;
			}

			if (flagShowConnections) {
				temp += game.getCurrentLocation().showConnections(
						flagShowConnectionsState);
			}
		}
		return temp;
	}
}
