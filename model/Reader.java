package model;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;

import controller.MyController;
import javafx.application.Platform;
import model.Contact;

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
			//objectIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void importContactList() throws IOException {
		LinkedList<Contact> contactList = new LinkedList<Contact>();
		
		int contactListSize = Integer.parseInt(stringIn.readLine());
		String name, telNum;
		
		while(contactList.size() != contactListSize) {
			name = stringIn.readLine();
			telNum = stringIn.readLine();
			contactList.add(new Contact(name, telNum));
		}
		
		System.out.println("contactList : "+contactList);
		Server.setContactList(contactList);

	}
	private void display() throws IOException {
		final String messageFrom = stringIn.readLine();
		String line, messageContentBuilder = "";
		while(!(line = stringIn.readLine()).equals("END_OF_DISPLAY")) {
			messageContentBuilder += line +"\n";
		}
		final String messageContent = messageContentBuilder.substring(0, messageContentBuilder.length() - 1);
		
		Platform.runLater(
				  () -> {
					  new MyController().displayMessage(messageFrom, messageContent);
				  }
				);
		
	/*	HomeWindow.newMessageIncoming(messageFrom, messageContent);
		if(!HomeWindow.isActive()){
			//Windows notification
			if(SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();
				Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
				TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
				trayIcon.setImageAutoSize(true);
				trayIcon.setToolTip("System tray icon demo");
				trayIcon.addActionListener(new NewMessageIncomingAction(messageFrom, messageContent));
				try {
					tray.add(trayIcon);
					trayIcon.displayMessage("New message from "+messageFrom, "Click to see", MessageType.INFO);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}*/

	}

	public class NewMessageIncomingAction implements ActionListener {

		private String messageFrom;
		private String messageContent;

		public NewMessageIncomingAction(String messageFrom, String messageContent) {
			this.messageFrom = messageFrom;
			this.messageContent = messageContent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// BEG BDD PART
			//      *
			//      *
			//		*
			// END BDD PART

			//	MainWindow.displayIncomingMessage(messageFrom,messageContent);

		}

	}


}
