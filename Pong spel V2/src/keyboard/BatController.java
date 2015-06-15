package keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import view.Program;

import model.Bat;

public class BatController extends KeyAdapter{
	
	private final int reactionSpeed = 60;
	private Thread leftmover, rightmover;
	
	private static BatController instance;
	private Bat bat = null;
	
	
	
	public BatController(){
		instance = this;
	}
	
	
	
	public void keyPressed(KeyEvent event){
		if(leftmover == null){
			if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A){
				leftmover = new Thread(new Runnable(){
					public void run(){
						while(true){
							if(rightmover == null){
								moveLeft();
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
								moveRight();
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

	private void moveLeft(){
		if(this.bat != null){
			this.bat.moveLeft();
		}else{
			try{
				Program.secured.moveBat(Program.userID, true);
			}catch(RemoteException e){}
		}
	}

	private void moveRight(){
		if(this.bat != null){
			this.bat.moveRight();
		}else{
			try{
				Program.secured.moveBat(Program.userID, false);
			}catch(RemoteException e){}
		}
	}
	
	
	
	public static void setBat(){
		if(Program.offlinePlayer != null){
			instance.bat = Program.offlinePlayer.getBat();
		}
	}
	
	public static void resetBat(){
		if(instance != null){
			instance.bat = null;
		}
	}
	
}
