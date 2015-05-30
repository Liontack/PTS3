package model;

import java.awt.Graphics;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import model.interfaces.IGameVoorBeveiliging;

import view.GameScreen;

public class GameManagement extends UnicastRemoteObject implements IGameVoorBeveiliging{
	private static final long serialVersionUID = 1L;

	private static GameManagement instance;
	
	private Set<Game> games = new HashSet<>();
	
	
	
	private GameManagement() throws RemoteException{
		
	}
	
	public static GameManagement getInstance(){
		if(GameManagement.instance == null){
			try{
				instance = new GameManagement();
			}catch(RemoteException ex){
				System.err.println("The GameManagement instance was not created due to an remote exception");
				ex.printStackTrace();
			}
		}
		return GameManagement.instance;
	}
	
	
	
	public boolean joinGame(User user) throws RemoteException{
		if(user.getPlayer() != null){
			return false;
		}
		for(Game game : this.games){
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
	
	
	
	public boolean startGame(Player player) throws RemoteException{
		for(Game game : instance.games){
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
	
	public void moveBat(Player player, boolean left) throws RemoteException{
		if(left){
			player.getBat().moveLeft();
		}else{
			player.getBat().moveRight();
		}
	}
	
	public void userPowerUp(Player player, int nr){
		player.getPowerUp(nr).use();
	}
	
	
	
	public void draw(Game game, Graphics g) throws RemoteException{
		game.draw(g);
	}
	
}
