package remote;

import java.io.Serializable;

public class GameUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final int currentRound;
	public final int puckX;
	public final int puckY;
	public final int[] batPositions;
	public final int[] playerPoints;
	
	public GameUpdate(int gameID, int currentRound, int puckX, int puckY, int[] batPositions, int[] playerPoints){
		this.gameID = gameID;
		this.currentRound = currentRound;
		this.puckX = puckX;
		this.puckY = puckY;
		this.batPositions = batPositions;
		this.playerPoints = playerPoints;
	}
	
}
