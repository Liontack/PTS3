package model;

public class Game{
	
	public static int ROUND_AMOUNT = 10;
	
	private int currentRound = 1;
	private boolean started = false;
	private GameField gameField;
	private Player[] players = new Player[3];
	
	public Game(){
		this.gameField = new GameField();
	}
	
	/**
	 * Start this game with the 3 joined players; only if not started
	 * @return	True if the game could be started or false if the game has not yet 3 players
	 */
	public boolean startGame(){
		if(!started){
			this.started = this.isReadyToPlay();
			return this.isReadyToPlay();
		}
		return false;
	}
	
	/**
	 * Checks if there 3 players joined
	 * @return	True if there are 3 players, otherwise false
	 */
	public boolean isReadyToPlay(){
		int players = 0;
		for(Player player : this.players){
			if(player != null){
				players++;
			}
		}
		
		return players == this.players.length;
	}
	
	public int getCurrentRound(){
		return this.currentRound;
	}
	
	public GameField getGameField(){
		return this.gameField;
	}
	
	/**
	 * Get the player based on his colour
	 * @param playerColour	The colour of the player you want
	 * @return				The player with colour playerColour or null if this player is not yet present
	 */
	public Player getPlayer(Player.Colour playerColour){
		for(Player player : this.players){
			if(player.colour == playerColour){
				return player;
			}
		}
		return null;
	}
	
	public void increaseRound(){
		if(started){
			if(this.currentRound == Game.ROUND_AMOUNT){
				this.finish();
			}
			this.currentRound++;
		}
	}
	
	private void finish(){
		//TODO -<game>.finish();
	}
	
	/**
	 * Add a new player to this game; only if not started
	 * @return	True the player which was created and added or null if it couldn't
	 */
	public Player addPlayer(boolean ai){
		if(!started){
			for(int i = 0; i < this.players.length; i++){
				if(this.players[i] == null){
					Player.Colour colour = Player.Colour.values()[i];
					Player newPlayer = new Player(colour, gameField.getSide(colour), ai);
					this.players[i] = newPlayer; 
					return newPlayer;
				}
			}
		}
		return null;
	}
	
	/**
	 * Remove the player from this game; only if not started
	 * @param player	The player to remove
	 * @return			True if the player was removed
	 */
	public boolean removePlayer(Player player){
		if(!started){
			for(int i = 0; i < this.players.length; i++){
				if(this.players[i] == player){
					this.players[i] = null;
					return true;
				}
			}
		}
		return false;
	}
}
