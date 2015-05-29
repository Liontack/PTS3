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
	
	public PuckState isInGoal(Side side, Puck puck){
		// Calculate the perpendicular lines through a and b
		double Py_perx = this.gety_perx() * -1;
		double Pa__y_on0x = a.y - (Py_perx * a.x);
		double Pb__y_on0x = b.y - (Py_perx * b.x);
		
		double ya_on_px = (Py_perx * puck.getPosition().x) + Pa__y_on0x;
		double yb_on_px = (Py_perx * puck.getPosition().x) + Pb__y_on0x;
		
		// If the puck is in between those two lines return true
		boolean inbetween = false;
		switch(side.getColour()){
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
		
		if(inbetween){
			if(this.bat.hit(puck, this.a, this.b, side.getColour())){
				return PuckState.HIT_BAT;
			}else{
				return PuckState.IN_GOAL;
			}
		}
		
		return PuckState.NOT_IN_GOAL;
	}
	
	public double gety_perx(){
		return ((double)(b.y - a.y)) / ((double)(b.x - a.x));
	}
	
	
	
	public void draw(Graphics g, Player.Colour colour){
		g.setColor(Color.black);
		//g.drawLine(a.x, a.y, b.x, b.y);
		
		int thickness = 10;
		switch(colour){
			case RED:
				Point rc = new Point(a.x, a.y + thickness);
				Point rd = new Point(b.x, b.y + thickness);
				g.fillPolygon(new int[]{ a.x, b.x, rd.x, rc.x }, new int[]{ a.y, b.y, rd.y, rc.y }, 4);
				break;
			case BLUE:
				double alpha = Math.toDegrees(Math.atan(this.gety_perx() / 1)) + 360;
				Point bc = new Point((int)(a.x + (Math.cos(alpha) * thickness * 1.4)), (int)(a.y + (Math.sin(alpha) * thickness * 1.4)));
				Point bd = new Point((int)(b.x + (Math.cos(alpha) * thickness * 1.4)), (int)(b.y + (Math.sin(alpha) * thickness * 1.4)));
				g.fillPolygon(new int[]{ a.x, b.x, bd.x, bc.x }, new int[]{ a.y, b.y, bd.y, bc.y }, 4);
				break;
			case GREEN:
				double g_alpha = Math.toDegrees(Math.atan(this.gety_perx() / 1)) + 360;
				Point gc = new Point((int)(a.x + (Math.cos(g_alpha) * thickness * 1)), (int)(a.y + (Math.sin(g_alpha) * thickness * 1)));
				Point gd = new Point((int)(b.x + (Math.cos(g_alpha) * thickness * 1)), (int)(b.y + (Math.sin(g_alpha) * thickness * 1)));
				g.fillPolygon(new int[]{ a.x, b.x, gd.x, gc.x }, new int[]{ a.y, b.y, gd.y, gc.y }, 4);
				break;
		}
		
		this.bat.draw(g, this.a, this.b, colour);
	}
	
}
