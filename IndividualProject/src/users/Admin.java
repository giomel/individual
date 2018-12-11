package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Scanner;

import dbconnection.DBConnection;
import menus.Login;

public class Admin {
	
	public static void getAdminMenu() {
		Scanner scanner= new Scanner(System.in);
		System.out.println("+------------------------+\n| Choose among options   |\n+------------------------+\n"
							+ "1.Show UserList/Update User\n2.Delete User\n3.Create User\n4.Logout");
			String choice =scanner.nextLine();
			boolean wrong=false;
			while(!wrong)

			switch(choice) {
			case "1": getUserList(); wrong=true;break;
			case "2": removeUser();wrong=true;break;	
			case "3": createUser();wrong=true;break;
			case "4": WriteToFile.writeToFile("Logout", "admin", LocalDateTime.now());Login.validateCredentials();wrong=true;break;
			default: System.out.println("Choose 1 for User List/Update User, 2 for User Delete, 3 for User Creation, 4 to Logout");
						choice=scanner.nextLine(); continue;
			}
	}
	
	
	
	public static void getUserList() {
		DBConnection db= new DBConnection();	
		Statement stm;
		Scanner scanner= new Scanner(System.in);
		try {
			stm = db.connect().createStatement();
			ResultSet set= stm.executeQuery("select * from user;");
			System.out.println("User List\n------------------------------");
			while(set.next()) {
				int id=set.getInt("user_id");
				String username= set.getString("username");
				//String password= set.getString("password");
				String type= set.getString("type");
				
				
				System.out.println("#: "+id );
				System.out.println("username: "+username);
				System.out.println("type: "+type);
				System.out.println("------------------------------");
			}
			System.out.println("\nPress u to update a user or c to continue");
			String choice= scanner.nextLine();
			boolean con=true;
			
			while(con) {
				switch(choice) {
				case "u":db.updateUser();
				case "c":continueMenu();
				default: System.out.println("\nPress u to update a user or c to continue");
				 choice= scanner.nextLine();continue;
				}
				
			}
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createUser() {
		Scanner scanner= new Scanner(System.in);
		DBConnection db= new DBConnection();
		Statement stm;
		String name=null;
		String password=null;
		String type=null;
		
		System.out.println("Give a new username(1-8 characters):");
		name= scanner.nextLine();
		
		while(name.length()>8 || name.length()<1) {
		System.out.println("Username must be 1-8 characters:");
		name= scanner.nextLine();}
		
		System.out.println("Give a password(4-10 characters):");
		password= scanner.nextLine();
		while((password.length()>10)||(password.length()<4)) {
		System.out.println("Password must be 4-10 characters:");
		password= scanner.nextLine();}
		
		System.out.println("Give user type (low, middle or high):");
		type=scanner.nextLine();
		
		while(!(type.equalsIgnoreCase("low"))&&(!(type.equalsIgnoreCase("middle")))
				&&(!(type.equalsIgnoreCase("high")))){
			System.out.println("Available types are low, middle or high:");
			type=scanner.nextLine();
		}
		
		db.insertUser(name, password, type);
		System.out.println("User created!");
		continueMenu();
		
	}
	
	public static void removeUser() {
		Scanner scanner= new Scanner(System.in);
		DBConnection.connectDB();
		DBConnection db= new DBConnection();
		Statement stm;
		db.connect();	
		
		
		
		ResultSet rs2;
		boolean exists=false;
		int find=0;
		while(!exists) {
			System.out.println("Give a valid username:");
			String username =scanner.nextLine();
		
	
		try {
			stm = db.connect().createStatement();
			rs2 = stm.executeQuery("select count(username) from user where username='"+username+"'");
			if(rs2.next())  find=rs2.getInt("count(username)");
			System.out.println(find);
			if(find==0)continue;
			else {db.deleteUser(username);
					System.out.println("User deleted!");
					db.connect().close();
					continueMenu();
					exists=true;}
				
				
		} catch (SQLException e) {
			
			
			continue;
		}
		
		}
		
	}
	
	
	public static void continueMenu() {
		Scanner scanner= new Scanner(System.in);
	
		System.out.println("Press i for initial menu, e to exit");
		String con=scanner.nextLine();
		boolean contin= false;
		while(!contin) {
			switch(con) {
			case "i": getAdminMenu();contin=true;break;
			case "e": Login.validateCredentials();contin=true;break;
			default: System.out.println("\nPress i for initial menu, e to exit");
						con=scanner.nextLine();continue;
			}
		}
	}
	
	
}
