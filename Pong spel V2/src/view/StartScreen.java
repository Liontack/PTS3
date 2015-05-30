package view;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import model.UserManagement;

@SuppressWarnings("serial")
public class StartScreen extends JPanel{
	
	// User interface objects
	private JLabel welcomeUser = new JLabel("");
	private JScrollPane userRatings = new JScrollPane();
		private JTable userRatingsTable;
	private JButton playGameOnline = new JButton("Speel spel online");
	private JButton playGameOffline = new JButton("Speel spel offline");
	private JButton logOut = new JButton("Afsluiten");
	
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
		playGameOnline.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				// Go to a next screen, dependent on whether there is someone logged in
				if(Program.loggedInUser == null){
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
		playGameOffline.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				// Go directly to the game screen
				Program.switchToPanel(GameScreen.class);
				
				Game offlineGame = new Game();
				Program.activeGame = offlineGame;
				Player offlinePlayer = offlineGame.addPlayer(false);
				if(Program.loggedInUser != null){
					Program.loggedInUser.setPlayer(offlinePlayer);
				}else{
					Program.offlinePlayer = offlinePlayer;
				}
				offlineGame.addPlayer(true);
				offlineGame.addPlayer(true);
				offlineGame.startGame();
			}
		});
		this.add(playGameOffline);
		
		logOut.setSize(200, 50);
		logOut.setLocation((Program.windowSize.width * 3 / 4) - (playGameOnline.getWidth() * 1 / 2) , Program.windowSize.height * 7 / 10);
		logOut.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				if(Program.loggedInUser != null){
					// Let the user know, he really is logged out
					Program.setFeedback("Gebruiker " + Program.loggedInUser.getUsername() + " uitgelogd.", Color.green);
					
					// Log out
					UserManagement.userLogout(Program.loggedInUser);
					
					// Close application after a delay of 5 seconds
					new Thread(new Runnable(){
						public void run(){
							try{
								Thread.sleep(4000);
							}catch(InterruptedException e){}
							Program.close();
						}
					}).start();
				}
			}
		});
		this.add(logOut);
	}
	
	public void initScreen(){
		// Reload the user rating table
		Set<RatingWrapper> ratings = new TreeSet<>(new comparator.UserRating());
		ratings.addAll(UserManagement.getUserRatings());
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
		if(Program.loggedInUser == null){
			welcomeUser.setText("");
			logOut.setText("Afsluiten");
		}else{
			welcomeUser.setText("Hallo " + Program.loggedInUser.getUsername());
			logOut.setText("Uitloggen en Afsluiten");
		}
	}
	
}
