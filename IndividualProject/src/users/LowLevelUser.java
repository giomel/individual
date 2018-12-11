package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dbconnection.DBConnection;
import menus.Login;


public class LowLevelUser {
	
	public static void getUserMenu(String user) {
		Scanner scanner= new Scanner(System.in);
		System.out.println("+------------------------+\n| Choose among options   |\n+------------------------+\n"
							+ "1.New Message\n2.Inbox\n3.Outbox\n4.Logout");
			String choice =scanner.nextLine();
			
			boolean wrong=false;
			while(!wrong) {
				
			switch(choice) {
			case "1": sendMessage(user); getUserMenu(user); wrong=true; break;
			case "2": getInbox(user);getUserMenu(user); wrong=true; break;
			case "3": getOutbox(user);getUserMenu(user); wrong=true;break;	
			case "4": WriteToFile.writeToFile("Logout", user, LocalDateTime.now());Login.validateCredentials();wrong=true;break;
			default:System.out.println("Choose 1 for New Message, 2 for Inbox, 3 for Outbox, 4 to Logout");
			choice=scanner.nextLine();continue;
			}
			}
	}
	
	public static void sendMessage(String sender) {
		Scanner scanner= new Scanner(System.in);
		System.out.println("-------------\nNew Message:\n-------------");
		DBConnection db= new DBConnection();
		Statement stm;
		boolean exists=false;
		
		try {
		stm = db.connect().createStatement();
		
		ResultSet rs = stm.executeQuery("select user_id from user where username='"+sender+"'");
		int sen=0;
		if(rs.next()) sen=rs.getInt("user_id");
		
		
		int sen2=0;
		String receiver="";
		while(sen2==0) {
		System.out.println("Give a valid receiver username:");
		receiver =scanner.nextLine();
		ResultSet rs2 = stm.executeQuery("select user_id from user where username='"+receiver+"'");
		if(rs2.next()) sen2=rs2.getInt("user_id");
		exists= true;
		}
		
		boolean desclength=false;
		String description=null;
		while(!desclength) {
			System.out.println("Description(30 characters):\n-----------");
			description= scanner.nextLine();
			if (description.length()<=30) desclength=true;
			else continue;
		}
		
		
		boolean textlength=false;
		String text=null;
		while(!textlength) {
			System.out.println("Type text(250 characters):\n-----------");
			text= scanner.nextLine();
			if (text.length()<=250) textlength=true;
			else continue;
		}
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.now();
		String date = dateTime.format(formatter); 
		
		db.insertMessage(sen, sen2,description, text, date);
		WriteToFile.writeMessageToFile(sender, receiver, text, dateTime);
		db.connect().close();
		
		
		
		} catch (SQLException e) {
			System.out.println("Username not found");
			//e.printStackTrace();
		}
	}
	
	public static void getInbox(String username) {
		DBConnection db= new DBConnection();	
		Statement stm;
		Scanner scanner = new Scanner(System.in);
		
		try {
			stm = db.connect().createStatement();
			int receiver_id=0;
			boolean displayInbox=true;
			while(displayInbox) {
			ResultSet rs1=stm.executeQuery("select user_id from user where username='"+username+"';");
			if(rs1.next()) receiver_id=rs1.getInt("user_id");
			
			
			ResultSet rs = stm.executeQuery("SELECT message.message_id,user.username, message.description, message.date FROM user, message where"
											+ " user.user_id=message.sender_id and message.receiver_id="+receiver_id+";");
			
			
			
			List<String> list_id=new ArrayList<>();
			while(rs.next()) {
				Integer messid= rs.getInt("message.message_id");
				list_id.add(""+messid);
				
				String userna=rs.getString("user.username");
				String description=rs.getString("message.description");
				String date=rs.getDate("message.date").toString();
				
				System.out.println("---------------------------------");
				System.out.println("#: " +messid);
				System.out.println("Date: "+date);
				System.out.println("From: "+userna);
				System.out.println("Description: "+description);
				System.out.println("---------------------------------");
			
			}
			boolean found=false;
			String message="";
			while(!found) {
			System.out.println("\nSelect Message or press b to go back");
			  message =scanner.nextLine();
			 
			 for(String mess:list_id) {
				 if(message.equals(mess)) {found=true;
				 							break;}
			 }
			 if(message.equalsIgnoreCase("b")) {found=true; getUserMenu(username);}
			 
			}
			String text;
			ResultSet rs2=stm.executeQuery("SELECT text from message where message_id="+ Integer.parseInt(message)+";");
			if(rs2.next())  {text=rs2.getString("text");
							 System.out.println("---------------------------------");
							 System.out.println(text);
							 System.out.println("\n");}
			
			
			}
			db.connect().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void getOutbox(String username) {
		DBConnection db= new DBConnection();	
		Statement stm;
		Scanner scanner = new Scanner(System.in);
		
		try {
			stm = db.connect().createStatement();
			int sender_id=0;
			ResultSet rs1=stm.executeQuery("select user_id from user where username='"+username+"';");
			if(rs1.next()) sender_id=rs1.getInt("user_id");
			
			
			
			ResultSet rs = stm.executeQuery("SELECT message.message_id,user.username, message.description, message.date FROM user, message "+
					"where message.receiver_id=user.user_id "+
					"and message.sender_id="+ sender_id+";");
			
			
			
			List<String> list_id=new ArrayList<>();
			while(rs.next()) {
				int messid= rs.getInt("message.message_id");
				list_id.add(""+messid);
				String userna=rs.getString("user.username");
				String description=rs.getString("message.description");
				String date=rs.getDate("message.date").toString();
			
				System.out.println("---------------------------------");
				System.out.println("#: " +messid);
				System.out.println("Date: "+date);
				System.out.println("From: "+userna);
				System.out.println("Description: "+description);
				System.out.println("---------------------------------");
			
			}
			
			boolean found=false;
			String message="";
			while(!found) {
			System.out.println("\nSelect Message or press b to go back");
			  message =scanner.nextLine();
			 
			 for(String mess:list_id) {
				 if(message.equals(mess)) {found=true;
				 							break;}
			 }
			 if(message.equalsIgnoreCase("b")) {found =true; getUserMenu(username);}
			 
			}
			String text;
			ResultSet rs2=stm.executeQuery("SELECT text from message where message_id="+ Integer.parseInt(message)+";");
			if(rs2.next())  {text=rs2.getString("text");
							 System.out.println("---------------------------------");
							 System.out.println(text);
							 System.out.println("");}
			
			
			
			
			db.connect().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void continueMenu(String username, String inboxOrOutbox) {
		Scanner scanner= new Scanner(System.in);
	
		System.out.println("Press b to go back, e to exit");
		String con=scanner.nextLine();
		boolean contin= false;
		while(!contin) {
			switch(con) {
			case "b": 
				if(inboxOrOutbox.equals("i")) {getInbox(username);contin=true;break;}
				else getOutbox(username);
			case "e": getUserMenu(username);contin=true;break;
			default: System.out.println("\nPress b for initial menu, e to exit");
						con=scanner.nextLine();continue;
			}
		}
	}

}
