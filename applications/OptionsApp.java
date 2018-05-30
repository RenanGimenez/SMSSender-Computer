package applications;

import java.io.IOException;

import controller.OptionsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class OptionsApp extends Application{

	private AnchorPane root;
	private OptionsApp application;
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Options.fxml"));

			
			Scene scene = new Scene(root,800, 600);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			primaryStage.setTitle("options");

			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			OptionsController.setParamettered(false);
			primaryStage.show();


		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void stop() {
		try {
			super.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
