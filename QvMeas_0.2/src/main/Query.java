package main;

import java.util.ArrayList;

public class Query {
	private String current;
	private String step;
	private String numberOfSteps;
	private String currentCompliance;
	private String voltageCompliance;
	private String xMin;
	private String xMax;
	private String yMin;
	private String yMax;
	private String yLinLog;
	private String comments;
	private String name;
	private String number;
	private String calibration;
	
	public Query(ArrayList<String> settings) {
		current = settings.get(0)+settings.get(1);
		step = settings.get(2);
		numberOfSteps = settings.get(3);
		currentCompliance = settings.get(4)+settings.get(5);
		voltageCompliance = settings.get(6)+settings.get(7);
		xMin = settings.get(8);
		xMax = settings.get(9);
		yMin = settings.get(10)+settings.get(12);
		yMax = settings.get(11)+settings.get(12);
		yLinLog = settings.get(13);
		comments = settings.get(14);
		name = settings.get(15);
		number = settings.get(16);
		calibration = settings.get(17);
	}

	public String getxMin() {
		return xMin;
	}

	public String getxMax() {
		return xMax;
	}

	public String getyMin() {
		return yMin;
	}

	public String getyMax() {
		return yMax;
	}

	public String getyLinLog() {
		return yLinLog;
	}

	public String getCurrent() {
		return current;
	}

	public String getStep() {
		return step;
	}

	public String getNumberOfSteps() {
		return numberOfSteps;
	}

	public String getCurrentCompliance() {
		return currentCompliance;
	}

	public String getVoltageCompliance() {
		return voltageCompliance;
	}

	public String getComments() {
		return comments;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNumber() {
		return number;
	}
	public int getCalibration() {
		int calibrationState = 0;
		
		if (calibration == "ON"){
			calibrationState = 1;
		} else if (calibration == "OFF") {
			calibrationState = 0;
		}
		return calibrationState;
	}
	public String toString() {
		return 	"### Settings ###"+"\n"+
				"SMU1;mode:I"+"\n"+
				"SMU3;mode:V"+"\n"+
				"I: "+current+"\n"+
				"Step: "+step+"\n"+
				"Step_count: "+numberOfSteps+"\n"+
				"I_comp: "+currentCompliance+"\n"+
				"V_comp: "+voltageCompliance+"\n"+
				"X_min: "+xMin+"\n"+
				"X_max: "+xMax+"\n"+
				"Y_min: "+yMin+"\n"+
				"Y_max: "+yMax+"\n"+
				"Y_scale: "+yLinLog+"\n"+
				"AUTO CAL: "+calibration+"\n"+
				"################"+"\n";

	}

}
