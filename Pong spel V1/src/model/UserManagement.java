package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import comparator.RatingWrapper;



public class UserManagement{
	
	private final static UserManagement instance = new UserManagement();
	
	private Map<User, Boolean> users;
	
	
	
	private UserManagement(){
		fillUsersSet();
	}
	
	//XXX Should fill users from backup file
	private void fillUsersSet(){
		users = new HashMap<>();
		users.put(new User("Anjo", "a", new int[]{20, 10, 20, 10, 12}), false);
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
	
	public static synchronized User getUserOfPlayer(Player player){
		for(User user : UserManagement.getUsers()){
			if(user.getPlayer() == player){
				return user;
			}
		}
		return null;
	}
	
	public static synchronized Set<User> getUsers(){
		return Collections.unmodifiableSet(instance.users.keySet());
	}
	
	public static synchronized User addUser(String username, String password){
		// Check if there isn't already a user with the given username; username must be unique
		for(User user : instance.users.keySet()){
			if(user.getUsername() == username){
				return null;
			}
		}
		
		// Return true if the user was created and logged in
		instance.users.put(new User(username, password), false);
		return userLogin(username, password);
	}
	
	public static synchronized Set<RatingWrapper> getUserRatings(){
		Set<RatingWrapper> ratings = new HashSet<>();
		for(User user : instance.users.keySet()){
			ratings.add(new RatingWrapper(user.getUsername(), user.getRating()));
		}
		
		return ratings;
	}
	
	public static synchronized boolean isUserLoggedIn(User user){
		return instance.users.get(user);
	}
	
}
