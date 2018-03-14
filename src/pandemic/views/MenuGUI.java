package pandemic.views;

import java.awt.BasicStroke;
import java.io.InputStream;
import java.nio.file.*;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class MenuGUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane main = new Pane();
		main.setPrefSize(1024, 768);

		InputStream iStream = Files.newInputStream(Paths
				.get("C:\\Users\\G\\eclipse-workspace\\pandemic\\src\\pandemic\\resources\\MainMenu\\MainMenu.jpg"));
		Image img = new Image(iStream);
		iStream.close();

		ImageView imageView = new ImageView(img);
		imageView.setFitWidth(1024);
		imageView.setFitHeight(768);

		MenuLayout MenuLayout = new MenuLayout();
		MenuLayout.setVisible(false);
		main.getChildren().addAll(imageView, MenuLayout);

		Scene s = new Scene(main);
		FadeTransition ft = new FadeTransition(Duration.seconds(3.0), MenuLayout);
		ft.setFromValue(0);
		ft.setToValue(1);
		MenuLayout.setVisible(true);
		ft.play();
		


		primaryStage.setScene(s);
		primaryStage.show();

	}

}
