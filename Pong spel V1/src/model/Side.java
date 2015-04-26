package model;

public class Side{
	
	public final static int LENGTH = 100;
	public final static int GOAL_LENGTH_PERCENT_OF_SIDE_LENGTH = 40;
	public final static int THICKNESS = 10; 
	
	public static int getGoalLength(){
		return (GOAL_LENGTH_PERCENT_OF_SIDE_LENGTH / 100) * LENGTH;
	}
	
	
	
	public final Bat bat;
	
	public Side(){
		this.bat = new Bat();
	}
	
}
