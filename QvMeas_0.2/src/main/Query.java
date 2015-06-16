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

	public Query(ArrayList<String> settings) {
		current = settings.get(0);
		step = settings.get(1);
		numberOfSteps = settings.get(2);
		currentCompliance = settings.get(3);
		voltageCompliance = settings.get(4);
		xMin = settings.get(5);
		xMax = settings.get(6);
		yMin = settings.get(7);
		yMax = settings.get(8);
		yLinLog = settings.get(9);
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
				"################"+"\n";

	}

}
