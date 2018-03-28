package peer;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import communication.*;
import exceptions.InvalidArgumentsException;
import rmi.RMIinterface;
import structures.PeerInfo;

public class Peer implements RMIinterface{
	
	private static PeerInfo info;
	private static final long serialVersionUID = 1L;

	protected Peer() throws RemoteException {
		super();
	}
	
	// 
	
	public static void main(String[] args) throws Exception {
		
		if(!parseArguments(args)) {
			throw new InvalidArgumentsException("peer");
		}
		
		Peer peer = new Peer();
		
		RMIinterface stub = (RMIinterface) UnicastRemoteObject.exportObject(peer, 0);
		Registry registry = LocateRegistry.getRegistry();
		
		registry.bind("MyServer", stub);
		
		System.err.println("ok");
	
	}
	
	public static boolean parseArguments(String[] args) {
		
		if(args.length < 6) return false;
		
		if(matchesVersion(args[0])) {
			int peerId = Integer.parseInt(args[1]);
			
			//check service access point
			if(parseMCname(args[3]) && parseMDBname(args[4]) && parseMDRname(args[4])){
				info = new PeerInfo(peerId);
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean parseMDRname(String name) {
		
		String[] channelInfo = name.split(":");
		
		int port = Integer.parseInt(channelInfo[1]);
		try {
			(new Thread(new MDRchannel(channelInfo[0], port))).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean parseMDBname(String name) {
		
		String[] channelInfo = name.split(":");
		
		int port = Integer.parseInt(channelInfo[1]);
		try {
			(new Thread(new MDBchannel(channelInfo[0], port))).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean parseMCname(String name) {
		
		String[] channelInfo = name.split(":");
		
		int port = Integer.parseInt(channelInfo[1]);
		try {
			(new Thread(new MCchannel(channelInfo[0], port))).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private static boolean matchesVersion(String version) {
		return version.matches("[0-9].[0-9]");
	}

	@Override
	public void backup() throws RemoteException {
		
		//backup
		
	}

	@Override
	public void restore() throws RemoteException {
		 
		//restore
	}

	@Override
	public void delete() throws RemoteException {
		 
		// delete
	}

	@Override
	public void reclaim() throws RemoteException {
		
		//reclaim
	}

	@Override
	public void state() throws RemoteException {
		//state
	}
}
