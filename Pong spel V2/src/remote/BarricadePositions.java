package remote;

import java.io.Serializable;

public class BarricadePositions implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final int[] barricadeXs;
	public final int[] barricadeYs;
	
	public BarricadePositions(int gameID, int[] xs, int[] ys){
		this.gameID = gameID;
		this.barricadeXs = xs;
		this.barricadeYs = ys;
	}
	
}
