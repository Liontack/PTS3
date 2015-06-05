package model;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import view.GameScreen;

public class GameManagement{
	private static GameManagement instance;
	
	private Set<Game> games = new HashSet<>();
	
	
	
	private GameManagement(){
		
	}
	
	public static GameManagement getInstance(){
		if(GameManagement.instance == null){
			instance = new GameManagement();
		}
		return GameManagement.instance;
	}
	
	
	
	public static boolean joinGame(User user){
		if(user.getPlayer() != null){
			return false;
		}
		for(Game game : GameManagement.instance.games){
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
		
		GameManagement.instance.games.add(newGame);
		
		return player != null && newGame != null;
	}
	
	
	
	public static boolean startGame(Player player){
		for(Game game : GameManagement.instance.games){
			if(game.getPlayers().contains(player)){
				return game.startGame();
			}
		}
		return false;
	}
	
	
	
	private void addListener(GameScreen gameScreen){
		
	}
	
	private void removeListener(GameScreen gameScreen){
		
	}
	
	
	
	public static void draw(Game game, Graphics g){
		game.draw(g);
	}
	
}
