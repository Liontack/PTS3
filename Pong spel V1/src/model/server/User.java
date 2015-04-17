package model.server;

import model.Player;

public class User{
	
	private static final int INITIAL_RATING = 15;
	
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
	
	User(String username, String password){
		this.username = username;
		this.password = password;
		this.rating = INITIAL_RATING;
	}
	
	public boolean equalsPassword(String password){
		return this.password.equals(password);
	}
	
	public int rating(){
		return rating;
	}
	
}
