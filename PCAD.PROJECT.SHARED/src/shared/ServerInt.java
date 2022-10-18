package shared;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInt extends Serializable, Remote {
	public boolean signIn(User user) throws RemoteException;  //ritorna se sei registrato o no
	public boolean logIn(String id, ClientInt stub) throws RemoteException;
	public boolean logOut(String id) throws RemoteException;
	public boolean removeUser(String id) throws RemoteException;
	public boolean doAction(String id,String action) throws RemoteException;
	public boolean doActionSend(String id,String action, String text) throws RemoteException;
	public boolean doActionSearch(String id,String action, String text) throws RemoteException;
	public boolean sendText(String id,String text) throws RemoteException;
	public boolean countWords(String text) throws RemoteException;
}
