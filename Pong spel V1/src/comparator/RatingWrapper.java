package comparator;

public class RatingWrapper{
	
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
