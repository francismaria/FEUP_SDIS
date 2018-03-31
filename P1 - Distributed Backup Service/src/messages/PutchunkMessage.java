package messages;

public class PutchunkMessage extends Message{

	private final static long MAX_MSG_SIZE = Message.MAX_HEADER_SIZE + 64000;
	
	private byte[] message = new byte[(int)MAX_MSG_SIZE];
	
	private int chunkNo;
	private int replicationDegree;
	
	public PutchunkMessage(String version, int senderID, String fileID, int chunkNo, int replicationDegree) {
		super("PUTCHUNK", version, senderID, fileID);
		this.chunkNo = chunkNo;
		this.replicationDegree = replicationDegree;
		
		constructMessage();
	}
	
	private void constructMessage() {
		
		String messageString = getType() + " " + getProtocolVersion() + " " + Integer.toString(getSenderId()) + " "
				+ "" + getFileId() + " " + Integer.toString(chunkNo) + Integer.toString(replicationDegree);
		
		byte[] header = messageString.getBytes();
		
		System.arraycopy(header, 0, message, 0, header.length);
		
		
	}
	
}
