package peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import communication.*;
import exceptions.InvalidArgumentsException;
import protocol.Backup;
import protocol.Delete;
import protocol.Reclaim;
import protocol.Restore;
import protocol.State;
import rmi.RMIinterface;
import structures.BackupChunkInfo;
import structures.FileInfo;
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
		initBackupInfo();
		startListeningChannels();
		
	}
	
	public static void initBackupInfo() {
		
		String backupFolderPath = "/tmp/Peer_" + info.getId() + "/BACKUP";
		File backupFolder = new File(backupFolderPath);
		
		if(!backupFolder.isDirectory()) {
			return;
		}
		
		for(File fileEntry : backupFolder.listFiles()) {
			
			FileInfo infoFile = parseFile(fileEntry);

			info.saveBackupInfo(infoFile);
		}
	}
	
	private static FileInfo parseFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			String[] parsedFile = line.split(" ");
			List<BackupChunkInfo> chunksInfo = new ArrayList<BackupChunkInfo>();
			
			while((line = br.readLine()) != null) {
				if(line.equals("")) break;
				String[] parsedChunkInfo = line.split(":");
				int chunkNo = Integer.parseInt(parsedChunkInfo[0]);
				int peersNo = Integer.parseInt(parsedChunkInfo[1]);
				BackupChunkInfo info = new BackupChunkInfo(chunkNo, peersNo);
				chunksInfo.add(info);
			}
			
			FileInfo info = new FileInfo(parsedFile[0], parsedFile[1], parsedFile[2],
					(long)Integer.parseInt(parsedFile[3]), Integer.parseInt(parsedFile[4]), chunksInfo);
			
			return info;
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read from file");
			return null;
		} catch (IOException e) {
			System.out.println("Error reading from buffered reader.");
			return null;
		}	
	}
	
	public static boolean parseArguments(String[] args) {
		
		if(args.length < 6) return false;
		
		if(matchesVersion(args[0])) {
			int peerId = Integer.parseInt(args[1]);
			remoteObjName = args[2];
			
			if(parseMCname(args[3]) && parseMDBname(args[4]) && parseMDRname(args[5])){
				info = new PeerInfo(peerId, args[0], communicationChannel,
						backupChannel, restoreChannel);
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean parseMDRname(String name) {
		
		String[] channelInfo = name.split(":");
		
		int port = Integer.parseInt(channelInfo[1]);
		try {
			restoreChannel = new MDRchannel(info, channelInfo[0], port);
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
			backupChannel = new MDBchannel(communicationChannel, info, channelInfo[0], port);
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
			communicationChannel = new MCchannel(info, channelInfo[0], port);
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
	public void backup(File file, int replicationDegree) throws RemoteException {
		
		(new Thread(new Backup(info, file, replicationDegree, communicationChannel, backupChannel))).start();
		
	}

	@Override
	public void restore(String fileName) throws RemoteException {
		 
		(new Thread(new Restore(info, fileName, communicationChannel, restoreChannel))).start();
	}

	@Override
	public void delete(File file) throws RemoteException {
		 
		(new Thread(new Delete(info, file))).start();
	}

	@Override
	public void reclaim(int diskSpace) throws RemoteException {
		
		(new Thread(new Reclaim(info, diskSpace))).start();
	}

	@Override
	public String state() throws RemoteException {
		
		return info.toString();
	}
}
