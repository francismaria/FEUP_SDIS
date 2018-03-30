package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MDBchannel extends ChannelInformation implements Runnable{
	
	private static MulticastSocket socket;
	
	private final static int MAX_HEADER_SIZE = 1024;
	private final static long MAX_CHUNK_SIZE = 64000;
	
	private final static long MAX_MSG_SIZE = 65024;
	
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
			
			buf = new byte[(int)MAX_MSG_SIZE];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
			byte[] header = new byte[MAX_HEADER_SIZE];
			byte[] chunk = new byte[(int)MAX_CHUNK_SIZE];
			
			parseHeader(buf, header, chunk);
			
			String out = new String(header);
			System.out.println(out + " CHUNK SIZE: " + chunk.length);		//Está certo??
		}
		
		
	}
	
	public void parseHeader(byte[] message, byte[] header, byte[] body) {
		
		int headerLength = getHeaderLength(message);
		int bodyLength = message.length - (int)MAX_HEADER_SIZE;
		
		System.out.println(bodyLength);
		
		if(headerLength == 0) {
			System.out.println("Couldn't find header(?).");
			return;
		}
		
		System.arraycopy(message, 0, header, 0, headerLength);
		System.arraycopy(message, headerLength, body, 0, bodyLength);

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
