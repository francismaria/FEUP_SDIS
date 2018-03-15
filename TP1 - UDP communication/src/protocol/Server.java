package protocol;

import java.io.IOException;
import java.net.*;
import java.util.*;

import objects.PlateInfo;

public class Server {
	
	private static List<PlateInfo> plates = new ArrayList<PlateInfo>();
	
	public static void main(String[] args) throws Exception{
		
		DatagramSocket socket = new DatagramSocket(9999);
		boolean running = true;
		DatagramPacket packet;
		int port;
		InetAddress address;
		byte[] buf;
		String[] parsedCommand;
		String response = "";
		
		while(running){
			
			buf = new byte[2048];
			
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			
			byte[] data = new byte[packet.getLength()];
			System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
			
			String command = new String(data);
			
			parsedCommand = command.split(" ");
			
			if(parsedCommand[0].equals("register"))
				response = registerCommand(parsedCommand[1], parsedCommand[2]);
			else
				response = lookupCommand(parsedCommand[1]);
			
			buf = response.getBytes();
			address = packet.getAddress();
			port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);
			
		}
		
		socket.close();
	}

	public static String registerCommand(String plateNumber, String ownerName){
		
		if(getPlate(plateNumber) != null){
			return "-1 " + plateNumber + " " + ownerName; 
		}
		else{
			addNewPlate(plateNumber, ownerName);
			return Integer.toString(plates.size()) + " " + plateNumber + " " + ownerName;
		}
	}
	
	public static String lookupCommand(String plateNumber){
		
		PlateInfo p = getPlate(plateNumber);
		
		if(p != null){
			System.out.println(plateNumber + " encountered.");
			return Integer.toString(plates.size()) + " " + plateNumber + " " + p.getOwnerName();
		}

		System.out.println(plateNumber + " was not encountered");
		return "-1 " + plateNumber;
	}
	
	public static void addNewPlate(String plateNumber, String ownerName){
		plates.add(new PlateInfo(plateNumber, ownerName));
		System.out.println("Added new plate");
	}
	
	public static PlateInfo getPlate(String plateNumber){
		
		for(PlateInfo p : plates){
			if(p.getPlateNumber().equals(plateNumber)){
				return p;
			}
		}
		return null;
	}
}
