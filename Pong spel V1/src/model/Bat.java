package model;

import java.awt.Graphics;
import java.awt.Point;

public class Bat{
	
	public final static int LENGTH_PERCENT_OF_SIDE_LENGTH = 10;
	private final static int MOVE_SPEED = 2;

	private int maximumGoalPosition;
	private int minimumGoalPosition;
	private int positionInGoal;
	
	
	
	public Bat(int goalLength){
		this.maximumGoalPosition = goalLength - (Bat.getLength() / 2);
		this.minimumGoalPosition = Bat.getLength() / 2;
		this.positionInGoal = this.maximumGoalPosition / 2;
	}
	
	
	
	public int getPositionInGoal(){
		return this.positionInGoal;
	}
	
	public static int getLength(){
		return Bat.LENGTH_PERCENT_OF_SIDE_LENGTH * Side.LENGTH / 100;
	}

	public void moveLeft(){
		this.positionInGoal = Math.max(0, this.positionInGoal - MOVE_SPEED);//0?
	}

	public void moveRight(){
		this.positionInGoal = Math.min(this.maximumGoalPosition - (Bat.getLength() / 2), this.positionInGoal + MOVE_SPEED);
	}
	
	public boolean hit(Puck puck, Point goalA, Point goalB, Player.Colour colour){
		// Get this bat's points
		Point[] points = this.getPoints(goalA, goalB);
		Point a = points[0];
		Point b = points[1];
		
		// Calculate the perpendicular lines through a and b
		double Py_perx = (((double)(b.y - a.y)) / ((double)(b.x - a.x))) * -1;
		double Pa__y_on0x = a.y - (Py_perx * a.x);
		double Pb__y_on0x = b.y - (Py_perx * b.x);
		
		double ya_on_px = (Py_perx * puck.getPosition().x) + Pa__y_on0x;
		double yb_on_px = (Py_perx * puck.getPosition().x) + Pb__y_on0x;
		
		// If the puck is in between those two lines return true
		boolean inbetween = false;
		switch(colour){
			case RED:
				inbetween = (puck.getPosition().x > a.x && puck.getPosition().x < b.x);
				break;
			case BLUE:
				inbetween = puck.getPosition().y < yb_on_px && puck.getPosition().y > ya_on_px;
				break;
			case GREEN:
				inbetween = puck.getPosition().y < ya_on_px && puck.getPosition().y > yb_on_px;
				break;
		}
		return inbetween;
	}
	
	private Point[] getPoints(Point goalA, Point goalB){
		// Construct the goal line y=ax+b
		double y_perx = ((double)(goalB.y - goalA.y)) / ((double)(goalB.x - goalA.x));
		double y_on0x = goalA.y - (y_perx * goalA.x);
		double lx_prime = ((goalB.x - goalA.x) * getLength() / this.maximumGoalPosition) / 2;
		
		// Calculate the new coordinates
		double batAx = goalA.x + (this.positionInGoal * (goalB.x - goalA.x - lx_prime) / (this.maximumGoalPosition - this.minimumGoalPosition));
		double batAy = (y_perx * batAx) + y_on0x;
		double batBx = goalA.x + (this.positionInGoal * (goalB.x - goalA.x - lx_prime) / (this.maximumGoalPosition - this.minimumGoalPosition)) + lx_prime;
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
		g.drawLine(a.x, a.y + 3, b.x, b.y + 3);//XXX Bat is misdrawn
	}
	
}
