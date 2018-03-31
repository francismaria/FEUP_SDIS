package messages;

public abstract class Message {

	private String type;
	private int senderID;
	private String version;
	private String fileID;
	
	protected final static int MAX_HEADER_SIZE = 1024;
	protected final static int MAX_BODY_SIZE = 64000; 
	
	public byte[] delimiters = new byte[2];
	
	public Message() {
		this.type = "";
		this.senderID = 0;
		this.version = "";
		this.fileID = "";
		delimiters[0] = (byte)0xD;
		delimiters[1] = (byte)0xA;
	}
	
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
	
	protected void setType(String type) {
		this.type = type;
	}
	
	protected void setProtocolVersion(String version) {
		this.version = version;
	}
	
	protected void setSenderId(int senderID) {
		this.senderID = senderID;
	}
	
	protected void setFileId(String fileID) {
		this.fileID = fileID;
	}
	
	public abstract byte[] getMessageBytes();
	
	public int getHeaderLength(byte[] message) {
		
		for(int i = 0; i < message.length; i++) {
			if(message[i] == (byte)0xD) {
				i++;
				if(message[i] == (byte)0xA) {
					return i;
				}
				else {
					i--;
				}
			}
		}
		return 0;
	}
	
		
}

