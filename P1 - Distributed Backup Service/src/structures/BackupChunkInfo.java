package structures;

public class BackupChunkInfo {

	private int chunkNo;
	private int confirmedPeers;
	
	public BackupChunkInfo(int chunkNo, int confirmedPeers) {
		this.chunkNo = chunkNo;
		this.confirmedPeers = confirmedPeers;
	}
	
	@Override
	public String toString() {
		return chunkNo + ":" + Integer.toString(confirmedPeers);
	}
}
