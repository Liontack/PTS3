package model;

import java.awt.Graphics;
import java.awt.Point;

public class Side{
	
	public final static int LENGTH = 500;
	
	
	
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
		double y_perx = ((double)(b.y - a.y)) / ((double)(b.x - a.x));
		double y_on0x = a.y - (y_perx * a.x);
		
		double y_r = (y_perx * puck.getPosition().x) + y_on0x;
		
		// Calculations for a non-red side
		double angleBeta = Math.toDegrees(Math.atan(this.goal.gety_perx()));
		double angleAlpha = 90 - angleBeta;
		double k = (Puck.getDiameter() ) / (Math.cos(Math.toRadians(angleAlpha)));
		// Now i can construct the line y=ax+b(+-)k and check the puck position on that line
		double y_g = (y_perx * puck.getPosition().x) + y_on0x + k;
		double y_b = (y_perx * puck.getPosition().x) + y_on0x - k;
		
		if((y_r > puck.getPosition().y + (Puck.getDiameter()/2) && this.getColour() == Player.Colour.RED) ||
				((y_g < puck.getPosition().y) && this.getColour() == Player.Colour.GREEN) ||
				((y_b < puck.getPosition().y) && this.getColour() == Player.Colour.BLUE)){
			return PuckState.IN_FIELD;
		}else{
			PuckState inGoalState = this.goal.isInGoal(this, puck);
			switch(inGoalState){
				case NOT_IN_GOAL:
					return PuckState.OVER_LINE;
				default:
					return inGoalState;
			}
		}
	}
	
	public boolean isAboveLine(Point point){
		double y_perx = ((double)(b.y - a.y)) / ((double)(b.x - a.x));
		double y_on0x = a.y - (y_perx * a.x);
		
		double y_px = (y_perx * point.x) + y_on0x;
		
		return point.y < y_px;
	}
	
	public double getYonX(double x){
		double y_perx = ((double)(b.y - a.y)) / ((double)(b.x - a.x));
		double y_on0x = a.y - (y_perx * a.x);
		
		double y = (y_perx * x) + y_on0x;
		return y;
	}
	
	
	
	public void draw(Graphics g){
		g.setColor(this.colour.drawColor);
		
		int thickness = 10;
		switch(this.colour){
			case RED:
				Point rc = new Point(a.x, a.y + thickness);
				Point rd = new Point(b.x, b.y + thickness);
				g.fillPolygon(new int[]{ a.x, b.x, rd.x, rc.x }, new int[]{ a.y, b.y, rd.y, rc.y }, 4);
				break;
			case BLUE:
				double bPy_perx = 0;
				if(this.goal.gety_perx() > 0){
					bPy_perx = -0.666667;
				}else{
					bPy_perx = 0.666667;
				}
				double bPa__y_on0x = a.y - (bPy_perx * a.x);
				double bPb__y_on0x = b.y - (bPy_perx * b.x);
				Point bc = new Point((int)(a.x - thickness), (int)((bPy_perx * (a.x - thickness)) + bPa__y_on0x));
				Point bd = new Point((int)(b.x - thickness), (int)((bPy_perx * (b.x - thickness)) + bPb__y_on0x));
				g.fillPolygon(new int[]{ a.x, b.x, bd.x, bc.x }, new int[]{ a.y, b.y, bd.y, bc.y }, 4);
				break;
			case GREEN:
				double gPy_perx = 0;
				if(this.goal.gety_perx() > 0){
					gPy_perx = -0.666667;
				}else{
					gPy_perx = 0.666667;
				}
				double gPa__y_on0x = a.y - (gPy_perx * a.x);
				double gPb__y_on0x = b.y - (gPy_perx * b.x);
				Point gc = new Point((int)(a.x + thickness), (int)((gPy_perx * (a.x + thickness)) + gPa__y_on0x));
				Point gd = new Point((int)(b.x + thickness), (int)((gPy_perx * (b.x + thickness)) + gPb__y_on0x));
				g.fillPolygon(new int[]{ a.x, b.x, gd.x, gc.x }, new int[]{ a.y, b.y, gd.y, gc.y }, 4);
				break;
		}
		
		this.goal.draw(g, this.colour);
	}
	
}
