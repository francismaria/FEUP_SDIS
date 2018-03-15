package protocol;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) throws IOException{
		
		Scanner s = new Scanner(System.in);
		boolean running = true;
		DatagramSocket socket = new DatagramSocket();
		
		byte buf[];
		InetAddress address = InetAddress.getLocalHost();
		DatagramPacket packet;
		String command = "";
		
		System.out.println("Welcome to Plate Managment System.");
		
		while(running){
			System.out.println("\n\n1-REGISTER\n2-LOOKUP\n\nEnter your option: ");
			String userInput = s.nextLine();
			
			switch(Integer.parseInt(userInput)){
			case 1:
				command = registerNewPlate(s);
				break;
			case 2:
				command = lookupPlate(s);
				break;
			case 0:
				running = false;
				break;
			default:
				System.out.println("Please enter a valid command.");
				break;
			}
			
			if(command.equals(""))
				continue;
			
			// send request
			buf = command.getBytes();
			packet = new DatagramPacket(buf, buf.length, address, 9999);
			socket.send(packet);
			
			// get response
			buf = new byte[2048];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String response = new String(packet.getData());
			
			System.out.println("\n\n" + response);
		}
		
		s.close();
	}

	public static String registerNewPlate(Scanner s){
		String plateNumber, command;
		String ownerName = new String(new char[256]);
		
		System.out.println("\n\tREGISTER A NEW PLATE");
		System.out.println("\nPLATE NUMBER: ");
		plateNumber = s.next(); s.nextLine();

		if(!matchString(plateNumber)){
			System.out.println("Not a valid plate match.");
			return "";
		}
		
		System.out.println("\nOWNER'S NAME: ");
		ownerName = s.nextLine();
		
		command = "register " + plateNumber + " " + ownerName;
		return command;
	}
	
	public static String lookupPlate(Scanner s){
		
		String plateNumber, command;
		
		System.out.println("\n\tLOOKUP PLATE INFORMATION\nPLATE NUMBER: ");
		plateNumber = s.next(); s.nextLine();
		
		if(!matchString(plateNumber)){
			System.out.println("Not a valid plate match.");
			return "";
		}
		
		command = "lookup " + plateNumber;
		return command;
	}
	
	public static boolean matchString(String plate){
		return plate.matches("[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}");
	}
}
