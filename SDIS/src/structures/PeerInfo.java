package structures;

import java.net.*;

public class PeerInfo {
	
	private static int port;
	private static int peerID;
	private static InetAddress address;
	
	public PeerInfo(int peerID, String multicastAddress, int multicastPort) throws UnknownHostException {
		this.peerID = peerID;
		this.address = InetAddress.getByName(multicastAddress);
		this.port = multicastPort;
	}
	
	public PeerInfo(int peerID) {
		this.peerID = peerID;
	}

	public static int getPeerId() {
		return peerID;
	}
	
	
}
