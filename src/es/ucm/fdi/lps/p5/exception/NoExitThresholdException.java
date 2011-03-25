package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when a location has no exit threshold and
 * someone asks for it.
 */
@SuppressWarnings("serial")
public class NoExitThresholdException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public NoExitThresholdException(String message) {

	}

}
