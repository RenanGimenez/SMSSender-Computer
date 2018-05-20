package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MessagesManager {

	public static MessagesManager instance;
	private ArrayList<Message> messageList;
	private MessagesManager() {
		getMessagesFromPhone();
	}
	
	public HBox getMessageView(String text, String sentOrReceived){
		try {
			HBox messageBox = (HBox) FXMLLoader.load(getClass().getResource("../view/Message.fxml"));
			TextFlow messageTextFlow = (TextFlow) messageBox.getChildren().get(0);
			messageTextFlow.getChildren().add(new Text(text));
			messageTextFlow.getStyleClass().clear();
			messageTextFlow.getStyleClass().add("message");
			messageTextFlow.getStyleClass().add(sentOrReceived);
			messageBox.getStyleClass().clear();
			messageBox.getStyleClass().add("hbox");
			messageBox.getStyleClass().add(sentOrReceived);
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
	
	public void getMessagesFromPhone() {
		messageList = Server.getMessageList();	
	}

	public ArrayList<Message> getMessageList() {
		return messageList;
		
	}

	public ArrayList<Message> getMessagesOf(Contact contact) {
		ArrayList<Message> messagesOfContact = new ArrayList<Message>();
		
		for(Message m : messageList) {
			if(contact.getNumTel().equals(m.getSender()) || contact.getNumTel().equals(m.getReceiver())) {
				messagesOfContact.add(m);
			}
		}
		return messagesOfContact;
		// TODO Auto-generated method stub
		
	}
	
}
