package model;

import java.awt.Graphics;
import java.awt.Point;

public class Side{
	
	public final static int LENGTH = 500;
	public final static int THICKNESS = 1;
	
	
	
	private final Point a, b;
	private final Player.Colour colour;
	private final Goal goal;
	
	
	
	public Side(Player.Colour colour, Point a, Point b){
		this.a = a;
		this.b = b;
		this.colour = colour;
		
		// Create goal at the centre of this side
		double goal_ax = a.x + ((50 - Goal.LENGTH_PERCENT_OF_SIDE_LENGTH / 2) * (b.x-a.x) / 100);
		double goal_bx = b.x - ((50 - Goal.LENGTH_PERCENT_OF_SIDE_LENGTH / 2) * (b.x-a.x) / 100);
		
		double perx = ((double)(a.y - b.y)) / ((double)(a.x - b.x));
		
		int goal_ay = (int) ((perx * goal_ax) + (a.y - (perx * a.x)));
		int goal_by = (int) ((perx * goal_bx) + (a.y - (perx * a.x)));
		this.goal = new Goal(new Point((int)goal_ax, goal_ay), new Point((int)goal_bx, goal_by));
	}
	
	public Goal getGoal(){
		return this.goal;
	}
	
	public Player.Colour getColour(){
		return this.colour;
	}

	public PuckState isAboveLine(Puck puck){
		int y_perx = (b.y - a.y)/(b.x - a.x);
		int y_on0x = a.y - (y_perx * a.x);
		
		int y_px = (y_perx * puck.getPosition().x) + y_on0x;
		
		if((y_px < puck.getPosition().y - (puck.getDiameter()/2) -.2 && this.getColour() == Player.Colour.RED) || (y_px >= puck.getPosition().y + (puck.getDiameter()/2) +.2 && this.getColour() != Player.Colour.RED)){
			return PuckState.IN_FIELD;
		}else{
			if(this.goal.isInGoal(this, puck)){
				return PuckState.IN_GOAL;
			}else{
				return PuckState.OVER_LINE;
			}
		}
	}
	
	public boolean isAboveLine(Point point){
		int y_perx = (b.y - a.y)/(b.x - a.x);
		int y_on0x = a.y - (y_perx * a.x);
		
		int y_px = (y_perx * point.x) + y_on0x;
		
		return point.y > y_px;
	}
	
	
	
	public void draw(Graphics g){
		g.setColor(this.colour.drawColor);
		g.drawLine(a.x, a.y, b.x, b.y);
		
		this.goal.draw(g, this.colour);
	}
	
}
