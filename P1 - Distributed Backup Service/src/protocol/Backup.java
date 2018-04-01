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
import messages.PutchunkMessage;
import structures.PeerInfo;


public class Backup extends Thread{
	
	private File file = null;
	private int replicationDegree;
	
	private final static long MAX_CHUNK_SIZE = 64000;
	private final static long MAX_MSG_SIZE = 65024;
	private static final int MAX_HEADER_SIZE = 1024;
	
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
				
				//while(confirmationPeers < repDegree)
				sendChunk(chunk, chunkNo);
				
				Thread.sleep(1000);
				
				if(communicationChannel.getConfirmedPeers() < replicationDegree) {
					System.out.println("NOT ENOUGH PEERS BACKING UP");
				}
				
				communicationChannel.restoreConfirmedPeers();
				chunkNo++;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File was not found.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted in sleep.");
		}

	}
	
	private void sendChunk(byte[] body, int chunkNo) {
		
		PutchunkMessage messageInfo = new PutchunkMessage(peer.getProtocolVersion(), peer.getId(), "FILE_ID", 
				chunkNo, replicationDegree, body);
		
		byte[] message = messageInfo.getMessageBytes();
		
		DatagramPacket packet = new DatagramPacket(message, message.length, backupChannel.getGroupAddress(), backupChannel.getPort());
		
		try {
			backupSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initSocket() {
		
		try {
			backupSocket = new DatagramSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket.");
		}
	}

}
