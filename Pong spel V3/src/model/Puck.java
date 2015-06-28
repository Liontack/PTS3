package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Puck{
	
	public static final int DIAMETER_PERCENT_OF_SIDE_LENGTH = 4;
	public static final double MIN_VELOCITY = 1.00;
	public static final double MAX_VELOCITY = 2.00;
	private static final double significance = 10000.000000;
	
	private double velocity;
	private double angle;// [0; 360>
	private Point position;
	
	
	
	public Puck(double angle, Point position, int averageRating){
		this.setVelocity(Puck.MIN_VELOCITY + (((double)averageRating / 40.00) * (Puck.MAX_VELOCITY - Puck.MIN_VELOCITY)));
		this.setAngle(angle);
		this.position = new Point((int)(position.x * significance), (int)(position.y * significance));
	}
	
	
	
	public double getVelocity(){
		return this.velocity;
	}
	
	public Point getPosition(){
		return new Point((int)(position.x / significance), (int)(position.y / significance));
	}
	
	public double getAngle(){
		return this.angle;
	}
	
	public static int getDiameter(){
		return (Puck.DIAMETER_PERCENT_OF_SIDE_LENGTH * Side.LENGTH / 100);
	}
	
	public void setVelocity(double velocity){
		this.velocity = velocity;
	}
	
	public void setAngle(double angle){
		if(angle >= 0 && angle < 360){
			this.angle = angle;
		}else{
			throw new IllegalArgumentException("The puck's angle was not in the range of degrees; given:" + angle);
		}
	}
	
	public void setPosition(Point p){
		this.position = new Point((int)(p.x * significance), (int)(p.y * significance));
	}
	
	public void move(){
		double newX = ((double)(this.position.x/significance) + (double)((double)Math.cos(Math.toRadians(this.angle)) * (double)this.velocity));
		double newY = ((double)(this.position.y/significance) + (double)((double)Math.sin(Math.toRadians(this.angle)) * (double)this.velocity));
		this.position = new Point((int)(newX * significance), (int)(newY * significance));
	}
	
	
	
	public void draw(Graphics g){
		g.setColor(Color.yellow);
		g.fillOval(this.getPosition().x - (Puck.getDiameter() / 2), this.getPosition().y - (Puck.getDiameter() / 2), Puck.getDiameter(), Puck.getDiameter());
		g.setColor(Color.black);
		g.drawOval(this.getPosition().x - (Puck.getDiameter() / 2), this.getPosition().y - (Puck.getDiameter() / 2), Puck.getDiameter(), Puck.getDiameter());
	}
	
}
