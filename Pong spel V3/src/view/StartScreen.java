package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import comparator.RatingWrapper;
import model.Game;
import model.Player;

@SuppressWarnings("serial")
public class StartScreen extends JPanel{
	
	// User interface objects
	private JLabel welcomeUser = new JLabel("");
	private JScrollPane userRatings = new JScrollPane();
		private JTable userRatingsTable;
	private JButton playGameOnline = new JButton("Multiplayer");
	private JButton playGameOffline = new JButton("Single player");
	private JButton logOut = new JButton("Afsluiten");
	
	// UI to create connection to server
	private JButton connectToServer = new JButton("Maak connectie met de game server");
	
	public StartScreen(){
		this.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				initScreen();
			}
		});
		
		/* User interface */
		this.setLayout(null);
		
		// West side
		userRatings.setSize(Program.windowSize.width * 1 / 2, Program.windowSize.height - 50);
		userRatings.setLocation(0, 0);
		this.add(userRatings);
		
		// East side
		playGameOnline.setSize(200, 50);
		playGameOnline.setLocation((Program.windowSize.width * 3 / 4) - (playGameOnline.getWidth() * 1 / 2) , Program.windowSize.height * 2 / 10);
		playGameOnline.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// Test the connection
				if(!Program.testConnection()){
					return;
				}
				
				// Go to a next screen, dependent on whether there is someone logged in
				if(Program.userID == 0){
					Program.switchToPanel(LoginScreen.class);
				}else{
					Program.switchToPanel(PreGameScreen.class);
				}
			}
		});
		this.add(playGameOnline);
		
		welcomeUser.setSize(300, 50);
		welcomeUser.setLocation((Program.windowSize.width * 3 / 4) - (playGameOnline.getWidth() * 1 / 2) , Program.windowSize.height * 1 / 10);
		this.add(welcomeUser);
		
		playGameOffline.setSize(200, 50);
		playGameOffline.setLocation((Program.windowSize.width * 3 / 4) - (playGameOnline.getWidth() * 1 / 2) , Program.windowSize.height * 4 / 10);
		playGameOffline.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// Go directly to the game screen
				Program.switchToPanel(GameScreen.class);
				
				Game offlineGame = new Game(false);
				Program.offlineGame = offlineGame;
				Player offlinePlayer = offlineGame.addPlayer(false);
				Program.offlinePlayer = offlinePlayer;
				offlineGame.addPlayer(true);
				offlineGame.addPlayer(true);
				offlineGame.startGame();
			}
		});
		this.add(playGameOffline);
		
		logOut.setSize(200, 50);
		logOut.setLocation((Program.windowSize.width * 3 / 4) - (playGameOnline.getWidth() * 1 / 2) , Program.windowSize.height * 7 / 10);
		logOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(Program.userID != 0){
					// Let the user know, he really is logged out
					Program.setFeedback("Gebruiker " + Program.username + " uitgelogd.", Color.green);
					
					// Log out
					try{
						Program.unsecured.userLogout(Program.userID);
						Program.userID = 0;
						Program.username = "";
					}catch(RemoteException exception){
						System.err.println("User " + Program.username + " was not logged out");
					}
					
					// Close application after a delay of 4 seconds
					new Thread(new Runnable(){
						public void run(){
							try{
								Thread.sleep(4000);
							}catch(InterruptedException e){}
							Program.close();
						}
					}).start();
				}else{
					Program.close();
				}
			}
		});
		this.add(logOut);
		
		connectToServer.setSize(250, 30);
		connectToServer.setLocation(Program.windowSize.width - connectToServer.getWidth() - 8 , Program.windowSize.height - connectToServer.getHeight() - 16);
		connectToServer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				Program.switchToPanel(ConnectServerScreen.class);
			}
		});
		this.add(connectToServer);
	}
	
	public void initScreen(){
		boolean connectedWithServer = Program.testConnection();
		// Only enable the multiplayer button if this application is connected to the server
		this.playGameOnline.setEnabled(connectedWithServer);
		// Only enable the connect to server button if this application is not connected to the server
		this.connectToServer.setEnabled(!connectedWithServer);
		
		// Reload the user rating table
		Set<RatingWrapper> ratings = new TreeSet<>(new comparator.UserRating());
		try{
			ratings.addAll(Program.unsecured.getUserRatings());
			//Program.setFeedback("De server is bereikbaar", Color.green);
		}catch(RemoteException | NullPointerException exception){
			System.err.println("The ratings were not retrieved from the server");
			//Program.setFeedback("De server is onbereikbaar", Color.red);
		}
		String[] columnNames = { "Username", "Rating" };
		Object[][] data = new Object[ratings.size()][2];
			int i = 0;
			for(RatingWrapper r: ratings){
				data[i][0] = r.getUsername();
				data[i++][1] = r.getRating();
			}
		userRatingsTable = new JTable(data, columnNames);
		userRatingsTable.setSize(Program.windowSize.width * 1 / 2, Program.windowSize.height);
		userRatingsTable.setLocation(0, 0);
		userRatings.setViewportView(userRatingsTable);
		
		// Recognize user and say hello
		if(Program.username.isEmpty()){
			welcomeUser.setText("");
			logOut.setText("Afsluiten");
		}else{
			welcomeUser.setText("Hallo " + Program.username);
			logOut.setText("Uitloggen en Afsluiten");
		}
	}
	
}
