package pandemic.views;

import java.io.InputStream;
import java.nio.file.*;

import client.PandemicClient;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import server.PandemicServer;

public class MenuGUI extends Application {

	PandemicServer pandemicServer = null;
	PandemicClient pandemicClient = null;

	class ShutDownHook extends Thread {
		public void run() {
			pandemicServer.close();
			pandemicClient.close();
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane main = new Pane();
		main.setPrefSize(1024, 768);

		InputStream iStream = Files.newInputStream(Paths
				.get("./src/pandemic/resources/MainMenu/MainMenu.jpg"));
		Image img = new Image(iStream);
		iStream.close();

		ImageView imageView = new ImageView(img);
		imageView.setFitWidth(1024);
		imageView.setFitHeight(768);


		MenuLayout MenuLayout = new MenuLayout(pandemicServer, pandemicClient, primaryStage);
		MenuLayout.setVisible(false);
		main.getChildren().addAll(imageView, MenuLayout);




		Scene s = new Scene(main);
		FadeTransition ft = new FadeTransition(Duration.seconds(3.0), MenuLayout);
		ft.setFromValue(0);
		ft.setToValue(1);
		MenuLayout.setVisible(true);
		ft.play();
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
			// Save file
		});
		primaryStage.setScene(s);
		primaryStage.show();


		//Runtime.getRuntime().addShutdownHook(new Message());


	}


}
