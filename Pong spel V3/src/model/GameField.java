package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import remote.GameStartState;


import view.Program;

public class GameField{
	
	public static final int UPDATE_SPEED = 10;
	private static final int MIN_BARRICADES = 0;
	private static final int MAX_BARRICADES = 5;
	
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
		this.setRandomPuck();
		
		// Create barricades
		int nBarricades = (int)Math.round(MIN_BARRICADES + (((double)averageRating / Player.MAX_RATING) * (MAX_BARRICADES - MIN_BARRICADES)));
		for(int i = 0; i < nBarricades; i++){
			barricades.add(new Barricade(this.getRandomPositionInCenter(), averageRating));
		}
	}
	
	public GameField(Game game, GameStartState state){
		if(game.drawOnly){
			this.game = game;
			
			// Create sides
			int height = (int) (Math.tan(Math.toRadians(60)) * Side.LENGTH / 2);
			Point leftBottom = new Point(0, height), rightBottom = new Point(Side.LENGTH, height), top = new Point(Side.LENGTH / 2, 0);
			this.sides[0] = new Side(Player.Colour.values()[0], leftBottom, rightBottom);
			this.sides[1] = new Side(Player.Colour.values()[1], top, leftBottom);
			this.sides[2] = new Side(Player.Colour.values()[2], rightBottom, top);
			
			
			// Create puck
			this.puck = new Puck(0, new Point(state.puckX, state.puckY), 0);
			
			// Create barricades
			for(int i = 0; i < state.barricadeXs.length; i++){
				barricades.add(new Barricade(new Point(state.barricadeXs[i], state.barricadeYs[i]), state.averageRating));
			}
		}else{
			throw new RuntimeException("A non drawonly game tried to create a wrong gamefield");
		}
	}
	
	
	
	public void setRandomPuck(){
		Point randomPosition = this.getRandomPositionInCenter();
		Point center = this.getCenter();
		double dy = (randomPosition.y - center.y);
		double dx = (randomPosition.x - center.x);
		double angle;
		if(dx == 0){
			angle = ((dy > 0) ? 90 : 270 );
		}else{
			double a = dy / dx;
			angle = Math.toDegrees(Math.atan(a / 1));
			if(dx > 0){
				angle += 180;
			}else if(dx < 0 && dy > 0){
				angle += 360;
			}
		}
		this.puck = new Puck(angle, randomPosition, this.averageRating);
	}
	
	public Point getCenter(){
		int height = (int) (Math.tan(Math.toRadians(60)) * Side.LENGTH / 2);
		return new Point(Side.LENGTH / 2, height * 2 / 3);
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
	
	Point getRandomPositionInCenter(){
		boolean hitBarricade = true;
		Point point = null;
		while(hitBarricade){
			hitBarricade = false;
			double randomAngle = Math.random() * 360;
			double randomLength = 10 + (Math.random() * Side.LENGTH / 8);
			point = new Point(this.getCenter().x + (int)(Math.cos(randomAngle) * randomLength), this.getCenter().y + (int)(Math.sin(randomAngle) * randomLength));
			
			// Check if the new point would interfere with another barricade
			for(Barricade barricade : this.barricades){
				if(barricade.getPosition().distance(point) < barricade.getDiameter()){
					hitBarricade = true;
					break;
				}
			}
			// Check if the new point would interfere with the puck
			if(this.getPuck() != null){
				if(this.getPuck().getPosition().distance(point) < Puck.getDiameter()){
					hitBarricade = true;
					break;
				}
			}
		}
		
		return point;
	}
	
	Point getRandomPosition(){
		int height = (int) (Math.tan(Math.toRadians(60)) * Side.LENGTH / 2) - 45;
		Point point = null;
		
		boolean wrongPoint = true;
		while(wrongPoint){
			wrongPoint = false;
			int randomX = (int) Math.round(Math.random() * Side.LENGTH / 2);
			int randomY = (int) Math.round(Math.random() * height);
			point = new Point(randomX, randomY);
			
			if(this.sides[1].isAboveLine(point)){
				point = new Point(point.x + Side.LENGTH / 2, height - point.y);
			}
			if((point.x < (Side.LENGTH / 2) && this.sides[1].getYonX(point.x) + 20 >= point.y) ||
					(point.x > (Side.LENGTH / 2) && this.sides[2].getYonX(point.x) + 20 >= point.y)){
				wrongPoint = true;
			}
		}
		
		return point;
	}
	
	
	
	public void startUpdaterThread(){
		updaterThread = new Thread(new Runnable(){
			public void run(){
				boolean offlineGame = Program.offlineGame != null;
				
				if(!offlineGame){
					// Inform the puck's position at start
					GameManagement.informGameUpdate(game);
				}
				
				if(offlineGame){
					// Announce the first round
					Program.setFeedback("Eerste ronde begint zo", Color.cyan);
				}
				
				// Wait 5 seconds, but also give gameUpdates in the meantime
				try{
					long startSleep = System.currentTimeMillis();
					
					while(System.currentTimeMillis() < startSleep + 5000){
						Thread.sleep(GameField.UPDATE_SPEED);
						
						if(!offlineGame){
							GameManagement.informGameUpdate(game);
						}
					}
				}catch(InterruptedException exception){
					System.err.println("The 5sec delay before the game was interrupted");
				}
				
				// Keep updating, until the thread gets interrupted
				while(!game.isFinished()){
					game.serialize();
					
					if(!offlineGame){
						GameManagement.informGameUpdate(game);
					}
					
					update();
					try{
						Thread.sleep(GameField.UPDATE_SPEED);
					}catch(InterruptedException exception){
						break;
					}
				}
				//XXX System.out.println("Game ended");
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
				case HIT_BAT:
					game.setScorer(side.getColour());
				case OVER_LINE:
					//XXX System.out.println("Puck hit " + side.getColour() + " side");
					// Adjust puck's angle (Not tested)
					double alpha = Math.toDegrees(Math.atan(side.getGoal().gety_perx() / 1));
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
					//XXX System.out.println("Puck went in " + side.getColour() + " goal");
					game.increaseRound(side.getColour());
					break;
			}
		}
		// Check for collisions with barricades
		for(Barricade barricade : this.barricades){
			if(barricade.hit(this.puck)){
				// Adjust puck's angle (Not tested)
				double a = (double)(barricade.getPosition().y - this.puck.getPosition().y) / (double)(barricade.getPosition().x - this.puck.getPosition().x);
				double angleToBarricade = Math.toDegrees(Math.atan(a));
				if(barricade.getPosition().x < this.puck.getPosition().x){
					angleToBarricade -= 180;
				}
				double newAngle = angleToBarricade - 180;
				while(newAngle < 0){
					newAngle += 360;
				}
				this.puck.setAngle(newAngle);
			}
		}
		
		if(Program.offlineGame != null && !game.isFinished()){
			// Update the Game screen
			((JPanel)Program.getActivePanel()).repaint();
		}
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
