package tools;


import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.MyController;
import main.Main;
import model.ContactsManager;



public class WindowsNotification {
private static boolean allowed = true;
	
	
	public static void setAllow(boolean allowed) {
		WindowsNotification.allowed = allowed;
	}
	
	public static boolean isAllowed() {
		return WindowsNotification.allowed;
	}
	
	public static void display(String messageFrom) {
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("System tray icon demo");
		
		//Does not work on Windows 10, see https://bugs.openjdk.java.net/browse/JDK-8146537
		/*trayIcon.addActionListener(new ActionListener() {  
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.setMinimizedWindow(false);
				new MyController().showChatForContact(ContactsManager.getInstance().getButtonOf(messageFrom));
				
			}
		});*/
		try {
			tray.add(trayIcon);
			trayIcon.displayMessage("New message from "+ContactsManager.getInstance().getContact(messageFrom).getName(), "", MessageType.INFO);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
