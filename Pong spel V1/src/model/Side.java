package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Side{
	
	public final static int LENGTH = 100;
	public final static int THICKNESS = 1;
	
	
	
	private final double lineThroughZeroX, lineAscendingPerX;
	private final Player.Colour colour;
	private final Goal goal;
	
	
	//XXX points
	public Side(Player.Colour colour, int lineThroughZeroX, int lineAscendingPerX){
		this.lineThroughZeroX = lineThroughZeroX;
		this.lineAscendingPerX = lineAscendingPerX;
		this.colour = colour;
		this.goal = new Goal();
	}
	
	public Goal getGoal(){
		return this.goal;
	}
	
	public Player.Colour getColour(){
		return this.colour;
	}

	public PuckState isAboveLine(Puck puck){
		//TODO Side --> isAboveLine
		return null;
	}
	
	public boolean isAboveLine(Point point){
		//TODO Side --> isAboveLine
		return false;
	}
	
	
	
	public void draw(Graphics g){
		//TODO Side --> draw
		g.setColor(Color.black);
		//g.drawline();
	}
	
}
