package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when the game has no new events to report
 * and someone asks for them.
 */
@SuppressWarnings("serial")
public class NoGameEventsException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public NoGameEventsException(String message) {

	}
}
