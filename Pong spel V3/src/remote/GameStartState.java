package remote;

import java.io.Serializable;

public class GameStartState implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final int[] barricadeXs;
	public final int[] barricadeYs;
	public final int averageRating; //To calculate the size of the barricades
	public final int puckX;
	public final int puckY;
	
	public GameStartState(int gameID, int[] xs, int[] ys, int averageRating, int puckX, int puckY){
		this.gameID = gameID;
		this.barricadeXs = xs;
		this.barricadeYs = ys;
		this.averageRating = averageRating;
		this.puckX = puckX;
		this.puckY = puckY;
	}
	
}
