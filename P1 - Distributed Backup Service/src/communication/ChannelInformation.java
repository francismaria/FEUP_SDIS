package communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

import structures.PeerInfo;

public abstract class ChannelInformation {

	private int port;
	private PeerInfo peer = null;
	private InetAddress groupAddress = null;
	
	public ChannelInformation(PeerInfo peer, String ipAddress, int port) throws UnknownHostException {
		this.peer = peer;
		this.groupAddress = InetAddress.getByName(ipAddress);
		this.port = port;
	}
	
	public InetAddress getGroupAddress() {
		return groupAddress;
	}
	
	public int getPort() {
		return port;
	}
	
	public PeerInfo getPeer() {
		return peer;
	}
	
	protected String checkMessageType(byte[] message) {
		
		byte[] typeBytes = new byte[10]; 		//10 is the sufficient number of bytes to parse the header to know which type the message is
		
		System.arraycopy(message, 0, typeBytes, 0, 10);
		
		String typeAux = new String(typeBytes);
		String[] parsedType = typeAux.split(" +");
		
		return parsedType[0];
	}
	
}
