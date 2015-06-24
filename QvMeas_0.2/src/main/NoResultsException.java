package main;

/*
 * Exception class for empty result array
 * 
 * @author 		Aleksi Oja
 * @email		alejee@live.com
 * @version		0.1
 * @since		0.2
 */
public class NoResultsException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoResultsException() {}
	public NoResultsException(String message) {
		super(message);
	}
}


