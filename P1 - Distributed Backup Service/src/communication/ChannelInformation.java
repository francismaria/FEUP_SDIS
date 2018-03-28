package communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class ChannelInformation {

	private static int port;
	private static InetAddress address = null;
	
	public ChannelInformation(String ipAddress, int port) throws UnknownHostException {
		this.address = InetAddress.getByName(ipAddress);
		this.port = port;
	}
	
	public static InetAddress getAddress() {
		return address;
	}
	
	public static int getPort() {
		return port;
	}
}
