package es.ucm.fdi.lps.p5.command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import es.ucm.fdi.lps.p5.Game;
import es.ucm.fdi.lps.p5.Main;
import es.ucm.fdi.lps.p5.exception.UnparsedCommandException;

public class LoadCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The LoadCommand keyword
	 * <ul>
	 * <li>This property is called "keyword.loadCommand" and its default value
	 * is "load".</li>
	 * </ul>
	 */
	private final String KEYWORD_LOADCOMMAND = "keyword.loadCommand";
	private String keywordLoadCommand;

	/**
	 * The LoadCommand keyword abbreviation
	 * <ul>
	 * <li>This property is called "keyword.loadCommand.abbrev" and its default
	 * value is "lo".</li>
	 * </ul>
	 */
	private final String KEYWORD_LOADCOMMAND_ABBREV = "keyword.loadCommand.abbrev";
	private String keywordLoadCommandAbbrev;

	/**
	 * The LoadCommand keyword alternative
	 * <ul>
	 * <li>This property is called "keyword.loadCommand.alt" and its default
	 * value is "continue".</li>
	 * </ul>
	 */
	private final String KEYWORD_LOADCOMMAND_ALT = "keyword.loadCommand.alt";
	private String keywordLoadCommandAlt;

	/**
	 * The LoadCommand help message
	 * <ul>
	 * <li>This property is called "message.loadCommand.help" and its default
	 * value is "(load|lo|continue) <file path>".</li>
	 * </ul>
	 */
	private final String MESSAGE_LOADCOMMAND_HELP = "message.loadCommand.help";
	private String msgLoadCommandHelp;

	/**
	 * The LoadCommand success message
	 * <ul>
	 * <li>This property is called "message.loadCommand.success" and its default
	 * value is "Game loaded.".</li>
	 * </ul>
	 */
	private final String MESSAGE_LOADCOMMAND_SUCCESS = "message.loadCommand.success";
	@SuppressWarnings("unused")
	private String msgLoadCommandSuccess;

	/**
	 * The LoadCommand failure message
	 * <ul>
	 * <li>This property is called "message.loadCommand.failure" and its default
	 * value is "Unable to load the game".</li>
	 * </ul>
	 */
	private final String MESSAGE_LOADCOMMAND_FAILURE = "message.loadCommand.failure";
	private String msgLoadCommandFailure;

	private String filePath;

	/**
	 * Constructs a Load command (as an specific type of Command). Initially
	 * the command is unparsed and unexecuted. Default configuration is assumed
	 * at this moment.
	 * 
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The argument 'game' cannot be null.
	 */
	public LoadCommand(Game game) {
		super(game);
		if (game == null) {
			throw new IllegalArgumentException();
		}

		this.game = game;
		setDefaultConfiguration();
	}

	/**
	 * Constructs a Load command (as an specific type of Command). Initially
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
	public LoadCommand(Game game, Properties config) {
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
			if (obj.toString().equalsIgnoreCase(KEYWORD_LOADCOMMAND))
				keywordLoadCommand = config.getProperty(obj.toString());
			else if (obj.toString()
					.equalsIgnoreCase(KEYWORD_LOADCOMMAND_ABBREV))
				keywordLoadCommandAbbrev = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(KEYWORD_LOADCOMMAND_ALT))
				keywordLoadCommandAlt = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(MESSAGE_LOADCOMMAND_HELP))
				msgLoadCommandHelp = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_LOADCOMMAND_SUCCESS))
				msgLoadCommandSuccess = config.getProperty(obj.toString());
			else if (obj.toString().equalsIgnoreCase(
					MESSAGE_LOADCOMMAND_FAILURE))
				msgLoadCommandFailure = config.getProperty(obj.toString());
		}
	}

	/**
	 * Sets the default configuration
	 */
	private void setDefaultConfiguration() {
		keywordLoadCommand = "load";
		keywordLoadCommandAbbrev = "lo";
		keywordLoadCommandAlt = "continue";

		msgLoadCommandHelp = "(load|lo|continue) <file path>";
		msgLoadCommandSuccess = "Game loaded";
		msgLoadCommandFailure = "Unable to load the game";
	}
	
	/**
	 * Executes the Load command, loading a game from the given file path.
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
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					filePath));
			this.game = (Game) ois.readObject();
			ois.close();
			//Termino el juego original
			game.end();
			result = "";
			//Creo un nuevo juego a partir del juego salvado
			Main.setEngine(game);
			return true;
		} catch (FileNotFoundException e) {
			result = msgLoadCommandFailure + LINE_SEPARATOR;
			return false;
		} catch (IOException e) {
			result = msgLoadCommandFailure + LINE_SEPARATOR;
			return false;
		} catch (ClassNotFoundException e) {
			result = msgLoadCommandFailure + LINE_SEPARATOR;
			return false;
		}
	}
	

	/**
	 * Parses a text line trying to identify a player invocation to this Load
	 * command
	 * 
	 * @see Command#parse(String)
	 */
	@Override
	public boolean parse(String line) {
		Scanner reader = new Scanner(line);
		if (reader.hasNext()) {
			String firstCommand = reader.next();
			if ((firstCommand.equalsIgnoreCase(keywordLoadCommand)
					|| firstCommand.equalsIgnoreCase(keywordLoadCommandAbbrev) || firstCommand
					.equalsIgnoreCase(keywordLoadCommandAlt))
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
	 * Gets the help information about this Load command: load.
	 * 
	 * @see Command#getHelp()
	 */
	@Override
	public String getHelp() {
		return msgLoadCommandHelp;
	}
	
	/**
	 * Returns a String representation for this object: LoadCommand. This is
	 * useful for debugging purposes.
	 * 
	 * @see Command#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + filePath + "]";
	}
}