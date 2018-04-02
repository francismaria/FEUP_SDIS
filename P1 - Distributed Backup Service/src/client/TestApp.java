package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.RMIinterface;

import exceptions.*;

public class TestApp {
	
	private static File file = null;
	
	private static byte[] fileBytes = null;
	
	private static String command = null;
	private static String remoteObjName = null;
	
	//BACKUP
	private static int replicationDegree = 0;
	
	//RECLAIM
	private static int diskSpace = 0;
	
	private static RMIinterface stub = null;
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		if(!parseArguments(args)) return;
		
		Registry registry = LocateRegistry.getRegistry(null);
		stub = (RMIinterface) registry.lookup(remoteObjName);
		
		executeCommand();
	}
	
	public static void executeCommand() throws RemoteException {
		
		switch(command) {
		case "BACKUP":
			stub.backup(file, replicationDegree);
			break;
		case "RESTORE":
			stub.restore(file);
			break;
		case "DELETE":
			stub.delete(file);
			break;
		case "RECLAIM":
			stub.reclaim(diskSpace);
			break;
		case "STATE":
			stub.state();
		default:
			break;
		}
	}
	
	public static boolean parseArguments(String[] args) {
		
		if(args[1].equals("BACKUP"))
			return checkBackupArgs(args);
		else if(args[1].equals("RESTORE"))
			return checkRestoreArgs(args);
		else if(args[1].equals("DELETE"))
			return checkDeleteArgs(args);
		else if(args[1].equals("RECLAIM"))
			return checkReclaimArgs(args);
		else if(args[1].equals("STATE"))
			return checkStateArgs(args);
		else {
			System.out.println("Your command was not valid.\nExpected commands: BACKUP | "
					+ "RESTORE | DELETE | RECLAIM | STATE.");

			return false;
		}
	}
	
	public static boolean checkBackupArgs(String[] args) {
		
		if(args.length != 4) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "BACKUP fileName replicationDegree");
			return false;
		}
		
		file = new File(args[2]);
		try {
			fileBytes = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(file.exists() && !file.isDirectory()) {
			remoteObjName = args[0];
			command = args[1];
			replicationDegree = Integer.parseInt(args[3]);
			return true;
		}
		else {
			System.out.println("Error in path");
			return false;
		}
	}
	
	public static boolean checkRestoreArgs(String[] args) {
		 
		if(args.length != 3) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "RESTORE fileName");
			return false;
		 }
		
		file = new File(args[2]);
		
		if(file.exists() && !file.isDirectory()) {
			remoteObjName = args[0];
			command = args[1];
			return true;
		}
		
		System.out.println("File was not found/accepted");
		return false;
	}
	
	public static boolean checkDeleteArgs(String[] args) {
		
		if(args.length != 3) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "RESTORE fileName");
			return false;
		}

		file = new File(args[2]);
		if(file.exists() && !file.isDirectory()) {
			remoteObjName = args[0];
			command = args[1];
			return true;
		}
		
		System.out.println("File was not found/accepted");
		return false;
	}
	
	public static boolean checkReclaimArgs(String[] args) {
		
		if(args.length != 3) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "RECLAIM diskSpace");
			return false;
		}
		
		remoteObjName = args[0];
		command = args[1];
		diskSpace = Integer.parseInt(args[2]);
		return true;
	}
	
	public static boolean checkStateArgs(String[] args) {
		
		if(args.length != 2) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "STATE");
			return false;
		}
		
		remoteObjName = args[0];
		command = args[1];
		return true;
	}

}
