package structures;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import communication.*;


public class PeerInfo {
	
	private static int peerID;
	private static String version;
	private static MCchannel communicationChannel;
	private static MDBchannel backupChannel;
	private static MDRchannel restoreChannel;
	
	private static List<ChunkInfo> savedChunks = new ArrayList<ChunkInfo>();
	
	private static long diskSpace = 1000000;
	private static long usedSpace = 0;
	
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
	
	public static List<ChunkInfo> getSavedChunks(){
		return savedChunks;
	}
	
	public static long getTotalDiskSpace() {
		return diskSpace;
	}
	
	public static long getUsedSpace() {
		return usedSpace;
	}
	
	public static long getAvailableSpace() {
		return diskSpace-usedSpace;
	}
	
	public static void saveChunk(ChunkInfo chunk) {
		savedChunks.add(chunk);
		usedSpace += chunk.getData().length;
	}
}
