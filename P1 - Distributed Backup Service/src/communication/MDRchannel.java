package communication;

import java.net.UnknownHostException;

public class MDRchannel extends ChannelInformation implements Runnable{
	
	public MDRchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}
	
	@Override
	public void run() {
		
		System.out.println(getAddress() + "  " + getPort());
		
		boolean running = true;
		
		while(running) {
			
			//listen to MDR channel
			
		}
	}
}
