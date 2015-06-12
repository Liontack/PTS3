package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import comparator.RatingWrapper;

public interface IUnsecured extends Remote{
	
	/**
	 * Register a new user with username and password, if username is not in use
	 * @param username
	 * @param password
	 * @return 0 if the given username was already in use, otherwise the userID of the new user 
	 * @throws RemoteException
	 */
	public int registerUser(String username, String password) throws RemoteException;
	
	/**
	 * Try to login an user with username and password
	 * @param username
	 * @param password
	 * @return 0 if the user could not get logged in (wrong password or already logged in),
	 * 		   otherwise the userID of the user that was logged in
	 * @throws RemoteException
	 */
	public int userLogin(String username, String password) throws RemoteException;
	
	/**
	 * Log the user with userID out
	 * @param userID
	 * @throws RemoteException
	 */
	public void userLogout(int userID) throws RemoteException;
	
	/**
	 * Get the user ratings
	 * @return a set with usernames and ratings in a wrapperclass
	 * @throws RemoteException
	 */
	public Set<RatingWrapper> getUserRatings() throws RemoteException;
	
}
