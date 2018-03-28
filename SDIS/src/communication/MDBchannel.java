package communication;

import java.net.UnknownHostException;

public class MDBchannel extends ChannelInformation implements Runnable{
	
	public MDBchannel(String ipAddress, int port) throws UnknownHostException {
		super(ipAddress, port);
	}

	public void run() {
		System.out.println(getAddress() + "  " + getPort());
		
		boolean running = true;
		
		while(running) {/*
			System.out.println("-------------- MDB");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
	}
	
	
}
