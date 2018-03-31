package messages;

public class StoredMessage extends Message {

	private byte[] message = new byte[Message.MAX_HEADER_SIZE];
	
	private int chunkNo;
	
	public StoredMessage(String version, int senderID, String fileID, int chunkNo) {
		
		super("STORED", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String messageString = getType() + " " + Integer.toString(getSenderId()) + " " + "FILE_ID" + " " + chunkNo;
		
		byte[] header = messageString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
	
}
