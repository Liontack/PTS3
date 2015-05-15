package model;

import java.awt.Color;

public class Goal{
	
	public final static int LENGTH_PERCENT_OF_SIDE_LENGTH = 40;
	
	
	
	private Bat bat;
	
	
	public Goal(){
		this.bat = new Bat(this.getGoalLength());
	}
	
	
	
	public Bat getBat(){
		return this.bat;
	}
	
	public int getGoalLength(){
		return (Goal.LENGTH_PERCENT_OF_SIDE_LENGTH / 100) * Side.LENGTH;
	}
	
	public boolean isInGoal(Puck puck){
		//TODO Goal --> isingoal puck
		return false;
	}
	
	
	
	public void draw(Graphics g){
		//TODO Goal --> draw
		g.setColor(Color.white);
		//g.drawLine();
		
		this.bat.draw(g);
	}
	
}
