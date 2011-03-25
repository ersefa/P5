package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when there is no next command to parse
 * and someone asks for it.
 */
@SuppressWarnings("serial")
public class NoNextCommandException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public NoNextCommandException(String message) {

	}

}
