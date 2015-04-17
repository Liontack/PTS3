package model.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class UserManagement{
	
	private final static UserManagement instance = new UserManagement();
	
	private Set<User> users;
	
	private UserManagement(){
		fillUsersSet();
	}
	
	private void fillUsersSet(){
		//XXX Should fill users from backup file
		users = new HashSet<>();
		users.add(new User("Anjo", "a", 20));
		users.add(new User("Bart", "b", 21));
		users.add(new User("Cleo", "c"));
		users.add(new User("Dirk", "d", 32));
		users.add(new User("Echo", "e", 10));
	}
	
	
	
	public static synchronized User userLogin(String username, String password){
		for(User user : instance.users){
			if(user.username.toLowerCase().equals(username.toLowerCase()) && user.equalsPassword(password)){
				return user;
			}
		}
		return null;
	}
	
	public static synchronized boolean addUser(String username, String password){
		// Check if there isn't already a user with the given username; username must be unique
		for(User user : instance.users){
			if(user.username == username){
				return false;
			}
		}
		
		// Return true if the user was created
		int userAmount = instance.users.size();
		instance.users.add(new User(username, password));
		return (instance.users.size() == userAmount++);
	}
	
	public static synchronized Set<User> getUsers(){
		return Collections.unmodifiableSet(instance.users);
	}
	
}
