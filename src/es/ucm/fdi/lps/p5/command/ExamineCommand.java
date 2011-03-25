package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.Item;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents an Examine command from the player that that shows a list of the
 * items in the player inventory or shows the description of an specific item,
 * depending on the use mode.
 * 
 * <ul>
 * <li>It uses a command-specific property called "keyword.examineCommand" which
 * default value is "examine".</li>
 * <li>It uses a command-specific property called
 * "keyword.examineCommand.abbrev" which default value is "x".</li>
 * <li>
 * It uses a command-specific property called "keyword.examineCommand.alt" which
 * default value is "inventory".</li>
 * <li>It uses a command-specific property called "message.examineCommand.help"
 * which default value is "(examine|x|inventory) [item name]".</li>
 * <li>It uses a command-specific property called
 * "message.examineCommand.inventoryWithItems" which default value is
 * "The inventory contains the following items: ".</li>
 * <li>It uses a command-specific property called
 * "message.examineCommand.inventoryWithoutItems" which default value is
 * "The inventory has no items.".</li>
 * <li>It uses a command-specific property called
 * "message.examineCommand.itemNameNotInInventory" which default value is
 * "There is no item with that name in the inventory.".</li>
 * <li>It uses a command-specific property called
 * "message.examineCommand.itemNameRepeatedInInventory" which default value is
 * "There are several items with that name in the inventory.".</li>
 * </ul>
 */
public class ExamineCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Platform-independent line separator
	 */
	private final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * The ExamineCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.examineCommand" and its default
	 * value is "examine".</li>
	 * </ul>
	 */
	private final String KEYWORD_EXAMINECOMMAND = "keyword.examineCommand";
	private String keywordExamineCommand;

	/**
	 * The ExamineCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.examineCommand.abbrev" and its
	 * default value is "x".</li>
	 * </ul>
	 */
	private final String KEYWORD_EXAMINECOMMAND_ABBREV = "keyword.examineCommand.abbrev";
	private String keywordExamineCommandAbbrev;

	/**
	 * The ExamineCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.examineCommand.alt" and its default
	 * value is "inventory".</li>
	 * </ul>
	 */
	private final String KEYWORD_EXAMINECOMMAND_ALT = "keyword.examineCommand.alt";
	private String keywordExamineCommandAlt;

	/**
	 * The ExamineCommand help message
	 * <ul>
	 * <li>This property is called "message.examineCommand.help" and its default
	 * value is "(drop|d|unpick) <item name>".</li>
	 * </ul>
	 */
	private final String MESSAGE_EXAMINECOMMAND_HELP = "message.examineCommand.help";
	private String msgExamineCommandHelp;

	/**
	 * The ExamineCommand item list message
	 * <ul>
	 * <li>This property is called "message.examineCommand.inventoryWithItems"
	 * and its default value is "The inventory contains the following items: ".</li>
	 * </ul>
	 */
	private final String MESSAGE_EXAMINECOMMAND_INVENTORYWITHITEMS = "message.examineCommand.inventoryWithItems";
	private String msgExamineCommandInventoryWithItems;

	/**
	 * The ExamineCommand empty inventory message
	 * <ul>
	 * <li>This property is called
	 * "message.examineCommand.inventoryWithoutItems" and its default value is
	 * "The inventory has no items.".</li>
	 * </ul>
	 */
	private final String MESSAGE_EXAMINECOMMAND_INVENTORYWITHOUTITEMS = "message.examineCommand.inventoryWithoutItems";
	private String msgExamineCommandInventoryWithoutItems;

	/**
	 * The ExamineCommand item not in the inventory message
	 * <ul>
	 * <li>This property is called
	 * "message.examineCommand.itemNameNotInInventory" and its default value is
	 * "There is no item with that name in the inventory.".</li>
	 * </ul>
	 */
	private final String MESSAGE_EXAMINECOMMAND_ITEMNAMENOTININVENTORY = "message.examineCommand.itemNameNotInInventory";
	private String msgExamineCommandItemNameNotInInventory;

	/**
	 * The ExamineCommand item name repeated in the inventory message
	 * <ul>
	 * <li>This property is called
	 * "message.examineCommand.itemNameRepeatedInInventory" and its default
	 * value is "There are several items with that name in the inventory.".</li>
	 * </ul>
	 */
	private final String MESSAGE_EXAMINECOMMAND_ITEMNAMEREPEATEDININVENTORY = "message.examineCommand.itemNameRepeatedInInventory";
	private String msgExamineCommandItemNameRepeatedInInventory;

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
	private final String FLAG_SHOWITEMWEIGHT = "flag.showItemsWeight";
	private boolean flagShowItemsWeight;

	/**
	 * The game configuration
	 */
	private Properties config;

	/**
	 * A flag that decides if examining the whole inventory or only an item
	 */
	private boolean examineAllInventory;

	/**
	 * The item name to be examined
	 */
	private String itemName;
	
	/**
	 * The item to be examined
	 */
	private Item it;
	
	/**
	 * The item list
	 */
	Set<Item> itemList = new LinkedHashSet<Item>();

	

	/**
	 * Constructs an Examine command (as an specific type of Command) that has
	 * access to a given game. Initially the command is unparsed and unexecuted.
	 * Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public ExamineCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs an Examine command (as an specific type of Command) that has
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
	public ExamineCommand(Game game, Properties config) {
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
		keywordExamineCommand = "examine";
		keywordExamineCommandAbbrev = "x";
		keywordExamineCommandAlt = "inventory";

		msgExamineCommandHelp = "(examine|x|inventory) [<item name>]";
		msgExamineCommandInventoryWithItems = "The inventory contains the following items: ";
		msgExamineCommandInventoryWithoutItems = "The inventory has no items.";
		msgExamineCommandItemNameNotInInventory = "The inventory has no item with that name.";
		msgExamineCommandItemNameRepeatedInInventory = "The inventory has several items with that name.";

		flagShowItemsValues = true;
		flagShowItemsWeight = true;
	}
	
	/**
	 * Parses the configuration from the configuration file
	 */
	private void setConfiguration() {
		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(KEYWORD_EXAMINECOMMAND))
				keywordExamineCommand = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_EXAMINECOMMAND_ABBREV))
				keywordExamineCommandAbbrev = config
						.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_EXAMINECOMMAND_ALT))
				keywordExamineCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_EXAMINECOMMAND_HELP))
				msgExamineCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_EXAMINECOMMAND_ITEMNAMENOTININVENTORY))
				msgExamineCommandItemNameNotInInventory = config
						.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_EXAMINECOMMAND_ITEMNAMEREPEATEDININVENTORY))
				msgExamineCommandItemNameRepeatedInInventory = config
						.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_EXAMINECOMMAND_INVENTORYWITHITEMS))
				msgExamineCommandInventoryWithItems = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_EXAMINECOMMAND_INVENTORYWITHOUTITEMS))
				msgExamineCommandInventoryWithoutItems = config.getProperty(obj
						.toString());
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMSVALUES))
				flagShowItemsValues = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
			else if (obj.toString().equalsIgnoreCase(FLAG_SHOWITEMWEIGHT))
				flagShowItemsWeight = Boolean.parseBoolean(config
						.getProperty(obj.toString()));
		}
	}

	/**
	 * Executes the Examine command, obtaining as a result the list of all the
	 * items in the inventory (using the command without arguments) or the
	 * description of an specific item (using the command with the item name as
	 * argument).
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed) {
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		}
		executed = true;

		if (game.reportAllInventoryItems().isEmpty()) {
			result = msgExamineCommandInventoryWithoutItems + LINE_SEPARATOR;
			return false;
		}

		if (examineAllInventory) {
			result = msgExamineCommandInventoryWithItems
					+ game.setToString(game.reportAllInventoryItems(),
							flagShowItemsValues, flagShowItemsWeight) + LINE_SEPARATOR;
			itemList = game.reportAllInventoryItems();
			return true;
		}

		if (!examineAllInventory) {
			itemList = game.getItemsFromInventory(itemName);
			if (itemList.size() > 1) {
				result = msgExamineCommandItemNameRepeatedInInventory
						+ LINE_SEPARATOR;
				return false;
			}

			if (itemList.size() == 1) {
				it = (Item) itemList.toArray()[0];
				if (game.isItemInInventory(it)) {
					itemList = game.getItemsFromInventory(itemName);
					result = it.getDescription() + LINE_SEPARATOR;
					return true;
				} else {
					result = msgExamineCommandItemNameNotInInventory
							+ LINE_SEPARATOR;
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the help information about this Examine command.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgExamineCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Examine
	 * command (e.g. "examine").
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if (firstCommand.equalsIgnoreCase(keywordExamineCommand)
					|| firstCommand
							.equalsIgnoreCase(keywordExamineCommandAbbrev)
					|| firstCommand.equalsIgnoreCase(keywordExamineCommandAlt)) {
				parsed = true;
				examineAllInventory = true;
				if (reader.hasNext()) {
					examineAllInventory = false;
					itemName = reader.next();
				}
			} else
				return false;
		} else
			return false;
		return true;
	}

	/**
	 * Returns a String representation for this object: ExamineCommand or
	 * ExamineCommand[]. This is useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		if (itemName == null)
			return this.getClass().getSimpleName();
		else
			return this.getClass().getSimpleName() + "[" + itemName + "]";
	}
}