package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.Item;
import es.ucm.fdi.lps.p5.exception.UnexecutedCommandException;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents a Drop command from the player that moves an item from the player
 * inventory to the player location.
 * 
 * <ul>
 * <li>It uses a command-specific property called "keyword.dropCommand" which
 * default value is "drop".</li>
 * <li>It uses a command-specific property called "keyword.dropCommand.abbrev"
 * which default value is "d".</li>
 * <li>It uses a command-specific property called "keyword.dropCommand.alt"
 * which default value is "unpick".</li>
 * <li>It uses a command-specific property called "message.dropCommand.help"
 * which default value is "(drop|d|unpick) <item name>".</li>
 * <li>It uses a command-specific property called
 * "message.dropCommand.itemNameNotInInventory" which default value is
 * "There is no item with that name in the inventory.".</li>
 * <li>It uses a command-specific property called
 * "message.dropCommand.itemNameRepeatedInInventory" which default value is
 * "There are several items with that name in the inventory.".</li>
 * <li>It uses a command-specific property called
 * "message.dropCommand.itemNameAlreadyInLocation" which default value is
 * "There is another item with that name in this location.".</li>
 * <li>It uses a command-specific property called "message.dropCommand.success"
 * which default value is "It has been dropped.".</li>
 * <li>It uses a command-specific property called "message.dropCommand.failure"
 * which default value is "That item cannot be dropped in this location.".</li>
 * <li>It uses a command-specific property called
 * "message.dropCommand.undoSuccess" which default value is
 * "It has returned to the inventory.".</li>
 * <li>It uses a command-specific property called
 * "message.dropCommand.undoFailure" which default value is
 * "The item cannot return to the inventory from this location.".</li>
 * <li>It uses a command-specific property called
 * "flag.dropCommand.allowFIFODisambiguationForItemNameRepeatedInInventory"
 * which default value is "false".</li>
 * <li>It uses a command-specific property called
 * "flag.dropCommand.allowRepetitionsInLocationItemNames" which default value is
 * "false".</li>
 * </ul>
 */
public class DropCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * The DropCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.dropCommand" and its default value
	 * is "drop".</li>
	 * </ul>
	 */
	private final String KEYWORD_DROPCOMMAND = "keyword.dropCommand";
	private String keywordDropCommand;

	/**
	 * The DropCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.dropCommand.abbrev" and its default
	 * value is "d".</li>
	 * </ul>
	 */
	private final String KEYWORD_DROPCOMMAND_ABBREV = "keyword.dropCommand.abbrev";
	private String keywordDropCommandAbbrev;

	/**
	 * The DropCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.dropCommand.alt" and its default
	 * value is "unpick".</li>
	 * </ul>
	 */
	private final String KEYWORD_DROPCOMMAND_ALT = "keyword.dropCommand.alt";
	private String keywordDropCommandAlt;

	/**
	 * The DropCommand help message
	 * <ul>
	 * <li>This property is called "message.dropCommand.help" and its default
	 * value is "(drop|d|unpick) <item name>".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_HELP = "message.dropCommand.help";
	private String msgDropCommandHelp;

	/**
	 * The DropCommand item not in the inventory message
	 * <ul>
	 * <li>This property is called "message.dropCommand.itemNameNotInInventory"
	 * and its default value is
	 * "There is no item with that name in the inventory.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_ITEMNAMENOTININVENTORY = "message.dropCommand.itemNameNotInInventory";
	private String msgDropCommandItemNameNotInInventory;

	/**
	 * The DropCommand item name repeated in the inventory message
	 * <ul>
	 * <li>This property is called
	 * "message.dropCommand.itemNameRepeatedInInventory" and its default value
	 * is "There are several items with that name in the inventory.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_ITEMNAMEREPEATEDININVENTORY = "message.dropCommand.itemNameRepeatedInInventory";
	private String msgDropCommandItemNameRepeatedInInventory;

	/**
	 * The DropCommand item name already in location message
	 * <ul>
	 * <li>This property is called
	 * "message.dropCommand.itemNameAlreadyInLocation" and its default value is
	 * "There is another item with that name in this location.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_ITEMNAMEALREADYINLOCATION = "message.dropCommand.itemNameAlreadyInLocation";
	private String msgDropCommandItemNameAlreadyInLocation;

	/**
	 * The DropCommand success message
	 * <ul>
	 * <li>This property is called "message.dropCommand.success" and its default
	 * value is "It has been dropped.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_SUCCESS = "message.dropCommand.success";
	private String msgDropCommandSuccess;

	/**
	 * The DropCommand failure message
	 * <ul>
	 * <li>This property is called "message.dropCommand.failure" and its default
	 * value is "That item cannot be dropped in this location.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_FAILURE = "message.dropCommand.failure";
	@SuppressWarnings("unused")
	private String msgDropCommandFailure;

	/**
	 * The DropCommand undo success message
	 * <ul>
	 * <li>This property is called "message.dropCommand.undoSuccess" and its
	 * default value is "It has returned to the inventory.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_UNDOSUCCESS = "message.dropCommand.undoSuccess";
	private String msgDropCommandUndoSuccess;

	/**
	 * The DropCommand undo failure message
	 * <ul>
	 * <li>This property is called "message.dropCommand.undoFailure" and its
	 * default value is
	 * "The item cannot return to the inventory from this location.".</li>
	 * </ul>
	 */
	private final String MESSAGE_DROPCOMMAND_UNDOFAILURE = "message.dropCommand.undoFailure";
	private String msgDropCommandUndoFailure;

	/**
	 * The DropCommand FIFO flag
	 * <ul>
	 * <li>This property is called
	 * "flag.dropCommand.allowFIFODisambiguationForItemNameRepeatedInInventory"
	 * and its default value is "false".</li>
	 * </ul>
	 */
	private final String FLAG_DROPCOMMAND_FIFO = "flag.dropCommand.allowFIFODisambiguationForItemNameRepeatedInInventory";
	private boolean flagDropCommandFIFO;

	/**
	 * The DropCommand allow repetition flag
	 * <ul>
	 * <li>This property is called
	 * "flag.dropCommand.allowRepetitionsInLocationItemNames" and its default
	 * value is "false".</li>
	 * </ul>
	 */
	private final String FLAG_DROPCOMMAND_REPETITIONS = "flag.dropCommand.allowRepetitionsInLocationItemNames";
	private boolean flagDropCommandRepetitions;

	/**
	 * The name of the item to be dropped
	 */
	private String itemName;
	
	/**
	 * The item to be dropped
	 */
	private Item firstItem;

	/**
	 * Constructs a Drop command (as an specific type of Command) that has
	 * access to a given game. Initially the command is unparsed and unexecuted.
	 * Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public DropCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Drop command (as an specific type of Command) that has
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
	public DropCommand(Game game, Properties config) {
		this(game);
		if (config == null) {
			throw new IllegalArgumentException();
		}
		this.config = config;
		setConfiguration();
	}

	/**
	 * Parses the configuration from the configuration file
	 */
	private void setConfiguration() {

		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(KEYWORD_DROPCOMMAND))
				keywordDropCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_DROPCOMMAND_ABBREV))
				keywordDropCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_DROPCOMMAND_ALT))
				keywordDropCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_DROPCOMMAND_HELP))
				msgDropCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_ITEMNAMENOTININVENTORY))
				msgDropCommandItemNameNotInInventory = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_ITEMNAMEREPEATEDININVENTORY))
				msgDropCommandItemNameRepeatedInInventory = config
						.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_ITEMNAMEALREADYINLOCATION))
				msgDropCommandItemNameAlreadyInLocation = config
						.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_SUCCESS))
				msgDropCommandSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_FAILURE))
				msgDropCommandFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_UNDOSUCCESS))
				msgDropCommandUndoSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_DROPCOMMAND_UNDOFAILURE))
				msgDropCommandUndoFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(FLAG_DROPCOMMAND_FIFO))
				flagDropCommandFIFO = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(
					FLAG_DROPCOMMAND_REPETITIONS))
				flagDropCommandRepetitions = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
		}
	}
	
	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		keywordDropCommand = "drop";
		keywordDropCommandAbbrev = "d";
		keywordDropCommandAlt = "unpick";

		msgDropCommandHelp = "(drop|d|unpick) <item name>";
		msgDropCommandItemNameNotInInventory = "There is no item with that name in the inventory.";
		msgDropCommandItemNameRepeatedInInventory = "There are several items with that name in the inventory.";
		msgDropCommandItemNameAlreadyInLocation = "There is another item with that name in this location.";
		msgDropCommandSuccess = "It has been dropped.";
		msgDropCommandFailure = "That item cannot be dropped in this location.";
		msgDropCommandUndoSuccess = "It has returned to the inventory.";
		msgDropCommandUndoFailure = "The item cannot return to the inventory from this location.";

		flagDropCommandFIFO = false;
		flagDropCommandRepetitions = false;
	}

	/**
	 * Executes the Drop command, moving the item from the player inventory to
	 * the player location, if it is possible. When the item name is repeated in
	 * the player inventory, this command may fail or allow a FIFO
	 * disambiguation (chosing the first item with that name that was added to
	 * the inventory), depending on the configuration. When there is already an
	 * item with that name in the location, this command may fail or succeed
	 * (dropping the item anyway), depending on the configuration.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed) {
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		}
		executed = true;
		Set<Item> itemsFoundedinInventory = new LinkedHashSet<Item>();
		Set<Item> itemsFoundedinLocation = new LinkedHashSet<Item>();

		// False = If there is no item in the inventory
		if (game.reportAllInventoryItems().isEmpty()) {
			result = msgDropCommandItemNameNotInInventory + LINE_SEPARATOR;
			return false;
		}

		itemsFoundedinInventory = game.getItemsFromInventory(itemName);

		// False = If the user don´t have the item in his inventory
		if (itemsFoundedinInventory.isEmpty()) {
			result = msgDropCommandItemNameNotInInventory + LINE_SEPARATOR;
			return false;
		}

		// False = More than one item with the same name
		if ((itemsFoundedinInventory.size() > 1) && (!flagDropCommandFIFO)) {
			result = msgDropCommandItemNameRepeatedInInventory + LINE_SEPARATOR;
			return false;
		}

		// I take the first item with that name
		firstItem = (Item) itemsFoundedinInventory.toArray()[0];

		itemsFoundedinLocation = game.getItemsFromLocation(itemName);

		// False = The element already exists in that location
		if (itemsFoundedinLocation.size() >= 1 && !flagDropCommandRepetitions) {
			result = msgDropCommandItemNameAlreadyInLocation + LINE_SEPARATOR;
			return false;
		}
		
		// True = No error found
		game.moveItemFromInventoryToLocation(firstItem);
		result = msgDropCommandSuccess + LINE_SEPARATOR;
		return true;
	}

	/**
	 * Gets the help information about this Drop command: drop .
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgDropCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Drop
	 * command (e.g. "drop sword").
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if ((firstCommand.equalsIgnoreCase(keywordDropCommand)
					|| firstCommand.equalsIgnoreCase(keywordDropCommandAbbrev) || firstCommand
					.equalsIgnoreCase(keywordDropCommandAlt))
					&& reader.hasNext()) {
				itemName = reader.next();
				parsed = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * Undoes the Drop command execution, moving the item back from the player
	 * location to the player inventory, if it is possible.
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

		if (game.moveItemFromLocationToInventory(firstItem)) {
			result = msgDropCommandUndoSuccess + LINE_SEPARATOR;
			return true;
		} else {
			result = msgDropCommandUndoFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Returns a String representation for this object: DropCommand[item name].
	 * This is useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[]";
	}
}