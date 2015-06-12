package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import remote.BarricadePositions;
import remote.GameUpdate;

import fontys.observer.RemotePropertyListener;

import keyboard.BatController;

import model.Game;
import model.Player;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements RemotePropertyListener{
	
	// Draw the players score
	// Draw the game field
	// Draw the rectangles with power up information; if supported
	
	public GameScreen(){
		this.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				initScreen();
			}
		});
		
		this.addKeyListener(new BatController());
	}
	
	public void initScreen(){
		Program.offlineGame.getGameField().startUpdaterThread();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// Draw the game
		if(Program.offlineGame != null){
			Program.offlineGame.draw(g);
			
			// Also draw some strings
			g.setColor(Color.black);
			g.drawString("Ronde " + Program.offlineGame.getCurrentRound() + "/" + Game.ROUND_AMOUNT, 8, 16);
			int i = 0;
			for(Player player : Program.offlineGame.getPlayers()){
				g.setColor(player.getColour().drawColor);
				g.drawString(String.valueOf(player.getPoints()), 8, 40 + i);
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
				g.drawString(username, 30, 40 + i);
				i += 16;
			}
		}
		
	}
	
	
	
	@Override
	public void propertyChange(PropertyChangeEvent event) throws RemoteException{
		// If it is a BarricadePositions
		if(event.getNewValue() instanceof BarricadePositions){
			// Set the barricade positions
			//TODO
		}
		
		// If it is a GameUpdate
		if(event.getNewValue() instanceof GameUpdate){
			// Update game values for drawing purposes only
			//TODO
		}
	}
	//XXX duplicate (dummy) game object
	//XXX aan- en afmelden op server updates
}
