package remote;

import java.io.Serializable;

public class PlayersInGameUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final String[] usernames;
	public final double[] ratings;
	
	public PlayersInGameUpdate(int gameID, String[] usernames, double[] ratings){
		this.gameID = gameID;
		this.usernames = usernames;
		this.ratings = ratings;
	}
}
