package server;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/*added*/
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Iterator;
/*******************************************/

import shared.ClientInt;
import shared.ServerInt;
import shared.User;
import androidServer.AndroidServer;

public class Server  implements ServerInt {
	private static final long serialVersionUID = 1L;
	HashMap<String,ClientInt> stubUsers;  // hasmap per gli stub dei client RMI , sincronizzata attraverso blocchi synchronized
	ConcurrentHashMap<String,Session> sessionMap;  // hasmap per le sessioni di tutti i client
	//la seconda hashMap e' concorrente per garantire efficienza nell'updatePosition
	Random random;
	protected ServerGUI gui;

	/*added struttura dati condivisa con tutti gli utenti contenente <*/
	ConcurrentHashMap<String, HashMap<String,Integer>> map = new ConcurrentHashMap<String,HashMap<String,Integer>>();
	HashMap<String, Integer> aux ;

	public Server(ServerGUI x) {
		gui=x;
		stubUsers = new HashMap<String,ClientInt>();
		sessionMap = new ConcurrentHashMap<String,Session>();
		try {
			System.setProperty("java.security.policy","file:./sec.policy");
			System.setProperty("java.rmi.server.codebase","file:${workspace_loc}/PCAD.PROJECT.SERVER/");
			if(System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname","localhost");
			Registry r = null;
			try {
				r = LocateRegistry.createRegistry(8080);
			} catch (RemoteException e) {
				r = LocateRegistry.getRegistry(8080);
			}
			//Server Server = (Server) new Server(gui);
		    ServerInt stubRequest = (ServerInt) UnicastRemoteObject.exportObject(this,0);
			r.rebind("REG", stubRequest);
			//gui.Update("Server REG in ascolto");
			AndroidServer androidS = new AndroidServer(5005,this);
			(new Thread(){
				public void run(){
					androidS.runWebServer();
				}
			}).start();
			gui.Update("Server REG in ascolto");
			//this.console(r, this, androidS);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//funzione per la registrazione
	@Override
	public boolean signIn(User user) throws RemoteException {
		String id = user.getId();
		Session session = new Session(user);
		if(sessionMap.putIfAbsent(id, session) == null) {
			gui.Update("Utente "+id+" inserito" );
			return true;
			}
		gui.Update("Utente "+id+" non inserito");
		return false;
	}

	@Override    //funzione per il LogIn
	public  boolean logIn(String id, ClientInt stub) throws RemoteException {
		Session userSession = searchSession(id);
		if (userSession == null) return false;
		synchronized (userSession){ // per evitare che due client provino a fare login sulla stessa sessione
			                        // e per evitare race coondition con closeAllSessionOpened()
			if (userSession.status == true) return false;
		    userSession.status = true;
		}
		if (stub != null) synchronized (stubUsers) {stubUsers.put(id, stub);} // per  evitare race contidion con la notifyAllUsers()
		return true;
	}

	            //la logOut non e' synchronized perche' lavora su singole sessioni ed e' effettuata da utenti
	@Override   //che hanno gia' effettuato il logIn
	public boolean logOut(String id) throws RemoteException {
		Session userSession = searchSession(id);
		if (userSession == null) return false;
		synchronized(userSession){ // per evitare race condition con CloseAllSessionOpened()
			if (userSession.status == false) return false;
			userSession.status = false;
		}
		synchronized (stubUsers){ // per  evitare race contidion con la notifyAllUsers()
		stubUsers.remove(id);
		}
		return true;
	}

	@Override    //synchronized per evitare race condition con printCurrentPosition()
	public synchronized  boolean removeUser(String id) throws RemoteException {
		Session userSession = searchSession(id);
		if (userSession == null) return false;
		if (userSession.status == false) return false; // si puo' rimuovere un utente solo se si e' loggati
		boolean result = (sessionMap.remove(id) != null); // true se ha eliminato la sessione
		if (result){
			synchronized (stubUsers){ // per evitare race condition con notifyAllUsers()
				stubUsers.remove(id);
				}
			return true;
		}
		return false ;
	}


	//funzione per trovare la sessione corretta, tramite id dell'utente
	private Session searchSession(String id) {
		return sessionMap.get(id);
	}


	private synchronized void printSession() {
		for (String key : sessionMap.keySet()) {
			gui.Update("\nUtente: " + key);
		}
	}

	// non synchronized: chiamata all'interno della exit  synchronized
	private void closeAllSessionsOpened() {
		for (String key : sessionMap.keySet()) {
			Session currentSession = sessionMap.get(key);
			synchronized (currentSession){ // per evitare raceCondition con login e logout
				if (currentSession.status == true) {
					currentSession.status = false;
					ClientInt stub = stubUsers.get(key);
					try {
						if( stub != null) stub.notifyC("Server chiuso");
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					stubUsers.remove(key);
				}
			}
		}
	}

	public synchronized boolean doAction(String id,String action)  {
		Session userSession = searchSession(id);
		if (userSession == null) return false;
		gui.Update("User "+id+": "+action);
		return true;
	}
	public synchronized boolean doActionSend(String id,String action, String text)  {
		Session userSession = searchSession(id);
		if (userSession == null) return false;
		gui.Update("User "+id+": "+action+" testo: "+text);
		sendText(id,text);
		return true;
	}
	public synchronized boolean doActionSearch(String id,String action, String text)  {
		Session userSession = searchSession(id);
		if (userSession == null) return false;
		gui.Update("User "+id+": "+action+" testo: "+text);
		countWords(text);
		return true;
	}
	
	/*passato un valore ad una hashmap, glielo aggiunge*/
	private synchronized HashMap<String,Integer> map_value(HashMap<String,Integer> hm, String p){
		hm.put(p, hm.containsKey(p) ? hm.get(p) + 1 : 1);
		return hm;
	}

	/*scansione del testo*/
	public boolean sendText(String id,String text) {

		
		Session userSession = searchSession(id);
		if(userSession == null)
			return false;
		
		Scanner sc = new Scanner(text);
		sc.useDelimiter("[^a-zA-Z]+");
		String position = sc.hasNext() ? String.format(sc.next()).toLowerCase() : "Fail";
		if(position.equals("Fail")) {
			sc.close();
			return false;
		}
		synchronized (userSession) {
			userSession.setPosition(position);
		}
		
		aux = new HashMap<String,Integer>();
		
		while (sc.hasNext())
		{
			String word = String.format(sc.next()).toLowerCase();
			
			synchronized (aux) {
				aux.put(word, aux.containsKey(word)? aux.get(word) + 1 : 1);
			}
			
			if(map.containsKey(position)) {
				map.put(userSession.getPosition(), map_value(map.get(position), word));
			}
			else
			{
				HashMap<String, Integer> a = new HashMap<String, Integer>();
				synchronized (a) {
					a.putAll(aux);
				}
				map.put(position, a);

			}
			synchronized (userSession) {
				userSession.getData().put(word,userSession.getData().containsKey(word)? userSession.getData().get(word)+1:1);
			}
		}
		sc.close();
		gui.Update("User "+id+": "+"["+userSession.getPosition()+"]"+": "+userSession.getData().entrySet());

		System.out.println("Debug map");
		System.out.println("Elementi totali nella map generale: "+map.entrySet());

		return true;
	}


	/*algoritmo per la ricerca delle parole più utilizzate*/
	private static <K, V extends Comparable<? super V>> List<Entry<K, V>>
    findGreatest(Map<K, V> map, int n)
{
    Comparator<? super Entry<K, V>> comparator =
        new Comparator<Entry<K, V>>()
    {
        @Override
        public int compare(Entry<K, V> e0, Entry<K, V> e1)
        {
            V v0 = e0.getValue();
            V v1 = e1.getValue();
            return v0.compareTo(v1);
        }
    };
    PriorityQueue<Entry<K, V>> highest =
        new PriorityQueue<Entry<K,V>>(n, comparator);
    for (Entry<K, V> entry : map.entrySet())
    {
        highest.offer(entry);
        while (highest.size() > n)
        {
            highest.poll();
        }
    }

    List<Entry<K, V>> result = new ArrayList<Map.Entry<K,V>>();
    while (highest.size() > 0)
    {
        result.add(highest.poll());
    }
    return result;
	}

	//stampa su console le 3 parole più usate per zona
		public synchronized boolean countWords(String position) {
			HashMap <String,Integer> mp = new HashMap<String,Integer>();
			mp = map.get(position);
			if(mp == null)
				return false;

			int n = 3;
	        List<Entry<String, Integer>> greatest = findGreatest(mp, n);

	        System.out.println("Top "+n+" entries in "+position);
	        for (Entry<String, Integer> entry : greatest)
	        	System.out.println(entry);

	        return true;
		}

	//Funzione per notificare la modifica del grafo a tutti i client RMI connessi ( nessuna notifica per android)
	private void notifyAllUsers(){
		synchronized (stubUsers){ // per evitare race condition dovute a modifiche su hasmap stubUsers
		for (String key : stubUsers.keySet()) {
			ClientInt stub = stubUsers.get(key);
			try {
				stub.notifyC("Hello");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		}

	}

	//funzione per avviare la console del server
	private void console(Registry r, Server s , AndroidServer androidS) throws IOException {
		String line = "";
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		while(!line.equals("exit")) {
			gui.Update("- 0: per terminare i server");
			gui.Update("- 1: per visualizzare sessioni aperte");
			gui.Update("REG>");
		}
	}

	//funzione per chiudere le sessioni attive e il server
	public synchronized void exit(Registry r, Server s) {
		try {
			closeAllSessionsOpened();
			r.unbind("REG");
			UnicastRemoteObject.unexportObject(s,true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}