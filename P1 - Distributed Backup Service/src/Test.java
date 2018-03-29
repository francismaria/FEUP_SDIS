import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
		MulticastSocket socketMC = new MulticastSocket(8000);
		InetAddress groupMC = InetAddress.getByName("224.0.0.0");
		//socketMC.setTimeToLive(1);
		socketMC.joinGroup(groupMC);
		
		String toSend = "MESSAGE 1";
		byte[] buf = toSend.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, groupMC, 8000);
		socketMC.send(packet);
		
		buf = new byte[2048];
		packet = new DatagramPacket(buf, buf.length);
		
		socketMC.receive(packet);
		String response = new String(packet.getData(), 0, packet.getLength());
		System.out.println(response);
		
		socketMC.leaveGroup(groupMC);
		socketMC.close();
		
		
	}
}
