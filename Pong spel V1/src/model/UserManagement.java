package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class UserManagement{
	
	private final static UserManagement instance = new UserManagement();
	
	private Map<User, Boolean> users;
	
	
	
	private UserManagement(){
		fillUsersSet();
	}
	
	//XXX Should fill users from backup file
	private void fillUsersSet(){
		users = new HashMap<>();
		users.put(new User("Anjo", "a", new int[]{20, 20, 20, 20, 20}), false);
		users.put(new User("Bart", "b", new int[]{21, 21, 21, 21, 21}), false);
		users.put(new User("Cleo", "c"), false);
		users.put(new User("Dirk", "d", new int[]{32, 32, 32, 32, 32}), false);
		users.put(new User("Echo", "e", new int[]{10, 10, 10, 10, 10}), false);
	}
	
	
	
	public static synchronized User userLogin(String username, String password){
		// Login if the username and password are correct and the user wasn't yet logged in
		for(User user : instance.users.keySet()){
			if(instance.users.get(user) != true && user.getUsername().toLowerCase().equals(username.toLowerCase()) && user.equalsPassword(password)){
				instance.users.put(user, true);
				return user;
			}
		}
		return null;
	}
	
	public static synchronized void userLogout(User user){
		instance.users.put(user, false);
	}

	public static synchronized Set<User> getUsers(){
		return Collections.unmodifiableSet(instance.users.keySet());
	}
	
	public static synchronized boolean addUser(String username, String password){
		// Check if there isn't already a user with the given username; username must be unique
		for(User user : instance.users.keySet()){
			if(user.getUsername() == username){
				return false;
			}
		}
		
		// Return true if the user was created
		int userAmount = instance.users.size();
		instance.users.put(new User(username, password), true);
		return (instance.users.size() == userAmount++);
	}
	
	public static synchronized Map<String, Double> getUserRatings(){
		Map<String, Double> ratings = new HashMap<>();
		for(User user : instance.users.keySet()){
			ratings.put(user.getUsername(), user.getRating());
		}
		
		return ratings;
	}
	
	public static synchronized boolean isUserLoggedIn(User user){
		return instance.users.get(user);
	}
	
}
