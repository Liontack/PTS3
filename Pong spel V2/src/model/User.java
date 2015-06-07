package model;

import java.io.Serializable;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


@SuppressWarnings("serial")
public class User implements Serializable{
	private static final double INITIAL_RATING = 15.0;
	
	private final String username; // Unique
	private final String password;
	private int[] mostRecentPoints = new int[5];// index 0 contains the most recent points, domain is [0;40]
	
	private transient Player player;
	
	
	
	User(String username, String password, int[] mostRecentPoints){
		this(username, password);
		for(int i = 0; i < Math.min(mostRecentPoints.length, this.mostRecentPoints.length); i++){
			this.mostRecentPoints[i] = mostRecentPoints[i];
		}
	}
	
	User(String username, String password){
		this.username = username;
		this.password = User.encrypt(password);
		Arrays.fill(this.mostRecentPoints, -1);
	}
	
	
	
	
	public String getUsername(){
		return this.username;
	}
	
	public boolean equalsPassword(String password){
		return this.password.equals(User.encrypt(password));
	}
	
	/**
	 * The rating of this player, based on his last 5 points
	 * @return this players' rating within [0;40]
	 */
	public double getRating(){
		int rating = 0;
		for(int i = 0; i < this.mostRecentPoints.length; i++){
			if(this.mostRecentPoints[i] == -1){
				return INITIAL_RATING;
			}
			rating += (5 - i) * this.mostRecentPoints[i];
		}
		return (double)Math.round((double)rating / 1.5) / 10.0;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public void addNewRecentPoints(int points){
		if(points > 40 || points < 0){
			return;
		}
		
		// Move all points one up
		for(int i = this.mostRecentPoints.length - 1; i > 0; i--){
			this.mostRecentPoints[i] = this.mostRecentPoints[i - 1];
		}
		// And place the new points at the front
		this.mostRecentPoints[0] = points;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public void clearPlayer(){
		this.player = null;
	}
	
	
	
	private static final String key = "Bar12345Bar12345";
	private static String encrypt(String string){
		try{
			Key cipherKey = new SecretKeySpec(User.key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, cipherKey);
			return new String(cipher.doFinal(string.getBytes()));
		}catch(Exception exception){
			System.err.println("The string " + string + " was not encrypted.");
			return string;
		}
	}
	
}
