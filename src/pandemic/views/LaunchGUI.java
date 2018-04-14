package pandemic.views;

import java.awt.EventQueue;

import javafx.application.Application;

import javax.swing.*;

public class LaunchGUI {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
        //System.out.println("Working Directory = " +
        //        System.getProperty("user.dir"));
		//display GUI components properly on Mac
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {e.printStackTrace(); }

		Application.launch(MenuGUI.class, args);

        //Application.launch(MyApp.class, args);
	


		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI("jbh12");
					frame.setVisible(true);
					frame.draw();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/

}
}


