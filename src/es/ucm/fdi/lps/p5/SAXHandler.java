package es.ucm.fdi.lps.p5;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.ucm.fdi.lps.p5.Game.Direction;
import es.ucm.fdi.lps.p5.exception.InvalidBoundedItemException;
import es.ucm.fdi.lps.p5.exception.InvalidBoundedObstacleException;

public class SAXHandler extends DefaultHandler {

	private Game game;

	private Location tempLocation;
	private String locationId;
	private String locationName;
	private String locationDescription;
	private int locationThreshold;
	private String locationExitMsg;
	private boolean locationExitThresholdFlag;

	private Item tempItem;
	private String itemID;
	private String itemName;
	private int itemValue;
	private int itemWeight;
	private String itemDescription;

	private EnumMap<Direction, String> tempDirection;
	private Obstacle tempObstacle;
	private String obstacleId;
	private String obstacleName;
	private String obstacleDescription;
	private boolean obstacleStatus;
	private String obstacleErrorMsg;
	private String itemRefId;
	private String obstacleRefId;

	private String tagText;
	private String actualTag;
	private Direction actualConnectionDir;

	private Map<String, Location> tempLocationList;
	private Map<String, EnumMap<Direction, String>> tempConnectionList;
	private Map<String, Item> tempItemList;
	private Map<String, Obstacle> tempObstacleList;
	private Hashtable<Obstacle, ArrayList<String>> tempItemRefList;
	private Hashtable<Obstacle, ArrayList<String>> tempObstacleRefList;

	private ArrayList<String> tempItemRefs;
	private ArrayList<String> tempObstacleRefs;

	/**
	 * Handler constructor
	 * 
	 * @param game
	 *            The game.
	 */
	public SAXHandler(Game game) {
		this.game = game;
		tempDirection = new EnumMap<Direction, String>(Direction.class);
		tempConnectionList = new Hashtable<String, EnumMap<Direction, String>>();
		tempLocationList = game.getLocations();
		tempItemList = new Hashtable<String, Item>();
		tempObstacleList = new Hashtable<String, Obstacle>();
		tempItemRefList = new Hashtable<Obstacle, ArrayList<String>>();
		tempObstacleRefList = new Hashtable<Obstacle, ArrayList<String>>();
		tempItemRefs = new ArrayList<String>();
		tempObstacleRefs = new ArrayList<String>();
	}

	/**
	 * Method called when a start tag is detected
	 */
	public void startElement(String namespace, String sName, String qName,
			Attributes atrs) throws SAXException {
		actualTag = qName;
		if (qName.equalsIgnoreCase("game")) {
			if (atrs != null) {
				game.setTitle(atrs.getValue(0));
				game.setAuthor(atrs.getValue(1));
			}
		}

		if (qName.equalsIgnoreCase("location")) {
			if (atrs != null) {
				locationExitThresholdFlag = false;
				locationId = atrs.getValue(0);
				locationName = atrs.getValue(1);
				if ((atrs.getValue(2) != null) && (atrs.getValue(3) != null)) {
					locationThreshold = Integer.parseInt(atrs.getValue(2));
					locationExitMsg = atrs.getValue(3);
					locationExitThresholdFlag = true;
				}
			}
		}

		if (qName.equalsIgnoreCase("item")) {
			if (atrs != null) {
				itemID = atrs.getValue(0);
				itemName = atrs.getValue(1);
				itemValue = Integer.parseInt(atrs.getValue(2));
				itemWeight = Integer.parseInt(atrs.getValue(3));
			}
		}

		if (qName.equalsIgnoreCase("connection")) {
			if (atrs != null) {
				String shortDirection = atrs.getValue(0);
				for (Direction dir : Direction.values()) {
					if (dir.getKeyword().equals(shortDirection)) {
						tempDirection.put(dir, atrs.getValue(1));
						// Direction for obstacles
						actualConnectionDir = dir;
						break;
					}
				}
			}
		}

		if (qName.equalsIgnoreCase("obstacle")) {
			if (atrs != null) {
				obstacleId = atrs.getValue(0);
				obstacleName = atrs.getValue(1);
				obstacleStatus = Boolean.parseBoolean(atrs.getValue(2));
				obstacleErrorMsg = atrs.getValue(3);
			}
		}

		if (qName.equalsIgnoreCase("item-ref")) {
			if (atrs != null) {
				itemRefId = atrs.getValue(0);
			}
		}

		if (qName.equalsIgnoreCase("obstacle-ref")) {
			if (atrs != null) {
				obstacleRefId = atrs.getValue(0);
			}
		}
	}

	/**
	 * Method called between start and end tag
	 */
	public void characters(char buf[], int offset, int len) {
		tagText = new String(buf, offset, len);
		tagText = tagText.replaceAll("\\t", "");
		tagText = tagText.replaceAll("\\n", "");

		if (actualTag.equals("location")) {
			locationDescription = tagText;
			if (locationExitThresholdFlag) {
				tempLocation = new Location(locationId, locationName,
						locationDescription, locationThreshold, locationExitMsg);
			} else {
				tempLocation = new Location(locationId, locationName,
						locationDescription);
			}
		}
	}

	/**
	 * Method called when a finish tag is detected
	 */
	@SuppressWarnings("unchecked")
	public void endElement(String uri, String localName, String qName) {
		if (qName.equals("game")) {
			game.setDescription(tagText);

			linkItemsToLocation();
			linkConnectionsToLocation();
			linkObstaclesToLocation();
			linkBoundItemsToObstacle();
			linkBoundObstaclesToObstacle();
		}

		if (qName.equals("location")) {

			if (game.getCurrentLocation() == null)
				game.setCurrentLocation(tempLocation);

			tempLocationList.put(tempLocation.getId(), tempLocation);

			// Add connections and clean temporal connections
			tempConnectionList.put(tempLocation.getId(), tempDirection.clone());
			tempDirection.clear();
		}

		if (qName.equals("item")) {
			itemDescription = tagText;
			tempItem = new Item(itemID, itemName, itemDescription, itemValue,
					itemWeight);
			tempLocation.addItem(tempItem);

			tempItemList.put(tempItem.getId(), tempItem);
		}

		if (qName.equals("connection")) {

		}

		if (qName.equals("obstacle")) {
			obstacleDescription = tagText;
			tempObstacle = new Obstacle(obstacleId, obstacleName,
					obstacleDescription, obstacleStatus, obstacleErrorMsg,
					actualConnectionDir);

			tempObstacleList.put(tempLocation.getId(), tempObstacle);

			if (!tempItemRefs.isEmpty()) {
				tempItemRefList.put(tempObstacle,
						(ArrayList<String>) tempItemRefs.clone());
			}
			tempItemRefs.clear();

			if (!tempObstacleRefs.isEmpty()) {
				tempObstacleRefList.put(tempObstacle,
						(ArrayList<String>) tempObstacleRefs.clone());
			}
			tempObstacleRefs.clear();
		}

		if (qName.equals("item-ref")) {
			tempItemRefs.add(itemRefId);
		}

		if (qName.equals("obstacle-ref")) {
			tempObstacleRefs.add(obstacleRefId);
		}

		if (qName.equalsIgnoreCase("help")) {
			game.setSpecialHelp(tagText);
		}
	}

	/**
	 * Links Items to Locations when the parse is finished
	 */
	private void linkItemsToLocation() {
		for (Location loc : tempLocationList.values()) {
			if (tempItemList.containsKey(loc.getId())) {
				loc.addItem(tempItemList.get(loc.getId()));
			}
		}
	}
	
	/**
	 * Links Obstacle to Locations when the parse is finished
	 */
	private void linkObstaclesToLocation() {
		for (Location loc : tempLocationList.values()) {
			if (tempObstacleList.containsKey(loc.getId())) {
				Obstacle obstacle = tempObstacleList.get(loc.getId());
				Direction direction = obstacle.getDirection();
				loc.addObstacle(direction, obstacle);
			}
		}
	}

	/**
	 * Links the locations when the parse is finished
	 */
	private void linkConnectionsToLocation() {
		for (Location loc : tempLocationList.values()) {
			if (tempConnectionList.containsKey(loc.getId())) {
				EnumMap<Direction, String> connection = tempConnectionList
						.get(loc.getId());

				for (Direction dir : connection.keySet()) {
					String targetLocationName = connection.get(dir);
					if (tempLocationList.containsKey(targetLocationName)) {
						Location targetLocation = tempLocationList
								.get(targetLocationName);
						loc.setConnection(dir, targetLocation);
					}
				}
			}
		}
	}

	/**
	 * Links bounded items to Obstacles when the parse is finished
	 */
	private void linkBoundItemsToObstacle() {
		boolean founded = false;
		for (Obstacle obs : tempItemRefList.keySet()) {
			ArrayList<String> items = tempItemRefList.get(obs);

			Iterator<String> itr = items.iterator();
			String itemName;
			
			while (itr.hasNext()) {
				founded = false;
				itemName = itr.next();
				if (tempItemList.containsKey(itemName)) {
					Item it = tempItemList.get(itemName);
					tempObstacle.addBoundedItem(it);
					founded = true;
				}
			}
			if (!founded) throw new InvalidBoundedItemException("InvalidBoundedItemException");
		}
	}
	
	/**
	 * Links bounded obstacles to Obstacles when the parse is finished
	 */
	private void linkBoundObstaclesToObstacle() {
		boolean founded = false;
		for (Obstacle obs : tempObstacleRefList.keySet()) {
			ArrayList<String> obstacles = tempObstacleRefList.get(obs);

			Iterator<String> itr = obstacles.iterator();
			String obstacleName;

			while (itr.hasNext()) {
				obstacleName = itr.next();
				founded = false;
				for (Obstacle obstacle : tempObstacleList.values()) {
					if (obstacle.getId().equals(obstacleName)) {
						tempObstacle.addBoundedObstacle(obstacle);
						founded = true;
					}
				}
			}
			if (!founded) throw new InvalidBoundedObstacleException("InvalidBoundedObstacleException");
		}
	}
}
