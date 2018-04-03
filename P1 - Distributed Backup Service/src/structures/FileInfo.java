package structures;

import java.util.ArrayList;
import java.util.List;

public class FileInfo {

	private String name;
	private String path;
	private String fileID;
	private long bytes;
	private int replicationDegree;
	private List<BackupChunkInfo> chunksInfo = new ArrayList<BackupChunkInfo>();
	
	public FileInfo(String name, String path, String fileID, long bytes, int replicationDegree, List<BackupChunkInfo> chunksInfo) {
		this.name = name;
		this.path = path;
		this.fileID = fileID;
		this.bytes = bytes;
		this.replicationDegree = replicationDegree;
		this.chunksInfo = chunksInfo;
	}
	
	public int getNumberOfChunks() {
		return chunksInfo.size();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getFileId() {
		return fileID;
	}
	
	public long getBytes() {
		return bytes;
	}
	
	@Override
	public String toString() {
		String ret = path + " " + fileID + " " + replicationDegree;
		
		for(BackupChunkInfo info : chunksInfo) {
			ret += " " + info.toString();
		}
		
		return ret;
	}
}
