package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private BorderPane root;
	private Scene scene;
	@FXML
    private VBox VBoxContacts;
	@Override
	public void start(Stage primaryStage) {
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource("Chat.fxml"));
		
			scene = new Scene(root,939,608);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(800);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		//new MyController().displayContacts();
	}
	
	

	public static void main(String[] args) {
		launch(args);
	}

	

	

	
}
