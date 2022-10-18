package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import shared.ClientInt;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	protected ClientGUI guiReference = this;
	protected ClientInt client;
	protected String IP ;
	protected String ID ;
	protected JPanel contentPane;
	protected JTextField usernameField;
	protected JTextField nameField;
	protected JTextField surnameField;
	protected JLabel lblCognome;
	protected JButton signinButton;
	protected JButton removeButton;
	protected JLabel statusField;
	protected JTextField loginField;
	protected JLabel messageField;
	protected JLabel positionField;
	protected JButton loginButton;
	protected JButton logoutButton;
	protected JButton startButton;
	protected JLabel stringUser;
	protected JTextField IpField;
    protected JLabel lblIndirizzoIpServer;
    protected JButton IpButton;
    protected volatile boolean status ; // ONLINE / OFFLINE
    /*added*/
    protected JTextField textTosendField; // message to send
    protected JLabel text;
    protected JButton sendButton;
    protected JLabel position;
    protected JTextField positionF;
    protected JButton searchButton;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		//client = new Client(this);
		this.addWindowListener(new WindowAdapter(){
			                   public void windowClosing(WindowEvent e){
			                	   if (status )
									try {
										client.logOut();
									} catch (RemoteException e1) {
										e1.printStackTrace();
									}
			                	   System.exit(0);
			                   }
			                  });

		//setBounds(100, 100, 512, 497);
		setBounds(100, 100, 512, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblIndirizzoIpServer = new JLabel("INDIRIZZO IP SERVER");
		lblIndirizzoIpServer.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblIndirizzoIpServer.setBounds(22, 10, 172, 14);
		contentPane.add(lblIndirizzoIpServer);

		IpField = new JTextField();
		IpField.setBounds(48, 30, 172, 20);
		contentPane.add(IpField);
		IpField.setColumns(10);
		IpField.setText("localhost");
		IpButton = new JButton("Inserisci");
		IpButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent arg0) {
				loginButton.setEnabled(false);
				signinButton.setEnabled(false);
				String ip = IpField.getText();
				IP = ip ;
				//(new Thread(){
				//	public void run(){
						client = new Client(guiReference ,IP);
						SwingUtilities.invokeLater(new Runnable(){
	                         public void run(){
	                        	 JOptionPane.showMessageDialog (guiReference,"INDIRIZZO IP INSERITO");
	                        	 loginButton.setEnabled(true);
	             			 signinButton.setEnabled(true);
	                         }

						});
				//	}
				//}).start();
			}
		});
		IpButton.setBounds(250, 30, 89, 23);
		contentPane.add(IpButton);

		JLabel lblNewLabel_4 = new JLabel("REGISTRAZIONE");
		lblNewLabel_4.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel_4.setBounds(22, 60, 136, 16);
		contentPane.add(lblNewLabel_4);

		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(48, 80, 106, 16);
		contentPane.add(lblNewLabel);

		usernameField = new JTextField();
		usernameField.setBounds(48, 100, 172, 26);
		contentPane.add(usernameField);
		usernameField.setColumns(10);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(48, 125, 106, 16);
		contentPane.add(lblNome);

		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(48, 140, 172, 26);
		contentPane.add(nameField);

		lblCognome = new JLabel("Cognome:");
		lblCognome.setBounds(48, 165, 106, 16);
		contentPane.add(lblCognome);

		surnameField = new JTextField();
		surnameField.setColumns(10);
		surnameField.setBounds(48, 180, 172, 26);
		contentPane.add(surnameField);


		signinButton = new JButton("Registrazione");
		signinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkIp()) return;
				String id = usernameField.getText();
				String name = nameField.getText();
				String surname = surnameField.getText();
				(new ConnectionWorker(guiReference, "signIn", id, name, surname, client, "")).execute();
			}
		});
		signinButton.setBounds(250, 100, 136, 29);
		contentPane.add(signinButton);

		removeButton = new JButton("Cancella Utente");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = usernameField.getText();
				(new ConnectionWorker(guiReference, "remove", id, null, null, client, "")).execute();
			}
		});
		removeButton.setBounds(250, 133, 136, 29);
		contentPane.add(removeButton);
		removeButton.setEnabled(false);

		statusField = new JLabel("OFFLINE");
		statusField.setForeground(Color.GRAY);
		statusField.setBounds(445, 6, 61, 16);
		contentPane.add(statusField);

		JLabel lblSessione = new JLabel("SESSIONE");
		lblSessione.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblSessione.setBounds(22, 210, 136, 16);
		contentPane.add(lblSessione);


		JLabel label = new JLabel("Username:");
		label.setBounds(48, 240, 106, 16);
		contentPane.add(label);

		loginField = new JTextField();
		loginField.setColumns(10);
		loginField.setBounds(48, 260, 172, 26);
		contentPane.add(loginField);

		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkIp()) return;
				String id  = loginField.getText();
				(new ConnectionWorker(guiReference, "logIn", id, null, null, client, "")).execute();
			}
		});
		loginButton.setBounds(245, 260, 87, 29);
		contentPane.add(loginButton);

		logoutButton = new JButton("Logout");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = loginField.getText();
				(new ConnectionWorker(guiReference, "logOut", id, null, null, client, "")).execute();
			}
		});
		logoutButton.setBounds(327, 260, 87, 29);
		contentPane.add(logoutButton);
		logoutButton.setEnabled(false);

		messageField = new JLabel("");
		messageField.setBounds(48, 290, 403, 45);
		contentPane.add(messageField);

		positionField = new JLabel("");
		positionField.setBounds(48, 401, 172, 26);
		contentPane.add(positionField);


		stringUser = new JLabel("");
		stringUser.setBounds(202, 211, 197, 16);
		contentPane.add(stringUser);

		/*added for message*/

		JLabel lblAnalisi = new JLabel("ANALISI TESTO");
		lblAnalisi.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblAnalisi.setBounds(22, 330, 136, 16);
		contentPane.add(lblAnalisi);

		text = new JLabel("Testo:");
		text.setBounds(48, 370, 106, 16);
		contentPane.add(text);

		textTosendField = new JTextField();
		textTosendField.setColumns(10);
		textTosendField.setBounds(48, 390, 172, 26);
		contentPane.add(textTosendField);

		sendButton = new JButton("Invio");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				(new ConnectionWorker(guiReference, "send", ID, null, null, client, textTosendField.getText())).execute();
			}
		});
		sendButton.setBounds(250, 390, 136, 29);
		contentPane.add(sendButton);
		sendButton.setEnabled(false);
		/********************************************/
		/*added for searching words*/
		position = new JLabel("Posizione:");
		position.setBounds(48, 420, 106, 16);
		contentPane.add(position);

		positionF = new JTextField();
		positionF.setColumns(10);
		positionF.setBounds(48, 440, 172, 26);
		contentPane.add(positionF);

		searchButton = new JButton("Cerca");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				(new ConnectionWorker(guiReference, "search", ID, null, null, client, positionF.getText())).execute();
			}
		});

		searchButton.setBounds(250, 440, 136, 29);
		contentPane.add(searchButton);
		searchButton.setEnabled(false);

	}

	private boolean checkIp(){
		if (IP == null || IP.equals("")){
			JOptionPane.showMessageDialog (guiReference,"INDIRIZZO IP MANCANTE");
			return false;
		}
		return true;

	}
}
