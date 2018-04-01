package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import messages.GetchunkMessage;
import messages.StoredMessage;
import structures.PeerInfo;

public class MCchannel extends ChannelInformation implements Runnable{
	
	private MulticastSocket socket;
	private List<StoredMessage> confirmedPeers = new ArrayList<StoredMessage>();
	
	public MCchannel(PeerInfo peer, String ipAddress, int port) throws UnknownHostException {
		super(peer, ipAddress, port);
	}
	
	public int getConfirmedPeers() {
		return confirmedPeers.size();
	}
	
	@Override
	public void run() {
		System.out.println(getGroupAddress() + "  " + getPort());

		boolean running = true;
		byte[] buf;
		DatagramPacket packet;
		
		try {
			socket = new MulticastSocket(getPort());
			socket.setTimeToLive(1);
			socket.joinGroup(getGroupAddress());
		} catch (IOException e) {
			System.out.println("Unable to create a socket");
		}
		
		System.out.println("Connection Established: MC CHANNEL");
		
		while(running) {

			buf = new byte[2048];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
			String type = checkMessageType(buf);
			
			switch(type) {
				case "STORED":
					parseSTOREDMessage(buf);
					break;
				case "GETCHUNK":
					parseGETCHUNKMessage(buf);
					break;
				default:
					break;
			}
			/*
			String receivedInfo = new String(packet.getData(), 0, packet.getLength());
			System.out.println(receivedInfo.trim() + "---- this is MC channel speaking");
		*/
		}
		
		try {
			socket.leaveGroup(getGroupAddress());
		} catch (IOException e) {
			System.out.println("Unable to leave Group.");
		}
		socket.close();
	}
	
	private void parseSTOREDMessage(byte[] message) {
		
		StoredMessage receivingACK = new StoredMessage();	
		receivingACK.parseMessage(message);
		
		confirmedPeers.add(receivingACK);
	}
	
	private void parseGETCHUNKMessage(byte[] message) {
		
		GetchunkMessage receivingRequest = new GetchunkMessage();
		receivingRequest.parseMessage(message);
		
		if(receivingRequest.getSenderId() == getPeer().getId()) {
			return;
		}
		
		
	}
	
	public void restoreConfirmedPeers() {
		confirmedPeers.clear();
	}

}
