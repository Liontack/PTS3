package model;

import java.awt.Graphics;

public class Bat{
	
	public final static int LENGTH_PERCENT_OF_SIDE_LENGTH = 8;
	private final static int MOVE_SPEED = 2;

	private int maximumGoalPosition;
	private int positionInGoal;
	
	
	
	public Bat(int goalLength){
		this.maximumGoalPosition = goalLength - (2 * Bat.LENGTH_PERCENT_OF_SIDE_LENGTH);
		this.positionInGoal = this.maximumGoalPosition / 2;
	}
	
	
	
	public int getPositionInGoal(){
		return this.positionInGoal;
	}
	
	public int getLength(){
		return (Bat.LENGTH_PERCENT_OF_SIDE_LENGTH / 100) * Side.LENGTH;
	}
	
	public void setPositionInGoal(int position){
		if(position > 0 && position < maximumGoalPosition){
			
		}else{
			throw new IllegalArgumentException("Can't position the bat out of the goal");
		}
	}
	
	public boolean hit(Puck puck){
		//TODO Bat --> hit
		return true;
	}
	
	
	
	public void draw(Graphics g){
		//TODO Bat --> draw
		//g.setColor(getColroFromPlayerColour)
		//drawLine
	}
	
}
