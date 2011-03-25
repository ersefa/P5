package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.Game.Direction;
import es.ucm.fdi.lps.p5.exception.UnexecutedCommandException;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents a Go command from the player that moves the player in a given
 * direction.
 * 
 * <ul>
 * <li>It uses a command-specific property called "keyword.goCommand" which
 * default value is "go".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.abbrev"
 * which default value is "g".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.alt" which
 * default value is "move".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.north"
 * which default value is "north".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.north.abbrev" which default value is "n".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.northeast"
 * which default value is "northeast".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.northeast.abbrev" which default value is "ne".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.east" which
 * default value is "east".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.east.abbrev" which default value is "e".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.southeast"
 * which default value is "southeast".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.southeast.abbrev" which default value is "se".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.south"
 * which default value is "south".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.south.abbrev" which default value is "s".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.southwest"
 * which default value is "southwest".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.southwest.abbrev" which default value is "sw".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.west" which
 * default value is "west".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.west.abbrev" which default value is "w".</li>
 * <li>It uses a command-specific property called "keyword.goCommand.northwest"
 * which default value is "northwest".</li>
 * <li>It uses a command-specific property called
 * "keyword.goCommand.northwest.abbrev" which default value is "nw".</li>
 * <li>It uses a command-specific property called "message.goCommand.help" which
 * default value is "(go|g|move) <direction>".</li>
 * <li>It uses a command-specific property called "message.goCommand.failure"
 * which default value is "There is no way in that direction.".</li>
 * <li>It uses a command-specific property called
 * "message.goCommand.undoFailure" which default value is
 * "There is no return to the previous location.".</li>
 * </ul>
 */
public class GoCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * The GoCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand" and its default value is
	 * "go".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND = "keyword.goCommand";
	private String keywordGoCommand;

	/**
	 * The GoCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.abbrev" and its default
	 * value is "g".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_ABBREV = "keyword.goCommand.abbrev";
	private String keywordGoCommandAbbrev;

	/**
	 * The GoCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.goCommand.alt" and its default value
	 * is "move".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_ALT = "keyword.goCommand.alt";
	private String keywordGoCommandAlt;

	/**
	 * The GoCommand north keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.north" and its default
	 * value is "north".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_NORTH = "keyword.goCommand.north";
	private String keywordGoCommandNorth;

	/**
	 * The GoCommand north keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.north.abbrev" and its
	 * default value is "n".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_NORTH_ABBREV = "keyword.goCommand.north.abbrev";
	private String keywordGoCommandNorthAbbrev;

	/**
	 * The GoCommand northeast keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.northeast" and its default
	 * value is "northeast".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_NORTHEAST = "keyword.goCommand.northeast";
	private String keywordGoCommandNorthEast;

	/**
	 * The GoCommand northeast keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.northeast.abbrev" and its
	 * default value is "ne".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_NORTHEAST_ABBREV = "keyword.goCommand.northeast.abbrev";
	private String keywordGoCommandNorthEastAbbrev;

	/**
	 * The GoCommand east keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.east" and its default
	 * value is "east".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_EAST = "keyword.goCommand.east";
	private String keywordGoCommandEast;

	/**
	 * The GoCommand east keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.east.abbrev" and its
	 * default value is "e".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_EAST_ABBREV = "keyword.goCommand.east.abbrev";
	private String keywordGoCommandEastAbbrev;

	/**
	 * The GoCommand southeast keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.southeast" and its default
	 * value is "southeast".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_SOUTHEAST = "keyword.goCommand.southeast";
	private String keywordGoCommandSouthEast;

	/**
	 * The GoCommand southeast keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.southeast.abbrev" and its
	 * default value is "ne".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_SOUTHEAST_ABBREV = "keyword.goCommand.southeast.abbrev";
	private String keywordGoCommandSouthEastAbbrev;

	/**
	 * The GoCommand south keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.south" and its default
	 * value is "south".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_SOUTH = "keyword.goCommand.south";
	private String keywordGoCommandSouth;

	/**
	 * The GoCommand south keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.south.abbrev" and its
	 * default value is "ne".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_SOUTH_ABBREV = "keyword.goCommand.south.abbrev";
	private String keywordGoCommandSouthAbbrev;

	/**
	 * The GoCommand southwest keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.southwest" and its default
	 * value is "southwest".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_SOUTHWEST = "keyword.goCommand.southwest";
	private String keywordGoCommandSouthWest;

	/**
	 * The GoCommand southwest keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.southwest.abbrev" and its
	 * default value is "ne".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_SOUTHWEST_ABBREV = "keyword.goCommand.southwest.abbrev";
	private String keywordGoCommandSouthWestAbbrev;

	/**
	 * The GoCommand west keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.west" and its default
	 * value is "west".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_WEST = "keyword.goCommand.west";
	private String keywordGoCommandWest;

	/**
	 * The GoCommand west keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.west.abbrev" and its
	 * default value is "w".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_WEST_ABBREV = "keyword.goCommand.west.abbrev";
	private String keywordGoCommandWestAbbrev;

	/**
	 * The GoCommand northwest keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.northwest" and its default
	 * value is "northwest".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_NORTHWEST = "keyword.goCommand.northwest";
	private String keywordGoCommandNorthWest;

	/**
	 * The GoCommand northwest keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.northwest.abbrev" and its
	 * default value is "nw".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_NORTHWEST_ABBREV = "keyword.goCommand.northwest.abbrev";
	private String keywordGoCommandNorthWestAbbrev;

	/**
	 * The GoCommand up keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.up" and its default value
	 * is "up".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_UP = "keyword.goCommand.up";
	private String keywordGoCommandUp;

	/**
	 * The GoCommand up keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.up.abbrev" and its default
	 * value is "u".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_UP_ABBREV = "keyword.goCommand.up.abbrev";
	private String keywordGoCommandUpAbbrev;

	/**
	 * The GoCommand down keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.down" and its default
	 * value is "down".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_DOWN = "keyword.goCommand.down";
	private String keywordGoCommandDown;

	/**
	 * The GoCommand down keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.down.abbrev" and its
	 * default value is "d".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_DOWN_ABBREV = "keyword.goCommand.down.abbrev";
	private String keywordGoCommandDownAbbrev;

	/**
	 * The GoCommand in keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.in" and its default value
	 * is "in".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_IN = "keyword.goCommand.in";
	private String keywordGoCommandIn;

	/**
	 * The GoCommand in keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.in.abbrev" and its default
	 * value is "i".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_IN_ABBREV = "keyword.goCommand.in.abbrev";
	private String keywordGoCommandInAbbrev;

	/**
	 * The GoCommand out keyword
	 * <ul>
	 * <li>This property is called "keyword.goCommand.out" and its default value
	 * is "out".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_OUT = "keyword.goCommand.out";
	private String keywordGoCommandOut;

	/**
	 * The GoCommand out keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.goCommand.out.abbrev" and its
	 * default value is "o".</li>
	 * </ul>
	 */
	private final String KEYWORD_GOCOMMAND_OUT_ABBREV = "keyword.goCommand.out.abbrev";
	private String keywordGoCommandOutAbbrev;

	/**
	 * Flag for showing the items of each location (the name of the property).
	 * <ul>
	 * <li>This property is called "flag.showLocationItems" and its default
	 * value is "true".</li>
	 * </ul>
	 */
	private final String FLAG_GOCOMMAND_SHOWLOCATIONITEMS = "flag.showLocationItems";
	private boolean flagGoCommandShowLocationItems;

	/**
	 * Message of location with items (the name of the property).
	 * <ul>
	 * <li>This property is called "message.locationWithItems" and its default
	 * value is "This location contains the following items: ".</li>
	 * </ul>
	 */
	private final String MESSAGE_GOCOMMAND_LOCATIONWITHITEMS = "message.locationWithItems";
	private String msgGoCommandLocationWithItems;

	/**
	 * Message of location without items (the name of the property).
	 * <ul>
	 * <li>This property is called "message.locationWithoutItems" and its
	 * default value is "This location has no items.".</li>
	 * </ul>
	 */
	private final String MESSAGE_GOCOMMAND_LOCATIONWITHOUTITEMS = "message.locationWithoutItems";
	private String msgGoCommandLocationWithoutItems;

	/**
	 * The GoCommand help message
	 * <ul>
	 * <li>This property is called "message.goCommand.help" and its default
	 * value is "(go|g|move) <direction>".</li>
	 * </ul>
	 */
	private final String MESSAGE_GOCOMMAND_HELP = "message.goCommand.help";
	private String msgGoCommandHelp;

	/**
	 * The GoCommand failure message
	 * <ul>
	 * <li>This property is called "message.goCommand.failure" and its default
	 * value is "There is no way in that direction.".</li>
	 * </ul>
	 */
	private final String MESSAGE_GOCOMMAND_FAILURE = "message.goCommand.failure";
	private String msgGoCommandFailure;

	/**
	 * The GoCommand undo failure message
	 * <ul>
	 * <li>This property is called "message.goCommand.undoFailure" and its
	 * default value is "There is no return to the previous location.".</li>
	 * </ul>
	 */
	private final String MESSAGE_GOCOMMAND_UNDOFAILURE = "message.goCommand.undoFailure";
	private String msgGoCommandUndoFailure;

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
	 * The movement direction
	 */
	private Direction dir;

	/**
	 * Constructs a Go command (as an specific type of Command) that has access
	 * to a given game. Initially the command is unparsed and unexecuted.
	 * Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public GoCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Go command (as an specific type of Command) that has access
	 * to a given game. Initially the command is unparsed and unexecuted. The
	 * properties defined in the given configuration override those of the
	 * default configuration.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public GoCommand(Game game, Properties config) {
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
		keywordGoCommand = "go";
		keywordGoCommandAbbrev = "g";
		keywordGoCommandAlt = "move";

		keywordGoCommandNorth = "north";
		keywordGoCommandNorthAbbrev = "n";
		keywordGoCommandNorthEast = "northeast";
		keywordGoCommandNorthEastAbbrev = "ne";
		keywordGoCommandEast = "east";
		keywordGoCommandEastAbbrev = "e";
		keywordGoCommandSouthEast = "southeast";
		keywordGoCommandSouthEastAbbrev = "se";
		keywordGoCommandSouth = "south";
		keywordGoCommandSouthAbbrev = "s";
		keywordGoCommandSouthWest = "southwest";
		keywordGoCommandSouthWestAbbrev = "sw";
		keywordGoCommandWest = "west";
		keywordGoCommandWestAbbrev = "w";
		keywordGoCommandNorthWest = "northwest";
		keywordGoCommandNorthWestAbbrev = "nw";
		keywordGoCommandUp = "up";
		keywordGoCommandUpAbbrev = "u";
		keywordGoCommandDown = "down";
		keywordGoCommandDownAbbrev = "d";
		keywordGoCommandIn = "in";
		keywordGoCommandInAbbrev = "i";
		keywordGoCommandOut = "out";
		keywordGoCommandOutAbbrev = "o";

		msgGoCommandHelp = "(go|g|move) <direction>";
		msgGoCommandFailure = "There is no way in that direction.";
		msgGoCommandUndoFailure = "There is no return to the previous location.";

		msgGoCommandLocationWithItems = "This location contains the following items: ";
		msgGoCommandLocationWithoutItems = "This location has no items.";
		flagGoCommandShowLocationItems = true;
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
			if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND))
				keywordGoCommand = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_ABBREV))
				keywordGoCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_ALT))
				keywordGoCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_NORTH))
				keywordGoCommandNorth = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_NORTH_ABBREV))
				keywordGoCommandNorthAbbrev = config
						.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_NORTHEAST))
				keywordGoCommandNorthEast = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_NORTHEAST_ABBREV))
				keywordGoCommandNorthEastAbbrev = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_EAST))
				keywordGoCommandEast = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_EAST_ABBREV))
				keywordGoCommandNorthEastAbbrev = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_SOUTHEAST))
				keywordGoCommandSouthEast = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_SOUTHEAST_ABBREV))
				keywordGoCommandSouthEastAbbrev = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_SOUTH))
				keywordGoCommandSouth = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_SOUTH_ABBREV))
				keywordGoCommandSouthAbbrev = config
						.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_SOUTHWEST))
				keywordGoCommandSouthWest = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_SOUTHWEST_ABBREV))
				keywordGoCommandSouthWestAbbrev = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_WEST))
				keywordGoCommandWest = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_WEST_ABBREV))
				keywordGoCommandWestAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_NORTHWEST))
				keywordGoCommandNorthWest = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_NORTHWEST_ABBREV))
				keywordGoCommandNorthWestAbbrev = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_UP))
				keywordGoCommandUp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_UP_ABBREV))
				keywordGoCommandUpAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_DOWN))
				keywordGoCommandDown = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_DOWN_ABBREV))
				keywordGoCommandDownAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_IN))
				keywordGoCommandIn = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_IN_ABBREV))
				keywordGoCommandInAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_GOCOMMAND_OUT))
				keywordGoCommandOut = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_GOCOMMAND_OUT_ABBREV))
				keywordGoCommandOutAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_GOCOMMAND_HELP))
				msgGoCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_GOCOMMAND_FAILURE))
				msgGoCommandFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_GOCOMMAND_UNDOFAILURE))
				msgGoCommandUndoFailure = config.getProperty(obj.toString());

			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_GOCOMMAND_LOCATIONWITHITEMS))
				msgGoCommandLocationWithItems = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_GOCOMMAND_LOCATIONWITHOUTITEMS))
				msgGoCommandLocationWithoutItems = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					FLAG_GOCOMMAND_SHOWLOCATIONITEMS))
				flagGoCommandShowLocationItems = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSVALUES))
				flagShowItemsValues = Boolean.parseBoolean(config
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
	 * Executes the Go command, moving the player in a given direction, if it is
	 * possible.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		executed = true;

		if (game.hasConnectedLocation(dir)) {
			if (!game.hasObstacle(dir)) {
				if (game.movePlayer(dir)) {
					result = movementOk();
					return true;
				} else {
					return false;
				}
			} else {
				result = game.getObstacle(dir).getErrorMsg() + LINE_SEPARATOR;
				return false;
			}
		} else {
			result = msgGoCommandFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Gets the help information about this Go command.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgGoCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Go
	 * command (e.g. "go north").
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if ((firstCommand.equalsIgnoreCase(keywordGoCommand)
					|| firstCommand.equalsIgnoreCase(keywordGoCommandAbbrev) || firstCommand
					.equalsIgnoreCase(keywordGoCommandAlt))
					&& (reader.hasNext())) {
				String secondCommand = reader.next();
				if (secondCommand.equalsIgnoreCase(keywordGoCommandNorth)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandNorthAbbrev)) {
					dir = Direction.NORTH;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandNorthEast)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandNorthEastAbbrev)) {
					dir = Direction.NORTHEAST;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandEast)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandEastAbbrev)) {
					dir = Direction.EAST;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandSouthEast)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandSouthEastAbbrev)) {
					dir = Direction.SOUTHEAST;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandSouth)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandSouthAbbrev)) {
					dir = Direction.SOUTH;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandSouthWest)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandSouthWestAbbrev)) {
					dir = Direction.SOUTHWEST;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandWest)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandWestAbbrev)) {
					dir = Direction.WEST;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandNorthWest)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandNorthWestAbbrev)) {
					dir = Direction.NORTHWEST;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandUp)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandUpAbbrev)) {
					dir = Direction.UP;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandDown)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandDownAbbrev)) {
					dir = Direction.DOWN;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandIn)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandInAbbrev)) {
					dir = Direction.IN;
					parsed = true;
					return true;
				}
				if (secondCommand.equalsIgnoreCase(keywordGoCommandOut)
						|| secondCommand
								.equalsIgnoreCase(keywordGoCommandOutAbbrev)) {
					dir = Direction.OUT;
					parsed = true;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Undoes the Go command execution, moving the player back to the previous
	 * location. This command supposes that two locations are connected
	 * symmetrically.
	 * 
	 * @see Command#undo()
	 */
	public boolean undo() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		if (!executed)
			throw new UnexecutedCommandException(
					"Error: UnexecutedCommandException");

		if (game.hasConnectedLocation(dir.getOppositeDirection())) {
			if (game.movePlayer(dir.getOppositeDirection())) {
				result = movementOk();
				return true;
			} else {
				result = msgGoCommandUndoFailure + LINE_SEPARATOR;
				return false;
			}
		} else {
			result = msgGoCommandUndoFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Returns a String representation for this object: GoCommand[]. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[]";
	}

	/**
	 * String with the result of a well executed movement
	 * @return the String with the result
	 */
	private String movementOk() {
		String temp = "";
		if (flagGoCommandShowLocationItems) {
			temp = game.reportLocationName() + LINE_SEPARATOR
					+ game.reportLocationDescription() + LINE_SEPARATOR;

			if (game.reportAllLocationItems().isEmpty()) {
				temp += LINE_SEPARATOR + msgGoCommandLocationWithoutItems
						+ LINE_SEPARATOR;
			} else {
				temp += LINE_SEPARATOR
						+ msgGoCommandLocationWithItems
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