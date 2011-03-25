package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when there is already an item in an item
 * repository and someone tries to add it.
 */
@SuppressWarnings("serial")
public class ItemAlreadyInRepositoryException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public ItemAlreadyInRepositoryException(String message) {

	}
}
