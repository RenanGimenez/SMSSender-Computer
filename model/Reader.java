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

import model.Contact;

public class Reader implements Runnable {

	
//	private ObjectInputStream objectIn;
	private BufferedReader stringIn;

	public Reader(InputStream in) {
		//this.objectIn = new ObjectInputStream(in);
		this.stringIn = new BufferedReader(new InputStreamReader(in));
	}
	@Override
	public void run() {
		try {
			while(true) {
				String command = stringIn.readLine();
				System.out.println(command);
				switch (command) {
				case "DISPLAY":
					display();
					break;
				case "CONTACT_LIST":
					try{
						importContactList();
					} catch(Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					System.out.println("error");
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
		ObjectInputStream objectIn = new ObjectInputStream(Server.getInputStream());
		LinkedList<Contact> contactList;
		try {
			contactList = (LinkedList<Contact>) objectIn.readObject();
		
		} catch (ClassNotFoundException | IOException e) {
			contactList = new LinkedList<Contact>();
			e.printStackTrace();
		}
		System.out.println(contactList);
		Server.setContactList(contactList);
		
	}
	private void display() throws IOException {
	/*	String messageFrom = stringIn.readLine();
		String messageContent = stringIn.readLine();
		HomeWindow.newMessageIncoming(messageFrom, messageContent);
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
