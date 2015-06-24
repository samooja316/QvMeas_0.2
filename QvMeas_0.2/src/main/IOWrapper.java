package main;

import java.awt.FileDialog;
import java.io.*;
import java.util.Scanner;

import javax.swing.JFrame;



/*
 * This class is for file i/o functions
 * 
 * @author Tuukka Panula, Aleksi Oja
 * @email tujupan@utu.fi, alejee@live.com
 * @date 6/2015
 * @.version 0.1
 * 
 */
public class IOWrapper {

	private String _data;
	
	private FileOutputStream _file;
	private DataOutputStream _dataFile;
	Core controller;
	public IOWrapper(Core controller) {
		this.controller = controller;
		_data = "Testidatateksti";
	}

	
	
	/*
 	* Method for choosing a file to write or read to/from
 	* 
	* @version 0.1
	* @since 0.1
	* @return path to file in which measurement results will be written
	* @.pre true
	* @.post true
 	*/
	public String giveFilePath() {
		FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD);
		fd.setDirectory("C:\\");
		fd.setFile("*.*");
		fd.setVisible(true);
		String path = fd.getDirectory();
		if (path.equals(null)) path = "";
		String name = fd.getFile();
		if (name.equals(null)) name = "";
		String filePath = path + name;	
		return(filePath);
	}
	
	
	
	
	/*
 	* Method for writing data to a file
 	* 
	* @version 0.1
	* @since 0.1
	* @.pre true
	* @.post (resData is written to a file specified in the filePath
 	*/
	public void writeData(String filePath, String resData) {
		try {
			_file = new FileOutputStream(filePath, true);
			_dataFile = new DataOutputStream(_file);
			System.out.println("Trying to write data to the file");
			System.out.println(formatResultData(resData));
			_dataFile.writeUTF(formatResultData(resData));
			controller.toResultConsole(formatResultData(resData));
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			if (_dataFile!=null) {
				try{
					_dataFile.close();
					System.out.println("Datafile closed");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/*
 	* Method for formatting result data
 	* 
	* @version 0.1
	* @since 0.1
	* @return formatted result data, two columns: time[s], voltage[V]
	* @.pre true
	* @.post true
 	*/
	public String formatResultData(String resData){
		Scanner scanner = new Scanner(resData);
		char currentChar;

		String resLine="";
		String finalResData="";
		
		for (int i=0;i<12;i++){
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
		return finalResData;
	}
}
