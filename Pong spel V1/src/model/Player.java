package model;

public class Player{
	
	private static final int START_POINTS = 20;
	
	public enum Colour{
		RED,
		BLUE,
		GREEN
	}
	
	public final Colour colour;
	public final boolean isAI;
	private int points = START_POINTS;
	
	public PowerUp[] powerUps = new PowerUp[3];
	public final Side side;
	
	Player(Colour colour, Side side, boolean isAI){
		this.colour = colour;
		this.side = side;
		this.isAI = isAI;
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
	 * @param nr	the n-th PowerUp to get, where 0<=nr<=3
	 * @return		PowerUp on position nr, or null if it is empty
	 */
	public PowerUp getPowerUp(int nr){
		if(nr >= 0 && nr <= 3){
			return this.powerUps[nr];
		}else{
			throw new IllegalArgumentException("Parameter 'nr', is not in the given range");
		}
	}
	
	public Side getSide(){
		return this.side;
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
			this.powerUps[nr] = null;
			return true;
		}
	}
	
}
