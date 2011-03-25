package es.ucm.fdi.lps.p5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.ucm.fdi.lps.p5.command.CloseCommand;
import es.ucm.fdi.lps.p5.command.Command;
import es.ucm.fdi.lps.p5.command.DropCommand;
import es.ucm.fdi.lps.p5.command.ExamineCommand;
import es.ucm.fdi.lps.p5.command.GoCommand;
import es.ucm.fdi.lps.p5.command.HelpCommand;
import es.ucm.fdi.lps.p5.command.LookCommand;
import es.ucm.fdi.lps.p5.command.OpenCommand;
import es.ucm.fdi.lps.p5.command.QuitCommand;
import es.ucm.fdi.lps.p5.command.TakeCommand;
import es.ucm.fdi.lps.p5.command.UndoCommand;
import es.ucm.fdi.lps.p5.exception.InvalidBoundedItemException;
import es.ucm.fdi.lps.p5.exception.InvalidBoundedObstacleException;
import es.ucm.fdi.lps.p5.exception.InvalidGameDefinitionException;
import es.ucm.fdi.lps.p5.exception.ItemAlreadyInRepositoryException;
import es.ucm.fdi.lps.p5.exception.ItemNotInRepositoryException;
import es.ucm.fdi.lps.p5.exception.NoConnectedLocationException;
import es.ucm.fdi.lps.p5.exception.NoExecutedCommandsException;
import es.ucm.fdi.lps.p5.exception.NoGameEventsException;

/**
 * Represents the state of all the elements of the game (locations, items,
 * player inventory, etc.), and relevant constants for the game (the available
 * movement directions, the keywords for a game textual definition, etc.).
 */
public class Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Platform-independent line separator
	 */
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * Game title
	 */
	private String title;

	/**
	 * Game author
	 */
	private String author;

	/**
	 * Game description.
	 */
	private String description;

	/**
	 * Game specialHelp
	 */
	private String specialHelp;

	/**
	 * Game initialLocation;
	 */
	private Location currentLocation;

	/**
	 * The location id.
	 */
	private String locationId;

	/**
	 * Player inventory
	 */
	private ItemRepository playerInventory;

	/**
	 * Successfully executed command history
	 */
	private Vector<Command> commandHistory;

	/**
	 * Vector with all gameEvent messages.
	 */
	private Vector<String> gameEvents;

	/**
	 * Flag that decides when the game has finished.
	 */
	private boolean quitFlag = false;

	/**
	 * Flag that points if there are new events to report from the game
	 */
	private boolean eventFlag;

	/**
	 * Maps a the location name with the location object itself
	 */
	private Map<String, Location> locations;

	/**
	 * Maps a connection name with the connection itself
	 */
	private Map<String, EnumMap<Direction, String>> connections;

	/**
	 * Temporal direction that maps a Direction to a location name
	 */
	private EnumMap<Direction, String> tempDirection;

	/**
	 * Contains a list of items
	 */
	private List<Item> itemList;

	/**
	 * Contains a list of obstacles
	 */
	private List<Obstacle> obstacleList;

	/**
	 * Maps the command name with the number of executions of that command
	 */
	private Map<String, Integer> commandCount;

	private Hashtable<Obstacle, ArrayList<String>> boundedItemsList;
	private Hashtable<Obstacle, ArrayList<String>> boundedObstaclesList;
	private ArrayList<String> boundedItems;
	private ArrayList<String> boundedObstacles;

	/**
	 * Represents all the possible directions for the connections between
	 * locations: NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST,
	 * NORTHWEST, UP, DOWN, IN and OUT.
	 */
	public enum Direction implements Serializable {
		NORTH("n") {
			@Override
			public Direction getOppositeDirection() {
				return SOUTH;
			}
		},
		NORTHEAST("ne") {
			@Override
			public Direction getOppositeDirection() {
				return SOUTHWEST;
			}
		},
		EAST("e") {
			@Override
			public Direction getOppositeDirection() {
				return WEST;
			}
		},
		SOUTHEAST("se") {
			@Override
			public Direction getOppositeDirection() {
				return NORTHWEST;
			}
		},
		SOUTH("s") {
			@Override
			public Direction getOppositeDirection() {
				return NORTH;
			}
		},
		SOUTHWEST("sw") {
			@Override
			public Direction getOppositeDirection() {
				return NORTHEAST;
			}
		},
		WEST("w") {
			@Override
			public Direction getOppositeDirection() {
				return EAST;
			}
		},
		NORTHWEST("nw") {
			@Override
			public Direction getOppositeDirection() {
				return SOUTHEAST;
			}
		},
		UP("u") {
			@Override
			public Direction getOppositeDirection() {
				return DOWN;
			}
		},
		DOWN("d") {
			@Override
			public Direction getOppositeDirection() {
				return UP;
			}
		},
		IN("i") {
			@Override
			public Direction getOppositeDirection() {
				return OUT;
			}
		},
		OUT("o") {
			@Override
			public Direction getOppositeDirection() {
				return IN;
			}
		};

		/**
		 * Contains the Direction shorter form
		 */
		private String dir;

		/**
		 * Direction enum constructor
		 * 
		 * @param dir
		 *            the shorter Direction form
		 */
		private Direction(String dir) {
			this.dir = dir;
		}

		/**
		 * Gets the keyword string used for representing connection between
		 * locations in a game definition.
		 * 
		 * @return The keyword.
		 */
		public String getKeyword() {
			return this.dir;
		}

		/**
		 * It returns the opposite direction of a given one
		 * 
		 * @return The opposite direction
		 */
		public abstract Direction getOppositeDirection();
	}

	/**
	 * Constructs a game using some basic information (a given title, a given
	 * author and a given description) and an initial location.
	 * 
	 * @param title
	 *            The title.
	 * @param author
	 *            The author.
	 * @param description
	 *            The description.
	 * @param initialLocation
	 *            The initial location.
	 * @throws IllegalArgumentException
	 *             The arguments 'title', 'author', 'description' and
	 *             'initialLocation' cannot be null.
	 */
	public Game(String title, String author, String description,
			Location initialLocation) {
		if ((title == null) || (author == null) || (description == null)
				|| (initialLocation == null))
			throw new IllegalArgumentException();
		this.title = title;
		this.author = author;
		this.description = description;
		this.currentLocation = initialLocation;

		locations = new Hashtable<String, Location>();
		connections = new Hashtable<String, EnumMap<Direction, String>>();
		commandHistory = new Vector<Command>();
		playerInventory = new ItemRepository();
		gameEvents = new Vector<String>();

		itemList = new ArrayList<Item>();
		obstacleList = new ArrayList<Obstacle>();
		commandCount = new Hashtable<String, Integer>();

		boundedItemsList = new Hashtable<Obstacle, ArrayList<String>>();
		boundedObstaclesList = new Hashtable<Obstacle, ArrayList<String>>();
		boundedItems = new ArrayList<String>();
		boundedObstacles = new ArrayList<String>();
	}

	/**
	 * Constructs a game, based on a textual definition of it. A game definition
	 * consists on: <br>
	 * <ul>
	 * <li>The keyword 'game' plus three strings with the title, the author/s
	 * and the initial description of the game.</li>
	 * <li>One or more location definitions (the first one will be considered
	 * the initial location). A location definition consists on:
	 * <ul>
	 * <li>The keyword 'location' plus a unique id for the location, two strings
	 * with the name and the description of the location plus (optionally) an
	 * exit threshold and the string of the corresponding exit message for this
	 * location.</li>
	 * <li>Zero or more item definitions. An item definition consists on:</li>
	 * <ul>
	 * <li>The keyword 'item' plus two strings and an integer with the name, the
	 * description and the value of the item.</li>
	 * </ul>
	 * <li>Zero or more connections to other locations. A connection consists
	 * on:
	 * <ul>
	 * <li>A direction keyword plus the id of the connected location.</li>
	 * </ul>
	 * </ul>
	 * </ul>
	 * 
	 * @param gameDefinition
	 *            The game definition.
	 * @throws IllegalArgumentException
	 *             The argument 'gameDefinition' cannot be null.
	 * @throws InvalidGameDefinitionException
	 *             The game definition is invalid so a new game cannot be
	 *             created with it. Additional information is provided about the
	 *             error found in the definition and its position (e.g.
	 *             "Error in Token[location], line 14: First-level structures should be locations."
	 *             ).
	 */
	public Game(InputStream gameDefinition)
			throws InvalidGameDefinitionException {
		if (gameDefinition == null)
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");

		locations = new Hashtable<String, Location>();
		connections = new Hashtable<String, EnumMap<Direction, String>>();
		commandHistory = new Vector<Command>();
		playerInventory = new ItemRepository();
		gameEvents = new Vector<String>();

		itemList = new ArrayList<Item>();
		obstacleList = new ArrayList<Obstacle>();
		commandCount = new Hashtable<String, Integer>();

		boundedItemsList = new Hashtable<Obstacle, ArrayList<String>>();
		boundedObstaclesList = new Hashtable<Obstacle, ArrayList<String>>();
		boundedItems = new ArrayList<String>();
		boundedObstacles = new ArrayList<String>();

		// Try to parse as XML file, if not a valid XML -> then try to parse as
		// plain text
		try {
			createXMLGameDefinition(gameDefinition);
		} catch (ParserConfigurationException e) {
			createGameDefinition(gameDefinition);
		} catch (SAXException e) {
			createGameDefinition(gameDefinition);
		} catch (IOException e) {
			createGameDefinition(gameDefinition);
		}
	}

	/**
	 * Parses the game elements from a XML gameDefinition file
	 * 
	 * @param gameDefinition
	 *            The XML file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void createXMLGameDefinition(InputStream gameDefinition)
			throws ParserConfigurationException, SAXException, IOException {
		if (gameDefinition == null)
			throw new IllegalArgumentException();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		SAXParser parser = factory.newSAXParser();
		DefaultHandler oyente = new SAXHandler(this);
		parser.parse(gameDefinition, oyente);
	}

	/**
	 * Sets the game title
	 * 
	 * @param title
	 *            the title
	 */
	public void setTitle(String title) {
		if (title == null)
			throw new IllegalArgumentException();

		this.title = title;
	}

	/**
	 * Sets the game author
	 * 
	 * @param author
	 *            the author
	 */
	public void setAuthor(String author) {
		if (author == null)
			throw new IllegalArgumentException();

		this.author = author;
	}

	/**
	 * Sets the game description
	 * 
	 * @param description
	 *            The string with the description
	 */
	public void setDescription(String description) {
		if (description == null)
			throw new IllegalArgumentException();

		this.description = description;
	}

	/**
	 * Returns the current location
	 * 
	 * @return the current location
	 */
	public Location getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * Sets the currentLocation
	 * 
	 * @param currentLocation
	 *            the location
	 */
	public void setCurrentLocation(Location currentLocation) {
		if (currentLocation == null)
			throw new IllegalArgumentException();

		this.currentLocation = currentLocation;
	}

	/**
	 * Returns the game locations
	 * 
	 * @return the game locations
	 */
	public Map<String, Location> getLocations() {
		return locations;
	}

	/**
	 * Parses the game elements from a gameDefinitions file
	 * 
	 * @param gameDefinition
	 *            The gameDefinition file
	 * @throws InvalidGameDefinitionException
	 */
	private void createGameDefinition(InputStream gameDefinition)
			throws InvalidGameDefinitionException {
		if (gameDefinition == null)
			throw new IllegalArgumentException();

		Reader r = new BufferedReader(new InputStreamReader(gameDefinition));
		StreamTokenizer st = new StreamTokenizer(r);
		boolean firstLineParsed = false;
		st.commentChar(35);
		st.quoteChar(34);

		try {
			st.nextToken();

			if (st.ttype == StreamTokenizer.TT_EOF)
				throw new InvalidGameDefinitionException(
						"Error in definitions game.");

			while (st.ttype != StreamTokenizer.TT_EOF) {
				if (st.ttype == StreamTokenizer.TT_WORD) {
					if ((!firstLineParsed) && !(st.sval.equals("game"))) {
						throw new InvalidGameDefinitionException(
								"Error in line: " + st.lineno()
										+ " in definitions game"
										+ LINE_SEPARATOR
										+ "First token must be *game*");
					}

					if (st.sval.equals("game") && (!firstLineParsed)) {
						createDescription(st);
						firstLineParsed = true;
					} else if (st.sval.equals("location")) {
						createLocation(st);
					} else if (st.sval.equals("help")) {
						createSpecialHelp(st);
					} else
						throw new InvalidGameDefinitionException(
								"Error in line: "
										+ st.lineno()
										+ " in definitions game"
										+ LINE_SEPARATOR
										+ "Second token after *game* must be *location*");
				} else
					throw new InvalidGameDefinitionException("Error in line: "
							+ st.lineno() + " in definitions game"
							+ LINE_SEPARATOR + "Not a word");
			}
			linkLocations();
			linkBoundedItemsToObstacles();
			linkBoundedObstaclesToObstacles();
			r.close();
		} catch (IOException e) {
			throw new InvalidGameDefinitionException("Error in line: "
					+ st.lineno() + " in definitions game");
		} catch (NullPointerException e) {
			throw new InvalidGameDefinitionException("Error in line: "
					+ st.lineno() + " in definitions game");
		}
	}

	/**
	 * Parses the special help from the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @throws IOException
	 */
	private void createSpecialHelp(StreamTokenizer st) throws IOException {
		if (st == null)
			throw new IllegalArgumentException();

		st.nextToken();
		specialHelp = st.sval;
		st.nextToken();
	}

	/**
	 * Parses the game description from the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @throws IOException
	 */
	private void createDescription(StreamTokenizer st) throws IOException {
		if (st == null)
			throw new IllegalArgumentException();

		st.nextToken();
		title = st.sval;
		st.nextToken();
		author = st.sval;
		st.nextToken();
		description = st.sval;
		st.nextToken();

	}

	/**
	 * Creates the locations parsing the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @throws IOException
	 */
	private void createLocation(StreamTokenizer st) throws IOException {
		if (st == null)
			throw new IllegalArgumentException();

		tempDirection = new EnumMap<Direction, String>(Direction.class);

		Location tempLocation = readLocation(st);
		readItems(st, tempLocation);
		readConnections(st, tempLocation);

		connections.put(locationId, tempDirection);
		locations.put(locationId, tempLocation);
	}

	/**
	 * Parses locations from the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @return The parsed location
	 * @throws IOException
	 */
	private Location readLocation(StreamTokenizer st) throws IOException {
		if (st == null)
			throw new IllegalArgumentException();

		st.nextToken();
		locationId = st.sval;
		st.nextToken();
		String locationName = st.sval;
		st.nextToken();
		String locationDescription = st.sval;

		st.nextToken();
		if (st.ttype == StreamTokenizer.TT_NUMBER) {
			int threshold = (int) st.nval;
			st.nextToken();
			String endMsg = st.sval;
			st.nextToken();
			Location tempLocation = new Location(locationId, locationName,
					locationDescription, threshold, endMsg);
			// Agregar localización inicial
			if (currentLocation == null)
				currentLocation = tempLocation;
			return tempLocation;
		} else {
			Location tempLocation = new Location(locationId, locationName,
					locationDescription);
			// Agregar localización inicial
			if (currentLocation == null)
				currentLocation = tempLocation;
			return tempLocation;
		}
	}

	/**
	 * Parses the items from the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @param tempLocation
	 *            The temporal location
	 * @throws IOException
	 */
	private void readItems(StreamTokenizer st, Location tempLocation)
			throws IOException {
		if ((st == null) || (tempLocation == null))
			throw new IllegalArgumentException();

		if (st.sval.equals("item")) {
			while (st.sval.equals("item")) {
				st.nextToken();
				String itemID = st.sval;
				st.nextToken();
				String itemName = st.sval;
				st.nextToken();
				String itemDescription = st.sval;
				st.nextToken();
				int itemValue = (int) st.nval;
				st.nextToken();
				int itemWeight = (int) st.nval;
				st.nextToken();

				Item newItem = new Item(itemID, itemName, itemDescription,
						itemValue, itemWeight);
				tempLocation.addItem(newItem);

				itemList.add(newItem);
			}
		}
	}

	/**
	 * Parses the connections from the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @param tempLocation
	 *            The temporal location
	 * @throws IOException
	 */
	private void readConnections(StreamTokenizer st, Location tempLocation)
			throws IOException {
		if ((st == null) || (tempLocation == null))
			throw new IllegalArgumentException();

		Direction oldDir = null;
		while (!st.sval.equals("location")) {
			if (st.sval.equals("obstacle")) {
				readObstacle(st, oldDir, tempLocation);
			}
			String shortDirection = st.sval;
			st.nextToken();
			String connectedLocationID = st.sval;
			for (Direction dir : Direction.values()) {
				if (dir.getKeyword().equals(shortDirection)) {
					tempDirection.put(dir, connectedLocationID);
					oldDir = dir;
					break;
				}
			}
			if (st.nextToken() == StreamTokenizer.TT_EOF) {
				break;
			}
		}
	}

	/**
	 * Parses the obstacles from the gameDefinition file
	 * 
	 * @param st
	 *            The file stream
	 * @param dir
	 *            The direction where the obstacle resides
	 * @param tempLocation
	 *            The temporal location
	 * @throws IOException
	 */

	@SuppressWarnings("unchecked")
	private void readObstacle(StreamTokenizer st, Direction dir,
			Location tempLocation) throws IOException {
		if ((st == null) || (tempLocation == null) || (dir == null))
			throw new IllegalArgumentException();

		st.nextToken();
		String obstacleId = st.sval;
		st.nextToken();
		String obstacleName = st.sval;
		st.nextToken();
		String obstacleDescription = st.sval;
		st.nextToken();
		Boolean obstacleStatus = Boolean.parseBoolean(st.sval);
		st.nextToken();
		String obstacleErrorMsg = st.sval;
		st.nextToken();

		Obstacle tempObstacle = new Obstacle(obstacleId, obstacleName,
				obstacleDescription, obstacleStatus, obstacleErrorMsg, dir);

		obstacleList.add(tempObstacle);
		tempLocation.addObstacle(dir, tempObstacle);

		if (st.sval.equals("itemRef")) {
			while (st.sval.equals("itemRef")) {
				st.nextToken();
				String itemRefName = st.sval;
				boundedItems.add(itemRefName);
				st.nextToken();
			}
			
		}

		if (!boundedItems.isEmpty()) {
			boundedItemsList.put(tempObstacle,
					(ArrayList<String>) boundedItems.clone());
		}

		if (st.sval.equals("obsRef")) {
			while (st.sval.equals("obsRef")) {
				st.nextToken();
				String obstacleRefName = st.sval;
				boundedObstacles.add(obstacleRefName);
				st.nextToken();
			}
		}

		if (!boundedObstacles.isEmpty()) {
			boundedObstaclesList.put(tempObstacle,
					(ArrayList<String>) boundedObstacles.clone());
		}
	}

	/**
	 * Link the locations when the parse finishes
	 */
	private void linkLocations() {
		Location connectedLocation;
		String connectedLocationName;
		tempDirection = new EnumMap<Direction, String>(Direction.class);

		for (String name : locations.keySet()) {
			tempDirection = connections.get(name);

			for (Direction dir : Direction.values()) {
				if (tempDirection.containsKey(dir)) {
					connectedLocationName = tempDirection.get(dir);
					if (locations.containsKey(connectedLocationName)) {
						connectedLocation = locations
								.get(connectedLocationName);
						locations.get(name).setConnection(dir,
								connectedLocation);
					}
				}
			}
		}
	}

	/**
	 * Links the boundedItems to the obstacles when the parse finishes
	 */
	private void linkBoundedItemsToObstacles() {
		boolean founded;
		for (Obstacle obs : boundedItemsList.keySet()) {
			ArrayList<String> obsBoundedItems = boundedItemsList.get(obs);

			Iterator<String> obsBoundedItemsItr = obsBoundedItems.iterator();
			
			while (obsBoundedItemsItr.hasNext()) {
				String itemName = obsBoundedItemsItr.next();
				Iterator<Item> itemItr = itemList.iterator();
				founded = false;
				while (itemItr.hasNext()) {
					Item it = itemItr.next();
					if (it.getId().equals(itemName)) {
						founded = true;
						obs.addBoundedItem(it);
					}
				}
				if (!founded) throw new InvalidBoundedItemException("InvalidBoundedItemException");

			}
		}
	}

	/**
	 * Links the boundedObstacles to the obstacles when the parse finishes
	 */
	private void linkBoundedObstaclesToObstacles() {
		boolean founded;
		for (Obstacle obs : boundedObstaclesList.keySet()) {
			ArrayList<String> obsBoundedObstacles = boundedObstaclesList
					.get(obs);

			Iterator<String> obsBoundedObstaclesItr = obsBoundedObstacles
					.iterator();
			

			while (obsBoundedObstaclesItr.hasNext()) {
				String obstacleName = obsBoundedObstaclesItr.next();
				Iterator<Obstacle> obstacleItr = obstacleList.iterator();
				founded = false;
				while (obstacleItr.hasNext()) {
					Obstacle obstacle = obstacleItr.next();
					if (obstacle.getId().equals(obstacleName)) {
						founded = true;
						obs.addBoundedObstacle(obstacle);
					}
				}
				if (!founded) throw new InvalidBoundedObstacleException("InvalidBoundedObstacleException");
			}
		}
	}

	/**
	 * Adds an executed command to the command history.
	 * 
	 * @param command
	 *            The executed command.
	 * @throws IllegalArgumentException
	 *             The argument 'command' cannot be null.
	 */
	public void addExecutedCommand(Command command) {
		if (command == null)
			throw new IllegalArgumentException();
		commandHistory.add(command);
	}

	/**
	 * Clears the history of executed commands.
	 */
	public void clearExecutedCommands() {
		commandHistory.clear();
	}

	/**
	 * Gets the number of executed commands that are in the command history.
	 * 
	 * @return The number of executed commands.
	 */
	public int getNumberOfExecutedCommands() {
		return commandHistory.size();
	}

	/**
	 * Removes the newest executed command of the command history.
	 * 
	 * @return The command.
	 * @throws NoExecutedCommandsException
	 *             There are not executed commands in the command history so the
	 *             newest executed command cannot be removed.
	 */
	public Command removeNewestExecutedCommand() {
		if (commandHistory.isEmpty())
			throw new NoExecutedCommandsException(
					"Error: NoExecutedCommandsException");
		Command temp = commandHistory.lastElement();
		commandHistory.remove(commandHistory.lastElement());
		return temp;
	}

	/**
	 * Removes the oldest executed command of the command history.
	 * 
	 * @return The command.
	 * @throws NoExecutedCommandsException
	 *             There are not executed commands in the command history so the
	 *             oldest executed command cannot be removed.
	 */
	public Command removeOldestExecutedCommand() {
		if (commandHistory.isEmpty())
			throw new NoExecutedCommandsException(
					"Error: NoExecutedCommandsException");
		Command temp = commandHistory.firstElement();
		commandHistory.remove(commandHistory.firstElement());
		return temp;
	}

	/**
	 * Reports the basic information of the game (title, author and
	 * description).
	 * 
	 * @return The basic information of the game.
	 */
	public String reportInformation() {
		return title + LINE_SEPARATOR + author + LINE_SEPARATOR + description;
	}

	/**
	 * Reports the value of the player inventory.
	 * 
	 * @return The value.
	 */
	public int reportInventoryValue() {
		return playerInventory.getTotalValue();
	}

	/**
	 * Reports the weight of the player inventory.
	 * 
	 * @return The weight.
	 */
	public int reportInventoryWeight() {
		return playerInventory.getTotalWeight();
	}

	/**
	 * Checks whether the player location has a connected location in a given
	 * direction.
	 * 
	 * @param direction
	 *            The direction.
	 * @return true if there is a connected location in the given direction,
	 *         false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'direction' cannot be null.
	 */
	public boolean hasConnectedLocation(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException();

		return currentLocation.hasConnectedLocation(direction);
	}

	/**
	 * Checks whether the player location has an active obstacle in a given
	 * direction.
	 * 
	 * @param direction
	 *            The direction.
	 * @return true if there is an active obstacle in the given direction, false
	 *         otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'direction' cannot be null.
	 */
	public boolean hasObstacle(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException();

		return currentLocation.getObstacleStatus(direction);
	}

	/**
	 * Returns the obstacle in the given direction for the current location
	 * 
	 * @param direction
	 *            The direction.
	 * @return The obstacle
	 * @throws IllegalArgumentException
	 *             The argument 'direction' cannot be null.
	 */
	public Obstacle getObstacle(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException();

		return currentLocation.getObstacle(direction);
	}

	/**
	 * Moves the player to a location connected to the player location in a
	 * given direction.
	 * 
	 * @param direction
	 *            The direction.
	 * @return true if the player was correctly moved; false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'direction' cannot be null.
	 * @throws NoConnectedLocationException
	 *             There is no connected location to the player location in
	 *             direction .
	 */
	public boolean movePlayer(Direction direction) {
		if (direction == null)
			throw new IllegalArgumentException();

		if (currentLocation.hasConnectedLocation(direction)) {
			currentLocation = currentLocation.getConnectedLocation(direction);
			if (currentLocation.hasExitThreshold()) {
				if (playerInventory.getTotalValue() >= currentLocation
						.getExitThreshold()) {
					this.eventFlag = true;
					gameEvents.add(currentLocation.getExitMessage());
					end();
				}
			}
			return true;
		} else
			throw new NoConnectedLocationException(
					"Error: NoConnectedLocationException");
	}

	/**
	 * Checks whether there are new events to report from the game.
	 * 
	 * @return true if there are events to report; false otherwise.
	 */
	public boolean hasEvents() {
		return eventFlag;
	}

	/**
	 * Reports the new events from the game. These 'events' are implemented as a
	 * simple text that can be shown to the player, adding some extra
	 * information to the result of a previously executed command (i.e. the exit
	 * message of a location where the game ends, after a Go command).
	 * 
	 * @return The events.
	 * @throws NoGameEventsException
	 *             The game has no new events to report.
	 */
	public String reportEvents() {
		if (gameEvents.isEmpty())
			throw new NoGameEventsException("Error: NoGameEventsException");
		String temp = "";
		boolean firstElementReaded = false;
		for (String it : gameEvents) {
			if (!firstElementReaded) {
				temp += it;
				firstElementReaded = true;
			} else {
				temp += LINE_SEPARATOR + it;
			}
		}
		return temp;
	}

	/**
	 * Clears the events to report from the game .
	 */
	public void clearEvents() {
		gameEvents.clear();
	}

	/**
	 * Reports the description of the player location.
	 * 
	 * @return The description.
	 */
	public String reportLocationDescription() {
		return currentLocation.getDescription();
	}

	/**
	 * Reports the name of the player location.
	 * 
	 * @return The name.
	 */
	public String reportLocationName() {
		return currentLocation.getName();
	}

	/**
	 * Reports all the items of the player location.
	 * 
	 * @return The set of items.
	 */
	public Set<Item> reportAllLocationItems() {
		return currentLocation.getAllItems();
	}

	/**
	 * Moves an item from the player location to the player inventory.
	 * 
	 * @param item
	 *            The item.
	 * @return true if the item was correctly moved; false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 * @throws ItemAlreadyInRepositoryException
	 *             The item is already in this repository.
	 * @throws ItemNotInRepositoryException
	 *             The item is not in this repository.
	 */
	public boolean moveItemFromLocationToInventory(Item item) {
		if (item == null)
			throw new IllegalArgumentException();

		if (!playerInventory.hasItem(item)) {
			if (currentLocation.hasItem(item)) {
				playerInventory.addItem(item);
				currentLocation.removeItem(item);
				return true;
			} else
				throw new ItemNotInRepositoryException(
						"Error: ItemNotInRepositoryException");
		} else
			throw new ItemAlreadyInRepositoryException(
					"Error: ItemAlreadyInRepositoryException");
	}

	/**
	 * Moves an item from the player inventory to the player location.
	 * 
	 * @param item
	 *            The item.
	 * @return true if the item was correctly moved; false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 * @throws ItemAlreadyInRepositoryException
	 *             The item is already in this repository.
	 * @throws ItemNotInRepositoryException
	 *             The item is not in this repository.
	 */
	public boolean moveItemFromInventoryToLocation(Item item) {
		if (item == null)
			throw new IllegalArgumentException();

		if (playerInventory.hasItem(item)) {
			if (!currentLocation.hasItem(item)) {
				currentLocation.addItem(item);
				playerInventory.removeItem(item);
				return true;
			} else
				throw new ItemAlreadyInRepositoryException(
						"Error: ItemAlreadyInRepositoryException");
		} else
			throw new ItemNotInRepositoryException(
					"Error: ItemNotInRepositoryException");
	}

	/**
	 * Checks whether an specific item is in the player inventory.
	 * 
	 * @param item
	 *            The item.
	 * @return true if the item is in the player inventory; false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 */
	public boolean isItemInInventory(Item item) {
		if (item == null) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		return playerInventory.hasItem(item);
	}

	/**
	 * Checks whether an specific item is in the player location.
	 * 
	 * @param item
	 *            The item.
	 * @return true if the item is in the player location; false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 */
	public boolean isItemInLocation(Item item) {
		if (item == null) {
			throw new IllegalArgumentException(
					"Error: IllegalArgumentException");
		}
		return currentLocation.hasItem(item);
	}

	/**
	 * Gets items with a common name from the player inventory.
	 * 
	 * @param name
	 *            The common name.
	 * @return The set of items with a common name.
	 * @throws IllegalArgumentException
	 *             The argument 'name' cannot be null.
	 */
	public Set<Item> getItemsFromInventory(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		return playerInventory.getItems(name);
	}

	/**
	 * Reports all the items of the player inventory.
	 * 
	 * @return The set of items.
	 */
	public Set<Item> reportAllInventoryItems() {
		return playerInventory.getAllItems();
	}

	/**
	 * Gets items with a common name from the player location.
	 * 
	 * @param name
	 *            The common name.
	 * @return The set of items with a common name.
	 * @throws IllegalArgumentException
	 *             The argument 'name' cannot be null.
	 */
	public Set<Item> getItemsFromLocation(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		return currentLocation.getItems(name);
	}

	/**
	 * Checks whether the game is ended.
	 * 
	 * @return true if the game is ended; false otherwise.
	 */
	public boolean isEnded() {
		return quitFlag;
	}

	/**
	 * Ends the game.
	 */
	public void end() {
		quitFlag = true;
	}

	/**
	 * Returns a String representation for this object
	 * 
	 * @see Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName() + "[" + title + "]";
	}

	/**
	 * Transforms a set into a item String list
	 * 
	 * @param set
	 *            The item set
	 * @param flagValue
	 *            The value flag, used to show item value or not
	 * @param flagWeight
	 *            The weight flag, used to show item weight or not
	 * @return The String list
	 */
	public String setToString(Set<Item> set, boolean flagValue,
			boolean flagWeight) {
		if (set == null)
			throw new IllegalArgumentException();

		boolean firstItemReaded = false;
		Iterator<Item> itr = set.iterator();
		String temp = "";
		Item it;

		while (itr.hasNext()) {
			it = itr.next();
			if (!firstItemReaded) {
				temp += LINE_SEPARATOR + it.getName();
				if (flagValue) {
					temp += " [Value(" + it.getValue() + ")";
				}
				if (flagWeight) {
					temp += " Weight(" + it.getWeight() + ")]";
				}
				firstItemReaded = true;
			} else {
				temp += it.getName();
				if (flagValue) {
					temp += " [Value(" + it.getValue() + ")";
				}
				if (flagWeight) {
					temp += " Weight(" + it.getWeight() + ")]";
				}
			}
			temp += LINE_SEPARATOR;
		}
		return temp;
	}

	/**
	 * Sets the special game for the game
	 * 
	 * @param specialHelp
	 *            The special help String
	 */
	public void setSpecialHelp(String specialHelp) {
		if (specialHelp == null)
			throw new IllegalArgumentException();

		this.specialHelp = specialHelp;
	}

	/**
	 * Returns the special game help
	 * 
	 * @return The special help String
	 */
	public String getSpecialHelp() {
		return specialHelp;
	}

	/**
	 * Counts the number of executed Commands
	 * 
	 * @param command
	 *            The executed command
	 */
	public void commandCount(Command command) {
		if (command == null)
			throw new IllegalArgumentException();

		if (command instanceof CloseCommand) {
			if (commandCount.get("CloseCommand") != null) {
				int closeCommandCounter = commandCount.get("CloseCommand");
				closeCommandCounter++;
				commandCount.put("CloseCommand", closeCommandCounter);
			} else
				commandCount.put("CloseCommand", 1);
		} else if (command instanceof DropCommand) {
			if (commandCount.get("DropCommand") != null) {
				int dropCommandCounter = commandCount.get("DropCommand");
				dropCommandCounter++;
				commandCount.put("DropCommand", dropCommandCounter);
			} else
				commandCount.put("DropCommand", 1);
		} else if (command instanceof ExamineCommand) {
			if (commandCount.get("ExamineCommand") != null) {
				int examineCommandCounter = commandCount.get("ExamineCommand");
				examineCommandCounter++;
				commandCount.put("ExamineCommand", examineCommandCounter);
			} else
				commandCount.put("ExamineCommand", 1);
		} else if (command instanceof GoCommand) {
			if (commandCount.get("GoCommand") != null) {
				int goCommandCounter = commandCount.get("GoCommand");
				goCommandCounter++;
				commandCount.put("GoCommand", goCommandCounter);
			} else
				commandCount.put("GoCommand", 1);
		} else if (command instanceof HelpCommand) {
			if (commandCount.get("HelpCommand") != null) {
				int helpCommandCounter = commandCount.get("HelpCommand");
				helpCommandCounter++;
				commandCount.put("HelpCommand", helpCommandCounter);
			} else
				commandCount.put("HelpCommand", 1);
		} else if (command instanceof LookCommand) {
			if (commandCount.get("LookCommand") != null) {
				int lookCommandCounter = commandCount.get("LookCommand");
				lookCommandCounter++;
				commandCount.put("LookCommand", lookCommandCounter);
			} else
				commandCount.put("LookCommand", 1);
		} else if (command instanceof OpenCommand) {
			if (commandCount.get("OpenCommand") != null) {
				int openCommandCounter = commandCount.get("OpenCommand");
				openCommandCounter++;
				commandCount.put("OpenCommand", openCommandCounter);
			} else
				commandCount.put("OpenCommand", 1);
		} else if (command instanceof QuitCommand) {
			if (commandCount.get("QuitCommand") != null) {
				int quitCommandCounter = commandCount.get("QuitCommand");
				quitCommandCounter++;
				commandCount.put("QuitCommand", quitCommandCounter);
			} else
				commandCount.put("QuitCommand", 1);
		} else if (command instanceof TakeCommand) {
			if (commandCount.get("TakeCommand") != null) {
				int takeCommandCounter = commandCount.get("TakeCommand");
				takeCommandCounter++;
				commandCount.put("TakeCommand", takeCommandCounter);
			} else
				commandCount.put("TakeCommand", 1);
		} else if (command instanceof UndoCommand) {
			if (commandCount.get("UndoCommand") != null) {
				int undoCommandCounter = commandCount.get("UndoCommand");
				undoCommandCounter++;
				commandCount.put("UndoCommand", undoCommandCounter);
			} else
				commandCount.put("UndoCommand", 1);
		}

		if (commandCount.get("Command") != null) {
			int totalNumberOfCommands = commandCount.get("Command");
			totalNumberOfCommands++;
			commandCount.put("Command", totalNumberOfCommands);
		} else
			commandCount.put("Command", 1);

	}

	/**
	 * Returns the total number of executed commands for statistic purpose
	 * 
	 * @return The total number of executed commands
	 */
	public int getTotalNumberOfExecutedCommands() {
		if (commandCount.get("Command") != null) {
			return commandCount.get("Command");
		} else
			return 0;
	}

	/**
	 * Return the number of executions for the command given
	 * 
	 * @param command
	 *            The command
	 * @return The number of executions
	 */
	public int getNumberOfExecutedCommands(String command) {
		if (command == null)
			throw new IllegalArgumentException();

		if (commandCount.get(command) != null) {
			return commandCount.get(command);
		} else
			return 0;
	}

	/**
	 * Calculates the command execution percentage
	 * 
	 * @param command
	 *            The command
	 * @return The percentage
	 */
	public int getCommandPercentage(String command) {
		if (command == null)
			throw new IllegalArgumentException();

		if (getTotalNumberOfExecutedCommands() != 0) {
			int temp = (getNumberOfExecutedCommands(command) * 100)
					/ getTotalNumberOfExecutedCommands();
			return temp;
		} else
			return 0;
	}

	/**
	 * Returns the command Statistics
	 * 
	 * @return The command statistics
	 */
	public String getCommandStatistics() {
		String temp = "";
		if (getTotalNumberOfExecutedCommands() != 0) {
			temp += "CloseCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("CloseCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("CloseCommand") + LINE_SEPARATOR;
			temp += "DropCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("DropCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("DropCommand") + LINE_SEPARATOR;
			temp += "ExamineCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("ExamineCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("ExamineCommand") + LINE_SEPARATOR;
			temp += "GoCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("GoCommand") + LINE_SEPARATOR
					+ "\t" + "Percentage: " + getCommandPercentage("GoCommand")
					+ LINE_SEPARATOR;
			temp += "HelpCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("HelpCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("HelpCommand") + LINE_SEPARATOR;
			temp += "LookCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("LookCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("LookCommand") + LINE_SEPARATOR;
			temp += "OpenCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("OpenCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("OpenCommand") + LINE_SEPARATOR;
			temp += "QuitCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("QuitCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("QuitCommand") + LINE_SEPARATOR;
			temp += "TakeCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("TakeCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("TakeCommand") + LINE_SEPARATOR;
			temp += "UndoCommand: " + LINE_SEPARATOR + "\t" + "Executions: "
					+ getNumberOfExecutedCommands("UndoCommand")
					+ LINE_SEPARATOR + "\t" + "Percentage: "
					+ getCommandPercentage("UndoCommand") + LINE_SEPARATOR;

			temp += "TOTAL COMMAND NUMBER: "
					+ getTotalNumberOfExecutedCommands() + LINE_SEPARATOR;

			return temp;
		} else
			return "There is no executed commands yet";
	}
}
