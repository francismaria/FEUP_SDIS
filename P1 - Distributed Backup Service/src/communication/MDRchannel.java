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
		
		while(running) {/*
			System.out.println("------------ MDR");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
	}
}
