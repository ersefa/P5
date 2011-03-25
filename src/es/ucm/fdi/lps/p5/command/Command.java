package es.ucm.fdi.lps.p5.command;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.exception.NoCommandResultException;
import es.ucm.fdi.lps.p5.exception.UnexecutedCommandException;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents an abstract command from the player (following the Command
 * pattern). Specific commands know how to parse themselves into a string, clone
 * themselves, execute themselves, undo themselves and print their own help.
 * <ul>
 * <li>It uses a command-specific property called "message.command.failure"
 * which default value is "Execution failure: ".</li>
 * <li>It uses a command-specific property called "message.command.undoSuccess"
 * which default value is "Undo success: ".</li>
 * </ul>
 * 
 */
public abstract class Command implements Cloneable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * Message when a commands fails.
	 * <ul>
	 * <li>This property is called "message.command.failure" and its default
	 * value is "Execution failure: ".</li>
	 * </ul>
	 */
	protected final String MESSAGE_COMMAND_FAILURE = "message.command.failure";
	private String msgCommandFailure;

	/**
	 * Message UndoCommand Success.
	 * <ul>
	 * <li>This property is called "message.command.undoSuccess" and its default
	 * value is "Undo success: ".</li>
	 * </ul>
	 */
	protected final String MESSAGE_COMMAND_UNDOSUCCESS = "message.command.undoSuccess";
	private String msgCommandUndoSuccess;

	/**
	 * A reference to a game object. It will be useful for accessing the room
	 * where the player stays, for manipulating the player inventory and for
	 * printing messages.
	 */
	protected Game game;

	/**
	 * A copy of the game configuration.
	 */
	protected Properties config;
	
	/**
	 * The String command result
	 */
	protected String result;

	/**
	 * Flag that tells whether the command is parsed or not
	 */
	protected boolean parsed;
	
	/**
	 * Flag that tells whether the command is executed or not
	 */
	protected boolean executed;

	/**
	 * Constructs a command that has access to a given game. Initially the
	 * command is unparsed, unexecuted and its configuration has no properties.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public Command(Game game) {
		if (game == null)
			throw new IllegalArgumentException();
		this.game = game;
		parsed = false;
		executed = false;
		setDefaultConfiguration();
	}

	/**
	 * Sets a new configuration. Properties that are not overriden maintain
	 * their previous values.
	 * 
	 * @param config
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The argument 'config' should not be null.
	 */
	public void setConfig(Properties config) {
		if (config == null)
			throw new IllegalArgumentException();
		this.config = config;
		setDefaultConfiguration();

		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(MESSAGE_COMMAND_FAILURE))
				msgCommandFailure = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_COMMAND_UNDOSUCCESS))
				msgCommandUndoSuccess = config.getProperty(obj.toString());
		}
	}

	/**
	 * Sets the default configuration for the Command
	 */
	private void setDefaultConfiguration() {
		msgCommandFailure = "Execution failure: ";
		msgCommandUndoSuccess = "Undo success: ";
	}

	/**
	 * Clones this command, returning a shallow copy of itself (following the
	 * Prototype pattern and the conventions of the clone method of Java
	 * Object).
	 * 
	 * @see Object#clone()
	 */
	public Object clone() {
		try {
			return (Command) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Executes the command. By default, the execution of a command is
	 * unsuccessful (nothing is done but showing the undo failure message).
	 * 
	 * @return true if the command has been correctly executed; false otherwise.
	 * @throws UnparsedCommandException
	 *             This command has not been parsed so it cannot be executed.
	 */
	public boolean execute() {
		if (parsed) {
			return true;
		} else
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
	}

	/**
	 * Checks whether there is a result of the execution (or undoing) of this
	 * command.
	 * 
	 * @return true if there is a result; false otherwise.
	 * @throws UnparsedCommandException
	 *             This command has not been parsed so it cannot have a result.
	 * @throws UnexecutedCommandException
	 *             This command has not been executed so it cannot have a
	 *             result.
	 */
	public boolean hasResult() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		if (!executed)
			throw new UnexecutedCommandException(
					"Error: UnexecutedCommandException");
		return (executed && parsed);

	}

	/**
	 * Gets the result of the execution of this command. This 'result' is
	 * implemented as a simple text that can be shown to the player, giving
	 * information about the last executed command (i.e. whether an item have
	 * been taken or not, after a Take command).
	 * 
	 * @return The result of the execution.
	 * @throws UnparsedCommandException
	 *             This command has not been parsed so it cannot have a result.
	 * @throws UnexecutedCommandException
	 *             This command has not been executed so it cannot have a
	 *             result.
	 * @throws NoCommandResultException
	 *             The execution of this command had no result.
	 */
	public String getResult() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		if (!executed)
			throw new UnexecutedCommandException(
					"Error: UnexecutedCommandException");
		if (executed) {
			return result;
		} else
			return msgCommandFailure + LINE_SEPARATOR;
	}

	/**
	 * Undoes the command execution. By default, the undo of the execution of a
	 * command is successful (nothing is done but showing the undo success
	 * message).
	 * 
	 * @return true if the command has been correctly undone; false otherwise.
	 * @throws UnparsedCommandException
	 *             This command has not been parsed so it cannot be undone.
	 * @throws UnexecutedCommandException
	 *             This command has not been executed so it cannot be undone.
	 */
	public boolean undo() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		if (!executed)
			throw new UnexecutedCommandException(
					"Error: UnexecutedCommandException");
		result = msgCommandUndoSuccess + this.toString() + LINE_SEPARATOR;
		return true;
	}

	/**
	 * Gets the help information about this command.
	 * 
	 * @return The help information.
	 */
	public abstract String getHelp();

	/**
	 * Parses a text line trying to identify a player invocation to this
	 * command.
	 * 
	 * @param line
	 *            The text line.
	 * @return true if the text line has been correctly parsed and the command
	 *         correctly configured; false otherwise.
	 */
	public abstract boolean parse(String line);

	/**
	 * Returns a String representation for this object, depending on the type of
	 * specific command. This is useful for debugging purposes.
	 */
	public abstract String toString();

}
