package messages;

public class DeleteMessage extends Message{

	private byte[] message = new byte[Message.MAX_HEADER_SIZE];
	
	public DeleteMessage(String version, int senderID, String fileID) {
		super("DELETE", version, senderID, fileID);
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId();
		
		byte[] header = headerString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
}
