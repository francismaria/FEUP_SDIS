package structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	private static String dirPath;
	
	private static List<ChunkInfo> savedChunks = new ArrayList<ChunkInfo>();
	
	private static long diskSpace = 1000000;
	private static long usedSpace = 0;
	
	public PeerInfo(int peerID, String version, MCchannel communication, MDBchannel backup, MDRchannel restore) {
		this.peerID = peerID;
		this.version = version;
		this.communicationChannel = communication;
		this.backupChannel = backup;
		this.restoreChannel = restore;
		initStorage();
	}
	
	private void initStorage() {
		
		//este tem de ser um path que dê em todos os PCS!!!   "/tmp/"
		dirPath = "/home/francisco/Desktop/Peer_" + peerID;
		File dir = new File(dirPath);
		
		if(!dir.exists()) {
			dir.mkdir();
		}
		//else ler o que lá está
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
		

		FileOutputStream stream;
		try {
			//stream = new FileOutputStream("/home/francisco/FEUP/SDIS/Files/myfile.jpg",true);
			String chunkPath = dirPath + "/" + chunk.getChunkNo() + ".bin";
			stream = new FileOutputStream(chunkPath);
			stream.write(chunk.getData());
			stream.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error ocurred with the path to the chunk location.");
			return;
		} catch (IOException e) {
			System.out.println("Writing error");
			return;
		}

	}
}
