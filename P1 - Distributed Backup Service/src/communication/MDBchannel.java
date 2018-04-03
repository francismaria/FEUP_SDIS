package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import messages.PutchunkMessage;
import messages.StoredMessage;
import structures.ChunkInfo;
import structures.PeerInfo;

public class MDBchannel extends ChannelInformation implements Runnable{
	
	private MulticastSocket socket;
	private MulticastSocket MCchannelSocket;
	private MCchannel communicationChannel = null; 
	
	private final static long MAX_MSG_SIZE = 65024;
	
	public MDBchannel(MCchannel communicationChannel, PeerInfo peer, String ipAddress, int port) throws UnknownHostException {
		super(peer, ipAddress, port);
		this.communicationChannel = communicationChannel;
	}

	@Override
	public void run() {
		System.out.println(getGroupAddress() + "  " + getPort());
		
		boolean running = true;
		byte[] buf;
		DatagramPacket packet;
		
		initCommunications();
		
		System.out.println("Connection Established: MDB CHANNEL");
		
		while(running) {
			
			buf = new byte[(int)MAX_MSG_SIZE];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
				continue;
			}
			
			String type = checkMessageType(buf);
			
			switch(type) {
			case "PUTCHUNK":
				parsePUTCHUNKMessage(buf, packet.getLength());
				break;
			default:
				break;
			}
			
		}
		
		MCchannelSocket.close();
		socket.close();
	}
	

	public void parsePUTCHUNKMessage(byte[] message, int messageLength) {
		
		PutchunkMessage receivedMessage = new PutchunkMessage(messageLength);
		receivedMessage.parseMessageBytes(message);
		
		if(receivedMessage.getSenderId() == getPeer().getId()) {
			System.out.println("This peer is the initiator-peer");
			return;			//it does not accepts messages from itself
		}
		
		if(receivedMessage.getBody().length > getPeer().getAvailableSpace()) {
			System.out.println("This peer hasn't got sufficient disk space to save this chunk: " + receivedMessage.getFileId() + " "
					+ "" + receivedMessage.getChunkNo());
			return;
		}
		
		saveChunk(receivedMessage);
		sendACK(receivedMessage.getProtocolVersion(), getPeer().getId(), receivedMessage.getFileId(), receivedMessage.getChunkNo());
	}

	public void saveChunk(PutchunkMessage receivedMessage) {
		
		ChunkInfo chunk = new ChunkInfo(receivedMessage.getFileId(), receivedMessage.getChunkNo(),
				receivedMessage.getBody());
		getPeer().saveChunk(chunk);
		
		System.out.println(PeerInfo.getAvailableSpace());
	}
	
	public void sendACK(String protocolVersion, int peerID, String fileID, int chunkNo) {
		
		StoredMessage ackMessage = new StoredMessage(protocolVersion, peerID, fileID, chunkNo);
		byte[] message = ackMessage.getMessageBytes();
		
		DatagramPacket responsePacket = new DatagramPacket(message, message.length,
				communicationChannel.getGroupAddress(), communicationChannel.getPort());
		
		try {	
			long randomTime = (long)(Math.random()*401);
			Thread.sleep(randomTime);
			MCchannelSocket.send(responsePacket);
		} catch (IOException e) {
			System.out.println("Unable to send response socket to MC channel.\n");
		} catch (InterruptedException e) {
			System.out.println("Error ocurred in sleeping.");
		}	
	}
	
	public void joinMDBchannel(InetAddress groupAddress, int port) {
		
		try {
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(groupAddress);
			
		} catch (IOException e) {
			System.out.println("Unable to create a socket");
		}
	}
	
	public void initMCsocket() {
		
		try {	
			MCchannelSocket = new MulticastSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket to MC channel");
			return;
		}
	}
	
	private void initCommunications() {
		
		joinMDBchannel(getGroupAddress(), getPort());
		initMCsocket();
	}
}
