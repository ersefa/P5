package es.ucm.fdi.lps.p5;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import es.ucm.fdi.lps.p5.command.CloseCommand;
import es.ucm.fdi.lps.p5.command.Command;
import es.ucm.fdi.lps.p5.command.DropCommand;
import es.ucm.fdi.lps.p5.command.ExamineCommand;
import es.ucm.fdi.lps.p5.command.GoCommand;
import es.ucm.fdi.lps.p5.command.HelpCommand;
import es.ucm.fdi.lps.p5.command.LoadCommand;
import es.ucm.fdi.lps.p5.command.LookCommand;
import es.ucm.fdi.lps.p5.command.OpenCommand;
import es.ucm.fdi.lps.p5.command.QuitCommand;
import es.ucm.fdi.lps.p5.command.SaveCommand;
import es.ucm.fdi.lps.p5.command.TakeCommand;
import es.ucm.fdi.lps.p5.command.UndoCommand;
import es.ucm.fdi.lps.p5.exception.NoNextCommandException;

/**
 * Represents the parser of the game engine that analyzes the input stream and
 * identifies valid commands.
 */
public class Parser {

	/**
	 * Platform-independent line separator
	 */
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * A reference to the game that created the Interpreter
	 */
	private Game game;

	/**
	 * Contains the prototypes of every command that the player can use during
	 * the game
	 */
	private Vector<Command> commandPrototypes;

	/**
	 * Scanner stream to parse the input
	 */
	private Scanner reader;

	/**
	 * The properties object to store game configuration parsed from file
	 */
	private Properties config;

	/**
	 * Constructs a parser, specifying the input stream from which the game
	 * receives the player commands and a reference to the game itself (to be
	 * used for constructing the commands), assuming the default configuration.
	 * 
	 * @param in
	 *            The input stream.
	 * @param game
	 *            The game.
	 * @throws IllegalArgumentException
	 *             The arguments 'in' and 'game' cannot be null.
	 */
	public Parser(InputStream in, Game game) {
		if ((in == null) || (game == null)) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		this.game = game;
		setInput(in);
		createPrototype();
	}

	/**
	 * Constructs a parser, specifying the input stream from which the game
	 * receives the player commands, a reference to the game itself (to be used
	 * for constructing the commands) and a new configuration.
	 * 
	 * @param in
	 *            The input stream.
	 * @param game
	 *            The game.
	 * @param config
	 * 
	 *            The configuration.
	 * @throws IllegalArgumentException
	 *             The arguments 'in', 'game' and 'config' cannot be null.
	 */
	public Parser(InputStream in, Game game, Properties config) {
		if ((in == null) || (game == null)) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		this.game = game;
		this.config = config;
		setInput(in);
		createConfigPrototype();
		setConfig(this.config);
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
		if (config == null) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}

		for (Command com : commandPrototypes) {
			com.setConfig(config);
		}
	}

	/**
	 * Sets the input stream for the parser.
	 * 
	 * @param input
	 *            The input.
	 * @throws IllegalArgumentException
	 *             The argument 'input' cannot be null.
	 */
	public void setInput(InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		reader = new Scanner(input);
	}

	/**
	 * Reports the help information of all the available commands.
	 * 
	 * @return The help information.
	 */
	public String reportHelp() {
		String help = "";
		for (Command com : commandPrototypes) {
			help += com.getHelp();
			help += LINE_SEPARATOR;
		}
		return help;
	}

	/**
	 * Checks whether there is text for another command in the player input.
	 * 
	 * @return true if there is text for another command; false otherwise.
	 */
	public boolean hasNextCommand() {
		return (reader.hasNext());

	}

	/**
	 * Parses the player input, consuming its characters and returning the next
	 * command that has been identified.
	 * 
	 * @return The parsed command. It can be null, meaning an invalid command
	 *         was parsed.
	 * @throws NoNextCommandException
	 *             There is no next command to parse.
	 */
	public Command parseNextCommand() {
		if (!hasNextCommand())
			throw new NoNextCommandException("Error: NoNextCommandException");

		String line = reader.nextLine();
		for (Command com : commandPrototypes) {
			if (com.parse(line))
				return (Command) com.clone();
		}
		return null;

	}

	/**
	 * Creates a vector with all command Prototypes
	 */
	private void createPrototype() {
		commandPrototypes = new Vector<Command>();

		commandPrototypes.add(new HelpCommand(game, this));
		commandPrototypes.add(new GoCommand(game));
		commandPrototypes.add(new LookCommand(game));
		commandPrototypes.add(new ExamineCommand(game));
		commandPrototypes.add(new TakeCommand(game));
		commandPrototypes.add(new DropCommand(game));
		commandPrototypes.add(new UndoCommand(game));
		commandPrototypes.add(new QuitCommand(game));
		commandPrototypes.add(new OpenCommand(game));
		commandPrototypes.add(new CloseCommand(game));
		commandPrototypes.add(new SaveCommand(game));
		commandPrototypes.add(new LoadCommand(game));
	}

	/**
	 * Creates a vector with all command Prototypes and its configuration
	 * properties
	 */
	private void createConfigPrototype() {
		commandPrototypes = new Vector<Command>();

		commandPrototypes.add(new HelpCommand(game, this, config));
		commandPrototypes.add(new GoCommand(game, config));
		commandPrototypes.add(new LookCommand(game, config));
		commandPrototypes.add(new ExamineCommand(game, config));
		commandPrototypes.add(new TakeCommand(game, config));
		commandPrototypes.add(new DropCommand(game, config));
		commandPrototypes.add(new UndoCommand(game, config));
		commandPrototypes.add(new QuitCommand(game, config));
		commandPrototypes.add(new OpenCommand(game, config));
		commandPrototypes.add(new CloseCommand(game, config));
		commandPrototypes.add(new SaveCommand(game, config));
		commandPrototypes.add(new LoadCommand(game, config));
	}

}
