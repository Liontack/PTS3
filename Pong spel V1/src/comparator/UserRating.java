package comparator;

import java.util.Comparator;

public class UserRating implements Comparator<RatingWrapper>{
	
	@Override
	public int compare(RatingWrapper o1, RatingWrapper o2){
		int ratingCompared = Double.compare(o2.getRating(), o1.getRating());
		if(ratingCompared == 0){
			return o1.getUsername().compareTo(o2.getUsername());
		}else{
			return ratingCompared;
		}
	}
	
}
