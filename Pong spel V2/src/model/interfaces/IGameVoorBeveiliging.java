package model.interfaces;

import java.awt.Graphics;
import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Game;
import model.Player;
import model.User;

public interface IGameVoorBeveiliging extends Remote{
	
	public boolean joinGame(User user) throws RemoteException;
	
	public boolean startGame(Player player) throws RemoteException;
	
	public void moveBat(Player player, boolean left) throws RemoteException;
	
	public void userPowerUp(Player player, int nr) throws RemoteException;
	
	public void draw(Game game, Graphics g) throws RemoteException;
	
}
