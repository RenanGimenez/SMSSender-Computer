package model;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import javafx.scene.control.Button;
import model.Contact;

public class Server {
	private static final int PORT = 10001;
	private static Socket socket;
	private static InputStream in;
	private static PrintWriter out;
	private static LinkedList<Contact> contactList;

	static {
		contactList = new LinkedList<Contact>();
	}

	public static void connectToPhone(String code) throws Exception {
		try {
			socket = new Socket(codeToIP(code), PORT);
			socket = Server.getSocket();	

			in = socket.getInputStream();
			new Thread(new Reader(in)).start();

			out = new PrintWriter(socket.getOutputStream());
			System.out.println("successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Impossible to connect to the phone!");
		}
	}

	public static Socket getSocket() {
		return socket;
	}

	public static InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}


	public static LinkedList<Contact> getContactList() {
		synchronized (contactList){
			while(contactList.isEmpty())
				try {
					System.out.println(Thread.currentThread().getName());
					contactList.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return sortByName(contactList);
		}


	}

	private static LinkedList<Contact> sortByName(LinkedList<Contact> contactList) {
		for(int i=0;i<contactList.size();i++) {
			Contact temp = (Contact)contactList.get(i);
			int left=0;
			int right=i-1;
			int mid=0;
			while(left<=right) {
				mid=(left+right)/2;
				if(temp.getName().compareTo(contactList.get(mid).getName()) < 0)	{
					right=mid-1;
				}
				else {
					left=mid+1;
				}
			}

			for(int j=i-1;j>=left;j--) {
				contactList.set(j+1, contactList.get(j));
			}
			if(left!=i) {
				contactList.set(left,temp);
			}
		}
		return contactList;

	}

	public static void setContactList(LinkedList<Contact> contactList) {
		synchronized (Server.contactList) {
			Server.contactList.removeAll(Server.contactList);
			Server.contactList.addAll(contactList);
			Server.contactList.notify();
		}
		System.out.println(Thread.currentThread().getName() + "OK");
	}

	private static String codeToIP(String code) {
		String IP = new String();
		for (int i=0; i<code.length(); i+=3) {
			String stringByte = code.substring(i,i+3);
			IP += Integer.parseInt(stringByte, 16) + ".";
		}
		System.out.println(IP);
		return IP.substring(0, IP.length()-1); //remove the last "."

	}

	public static void sendMessage(String numTel, String message) {
		out.println("SEND");
		out.println(numTel);
		out.println(message);
		out.println("END_OF_MESSAGE");
		out.flush();
		System.out.println("Sent to "+numTel + ": "+message);

	}

}
