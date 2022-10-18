package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.Serializable;

public interface ClientInt extends Remote,Serializable {
	public void notifyC(String message) throws RemoteException;
	public boolean logIn(String id) throws RemoteException ;
	public boolean logOut() throws RemoteException;
	public boolean signIn(String id, String name, String surname) throws RemoteException;
	public boolean removeUser() throws RemoteException;
	public boolean doAction(String id,String action) throws RemoteException;
	public boolean sendText(String id,String text) throws RemoteException;
	public boolean countWords(String text) throws RemoteException;
}
