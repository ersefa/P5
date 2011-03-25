package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.Properties;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents a Take command from the player that undoes the last executed
 * command.
 * 
 * <ul>
 * <li>It uses a command-specific property called "keyword.undoCommand" which
 * default value is "undo".</li>
 * <li>It uses a command-specific property called "keyword.undoCommand.abbrev"
 * which default value is "u".</li>
 * <li>It uses a command-specific property called "keyword.undoCommand.alt"
 * which default value is "reverse".</li>
 * <li>It uses a command-specific property called "message.undoCommand.help"
 * which default value is "undo|u|reverse".</li>
 * <li>It uses a command-specific property called
 * "message.undoCommand.noExecutedCommands" which default value is
 * "There is no command that can be undone.".</li>
 * </ul>
 */
public class UndoCommand extends Command {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Platform-independent line separator
	 */
	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The UndoCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.undoCommand" and its default value
	 * is "undo".</li>
	 * </ul>
	 */
	private final String KEYWORD_UNDOCOMMAND = "keyword.undoCommand";
	private String keywordUndoCommand;

	/**
	 * The UndoCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.undoCommand.abbrev" and its default
	 * value is "u".</li>
	 * </ul>
	 */
	private final String KEYWORD_UNDOCOMMAND_ABBREV = "keyword.undoCommand.abbrev";
	private String keywordUndoCommandAbbrev;

	/**
	 * The UndoCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.undoCommand.alt" and its default
	 * value is "reverse".</li>
	 * </ul>
	 */
	private final String KEYWORD_UNDOCOMMAND_ALT = "keyword.undoCommand.alt";
	private String keywordUndoCommandAlt;

	/**
	 * The UndoCommand help message
	 * <ul>
	 * <li>This property is called "message.undoCommand.help" and its default
	 * value is "undo|u|reverse".</li>
	 * </ul>
	 */
	private final String MESSAGE_UNDOCOMMAND_HELP = "message.undoCommand.help";
	private String msgUndoCommandHelp;
	/**
	 * The UndoCommand no executed commands message
	 * <ul>
	 * <li>This property is called "message.undoCommand.noExecutedCommands" and
	 * its default value is "There is no command that can be undone.</li>
	 * </ul>
	 */
	private final String MESSAGE_UNDOCOMMAND_NOEXECUTEDCOMMANDS = "message.undoCommand.noExecutedCommands";
	private String msgUndoCommandNoExecutedCommands;

	/**
	 * A temporal Command container
	 */
	Command tempCommand = null;

	/**
	 * Constructs an Undo command (as an specific type of Command) that has
	 * access to a given game. Initially the command is unparsed and unexecuted.
	 * Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public UndoCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs an Undo command (as an specific type of Command) that has
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
	public UndoCommand(Game game, Properties config) {
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
		keywordUndoCommand = "undo";
		keywordUndoCommandAbbrev = "u";
		keywordUndoCommandAlt = "reverse";

		msgUndoCommandHelp = "undo|u|reverse";
		msgUndoCommandNoExecutedCommands = "There is no command that can be undone.";
	}

	/**
	 * Sets the specific configuration given in the properties file
	 */
	private void setConfiguration() {
		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(KEYWORD_UNDOCOMMAND))
				keywordUndoCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_UNDOCOMMAND_ABBREV))
				keywordUndoCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_UNDOCOMMAND_ALT))
				keywordUndoCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_UNDOCOMMAND_HELP))
				msgUndoCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_UNDOCOMMAND_NOEXECUTEDCOMMANDS))
				msgUndoCommandNoExecutedCommands = config.getProperty(obj
						.toString());
		}
	}

	/**
	 * Executes the Undo command, asking the game to undo the last executed
	 * command, if it is possible.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed)
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");

		executed = true;

		if (game.getNumberOfExecutedCommands() > 0) {
			tempCommand = game.removeNewestExecutedCommand();
			if (tempCommand != null) {
				tempCommand.undo();
				result = tempCommand.getResult();
				return false;
			}
		} else {
			result = msgUndoCommandNoExecutedCommands + LINE_SEPARATOR;
			return false;
		}
		return false;
	}

	/**
	 * Gets the help information about this Help command: undo.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgUndoCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Undo
	 * command (e.g. "undo").
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		if (line.equalsIgnoreCase(keywordUndoCommand)
				|| line.equalsIgnoreCase(keywordUndoCommandAbbrev)
				|| line.equalsIgnoreCase(keywordUndoCommandAlt)) {
			parsed = true;
			return true;
		} else
			return false;
	}

	/**
	 * Returns a String representation for this object: UndoCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}