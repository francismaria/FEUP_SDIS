package structures;

public class ChunkInfo {
	
	private byte[] data;
	private int fileID;
	//private int senderID;
	
	public ChunkInfo(int fileID, byte[] data) {
		this.fileID = fileID;
		this.data = data;
	}
	
	public int getFileId() {
		return fileID;
	}
	
	public byte[] getData() {
		return data;
	}
}
