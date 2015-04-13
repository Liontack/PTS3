package model.server;

import model.Player;

public class User{
	
	public final String username; // Unique
	private final String password;
	
	private int rating;
	
	private Player player;
	
	// XXX creating user with password like this, doesn't feel save
	User(String username, String password, int rating){
		this.username = username;
		this.password = password;
		this.rating = rating;
	}
	
	public boolean equalsPassword(String password){
		return this.password.equals(password);
	}
	
	public int rating(){
		return rating;
	}
	
}
