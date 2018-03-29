package protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.Scanner;

import communication.MDBchannel;

public class Backup extends Thread{
	
	private File file = null;
	private MDBchannel backupChannel = null;
	
	public Backup(File file, MDBchannel backupChannel) {
		this.file = file;
		this.backupChannel = backupChannel;
	}
	
	public void run() {
	
		System.out.println("STARTING BACKUP THREAD " + file.getAbsolutePath() + "\n"
				+ "Size of file: " + file.length());
		
		DatagramSocket backupSocket = null;
		try {
			backupSocket = new DatagramSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String str = "MESSAGE TO MDB CHANNEL";
		byte[] buf = str.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, backupChannel.getGroupAddress(), backupChannel.getPort());
		
		try {
			backupSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		Scanner input = null;
		
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("It occurred an error with the file. File was not found.");
		}
		
		while(input.hasNextLine()) {
			System.out.println(input.nextLine());
		}*/
	}

}
