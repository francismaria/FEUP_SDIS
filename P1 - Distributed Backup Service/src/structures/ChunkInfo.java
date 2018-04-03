package structures;

public class ChunkInfo {
	
	private byte[] data;
	private String fileID;
	private int chunkNo;
	
	public ChunkInfo() {}
	
	public ChunkInfo(String fileID, int chunkNo, byte[] data) {
		this.fileID = fileID;
		this.chunkNo = chunkNo;
		this.data = new byte[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);
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
	
	public int getSize() {
		return data.length/1024;
	}
}
