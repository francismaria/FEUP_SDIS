package messages;

public class PutchunkMessage extends Message{
	
	private byte[] message;
	
	private int chunkNo;
	private int replicationDegree;
	private byte[] body;
	private int bodyLength;
	
	public PutchunkMessage(int messageLength) {
		
		super();
		
		this.chunkNo = 0;
		this.replicationDegree = 0;
		this.body = null;
		this.message = new byte[messageLength];
	}
	
	public PutchunkMessage(String version, int senderID, String fileID, int chunkNo,
			int replicationDegree, byte[] body, int bodyLength) {
		
		super("PUTCHUNK", version, senderID, fileID);
		
		this.chunkNo = chunkNo;
		this.replicationDegree = replicationDegree;
		this.body = new byte[bodyLength];
		System.arraycopy(body, 0, this.body, 0, bodyLength);
		this.bodyLength = bodyLength;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String headerString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId() + " " + Integer.toString(chunkNo) + " " + Integer.toString(replicationDegree);
		
		byte[] header = headerString.getBytes();
		
		this.message = new byte[header.length + 2 + bodyLength];
		
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(delimiters, 0, message, header.length, delimiters.length);
		System.arraycopy(body, 0, message, header.length+2, bodyLength);
	}
	
	public void parseMessageBytes(byte[] message) {
		
		System.arraycopy(message, 0, this.message, 0, this.message.length);
		
		parseMessage();
	}
	
	private void parseMessage(){
		
		int headerLength = getHeaderLength(message) + 1;		//includes '0xD' '0xA'
		bodyLength = message.length - headerLength;
		
		System.out.println("PUTCHUNK: " + headerLength);
		
		if(headerLength == 0) {
			System.out.println("Unable to read header.");
			return;
		}
		
		byte[] header = new byte[headerLength];
		body = new byte[bodyLength];
		
		System.arraycopy(message, 0, header, 0, headerLength);
		System.arraycopy(message, headerLength, body, 0, bodyLength);
		
		String headerString = new String(header);
		String[] parsedHeader = headerString.split(" +");
		
		setType(parsedHeader[0]);
		setProtocolVersion(parsedHeader[1]);
		setSenderId(Integer.parseInt(parsedHeader[2]));
		setFileId(parsedHeader[3]);
		chunkNo = Integer.parseInt(parsedHeader[4]);
		replicationDegree = Integer.parseInt(parsedHeader[5].trim());
	}

	public byte[] getMessageBytes() {
		return message;
	}
	
	public int getChunkNo() {
		return chunkNo;
	}
	
	public int getReplicationDegree() {
		return replicationDegree;
	}
	
	public byte[] getBody() {
		return body;
	}
	
}
