package messages;

public class DeleteMessage extends Message{

	private int messageLength;
	private byte[] message;
	
	public DeleteMessage(int messageLength) {
		super();
		this.messageLength = messageLength;
		message = new byte[messageLength];
	}
	
	public DeleteMessage(String version, int senderID, String fileID) {
		super("DELETE", version, senderID, fileID);
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId();
		
		byte[] header = headerString.getBytes();
		this.message = new byte[header.length + 2];
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
	}
	
	public byte[] getMessageBytes() {
		return message;
	}
	
	public void parseMessage(byte[] message) {
		System.arraycopy(message, 0, this.message, 0, messageLength);
		
		String headerString = new String(this.message);
		String[] parsedHeader = headerString.split(" +");
		
		setType(parsedHeader[0]);
		setProtocolVersion(parsedHeader[1]);
		setSenderId(Integer.parseInt(parsedHeader[2]));
		setFileId(parsedHeader[3].trim());
	}
}
