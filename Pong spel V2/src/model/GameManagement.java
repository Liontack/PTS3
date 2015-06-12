package model;

import java.awt.Graphics;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;

import remote.ISecured;

public class GameManagement extends UnicastRemoteObject implements ISecured, RemotePublisher{
	private static final long serialVersionUID = 1L;
	
	private static GameManagement instance;
	
	private Set<Game> games = new HashSet<>();
	
	
	
	private GameManagement() throws RemoteException{
		
	}
	
	public static GameManagement getInstance(){
		if(GameManagement.instance == null){
			try{
				instance = new GameManagement();
			}catch(RemoteException exception){}
		}
		return GameManagement.instance;
	}
	
	
	
	public int joinGame(int userID) throws RemoteException{
		User user = UserManagement.getUserByID(userID);
		if(user.getPlayer() != null){
			return 0;
		}
		for(Game game : GameManagement.getInstance().games){
			if(!game.isReadyToPlay()){
				Player player = game.addPlayer(false);
				user.setPlayer(player);
				return (player == null) ? 0 : game.getID();
			}
		}
		return GameManagement.createGame(user);
	}
	
	public void leaveGame(int userID) throws RemoteException{
		User user = UserManagement.getUserByID(userID);
		
		for(Game game : GameManagement.getInstance().games){
			if(game.containsPlayer(user.getPlayer())){
				game.removePlayer(user.getPlayer());
				user.clearPlayer();
			}
		}
	}
	
	public boolean startGame(int userID) throws RemoteException{
		Player player = UserManagement.getUserByID(userID).getPlayer();
		for(Game game : GameManagement.getInstance().games){
			if(game.getPlayers().contains(player)){
				return game.startGame();
			}
		}
		return false;
	}
	
	public void moveBat(int userID, boolean left) throws RemoteException{
		if(left){
			UserManagement.getUserByID(userID).getPlayer().getBat().moveLeft();
		}else{
			UserManagement.getUserByID(userID).getPlayer().getBat().moveRight();
		}
	}
	
	public void usePowerUp(int userID, int nr) throws RemoteException{
		UserManagement.getUserByID(userID).getPlayer().usePowerUp(nr);
	}
	
	
	
	private static int createGame(User user){
		Game newGame = new Game();
		Player player = newGame.addPlayer(false);
		user.setPlayer(player);
		
		GameManagement.getInstance().games.add(newGame);
		
		return (player != null && newGame != null) ? newGame.getID() : 0 ;
	}
	
	
	
	public static void draw(Game game, Graphics g){
		game.draw(g);
	}
	
	
	
	@Override
	public void addListener(RemotePropertyListener arg0, String arg1) throws RemoteException{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(RemotePropertyListener arg0, String arg1) throws RemoteException{
		// TODO Auto-generated method stub
		
	}
	
}
