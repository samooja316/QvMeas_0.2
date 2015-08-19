package main;

/*
 * Exception used when trying to measure without initializing the
 * measurement first
 * 
 * @version		0.1
 * @since		0.1
 */
public class MeasNotInitException extends Exception {
	//serial version id
	private static final long serialVersionUID = 1L;
	
	public MeasNotInitException() {}
	
	public MeasNotInitException(String message) {
		super(message);
	}
}
