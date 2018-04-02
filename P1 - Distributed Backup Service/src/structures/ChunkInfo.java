package structures;

public class ChunkInfo {
	
	private byte[] data;
	private String fileID;
	private int chunkNo;
	
	public ChunkInfo() {}
	
	public ChunkInfo(String fileID, int chunkNo, byte[] data) {
		this.fileID = fileID;
		this.chunkNo = chunkNo;
		this.data = data;
	}
	
	public String getFileId() {
		return fileID;
	}
	
	public int getChunkNo() {
		return chunkNo;
	}
	
	public byte[] getData() {
		return data;
	}
}
