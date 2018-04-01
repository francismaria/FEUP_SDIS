package messages;

public class ChunkMessage extends Message {
	
	private int chunkNo;
	private byte[] body = new byte[Message.MAX_HEADER_SIZE];
	private byte[] message = new byte[Message.MAX_HEADER_SIZE + Message.MAX_BODY_SIZE];
	
	public ChunkMessage() {
		super();
		
		this.chunkNo = 0;
		this.body = null;
	}
	
	public ChunkMessage(String version, int senderID, String fileID, int chunkNo, byte[] body) {
		
		super("CHUNK", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		this.body = body;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId() + " " + Integer.toString(chunkNo);
		
		byte[] header = headerString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
		System.arraycopy(body, 0, message, Message.MAX_HEADER_SIZE, body.length);
	}
	
	public void parseMessage(byte[] message) {
		
		this.message = message;
		
		int headerLength = getHeaderLength(message);
		int bodyLength = message.length - Message.MAX_HEADER_SIZE;
		
		if(headerLength == 0) {
			System.out.println("Unable to read header.");
			return;
		}
		
		byte[] header = new byte[headerLength];
		body = new byte[MAX_BODY_SIZE];
		
		System.arraycopy(message, 0, header, 0, headerLength);
		System.arraycopy(message, headerLength, body, 0, bodyLength);
		
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
}
