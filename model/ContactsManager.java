package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.server.Server;
import tools.Options;

public class ContactsManager {
	private static ContactsManager instance;
	@FXML
	private VBox VBoxContacts;
	private ArrayList<Contact> contacts;
	private HashMap<Button, ScrollPane> viewMap;
	private ScrollPane activeScrollPane;

	private ContactsManager() {
		contacts = new ArrayList<Contact>();
		viewMap = new HashMap<Button,ScrollPane>();
		activeScrollPane = null;
		getContactsFromPhone();
		generateViews();		
	};

	/* Generates the hashmap "viewMap" which contains binded Button and ScrollPane 
	 * The id of each scrollpane and button is the phone number of the contact associated to it*/
	private void generateViews() {
		for(int i = 0; i < contacts.size(); ++i) {
			try {
				Contact contact = contacts.get(i);
				AnchorPane contactPane = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/Contact.fxml"));
				Button contactButton = (Button) contactPane.getChildren().get(0);
				contactButton.setText(contact.getName());
				contactButton.setId(contact.getNumTel());
				ScrollPane scrollPane = (ScrollPane) FXMLLoader.load(getClass().getResource("../view/ChatScrollPane.fxml"));
				scrollPane.setId(contact.getNumTel());
				
				if(i==0) 
					activeScrollPane = scrollPane;
				viewMap.put(contactButton, scrollPane);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		}


	}

	public void setVBoxContacts(VBox vbox) {
		this.VBoxContacts = vbox;
	}
	
	/* asks the server for the contactlist then fill its own contactList*/
	public void getContactsFromPhone() {
		MessagesManager messagesManager = MessagesManager.getInstance();
		contacts = Server.getContactList();
		for (int i=0; i<contacts.size(); ++i) {
			contacts.get(i).setMessageList(messagesManager.getMessagesOf(contacts.get(i)));
		}

	}
	
	/*returns the contactList under a linked list of contacts */
	public ArrayList<Contact> getContactList(){
		return contacts;
	}
	
	public List<Node> getContacts() {
		Set keySet = viewMap.keySet();
		Object[] keyArray =  keySet.toArray();
		sortByName(keyArray);
		LinkedList<Node> contacts = new LinkedList<Node>();
		for(int i = 0; i < keyArray.length; ++i) {
			contacts.add((Node) keyArray[i]);
		}
		
		return contacts;
	}

	/* Sort buttons array by the name of their contact */
	private void sortByName(Object[] keyArray) {
		for(int i=0;i<keyArray.length;i++) {
			Button temp = (Button)keyArray[i];
			int left=0;
			int right=i-1;
			int mid=0;
			while(left<=right) {
				mid=(left+right)/2;
				if(temp.getText().compareTo(((Button)keyArray[mid]).getText()) < 0)	{
					right=mid-1;
				}
				else {
					left=mid+1;
				}
			}

			for(int j=i-1;j>=left;j--) {
				keyArray[j+1]=keyArray[j];
			}
			if(left!=i) {
				keyArray[left]=temp;
			}
		}


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
		final int firstIndex = 0;
		activeScrollPane = viewMap.get(source);
		activeScrollPane.setVisible(true);
		ObservableList<Node> children = VBoxContacts.getChildren(); //move contact to the top of the list
		for (int i = firstIndex; i < children.size(); ++i) {
			if(children.get(i) == source) {
				for (int j = i ; j > firstIndex; --j) {
					Node temp = children.get(j-1);
					children.set(j-1, new Button());
					children.set(j, temp);
					
				}
				children.set(firstIndex, source);
				break;
			}
		}

	}

	public Contact getContact(String numTel) {
		for (Contact c : contacts) {
			if(c.getNumTel().equals(numTel)) {
				return c;
			}
		}
		return null;
	}
	
	public Contact getContact(ScrollPane scrollPane) {
		for (Contact c : contacts) {
			if(c.getNumTel().equals(scrollPane.getId())) {
				return c;
			}
		}
		return null;
	}

	public ScrollPane getScrollPaneOf(Button button) {
		return viewMap.get(button);
	}

	public Button getButtonOf(String messageFrom) {
		System.out.println("new Message from " + messageFrom);
		Set<Button> keySet = viewMap.keySet();
		for (Button b : keySet) {
			if(b.getId().equals(messageFrom) || (Options.getPrefixOfCountry()+b.getId()).substring(1).equals(messageFrom))
				return b;
		}
		return null;
	}

	public boolean isActiveContact(String messageFrom) {
		return activeScrollPane.getId().equals(messageFrom);
	}

}
