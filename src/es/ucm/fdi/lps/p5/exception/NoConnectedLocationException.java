package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when there is no connected location to
 * the player location in a given direction and someone asks for it.
 */
@SuppressWarnings("serial")
public class NoConnectedLocationException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public NoConnectedLocationException(String message) {

	}

}
