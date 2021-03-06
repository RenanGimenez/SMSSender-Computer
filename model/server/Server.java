package model.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javafx.scene.control.Button;
import model.Contact;
import model.Message;
import model.database.Database;

public class Server {
	private static final int PORT = 10001;
	private static Socket socket;
	private static InputStream in;
	private static PrintWriter out;
	private static ArrayList<Contact> contactList;
	private static ArrayList<Message> messageList;

	static {
		contactList = new ArrayList<Contact>();
		messageList = new ArrayList<Message>();
	}

	public static void connectToPhone(String code) throws Exception {

		ExecutorService service = Executors.newSingleThreadExecutor();

		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						socket = new Socket(codeToIP(code), PORT);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			Future<?> f = service.submit(r);

			f.get(2, TimeUnit.SECONDS);     // attempt the task for two minutes
		}
		catch (InterruptedException | TimeoutException e) {
			System.err.println("Connection failed");
			throw e;
		}

		in = socket.getInputStream();
		new Thread(new Reader(in)).start();

		out = new PrintWriter(socket.getOutputStream());
		new Thread(new Ping(codeToIP(code))).start();
		System.out.println("successfully connected");
	}

	public static Socket getSocket() {
		return socket;
	}

	public static InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}


	public static ArrayList<Contact> getContactList() {
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

	private static ArrayList<Contact> sortByName(ArrayList<Contact> contactList) {
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

	private static ArrayList<Message> sortByDate(ArrayList<Message> messageList) {
		for(int i=0;i<messageList.size();i++) {
			Message temp = (Message)messageList.get(i);
			int left=0;
			int right=i-1;
			int mid=0;
			while(left<=right) {
				mid=(left+right)/2;
				if(temp.getDate() < messageList.get(mid).getDate())	{
					right=mid-1;
				}
				else {
					left=mid+1;
				}
			}

			for(int j=i-1;j>=left;j--) {
				messageList.set(j+1, messageList.get(j));
			}
			if(left!=i) {
				messageList.set(left,temp);
			}
		}
		return messageList;

	}

	public static void setContactList(ArrayList<Contact> contactList) {
		synchronized (Server.contactList) {
			Server.contactList.removeAll(Server.contactList);
			Server.contactList.addAll(contactList);
			Server.contactList.notify();
		}
		System.out.println(Thread.currentThread().getName() + " ContactList OK");
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

	public static void sendMessage(String telNum, String message) {
		long date = new Date().getTime();
		out.println("SEND");
		out.println(telNum);
		out.println(message);
		out.println("END_OF_MESSAGE");
		out.flush();
		Database.insert((new Message("USER",telNum,"",date,message,"")));
		System.out.println("Sent to "+telNum + ": "+message);

	}

	public static ArrayList<Message> getMessageList() {
		synchronized (messageList){
			while(messageList.isEmpty())
				try {
					System.out.println(Thread.currentThread().getName());
					messageList.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return sortByDate(messageList);
		}


	}

	public static void setMessageList(ArrayList<Message> messageList) {
		synchronized (Server.messageList) {
			Server.messageList.removeAll(Server.messageList);
			Server.messageList.addAll(messageList);
			Server.messageList.addAll(Database.getMessages());
			Server.messageList.notify();
		}
		System.out.println(Thread.currentThread().getName() + " MessageList OK");
	}

}
