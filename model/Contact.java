package model;

import java.util.LinkedList;


public class Contact {
	private static final String NAME_USER = "USER";
	private String name;
	private String telNum;
	private LinkedList<Message> messages;
	
	public Contact(String name, String telNum, LinkedList<Message> messages) {
		this.name = name;
		this.telNum = telNum;
		this.messages = messages;
	}

	public Contact(String name, String telNum) {
        this.name = name;
        this.telNum = telNum;
        this.messages = new LinkedList<>();
    }
	
	public String getName() {
		return name;
	}

	public String getNumTel() {
		return telNum;
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

	@Override
	public String toString() {
		return "name: "+name+" telNum: "+telNum; 
	}
}
