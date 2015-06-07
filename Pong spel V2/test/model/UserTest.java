package model;

import static org.junit.Assert.*;
import org.junit.*;

public class UserTest{
	
	@Test
	public void testFields(){
		// Test the username and password fields
		User anjo = new User("anjo", "wachtwoord");
		assertEquals("anjo", anjo.getUsername());
		assertTrue(anjo.equalsPassword("wachtwoord"));
	}
	
	@Test
	public void testPlayer(){
		// Test the player property
		User anjo = new User("anjo", "wachtwoord");
		assertNull(anjo.getPlayer());
		anjo.setPlayer(new Player(Player.Colour.RED, false));
		assertNotNull(anjo.getPlayer());
		anjo.clearPlayer();
		assertNull(anjo.getPlayer());
	}
	
	@Test
	public void testRating(){
		// Test the rating property with adding points
		User anjo = new User("anjo", "ww", new int[]{ 20, 20, 21 });
		assertTrue(15.0 == anjo.getRating());
		anjo.addNewRecentPoints(22);
		assertTrue(15.0 == anjo.getRating());
		anjo.addNewRecentPoints(25);
		assertTrue(22.3 == anjo.getRating());
		anjo.addNewRecentPoints(10);
		assertTrue(18.4 == anjo.getRating());
	}
	
}
