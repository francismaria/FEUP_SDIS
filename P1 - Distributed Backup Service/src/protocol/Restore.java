package protocol;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import communication.MCchannel;
import communication.MDRchannel;
import messages.GetchunkMessage;
import structures.FileInfo;
import structures.PeerInfo;

public class Restore implements Runnable{
	
	private String fileName = null;
	private PeerInfo peer = null;
	private FileInfo fileInfo = null;
	
	private MDRchannel restoreChannel = null;
	private MCchannel communicationChannel = null;
	
	private DatagramSocket communicationSocket = null;
	
	public Restore(PeerInfo info, String fileName, MCchannel communicationChannel, MDRchannel restoreChannel) {
		peer = info;
		this.fileName = fileName;
		this.communicationChannel = communicationChannel;
		this.restoreChannel = restoreChannel;
	}
	
	@Override
	public void run() {
		
		boolean running  = true;
		
		initSocket();
		
		fileInfo = peer.getFileInfo(fileName);
		
		if(fileInfo == null) {
			System.out.println("It was not found file to backup.");
			return;
		}
		
		int i = 0, numberOfChunks = fileInfo.getNumberOfChunks(); 

		while(i < numberOfChunks) {
			
			sendGetchunkRequest(fileInfo.getFileId(), i);	
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		
		joinAllChunks(fileInfo.getFileId(), numberOfChunks);
	}
	
	private void joinAllChunks(String fileID, int numberOfChunks) {
		
		byte[] file = new byte[2000000];
		int lastPos = 0;
		
		for(int i = 0; i < numberOfChunks; i++) {
			byte[] data = new byte[65024];
			
			data = restoreChannel.getChunkData(fileID, i);
			if(data == null) continue;
			
			System.out.println(data.length +"asasdsfs");
			
			System.arraycopy(data, 0, file, lastPos, data.length);
			lastPos += data.length;
		}
		
		peer.restoreFile(fileInfo.getPath(), file);
	}
	
	private void sendGetchunkRequest(String fileID, int chunkNo) {
		
		GetchunkMessage retrieveMessage = new GetchunkMessage(peer.getProtocolVersion(), peer.getId(), fileID, 
				chunkNo);
		
		byte[] message = retrieveMessage.getMessageBytes();
		
		DatagramPacket packet = new DatagramPacket(message, message.length, communicationChannel.getGroupAddress(),
				communicationChannel.getPort());
		System.out.println(packet.getLength());
		
		try {
			communicationSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet");
		}
	}
	
	private void initSocket() {
		
		try {
			communicationSocket = new DatagramSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket.");
		}
	}
}
