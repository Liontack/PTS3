package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import remote.PlayersInGameUpdate;

import fontys.observer.RemotePropertyListener;

@SuppressWarnings("serial")
public class PreGameScreen extends JPanel implements RemotePropertyListener{
	
	JButton btn_back = new JButton("Terug");
	JLabel label_playername1 = new JLabel();
	JLabel label_playername2 = new JLabel();
	JLabel label_playername3 = new JLabel();
	JButton btn_play = new JButton("Start spel");
	
	public PreGameScreen(){
		this.setLayout(null);
		
		final PreGameScreen thisPreGameScreen = this;
		
		// Init gui
		btn_back.setSize(100, 25);
		btn_back.setLocation(0, 0);
		btn_back.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(StartScreen.class);
				
				// Leave game and stop listening for updates
				try{
					Program.secured.leaveGame(Program.userID);
					Program.secured.removeListener(thisPreGameScreen, "game" + Program.gameID);
					Program.gameID = 0;
				}catch(RemoteException exception){
					System.err.println("PreGameScreen: Could not leave game or stop listening to this game's updates");
				}
			}
		});
		this.add(btn_back);
		
		label_playername1.setSize(100, 25);
		label_playername1.setLocation((Program.windowSize.width * 1 / 2) - (label_playername1.getWidth() * 1 / 2) , Program.windowSize.height * 2 / 10);
		this.add(label_playername1);
		
		label_playername2.setSize(100, 25);
		label_playername2.setLocation((Program.windowSize.width * 1 / 2) - (label_playername2.getWidth() * 1 / 2) , Program.windowSize.height * 4 / 10);
		this.add(label_playername2);
		
		label_playername3.setSize(100, 25);
		label_playername3.setLocation((Program.windowSize.width * 1 / 2) - (label_playername3.getWidth() * 1 / 2) , Program.windowSize.height * 6 / 10);
		this.add(label_playername3);
		
		btn_play.setSize(200, 50);
		btn_play.setLocation((Program.windowSize.width * 1 / 2) - (btn_play.getWidth() * 1 / 2) , Program.windowSize.height * 8 / 10);
		btn_play.setEnabled(false);
		btn_play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				Program.switchToPanel(GameScreen.class);
				
				// Stop listening for updates
				try{
					Program.secured.removeListener(thisPreGameScreen, "game" + Program.gameID);
				}catch(RemoteException exception){
					System.err.println("PreGameScreen: Could not stop listening to this game's updates");
				}
			}
		});
		this.add(btn_play);
	}
	
	public void initScreen(){
		btn_play.setEnabled(false);
		
		// Join game and listen for updates
		try{
			Program.gameID = Program.secured.joinGame(Program.userID);
			Program.secured.addListener(this, "game" + Program.gameID);
		}catch(RemoteException exception){
			System.err.println("PreGameScreen: Could not join game or listen to this game's updates");
		}
	}
	
	
	
	@Override
	public void propertyChange(PropertyChangeEvent event) throws RemoteException{
		// Only do something with PlayersInGameUpdate
		if(event.getNewValue() instanceof PlayersInGameUpdate){
			// Update the names in the gui
			PlayersInGameUpdate players = (PlayersInGameUpdate) event.getNewValue();
			this.label_playername1.setText(players.usernames[0]);
			this.label_playername2.setText(players.usernames[1]);
			this.label_playername3.setText(players.usernames[2]);
		}
	}
	
}
