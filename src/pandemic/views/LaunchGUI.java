package pandemic.views;

import java.awt.EventQueue;

import javafx.application.Application;

public class LaunchGUI {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Application.launch(MenuGUI.class, args);
		//Application.launch(MyApp.class, args);
	
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
}
}


