package model;

import java.util.ArrayList;
import java.util.LinkedList;


public class Contact {
	private static final String NAME_USER = "USER";
	private String name;
	private String telNum;
	private ArrayList<Message> messages;
	private int nbOldMessagesDisplayed;
	
	public Contact(String name, String telNum, ArrayList<Message> messages) {
		this.name = name;
		if(telNum.startsWith("+33"))
			this.telNum = telNum;
		else
			this.telNum = "+33" + telNum.substring(1);
		this.messages = messages;
		nbOldMessagesDisplayed = 0;
	}

	public Contact(String name, String telNum) {
        this.name = name;
        this.telNum = telNum;
        this.messages = new ArrayList<>();
		nbOldMessagesDisplayed = 0;
    }
	
	public String getName() {
		return name;
	}

	public String getNumTel() {
		return telNum;
	}

	public ArrayList<Message> getMessages() {
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

	public void setMessageList(ArrayList<Message> messageList) {
		this.messages = messageList;
		
	}

	public int getNbOldMessagesDisplayed() {
		return nbOldMessagesDisplayed;
	}

	public ArrayList<Message> getOldMessages(int begin, int end) {
		if(begin < 0) 
			begin = 0;
		ArrayList<Message> messages = new ArrayList<Message>();
		for(int i=begin; i<end;++i)
			messages.add(this.messages.get(i));
		nbOldMessagesDisplayed += (end-begin);
		return messages;
	}

	public int getNbOldMessages() {
		return messages.size();
	}
}
