package peer;

import java.io.File;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import communication.*;
import exceptions.InvalidArgumentsException;
import protocol.Backup;
import rmi.RMIinterface;
import structures.PeerInfo;

public class Peer implements RMIinterface{
	
	private static PeerInfo info;
	private static String remoteObjName;
	
	private static MCchannel communicationChannel = null;
	private static MDBchannel backupChannel = null;
	private static MDRchannel restoreChannel = null;
	
	private static final long serialVersionUID = 1L;

	
	public static void main(String[] args) throws Exception {
		
		if(!parseArguments(args)) {
			throw new InvalidArgumentsException("peer");
		}
		
		initRMIservice();
		
		startListeningChannels();
	}
	
	public static boolean parseArguments(String[] args) {
		
		if(args.length < 6) return false;
		
		if(matchesVersion(args[0])) {
			int peerId = Integer.parseInt(args[1]);
			remoteObjName = args[2];
			
			if(parseMCname(args[3]) && parseMDBname(args[4]) && parseMDRname(args[5])){
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
			restoreChannel = new MDRchannel(channelInfo[0], port);
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
			backupChannel = new MDBchannel(channelInfo[0], port);
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
			communicationChannel = new MCchannel(channelInfo[0], port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private static boolean matchesVersion(String version) {
		return version.matches("[0-9].[0-9]");
	}
	
	public static void initRMIservice() throws RemoteException, AlreadyBoundException {
		
		Peer peer = new Peer();
		
		RMIinterface stub;
		
		stub = (RMIinterface) UnicastRemoteObject.exportObject(peer, 0);
		
		Registry registry = LocateRegistry.getRegistry();
		registry.bind(remoteObjName, stub);

	}
	
	public static void startListeningChannels() {
		
		(new Thread(communicationChannel)).start();
		(new Thread(backupChannel)).start();
		(new Thread(restoreChannel)).start();
	}

	@Override
	public void backup(File file) throws RemoteException {
		
		(new Thread(new Backup(file))).start();
		
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
