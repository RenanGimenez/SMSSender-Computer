package model.database;

import java.sql.*;
import java.util.ArrayList;

import model.Message;
public class Database {

	private static Connection c;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:messagesSent.db");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}

	}

	public static ArrayList<Message> getMessages() {


		try {
			ArrayList<Message> messages = new ArrayList<>();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM MESSAGESSENT;" );

			while ( rs.next() ) {
				int id = rs.getInt("id");
				String  address = rs.getString("address");
				String  body = rs.getString("body");
				Long date = rs.getLong("date");

				/*For debug:
				System.out.println( "ID = " + id );
				System.out.println( "ADDRESS = " + address );
				System.out.println( "BODY = " + body );
				System.out.println( "DATE = " + date );
				System.out.println();*/

				messages.add(new Message("USER","+"+address,"",date,body,""));
			}
			rs.close();
			stmt.close();
			return messages;
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}

	public static void insert(Message message) {


		try {
			//ResultSet rs = stmt.executeQuery( "SELECT * FROM MESSAGESSENT;" );
			PreparedStatement stmt = c.prepareStatement
					("insert into messagesSent (address,body,date) VALUES(?,?,?)");

			stmt.setString(1,message.getReceiver());
			stmt.setString(2,message.getContent());
			stmt.setLong(3,message.getDate());
			stmt.executeUpdate();


			stmt.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	public static void close() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
