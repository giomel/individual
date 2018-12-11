package users;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WriteToFile {
	PrintWriter writer;
	
	
	public static void writeToFile(String event, String username, LocalDateTime date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		String dateFormatted = date.format(formatter); 
		
		try(FileWriter fw = new FileWriter("Archive.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println("Date:\n"+dateFormatted);
			    out.println(event+": "+username);
			    out.println("------------------");
			 
			} catch (IOException e) {
			  
			}
		
	}
	public static void writeMessageToFile(String sender, String receiver,String text, LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String dateFormatted = date.format(formatter); 
		
		try(FileWriter fw = new FileWriter("Archive.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println("Date:\n"+dateFormatted);
			    out.println("Message From: "+sender);
			    out.println("To: "+receiver);
			    out.println("Message: "+text);
			    out.println("------------------");
			 
			} catch (IOException e) {
			  
			}
		
	}
	
	public static void writeMessageEditToFile(String event, String user, int messageId, LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String dateFormatted = date.format(formatter); 
		
		try(FileWriter fw = new FileWriter("Archive.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println("Date:\n"+dateFormatted);
			    out.println(event+" "+messageId);
			    out.println("By: "+user);
			    out.println("------------------");
			 
			} catch (IOException e) {
			  
			}
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
