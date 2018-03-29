package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MDRchannel extends ChannelInformation implements Runnable{
	
	private static MulticastSocket socket;
	
	public MDRchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}
	
	@Override
	public void run() {
		
		System.out.println(getGroupAddress() + "  " + getPort());
		
		boolean running = true;
		byte[] buf;
		DatagramPacket packet;
		
		joinChannel(getGroupAddress(), getPort());
		
		while(running) {
			
			buf = new byte[2048];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
			String receivedInfo = new String(packet.getData(), 0, packet.getLength());
			System.out.println(receivedInfo);
		
			
			
		}
	}
	
	public static void joinChannel(InetAddress groupAddress, int port) {
		
		try {
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(groupAddress);
			
		} catch (IOException e) {
			System.out.println("Unable to create a socket");
		}
	}
}
