package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import main.Main;
import model.Contact;
import model.ContactsManager;
import model.Message;
import model.MessagesManager;
import model.Server;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.border.LineBorder;

public class MyController implements Initializable {

	private static final int nbMessagesToDisplay = 100;
	@FXML
	private PasswordField passwordField;
	@FXML
	private BorderPane root;
	@FXML
	private TextArea textToSend;
	@FXML
	private VBox VBoxContacts;
	private String TALKING_TO = "talkingTo";
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
		ContactsManager manager = ContactsManager.getInstance();
		try {
			manager.setActiveScrollPane((Button) event.getSource());			
			Main.getApplication().getRoot().setCenter(manager.getActiveScrollPane());

			ObservableList<String> styles = ((Button) event.getSource()).getStyleClass();
			for (String style : styles) {
				if(style.equals("newMessage"))
					styles.remove(style);
			}
			AnchorPane top = (AnchorPane) ((Main) Main.getApplication()).getRoot().getTop();
			ObservableList<Node> topChildren = top.getChildren();
			for(Node child : topChildren) {
				if(child.getId().equals(TALKING_TO)) {
					((Label) child).setText(manager.getContact(manager.getActiveScrollPane().getId()).getName());
				}

			}
			System.out.println(manager.getActiveScrollPane().getId());
			((ScrollPane) ((Main) Main.getApplication()).getRoot().getLeft()).setVvalue(0);
			//talkingTo.setText(((Button) event.getSource()).getText());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@FXML
	public void displayOldMessages(ActionEvent event) throws InterruptedException{
		System.out.println("Hi");
		try {
			ContactsManager contactsManager = ContactsManager.getInstance();
			MessagesManager messagesManager = MessagesManager.getInstance();

			VBox messagesVBox = ((VBox) contactsManager.getActiveScrollPane().getContent());
			Contact contact = contactsManager.getContact(contactsManager.getActiveScrollPane());
			ArrayList<Message> messages = contact.getOldMessages(contact.getNbOldMessages() - contact.getNbOldMessagesDisplayed() - nbMessagesToDisplay, //begin
																contact.getNbOldMessages() - contact.getNbOldMessagesDisplayed()); //end
			for(int i=0; i<messages.size();++i) {
				if(messages.get(i).getSender().equals("USER"))
					messagesVBox.getChildren().add(i+1,messagesManager.getMessageView(messages.get(i).getContent(),"sent"));
				else
					messagesVBox.getChildren().add(i+1,messagesManager.getMessageView(messages.get(i).getContent(),"received"));

				// End
			}
			//	messagesManager.get

			//messagesVBox.getChildren().add(MessagesManager.getInstance().getMessageView(messageContent,"received"));
			System.out.println("Hi");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void displayMessage(String messageFrom, String messageContent) {
		Button senderButton = ContactsManager.getInstance().getButtonOf(messageFrom);
		ScrollPane senderScrollPane = ContactsManager.getInstance().getScrollPaneOf(senderButton);
		VBox messagesVBox = ((VBox) senderScrollPane.getContent());
		messagesVBox.getChildren().add(MessagesManager.getInstance().getMessageView(messageContent,"received"));
		senderScrollPane.vvalueProperty().bind(messagesVBox.heightProperty());

		ObservableList<String> styles = senderButton.getStyleClass();
		for (String style : styles) {
			if(style.equals("newMessage"))
				styles.remove(style);
		}
		styles.add("newMessage");

	}

}