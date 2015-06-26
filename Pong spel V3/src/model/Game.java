package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import remote.GameStartState;
import remote.GameUpdate;
import remote.PlayersInGameUpdate;

import keyboard.BatController;

import view.Program;
import view.StartScreen;

public class Game{
	private static int NEXT_GAME_ID = 1;
	public static int ROUND_AMOUNT = 10;//XXX reset to 10
	
	
	
	private final int id;
	private int currentRound = 0;
	private boolean started = false;
	private boolean finished = false;
	private GameField gameField;
	private Player[] players = new Player[3];
	private Player scorer;
	
	public final boolean drawOnly;
	
	
	public Game(boolean drawOnly){
		this.id = Game.NEXT_GAME_ID++;
		this.drawOnly = drawOnly;
	}
	
	
	
	public int getID(){
		return this.id;
	}
	
	/**
	 * Start this game with the 3 joined players; only if not started
	 * @return	True if the game could be started or false if the game has not yet 3 players
	 */
	public boolean startGame(){
		if(!this.started && !this.drawOnly){
			this.started = this.isReadyToPlay();
			
			// Create the gameField with knowledge of the players
			if(this.started){
				this.gameField = new GameField(this, this.getAverageRating());
				
				serialize();
				
				// Give every player an bat to play with
				for(Player player : this.players){
					player.setBat(gameField.getSide(player.getColour()).getGoal().getBat());
				}
				
				this.currentRound = 1;
			}
			
			return this.isReadyToPlay();
		}
		return false;
	}
	
	public boolean containsPlayer(Player player){
		if(!this.drawOnly){
			for(Player existingPlayer : this.players){
				if(existingPlayer == player){
					return true;
				}
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
		if(!this.drawOnly){
			for(Player player : this.players){
				if(player != null){
					players++;
				}
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
		if(this.drawOnly){
			return null;
		}
		Set<Player> players = new HashSet<Player>(Arrays.asList(this.players));
		players.remove(null);
		return players;
	}
	
	public Player getScorer(){
		return this.scorer;
	}
	
	public void increaseRound(Player.Colour scoredInGoalOf){
		if(this.started && !this.drawOnly){
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
			
			// Serialize
			serialize();
			
			if(this == Program.offlineGame){
				// Announce the next round
				Program.setFeedback("Volgende ronde begint zo", Color.cyan);
			}
			
			// Wait 5 seconds, but also give gameUpdates in the meantime
			try{
				long startSleep = System.currentTimeMillis();
				
				while(System.currentTimeMillis() < startSleep + 5000){
					Thread.sleep(GameField.UPDATE_SPEED);
					
					if(this != Program.offlineGame){
						GameManagement.informGameUpdate(this);
					}
				}
			}catch(InterruptedException exception){
				System.err.println("The 5sec delay before the game was interrupted");
			}
		}
	}
	
	private void finish(){
		this.finished = true;
		
		// Make this instance unusable
		if(this.gameField != null){
			this.gameField.stopUpdaterThread();
			this.gameField = null;
		}

		if(this != Program.offlineGame){
			try{
				GameManagement.informGameFinished(this);
			}catch(Exception exception){}
		}
		
		if(this != Program.offlineGame){
			// Add the player's scores to their user's point list
			for(Player player : this.players){
				if(!player.isAI()){
					User user = UserManagement.getUserOfPlayer(player);
					if(user != null){
						user.addNewRecentPoints(player.getPoints());
					}
					user.clearPlayer();
				}else{
					// Turn the ai movers off
					player.stopAiMoverThread();
				}
			}
		}
		
		// Remove the bat from the controller
		BatController.resetBat();
		
		// Remove the serialized back up
		this.removeSerializedBackup();
		
		final boolean isOfflineGame = this == Program.offlineGame;
		if(isOfflineGame){
			// Announce game over, wait a bit, and redirect to the start screen
			Program.setFeedback("Game over", Color.cyan);
			
		}
		
		new Thread(new Runnable(){
			public void run(){
				try{
					Thread.sleep(5000);
					
					Program.offlineGame = null;
					Program.offlinePlayer = null;
				}catch(InterruptedException exception){
					exception.printStackTrace();
				}
				if(isOfflineGame){
					Program.switchToPanel(StartScreen.class);
				}
			}
		}).start();
		
	}
	
	/**
	 * Add a new player to this game; only if not started
	 * @return	The player which was created and added or null if it couldn't
	 */
	public Player addPlayer(boolean ai){
		if(!this.started && !this.drawOnly){
			for(int i = 0; i < this.players.length; i++){
				if(this.players[i] == null){
					Player.Colour colour = Player.Colour.values()[i];
					Player newPlayer = new Player(colour, ai);
					this.players[i] = newPlayer;
					serialize();
					return newPlayer;
				}
			}
		}
		return null;
	}
	
	/**
	 * Remove the player from this game; only if not started
	 * It is always the case that the null values are at the end of the array
	 * Note: The colours of the players should also stay in the correct order,
	 * 		 so the existing users get new player objects with the correct colours
	 * @param player	The player to remove
	 * @return			True if the player was removed
	 */
	public boolean removePlayer(Player player){
		if(player == null){
			return false;
		}
		boolean playerRemoved = false;
		if(!started){
			for(int i = 0; i < this.players.length; i++){
				User userWithPlayerI = UserManagement.getUserOfPlayer(this.players[i]);
				
				if(this.players[i] == player){
					this.players[i] = null;
					userWithPlayerI.clearPlayer();
					playerRemoved = true;
				}else if(playerRemoved && userWithPlayerI != null){
					userWithPlayerI.setPlayer(new Player(Player.Colour.values()[i - 1], false));
					this.players[i - 1] = userWithPlayerI.getPlayer();
					this.players[i] = null;
				}
			}
		}
		return playerRemoved;
	}
	
	public void setScorer(Player.Colour colour){
		if(!this.drawOnly){
			for(Player player : this.players){
				if(player.getColour() == colour){
					this.scorer = player;
					break;
				}
			}
		}
	}
	
	private int getAverageRating(){
		double averageRating = 0;
		for(User user : UserManagement.getUsers()){
			if(user != null){
				if(this.containsPlayer(user.getPlayer())){
					averageRating += user.getRating();
				}
			}
		}
		averageRating /= this.players.length;
		return (int)averageRating;
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	
	
	public void serialize(){
		if(!this.drawOnly){
			// Serialize the following data, in the given order
			//	Game:			currentRound
			//	GameField:	all barricade positions each time an x and y int value
			//	Player:		colour, isAI, points, all powerUps, username from user
			try{
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("game" + this.id + ".data"));
				out.write(this.currentRound);
				if(this.getGameField() != null){
					for(Barricade barricade : this.getGameField().getBarricades()){
						out.writeObject(barricade.getPosition().x);
						out.writeObject(barricade.getPosition().y);
					}
				}
				for(Player player : this.getPlayers()){
					out.writeObject(player.getColour());
					out.writeObject(player.isAI());
					out.writeObject(player.getPoints());
					for(PowerUp powerUp : player.getPowerUps()){
						if(powerUp != null){
							out.writeObject(powerUp.getKind());
						}
					}
					
					String username = "";
					try{
						username = UserManagement.getUserOfPlayer(player).getUsername();
					}catch(NullPointerException exception){}
					out.writeObject(username);
				}
				out.close();
			}catch(IOException exception){}
		}
	}
	
	public void removeSerializedBackup(){
		if(!this.drawOnly){
			File gameData = new File("game" + this.id + ".data");
			if(gameData.exists()){
				gameData.delete();
			}
		}
	}
	
	
	
	public GameUpdate getGameUpdate(){
		if(this.drawOnly){
			return null;
		}
		
		int puckX = this.gameField.getPuck().getPosition().x;
		int puckY = this.gameField.getPuck().getPosition().y;
		
		int[] batPositions = new int[this.players.length];
		int[] playerPoints = new int[this.players.length];
		int i = 0;
		for(Player player : this.players){
			batPositions[i] = player.getBat().getPositionInGoal();
			playerPoints[i] = player.getPoints();
			i++;
		}
		
		return new GameUpdate(this.id, this.currentRound, puckX, puckY, batPositions, playerPoints);
	}
	
	public PlayersInGameUpdate getPlayersInGameUpdate(){
		if(this.drawOnly){
			return null;
		}
		
		String[] usernames = new String[this.players.length];
		double[] ratings = new double[this.players.length];
		for(int i = 0; i < this.players.length; i++){
			if(this.players[i] == null){
				usernames[i] = "";
				ratings[i] = 0;
			}else{
				User userOfPlayer = UserManagement.getUserOfPlayer(this.players[i]);
				usernames[i] = userOfPlayer.getUsername();
				ratings[i] = userOfPlayer.getRating();
			}
		}
		
		return new PlayersInGameUpdate(this.id, usernames, ratings);
	}
	
	public GameStartState getGameStartState(){
		if(this.drawOnly){
			return null;
		}
		
		int[] xs = new int[this.gameField.getBarricades().size()];
		int[] ys = new int[this.gameField.getBarricades().size()];
		int i = 0;
		for(Barricade barricade : this.gameField.getBarricades()){
			xs[i] = barricade.getPosition().x;
			ys[i] = barricade.getPosition().y;
			i++;
		}
		int puckX = this.gameField.getPuck().getPosition().x;
		int puckY = this.gameField.getPuck().getPosition().y;
		
		return new GameStartState(this.id, xs, ys, this.getAverageRating(), puckX, puckY);
	}
	
	
	
	public void draw(Graphics g, Player.Colour playerColourDown){
		((Graphics2D) g).translate(150, 50);
		if(this.gameField != null){
			// Rotate the gamefield graphics, such that playerColourDown is on the bottom of the screen
			int rotationDegrees = 0;// Default and RED
			switch(playerColourDown){
				case GREEN:
					rotationDegrees = 120;
					break;
				case BLUE:
					rotationDegrees = 240;
					break;
			}
			Point center = this.gameField.getCenter();
			((Graphics2D) g).rotate(Math.toRadians(rotationDegrees), center.x, center.y);
			
			this.gameField.draw(g);
		}
	}
	
	/* Draw only functions */
	public void setGameStartState(GameStartState state){
		if(this.drawOnly){
			// Set the gamefield with this state, it will see this game is a drawonly
			this.gameField = new GameField(this, state);
		}
	}
	
	public void setGameUpdate(GameUpdate update){
		if(this.drawOnly){
			if(this.gameField == null){
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){}
			}
			
			// Set the positions of the puck and all bats
			this.gameField.getPuck().setPosition(new Point(update.puckX, update.puckY));
			for(int i = 0; i < update.batPositions.length; i++){
				this.gameField.getSide(Player.Colour.values()[i]).getGoal().getBat().setPositionInGoal(update.batPositions[i]);
			}
			
			// If the currentRound is to change
			if(this.currentRound != 0 && this.currentRound != update.currentRound){
				// Announce the next round
				Program.setFeedback("Volgende ronde begint zo", Color.cyan);
			}
			
			// Set the currentRound
			this.currentRound = update.currentRound;
		}
	}
	/* End draw only functions */
	
}
