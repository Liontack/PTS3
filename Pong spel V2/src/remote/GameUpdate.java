package remote;

import java.io.Serializable;

public class GameUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final int puckX;
	public final int puckY;
	public final int[] batXs;
	public final int[] batYs;
	
	public GameUpdate(int gameID, int puckX, int puckY, int[] xs, int[] ys){
		this.gameID = gameID;
		this.puckX = puckX;
		this.puckY = puckY;
		this.batXs = xs;
		this.batYs = ys;
	}
	
}
