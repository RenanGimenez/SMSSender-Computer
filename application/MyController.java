package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.awt.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.border.LineBorder;

public class MyController implements Initializable {


	@FXML
	private TextArea textToSend;
	@FXML
	private VBox messagesVBox;
	@FXML 
	private ScrollPane messagesScrollPane;
	@FXML
	private VBox VBoxContacts;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		
	}

	@FXML
	public void send(ActionEvent event) throws InterruptedException {
		try {
			String messageText = textToSend.getText();
			textToSend.setText("");
			HBox messageBox = (HBox) FXMLLoader.load(getClass().getResource("Message.fxml"));
			TextFlow messageTextFlow = (TextFlow) messageBox.getChildren().get(0);
			messageTextFlow.getChildren().add(new Text(messageText));
			messageTextFlow.getStyleClass().clear();
			messageTextFlow.getStyleClass().add("message");
			messageTextFlow.getStyleClass().add("sent");
			messageBox.getStyleClass().clear();
			messageBox.getStyleClass().add("hbox");
			messageBox.getStyleClass().add("sent");
			messagesVBox.getChildren().add(messageBox);
			messagesScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());
			HBox.setMargin(messageTextFlow, new Insets(12.5,0.0,12.5,0.0));
			displayContacts();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayContacts() {

		try {
			for(int i = 0; i < 20; ++i) {
				System.out.println(VBoxContacts);
				AnchorPane contactPane = (AnchorPane) FXMLLoader.load(getClass().getResource("Contact.fxml"));
				Button contactButton = (Button) contactPane.getChildren().get(0);
				contactButton.setText("Contact "+ (i+1));
				VBoxContacts.getChildren().add(contactPane);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}