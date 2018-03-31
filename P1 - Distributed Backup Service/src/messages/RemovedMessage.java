package messages;

public class RemovedMessage extends Message {

	private int chunkNo;
	private byte[] message = new byte[Message.MAX_HEADER_SIZE];
	
	public RemovedMessage(String version, int senderID, String fileID, int chunkNo) {
		super("REMOVED", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + Integer.toString(getSenderId()) + " "
				+ "" + getFileId() + " " + Integer.toString(chunkNo);
		
		byte[] header = headerString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
}
