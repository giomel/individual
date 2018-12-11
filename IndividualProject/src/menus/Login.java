package menus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Scanner;

import dbconnection.DBConnection;
import users.Admin;
import users.HighLevelUser;
import users.LowLevelUser;
import users.MiddleLevelUser;
import users.WriteToFile;

public class Login {
	
	
	public static void validateCredentials() {
		
		DBConnection.connectDB();
		DBConnection db= new DBConnection();
		db.connect();	
		Statement stm;
		
		Scanner scanner= new Scanner(System.in);
		boolean validation =false;
		
			System.out.println("+------------------------+\n| Communication Platform |\n+------------------------+\n");
		while(!validation) { 
			try {
			System.out.println("Username:");
			String username= scanner.nextLine();
			System.out.println("Password:");
			String password= scanner.nextLine();
		
			stm = db.connect().createStatement();
			ResultSet rs = stm.executeQuery("select password from user where username='"+username+"'");
			
			while(rs.next()) {
				String pass=rs.getString("password");
				if(pass.equals(password)) { System.out.println("Login successful!");
					WriteToFile.writeToFile("Login", username, LocalDateTime.now());
					
					ResultSet rstype=stm.executeQuery("select type from user where username='"+username+"' and password='"+
														password+"'");
					while (rstype.next()) {
						String type=rstype.getString("type");
						db.connect().close();
						switch(type) {
						case "admin":Admin.getAdminMenu(); validation=true;break;
						case "low": LowLevelUser.getUserMenu(username);validation=true; break;	
						case "middle": MiddleLevelUser.getUserMenu(username);validation=true; break;
						case "high": HighLevelUser.getUserMenu(username);validation=true;break;
						}	
					}
							
							break;
				}
				else {
					System.out.println("Invalid password");
					continue;
					}
				}
			
			} catch (SQLException e) {
				System.out.println("Username not found");
				
				continue;
		}
			
		}
		
		
		
	}
		public static void main(String [] args) {
				Login.validateCredentials();
		}
}
