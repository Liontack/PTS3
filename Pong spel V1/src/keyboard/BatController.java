package keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import view.Program;

import model.Bat;
import model.Player;
import model.UserManagement;

public class BatController extends KeyAdapter{
	
	public void keyPressed(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A){
			bat().moveLeft();
		}else if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D){
			bat().moveRight();
		}
		
		// Update the Game screen
		((JPanel)Program.getActivePanel()).repaint();
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
