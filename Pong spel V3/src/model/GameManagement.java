package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;

import remote.GameFinished;
import remote.ISecured;

public class GameManagement extends UnicastRemoteObject implements ISecured{
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
				
				this.basicPublisher.inform(this, "game" + game.getID(), null, game.getPlayersInGameUpdate());
				
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
				
				this.basicPublisher.inform(this, "game" + game.getID(), null, game.getPlayersInGameUpdate());
				
			}
		}
		
	}
	
	public boolean startGame(int userID) throws RemoteException{
		Player player = UserManagement.getUserByID(userID).getPlayer();
		for(Game game : GameManagement.getInstance().games){
			if(game.getPlayers().contains(player)){
				if(game.startGame()){
					this.basicPublisher.inform(this, "game" + game.getID(), null, game.getGameStartState());
					
					game.getGameField().startUpdaterThread();
					
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
	public void moveBat(int userID, boolean left) throws RemoteException{
		try{
			if(left){
				UserManagement.getUserByID(userID).getPlayer().getBat().moveLeft();
			}else{
				UserManagement.getUserByID(userID).getPlayer().getBat().moveRight();
			}
		}catch(NullPointerException exception){
			System.out.println("A moveBat() request was found, but no bat to move. Maybe the game is over?");
		}
	}
	
	public void usePowerUp(int userID, int nr) throws RemoteException{
		UserManagement.getUserByID(userID).getPlayer().usePowerUp(nr);
	}
	
	
	
	private static int createGame(User user){
		Game newGame = new Game(false);
		Player player = newGame.addPlayer(false);
		user.setPlayer(player);
		
		GameManagement.getInstance().games.add(newGame);
		
		GameManagement.getInstance().basicPublisher.addProperty("game" + newGame.getID());
		
		return (player != null && newGame != null) ? newGame.getID() : 0 ;
	}
	
	private static Game getGameByID(int gameID){
		for(Game game : GameManagement.getInstance().games){
			if(game.getID() == gameID){
				return game;
			}
		}
		return null;
	}
	
	
	
	// RemoteObservable part
	private BasicPublisher basicPublisher = new BasicPublisher(new String[]{  });
	
	@Override
	public void addListener(RemotePropertyListener listener, String property) throws RemoteException{
		this.basicPublisher.addListener(listener, property);
		
		int gameID = Integer.valueOf(property.substring(4));
		Game game = GameManagement.getGameByID(gameID);
		
		this.basicPublisher.inform(this, property, null, game.getPlayersInGameUpdate());
	}
	
	@Override
	public void removeListener(RemotePropertyListener listener, String property) throws RemoteException{
		this.basicPublisher.removeListener(listener, property);
	}

	public static void informGameUpdate(Game game){
		GameManagement.getInstance().basicPublisher.inform(GameManagement.getInstance(), "game" + game.getID(), null, game.getGameUpdate());
	}

	public static void informGameFinished(Game game){
		GameManagement.getInstance().games.remove(game);
		GameManagement.getInstance().basicPublisher.inform(GameManagement.getInstance(), "game" + game.getID(), null, new GameFinished());
	}
	
}
