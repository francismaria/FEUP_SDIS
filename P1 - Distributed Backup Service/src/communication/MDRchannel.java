package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import messages.ChunkMessage;
import structures.PeerInfo;

public class MDRchannel extends ChannelInformation implements Runnable{
	
	private static MulticastSocket socket;
	
	private final static long MAX_MSG_SIZE = 65024;
	
	public MDRchannel(PeerInfo peer, String ipAddress, int port) throws UnknownHostException {
		super(peer, ipAddress, port);
	}
	
	@Override
	public void run() {
		
		System.out.println(getGroupAddress() + "  " + getPort());
		
		boolean running = true;
		byte[] buf;
		DatagramPacket packet;
		
		joinChannel(getGroupAddress(), getPort());
		
		while(running) {
			
			buf = new byte[(int)MAX_MSG_SIZE];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
			String type = checkMessageType(buf);
			
			switch(type) {
				case "CHUNK":
					parseCHUNKMessage(buf, packet.getLength());
				default:
					break;
			}
		}
	}
	
	private void parseCHUNKMessage(byte[] message, int messageLength) {
		
		ChunkMessage chunk = new ChunkMessage(messageLength);
		chunk.parseMessage(message);
		

		System.out.println("LENGTH:  ------------" + messageLength);
		System.out.println(chunk.getBody().length);
	}
	
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
