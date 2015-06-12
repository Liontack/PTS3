package comparator;

import java.io.Serializable;

public class RatingWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private double rating;
	
	public RatingWrapper(String username, double rating){
		this.username = username;
		this.rating = rating;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public double getRating(){
		return this.rating;
	}
	
}
