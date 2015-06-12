package remote;

import java.io.Serializable;

public class GameUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final int puckX;
	public final int puckY;
	public final int[] batPositions;
	
	public GameUpdate(int gameID, int puckX, int puckY, int[] batPositions){
		this.gameID = gameID;
		this.puckX = puckX;
		this.puckY = puckY;
		this.batPositions = batPositions;
	}
	
}
