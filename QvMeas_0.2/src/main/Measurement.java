package main;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;

import be.ac.ulb.gpib.*;



/*
 * 
 * Class for a single measurement: this class is the lowest level class which
 * in abstraction pile. This class gives the GPIB commands for the measuring hardware.
 * 
 * @author Tuukka Panula
 * @email tujupan@utu.fi
 * @date 6/2015
 * @.version 0.1
 * 
 */
public class Measurement extends Thread {
	
	GPIBDevice myDevice;
	String resData;
	Instrument instrument;
	Query query;
	Core controller;
	
	
	
	/*
	 * the constructor
	 * 
	 * @version 0.1
	 * @since 0.1
	 * @param    myDevice, GPIBDevice - low level interface to hardware
	 * 			 query, object containing all parameter necessary for a measurement
	 * 			 inst, Instrument -object for returning results
	 * 			 controller, Core-object for writing results to the consoles	
	 *  
	 */
	public Measurement(GPIBDevice myDevice, Instrument inst,Query query,Core controller){
		this.myDevice = myDevice;
		instrument = inst;
		this.query = query;
		this.controller=controller;
	}
	
	
	/*
	 * Method for running a measurement
	 * 
	 * @version 0.1
	 * @since 0.1
	 * @.pre myDevice != null AND controller != null AND instrument != null
	 * @.post (Measurement will be executed in its own thread, 
	 * 			GPIB commands will be sent to the hardware and the
	 * 			result will be passed to the Instrument-object)
	 * 
	 */
	public void run(){
		try{
			int measTime = (int) (Float.valueOf(query.getStep())*Integer.valueOf(query.getNumberOfSteps()));
			myDevice.writeCommand("MD ME1");
			try {
			    Thread.sleep(measTime*1000+3000);
			} catch(InterruptedException ex) {
				myDevice.writeCommand("ME4");
			}
		
			System.out.println("done!");
			controller.toConsole("done!\n");
			String resData = (myDevice.sendCommand("PR"));
			System.out.println("Print done!");
			controller.toConsole("Print done!\n");
			this.resData = resData;
			//data for result
			int id = controller.getNextResultIndex(); //id for new result 
		//	Result res = new Result(id, resData, query.getComments(), query.getCurrent());
			Result res = new Result(id, resData, query);
			System.out.println("Result no. "+res.getMeasurementId()+" ready\n");
			controller.toConsole("Result no. "+res.getMeasurementId()+" ready\n");
			instrument.measurementReady(res);
			//instrument.measurementReady(resData);  //this is the old way to do the thing 24.6. by Aleware
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * Method for initializing a measurement
	 * 
	 * @version 0.1
	 * @since 0.1 
	 * @.pre myDevice != null AND controller != null 
	 * @.post (Measurement will be initialized, GPIB commands will be sent to the hardware)
	 * 
	 */
	public void initialize(){
		try{
			String linlog = "";
			switch (query.getyLinLog()){
				case "Lin": linlog = "1";break;
				case "Lg": linlog = "2";break;
			}
			System.out.print("Setting up... ");
			myDevice.writeCommand("IT3 DR0 BC");
			myDevice.writeCommand("DE CH1,'VG','IG',2,3;CH2;CH3,'VS','IS',1,3;CH4;");
			myDevice.writeCommand("VS1;VS2;VM1;VM2;");
			myDevice.writeCommand("SS IC1,"+query.getCurrent()+","+query.getVoltageCompliance()+";VC3,0,"+query.getCurrentCompliance()+";");
			myDevice.writeCommand("SM WT 0;IN "+query.getStep()+";NR "+query.getNumberOfSteps()+";DM1 XT "+query.getxMin()+","+query.getxMax()+";YA 'VG',"+linlog+","+query.getyMin()+","+query.getyMax()+";");
			System.out.println("done!");
			controller.toConsole("done!\n");
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
