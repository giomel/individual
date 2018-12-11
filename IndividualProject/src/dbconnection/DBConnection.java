package dbconnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import users.Admin;

public class DBConnection {
	
	Connection connection;
	DriverManager dm;
	Statement stm;
	PreparedStatement pst;
	
	public DBConnection() {
		// TODO Auto-generated constructor stub
	}
	
	public static void connectDB() {
		DBConnection db= new DBConnection();
		db.connect();
	}
	
	public Connection connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/individual?autoReconnect=true&useSSL=false", "root", "18051990");
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public int executeStatement(String sql) {
		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				int name = rs.getInt("user_id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				System.out.println(name + username+password);
				
			}
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int insertMessage(int sender_id, int receiver_id, String description, String text, String date) {
		try {
			String query = ("insert into message values(null,?,?,?,?,?);");
			PreparedStatement ptm = connection.prepareStatement(query);
			ptm.setInt(1, sender_id);
			ptm.setInt(2, receiver_id);
			ptm.setString(3, description);
			ptm.setString(4, text);
			ptm.setString(5,date);
			ptm.executeUpdate();
			
			connection.close();
			System.out.println("Message sent!");
			return 1;
			//ptm.setString(1, type);
			//ptm.setInt(2, name );
	
		}catch (SQLException e) {

			e.printStackTrace();
			return -1;
		}
	}
	
	
	public int insertUser(String username, String password, String type) {
		try {
			String query = ("insert into user values(null,?,?,?);");
			PreparedStatement ptm = connect().prepareStatement(query);
			ptm.setString(1, username);
			ptm.setString(2, password);
			ptm.setString(3, type);
			ptm.executeUpdate();
			
			connection.close();
			return 1;
			//ptm.setString(1, type);
			//ptm.setInt(2, name );
		
		}catch (SQLException e) {

			e.printStackTrace();
			return -1;
		}
	}
	
	public int deleteUser(String username) {
		
		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("select user_id from user where username='"+username+"'");
			int id=0;
			while (rs.next()) {
				 id= rs.getInt("user_id");
				//System.out.println(id);
			}
			
			String query = ("delete from user where user_id='"+id+"'");
			PreparedStatement ptm = connection.prepareStatement(query);
			ptm.executeUpdate();
			connection.close();
			return 1;
			//ptm.setString(1, type);
			//ptm.setInt(2, name );
	
		}catch (SQLException e) {

			e.printStackTrace();
			return -1;
		}
	}
	
	public void updateUser() {
		Scanner scanner= new Scanner(System.in);
		
		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("select username from user;");
			List<String> users=new ArrayList<>();
			while(rs.next()) {users.add(rs.getString("username"));}
			
			
			System.out.println("Give a username you want to update");
			String username = scanner.nextLine();
			boolean found=false;
			while(!found) {
			  for(String user:users) {
				if(username.equals(user)) {
					
					found=true;
				    break;}
			  }
			  
			if(found) break;
			else{System.out.println("Username not found. Give a valid username.");
				 username=scanner.nextLine();}
			}
			
			System.out.println("Press u to update username, p to update password, t to update type");
			String choice =scanner.nextLine();
			
			boolean wrong=false;
			while(!wrong) {
				switch(choice) {
				case "u": System.out.println("Give a new username(1-8 characters):");
				 		  String name= scanner.nextLine();
				
				 		  while(name.length()>8 || name.length()<1) {
				 		  System.out.println("Username must be 1-8 characters:");
				 		  name= scanner.nextLine();}
				 		  
				 		  pst=connection.prepareStatement("update user set username='"+name+"' where username='"+username+"';");
				 		  pst.executeUpdate();
				 		  System.out.println("User updated!");
				 		  Admin.continueMenu();
				 		  wrong=true;
				 		  break;
				 		  
				case "p":	System.out.println("Give a password(4-10 characters):");
							String password= scanner.nextLine();
							while((password.length()>10)||(password.length()<4)) {
									System.out.println("Password must be 4-10 characters:");
									password= scanner.nextLine();}
				 		  pst=connection.prepareStatement("update user set password='"+password+"' where username='"+username+"';");
				 		  pst.executeUpdate();
				 		  System.out.println("User updated!");
				 		  Admin.continueMenu();
				 		  wrong=true;
				 		  break;
				 		  
				case "t": 	System.out.println("Give user type (low, middle or high):");
							String type=scanner.nextLine();
				
							while(!(type.equalsIgnoreCase("low"))&&(!(type.equalsIgnoreCase("middle")))
									&&(!(type.equalsIgnoreCase("high")))){
								System.out.println("Available types are low, middle or high:");
								type=scanner.nextLine();
							}
							
							 pst=connection.prepareStatement("update user set type='"+type+"' where username='"+username+"';");
							 pst.executeUpdate();
					 		  System.out.println("User updated!");
					 		  Admin.continueMenu();
					 		  wrong=true;
					 		  break;
					 		  
				default: System.out.println("Press u to update username, p to update password, t to update type");
						 choice =scanner.nextLine();continue;
				
				
				}
			}
			
			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
