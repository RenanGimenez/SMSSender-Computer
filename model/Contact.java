package model;

import java.util.LinkedList;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;


public class Contact {
	private static final String NAME_USER = "USER";
	private String name;
	private String numTel;
	private LinkedList<Message> messages;
	private Button contactButton;
	private ScrollPane contactChat;
	
	public Contact(String name, String numTel, LinkedList<Message> messages) {
		this.name = name;
		this.numTel = numTel;
		this.messages = messages;
		this.contactButton = null;
		this.contactChat = null;
	}

	public Contact(String name, String numTel) {
        this.name = name;
        this.numTel = numTel;
        this.messages = new LinkedList<>();
    }
	
	public String getName() {
		return name;
	}

	public String getNumTel() {
		return numTel;
	}

	public LinkedList<Message> getMessages() {
		return messages;
	}
	
	public void addMessage(Message message) {
		messages.add(message);
	}

	public boolean isUser() {
		return name.equals(NAME_USER);
	}

	public Button getContactButton() {
		return contactButton;
	}

	public void setContactButton(Button contactButton) {
		this.contactButton = contactButton;
	}

	public ScrollPane getContactChat() {
		return contactChat;
	}

	public void setContactChat(ScrollPane contactChat) {
		this.contactChat = contactChat;
	}

}
