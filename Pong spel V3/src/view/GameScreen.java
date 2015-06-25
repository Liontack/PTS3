package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import remote.GameScreenReceiver;
import keyboard.BatController;

import model.Game;
import model.Player;

@SuppressWarnings("serial")
public class GameScreen extends JPanel{
	
	GameScreenReceiver receiver;
	public Game drawOnlyGame;
	
	public String[] usernames = new String[]{};
	public int[] playerPoints = new int[]{ Player.START_POINTS, Player.START_POINTS, Player.START_POINTS };
	
	public GameScreen(){
		this.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				initScreen();
			}
		});
		
		try{
			this.receiver = new GameScreenReceiver(this);
		}catch(RemoteException exception){
			System.err.println("This GameScreen could not initialize his receiver");
		}
		
		this.addKeyListener(new BatController());
	}
	
	public void initScreen(){
		BatController.setBat();
		
		if(Program.offlineGame != null){
			Program.offlineGame.getGameField().startUpdaterThread();
		}else
		
		// Try to get only updates of the current game
		if(Program.gameID != 0 && this.drawOnlyGame == null){
			try{
				Program.secured.removeListener(receiver, null);// Receive no notifications
				Program.secured.addListener(receiver, "game" + Program.gameID);// but this one
				this.drawOnlyGame = new Game(true);
				this.drawOnlyGame.setBarricadesState(Program.barricadesState);
				Program.barricadesState = null;
			}catch(RemoteException exception){
				System.err.println("GameScreen: Could not listen to the game's updates");
			}
		}
	}
	
	public void gameIsFinished(){
		Program.gameID = 0;
		Program.barricadesState = null;
		
		this.drawOnlyGame = null;
		
		// Go to start screen
		Program.switchToPanel(StartScreen.class);
	}
	
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// Increase the font size a bit
		g.setFont(new Font("arial", Font.BOLD, 16));
		
		// Draw the game
		if(Program.offlineGame != null){
			// Draw some strings (unaffected by rotation)
			g.setColor(Color.black);
			g.drawString("Ronde " + Program.offlineGame.getCurrentRound() + "/" + Game.ROUND_AMOUNT, 28, 32);
			int i = 32;
			for(Player player : Program.offlineGame.getPlayers()){
				g.setColor(player.getColour().drawColor);
				g.drawString(String.valueOf(player.getPoints()), 28, 40 + i);
				String username = "";
				switch(player.getColour()){
					case RED:
						if(Program.username.isEmpty()){
							username = "Anoniem";
						}else{
							username = Program.username;
						}
						break;
					case BLUE:
						username = "Robot A";
						break;
					case GREEN:
						username = "Robot B";
						break;
				}
				g.drawString(username, 50, 40 + i);
				i += 24;
			}
			
			// Draw the rotated Game, red down
			Program.offlineGame.draw(g, Player.Colour.RED);
		}else if(this.drawOnlyGame != null){
			// Draw some strings (unaffected by rotation)
			g.setColor(Color.black);
			g.drawString("Ronde " + this.drawOnlyGame.getCurrentRound() + "/" + Game.ROUND_AMOUNT, 8, 16);
			Player.Colour thisLoggedInUsersColour = Player.Colour.RED;
			int i = 0;
			for(int k = 0; k < this.usernames.length; k++){
				if(this.usernames[k].toLowerCase().equals(Program.username.toLowerCase())){
					thisLoggedInUsersColour = Player.Colour.values()[k];
				}
				g.setColor(Player.Colour.values()[k].drawColor);
				g.drawString(String.valueOf(this.playerPoints[k]), 8, 40 + i);
				g.drawString(this.usernames[k], 30, 40 + i);
				i += 16;
			}
			
			// Draw the rotated Game, player colour down
			this.drawOnlyGame.draw(g, thisLoggedInUsersColour);
		}
		
	}
	
}
