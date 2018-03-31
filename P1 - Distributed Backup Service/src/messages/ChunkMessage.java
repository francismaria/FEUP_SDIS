package messages;

public class ChunkMessage extends Message {
	
	private int chunkNo;
	private byte[] body = new byte[Message.MAX_HEADER_SIZE];
	private byte[] message = new byte[Message.MAX_HEADER_SIZE + Message.MAX_BODY_SIZE];
	
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
	
	public byte[] getMessageBytes() {
		return message;
	}
}
