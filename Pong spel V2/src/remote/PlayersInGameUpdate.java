package remote;

import java.io.Serializable;

public class PlayersInGameUpdate implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final int gameID;
	public final String[] usernames;
	
	public PlayersInGameUpdate(int gameID, String[] usernames){
		this.gameID = gameID;
		this.usernames = usernames;
	}
}
