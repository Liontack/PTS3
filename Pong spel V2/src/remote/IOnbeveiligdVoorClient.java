package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import comparator.RatingWrapper;

public interface IOnbeveiligdVoorClient extends Remote{
	
	public boolean addUser(String username, String password) throws RemoteException;
	
	public boolean userLogin(String username, String password) throws RemoteException;
	
	public Set<RatingWrapper> getUserRatings() throws RemoteException;
	
}
