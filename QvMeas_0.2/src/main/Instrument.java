package main;
import java.io.IOException;
import be.ac.ulb.gpib.*;


/*
 * This class using a measurement instrument (in tests "HP34401...etc")
 * The class provides methods to do low level operations with the measurement
 * instrument(s)
 * 
 * @author Tuukka Panula, tujupan@utu.fi
 * @version 0.1
 * @since 0.1
 * 
 */
 
public class Instrument {
	private Core controller;
	private GPIBDevice myDevice;// = new GPIBDevice(13,deviceIdentifier.getDriver());
	Measurement meas;
	public Instrument() throws IOException,InterruptedException {
		WindowsGPIBDriver windrvr = new WindowsGPIBDriver();
		if (GPIBDeviceIdentifier.driverLoaded()){
			System.out.println("Driver loaded");
		}
		myDevice = new GPIBDevice(1,windrvr);
		System.out.print("Connecting... ");
		myDevice.open(8.0F);
		System.out.println("done!");
	}
	
	public void measure() {
		controller.toConsole("Measuring... ");
		meas.start();
	}
	
	/*
	 * Method for creating a new measurement and initializing it with a query
	 * 
	 */
	public void init(Query query) {
		meas = new Measurement(myDevice,this,query,controller);
		controller.toConsole("Setting up... ");
		meas.initialize();
	}
	public void stop() {
		System.out.println("stopped!");
		controller.toConsole("stopped!\n");
		meas.interrupt();
	}

	public void measurementReady(Result res) {
		System.out.println("Instrument got the Result object");
		controller.resultReady(res);
	}
	
	public void setController(Core control) {
		controller = control;
	}
}
 
