package messages;

public abstract class Message {

	private String type;
	private int senderID;
	private String version;
	private String fileID;
	
	protected final static int MAX_HEADER_SIZE = 1024;
	
	public byte[] delimiters = new byte[2];
	
	public Message(String type, String version, int senderID, String fileID) {
		this.type = type;
		this.version = version;
		this.senderID = senderID;
		this.fileID = fileID;
		delimiters[0] = (byte)0xD;
		delimiters[1] = (byte)0xA;
	}
	
	public String getType() {
		return type;
	}
	
	public String getProtocolVersion() {
		return version;
	}
	
	public int getSenderId() {
		return senderID;
	}
	
	public String getFileId() {
		return fileID;
	}
}
