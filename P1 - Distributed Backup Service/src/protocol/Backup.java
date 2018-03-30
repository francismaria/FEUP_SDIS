package protocol;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import communication.MCchannel;
import communication.MDBchannel;
import structures.PeerInfo;


public class Backup extends Thread{
	
	private File file = null;
	private int replicationDegree;
	
	private final static long MAX_CHUNK_SIZE = 64000;
	
	private MCchannel communicationChannel = null;
	private MDBchannel backupChannel = null;

	private DatagramSocket backupSocket = null;
	
	private PeerInfo peer;
	
	public Backup(PeerInfo peer, File file, int repDegree, MCchannel communicationChannel, MDBchannel backupChannel) {
		this.peer = peer;
		this.file = file;
		this.replicationDegree = repDegree;
		this.communicationChannel = communicationChannel;
		this.backupChannel = backupChannel;
	}
	
	public void run() {
		
		initSocket();

		FileInputStream is;

		try {
			is = new FileInputStream(file);
		
			byte[] chunk = new byte[(int)MAX_CHUNK_SIZE];
			
			int chunkLen = 0, chunkNo = 1;
			
			while((chunkLen = is.read(chunk)) != -1) {
				
				sendChunk(chunk, chunkNo);
				
				chunkNo++;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//sendMessage();

	}
	
	private void sendChunk(byte[] body, int chunkNo) {
		
		byte[] message = createPUTCHUNK(body, chunkNo); 
		
		/*byte[] buf = info.getBytes();*/
		DatagramPacket packet = new DatagramPacket(message, message.length, backupChannel.getGroupAddress(), backupChannel.getPort());
		
		try {
			backupSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] createPUTCHUNK(byte[] body, int chunkNo) {
		
		byte[] message = new byte[1024 + (int)MAX_CHUNK_SIZE];
		
		String headerString = "PUTCHUNK " + peer.getProtocolVersion() + " " + Integer.toString(peer.getId()) + 
				" " + "FILE_ID" + " " + Integer.toString(chunkNo) + " " + Integer.toString(replicationDegree);
		
		byte[] header = headerString.getBytes();
		byte[] delimiters = new byte[2];
		
		delimiters[0] = (byte)0xD;
		delimiters[1] = (byte)0xA;
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
		System.arraycopy(body, 0, message,header.length + delimiters.length, body.length);
		
		return message;
	}
	
	private void initSocket() {
		
		try {
			backupSocket = new DatagramSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket.");
		}
	}
	/*
	private void sendMessage() {
				
		String str = "MESSAGE TO MDB CHANNEL";
		byte[] buf = str.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, backupChannel.getGroupAddress(), backupChannel.getPort());
		
		try {
			backupSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
}
