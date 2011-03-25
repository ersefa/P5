package es.ucm.fdi.lps.p5.exception;

/**
 * Represents an exception that occurs when a game definition is invalid and
 * someone tries to create a new game with it.
 */
@SuppressWarnings("serial")
public class InvalidGameDefinitionException extends Exception {

	/**
	 * Constructs the exception with an explanatory message.
	 * 
	 * @param message
	 *            The error message.
	 */
	public InvalidGameDefinitionException(String message) {
		System.out.println(message);
	}

}
