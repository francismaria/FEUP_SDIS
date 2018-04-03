package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import messages.ChunkMessage;
import structures.ChunkInfo;
import structures.PeerInfo;

public class MDRchannel extends ChannelInformation implements Runnable{
	
	private static MulticastSocket socket;
	private List<ChunkInfo> receivedChunks = new ArrayList<ChunkInfo>();
	
	private final static long MAX_MSG_SIZE = 65024;
	
	public MDRchannel(PeerInfo peer, String ipAddress, int port) throws UnknownHostException {
		super(peer, ipAddress, port);
	}
	
	@Override
	public void run() {
		
		System.out.println(getGroupAddress() + "  " + getPort());
		
		boolean running = true;
		byte[] buf;
		DatagramPacket packet;
		
		joinChannel(getGroupAddress(), getPort());
		
		System.out.println("Connection Established: MDR CHANNEL");
		
		while(running) {
			
			buf = new byte[(int)MAX_MSG_SIZE];
			packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}
			
			String type = checkMessageType(buf);
			
			switch(type) {
				case "CHUNK":
					parseCHUNKMessage(buf, packet.getLength());
				default:
					break;
			}
		}
	}
	
	private void parseCHUNKMessage(byte[] message, int messageLength) {
		
		ChunkMessage chunk = new ChunkMessage(messageLength);
		chunk.parseMessage(message);
		
		if(hasChunk(chunk.getFileId(), chunk.getChunkNo())) {
			return;					//discards repeated chunks
		}
		
		ChunkInfo info = new ChunkInfo(chunk.getFileId(), chunk.getChunkNo(), chunk.getBody());
		receivedChunks.add(info);
	}
	
	public void joinChannel(InetAddress groupAddress, int port) {
		
		try {
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(groupAddress);
			
		} catch (IOException e) {
			System.out.println("Unable to create a socket");
		}
	}
	
	public boolean hasChunk(String fileID, int chunkNo) {
		for(ChunkInfo chunk : receivedChunks) {
			if(chunk.getFileId().equals(fileID) && chunk.getChunkNo() == chunkNo) {
				return true;
			}
		}
		return false;
	}
	
	public byte[] getChunkData(String fileID, int chunkNo) {
		
		for(ChunkInfo chunk : receivedChunks) {
			if(chunk.getFileId().equals(fileID) && chunk.getChunkNo() == chunkNo) {
				return chunk.getData();
			}
		}
		return null;
	}

}
