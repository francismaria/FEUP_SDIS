package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MDBchannel extends ChannelInformation implements Runnable{
	
	private static MulticastSocket socket;
	
	public MDBchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}

	@Override
	public void run() {
		System.out.println(getGroupAddress() + "  " + getPort());
		
		boolean running = true;
		byte[] buf;
		DatagramPacket packet;
		
		joinChannel(getGroupAddress(), getPort());
		
		System.out.println("Connection Established: MDB CHANNEL");
		
		while(running) {
			
			buf = new byte[70000];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
			parseHeader(buf);
		}
		
		
	}
	
	public void parseHeader(byte[] message) {
		
		int headerLength = getHeaderLength(message);
		
		if(headerLength == 0) {
			System.out.println("Couldn't find header(?).");
			return;
		}
		
		byte[] header = new byte[1024];
		
		System.arraycopy(message, 0, header, 0, headerLength);
		
		String out = new String(header);
		System.out.println(out);

	}
	
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
