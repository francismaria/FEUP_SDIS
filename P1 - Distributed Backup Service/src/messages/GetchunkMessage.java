package messages;

public class GetchunkMessage extends Message {

	private int chunkNo;
	private byte[] message = new byte[Message.MAX_HEADER_SIZE];
	
	public GetchunkMessage(String version, int senderID, String fileID, int chunkNo) {
		
		super("GETCHUNK", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + "FILE_ID" + " " + chunkNo;
		
		byte[] header = headerString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
	
}
