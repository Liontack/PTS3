package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import keyboard.BatController;

import view.Program;
import view.StartScreen;

public class Game{
	
	public static int ROUND_AMOUNT = 10;
	
	
	
	private int currentRound = 0;
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
					if(user != null){
						if(this.containsPlayer(user.getPlayer())){
							averageRating += user.getRating();
						}
					}
				}
				averageRating /= this.players.length;
				averageRating /= 40;
				this.gameField = new GameField(this, (int)averageRating);
				
				// Give every player an bat to play with
				for(Player player : this.players){
					player.setBat(gameField.getSide(player.getColour()).getGoal().getBat());
				}
				
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
	 * Checks if 3 players joined
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
		Set<Player> players = new HashSet<Player>(Arrays.asList(this.players));
		players.remove(null);
		return players;
	}
	
	public Player getScorer(){
		return this.scorer;
	}
	
	public void increaseRound(Player.Colour scoredInGoalOf){
		if(started){
			// Change player's scores
			for(Player player : this.players){
				if(player.getColour() == scoredInGoalOf){
					player.setPoints(player.getPoints() - 2);
				}else if(player == this.scorer){
					player.setPoints(player.getPoints() + 2);
				}
			}
			
			// Reset the puck's position and angle
			try{
				this.gameField.setRandomPuck();
			}catch(NullPointerException exception){}
			
			// Reset the scorer
			this.scorer = null;
			
			// Increase the round
			if(this.currentRound == Game.ROUND_AMOUNT){
				this.finish();
				return;
			}
			this.currentRound++;
			
			// Announce the next round
			Program.setFeedback("Volgende ronde begint zo", Color.cyan);

			// Wait a bit
			try{
				Thread.sleep(3000);
			}catch(InterruptedException exception){}
		}
	}
	
	private void finish(){
		// Make this instance unusable
		if(this.gameField != null){
			this.gameField.stopUpdaterThread();
			this.gameField = null;
		}
		
		// Add the player's scores to their user's point list
		for(Player player : this.players){
			if(!player.isAI()){
				User user = UserManagement.getUserOfPlayer(player);
				if(user != null){
					user.addNewRecentPoints(player.getPoints());
				}
			}else{
				// Turn the ai movers off
				player.stopAiMoverThread();
			}
		}
		
		// Remove the bat from the controller
		BatController.resetBat();
		
		// Announce game over, wait a bit, and redirect to the start screen
		Program.setFeedback("Game over", Color.cyan);
		new Thread(new Runnable(){
			public void run(){
				try{
					Thread.sleep(5000);
				}catch(InterruptedException exception){}
				Program.switchToPanel(StartScreen.class);
			}
		}).start();
	}
	
	/**
	 * Add a new player to this game; only if not started
	 * @return	The player which was created and added or null if it couldn't
	 */
	public Player addPlayer(boolean ai){
		if(!this.started){
			for(int i = 0; i < this.players.length; i++){
				if(this.players[i] == null){
					Player.Colour colour = Player.Colour.values()[i];
					Player newPlayer = new Player(colour, ai);
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
		if(this.gameField != null){
			this.gameField.draw(g);
		}
	}
	
}
