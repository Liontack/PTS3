package remote;

import java.io.Serializable;

public class BarricadesState implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final int[] barricadeXs;
	public final int[] barricadeYs;
	public final int averageRating; //To calculate the size of the barricades
	
	public BarricadesState(int gameID, int[] xs, int[] ys, int averageRating){
		this.gameID = gameID;
		this.barricadeXs = xs;
		this.barricadeYs = ys;
		this.averageRating = averageRating;
	}
	
}
