package pandemic.views;

import java.awt.EventQueue;

import javafx.application.Application;

public class LaunchGUI {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
        //System.out.println("Working Directory = " +
        //        System.getProperty("user.dir"));
//		Application.launch(MenuGUI.class, args);

        //Application.launch(MyApp.class, args);
	

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI("jbh12");
					frame.setVisible(true);
					frame.draw();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

}
}


