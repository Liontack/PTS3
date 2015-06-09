package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISecured extends Remote{
	
	public boolean joinGame(int userID) throws RemoteException;

	public boolean startGame(int userID) throws RemoteException;

	public void leaveGame(int userID) throws RemoteException;
	
	public void moveBat(int userID, boolean left) throws RemoteException;
	
	public void usePowerUp(int userID, int nr) throws RemoteException;
	
}
