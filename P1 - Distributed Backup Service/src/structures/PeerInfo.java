package structures;

import java.net.*;

public class PeerInfo {
	
	private static int port;
	private static int peerID;
	private static String version;
	private static InetAddress address;
	
	public PeerInfo(int peerID, String version, String multicastAddress, int multicastPort) throws UnknownHostException {
		this.peerID = peerID;
		this.version = version;
		//this.address = InetAddress.getByName(multicastAddress); 		//tem que ser o ip do peer!!
		//this.port = multicastPort;
	}
	
	public PeerInfo(int peerID, String version) {
		this.peerID = peerID;
		this.version = version;
	}

	public static int getId() {
		return peerID;
	}
	
	public static String getProtocolVersion() {
		return version;
	}
}
