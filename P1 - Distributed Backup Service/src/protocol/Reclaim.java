package protocol;

import structures.PeerInfo;

public class Reclaim implements Runnable {

	private int diskSpace = 0;
	private PeerInfo peer = null;
	
	public Reclaim(PeerInfo peer, int diskSpace) {
		this.peer = peer;
		this.diskSpace = diskSpace;
	}

	@Override
	public void run() {
		
		//do something
		
	}
}
