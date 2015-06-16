package main;

public class MeasNotInitException extends Exception {
	//serial version id
	private static final long serialVersionUID = 1L;
	public MeasNotInitException() {}
	public MeasNotInitException(String message) {
		super(message);
	}
}
