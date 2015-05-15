package comparator;

import java.util.Comparator;

import model.User;

public class UserRating implements Comparator<User>{
	
	@Override
	public int compare(User o1, User o2){
		return Integer.compare(o2.rating(), o1.rating());
	}
	
}
