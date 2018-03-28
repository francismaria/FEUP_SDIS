package communication;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MCchannel extends ChannelInformation implements Runnable{
	
	private static List<Integer> confirmedPeers = new ArrayList<Integer>();
	
	public MCchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}
	
	public void run() {
		
		System.out.println(getAddress() + "  " + getPort());
		
		boolean running = true;
		
		while(running) {/*
			System.out.println("----------- MC");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	public static int getConfirmedPeers() {
		return confirmedPeers.size();
	}
}
