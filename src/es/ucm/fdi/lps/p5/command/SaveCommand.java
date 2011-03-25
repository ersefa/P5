package es.ucm.fdi.lps.p5.command;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

public class SaveCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The SaveCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.saveCommand" and its default value
	 * is "save".</li>
	 * </ul>
	 */
	private final String KEYWORD_SAVECOMMAND = "keyword.saveCommand";
	private String keywordSaveCommand;

	/**
	 * The SaveCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.saveCommand.abbrev" and its default
	 * value is "s".</li>
	 * </ul>
	 */
	private final String KEYWORD_SAVECOMMAND_ABBREV = "keyword.saveCommand.abbrev";
	private String keywordSaveCommandAbbrev;

	/**
	 * The SaveCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.saveCommand.alt" and its default
	 * value is "backup".</li>
	 * </ul>
	 */
	private final String KEYWORD_SAVECOMMAND_ALT = "keyword.saveCommand.alt";
	private String keywordSaveCommandAlt;

	/**
	 * The SaveCommand help message
	 * <ul>
	 * <li>This property is called "message.saveCommand.help" and its default
	 * value is "(save|s|backup) <file path>".</li>
	 * </ul>
	 */
	private final String MESSAGE_SAVECOMMAND_HELP = "message.saveCommand.help";
	private String msgSaveCommandHelp;

	/**
	 * The SaveCommand success message
	 * <ul>
	 * <li>This property is called "message.saveCommand.success" and its default
	 * value is "Game saved.".</li>
	 * </ul>
	 */
	private final String MESSAGE_SAVECOMMAND_SUCCESS = "message.saveCommand.success";
	private String msgSaveCommandSuccess;

	/**
	 * The SaveCommand failure message
	 * <ul>
	 * <li>This property is called "message.saveCommand.failure" and its default
	 * value is "Unable to save the game".</li>
	 * </ul>
	 */
	private final String MESSAGE_SAVECOMMAND_FAILURE = "message.saveCommand.failure";
	private String msgSaveCommandFailure;

	/**
	 * The path of the file where the contents will be saved
	 */
	private String filePath;

	/**
	 * Constructs a Save command (as an specific type of Command). Initially
	 * the command is unparsed and unexecuted. Default configuration is assumed
	 * at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public SaveCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}

		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Save command (as an specific type of Command). Initially
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
	public SaveCommand(Game game, Properties config) {
		this(game);
		if (game == null) {
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
			if (obj.toString().equalsIgnoreCase(KEYWORD_SAVECOMMAND))
				keywordSaveCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_SAVECOMMAND_ABBREV))
				keywordSaveCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_SAVECOMMAND_ALT))
				keywordSaveCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_SAVECOMMAND_HELP))
				msgSaveCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_SAVECOMMAND_SUCCESS))
				msgSaveCommandSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_SAVECOMMAND_FAILURE))
				msgSaveCommandFailure = config.getProperty(obj.toString());
		}
	}

	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		keywordSaveCommand = "save";
		keywordSaveCommandAbbrev = "s";
		keywordSaveCommandAlt = "backup";

		msgSaveCommandHelp = "(save|s|backup) <file path>";
		msgSaveCommandSuccess = "Game saved";
		msgSaveCommandFailure = "Unable to save the game";
	}
		
	/**
	 * Executes the Save command, saving a game in the given file path.
	 * 
	 * @see Command#execute()
	 */
	public boolean execute() {
		if (!parsed) {
			throw new UnparsedCommandException(
					"Error: UnparsedCommandException");
		}
		executed = true;
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
					filePath));
//			oos.writeObject(new Integer(9));
			oos.writeObject(game);
			oos.close();
			result = msgSaveCommandSuccess + LINE_SEPARATOR;
			return true;
		} catch (FileNotFoundException e) {
			result = msgSaveCommandFailure + " Error: Fichero no encontrado" + LINE_SEPARATOR;
			return false;
		} catch (IOException e) {
			result = msgSaveCommandFailure + " Error: IOException" + LINE_SEPARATOR;
			return false;
		}
	}
		
	/**
	 * Parses a text line trying to identify a player invocation to this Save
	 * command
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if ((firstCommand.equalsIgnoreCase(keywordSaveCommand)
					|| firstCommand.equalsIgnoreCase(keywordSaveCommandAbbrev) || firstCommand
					.equalsIgnoreCase(keywordSaveCommandAlt))
					&& reader.hasNext()) {
				filePath = reader.next();
				parsed = true;
				return true;
			} else
				return false;
		} else
			return false;
	}
	
	/**
	 * Gets the help information about this Save command: save.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgSaveCommandHelp;
	}

	/**
	 * Returns a String representation for this object: SaveCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + filePath + "]";
	}
}
