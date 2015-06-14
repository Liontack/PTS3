package view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JPanel;

import remote.BarricadesState;
import remote.PreGameScreenReceiver;

@SuppressWarnings("serial")
public class PreGameScreen extends JPanel{
	
	JButton btn_back = new JButton("Terug");
	JButton btn_play = new JButton("Start spel");
	
	public String[] usernames = new String[]{};
	
	private PreGameScreenReceiver receiver;
	
	public PreGameScreen(){
		this.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				initScreen();
			}
		});
		
		this.setLayout(null);
		
		try{
			this.receiver = new PreGameScreenReceiver(this);
		}catch(RemoteException exception){
			System.err.println("This PreGameScreen could not initialize his receiver");
		}
		
		// Init gui
		btn_back.setSize(100, 25);
		btn_back.setLocation(0, 0);
		btn_back.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				Program.switchToPanel(StartScreen.class);
				
				// Leave game and stop listening for updates
				try{
					Program.secured.leaveGame(Program.userID);
					Program.secured.removeListener(receiver, "game" + Program.gameID);
					Program.gameID = 0;
				}catch(RemoteException exception){
					System.err.println("PreGameScreen: Could not leave game or stop listening to this game's updates");
				}
			}
		});
		this.add(btn_back);
		
		btn_play.setSize(200, 50);
		btn_play.setLocation((Program.windowSize.width * 1 / 2) - (btn_play.getWidth() * 1 / 2) , Program.windowSize.height * 8 / 10);
		btn_play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// You go to the GameScreen by an property change update
				
				// Start the game
				try{
					Program.secured.startGame(Program.gameID);
				}catch(RemoteException exception){
					System.err.println("PreGameScreen: Could not stop listening to this game's updates");
				}
			}
		});
		this.add(btn_play);
	}
	
	public void initScreen(){
		setPlayButtonEnabled();
		
		if(Program.gameID == 0){
			// Join game and listen for updates
			try{
				Program.gameID = Program.secured.joinGame(Program.userID);
				Program.secured.removeListener(receiver, null);// Listen to nothing
				Program.secured.addListener(receiver, "game" + Program.gameID);// but updates from current game
			}catch(RemoteException exception){
				System.err.println("PreGameScreen: Could not join game or listen to this game's updates");
			}
		}
	}
	
	public void setPlayButtonEnabled(){
		// The first player in this game may press the play button
		if(this.usernames.length == 3 && this.usernames[0].toLowerCase().equals(Program.username.toLowerCase())){
			boolean noEmptyNames = true;
			for(String name : this.usernames){
				if(name.isEmpty()){
					noEmptyNames = false;
					break;
				}
			}
			this.btn_play.setEnabled(noEmptyNames);
		}else{
			this.btn_play.setEnabled(false);
		}
	}
	
	public void gameIsStarted(BarricadesState barricadesState){
		// Stop listening for updates
		new Thread(new Runnable(){
			public void run(){
				try{
					Program.secured.removeListener(receiver, "game" + Program.gameID);
				}catch(RemoteException exception){
					System.err.println("PreGameScreen: Could not stop listening to this game's updates");
				}	
			}
		}).start();
		
		// Go to GameScreen
		if(Program.getActivePanel() == this){
			Program.switchToPanel(GameScreen.class);
		}
		
		// Set these usernames in the gamescreen
		((GameScreen)Program.getActivePanel()).usernames = this.usernames;
		Program.barricadesState = barricadesState;
	}
	
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// Draw the usernames
		int i = 1;
		for(String username : this.usernames){
			int left = (Program.windowSize.width * 1 / 2) - (50);
			int top = Program.windowSize.height * (2 * i) / 10;
			g.drawString(username, left, top);
			i++;
		}
	}
	
}
