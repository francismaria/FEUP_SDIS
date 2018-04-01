package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import messages.PutchunkMessage;
import messages.StoredMessage;
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
			
			//String checkMessageType(buf);
			parsePUTCHUNKMessage(buf);

		}
		
		MCchannelSocket.close();
		socket.close();
	}
	

	
	public String[] parseHeader(String header) {
		
		String[] parsedHeader = header.split(" ");
		
		if(parsedHeader[0].equals("PUTCHUNK")) {
			//if(Integer.parseInt(parsedHeader[2]) == ) peer.getId()  não aceita pois vem do mesmo peer
			//acceptChunk();
			return parsedHeader;
		}
		return null;
	}
	
	public void parsePUTCHUNKMessage(byte[] message) {
		
		PutchunkMessage receivedMessage = new PutchunkMessage();
		
		receivedMessage.parseMessageBytes(message);
		
		//save chunk in file 
		sendACK(receivedMessage.getProtocolVersion(), getPeer().getId(), receivedMessage.getFileId(), receivedMessage.getChunkNo());
	}

	public void sendACK(String protocolVersion, int peerID, String fileID, int chunkNo) {
		
		StoredMessage ackMessage = new StoredMessage(protocolVersion, peerID, fileID, chunkNo);
		byte[] message = ackMessage.getMessageBytes();
		
		DatagramPacket responsePacket = new DatagramPacket(message, message.length,
				communicationChannel.getGroupAddress(), communicationChannel.getPort());
		
		try {
			long randomTime = (long)(Math.random()*401);
			Thread.sleep(randomTime);
			System.out.println(randomTime + "   random time");
			
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
