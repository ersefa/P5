package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when a command has not been parsed and
 * someone tries to execute it or undo it.
 */
@SuppressWarnings("serial")
public class UnparsedCommandException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public UnparsedCommandException(String message) {

	}

}
