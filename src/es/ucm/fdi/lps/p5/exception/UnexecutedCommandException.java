package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when a command has not been executed and
 * someone tries to undo it.
 */
@SuppressWarnings("serial")
public class UnexecutedCommandException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public UnexecutedCommandException(String message) {

	}

}
