package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when there are not executed commands in
 * the command history and someone tries to remove the newest or oldest executed
 * command.
 */
@SuppressWarnings("serial")
public class NoExecutedCommandsException extends RuntimeException {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public NoExecutedCommandsException(String message) {

	}

}
