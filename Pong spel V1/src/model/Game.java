package model;

import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Game{
	
	public static int ROUND_AMOUNT = 10;
	
	
	
	private int currentRound = 1;
	private boolean started = false;
	private GameField gameField;
	private Player[] players = new Player[3];
	private Player scorer;
	
	
	
	public Game(){}
	
	
	
	/**
	 * Start this game with the 3 joined players; only if not started
	 * @return	True if the game could be started or false if the game has not yet 3 players
	 */
	public boolean startGame(){
		if(!this.started){
			this.started = this.isReadyToPlay();
			
			// Create the gameField with knowledge of the players
			if(this.started){
				double averageRating = 0;
				for(User user : UserManagement.getUsers()){
					if(this.containsPlayer(user.getPlayer())){
						averageRating += user.getRating();
					}
				}
				averageRating /= this.players.length;
				averageRating /= 40;// XXX averageRatings should be used correct
				this.gameField = new GameField((int)averageRating);
			}
			
			return this.isReadyToPlay();
		}
		return false;
	}
	
	private boolean containsPlayer(Player player){
		for(Player existingPlayer : this.players){
			if(existingPlayer == player){
				return true;
			}
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
	
	public Set<Player> getPlayers(){
		return new HashSet<Player>(Arrays.asList(this.players));
	}
	
	public Player getScorer(){
		return this.scorer;
	}
	
	public void increaseRound(){
		//TODO Game --> increase round
		if(started){
			if(this.currentRound == Game.ROUND_AMOUNT){
				this.finish();
			}
			this.currentRound++;
		}
	}
	
	private void finish(){
		//TODO Game --> finish
	}
	
	/**
	 * Add a new player to this game; only if not started
	 * @return	True the player which was created and added or null if it couldn't
	 */
	public Player addPlayer(boolean ai){
		if(!this.started){
			for(int i = 0; i < this.players.length; i++){
				if(this.players[i] == null){
					Player.Colour colour = Player.Colour.values()[i];
					Player newPlayer = new Player(colour, gameField.getSide(colour).getGoal().getBat(), ai);
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
	
	public void setScorer(Player.Colour colour){
		for(Player player : this.players){
			if(player.getColour() == colour){
				this.scorer = player;
				break;
			}
		}
	}
	
	
	public void draw(Graphics g){
		this.gameField.draw(g);
	}
	
}
