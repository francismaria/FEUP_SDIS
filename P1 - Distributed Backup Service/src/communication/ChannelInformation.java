package communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class ChannelInformation {

	private int port;
	private InetAddress groupAddress = null;
	
	public ChannelInformation(String ipAddress, int port) throws UnknownHostException {
		this.groupAddress = InetAddress.getByName(ipAddress);
		this.port = port;
	}
	
	public InetAddress getGroupAddress() {
		return groupAddress;
	}
	
	public int getPort() {
		return port;
	}
}
