package es.ucm.fdi.lps.p5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.ucm.fdi.lps.p5.Game.Direction;

public class Obstacle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The obstacle id.
	 */
	private String id;

	/**
	 * The obstacle name.
	 */
	private String name;

	/**
	 * The obstacle description.
	 */
	private String description;

	/**
	 * The obstacle error message.
	 */
	private String errorMsg;

	/**
	 * The item status. True = Active, False = Inactive
	 */
	private boolean status;

	/**
	 * The direction of the obstacle
	 */
	private Direction direction;

	/**
	 * The obstacle keys.
	 */
	private List<Item> boundedItems;

	/**
	 * The bound obstacles.
	 */
	private List<Obstacle> boundedObstacles;

	/**
	 * Constructs a obstacle using a given name, a given description and a given
	 * status
	 * 
	 * @param id
	 *            The obstacle id
	 * @param name
	 *            The name.
	 * @param description
	 *            The description.
	 * @param status
	 *            The status.
	 * @param errorMsg
	 *            The obstacle error message.
	 * @param direction
	 *            The direction where the obstacle resides
	 * @throws IllegalArgumentException
	 *             The arguments 'name' and 'description' cannot be null.
	 */
	public Obstacle(String id, String name, String description, boolean status,
			String errorMsg, Direction direction) {
		if ((id == null) || (name == null) || (description == null)
				|| (errorMsg == null) || (direction == null))
			throw new IllegalArgumentException();
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.errorMsg = errorMsg;
		this.direction = direction;

		boundedItems = new ArrayList<Item>();
		boundedObstacles = new ArrayList<Obstacle>();
	}

	/**
	 * Returns the id.
	 * 
	 * @return The id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the description.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the obstacle error message.
	 * 
	 * @return The error message.
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * Returns the obstacle status.
	 * 
	 * @return The obstacle status.
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * Returns the obstacle direction.
	 * 
	 * @return The obstacle direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Returns a list with the obstacle keys
	 * 
	 * @return The obstacle keys.
	 */
	public List<Item> getBoundedItems() {
		return boundedItems;
	}

	/**
	 * Return true if the obstacle have bounded items, false otherwise
	 * 
	 * @return true if the obstacle have bounded items, false otherwise
	 */
	public boolean hasBoundedItems() {
		return (!boundedItems.isEmpty());
	}

	/**
	 * Return true if the obstacle have bounded obstacles, false otherwise
	 * 
	 * @return true if the obstacle have bounded obstacles, false otherwise
	 */
	public boolean hasBoundedObstacles() {
		return (!boundedObstacles.isEmpty());
	}

	/**
	 * Returns the bounded obstacles.
	 * 
	 * @return The bounded obstacles.
	 */
	public List<Obstacle> getBoundedObstacles() {
		return boundedObstacles;
	}

	/**
	 * Add a key to the obstacle
	 * 
	 * @param item
	 *            The key to be added
	 * @return true if added; false otherwise
	 */
	public boolean addBoundedItem(Item item) {
		if ((item == null))
			throw new IllegalArgumentException();
		return (boundedItems.add(item));
	}

	/**
	 * Removes a key to the obstacle
	 * 
	 * @param item
	 *            The key to be removed
	 * @return true if removed; false otherwise
	 */
	public boolean removeBoundedItem(Item item) {
		if ((item == null))
			throw new IllegalArgumentException();
		return (boundedItems.remove(item));
	}

	/**
	 * Add a bounded obstacle to the obstacle
	 * 
	 * @param obstacle
	 *            The obstacle to be added
	 * @return true if added; false otherwise
	 */
	public boolean addBoundedObstacle(Obstacle obstacle) {
		if ((obstacle == null))
			throw new IllegalArgumentException();
		return (boundedObstacles.add(obstacle));
	}

	/**
	 * Remove a bounded obstacle of the obstacle
	 * 
	 * @param obstacle
	 *            The obstacle to be removed
	 * @return true if removed; false otherwise
	 */
	public boolean removeBoundedObstacle(Obstacle obstacle) {
		if ((obstacle == null))
			throw new IllegalArgumentException();
		return (boundedObstacles.remove(obstacle));
	}

	/**
	 * Change the obstacle Status. If true to false, if false to true.
	 */
	public void changeStatus() {
		status = !status;
		if (!boundedObstacles.isEmpty()) {
			Iterator<Obstacle> itr = boundedObstacles.iterator();
			Obstacle obstacle;

			while (itr.hasNext()) {
				obstacle = itr.next();
				obstacle.status = !obstacle.status;
			}
		}
	}

	/**
	 * Returns a String representation for this object: Obstacle[]. This is
	 * useful for debugging purposes.
	 * 
	 * @see Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getName() + "]";
	}

}
