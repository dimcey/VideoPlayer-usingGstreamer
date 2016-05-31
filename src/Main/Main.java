package Main;

import org.gstreamer.Gst;
 
import GUI.GUI;

public class Main {

	public static void main(String[] args) {
		 args = Gst.init("AVPlayer", args);
		 
		 //new thread which is calling the GUI class
		 java.awt.EventQueue.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new GUI();
	            }
	        });
	}


}
