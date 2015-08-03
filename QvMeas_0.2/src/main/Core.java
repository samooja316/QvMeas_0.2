package main;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDesktopPane;

import org.jfree.ui.RefineryUtilities;

import be.ac.ulb.gpib.GPIBDeviceIdentifier;



/*
 * This class is the implements the core logic of the system
 * Basic idea: own an Instrument-object, use the instrument, pass results to a file and the result console
 *
 * @author Aleksi Oja
 * @email alejee@live.com
 * @date 6/2015
 * @.version 0.1
 * 
 * 
 */
public class Core {
	
	
	//mainframe
	private JDesktopPane _mainPane;
	
	//system main window
	private Window _window;
	
	//io object
	private IOWrapper _io;
	
	//instrument to measure with
	private Instrument _instrument;
	
	//query to execute a measurement
	private Query _query;
	
	//meas initialized
	private boolean _measInitialized = false;
	 
	private ArrayList<Result> _results = new ArrayList<Result>();
	
	/*
	 * the Constructor
	 */
	public Core() {
				
		//creating io wrapper object
		_io = new IOWrapper(this);
		
		//creating the instrument
		try {
		_instrument = new Instrument(); 
		_instrument.setController(this);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}

		
		//creating window
		_window = new Window();
		_window.setController(this);
		_window.setVisible(true);
		
		
		//testing system state and printing info for user
		toConsole("Launching the system...\n");
		
		if (GPIBDeviceIdentifier.driverLoaded()) {
			toConsole("GPIBDriver loaded\n");
		} else {
			toConsole("GPIBDriver failure\n");
		}
	}
	
	
	/*
	 * Method for accessing Measurements done
	 * 
	 * @version 	0.1
	 * @since 		0.2
	 * @.pre		true
	 * @.post		true
	 * @.result		ArrayList<Result> (empty or not)
	 */
	public ArrayList<Result> getResults() {
		return _results;
	}
	
	
	/*
	 * Method for getting last (and greatest) index of the
	 * getResults() -array
	 * 
	 * @version 	0.1
	 * @since		0.2
	 * 
	 * @.pre		true
	 * @.post		true
	 * @.return		(id of the last measurement's result + 1)
	 */
	public int getNextResultIndex() {
		if (getResults().size()>0) {
			return getResults().get(_results.size()-1).getMeasurementId() + 1;
		} else {
			return 1;
		}
	}
	
	
	/*
	 * Call IOWrapper -object to choose outputfile path for result data
	 * 
	 * @version 0.1
	 * @since 0.1
	 * @.pre true
	 * @.post (default filepath for results is set)
	 * 
	 */
	public void chooseFile(String baseName) {
		String file = _io.giveFilePath(baseName);		
		if(file != null) _window.writeFilePath(file);
	}
	
	/*
	 * Call IOWrapper -object to choose default directory for results
	 * 
	 * @version 0.1
	 * @since 0.2
	 * @.pre true
	 * @.post (filepath for res data will be set)
	 * 
	 */
	public void chooseDirectory() {
		_io.setDefaultDirectory();
		System.out.println(_io.getDefaultDirectory());
	}
	
	/*
	 * Get the default directory for results
	 * 
	 * @version 	0.1
	 * @since 		0.2
	 * @return 		default directory path, String 
	 * @.pre 		true
	 * @.post 		true
	 * 
	 */
	public String getDefaultDirectory() {
		return _io.getDefaultDirectory();
	}
	
	/*
	 * 
	 * @version 0.1
	 * @since 0.2
	 * @return TRUE if (Instrument object set) else return FALSE
	 * @.pre true
	 * @.post true
	 * 
	 */
	public boolean instrumentCreated() {
		return _instrument != null;
	}
	
	
	
	
	/*
	 * initMeas-method creates new Query object and sets the controller ready to
	 * measure with the Instrument
	 * 
	 * @version 0.1
	 * @since 0.1
	 * @.pre instrumentCreated == TRUE  AND 
	 * 		params != null AND params is right form:
	 * 		for meas params ArrayList<String>: 
	 * 			params.get(0) = current, 
	 * 			params.get(1) =	step, 
	 * 			params.get(2) = nosteps, 
	 * 			params.get(3) = currentcompliance, 
	 * 			params.get(4) = voltagecompliance, 
	 * 			params.get(5) = xMin, 
	 * 			params.get(6) = xMax, 
	 * 			params.get(7) = yMin, 
	 * 			params.get(8) = yMax, 
	 * 			params.get(9) = yLinLog
	 * @.post (Measurement will be initialized with proper Query object)
	 * 
	 */
	public void initMeas(ArrayList<String> params) {
		_query = new Query(params);
		_instrument.init(_query);		
		_window.setStartStatus(true);
		toConsole(_query.toString());
		_measInitialized = true;	
	}
	
	
	
	
	/*
	 * Method for starting a measurement
	 * 
	 * @version 0.1
	 * @since 0.1
	 * @throws measNotInitException 
	 * @.pre true
	 * @.post (Measurement will be launched)
	 * 
	 */
	public void start() throws MeasNotInitException {		
		if(_measInitialized) {
			_instrument.measure();
			_window.setInitStatus(false);
			_window.setStartStatus(false);
			_window.setStopStatus(true);
			_measInitialized = false;
		} 
		else { 
			throw new MeasNotInitException();			
		}
	}
	
	
	
	
	/*
	 * Method for stopping measurement execution
	 * 
	 * @version 0.1
	 * @since 0.1 
	 * @.pre true
	 * @.post (Measurement will be stopped)
	 */
	public void stopMeas() {
		_window.setStartStatus(false);
		_window.setStopStatus(false);
		_window.setInitStatus(true);
		_instrument.stop();		
	}
	
	

	
	/*
	 * Method for handling result object data 
	 * 
	 * @version 0.1
	 * @since 0.2
	 *  
	 * @.pre resData != null
	 * @.post (Result will be written to the file specified AND
	 * 			Result will be printed to the result console (from the IOWrapper) AND
	 * 			Result will be added to the list keeping results in Core)
	 */	
	public void resultReady(Result res) {
		System.out.println("core got a result object..");	
		_window.setInitStatus(true);
		_window.setStartStatus(false);
		_window.setStopStatus(false);
		_results.add(res);
		_io.writeData(_window.readFilePath(), res);		
		_window.drawGraph(res, GraphType.CV);
		_window.drawGraph(res, GraphType.VT);
	}
	
	
	/*
	 * methods to print to the console
	 * 
	 * @version 0.1
	 * @since 0.1
	 *  
	 * @.pre print != null
	 * @.post (print will be written to the console)
	 */
	public void toResultConsole(String print) {
		_window.printToResultConsole(print);
	}
	
	
	
	
	/*
	 * methods to print to the result console
	 * 
	 * @version 0.1
	 * @since 0.1
	 *  
	 * @.pre print != null
	 * @.post (print will be written to the result console)
	 */
	public void toConsole(String print) {
		_window.printToConsole(print);
	}
}
