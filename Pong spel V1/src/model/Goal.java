package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Goal{
	
	public final static int LENGTH_PERCENT_OF_SIDE_LENGTH = 40;
	
	
	
	private Point a, b;
	private Bat bat;
	
	
	public Goal(Point a, Point b){
		this.a = a;
		this.b = b;
		this.bat = new Bat(this.getGoalLength());
	}
	
	
	
	public Bat getBat(){
		return this.bat;
	}
	
	public int getGoalLength(){
		//return (Goal.LENGTH_PERCENT_OF_SIDE_LENGTH / 100) * Side.LENGTH;
		return (int) this.a.distance(this.b);
	}
	
	public boolean isInGoal(Side side, Puck puck){
		int y_perx = (b.y - a.y)/(b.x - a.x);
		int y_on0x = a.y - (y_perx * a.x);
		
		int y_px = (y_perx * puck.getPosition().x) + y_on0x;
		
		// If the puck is not near the goal, return false
		if((puck.getPosition().x <= a.x - (puck.getDiameter() / 2) || puck.getPosition().x >= b.x + (puck.getDiameter() / 2))){
			return false;
		}
		
		if((y_px >= puck.getPosition().y - (puck.getDiameter()/2) && side.getColour() == Player.Colour.RED) || (y_px <= puck.getPosition().y + (puck.getDiameter()/2) && side.getColour() != Player.Colour.RED)){
			return !this.bat.hit(puck, this.a, this.b, side.getColour());
		}else{
			return false;
		}
	}
	
	public int gety_perx(){
		return (b.y - a.y)/(b.x - a.x);
	}
	
	
	
	public void draw(Graphics g, Player.Colour colour){
		g.setColor(Color.white);
		g.drawLine(a.x, a.y, b.x, b.y);
		
		this.bat.draw(g, this.a, this.b, colour);
	}
	
}
