package communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class ChannelInformation {

	private int port;
	private InetAddress address = null;
	
	public ChannelInformation(String ipAddress, int port) throws UnknownHostException {
		this.address = InetAddress.getByName(ipAddress);
		this.port = port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
}
