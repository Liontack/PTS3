package model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import view.Program;

public class GameField{
	
	private static final int UPDATE_SPEED = 10;
	
	private int averageRating;
	
	private final Side[] sides = new Side[3];
	private Puck puck;
	private final Set<Barricade> barricades = new HashSet<>();
	
	private Game game;
	private Thread updaterThread;
	
	
	
	public GameField(Game game, int averageRating){
		this.game = game;
		this.averageRating = averageRating;
		
		// Create sides
		int height = (int) (Math.tan(Math.toRadians(60)) * Side.LENGTH / 2);
		Point leftBottom = new Point(0, height), rightBottom = new Point(Side.LENGTH, height), top = new Point(Side.LENGTH / 2, 0);
		this.sides[0] = new Side(Player.Colour.values()[0], leftBottom, rightBottom);
		this.sides[1] = new Side(Player.Colour.values()[1], top, leftBottom);
		this.sides[2] = new Side(Player.Colour.values()[2], rightBottom, top);
		
		
		// Create puck
		int randomAngle = (int) Math.round(Math.random() * 359);//XXX set the randomangle to an angle always to the middle of the field (2 places)
		/*XXX*/this.puck = new Puck(randomAngle, this.getRandomPosition(), averageRating);
		this.puck = new Puck(180, new Point(250, 250), averageRating);
		
		// Create barricades
		int nBarricades = (int)Math.round(0.00 + (((double)averageRating / 40.00) * (5.00 - 0.00)));
		for(int i = 0; i < nBarricades; i++){
			barricades.add(new Barricade(this.getRandomPosition(), averageRating));
		}
	}
	
	
	
	public void setRandomPuck(){
		int randomAngle = (int) Math.round(Math.random() * 359);
		this.puck = new Puck(randomAngle, this.getRandomPosition(), this.averageRating);
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
	
	Point getRandomPosition(){
		int height = (int) (Math.tan(Math.toRadians(60)) * Side.LENGTH / 2);
		int randomX = (int) Math.round(Math.random() * Side.LENGTH / 2);
		int randomY =  20 + (int) Math.round(Math.random() * (height - 40));
		Point point = new Point(randomX, randomY);
		
		// If the point falls above the left side, transform so it will fall under the right side
		if(this.sides[1].isAboveLine(point)){
			point = new Point(point.x + (Side.LENGTH / 2), height - point.y);
		}
		
		//XXX check if it is near(20) the blue or green line, yes--> do this again
		
		return point;
	}
	
	
	
	public void startUpdaterThread(){
		updaterThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					update();
					try{
						Thread.sleep(GameField.UPDATE_SPEED);
					}catch(InterruptedException exception){
						exception.printStackTrace();
						break;
					}
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
					System.out.println("Puck has gone over line " + side.getColour());
					// Adjust puck's angle (Not tested)
					double alpha = Math.toDegrees(Math.atan(side.getGoal().gety_perx() / 1));
					//double newAngle = m - (puck.getAngle() + 360 - m);
					double newAngle = (2 * alpha) - puck.getAngle() + 360;
					while(newAngle >= 360){
						newAngle -= 360;
					}
					while(newAngle < 0){
						newAngle += 360;
					}
					this.puck.setAngle(newAngle);
					this.puck.move();
					break;
				case IN_GOAL:
					System.out.println("Puck has gone in goal " + side.getColour());
					game.increaseRound(side.getColour());
					break;
			}
		}
		//TODO(low) also check for collisions with barricades
		
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
