package structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import communication.*;


public class PeerInfo {
	
	private static int peerID;
	private static String version;
	private static MCchannel communicationChannel;
	private static MDBchannel backupChannel;
	private static MDRchannel restoreChannel;
	
	private static StorageInfo storage;
	
	public PeerInfo(int peerID, String version, MCchannel communication, MDBchannel backup, MDRchannel restore) {
		this.peerID = peerID;
		this.version = version;
		this.communicationChannel = communication;
		this.backupChannel = backup;
		this.restoreChannel = restore;
		storage = new StorageInfo(peerID);
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
		return storage.getStoredChunks();
	}
	
	public static long getTotalDiskSpace() {
		return storage.getTotalDiskSpace();
	}
	
	public static long getUsedSpace() {
		return storage.getUsedDiskSpace();
	}
	
	public static long getAvailableSpace() {
		return storage.getAvailableDiskSpace();
	}
	
	public static void deleteChunksOfFile(String fileID) {
		storage.deleteChunksOfFile(fileID);
	}
	
	public static void saveChunk(ChunkInfo chunk) {
		storage.saveChunk(chunk);
	}
	
	public void saveBackupInfo(FileInfo file) {
		storage.saveBackup(file);
	}
	
	public FileInfo getFileInfo(String filePath) {
		return storage.getFileInfo(filePath);
	}
	
	public String toString() {
		
		return "to String method";
	}
}
