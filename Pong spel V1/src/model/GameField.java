package model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import view.Program;

public class GameField{
	
	private final Side[] sides = new Side[3];
	private final Puck puck;
	private final Set<Barricade> barricades = new HashSet<>();
	
	private Game game;
	private Thread updaterThread;
	
	
	
	public GameField(Game game, int averageRating){
		this.game = game;
		
		// Create sides
		int height = (int) (Math.tan(Math.toRadians(60)) * Side.LENGTH / 2);
		Point leftBottom = new Point(0, height), rightBottom = new Point(Side.LENGTH, height), top = new Point(Side.LENGTH / 2, 0);
		this.sides[0] = new Side(Player.Colour.values()[0], leftBottom, rightBottom);
		this.sides[1] = new Side(Player.Colour.values()[1], top, leftBottom);
		this.sides[2] = new Side(Player.Colour.values()[2], rightBottom, top);
		
		
		// Create puck
		int randomAngle = (int) Math.round(Math.random() * 360);
		this.puck = new Puck(randomAngle, this.getRandomPosition(), averageRating);
		
		// Create barricades
		int nBarricades = (int)Math.round(0.00 + (((double)averageRating / 40.00) * (5.00 - 0.00)));
		for(int i = 0; i < nBarricades; i++){
			barricades.add(new Barricade(this.getRandomPosition(), averageRating));
		}
	}
	
	
	
	public Puck getPuck(){
		return this.puck;
	}
	
	public Set<Barricade> getBarricades(){
		return this.barricades;
	}
	
	public Side getSide(Player.Colour colour){
		for(Side side : this.sides){
			if(side.getColour() == colour){
				return side;
			}
		}
		return null;
	}
	
	private Point getRandomPosition(){
		int randomX = (int) Math.round(Math.random() * Side.LENGTH / 2);
		int randomY = (int) Math.round(Math.random() * Side.LENGTH);
		Point point = new Point(randomX, randomY);
		
		// If the point falls above the left side, transform so it will fall under the right side
		if(!this.sides[1].isAboveLine(point)){
			point = new Point(point.x + (Side.LENGTH / 2), Side.LENGTH - point.y);
		}
		return point;
	}
	
	
	
	public void startUpdaterThread(){
		updaterThread = new Thread(new Runnable(){
			public void run(){
				update();
				try{
					Thread.sleep(500);
				}catch(InterruptedException exception){
					exception.printStackTrace();
				}
			}
		});
		updaterThread.start();
	}
	
	public void stopUpdaterThread(){
		this.updaterThread.interrupt();
	}
	
	private void update(){
		// Move the puck
		this.puck.move();
		
		// Check for collisions with the puck
		for(Side side : this.sides){
			switch(side.isAboveLine(puck)){
				case OVER_LINE:
					// Adjust puck's angle (Not tested)
					int m = (int)Math.toDegrees(Math.atan(side.getGoal().gety_perx() / 1));
					int newAngle = m - (puck.getAngle() + 360 - m);
					while(newAngle >= 360){
						newAngle -= 360;
					}
					this.puck.setAngle(newAngle);
					break;
				case IN_GOAL:
					game.increaseRound(side.getColour());
					break;
			}
		}
		
		// Update the Game screen
		((JPanel)Program.getActivePanel()).repaint();
	}
	
	
	
	public void draw(Graphics g){
		for(Side side : this.sides){
			side.draw(g);
		}
		for(Barricade barricade : this.barricades){
			barricade.draw(g);
		}
		this.puck.draw(g);
	}
	
}
