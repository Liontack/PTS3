package model;

import java.awt.Graphics;
import java.awt.Point;

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

	public void moveLeft(){
		this.positionInGoal = Math.max(0, this.positionInGoal - MOVE_SPEED);
	}

	public void moveRight(){
		this.positionInGoal = Math.min(this.maximumGoalPosition, this.positionInGoal + MOVE_SPEED);
	}
	
	public boolean hit(Puck puck, Point goalA, Point goalB, Player.Colour colour){
		// Get this bat's points
		Point[] points = this.getPoints(goalA, goalB);
		Point a = points[0];
		Point b = points[1];
		
		int y_perx = (b.y - a.y)/(b.x - a.x);
		int y_on0x = a.y - (y_perx * a.x);
		
		int y_px = (y_perx * puck.getPosition().x) + y_on0x;
		
		return (y_px > puck.getPosition().y + (puck.getDiameter()/2) && colour == Player.Colour.RED) || (y_px <= puck.getPosition().y + (puck.getDiameter()/2) && colour != Player.Colour.RED);
	}
	
	private Point[] getPoints(Point goalA, Point goalB){
		// Construct the goal line y=ax+b
		int y_perx = (goalB.y - goalA.y)/(goalB.x - goalA.x);
		int y_on0x = goalA.y - (y_perx * goalA.x);
		
		// Calculate the new coordinates
		double batAx = goalA.x + ((Math.sqrt((goalB.x - goalA.x) + (goalB.y - goalA.y))/(goalB.x - goalA.x)) * (this.positionInGoal - (.5 * this.getLength())));
		double batAy = (y_perx * batAx) + y_on0x;
		double batBx = goalA.x + ((Math.sqrt((goalB.x - goalA.x) + (goalB.y - goalA.y))/(goalB.x - goalA.x)) * (this.positionInGoal + (.5 * this.getLength())));
		double batBy = (y_perx * batBx) + y_on0x;
		
		// Create and return points
		Point[] points = new Point[2];
		points[0] = new Point((int)batAx, (int)batAy);
		points[1] = new Point((int)batBx, (int)batBy);
		return points;
	}
	
	
	
	public void draw(Graphics g, Point goalA, Point goalB, Player.Colour colour){
		// Get this bat's points
		Point[] points = this.getPoints(goalA, goalB);
		Point a = points[0];
		Point b = points[1];
		
		// Draw line
		g.setColor(colour.drawColor);
		g.drawLine(a.x, a.y, b.x, b.y);
	}
	
}
