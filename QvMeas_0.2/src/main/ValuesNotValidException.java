package main;

public class ValuesNotValidException extends Exception {

	private static final long serialVersionUID = 143L;

	public ValuesNotValidException() {}
	public ValuesNotValidException(String message) {
		super(message);
	}
}
