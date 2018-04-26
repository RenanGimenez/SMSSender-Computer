package model;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MessagesManager {

	public static MessagesManager instance;
	
	private MessagesManager() {
		
	}
	
	public HBox getMessageView(String text){
		try {
			HBox messageBox = (HBox) FXMLLoader.load(getClass().getResource("../view/Message.fxml"));
			TextFlow messageTextFlow = (TextFlow) messageBox.getChildren().get(0);
			messageTextFlow.getChildren().add(new Text(text));
			messageTextFlow.getStyleClass().clear();
			messageTextFlow.getStyleClass().add("message");
			messageTextFlow.getStyleClass().add("sent");
			messageBox.getStyleClass().clear();
			messageBox.getStyleClass().add("hbox");
			messageBox.getStyleClass().add("sent");
			HBox.setMargin(messageTextFlow, new Insets(12.5,0.0,12.5,0.0));
			return messageBox;
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
	
	}
	public static MessagesManager getInstance(){
		if(instance == null)
			instance = new MessagesManager();
		return instance;
	}
}
