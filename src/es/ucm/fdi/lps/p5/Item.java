package es.ucm.fdi.lps.p5;

import java.io.Serializable;

/**
 * Represents an item that can be placed in the player inventory or in the
 * locations of the game.
 */
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The item id.
	 */
	private String id;

	/**
	 * The item name.
	 */
	private String name;

	/**
	 * The item description.
	 */
	private String description;

	/**
	 * The item value.
	 */
	private int value;

	/**
	 * The item weight.
	 */
	private int weight;

	/**
	 * Constructs an item using a given name, a given description and a given
	 * value.
	 * 
	 * @param id
	 *            The item id.
	 * @param name
	 *            The name.
	 * @param description
	 *            The description.
	 * @param value
	 *            The value.
	 * @throws IllegalArgumentException
	 *             The arguments 'name' and 'description' cannot be null.
	 */
	public Item(String id, String name, String description, int value,
			int weight) {
		if ((name == null) || (description == null))
			throw new IllegalArgumentException();
		this.id = id;
		this.name = name;
		this.description = description;
		this.value = value;
		this.weight = weight;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name.
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
	 * Returns the value.
	 * 
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns the weight.
	 * 
	 * @return The weight.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Returns a String representation for this object: Item[]. This is useful
	 * for debugging purposes.
	 * 
	 * @see Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getName() + "]";
	}
}
