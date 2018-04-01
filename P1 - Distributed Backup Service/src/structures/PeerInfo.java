package structures;

import java.net.*;

import communication.*;


public class PeerInfo {
	
	private static int port;
	private static int peerID;
	private static String version;
	private static InetAddress address;
	private static MCchannel communicationChannel;
	private static MDBchannel backupChannel;
	private static MDRchannel restoreChannel;
	
	public PeerInfo(int peerID, String version, String multicastAddress, int multicastPort) throws UnknownHostException {
		this.peerID = peerID;
		this.version = version;
		//this.address = InetAddress.getByName(multicastAddress); 		//tem que ser o ip do peer!!
		//this.port = multicastPort;
	}
	
	public PeerInfo(int peerID, String version, MCchannel communication, MDBchannel backup, MDRchannel restore) {
		this.peerID = peerID;
		this.version = version;
		this.communicationChannel = communication;
		this.backupChannel = backup;
		this.restoreChannel = restore;
	}

	public static int getId() {
		return peerID;
	}
	
	public static String getProtocolVersion() {
		return version;
	}
	
	public static MCchannel getCommunicationChannel() {
		return communicationChannel;
	}
	
	public static MDBchannel getBackupChannel() {
		return backupChannel;
	}
	
	public static MDRchannel getRestoreChannel() {
		return restoreChannel;
	}
}
