package client;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.RMIinterface;

public class TestApp {
	
	private static String fileName = null;
	private static String command = null;
	private static String remoteObjName = null;
	private static int replicationDegree = 0;
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		if(!parseArguments(args)) return;
		
		File backupFile = new File(fileName);
		
		
		Registry registry = LocateRegistry.getRegistry(null);
		
		RMIinterface stub = (RMIinterface) registry.lookup(remoteObjName);
		
		System.out.println(fileName);
		stub.backup(backupFile);

		
	}
	
	public static boolean parseArguments(String[] args) {
		
		//fazer check do service access point
		
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
		
		if(matchesFileName(args[2])) {
			remoteObjName = args[0];
			command = args[1];
			fileName = args[2];
			replicationDegree = Integer.parseInt(args[3]);
			return true;
		}
		else {
			System.out.println("The filename specified is not valid.");
			return false;
		}
	}
	
	public static boolean checkRestoreArgs(String[] args) {
		 
		if(args.length != 3) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "RESTORE fileName");
			return false;
		 }
		
		if(matchesFileName(args[2])) {
			remoteObjName = args[0];
			command = args[1];
			fileName = args[2];
			return true;
		}
		else {
			System.out.println("The filename specified is not valid.");
			return false;
		}
	}
	
	public static boolean checkDeleteArgs(String[] args) {
		
		if(args.length != 3) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "RESTORE fileName");
			return false;
		}
		
		if(matchesFileName(args[2])) {
			remoteObjName = args[0];
			command = args[1];
			fileName = args[2];
			return true;
		}
		else {
			System.out.println("The filename specified is not valid.");
			return false;
		}
	}
	
	public static boolean checkReclaimArgs(String[] args) {
		
		if(args.length != 3) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "RECLAIM diskSpace");
			return false;
		}
		
		return true;
	}
	
	public static boolean checkStateArgs(String[] args) {
		
		if(args.length != 2) {
			System.out.println("Wrong usage of program.\nExpected: java TestApp RemoteObjectName "
					+ "STATE");
			return false;
		}
		
		return true;
	}
	
	private static boolean matchesFileName(String input) {
		return input.matches("[a-zA-Z0-9]+.[a-zA-Z0-9]+");
	}

}
