package model;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class GameManagement{
	
	private final static GameManagement instance = new GameManagement();
	
	private Set<Game> games = new HashSet<>();
	
	
	
	private GameManagement(){
		
	}
	
	
	
	public static boolean joinGame(User user){
		for(Game game : instance.games){
			if(!game.isReadyToPlay()){
				Player player = game.addPlayer(false);
				user.setPlayer(player);
				return player != null;
			}
		}
		return createGame(user);
	}
	
	private static boolean createGame(User user){
		Game newGame = new Game();
		Player player = newGame.addPlayer(false);
		user.setPlayer(player);
		
		instance.games.add(newGame);
		
		return player != null && newGame != null;
	}
	
	
	
	public static boolean startGame(Player player){
		for(Game game : instance.games){
			if(game.getPlayers().contains(player)){
				return game.startGame();
			}
		}
		return false;
	}
	
	/* XXX ~GameManagement add and remove listeners at iteration 2
	private void addListener(Gui){
		
	}
	
	private void removeListener(Gui){
		
	}
	*/
	
	public void draw(Game game, Graphics g){
		game.draw(g);
	}
	
}
