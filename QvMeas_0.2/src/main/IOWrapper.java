package main;

import java.awt.FileDialog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.swing.JFileChooser;
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

	private String _defDirPath = "";
	private String _data;
	private FileOutputStream _file;
	private DataOutputStream _dataFile;
	private Core controller;
	
	/*
	 * Constructor
	 */
	public IOWrapper(Core controller) {
		this.controller = controller;
		_data = "Testidatateksti";
	}

	
	
	/*
 	* Method for choosing a file to write or read to/from
 	* 
	* @version 		0.1
	* @since 		0.1
	* @return 		path to file in which measurement results will be written
	* @params		will use String baseName as a suggestion for a filename
	* @.pre 		true
	* @.post 		true
 	*/
	public String giveFilePath(String baseName) {
		FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD);		
		if (_defDirPath.equals("")) { fd.setDirectory("C:\\"); }
		else { fd.setDirectory(_defDirPath); }
		fd.setFile(baseName);
		fd.setVisible(true);
		String path = fd.getDirectory();
		if (path==null) path = "";
		String name = fd.getFile();
		if (name==null) name = "";
		String filePath = path + name;	
		return(filePath);
	}
	
	
	/*
 	* Method for choosing a directory 
 	* 
	* @version 		0.1
	* @since 		0.2
	* @return 		path (String) to a directory specified by the user
	* @.pre 		true
	* @.post 		true
 	*/
	public String getDirPath() {
		System.out.println("directory selection form");
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showDialog(new JFrame(), "Open");
		File f = fc.getSelectedFile();
		String st = "";
		st += f.getAbsolutePath();
		return st;
	}	
	
	
	/*
	 * Method for launching directory dialog and 
	 * setting the dir chosen by the user a a default directory
	 * for the meas results.
	 * 
	 * @version		0.1
	 * @since		0.2
	 * @.pre		true
	 * @.post		getDefaultDirectory() == (path which user chose)
	 */
	public void setDefaultDirectory() {
		_defDirPath = getDirPath();
		controller.toConsole("New default result directory:\n"+_defDirPath+"\n");
	}
	
	
	/*
	 * Get the default directory path
	 * 
	 * @version		0.1
	 * @since		0.2
	 * @return 		String, default directory path
	 * @.pre		true
	 * @.post		true
	 */
	public String getDefaultDirectory() {
		return _defDirPath;
	}
	
	
	/*
 	* Method for writing data to a file
 	* 
	* @version 0.2
	* @since 0.1
	* @.pre true
	* @.post (resData is written to a file specified in the filePath
	* 		and to the result console)
 	*/
	public void writeData(String filePath, Result res) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm");		
		try {
			File file = new File(filePath); 
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Measname: "+res.getMeasurementName()+"\n");
			writer.write("Date: "+sdf.format(res.getDate())+"\n");
			writer.write("Meascomment: "+res.getComment()+"\n");
			writer.write(formatResultData(res.getRawData()));
			writer.close();
			System.out.println("Trying to write data to the file");
			System.out.println(formatResultData(res.getRawData()));
			//consoleprint
			System.out.println("measname: "+res.getMeasurementName());
			System.out.println("meascomment: "+res.getComment());
			System.out.println("date: "+sdf.format(res.getDate()));
			controller.toResultConsole("Measname: "+res.getMeasurementName()+"\n");
			controller.toResultConsole("Date: "+sdf.format(res.getDate())+"\n");
			controller.toResultConsole("Meascomment: "+res.getComment()+"\n");
			controller.toResultConsole(formatResultData(res.getRawData())+"\n");
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
