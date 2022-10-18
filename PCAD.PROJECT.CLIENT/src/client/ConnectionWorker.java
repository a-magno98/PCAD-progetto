package client;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import shared.ClientInt;

import static java.util.Objects.requireNonNull;


public class ConnectionWorker extends SwingWorker<Boolean, Void> {

	private ClientGUI gui;
	private String command;
	private String id;
	private String name;
	private String surname;
	private ClientInt client;
	/*added*/
	private String text;
	
	
	public ConnectionWorker(ClientGUI gui, String command, String id, String name, String surname, ClientInt client, String text) {
		this.gui = gui;
		this.command = command;
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.client = client;
		this.text = text;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		switch(command) {
		case "signIn": return client.signIn(id, name, surname);
		case "remove": return client.removeUser();
		case "logIn":  return client.logIn(id);
		case "logOut": return client.logOut();
		/*added*/
		case "send": return client.sendText(id,text);
		case "search": return client.countWords(text);
		default: throw new AssertionError();
		}
	}
	
	@Override
	protected void done() {
		boolean result = false;
		try {
			result =  get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			gui.messageField.setText("SERVER NON RAGGIUNGIBILE");
			e.printStackTrace();
			return;
		}
		switch(command) {
		case "signIn": {
			if (!result) gui.messageField.setText("CAMPI MANCANTI/UTENTE PRESENTE");
			else gui.messageField.setText("REGISTRAZIONE EFFETTUATA");
			gui.usernameField.setText("");
			gui.nameField.setText("");
			gui.surnameField.setText("");
			break;
		}
		case "remove": {
			if (!result) gui.messageField.setText("UTENTE NON ESISTE");
			else {
				gui.messageField.setText("REGISTRAZIONE CANCELLATA");
				gui.statusField.setText("OFFLINE");
				gui.statusField.setForeground(Color.GRAY);
				gui.removeButton.setEnabled(false);
				gui.loginButton.setEnabled(true);
				gui.logoutButton.setEnabled(false);
				gui.startButton.setEnabled(false);
				gui.loginField.setEditable(true);
				gui.loginField.setText("");
				gui.stringUser.setText("");
			}
			break; 
		}
		case "logIn": {
			if (!result) gui.messageField.setText("SESSIONE "+id+" NON ESISTE/IN USO ");
			else {
				gui.messageField.setText("LOGIN EFFETTUATO");
				gui.statusField.setText("ONLINE");
				gui.statusField.setForeground(Color.GREEN);
				gui.removeButton.setEnabled(true);
				gui.loginButton.setEnabled(false);
				gui.logoutButton.setEnabled(true);
				gui.loginField.setEditable(false);
				/*added*/
				gui.sendButton.setEnabled(true);
				gui.searchButton.setEnabled(true);
				gui.stringUser.setText("Utente connesso: " + gui.loginField.getText());
				gui.loginField.setText("");
				gui.positionField.setText("");
				gui.ID=id;
			}
			break;
		}
		case "logOut": {
			if (!result) gui.messageField.setText("ERRORE: SESSIONE INESISTENTE");
			else {
				gui.messageField.setText("LOGOUT EFFETTUATO");
				gui.loginField.setEnabled(true);
				gui.loginField.setEditable(true);
				gui.loginField.setText("");
				gui.statusField.setText("OFFLINE");
				gui.statusField.setForeground(Color.GRAY);
				gui.removeButton.setEnabled(false);
				gui.loginButton.setEnabled(true);
				gui.logoutButton.setEnabled(false);
				gui.stringUser.setText("");
				gui.messageField.setText("");
				/*added*/
				gui.sendButton.setEnabled(false);
				gui.searchButton.setEnabled(false);
				gui.ID="";
			}
			break;
		}
		
		/*added*/
		case "send": {
			if(!result) gui.messageField.setText("ERRORE: TESTO NON INVIATO");
			else {
					gui.messageField.setText(id+": TESTO INVIATO");
					gui.textTosendField.setText("");
			}
			break;
		}
		
		case "search": {
			if(!result) gui.messageField.setText("ERRORE: RICERCA NON EFFETTUATA");
			else {
				gui.messageField.setText("CONTEGGIO SU LOCALITÀ: "+text+" EFFETTUATA");
				gui.positionF.setText("");
			}
			break;
		}
		default: throw new AssertionError();
		}
	}
	
}
