package main;
import be.ac.ulb.gpib.*;



/*
 * The Entry point of the program, 
 * QvMeas class contains just the main method
 * 
 * @author Aleksi Oja, Tuukka Panula
 * @email alejee@live.com, tujupan@utu.fi
 * @date 6/2015
 * @.version 0.1
 */
public class QvMeas {
	public static void main(String[] args) {
		System.out.println("QvMeas launched");
		GPIBTest.main(null); //use library program GPIBTest to wake instrument up 
		Core c = new Core(); //create new Core object for executing the logic of the program		
	}
}
