package model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class GameField{
	
	private final Side[] sides = new Side[3];
	private final Puck puck;
	private final Set<Barricade> barricades = new HashSet<>();
	
	
	
	public GameField(int averageRating){
		// Create sides
		Point leftBottom = new Point(0, 0), rightBottom = new Point(Side.LENGTH, 0), top = new Point(Side.LENGTH/2, (int)(1.73 * Side.LENGTH));
		this.sides[0] = new Side(Player.Colour.values()[0], leftBottom, rightBottom);
		this.sides[1] = new Side(Player.Colour.values()[1], top, leftBottom);
		this.sides[2] = new Side(Player.Colour.values()[2], rightBottom, top);
		
		
		// Create puck
		int randomAngle = (int) Math.round(Math.random() * 360);
		this.puck = new Puck(randomAngle, this.getRandomPosition(), averageRating);
		
		// Create barricades
		for(int i = 0; i < averageRating * 5; i++){
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
		Point point;
		do{
			int randomX = (int) Math.round(Math.random() * Side.LENGTH);
			int randomY = (int) Math.round(Math.random() * Side.LENGTH);
			point = new Point(randomX, randomY);
		}while(!this.sides[0].isAboveLine(point) || this.sides[1].isAboveLine(point) || this.sides[2].isAboveLine(point));
		
		return point;
	}
	
	
	
	public void startUpdaterThread(){
		//TODO GameField --> updater thread
	}
	
	private void update(){
		//XXX used in updater thread
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
