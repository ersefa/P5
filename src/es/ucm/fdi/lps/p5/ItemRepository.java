package es.ucm.fdi.lps.p5;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import es.ucm.fdi.lps.p5.exception.ItemAlreadyInRepositoryException;
import es.ucm.fdi.lps.p5.exception.ItemNotInRepositoryException;

/**
 * Represents a repository of items, as the player inventory or the contents of
 * a location. Important: The repository must not contain repetitions, and items
 * must be stored in the same order in which they were added to the repository.
 */
public class ItemRepository implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The items contained in the repository
	 */
	private Set<Item> items;

	/**
	 * Constructs a new, empty repository of items.
	 */
	public ItemRepository() {
		items = new LinkedHashSet<Item>();
	}

	/**
	 * Adds a new item to the repository.
	 * 
	 * @param item
	 *            The item.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 * @throws ItemAlreadyInRepositoryException
	 *             The item is already in this repository.
	 */
	public void addItem(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (items.contains(item))
			throw new ItemAlreadyInRepositoryException(
					"Error: ItemAlreadyInRepositoryException");
		items.add(item);
	}

	/**
	 * Gets items by name from the repository.
	 * 
	 * @param name
	 *            The name.
	 * @return The set of items.
	 * @throws IllegalArgumentException
	 *             The String name cannot be null.
	 */
	public Set<Item> getItems(String name) {
		if (name == null)
			throw new IllegalArgumentException();

		Set<Item> set = new LinkedHashSet<Item>();
		Iterator<Item> itr = items.iterator();
		Item it;

		while (itr.hasNext()) {
			it = itr.next();
			if (it.getName().equals(name)) {
				set.add(it);
			}
		}
		return set;
	}

	/**
	 * Gets all items from the repository.
	 * 
	 * @return The set of items.
	 */
	public Set<Item> getAllItems() {
		return items;
	}

	/**
	 * Gets the total value of the items of this repository.
	 * 
	 * @return The total value.
	 */
	public int getTotalValue() {
		int totalValue = 0;

		Iterator<Item> itr = items.iterator();
		while (itr.hasNext()) {
			totalValue += itr.next().getValue();
		}

		return totalValue;
	}
	
	/**
	 * Gets the total weight of the items of this repository.
	 * 
	 * @return The total weight.
	 */
	public int getTotalWeight() {
		int totalWeight = 0;

		Iterator<Item> itr = items.iterator();
		while (itr.hasNext()) {
			totalWeight += itr.next().getWeight();
		}

		return totalWeight;
	}

	/**
	 * Removes an item from the repository.
	 * 
	 * @param item
	 *            The item.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 * @throws ItemNotInRepositoryException
	 *             The item is not in this repository.
	 */
	public void removeItem(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		if (!items.contains(item))
			throw new ItemNotInRepositoryException(
					"Error: ItemNotInRepositoryException");
		items.remove(item);
	}

	/**
	 * Checks whether the repository is empty.
	 * 
	 * @return true if the repository is empty; false otherwise.
	 */
	public boolean isEmpty() {
		return (items.isEmpty());
	}

	/**
	 * Checks whether the repository contains an specific item.
	 * 
	 * @param item
	 *            The item.
	 * @return true if the repository contains the item; false otherwise.
	 * @throws IllegalArgumentException
	 *             The argument 'item' cannot be null.
	 */
	public boolean hasItem(Item item) {
		if (item == null)
			throw new IllegalArgumentException();
		return items.contains(item);
	}

	/**
	 * Returns a String representation for this object: ItemRepository[, , ...].
	 * This is useful for debugging purposes.
	 * 
	 * @see Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName() + "[]";
	}
}
