package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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
import main.Main;
import model.ContactsManager;
import model.MessagesManager;
import model.Server;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.border.LineBorder;

public class MyController implements Initializable {

	@FXML
	private PasswordField passwordField;
	@FXML
	private BorderPane root;
	@FXML
	private TextArea textToSend;
	@FXML
	private VBox VBoxContacts;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	@FXML
	public void send(ActionEvent event) throws InterruptedException {
		
		ScrollPane activeScrollPane = ContactsManager.getInstance().getActiveScrollPane();
		Server.sendMessage(activeScrollPane.getId(), textToSend.getText());
		VBox messagesVBox = ((VBox) activeScrollPane.getContent());
		messagesVBox.getChildren().add(MessagesManager.getInstance().getMessageView(textToSend.getText()));
		textToSend.setText("");
		activeScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());
		System.out.println(activeScrollPane);

	}

	private void displayContacts() throws InterruptedException {
		BorderPane root = (BorderPane) ((Main) Main.getApplication()).getRoot();	
		VBox vBoxContacts = (VBox) ((ScrollPane)root.getLeft()).getContent();
		vBoxContacts.getChildren().addAll(ContactsManager.getInstance().getContacts());
	}

	@FXML
	public void showChatForContact(ActionEvent event) throws InterruptedException {
		ContactsManager manager = ContactsManager.getInstance();

		ScrollPane activeScrollPane = manager.getActiveScrollPane();
		
		try {
			manager.setActiveScrollPane((Button) event.getSource());
			Main.getApplication().getRoot().setCenter(manager.getActiveScrollPane());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@FXML
	public void connect(ActionEvent event) throws InterruptedException {
		
		try {
			String code = passwordField.getText();
			System.out.println(code);
			Server.connectToPhone(code);
			((Main) Main.getApplication()).openChat();	
			displayContacts();
			ContactsManager manager = ContactsManager.getInstance();
			Main.getApplication().getRoot().setCenter(manager.getActiveScrollPane());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}