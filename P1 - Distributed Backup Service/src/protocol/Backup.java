package protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import communication.MCchannel;
import communication.MDBchannel;
import messages.PutchunkMessage;
import structures.BackupChunkInfo;
import structures.FileInfo;
import structures.PeerInfo;


public class Backup implements Runnable{
	
	private File file = null;
	private String fileID = null;
	private int replicationDegree;
	
	private final static long MAX_CHUNK_SIZE = 64000;
	private final static int MAX_ATTEMPTS = 5;
	
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
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hashed = digest.digest(file.getName().getBytes(StandardCharsets.UTF_8));
			//this.fileID = Base64.getEncoder().encodeToString(hashed);
			this.fileID = bytesToHex(hashed);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such Algorithm");
		}
	}
	
	// credits to : github: avilches
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
	
	public void run() {
		
		initSocket();

		FileInputStream is;

		try {
			is = new FileInputStream(file);
		
			byte[] chunk = new byte[(int)MAX_CHUNK_SIZE];
			
			int chunkLen = 0, chunkNo = 0;
			List<BackupChunkInfo> allChunkInfo = new ArrayList<BackupChunkInfo>();
			
			while((chunkLen = is.read(chunk)) != -1) {	
				
				int i = 0;
				long timeout = 1000;
				
				while(i < MAX_ATTEMPTS) {
					
					sendChunk(chunk, chunkNo, chunkLen);

					Thread.sleep(timeout);
					
					if(communicationChannel.getConfirmedPeers() >= replicationDegree) {
						break;
					}
					
					System.out.println("NOT ENOUGH PEERS BACKING UP\nAttempt: " + i + " timeout: " + timeout);
					timeout *= 2;
					i++;
					communicationChannel.restoreConfirmedPeers();
				}
				
				/*
				if(i == 5) {
					System.out.println("NOT ENOUGH PEERS");
				}
				else
					System.out.println("OK");
				*/
				
				BackupChunkInfo chunkInfo = new BackupChunkInfo(chunkNo,
						communicationChannel.getConfirmedPeers());
				allChunkInfo.add(chunkInfo);
				chunkNo++;
				communicationChannel.restoreConfirmedPeers();
			}
			
			saveFileInfo(allChunkInfo);
			
		} catch (FileNotFoundException e) {
			System.out.println("File was not found.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted in sleep.");
		}

	}
	
	private void saveFileInfo(List<BackupChunkInfo> chunksList) {
		
		FileInfo backupFile = new FileInfo(file.getName(), file.getPath(),
				fileID, file.length(), replicationDegree, chunksList);
		
		peer.saveBackupInfo(backupFile);
	}
	
	private void sendChunk(byte[] body, int chunkNo, int chunkLen) {
		
		PutchunkMessage messageInfo = new PutchunkMessage(peer.getProtocolVersion(), peer.getId(), this.fileID, 
				chunkNo, replicationDegree, body, chunkLen);
		
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
