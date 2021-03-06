package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.awt.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Contact;
import model.ContactsManager;
import model.Message;
import model.MessagesManager;
import model.server.Server;
import tools.Options;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.border.LineBorder;

import applications.Main;
import applications.OptionsApp;

public class MyController implements Initializable {

	@FXML
	private PasswordField passwordField;
	@FXML
	private BorderPane root;
	@FXML
	private TextArea textToSend;
	@FXML
	private VBox VBoxContacts;
	private String TALKING_TO = "talkingTo";

	@FXML
	private Button optionsButton;
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	@FXML
	public void send(ActionEvent event) throws InterruptedException {

		ScrollPane activeScrollPane = ContactsManager.getInstance().getActiveScrollPane();
		Server.sendMessage(activeScrollPane.getId(), textToSend.getText());
		VBox messagesVBox = ((VBox) activeScrollPane.getContent());
		messagesVBox.getChildren().add(MessagesManager.getInstance().getMessageView(textToSend.getText(),"sent"));
		textToSend.setText("");
		activeScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());


	}

	private void displayContacts() throws InterruptedException {
		ContactsManager manager = ContactsManager.getInstance();
		BorderPane root = (BorderPane) ((Main) Main.getApplication()).getRoot();	
		VBox vBoxContacts = (VBox) ((ScrollPane)root.getLeft()).getContent();
		vBoxContacts.getChildren().addAll(manager.getContacts());
		manager.setVBoxContacts(vBoxContacts);

		AnchorPane top = (AnchorPane) ((Main) Main.getApplication()).getRoot().getTop();
		ObservableList<Node> topChildren = top.getChildren();
		for(Node child : topChildren) {
			if(child.getId().equals(TALKING_TO)) {
				((Label) child).setText(manager.getContact(manager.getActiveScrollPane().getId()).getName());
				break;
			}
		}
		System.out.println(manager.getActiveScrollPane().getId());
	}

	@FXML
	public void showChatForContact(ActionEvent event) throws InterruptedException {
		showChatForContact((Button) event.getSource());
	}

	@FXML
	public void connectWhenEnterIsPressed(KeyEvent key) throws InterruptedException {
		try {
			byte code = key.getCharacter().getBytes("UTF-8")[0];
			if(code == 13)
				connect(null);
		} catch (UnsupportedEncodingException e) {
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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Connection failed");
			alert.setHeaderText("Connection failed");
			alert.setContentText("");
			alert.showAndWait();
		}	
	}
	@FXML
	public void displayOldMessages(ActionEvent event) throws InterruptedException{
		try {
			ContactsManager contactsManager = ContactsManager.getInstance();
			MessagesManager messagesManager = MessagesManager.getInstance();

			VBox messagesVBox = ((VBox) contactsManager.getActiveScrollPane().getContent());
			Contact contact = contactsManager.getContact(contactsManager.getActiveScrollPane());
			ArrayList<Message> messages = contact.getOldMessages(contact.getNbOldMessages() - contact.getNbOldMessagesDisplayed() - Integer.parseInt(Options.getShowMessagesBy()), //begin
					contact.getNbOldMessages() - contact.getNbOldMessagesDisplayed()); //end
			for(int i=0; i<messages.size();++i) {
				if(messages.get(i).getSender().equals("USER"))
					messagesVBox.getChildren().add(i+1,messagesManager.getMessageView(messages.get(i).getContent(),"sent"));
				else
					messagesVBox.getChildren().add(i+1,messagesManager.getMessageView(messages.get(i).getContent(),"received"));		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void displayMessage(String messageFrom, String messageContent) {
		Button senderButton = ContactsManager.getInstance().getButtonOf(messageFrom);
		System.out.println("Button id: "+senderButton.getId()+"  button text: "+senderButton.getText());
		ScrollPane senderScrollPane = ContactsManager.getInstance().getScrollPaneOf(senderButton);
		System.out.println("senderScrollPane id: "+senderScrollPane.getId());
		VBox messagesVBox = ((VBox) senderScrollPane.getContent());
		messagesVBox.getChildren().add(MessagesManager.getInstance().getMessageView(messageContent,"received"));
		senderScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());

		ObservableList<String> styles = senderButton.getStyleClass();

		//test if the style newMessage exists
		for (int i = 0; i < styles.size(); ++i)
			if(styles.get(i).equals("newMessage")) {
				return;
			}

		// the style newMessage does not exist
		styles.add("newMessage");

	}

	public void showChatForContact(Button buttonOfContact) {
		ContactsManager manager = ContactsManager.getInstance();
		try {
			manager.setActiveScrollPane(buttonOfContact);			
			Main.getApplication().getRoot().setCenter(manager.getActiveScrollPane());

			ObservableList<String> styles = (buttonOfContact).getStyleClass();

			for (int i=0; i<styles.size();++i) {
				if(styles.get(i).equals("newMessage")) {
					styles.remove(i);
					--i;
				}
			}

			AnchorPane top = (AnchorPane) ((Main) Main.getApplication()).getRoot().getTop();
			ObservableList<Node> topChildren = top.getChildren();
			for(Node child : topChildren) {
				if(child.getId().equals(TALKING_TO)) {
					((Label) child).setText(manager.getContact(manager.getActiveScrollPane().getId()).getName());
				}
			}

			((ScrollPane) ((Main) Main.getApplication()).getRoot().getLeft()).setVvalue(0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void openOptionsWindow(ActionEvent event) throws InterruptedException{
		Application optionsApp = new OptionsApp();
		Stage stage = new Stage();

		try {
			optionsButton.setDisable(true);
			optionsApp.start(stage);

			stage.setOnCloseRequest((WindowEvent event1) -> {
				optionsButton.setDisable(false);
				try {
					optionsApp.stop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});

			stage.setOnHidden((WindowEvent event1) -> {

				optionsButton.setDisable(false);
				try {
					optionsApp.stop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}