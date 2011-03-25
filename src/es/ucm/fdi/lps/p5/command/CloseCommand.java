package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.Game.Direction;
import es.ucm.fdi.lps.p5.exception.UnexecutedCommandException;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

public class CloseCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The CloseCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.closeCommand" and its default value
	 * is "close".</li>
	 * </ul>
	 */
	private final String KEYWORD_CLOSECOMMAND = "keyword.closeCommand";
	private String keywordCloseCommand;
	
	/**
	 * The CloseCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.closeCommand.abbrev" and its default
	 * value is "c".</li>
	 * </ul>
	 */
	private final String KEYWORD_CLOSECOMMAND_ABBREV = "keyword.closeCommand.abbrev";
	private String keywordCloseCommandAbbrev;

	/**
	 * The CloseCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.closeCommand.alt" and its default
	 * value is "lock".</li>
	 * </ul>
	 */
	private final String KEYWORD_CLOSECOMMAND_ALT = "keyword.closeCommand.alt";
	private String keywordCloseCommandAlt;
	
	/**
	 * The "with" clause in OpenCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.withCommand" and its default value
	 * is "with".</li>
	 * </ul>
	 */
	private final String KEYWORD_WITHCOMMAND = "keyword.withCommand";
	private String keywordWithCommand;

	/**
	 * The CloseCommand help message
	 * <ul>
	 * <li>This property is called "message.closeCommand.help" and its default
	 * value is "(close|c|lock) <dir> [WITH <objectName>]".</li>
	 * </ul>
	 */
	private final String MESSAGE_CLOSECOMMAND_HELP = "message.closeCommand.help";
	private String msgCloseCommandHelp;

	/**
	 * The CloseCommand success message
	 * <ul>
	 * <li>This property is called "message.closeCommand.success" and its
	 * default value is "Obstacle Locked.".</li>
	 * </ul>
	 */
	private final String MESSAGE_CLOSECOMMAND_SUCCESS = "message.closeCommand.success";
	private String msgCloseCommandSuccess;

	/**
	 * The CloseCommand failure message
	 * <ul>
	 * <li>This property is called "message.closeCommand.failure" and its
	 * default value is "Lock Fail".</li>
	 * </ul>
	 */
	private final String MESSAGE_CLOSECOMMAND_FAILURE = "message.closeCommand.failure";
	private String msgCloseCommandFailure;

	/**
	 * The CloseCommand undo success message
	 * <ul>
	 * <li>This property is called "message.closeCommand.undoSuccess" and its
	 * default value is "Obstacle Unlocked.".</li>
	 * </ul>
	 */
	private final String MESSAGE_CLOSECOMMAND_UNDOSUCCESS = "message.closeCommand.undoSuccess";
	private String msgCloseCommandUndoSuccess;

	/**
	 * The DropCommand undo failure message
	 * <ul>
	 * <li>This property is called "message.dropCommand.undoFailure" and its
	 * default value is "The obstacle cannot be unlocked".</li>
	 * </ul>
	 */
	private final String MESSAGE_CLOSECOMMAND_UNDOFAILURE = "message.closeCommand.undoFailure";
	private String msgCloseCommandUndoFailure;

	/**
	 * The item name that will be used to close an obstacle
	 */
	private String itemName;
	
	/**
	 * The direction where we want to close the obstacle
	 */
	private Direction dir;

	/**
	 * Flag to show if it is a With clause command
	 */
	private boolean withClause;

	/**
	 * Constructs a Close command (as an specific type of Command). Initially
	 * the command is unparsed and unexecuted. Default configuration is assumed
	 * at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public CloseCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Close command (as an specific type of Command). Initially
	 * the command is unparsed and unexecuted. The properties defined in the
	 * given configuration override those of the default configuration.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The arguments 'game' and 'config' cannot be null.
	 */
	public CloseCommand(Game game, Properties config) {
		this(game);
		if (config == null) {
			throw new IllegalArgumentException();
		}
		this.config = config;
		setConfiguration();
	}

	/**
	 * Sets the specific configuration given in the properties file
	 */
	private void setConfiguration() {

		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(KEYWORD_CLOSECOMMAND))
				keywordCloseCommand = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					KEYWORD_CLOSECOMMAND_ABBREV))
				keywordCloseCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_CLOSECOMMAND_ALT))
				keywordCloseCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_CLOSECOMMAND_HELP))
				msgCloseCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_CLOSECOMMAND_SUCCESS))
				msgCloseCommandSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_CLOSECOMMAND_FAILURE))
				msgCloseCommandFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_CLOSECOMMAND_UNDOSUCCESS))
				msgCloseCommandUndoSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_CLOSECOMMAND_UNDOFAILURE))
				msgCloseCommandUndoFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_WITHCOMMAND))
				keywordWithCommand = config.getProperty(obj.toString());
		}
	}

	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		keywordCloseCommand = "close";
		keywordCloseCommandAbbrev = "c";
		keywordCloseCommandAlt = "lock";
		keywordWithCommand = "with";

		msgCloseCommandHelp = "(close|c|lock) <dir> [WITH <objectName>]";
		msgCloseCommandSuccess = "Obstacle Locked";
		msgCloseCommandFailure = "Lock Fail";
		msgCloseCommandUndoSuccess = "Obstacle Unlocked";
		msgCloseCommandUndoFailure = "The obstacle cannot be Unlocked";

	}

	/**
	 * Executes the Close command, closing the obstacle in the given direction.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed) {
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		}
		executed = true;

		if (game.getCurrentLocation().hasConnectedLocation(dir)) {
			if (game.getCurrentLocation().hasObstacle(dir)) {
				if (!game.getCurrentLocation().getObstacleStatus(dir)) {
					if (!game.getCurrentLocation().getObstacle(dir)
							.hasBoundedItems()) {
						game.getCurrentLocation().getObstacle(dir)
								.changeStatus();
						//result = "El obstáculo ha sido desactivado"	+ LINE_SEPARATOR;
						result = msgCloseCommandSuccess + LINE_SEPARATOR;
						return true;
					} else {
						// WITH CLAUSE
						if (withClause) {
							if (!game.getItemsFromInventory(itemName).isEmpty()) {
								if (game.getCurrentLocation()
										.getObstacle(dir)
										.getBoundedItems()
										.contains(
												game.getItemsFromInventory(
														itemName).toArray()[0])) {
									game.getCurrentLocation().getObstacle(dir)
											.changeStatus();
									//result = "El obstáculo ha sido activado gracias al item: " + itemName + LINE_SEPARATOR;
									result = msgCloseCommandSuccess + LINE_SEPARATOR;
									return true;
								} else {
									//result = "El obstáculo no puede ser activado sin el item necesario" + LINE_SEPARATOR;
									result = msgCloseCommandFailure + LINE_SEPARATOR;
									return false;
								}
							} else {
								//result = "No tienes el objeto indicado en el inventario" + LINE_SEPARATOR;
								result = msgCloseCommandFailure + LINE_SEPARATOR;
								return false;
							}
						} else {
							//result = "Necesitas indicar un Item para cerrar el obstaculo" + LINE_SEPARATOR;
							result = msgCloseCommandFailure + LINE_SEPARATOR;
							return false;
						}
					}
				} else {
					//result = "El obstáculo ya se encuentra activado" + LINE_SEPARATOR;
					result = msgCloseCommandFailure + LINE_SEPARATOR;
					return false;
				}
			} else {
				//result = "No existe obstáculo en la dirección solicitada"	+ LINE_SEPARATOR;
				result = msgCloseCommandFailure + LINE_SEPARATOR;
				return false;
			}
		} else {
			//result = "No existe conexión con otra habitación en la dirección solicitada" + LINE_SEPARATOR;
			result = msgCloseCommandFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Gets the help information about this Close command: close .
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgCloseCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Close
	 * command
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if ((firstCommand.equalsIgnoreCase(keywordCloseCommand)
					|| firstCommand.equalsIgnoreCase(keywordCloseCommandAbbrev) || firstCommand
					.equalsIgnoreCase(keywordCloseCommandAlt))
					&& reader.hasNext()) {
				String secondCommand = reader.next();
				for (Direction direction : Direction.values()) {
					if (direction.getKeyword().equalsIgnoreCase(secondCommand)
							|| direction.name().equalsIgnoreCase(secondCommand)) {
						dir = Direction.valueOf(direction.name());
						parsed = true;
						break;
					}
				}
				// WITH CLAUSE
				if (reader.hasNext()) {
					if (reader.next().equalsIgnoreCase(keywordWithCommand)
							&& reader.hasNext()) {
						itemName = reader.next();
						parsed = true;
						withClause = true;
					} else
						return false;
				}
				if (parsed)
					return true;
			} else
				return false;
		} else
			return false;
		return false;
	}

	/**
	 * Undoes the Close command execution, opening the obstacle in the given
	 * direction.
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

		if (game.getCurrentLocation().hasObstacle(dir)) {
			if (game.getCurrentLocation().getObstacleStatus(dir)) {
				game.getCurrentLocation().getObstacle(dir).changeStatus();
				result = msgCloseCommandUndoSuccess + LINE_SEPARATOR;
				return true;
			} else {
				result = msgCloseCommandUndoFailure + LINE_SEPARATOR;
				return false;
			}
		} else {
			result = msgCloseCommandUndoFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Returns a String representation for this object: CloseCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[]";
	}
}