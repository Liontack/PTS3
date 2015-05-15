package model;


public class User{
	
	private static final double INITIAL_RATING = 15.0;
	
	private final String username; // Unique
	private final String password;
	private int[] mostRecentPoints = new int[5];// index 0 contains the most recent points, domain is [0;40]
	
	private Player player;
	
	
	
	// XXX creating user with password like this, doesn't feel save
	User(String username, String password, int[] mostRecentPoints){
		this(username, password);
		this.mostRecentPoints = mostRecentPoints;
	}
	
	User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	
	
	
	public String getUsername(){
		return this.username;
	}
	
	public boolean equalsPassword(String password){
		return this.password.equals(password);
	}
	
	/**
	 * The rating of this player, based on his last 5 points
	 * @return this players' rating within [0;40]
	 */
	public double getRating(){
		int rating = 0;
		for(int i = 0; i < this.mostRecentPoints.length; i++){
			if(this.mostRecentPoints[i] == 0){
				return INITIAL_RATING;
			}
			rating += (5 - i) * this.mostRecentPoints[i];
		}
		return (double) (Math.round(rating / 1.5) * 10);
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public void addNewRecentPoints(int points){
		if(points > 40 || points < 0){
			return;
		}
		
		// Move all points one up
		for(int i = this.mostRecentPoints.length - 1; i > 0; i--){
			this.mostRecentPoints[i] = this.mostRecentPoints[i - 1];
		}
		// And place the new points at the front
		this.mostRecentPoints[0] = points;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public void clearPlayer(){
		this.player = null;
	}
	
}