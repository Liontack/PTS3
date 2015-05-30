package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.interfaces.IBeveiligdVoorClient;
import model.interfaces.IOnbeveiligdVoorClient;

import comparator.RatingWrapper;



public class UserManagement extends UnicastRemoteObject implements IOnbeveiligdVoorClient, IBeveiligdVoorClient{
	private static final long serialVersionUID = 1L;

	private static UserManagement instance;
	
	private Map<User, Boolean> users;
	
	
	
	private UserManagement() throws RemoteException{
		fillUsersSet();
	}
	
	public static UserManagement getInstance(){
		if(UserManagement.instance == null){
			try{
				instance = new UserManagement();
			}catch(RemoteException ex){
				System.err.println("The UserManagement instance was not created due to an remote exception");
				ex.printStackTrace();
			}
		}
		return UserManagement.instance;
	}
	
	
	
	public void fillUsersSet(){
		try{
			ObjectInputStream reader = new ObjectInputStream(new FileInputStream("users.data"));
			this.users = new HashMap<>();
			while(true){
				try{
					User user = (User) reader.readObject();
					this.users.put(user, false);
				}catch(Exception exception){
					break;
				}
			}
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public static void saveUsersSet(){
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.data"));
			for(User user : instance.users.keySet()){
				out.writeObject(user);
			}
			out.close();
		}catch(IOException exception){}
	}
	
	
	
	public static synchronized User userLogin(String username, String password){
		// Login if the username and password are correct and the user wasn't yet logged in
		for(User user : instance.users.keySet()){
			if(user != null){
				if(instance.users.get(user) != true && user.getUsername().toLowerCase().equals(username.toLowerCase()) && user.equalsPassword(password)){
					instance.users.put(user, true);
					return user;
				}
			}
		}
		return null;
	}
	
	public static synchronized void userLogout(User user){
		instance.users.put(user, false);
	}
	
	public static synchronized User getUserOfPlayer(Player player){
		for(User user : UserManagement.getUsers()){
			if(user != null){
				if(user.getPlayer() == player){
					return user;
				}
			}
		}
		return null;
	}
	
	public static synchronized Set<User> getUsers(){
		instance.users.remove(null);
		return Collections.unmodifiableSet(instance.users.keySet());
	}
	
	public static synchronized User addUser(String username, String password){
		// Check if there isn't already a user with the given username; username must be unique
		for(User user : instance.users.keySet()){
			if(user != null){
				if(user.getUsername() == username){
					return null;
				}
			}
		}
		
		// Return true if the user was created and logged in
		instance.users.put(new User(username, password), false);
		return userLogin(username, password);
	}
	
	public static synchronized Set<RatingWrapper> getUserRatings(){
		Set<RatingWrapper> ratings = new HashSet<>();
		for(User user : instance.users.keySet()){
			if(user != null){
				ratings.add(new RatingWrapper(user.getUsername(), user.getRating()));
			}
		}
		
		return ratings;
	}
	
	public static synchronized boolean isUserLoggedIn(User user){
		return instance.users.get(user);
	}
	
}
