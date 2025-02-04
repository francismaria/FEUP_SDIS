package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import messages.ChunkMessage;
import messages.DeleteMessage;
import messages.GetchunkMessage;
import messages.StoredMessage;
import structures.ChunkInfo;
import structures.PeerInfo;

public class MCchannel extends ChannelInformation implements Runnable{

	private MulticastSocket socket;
	private MulticastSocket MDRchannelSocket;
	private List<StoredMessage> confirmedPeers = new ArrayList<StoredMessage>();

	public MCchannel(PeerInfo peer, String ipAddress, int port) throws UnknownHostException {
		super(peer, ipAddress, port);
	}

	public int getConfirmedPeers() {
		return confirmedPeers.size();
	}

	@Override
	public void run() {
		System.out.println(getGroupAddress() + "  " + getPort());

		boolean running = true;
		byte[] buf;
		DatagramPacket packet;

		try {
			socket = new MulticastSocket(getPort());
			socket.setTimeToLive(1);
			socket.joinGroup(getGroupAddress());
			MDRchannelSocket = new MulticastSocket();
		} catch (IOException e) {
			System.out.println("Unable to create a socket");
		}

		System.out.println("PEER " + getPeer().getId() + ": " + "Connection Established -> MC CHANNEL");

		while(running) {

			buf = new byte[2048];
			packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("Unable to receive packet.");
			}

			String type = checkMessageType(buf);

			switch(type) {
				case "STORED":
					parseSTOREDMessage(buf);
					break;
				case "GETCHUNK":
					parseGETCHUNKMessage(buf, packet.getLength());
					break;
				case "DELETE":
					parseDELETEMessage(buf, packet.getLength());
					break;
				default:
					break;
			}
		}

		try {
			socket.leaveGroup(getGroupAddress());
		} catch (IOException e) {
			System.out.println("Unable to leave Group.");
		}
		socket.close();
		MDRchannelSocket.close();
	}

	private void parseSTOREDMessage(byte[] message) {

		StoredMessage receivingACK = new StoredMessage();
		receivingACK.parseMessage(message);

		if(receivingACK.getSenderId() == getPeer().getId())
			return;

		System.out.println("PEER " + getPeer().getId() + ": RECEIVED " + receivingACK.getType() + " FROM PEER " + receivingACK.getSenderId() + " | CHUNK NO: " + receivingACK.getChunkNo());

		confirmedPeers.add(receivingACK);
	}

	private void parseGETCHUNKMessage(byte[] message, int messageLength) {

		GetchunkMessage receivingRequest = new GetchunkMessage(messageLength);
		receivingRequest.parseMessage(message);

		if(receivingRequest.getSenderId() == getPeer().getId()) {
			return;
		}

		System.out.println("PEER " + getPeer().getId() + ": RECEIVED " + receivingRequest.getType() + " FROM PEER " + receivingRequest.getSenderId() + " | CHUNK NO: " + receivingRequest.getChunkNo());

		ChunkInfo chunk = getPeer().getDesiredChunk(receivingRequest.getFileId(), receivingRequest.getChunkNo());

		try {
			Thread.sleep((long)Math.random()*401);
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted while sleeping.");
		}

		sendCHUNKmessage(receivingRequest, chunk.getData());
	}

	private void sendCHUNKmessage(GetchunkMessage receivingRequest, byte[] data) {

		ChunkMessage ackMessage = new ChunkMessage(receivingRequest.getProtocolVersion(),
				/*receivingRequest.getSenderId()*/getPeer().getId(), receivingRequest.getFileId(),
				receivingRequest.getChunkNo(), data, data.length);

		byte[] message = ackMessage.getMessageBytes();

		DatagramPacket packet = new DatagramPacket(message, message.length,
				getPeer().getRestoreChannel().getGroupAddress(),
				getPeer().getRestoreChannel().getPort());

		try {
			long randomTime = (long)(Math.random()*401);
			Thread.sleep(randomTime);
			MDRchannelSocket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet through MDR channel");
		} catch (InterruptedException e) {
			System.out.println("Error ocurred in sleeping.");
		}
	}

	private void parseDELETEMessage(byte[] message, int messageLength) {

		DeleteMessage deleteMessage = new DeleteMessage(messageLength);
		deleteMessage.parseMessage(message);

		if(deleteMessage.getSenderId() == getPeer().getId()) {
			return;		//itself
		}

		System.out.println("PEER " + getPeer().getId() + ": RECEIVED " + deleteMessage.getType() + " FROM PEER " + deleteMessage.getSenderId());

		getPeer().deleteChunksOfFile(deleteMessage.getFileId());
	}

	public void restoreConfirmedPeers() {
		confirmedPeers.clear();
	}

}
