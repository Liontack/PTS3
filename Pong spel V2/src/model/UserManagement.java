package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import comparator.RatingWrapper;



public class UserManagement{
	private static UserManagement instance;
	
	private Map<User, Boolean> users;
	
	
	
	private UserManagement(){
		fillUsersSet();
	}
	
	public static UserManagement getInstance(){
		if(UserManagement.instance == null){
			instance = new UserManagement();
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
			for(User user : getInstance().users.keySet()){
				out.writeObject(user);
			}
			out.close();
		}catch(IOException exception){}
	}
	
	
	
	public static synchronized User userLogin(String username, String password){
		// Login if the username and password are correct and the user wasn't yet logged in
		for(User user : getInstance().users.keySet()){
			if(user != null){
				if(getInstance().users.get(user) != true && user.getUsername().toLowerCase().equals(username.toLowerCase()) && user.equalsPassword(password)){
					getInstance().users.put(user, true);
					return user;
				}
			}
		}
		return null;
	}
	
	public static synchronized void userLogout(User user){
		getInstance().users.put(user, false);
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
		getInstance().users.remove(null);
		return Collections.unmodifiableSet(getInstance().users.keySet());
	}
	
	public static synchronized User addUser(String username, String password){
		// Check if there isn't already a user with the given username; username must be unique
		for(User user : getInstance().users.keySet()){
			if(user != null){
				if(user.getUsername().equals(username)){
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
		for(User user : UserManagement.getInstance().users.keySet()){
			if(user != null){
				ratings.add(new RatingWrapper(user.getUsername(), user.getRating()));
			}
		}
		
		return ratings;
	}
	
	public static synchronized boolean isUserLoggedIn(User user){
		return getInstance().users.get(user);
	}
	
}
