package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.management.modelmbean.RequiredModelMBean;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import shared.ServerInt;
import shared.User;
import shared.ClientInt;

public class Client implements ClientInt {

	private static final long serialVersionUID = 1L;
	private ClientGUI GUI;
	private ClientInt stub;
    private ServerInt server;
    private String currentId;
    private String serverIP;
	
	public Client(ClientGUI guiReference , String IP) {
		try {
			this.GUI = guiReference;
			this.serverIP = IP;
			System.setProperty("java.security.policy","file:./sec.policy");
			System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/PCAD.PROJECT.CLIENT/");
			if(System.getSecurityManager() == null)
				System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname",serverIP);
			Registry r = LocateRegistry.getRegistry(serverIP,8080);
			//Registry r = LocateRegistry.getRegistry(8000);
			server = (ServerInt) r.lookup("REG");
			stub = (ClientInt) UnicastRemoteObject.exportObject(this,0);	
			} catch (RemoteException | NotBoundException e) {
			System.out.println("Server non collegato");
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void notifyC(String message) throws RemoteException {
		SwingUtilities.invokeLater(new Runnable(){
			                         public void run(){
			                        	 JOptionPane.showMessageDialog (GUI,"ATTENZIONE: "+message);
			   
			                         }
		});
	}

	@Override
	public boolean logIn(String id) throws RemoteException {
		boolean result = server.logIn(id, stub);
		if (!result) return false;
		currentId = id;
		GUI.status = true;
		return true;
	}

	@Override
	public boolean logOut() throws RemoteException {
		if (!server.logOut(currentId)) return false;
		GUI.status = false;
		return true;
	}

	@Override
	public boolean signIn(String id, String name, String surname) throws RemoteException {
		if (id.equals("") || name.equals("") || surname.equals("")) return false;
		User u = new User(id,name,surname);
		return server.signIn(u);
	}

	@Override
	public boolean removeUser() throws RemoteException {
		if (! server.removeUser(currentId)) return false;
		GUI.status = false ;
		return true;
	}

	@Override
	public boolean doAction(String id,String action) throws RemoteException {
		if (id.equals("")) return false;
		return server.doAction(id,action);
	}
	
	/*added*/
	@Override
	public boolean sendText(String id,String text) throws RemoteException {
		if (text.matches("\"\"")) return false;
		return server.sendText(id,text);
	}
	
	@Override
	public boolean countWords(String position) throws RemoteException {
		if(position.matches("\"\"")) return false;
		return server.countWords(position);
	}
		
}
