package protocol;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import communication.MCchannel;
import communication.MDRchannel;
import messages.GetchunkMessage;
import structures.PeerInfo;

public class Restore implements Runnable{
	
	private File file = null;
	private PeerInfo peer = null;
	
	private MDRchannel restoreChannel = null;
	private MCchannel communicationChannel = null;
	
	private DatagramSocket communicationSocket = null;
	
	public Restore(PeerInfo info, File file, MCchannel communicationChannel, MDRchannel restoreChannel) {
		peer = info;
		this.communicationChannel = communicationChannel;
		this.restoreChannel = restoreChannel;
	}
	
	@Override
	public void run() {
		
		boolean running  = true;
		
		int i = 0;
		initSocket();
		
		//while(running) {
			
			sendGetchunkRequest(i);
			
			i++;
		//}
	}
	
	private void sendGetchunkRequest(int chunkNo) {
		
		GetchunkMessage retrieveMessage = new GetchunkMessage(peer.getProtocolVersion(), peer.getId(), "FILE_ID", 
				chunkNo);
		
		byte[] message = retrieveMessage.getMessageBytes();
		
		DatagramPacket packet = new DatagramPacket(message, message.length, communicationChannel.getGroupAddress(),
				communicationChannel.getPort());
		
		try {
			System.out.println("entrou");
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
