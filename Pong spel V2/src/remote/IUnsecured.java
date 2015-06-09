package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import comparator.RatingWrapper;

public interface IUnsecured extends Remote{
	
	public int registerUser(String username, String password) throws RemoteException;

	public int userLogin(String username, String password) throws RemoteException;

	public void userLogout(int userID) throws RemoteException;
	
	public Set<RatingWrapper> getUserRatings() throws RemoteException;
	
}
