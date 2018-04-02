package protocol;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import messages.DeleteMessage;
import structures.PeerInfo;

public class Delete implements Runnable {
	
	private File file = null;
	private String fileID = null;
	private PeerInfo peer = null;
	
	private DatagramSocket MCchannelSocket = null;
	
	public Delete(PeerInfo peer, File file) {
		this.peer = peer;
		this.file = file;		
		setFileID();
	}

	private void setFileID() {
		
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
		
		DeleteMessage deleteFile = new DeleteMessage(peer.getProtocolVersion(), peer.getId(), this.fileID);
		
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
