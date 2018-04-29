package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ContactsManager {
	private static ContactsManager instance;
	@FXML
	private VBox VBoxContacts;
	private LinkedList<Contact> contacts;
	private HashMap<Button, ScrollPane> viewMap;
	private ScrollPane activeScrollPane;
	
	private ContactsManager() {
		contacts = new LinkedList<Contact>();
		viewMap = new HashMap<Button,ScrollPane>();
		activeScrollPane = null;
		getContactsFromPhone();
		generateViews();		
	};
	
	private void generateViews() {
		for(int i = 0; i < contacts.size(); ++i) {
			try {
				AnchorPane contactPane = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/Contact.fxml"));
				Button contactButton = (Button) contactPane.getChildren().get(0);
				contactButton.setText(this.contacts.get(i).getName());
				
				ScrollPane scrollPane = (ScrollPane) FXMLLoader.load(getClass().getResource("../view/ChatScrollPane.fxml"));
				scrollPane.setId(this.contacts.get(i).getNumTel());
				if(i==0) 
					activeScrollPane = scrollPane;
				viewMap.put(contactButton, scrollPane);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			
		}
		
		
	}

	public void getContactsFromPhone() {
		
		LinkedList<Contact> contactList = Server.getContactList();
		for(int i = 0; i < contactList.size(); ++i) {
			contacts.add(new Contact(contactList.get(i).getName(), contactList.get(i).getNumTel()));
		}
		
	}
	public LinkedList<Contact> getContactList(){
		return contacts;
	}
	public List<Node> getContacts() {
		Set keySet = viewMap.keySet();
		Object[] keyArray = keySet.toArray();
		LinkedList<Node> contacts = new LinkedList<Node>();
		for(int i = 0; i < keyArray.length; ++i) {
			contacts.add((Node) keyArray[i]);
		}
		return contacts;
	}
	
	public static ContactsManager getInstance() {
		if(instance == null)
			instance = new ContactsManager();
		return instance;
	}

	public ScrollPane getActiveScrollPane() {
		return activeScrollPane;
	}

	public void setActiveScrollPane(Button source) throws IOException {
		System.out.println("debut: "+activeScrollPane);
		//activeScrollPane.setVisible(false);
		
		activeScrollPane = viewMap.get(source);
		
		activeScrollPane.setVisible(true);
		System.out.println("fin:      "+activeScrollPane);
		
	}

	public Contact getContact(String numTel) {
		for (Contact c : contacts) {
			if(c.getNumTel().equals(numTel)) {
				return c;
			}
		}
		return null;
	}
}
