package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Barricade{
	
	public static final int DIAMETER_PERCENT_MIN = 5;
	public static final int DIAMETER_PERCENT_MAX = 10;
	
	
	
	private final int diameterPercentOfSideLength;
	private Point position;
	
	
	
	public Barricade(Point position, int averageRating){
		this.diameterPercentOfSideLength = (DIAMETER_PERCENT_MIN + (int)(((double)averageRating / 40.00) * (DIAMETER_PERCENT_MAX - DIAMETER_PERCENT_MIN)));
		this.position = position;
	}
	
	
	
	public int getDiameter(){
		return this.diameterPercentOfSideLength * Side.LENGTH / 100;
	}
	
	public Point getPosition(){
		return this.position;
	}
	
	public boolean hit(Puck puck){
		return puck.getPosition().distance(this.position) <= ( (Puck.getDiameter() + this.getDiameter()) / 2);
	}
	
	
	
	public void draw(Graphics g){
		g.setColor(Color.black);
		g.fillOval(this.position.x - (this.getDiameter() / 2), this.position.y - (this.getDiameter() / 2), this.getDiameter(), this.getDiameter());
	}
	
}
