package protocol;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

import messages.DeleteMessage;
import structures.PeerInfo;

public class Delete implements Runnable {
	
	private File file = null;
	private PeerInfo peer = null;
	
	private DatagramSocket MCchannelSocket = null;
	
	public Delete(PeerInfo peer, File file) {
		this.peer = peer;
		this.file = file;
	}

	@Override
	public void run() {
		
		boolean running = true;
		
		initCommunicationSocket();
		
		//while(running) {
			
			sendDELETEMessage();
		//}
	}
	
	private void initCommunicationSocket() {
		
		try {
			MCchannelSocket = new MulticastSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket to communication channel");
		}
	}
	
	private void sendDELETEMessage() {
		
		DeleteMessage deleteFile = new DeleteMessage(peer.getProtocolVersion(), peer.getId(), "FILE_ID");
		
		byte[] message = deleteFile.getMessageBytes();
		
		DatagramPacket packet = new DatagramPacket(message, message.length,
				peer.getCommunicationChannel().getGroupAddress(),
				peer.getCommunicationChannel().getPort());
		
		try {
			MCchannelSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet");
		}
	}
}
