package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import messages.ChunkMessage;
import messages.DeleteMessage;
import messages.GetchunkMessage;
import messages.StoredMessage;
import structures.PeerInfo;

public class MCchannel extends ChannelInformation implements Runnable{
	
	private MulticastSocket socket;
	private MulticastSocket MDRchannelSocket;
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
			MDRchannelSocket = new MulticastSocket();
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
				case "DELETE":
					parseDELETEMessage(buf, packet.getLength());
					break;
				default:
					break;
			}
		}
		
		try {
			socket.leaveGroup(getGroupAddress());
		} catch (IOException e) {
			System.out.println("Unable to leave Group.");
		}
		socket.close();
		MDRchannelSocket.close();
	}
	
	private void parseSTOREDMessage(byte[] message) {
		
		StoredMessage receivingACK = new StoredMessage();	
		receivingACK.parseMessage(message);
		
		confirmedPeers.add(receivingACK);
	}
	
	private void parseGETCHUNKMessage(byte[] message) {
		
		GetchunkMessage receivingRequest = new GetchunkMessage();
		receivingRequest.parseMessage(message);
		/*
		if(receivingRequest.getSenderId() == getPeer().getId()) {
			return;
		}*/
		
		//se n tiver o chunk requisitado : return;
		try {
			Thread.sleep((long)Math.random()*401);
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted while sleeping.");
		}
		
		sendCHUNKmessage(receivingRequest.getProtocolVersion(), getPeer().getId(), receivingRequest.getFileId(), 0);
	}
	
	private void sendCHUNKmessage(String protocolVersion, int peerID, String fileID, int chunkNo) {
		
		byte[] exemplo = "ISTO É UM ACK SE TIVER.".getBytes();
		
		ChunkMessage ackMessage = new ChunkMessage(protocolVersion, peerID, fileID, chunkNo, exemplo);
		byte[] message = ackMessage.getMessageBytes();
		
		DatagramPacket packet = new DatagramPacket(message, message.length,
				getPeer().getRestoreChannel().getGroupAddress(), 
				getPeer().getRestoreChannel().getPort());
		
		try {
			MDRchannelSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet through MDR channel");
		}
	}
	
	private void parseDELETEMessage(byte[] message, int messageLength) {
		
		DeleteMessage deleteMessage = new DeleteMessage(messageLength);
		deleteMessage.parseMessage(message);
		
		System.out.println(deleteMessage.getFileId());
		
		//do smth
	}
	
	public void restoreConfirmedPeers() {
		confirmedPeers.clear();
	}

}
