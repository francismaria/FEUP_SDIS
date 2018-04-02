package protocol;

import structures.PeerInfo;

public class State implements Runnable {

	private PeerInfo peer = null;
	
	public State(PeerInfo peer) {
		this.peer = peer;
	}

	@Override
	public void run() {
		
		//do something
		
	}
}
