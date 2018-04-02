package structures;

import java.util.ArrayList;
import java.util.List;

public class FileInfo {

	private String name;
	private String path;
	private String fileID;
	private int replicationDegree;
	private List<BackupChunkInfo> chunksInfo = new ArrayList<BackupChunkInfo>();
	
	public FileInfo(String name, String path, String fileID, int replicationDegree, List<BackupChunkInfo> chunksInfo) {
		this.name = name;
		this.path = path;
		this.fileID = fileID;
		this.replicationDegree = replicationDegree;
		this.chunksInfo = chunksInfo;
	}
	
	public int getNumberOfChunks() {
		return chunksInfo.size();
	}
	
	public String getName() {
		return name;
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
