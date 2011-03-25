package es.ucm.fdi.lps.p5.command;

import java.util.Enumeration;
import java.util.Properties;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

/**
 * Represents a Help command from the player that ends the game.
 * 
 * <ul>
 * <li>It uses a command-specific property called "keyword.quitCommand" which
 * default value is "quit".</li>
 * <li>It uses a command-specific property called "keyword.quitCommand.abbrev"
 * which default value is "q".</li>
 * <li>It uses a command-specific property called "keyword.quitCommand.alt"
 * which default value is "exit".</li>
 * <li>It uses a command-specific property called "message.quitCommand.help"
 * which default value is "quit|q|exit".</li>
 * </ul>
 */
public class QuitCommand extends Command {

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
	 * The QuitCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.quitCommand" and its default value
	 * is "quit".</li>
	 * </ul>
	 */
	private final String KEYWORD_QUITCOMMAND = "keyword.quitCommand";
	private String keywordQuitCommand;

	/**
	 * The QuitCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.quitCommand.abbrev" and its default
	 * value is "l".</li>
	 * </ul>
	 */
	private final String KEYWORD_QUITCOMMAND_ABBREV = "keyword.quitCommand.abbrev";
	private String keywordQuitCommandAbbrev;

	/**
	 * The QuitCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.quitCommand.alt" and its default
	 * value is "search".</li>
	 * </ul>
	 */
	private final String KEYWORD_QUITCOMMAND_ALT = "keyword.quitCommand.alt";
	private String keywordQuitCommandAlt;

	/**
	 * The QuitCommand help message
	 * <ul>
	 * <li>This property is called "message.quitCommand.help" and its default
	 * value is "quit|l|search".</li>
	 * </ul>
	 */
	private final String MESSAGE_QUITCOMMAND_HELP = "message.quitCommand.help";
	private String msgQuitCommandHelp;
	
	/**
	 * Message of the player score (the name of the property).
	 * <ul>
	 * <li>This property is called "message.playerScore" and its default value
	 * is "Player score: ".</li>
	 * </ul>
	 */
	public final String MESSAGE_PLAYERSCORE = "message.playerScore";
	private String msgPlayerScore;

	/**
	 * Message of game over (the name of the property).
	 * <ul>
	 * <li>This property is called "message.gameOver" and its default value is
	 * "GAME OVER".</li>
	 * </ul>
	 */
	public final String MESSAGE_GAMEOVER = "message.gameOver";
	private String msgGameOver;

	/**
	 * Constructs a Quit command (as an specific type of Command) that has
	 * access to a given game. Initially the command is unparsed and unexecuted.
	 * Default configuration is assumed at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public QuitCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Quit command (as an specific type of Command) that has
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
	public QuitCommand(Game game, Properties config) {
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
		keywordQuitCommand = "quit";
		keywordQuitCommandAbbrev = "q";
		keywordQuitCommandAlt = "exit";

		msgQuitCommandHelp = "quit|q|exit";
		
		msgPlayerScore = "Player score: ";
		msgGameOver = "GAME OVER";
	}

	/**
	 * Sets the specific configuration given in the properties file
	 */
	private void setConfiguration() {
		for (Enumeration<Object> e = config.keys(); e.hasMoreElements();) {
			Object obj = e.nextElement();
			if (obj.toString().equalsIgnoreCase(KEYWORD_QUITCOMMAND))
				keywordQuitCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_QUITCOMMAND_ABBREV))
				keywordQuitCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_QUITCOMMAND_ALT))
				keywordQuitCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_QUITCOMMAND_HELP))
				msgQuitCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_PLAYERSCORE))
				msgPlayerScore = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_GAMEOVER))
				msgGameOver = config.getProperty(obj.toString());
		}
	}

	/**
	 * Executes the Quit command, ending the game.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed) {
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		}
		result = msgPlayerScore + game.reportInventoryValue() + LINE_SEPARATOR + msgGameOver;
		executed = true;
		game.end();
		return true;
	}

	/**
	 * Gets the help information about this Quit command.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgQuitCommandHelp;
	}

	/**
	 * Parses a text line trying to identify a player invocation to this Quit
	 * command (e.g. "quit").
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		if (line.equalsIgnoreCase(keywordQuitCommand)
				|| line.equalsIgnoreCase(keywordQuitCommandAbbrev)
				|| line.equalsIgnoreCase(keywordQuitCommandAlt)) {
			parsed = true;
			return true;
		} else
			return false;
	}

	/**
	 * Returns a String representation for this object: QuitCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
