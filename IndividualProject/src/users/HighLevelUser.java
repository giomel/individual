package users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dbconnection.DBConnection;
import menus.Login;

public class HighLevelUser extends MiddleLevelUser {
	
	
public static void getUserMenu(String user) {
		
		Scanner scanner= new Scanner(System.in);
		System.out.println("+------------------------+\n| Choose among options   |\n+------------------------+\n"
							+ "1.New Message\n2.Inbox\n3.Outbox\n4.Delete Message\n5.Logout");
			String choice =scanner.nextLine();
			boolean wrong=false;
			while(!wrong) {
			switch(choice) {
			case "1": sendMessage(user); getUserMenu(user);break;
			case "2": getInbox(user);getUserMenu(user); break;
			case "3": int message_id=getOutboxEdit(user);continueMenu(user,message_id);break;	
			case "4": deleteMessages(user);getUserMenu(user);break;
			case "5": WriteToFile.writeToFile("Logout", user, LocalDateTime.now());Login.validateCredentials();break;
			default: System.out.println("Choose 1 for New Message, 2 for Inbox, 3 for Outbox, 4 to Delete Message, 5 to Logout");
			choice=scanner.nextLine(); continue;
			}
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
		;
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
						 System.out.println("");}
		
		
		}
		db.connect().close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}



public static int getOutboxEdit(String username) {
	DBConnection db= new DBConnection();	
	Statement stm;
	Scanner scanner = new Scanner(System.in);
	
	try {
		stm = db.connect().createStatement();
		int sender_id=0;
		ResultSet rs1=stm.executeQuery("select user_id from user where username='"+username+"';");
		if(rs1.next()) sender_id=rs1.getInt("user_id");
		
		
		
		ResultSet rs = stm.executeQuery("SELECT message.message_id,user.username, message.description, message.date FROM user, message "+
				"where message.sender_id=user.user_id "+
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
		 if(message.equalsIgnoreCase("b")) {found=true; getUserMenu(username);}
		}
		String text;
		int message_id=Integer.parseInt(message);
		ResultSet rs2=stm.executeQuery("SELECT text from message where message_id="+message_id +";");
		if(rs2.next())  {text=rs2.getString("text");
						 System.out.println("---------------------------------");
						 System.out.println(text);
						 System.out.println("");}
		
		//continueMenu(username,message_id );
		return message_id;
		
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
	
}




public static void editMessage(String username,int message_id) {
	
	DBConnection db= new DBConnection();
	Scanner scanner= new Scanner(System.in);
	try {
		boolean textlength=false;
		String text=null;
		while(!textlength) {
			System.out.println("Provide new message text(250 characters):\n-----------");
			text= scanner.nextLine();
			if (text.length()<=250) textlength=true;
			else continue;
		}
		
		PreparedStatement ps=db.connect().prepareStatement("UPDATE message SET text='"+text+"' where message_id="+message_id);
		ps.executeUpdate();
		System.out.println("Message Edited!");
		WriteToFile.writeMessageEditToFile("Message edited:", username, message_id, LocalDateTime.now());
		db.connect().close();
		getUserMenu(username);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}


	public static void deleteMessages(String username) {
		DBConnection db=new DBConnection();
		Scanner scanner= new Scanner(System.in);
		try {
			
			Statement stm= db.connect().createStatement();
			ResultSet rs=stm.executeQuery("select * from message;");
			
			List<String> list= new ArrayList<>();
			System.out.println("All Messages");
			System.out.println("---------------------------------");
			while(rs.next()) {
				int message_id=rs.getInt("message_id");
				list.add(""+message_id);
				
				int sender_id=rs.getInt("sender_id");
				int receiver_id=rs.getInt("receiver_id");
				String description=rs.getString("description");
				String date=rs.getString("date");
				
				System.out.println("#: "+message_id);
				System.out.println("From: "+sender_id);
				System.out.println("To: "+receiver_id);
				System.out.println("Description: "+description);
				System.out.println("Date: "+date);	
				System.out.println("---------------------------------");
				
			}
			
			boolean found=false;
			String message="";
			while(!found) {
			System.out.println("\nSelect Message to Delete or press b to go back");
			  message =scanner.nextLine();
			 
			 for(String mess:list) {
				 if(message.equals(mess)) {found=true;
				 							break;}
			 }
			 
			 if(message.equalsIgnoreCase("b")) {found=true; getUserMenu(username);}
			}
			
			PreparedStatement ps =db.connect().prepareStatement("delete from message where message_id="+Integer.parseInt(message)+";");
			ps.executeUpdate();
			System.out.println("Message deleted!");
			WriteToFile.writeMessageEditToFile("Message edited:", username, Integer.parseInt(message), LocalDateTime.now());
			//continueMenu(username);
			db.connect().close();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void continueMenu(String username, int message_id) {
		Scanner scanner= new Scanner(System.in);
	
		System.out.println("Press b to go back, e to edit Message");
		String con=scanner.nextLine();
		boolean contin= false;
		while(!contin) {
			switch(con) {
			case "b": getUserMenu(username); contin=true; break;
			case "e": editMessage(username, message_id);contin=true;break;
			default: System.out.println("\nPress b for initial menu, e to edit Message");
						con=scanner.nextLine();continue;
			}
		}
	}
	

	
}
