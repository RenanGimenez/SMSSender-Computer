package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyController implements Initializable {

    private static final double LINE_SPACE = 1.2;
	private static final int BORDER_WIDTH = 3;
	private static final int MARGIN = 10;
	@FXML
    private TextArea textToSend;
    @FXML
    private VBox messagesVBox;
    @FXML 
    private ScrollPane messagesScrollPane;
    @FXML
	private VBox VBoxContacts;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    
    @FXML
    public void send(ActionEvent event) throws InterruptedException {
			try {
				String messageText = textToSend.getText();
				System.out.println(messageText);
				textToSend.setText("");
				HBox messageBox = (HBox) FXMLLoader.load(getClass().getResource("Message.fxml"));
				TextArea messageTextField = (TextArea) messageBox.getChildren().get(0);
				messageTextField.setText(messageText);
				
				//variables for the calcul of the TextArea height
				int nbLines = messageText.split("\n").length;
				double fontSize = 26.0;
				double TAHeight = nbLines * LINE_SPACE * fontSize + 2.0 * BORDER_WIDTH + 2.0 * MARGIN;
				
				System.out.println("nbLines = "+nbLines+"\n fontSize = "+fontSize+"\n  TAHeight = "+TAHeight);
				messageBox.setMinHeight(TAHeight + 50);
				messageBox.setPrefHeight(TAHeight + 50);
				messageBox.setMaxHeight(TAHeight + 50);
				messageTextField.setPrefHeight(TAHeight);
				messagesVBox.getChildren().add(messageBox);
				messagesScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());
				
				displayContacts();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public void displayContacts() {
    	
		try {
			System.out.println(VBoxContacts);
			AnchorPane contactPane = (AnchorPane) FXMLLoader.load(getClass().getResource("Contact.fxml"));
			Button contactButton = (Button) contactPane.getChildren().get(0);
			contactButton.setText("Contact 0");
			VBoxContacts.getChildren().add(contactPane);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}