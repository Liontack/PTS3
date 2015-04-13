package model;

public class Player{
	
	private static final int START_POINTS = 20;
	
	private int points = START_POINTS;
	public final Bat bat;
	
	public Player(){
		this.bat = null;//playField.getBat();
	}
	
	public void increasePoints(int points){
		this.points += points;
	}
	
	public void decreasePoints(int points){
		this.points -= points;
	}
	
}
