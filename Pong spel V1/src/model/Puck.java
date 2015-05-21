package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Puck{
	
	public static final int DIAMETER_PERCENT_OF_SIDE_LENGTH = 4;
	public static final int DEFAULT_VELOCITY = 2;
	
	private int velocity;
	private int angle;// [0; 360>
	private Point position;
	
	
	
	public Puck(int angle, Point position, int averageRating){
		this.setVelocity(averageRating * DEFAULT_VELOCITY);
		this.setAngle(angle);
		this.position = position;
	}
	
	
	
	public int getVelocity(){
		return this.velocity;
	}
	
	public Point getPosition(){
		return this.position;
	}
	
	public int getAngle(){
		return this.angle;
	}
	
	public int getDiameter(){
		return (Puck.DIAMETER_PERCENT_OF_SIDE_LENGTH * Side.LENGTH / 100);
	}
	
	public void setVelocity(int velocity){
		this.velocity = velocity;
	}
	
	public void setAngle(int angle){
		if(angle >= 0 && angle < 360){
			this.angle = angle;
		}else{
			throw new IllegalArgumentException("The puck's angle was not in the range of degrees");
		}
	}
	
	public void move(){
		int newX = (int)(this.position.x + (Math.cos(Math.toRadians(this.angle)) * this.velocity));
		int newY = (int)(this.position.y + (Math.sin(Math.toRadians(this.angle)) * this.velocity));
		this.position = new Point(newX, newY);
	}
	
	
	public void draw(Graphics g){
		g.setColor(Color.black);
		g.drawOval(this.position.x, this.position.y, this.getDiameter(), this.getDiameter());
	}
	
}
