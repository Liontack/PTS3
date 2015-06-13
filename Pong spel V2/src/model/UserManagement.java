package model;

import java.io.File;
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

import remote.IUnsecured;

import comparator.RatingWrapper;



public class UserManagement extends UnicastRemoteObject implements IUnsecured{
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
			}catch(RemoteException exception){}
		}
		return UserManagement.instance;
	}
	
	
	
	public void fillUsersSet(){
		this.users = new HashMap<>();
		File file = new File("users.data");
		if(file.exists()){
			try{
				ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
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
	}
	
	public static void saveUsersSet(){
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.data"));
			for(User user : getInstance().users.keySet()){
				out.writeObject(user);
			}
			out.close();
		}catch(IOException exception){}
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
		instance.users.put(new User(username, password), false);
		
		// Also save the new user
		UserManagement.saveUsersSet();
		
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
	
}
