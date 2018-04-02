package structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageInfo {

	private static int peerID;
	
	private static long diskSpace = 1000000;
	private static long usedSpace = 0;
	
	private static String peerDirPath;
	private static String backupDirPath;
	private static String chunkDirPath;
	
	private static List<ChunkInfo> savedChunks = new ArrayList<ChunkInfo>();
	
	public StorageInfo(int peerID) {
		this.peerID = peerID;
		initStorage();
	}
	
	private void initStorage() {
		
		//este tem de ser um path que dê em todos os PCS!!!   "/tmp/"
		peerDirPath = "/home/francisco/Desktop/Peer_" + peerID;
		File dir = new File(peerDirPath);
		
		if(!dir.exists()) {
			dir.mkdir();
		}
		//else ler o que lá está
		
		initChunkStorage();
		initBackupStorage();
	}

	
	private void initBackupStorage() {
		
		backupDirPath = peerDirPath + "/" + "BACKUP";
		
		File dir = new File(backupDirPath);
		
		if(!dir.exists()) {
			dir.mkdir();
		}
	}
	
	private void initChunkStorage() {
		
		chunkDirPath = peerDirPath + "/" + "CHUNKS";
		
		File dir = new File(chunkDirPath);
		
		if(!dir.exists()) {
			dir.mkdir();
		}
	}
	
	public void saveChunk(ChunkInfo chunk) {
		savedChunks.add(chunk);
		usedSpace += chunk.getData().length;
		
		String filePath = initFileDir(chunk);
		
		FileOutputStream stream;
		try {
			//stream = new FileOutputStream("/home/francisco/FEUP/SDIS/Files/myfile.jpg",true);
			String chunkPath = filePath + "/" + chunk.getChunkNo() + ".bin";
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
	
	private String initFileDir(ChunkInfo chunk) {
		
		String filePath = chunkDirPath + "/" + chunk.getFileId();
		
		File dir = new File(filePath);
		
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		return filePath;
	}
	
	public void deleteChunksOfFile(String fileID) {
		
		deleteFromArray(fileID);
		deleteFromDir(fileID);
	}
	
	private void deleteFromArray(String fileID) {
		
		for(ChunkInfo chunk : savedChunks) {
			if(chunk.getFileId().equals(fileID)) {
				savedChunks.remove(chunk);
			}
		}
	}
	
	private void deleteFromDir(String fileID) {
		
		String filesPath = chunkDirPath + "/" + fileID;
		File dir = new File(filesPath);
		
		if(!dir.exists())
			return;
		
		File[] allChunks = dir.listFiles();
		
		for(File chunk : allChunks) {
			chunk.delete();
		}
		dir.delete();
	}
	
	public long getTotalDiskSpace() {
		return diskSpace;
	}
	
	public long getUsedDiskSpace() {
		return usedSpace;
	}
	
	public long getAvailableDiskSpace() {
		return diskSpace-usedSpace;
	}
	
	public List<ChunkInfo> getStoredChunks(){
		return savedChunks;
	}
}
