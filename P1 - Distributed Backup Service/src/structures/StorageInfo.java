package structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageInfo {

	private static int peerID;
	
	private static long diskSpace = 1000000;
	private static long usedSpace = 0;
	
	private static String peerDirPath;
	private static String backupDirPath;
	private static String chunkDirPath;
	
	private static List<ChunkInfo> savedChunks = new ArrayList<ChunkInfo>();
	private static List<FileInfo> backedUpFiles = new ArrayList<FileInfo>();
	
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
	
	public void saveBackup(FileInfo fileInfo) {
		
		backedUpFiles.add(fileInfo);
	
		String filePath = backupDirPath + "/" + fileInfo.getName() + ".txt";
		List<String> lines = Arrays.asList(fileInfo.toString());
		
		Path file = Paths.get(filePath);
		
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.out.println("Error writing to file.");
		}
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
	
	public FileInfo getFileInfo(String filePath) {
		
		for(FileInfo f : backedUpFiles) {
			if(f.getPath().equals(filePath)) {
				return f;
			}
		}
		return null;
	}

	public ChunkInfo getChunk(String fileID, int chunkNo) {
		
		for(ChunkInfo chunk : savedChunks) {
			if(chunk.getFileId().equals(fileID) && chunk.getChunkNo() == chunkNo) {
				return chunk;
			}
		}
		return null;
	}
	
	public void restoreFile(String filePath, byte[] data) {
		
		FileOutputStream stream;
		try {
			String path = filePath + ".jpg";
			stream = new FileOutputStream(path);
			stream.write(data);
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
