package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

//Result class containing all information of a
//conducted measurement

public class Result {
	
	private String id;
	private String rawData;
	private String finalDataString;
	private String comment;
	private String rawSettingsData;
	private String measurementName;
	private ArrayList<Float> voltageSerie = new ArrayList<Float>();
	private ArrayList<Float> timeSerie = new ArrayList<Float>();
	private ArrayList<Float> capacitanceSerie = new ArrayList<Float>();
	String current;
	
	//When object is initialized it should have the next parameters:
	// - identification string
	// - raw measurement data from HP4145B Print
	// - comment line inserted by user
	// - current used in the measurement (String)
	
	public Result(String id, String rawData, String comment, String current) {
		super();
		/*
		this.id = id;
		this.rawData = rawData;
		this.current = current;
		this.comment = comment;
		parseRawData(rawData);
		calculateCapacitanceSerie(current);
		*/
	}
	
	//This method is called when an object is initialized.
	//It parses parses the raw print data and returns
	//a string variable that can be directly imported to e.g. excel.
	//The method also parses voltage and time into their individual
	//data series.
	
	public void parseRawData(String rawData){
		Scanner scanner = new Scanner(rawData);
		char currentChar;

		int timeStartVoltEnd=0;
		int timeEndVoltStart=0;
		
		String resLine="";
		String finalResData="";
		String voltageString="";
		String timeString="";
		
		boolean infoDataRead = false;
		
		for (int i = 0;i < 12;i++){
			String line = scanner.nextLine();
			finalResData = finalResData+line+"\n";
		}
		
		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();
			for (int i=0;i<line.length();i++){
				currentChar = line.charAt(i);
				if (currentChar != ' '){
					resLine = resLine+currentChar;
				}
			}
			resLine=resLine.replace("s","\t");
			resLine=resLine.replace("V","\n");


		}
	    finalResData = finalResData+"\n"+resLine;
		scanner.close();
		//System.out.println(finalResData);
		for (int i = 0;i < finalResData.length();i++){
			//System.out.println(finalResData.substring(i,i+21));
			if (i<finalResData.length()-22){
				if ((finalResData.substring(i,i+23)).equals("TIME       VG    -Ch1 \n")){
					System.out.println("ee");

					rawSettingsData = finalResData.substring(0,i-1);
					infoDataRead = true;
					i=i+23;
				}
			}
			if (infoDataRead==true){
				if (finalResData.charAt(i)=='\n'){
					//System.out.println("jj");
					timeStartVoltEnd = i;
					voltageString = finalResData.substring(timeEndVoltStart+1,timeStartVoltEnd);
					voltageSerie.add(Float.parseFloat(voltageString));
				}
				if (finalResData.charAt(i)=='\t'){
					timeEndVoltStart = i;
					//System.out.println("kk");

					timeString = finalResData.substring(timeStartVoltEnd+1,timeEndVoltStart);

					timeSerie.add(Float.parseFloat(timeString));
					System.out.println(voltageString+" "+timeString);
				}
				

			}
		}
		this.finalDataString=finalResData;
	}
	
	//This is a method for attaining capacitance using differentiation

	public void calculateCapacitanceSerie(String currentString){

		float singleCapacitance = 0;
		int currentInt = 0;
		for (int i = 0;i < currentString.length();i++){
			if (currentString.charAt(i)=='E'){
				currentInt = Integer.parseInt(currentString.substring(0,i-1));
			}
		}
		System.out.println(voltageSerie.size());

		for(int i=0;i<voltageSerie.size()-1;i++){
			singleCapacitance = currentInt/((voltageSerie.get(i+1)-voltageSerie.get(i))/(timeSerie.get(i+1)-timeSerie.get(i)));
			capacitanceSerie.add(singleCapacitance);
		}
	}
	public void generateMeasurementName(String id, int current){
		Date date = new Date();
		measurementName = id +" "+current+" A "+ date.toString();
	}
		
	//Getters
	public String getFinalDataString(){
		return finalDataString;
	}
	public String getRawData(){
		return rawData;
	}
	public String getRawSettingsData(){
		return rawSettingsData;
	}
	public String getMeasurementName(){
		return measurementName;
	}
	public ArrayList<Float> getVoltageSerie(){
			return voltageSerie;
	}
	public ArrayList<Float> getTimeSerie(){
		return timeSerie;
	}
	public ArrayList<Float> getCapacitanceSerie(){
		return capacitanceSerie;
	}
	public String getComment(){
		return comment;
	}
}