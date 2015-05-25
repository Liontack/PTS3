package model;

import java.awt.Color;

//TODO let a player move his bat through the keyboard
public class Player{
	
	private static final int START_POINTS = 20;
	private static final int AI_REACTION_TIME = 100;
	
	public enum Colour{
		RED(Color.red),
		BLUE(Color.blue),
		GREEN(Color.green.darker());
		
		public final Color drawColor;
		Colour(Color drawColor){
			this.drawColor = drawColor;
		}
		
	}
	
	
	
	private final boolean isAI;
	private int points = START_POINTS;
	
	private final Colour colour;
	public PowerUp[] powerUps = new PowerUp[3];
	private Bat bat;
	
	private Thread aiMoverThread;
	
	
	
	Player(Colour colour, boolean isAI){
		this.colour = colour;
		this.isAI = isAI;
		
		// Start a thread for this ai to move his bat
		if(this.isAI){
			this.startAiMoverThread();
		}
	}
	public void setBat(Bat bat){
		if(this.bat == null){
			this.bat = bat;
		}
	}
	
	
	
	public int getPoints(){
		return this.points;
	}
	
	public boolean isAI(){
		return this.isAI;
	}
	
	public Colour getColour(){
		return this.colour;
	}
	
	public PowerUp[] getPowerUps(){
		return this.powerUps;
	}
	
	/**
	 * Get the power up on position
	 * @param nr	the n-th PowerUp to get, where 0<=nr<=size(powerups)
	 * @return		PowerUp on position nr, or null if it is empty
	 */
	public PowerUp getPowerUp(int nr){
		if(nr >= 0 && nr <= powerUps.length){
			return this.powerUps[nr];
		}else{
			throw new IllegalArgumentException("Parameter 'nr', is not in the given range");
		}
	}
	
	public Bat getBat(){
		return this.bat;
	}
	
	/**
	 * Set a new amount of points, should be used with getPoints() and a change
	 * @param points	The new amount of points for this player, where points>=0
	 */
	public void setPoints(int points){
		if(points >= 0){
			this.points = points;
		}else{
			throw new IllegalArgumentException("Points can't be set to a negative number");
		}
	}
	
	/**
	 * Append a new power up to the list of powerUps of this player
	 * @param newPowerUp	The power up to add
	 * @return				True, if the powerUp was added, otherwise false; The player already has 3 PowerUps in the latter case
	 */
	public boolean addPowerUp(PowerUp newPowerUp){
		for(int i = 0; i < this.powerUps.length; i++){
			if(this.powerUps[i] == null){
				this.powerUps[i] = newPowerUp;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Remove the PowerUp on location nr and reposition all following PowerUps one down
	 * @param nr	The index of the powerUp to remove
	 * @return		True if the powerUp was removed or false if there was no powerUp
	 */
	public boolean removePowerUp(int nr){
		if(this.powerUps[nr] == null){
			return false;
		}else{
			// Reposition
			for(int i = nr; i < this.powerUps.length - 1; i++){
				this.powerUps[i] = this.powerUps[i + 1];
			}
			this.powerUps[this.powerUps.length - 1] = null;
			
			return true;
		}
	}
	
	public void usePowerUp(int nr){
		this.powerUps[nr].use();
		this.removePowerUp(nr);
	}
	
	
	
	private class AiMoverRunnable implements Runnable{
		private Player ai;
		
		AiMoverRunnable(Player ai){
			this.ai = ai;
		}
		
		public void run(){
			while(true){
				if(ai.bat != null){
					aiMover(ai.bat);
				}
				try{
					Thread.sleep(Player.AI_REACTION_TIME);
				}catch(InterruptedException exception){
					System.out.println("Game ended");
					break;
				}
			}
		}
	}
	
	private void startAiMoverThread(){
		aiMoverThread = new Thread(new AiMoverRunnable(this));
		aiMoverThread.start();
	}
	
	public void stopAiMoverThread(){
		this.aiMoverThread.interrupt();
	}
	
	private void aiMover(Bat bat){
		// Really dumb ai
		if(Math.random() < 0.5){
			bat.moveLeft();
		}else{
			bat.moveRight();
		}
	}
	
	
}
