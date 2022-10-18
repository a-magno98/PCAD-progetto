package server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import androidServer.AndroidServer;
import shared.ClientInt;
import shared.ServerInt;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	protected ServerGUI guiReference = this;
	protected ServerInt server;
	protected String IP ;
	protected String ID ;
	protected JPanel contentPane;
	protected JTextArea textArea;
    protected JLabel lblIndirizzoIpServer;
    protected  JButton Button;
    protected volatile boolean status ; // ONLINE / OFFLINE
    
    
	public ServerGUI() throws UnknownHostException {	
		/* this.addWindowListener(new WindowAdapter(){
			                   public void windowClosing(WindowEvent e){
			                	   if (status )
									try {
										//system.
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
			                	   System.exit(0);
			                   }
			                  });
		*/
		//server = new Server();
		setBounds(100, 100, 512, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		InetAddress inetAddress = InetAddress.getLocalHost();
        String iA=inetAddress.getHostAddress();
		lblIndirizzoIpServer = new JLabel(iA);
		lblIndirizzoIpServer.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblIndirizzoIpServer.setBounds(22, 10, 172, 14);
		contentPane.add(lblIndirizzoIpServer);
		/*
		Button = new JButton("Inserisci");
		Button.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent arg0) {
        	        // server  = new Server(guiReference);
			}
		});
		Button.setBounds(250, 30, 89, 23);
		contentPane.add(Button);
		*/
		textArea = new JTextArea(160, 250);
		JScrollPane scrollPane = new JScrollPane(textArea);
		//textArea.setBounds(20, 50, 470, 400);
		textArea.setBounds(20, 50, 10000, 10000);
		contentPane.add(textArea);
		textArea.setEditable(false);
		server  = new Server(guiReference);
	}
	
	public void Update(String msg) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				textArea.append(msg+"\n");
			}
	   });
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}