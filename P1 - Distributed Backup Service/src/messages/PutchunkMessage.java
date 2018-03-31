package messages;

public class PutchunkMessage extends Message{

	private final static long MAX_MSG_SIZE = Message.MAX_HEADER_SIZE + Message.MAX_BODY_SIZE;
	
	private byte[] message = new byte[(int)MAX_MSG_SIZE];
	
	private int chunkNo;
	private int replicationDegree;
	private byte[] body;
	
	public PutchunkMessage(String version, int senderID, String fileID, int chunkNo, int replicationDegree, byte[] body) {
		
		super("PUTCHUNK", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		this.replicationDegree = replicationDegree;
		this.body = body;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId() + " " + Integer.toString(chunkNo) + " " + Integer.toString(replicationDegree);
		
		byte[] header = headerString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
		System.arraycopy(body, 0, message, MAX_HEADER_SIZE, body.length);
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
	
	
}
