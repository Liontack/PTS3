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
		int[] zeroxs = new int[]{ 0, 0, (int)(3.46 * Side.LENGTH) };
		int[] perxs = new int[]{ 0, 2, -2 };
		for(int i = 0; i < this.sides.length; i++){
			this.sides[i] = new Side(Player.Colour.values()[i], zeroxs[i], perxs[i]);
		}
		
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
