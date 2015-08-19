package main;

/*
 * Exception class for the input values that can't be 
 * given for the measurement system
 * 
 * @version		0.1
 * @since		0.2
 */
public class ValuesNotValidException extends Exception {

	private static final long serialVersionUID = 143L;

	public ValuesNotValidException() {}
	public ValuesNotValidException(String message) {
		super(message);
	}
}
