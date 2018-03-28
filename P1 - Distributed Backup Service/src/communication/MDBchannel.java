package communication;

import java.net.UnknownHostException;

public class MDBchannel extends ChannelInformation implements Runnable{
	
	public MDBchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}

	@Override
	public void run() {
		System.out.println(getAddress() + "  " + getPort());
		
		boolean running = true;
		
		while(running) {
			
			//listen to MDB channel
			
		}
	}
	
	
}
