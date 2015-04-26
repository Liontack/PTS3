package model;

import java.util.Set;

public class GameField{
	
	public final Side[] sides = new Side[3];
	public final Puck puck;
	public final Set<Barricade> barricades;
	
	public GameField(){
		// Create sides
		for(int i = 0; i < this.sides.length; i++){
			this.sides[i] = new Side(Player.Colour.values()[i]);
		}
		
		// Create Puck
		this.puck = new Puck();
		
		// Create Barricades
		for(int i = 0; i < 5; i++){//XXX 5 = maximum barricades, moet nog berekend worden
			barricades.add(new Barricade());
		}
	}
	
	public Side getSide(Player.Colour colour){
		for(Side side : this.sides){
			if(side.getColour() == colour){
				return side;
			}
		}
		return null;
	}
	
}
