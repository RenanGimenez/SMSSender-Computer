package main;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	private Region root;
	private Scene scene;
	@FXML
    private VBox VBoxContacts;
	private Stage primaryStage;
	private static Application application;
	
	@Override
	public void start(Stage primaryStage) {
	
		
		try {
			application = this;
			this.primaryStage = primaryStage;
			root = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/Connection.fxml"));
		
			scene = new Scene(root,731,434);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	public static void main(String[] args) {
		launch(args);
		
	}

	public static Main getApplication() {
		return (Main) application;
	}


	public BorderPane getRoot() {
		return (BorderPane) root;
	}
	public void openChat() {
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource("../view/Chat.fxml"));
			primaryStage.setResizable(true);
			scene = new Scene(root,939,608);
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(800);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	

	
}
