package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when the execution of a command had no
 * result and someone asks for it.
 */
@SuppressWarnings("serial")
public class NoCommandResultException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public NoCommandResultException(String message) {

	}

}
