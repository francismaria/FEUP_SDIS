package client;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.RMIinterface;

public class TestApp {
	
	private static String fileName;
	private static int replicationDegree;
	
	public static void main(String[] args) throws RemoteException, NotBoundException {
		
		if(!parseArguments(args)) return;
		
		File backupFile = new File(fileName);
		
		Registry registry = LocateRegistry.getRegistry(null);
		
		RMIinterface stub = (RMIinterface) registry.lookup("Server1");
		
		System.out.println(fileName);
		stub.backup(backupFile);

		
	}
	
	public static boolean parseArguments(String[] args) {
		
		//fazer check do service access point
		
		if(args[1].equals("BACKUP")) {
			if(matchesFileName(args[2])) {
				fileName = args[2];
				replicationDegree = Integer.parseInt(args[3]);
				return true;
			}
			System.out.println("The filename specified is not valid.");
			return false;
		}
		
		return false;
	}
	
	public static boolean matchesFileName(String input) {
		return input.matches("[a-zA-Z0-9]+.[a-zA-Z0-9]+");
	}

}
