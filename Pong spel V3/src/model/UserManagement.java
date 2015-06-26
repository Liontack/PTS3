package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import remote.IUnsecured;
import storage.*;

import comparator.RatingWrapper;



public class UserManagement extends UnicastRemoteObject implements IUnsecured{
	private static final long serialVersionUID = 1L;

	private static UserManagement instance;
	
	private Map<User, Boolean> users = new HashMap<>();
	private StorageMediator storageMediator;
	
	
	
	private UserManagement() throws RemoteException{
		
	}
	
	public static UserManagement getInstance(){
		if(UserManagement.instance == null){
			try{
				instance = new UserManagement();
			}catch(RemoteException exception){}
		}
		return UserManagement.instance;
	}
	
	
	
	public synchronized int registerUser(String username, String password) throws RemoteException{
		// Check if there isn't already a user with the given username; username must be unique
		for(User user : getInstance().users.keySet()){
			if(user != null){
				if(user.getUsername().equals(username)){
					return 0;
				}
			}
		}
		
		// Return true if the user was created and logged in
		instance.users.put(new User(username, password, true), false);
		
		// Also save the new user
		this.storageMediator.save();
		
		return userLogin(username, password);
	}
	
	public synchronized int userLogin(String username, String password) throws RemoteException{
		// Login if the username and password are correct and the user wasn't yet logged in
		for(User user : getInstance().users.keySet()){
			if(user != null){
				if(getInstance().users.get(user) != true && user.getUsername().toLowerCase().equals(username.toLowerCase()) && user.equalsPassword(password)){
					getInstance().users.put(user, true);
					return user.getID();
				}
			}
		}
		return 0;
	}
	
	public synchronized void userLogout(int userID) throws RemoteException{
		for(User user : UserManagement.getInstance().users.keySet()){
			if(user.getID() == userID){
				getInstance().users.put(user, false);
			}
		}
	}
	
	public synchronized Set<RatingWrapper> getUserRatings() throws RemoteException{
		Set<RatingWrapper> ratings = new HashSet<>();
		for(User user : UserManagement.getInstance().users.keySet()){
			if(user != null){
				ratings.add(new RatingWrapper(user.getUsername(), user.getRating()));
			}
		}
		
		return ratings;
	}
	
	public static synchronized User getUserOfPlayer(Player player){
		if(player == null){
			return null;
		}
		
		for(User user : UserManagement.getUsers()){
			if(user != null){
				if(user.getPlayer() == player){
					return user;
				}
			}
		}
		return null;
	}
	
	public static synchronized User getUserByID(int userID){
		for(User user : UserManagement.getUsers()){
			if(user != null){
				if(user.getID() == userID){
					return user;
				}
			}
		}
		return null;
	}
	
	public static synchronized Set<User> getUsers(){
		getInstance().users.remove(null);
		return Collections.unmodifiableSet(getInstance().users.keySet());
	}
	
	public static synchronized boolean isUserLoggedIn(User user){
		return getInstance().users.get(user);
	}
	
	public static synchronized void save(){
		UserManagement.getInstance().storageMediator.save();
	}
	
	public static synchronized void load(){
		if(UserManagement.getInstance().storageMediator != null){
			Set<User> users = UserManagement.getInstance().storageMediator.load();
			int highestUserID = 0;
			for(User user : users){
				UserManagement.getInstance().users.put(user, false);
				highestUserID = Math.max(user.getID(), highestUserID);
			}
			
			User.setNextUserID(highestUserID + 1);
		}
	}
	
	public static void setStorageType(Class<? extends StorageMediator> mediatorClass){
		try {
			UserManagement.getInstance().storageMediator = mediatorClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
