package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MCchannel extends ChannelInformation implements Runnable{
	
	private MulticastSocket socket;
	private static List<Integer> confirmedPeers = new ArrayList<Integer>();
	
	public MCchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
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
			
			String receivedInfo = new String(packet.getData(), 0, packet.getLength());
			System.out.println(receivedInfo);
		
		}
		
		try {
			socket.leaveGroup(getGroupAddress());
		} catch (IOException e) {
			System.out.println("Unable to leave Group.");
		}
		socket.close();
	}
	
	public static int getConfirmedPeers() {
		return confirmedPeers.size();
	}
}
