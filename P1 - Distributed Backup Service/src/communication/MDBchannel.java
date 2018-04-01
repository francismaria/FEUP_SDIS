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
	
	private final static int MAX_HEADER_SIZE = 1024;
	private final static long MAX_CHUNK_SIZE = 64000;
	
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
		
		joinChannel(getGroupAddress(), getPort());
		initCommunicationSocket();
		
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
			
			parseMessage(buf);

		}
		
		MCchannelSocket.close();
		socket.close();
	}
	
	public void initCommunicationSocket() {
		
		try {	
			MCchannelSocket = new MulticastSocket();
		} catch (IOException e) {
			System.out.println("Unable to create socket to MC channel");
			return;
		}
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
	
	/*public void acceptChunk(String[] parsedHeader) {
		
		//byte[] message = createSTORED(parsedHeader);
		//String s = "RESPONSE TO MDB CHANNEL";
		//byte[] buf = s.getBytes();
		DatagramPacket packet = new DatagramPacket(message, message.length, communicationChannel.getGroupAddress(), communicationChannel.getPort());
		
		try {
			MCchannelSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void parseMessage(byte[] message) {
		
		PutchunkMessage receivedMessage = new PutchunkMessage();
		
		receivedMessage.parseMessageBytes(message);
		
		sendACK(receivedMessage.getProtocolVersion(),/*peer.getId(),*/ receivedMessage.getFileId(), receivedMessage.getChunkNo());
	}

	public void sendACK(String protocolVersion, /*this peer ID,*/ String fileID, int chunkNo) {
		
		StoredMessage ackMessage = new StoredMessage(protocolVersion, 0, fileID, chunkNo);
		byte[] message = ackMessage.getMessageBytes();
		
		DatagramPacket responsePacket = new DatagramPacket(message, message.length,
				communicationChannel.getGroupAddress(), communicationChannel.getPort());
		
		try {
			MCchannelSocket.send(responsePacket);
		} catch (IOException e) {
			System.out.println("Unable to send response socket to MC channel.\n");
		}
		
	}
	/*
	public int getHeaderLength(byte[] message) {
		
		for(int i = 0; i < message.length; i++) {
			if(message[i] == (byte)0xD) {
				i++;
				if(message[i] == (byte)0xA) {
					return i+1;
				}
				else {
					i--;
				}
			}
		}
		return 0;
	}
	*/
	public void joinChannel(InetAddress groupAddress, int port) {
		
		try {
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(groupAddress);
			
		} catch (IOException e) {
			System.out.println("Unable to create a socket");
		}
	}
}
