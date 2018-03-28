package communication;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MCchannel extends ChannelInformation implements Runnable{
	
	private static List<Integer> confirmedPeers = new ArrayList<Integer>();
	
	public MCchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}
	
	@Override
	public void run() {
		
		System.out.println(getAddress() + "  " + getPort());
		
		boolean running = true;
		
		while(running) {
			
			//listen to mc channel
		
		}
	}
	
	public static int getConfirmedPeers() {
		return confirmedPeers.size();
	}
}
