package messages;

public class StoredMessage extends Message {

	private int chunkNo;
	private byte[] message = new byte[Message.MAX_HEADER_SIZE];
	
	public StoredMessage() {
		super();
		
		this.chunkNo = 0;
	}
	
	public StoredMessage(String version, int senderID, String fileID, int chunkNo) {
		super("STORED", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String messageString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + "FILE_ID" + " " + chunkNo;
		
		byte[] header = messageString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
	}
	
	public void parseMessage(byte[] message) {
		
		this.message = message;
		
		int headerLength = getHeaderLength(message);
		
		if(headerLength == 0) {
			System.out.println("Unable to read header.");
			return;
		}

		byte[] header = new byte[headerLength];
		System.arraycopy(message, 0, header, 0, headerLength);
		
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
