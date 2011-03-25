package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.Game.Direction;
import es.ucm.fdi.lps.p5.exception.UnexecutedCommandException;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

public class OpenCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The OpenCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.openCommand" and its default value
	 * is "open".</li>
	 * </ul>
	 */
	private final String KEYWORD_OPENCOMMAND = "keyword.openCommand";
	private String keywordOpenCommand;

	/**
	 * The OpenCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.openCommand.abbrev" and its default
	 * value is "o".</li>
	 * </ul>
	 */
	private final String KEYWORD_OPENCOMMAND_ABBREV = "keyword.openCommand.abbrev";
	private String keywordOpenCommandAbbrev;

	/**
	 * The OpenCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.openCommand.alt" and its default
	 * value is "unlock".</li>
	 * </ul>
	 */
	private final String KEYWORD_OPENCOMMAND_ALT = "keyword.openCommand.alt";
	private String keywordOpenCommandAlt;
	
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
	 * The OpenCommand help message
	 * <ul>
	 * <li>This property is called "message.openCommand.help" and its default
	 * value is "(open|o|unlock) <dir> [WITH <objectName>]".</li>
	 * </ul>
	 */
	private final String MESSAGE_OPENCOMMAND_HELP = "message.openCommand.help";
	private String msgOpenCommandHelp;

	/**
	 * The OpenCommand success message
	 * <ul>
	 * <li>This property is called "message.openCommand.success" and its default
	 * value is "Obstacle Unlocked.".</li>
	 * </ul>
	 */
	private final String MESSAGE_OPENCOMMAND_SUCCESS = "message.openCommand.success";
	private String msgOpenCommandSuccess;

	/**
	 * The OpenCommand failure message
	 * <ul>
	 * <li>This property is called "message.openCommand.failure" and its default
	 * value is "Unlock Fail".</li>
	 * </ul>
	 */
	private final String MESSAGE_OPENCOMMAND_FAILURE = "message.openCommand.failure";
	private String msgOpenCommandFailure;

	/**
	 * The OpenCommand undo success message
	 * <ul>
	 * <li>This property is called "message.openCommand.undoSuccess" and its
	 * default value is "Obstacle Locked.".</li>
	 * </ul>
	 */
	private final String MESSAGE_OPENCOMMAND_UNDOSUCCESS = "message.openCommand.undoSuccess";
	private String msgOpenCommandUndoSuccess;

	/**
	 * The DropCommand undo failure message
	 * <ul>
	 * <li>This property is called "message.dropCommand.undoFailure" and its
	 * default value is "The obstacle cannot be locked".</li>
	 * </ul>
	 */
	private final String MESSAGE_OPENCOMMAND_UNDOFAILURE = "message.openCommand.undoFailure";
	private String msgOpenCommandUndoFailure;

	/**
	 * The direction of the obstacle to be opened
	 */
	private Direction dir;

	/**
	 * The item name to be used for opening the obstacle
	 */
	private String itemName;

	/**
	 * Tells if it is a Open WITH command or not
	 */
	private boolean withClause;

	/**
	 * Constructs a Open command (as an specific type of Command). Initially the
	 * command is unparsed and unexecuted. Default configuration is assumed at
	 * this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public OpenCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Open command (as an specific type of Command). Initially the
	 * command is unparsed and unexecuted. The properties defined in the given
	 * configuration override those of the default configuration.
	 * 
	 * @param game
	 *            The game.
	 * @param config
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The arguments 'game' and 'config' cannot be null.
	 */
	public OpenCommand(Game game, Properties config) {
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
			if (obj.toString().equalsIgnoreCase(KEYWORD_OPENCOMMAND))
				keywordOpenCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_OPENCOMMAND_ABBREV))
				keywordOpenCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_OPENCOMMAND_ALT))
				keywordOpenCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_OPENCOMMAND_HELP))
				msgOpenCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_OPENCOMMAND_SUCCESS))
				msgOpenCommandSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_OPENCOMMAND_FAILURE))
				msgOpenCommandFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_OPENCOMMAND_UNDOSUCCESS))
				msgOpenCommandUndoSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_OPENCOMMAND_UNDOFAILURE))
				msgOpenCommandUndoFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_WITHCOMMAND))
				keywordWithCommand = config.getProperty(obj.toString());
		}
	}

	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		keywordOpenCommand = "open";
		keywordOpenCommandAbbrev = "o";
		keywordOpenCommandAlt = "unlock";
		keywordWithCommand = "with";

		msgOpenCommandHelp = "(open|o|unlock) <dir> [WITH <objectName>]";
		msgOpenCommandSuccess = "Obstacle Unlocked";
		msgOpenCommandFailure = "Unlock Fail";
		msgOpenCommandUndoSuccess = "Obstacle Locked";
		msgOpenCommandUndoFailure = "The obstacle cannot be locked";
	}

	/**
	 * Executes the Open command, opening the obstacle in the given direction.
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
				if (game.getCurrentLocation().getObstacleStatus(dir)) {
					if (!game.getCurrentLocation().getObstacle(dir)
							.hasBoundedItems()) {
						game.getCurrentLocation().getObstacle(dir)
								.changeStatus();
						// result = "El obstáculo ha sido desactivado" +
						// LINE_SEPARATOR;
						result = msgOpenCommandSuccess + LINE_SEPARATOR;
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
									// result =
									// "El obstáculo ha sido desactivado gracias al item: "
									// + itemName + LINE_SEPARATOR;
									result = msgOpenCommandSuccess
											+ LINE_SEPARATOR;
									return true;
								} else {
									// result =
									// "El obstáculo no puede ser desactivado sin el item necesario"
									// + LINE_SEPARATOR;
									result = msgOpenCommandFailure
											+ LINE_SEPARATOR;
									return false;
								}
							} else {
								// result =
								// "No tienes el objeto indicado en el inventario"
								// + LINE_SEPARATOR;
								result = msgOpenCommandFailure + LINE_SEPARATOR;
								return false;
							}
						} else {
							// result =
							// "Necesitas indicar un Item para abrir el obstaculo"
							// + LINE_SEPARATOR;
							result = msgOpenCommandFailure + LINE_SEPARATOR;
							return false;
						}

					}
				} else {
					// result = "El obstáculo ya se encuentra desactivado" +
					// LINE_SEPARATOR;
					result = msgOpenCommandFailure + LINE_SEPARATOR;
					return false;
				}
			} else {
				// result = "No existe obstáculo en la dirección solicitada" +
				// LINE_SEPARATOR;
				result = msgOpenCommandFailure + LINE_SEPARATOR;
				return false;
			}
		} else {
			// result =
			// "No existe conexión con otra habitación en la dirección solicitada"
			// + LINE_SEPARATOR;
			result = msgOpenCommandFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Gets the help information about this Open command.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgOpenCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Open
	 * command
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if ((firstCommand.equalsIgnoreCase(keywordOpenCommand)
					|| firstCommand.equalsIgnoreCase(keywordOpenCommandAbbrev) || firstCommand
					.equalsIgnoreCase(keywordOpenCommandAlt))
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
	 * Undoes the Open command execution, closing the obstacle in the given
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
			if (!game.getCurrentLocation().getObstacleStatus(dir)) {
				game.getCurrentLocation().getObstacle(dir).changeStatus();
				result = msgOpenCommandUndoSuccess + LINE_SEPARATOR;
				return true;
			} else {
				result = msgOpenCommandUndoFailure + LINE_SEPARATOR;
				return false;
			}
		} else {
			result = msgOpenCommandUndoFailure + LINE_SEPARATOR;
			return false;
		}
	}

	/**
	 * Returns a String representation for this object: OpenCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[]";
	}
}