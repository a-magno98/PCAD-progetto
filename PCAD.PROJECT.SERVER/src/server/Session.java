package server;

import java.util.HashMap;
import shared.User;

public class Session {
	
	User user;  // classe contenuta nel progetto SHARED
	String position; // poszione corrente dell'utente
	boolean status; // ONLINE/OFFLINE
	HashMap<String,Integer> data; 
	
	public Session(User user) {
		this.user = user;
		position = null;
		status = false;
		data = new HashMap<String,Integer>();
	}
	
	public User getUser(){
		return user;
	}
	
	public HashMap<String,Integer> getData(){
		return data;
	}
	
	/*added metodo set position*/
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public void setData(HashMap<String,Integer> data) {
		this.data = data;
	}

}
