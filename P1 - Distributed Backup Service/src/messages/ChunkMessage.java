package messages;

public class ChunkMessage extends Message {
	
	private int chunkNo;
	private int bodyLength;
	private byte[] body;
	private byte[] message;
	
	public ChunkMessage(int messageLength) {
		super();
		
		this.message = new byte[messageLength];
		this.chunkNo = 0;
		this.body = null;
	}
	
	public ChunkMessage(String version, int senderID, String fileID, int chunkNo,
			byte[] body, int bodyLength) {
		
		super("CHUNK", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		this.body = new byte[bodyLength];
		System.arraycopy(body, 0, this.body, 0, bodyLength);
		this.bodyLength = bodyLength;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId() + " " + Integer.toString(chunkNo);
		
		byte[] header = headerString.getBytes();
		
		this.message = new byte[header.length + 2 + bodyLength];
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
		System.arraycopy(body, 0, message, header.length+2, body.length);
	}
	
	public void parseMessage(byte[] message) {
		
		System.arraycopy(message, 0, this.message, 0, this.message.length);
		
		int headerLength = getHeaderLength(message) + 1;
		bodyLength = this.message.length - headerLength;
		
		if(headerLength == 0) {
			System.out.println("Unable to read header.");
			return;
		}
		
		byte[] header = new byte[headerLength];
		body = new byte[bodyLength];
		
		System.arraycopy(this.message, 0, header, 0, headerLength);
		System.arraycopy(this.message, headerLength, body, 0, bodyLength);
		
		String headerString = new String(header);
		String[] parsedHeader = headerString.split(" +");
		
		setType(parsedHeader[0]);
		setProtocolVersion(parsedHeader[1]);
		setSenderId(Integer.parseInt(parsedHeader[2]));
		setFileId(parsedHeader[3]);
		chunkNo = Integer.parseInt(parsedHeader[4].trim());
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
	
	public byte[] getBody() {
		return body;
	}
	
	public int getChunkNo() {
		return chunkNo;
	}
}
