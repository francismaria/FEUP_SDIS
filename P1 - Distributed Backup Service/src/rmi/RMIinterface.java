package rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIinterface extends Remote{
	
	void backup(File file) throws RemoteException;
	
	void restore() throws RemoteException;
	
	void delete() throws RemoteException;
	
	void reclaim() throws RemoteException;
	
	void state() throws RemoteException;
}
