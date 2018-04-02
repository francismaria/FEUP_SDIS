package rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIinterface extends Remote{
	
	void backup(File file, int replicationDegree) throws RemoteException;
	
	void restore(String fileName) throws RemoteException;
	
	void delete(File file) throws RemoteException;
	
	void reclaim(int diskSpace) throws RemoteException;
	
	String state() throws RemoteException;
}
