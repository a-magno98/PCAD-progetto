package androidServer;

import java.net.Socket;
import server.Server;
import shared.User;
import static java.util.Objects.requireNonNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WorkerRunnable implements Runnable {

	protected Socket clientSocket ;
	protected Server serverRMI;

	public WorkerRunnable(Socket clientSocket, Server serverRMI) {
	this.clientSocket = requireNonNull(clientSocket);
	this.serverRMI = requireNonNull(serverRMI) ;
	}

	@Override
	public void run() {
		try (PrintWriter output =  new PrintWriter( clientSocket.getOutputStream() , true );
			BufferedReader input = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
			Socket client = clientSocket){
			String command = input.readLine();
			switch(command){
			case "signin":{
				manageSignIn(output,input);
				break;
				}
			case "remove":{
				manageRemove(output,input);
				break;
				}
			case "login" :{
				manageLogIn(output,input);
				break;
				}
			case "logout":{
				manageLogOut(output,input);
				break;
				}
			case "send" :
				{
				manageAction(command,output,input);
				break;
				}
			case "search":
			{
				manageActionSearch(command, output, input);
				break;
			}
			default: {
				output.println("FAIL");
			}


			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  // Stream for sending data.


	}

	private void manageAction (String action, PrintWriter output , BufferedReader input) throws IOException{
		output.println("OK");
		String id = input.readLine();
		String text = input.readLine();
		boolean result = serverRMI.doActionSend(id,action, text);
		if(result) output.println("OK");
		else output.println("FAIL");
	}
	private void manageActionSearch (String action, PrintWriter output , BufferedReader input) throws IOException{
		output.println("OK");
		String id = input.readLine();
		String text = input.readLine();
		boolean result = serverRMI.doActionSearch(id,action, text);
		if(result) output.println("OK");
		else output.println("FAIL");
	}

	private void manageSignIn (PrintWriter output , BufferedReader input) throws IOException{
		output.println("OK");
		String id = input.readLine();
		String name = input.readLine();
		String surname = input.readLine();
		boolean result = serverRMI.signIn(new User (id , name ,surname));
		if(result) output.println("OK");
		else output.println("FAIL");

	}

	private void manageRemove (PrintWriter output , BufferedReader input) throws IOException{
		output.println("OK");
		String id = input.readLine();
		boolean result = serverRMI.removeUser(id);
		if(result) output.println("OK");
		else output.println("FAIL");

	}

	private void manageLogIn (PrintWriter output , BufferedReader input) throws IOException{
		output.println("OK");
		String id = input.readLine();
		boolean result = serverRMI.logIn(id, null);
		if(result) output.println("OK");
		else  output.println("FAIL");

	}

	private void manageLogOut (PrintWriter output , BufferedReader input) throws IOException{
		output.println("OK");
		String id = input.readLine();
		boolean result = serverRMI.logOut(id);
		if(result) output.println("OK");
		else output.println("FAIL");

	}

}
