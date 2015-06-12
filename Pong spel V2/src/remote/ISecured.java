package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISecured extends Remote{
	
	/**
	 * Let user with userID join a game
	 * @param userID
	 * @return The gameID of the game the user joined
	 * @throws RemoteException
	 */
	public int joinGame(int userID) throws RemoteException;
	
	/**
	 * Let user with userID leave the game he is currently in
	 * @param userID
	 * @throws RemoteException
	 */
	public void leaveGame(int userID) throws RemoteException;
	
	/**
	 * The game user with userID is in, is tried to start
	 * @param userID; The userID of the red/first user in the game
	 * @return Whether the game was started or not
	 * @throws RemoteException
	 */
	public boolean startGame(int userID) throws RemoteException;
	
	/**
	 * Let the bat of the player of user with userID move left or right
	 * @param userID
	 * @param left; false indicates right
	 * @throws RemoteException
	 */
	public void moveBat(int userID, boolean left) throws RemoteException;
	
	/**
	 * Use the nr'th powerUp of user with userID
	 * @param userID
	 * @param nr; less than the number of powerUps user with userID has
	 * @throws RemoteException
	 */
	public void usePowerUp(int userID, int nr) throws RemoteException;
	
}
