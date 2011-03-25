package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when there is not an item in an item
 * repository and someone tries to remove it.
 */
@SuppressWarnings("serial")
public class ItemNotInRepositoryException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public ItemNotInRepositoryException(String message) {

	}

}
