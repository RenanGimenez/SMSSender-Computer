package model.server;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

import applications.Main;
import controller.MyController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import model.Contact;
import model.ContactsManager;
import model.Message;
import model.database.Database;
import tools.Options;
import tools.Sound;
import tools.WindowsNotification;

public class Reader implements Runnable {


	private ObjectInputStream objectIn;
	private BufferedReader stringIn;

	public Reader(InputStream in) throws IOException {

		this.objectIn = new ObjectInputStream(in);

		this.stringIn = new BufferedReader(new InputStreamReader(in));
	}
	@Override
	public void run() {
		try {

			Thread.currentThread().setName("Reader");
			while(true) {
				String command = stringIn.readLine();
				System.out.println(command);
				switch (command) {
				case "DISPLAY":
					display();
					break;
				case "CONTACT_LIST":
					importContactList();
					break;
				case "MESSAGE_LIST":
					importMessageList();
					break;
				default:
					//System.out.println("error");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		try {
			stringIn.close();
			Platform.runLater(
					() -> {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection lost");
						alert.setHeaderText("Connection lost");
						alert.setContentText("Click OK to close the program");
						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK){
							Database.close();
							System.exit(0);
						}
					}
					);


			//objectIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void importMessageList() throws NumberFormatException, IOException {

		int messageListSize = Integer.parseInt(stringIn.readLine());
		ArrayList<Message> messageList = new ArrayList<Message>(messageListSize);
		System.out.println("messageListSize = "+messageListSize);
		String sender, receiver, name, date, type;
		StringBuilder content = new StringBuilder();

		String line = "";
		while(messageList.size() != messageListSize) {
			sender = stringIn.readLine();
			receiver = stringIn.readLine();
			name = stringIn.readLine();
			date = stringIn.readLine();
			content.delete(0, content.length()); //delete the content of the string
			while(!(line = stringIn.readLine()).equals("END_OF_CONTENT"))
				content.append(line+"\n");
			if(content.length() > 0)
				content.deleteCharAt(content.length() - 1); //delete the last '\n'

			type = stringIn.readLine();
			messageList.add(new Message(sender, receiver, name, Long.parseLong(date), content.toString(), type));
		}
		Server.setMessageList(messageList);
	}
	
	private void importContactList() throws IOException {
		int contactListSize = Integer.parseInt(stringIn.readLine());
		ArrayList<Contact> contactList = new ArrayList<Contact>(contactListSize);
		String name, telNum;

		while(contactList.size() != contactListSize) {
			name = stringIn.readLine();
			telNum = stringIn.readLine();
			contactList.add(new Contact(name, telNum));
		}

		Server.setContactList(contactList);

	}
	private void display() throws IOException {
		final String messageFrom = stringIn.readLine();
		String line, messageContentBuilder = "";
		while(!(line = stringIn.readLine()).equals("END_OF_DISPLAY")) {
			messageContentBuilder += line +"\n";
		}
		final String messageContent = messageContentBuilder.substring(0, messageContentBuilder.length() - 1);

		//executed on main thread
		Platform.runLater(
			() -> {
				new MyController().displayMessage(messageFrom, messageContent);
				if((!Main.isWindowFocused() || !ContactsManager.getInstance().isActiveContact(messageFrom)) && Options.isSoundAllowed()) 
					Sound.play();
				if(Main.isWindowMinimized() && Options.isWindowsNotificationAllowed()) 
					WindowsNotification.display(messageFrom);
			}
		);
	}

}