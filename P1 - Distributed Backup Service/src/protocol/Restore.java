package protocol;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;

import communication.MCchannel;
import communication.MDRchannel;
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
		
		initSocket();
		
		//while(running) {
			
			sendGetchunkRequest();
		//}
	}
	
	private void sendGetchunkRequest() {
		
		
	}
	
	private void initSocket() {
		
		try {
			communicationSocket = new DatagramSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket.");
		}
	}
}
