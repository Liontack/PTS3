package model;

import static org.junit.Assert.*;
import org.junit.*;

public class UserManagementTest{
	
	@Test
	public void testAdd(){
		int userAmount = UserManagement.getUsers().size();
		assertNotNull(UserManagement.addUser("Programmer", "root"));
		assertEquals(++userAmount, UserManagement.getUsers().size());
		assertNull(UserManagement.addUser("Programmer", "also_root"));
		assertEquals(userAmount, UserManagement.getUsers().size());
		assertNotNull(UserManagement.addUser("Other programmer", "root"));
		assertEquals(++userAmount, UserManagement.getUsers().size());
	}
	
	@Test
	public void testLogin(){
		assertNull(UserManagement.userLogin("Person a", "a"));
		User a = UserManagement.addUser("Person a", "a");
		assertNull(UserManagement.userLogin("Person a", "a"));
		assertTrue(UserManagement.isUserLoggedIn(a));
		UserManagement.userLogout(a);
		assertFalse(UserManagement.isUserLoggedIn(a));
		assertNull(UserManagement.userLogin("Person a", "b"));
		assertNotNull(UserManagement.userLogin("person a", "a"));
		assertNull(UserManagement.userLogin("Person a", "a"));
		assertTrue(UserManagement.isUserLoggedIn(a));
	}
	
	@Test
	public void testRatings(){
		assertEquals(UserManagement.getUsers().size(), UserManagement.getUserRatings().size());
		UserManagement.addUser("test person for ratings", "r");
		assertEquals(UserManagement.getUsers().size(), UserManagement.getUserRatings().size());
	}
	
}
