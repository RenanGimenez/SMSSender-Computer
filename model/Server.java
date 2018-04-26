package model;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import model.Contact;

public class Server {
	private static final int PORT = 10000;
	private static Socket socket;
	private static InputStream in;
	private static OutputStream out;
	private static LinkedList<Contact> contactList = new LinkedList<Contact>();
	
	public static void connectToPhone(String code) throws Exception {
		try {
			socket = new Socket(codeToIP(code), PORT);
			socket = Server.getSocket();	

			in = socket.getInputStream();
			new Thread(new Reader(in)).start();

			out = socket.getOutputStream();
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
	public static OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	public static LinkedList<Contact> getContactList() {
		
		return contactList;
	}

	public static void setContactList(LinkedList<Contact> contactList) {
		Server.contactList = contactList;
		System.out.println("OK");
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

}
