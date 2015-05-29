package keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import view.Program;

import model.Bat;
import model.Player;
import model.UserManagement;

public class BatController extends KeyAdapter{
	
	private final int reactionSpeed = 60;
	private Thread leftmover, rightmover;
	
	public void keyPressed(KeyEvent event){
		if(leftmover == null){
			if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A){
				leftmover = new Thread(new Runnable(){
					public void run(){
						while(true){
							if(rightmover == null){
								bat().moveLeft();
							}
							
							// Update the Game screen
							((JPanel)Program.getActivePanel()).repaint();

							// Insert delay
							try{
								Thread.sleep(reactionSpeed);
							}catch(InterruptedException exception){
								break;
							}
						}
					}
				});
				leftmover.start();
			}
		}
		if(rightmover == null){
			if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D){
				rightmover = new Thread(new Runnable(){
					public void run(){
						while(true){
							if(leftmover == null){
								bat().moveRight();
							}
							
							// Update the Game screen
							((JPanel)Program.getActivePanel()).repaint();
							
							// Insert delay
							try{
								Thread.sleep(reactionSpeed);
							}catch(InterruptedException exception){
								break;
							}
						}
					}
				});
				rightmover.start();
			}
		}
		
	}
	
	public void keyReleased(KeyEvent event){
		if(leftmover != null && (event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)){
			leftmover.interrupt();
			leftmover = null;
		}
		if(rightmover != null && (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D)){
			rightmover.interrupt();
			rightmover = null;
		}
	}
	
	private static Bat bat = null;
	private Bat bat(){
		if(bat != null){
			return bat;
		}
		
		if(Program.offlinePlayer != null){
			bat = Program.offlinePlayer.getBat();
			return bat;
		}else{
			for(Player player : Program.activeGame.getPlayers()){
				if(UserManagement.getUserOfPlayer(player) == Program.loggedInUser){
					bat = player.getBat();
					return bat;
				}
			}
		}
		
		return null;
	}
	
	public static void resetBat(){
		bat = null;
	}
	
}
