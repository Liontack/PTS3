package model;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.*;

public class UserManagementTest{
	
	@Test
	public void testAdd(){
		try{
			// Test the registration of users
			int userAmount = UserManagement.getUsers().size();
			assertTrue(UserManagement.getInstance().registerUser("Programmer", "root") != 0);
			assertEquals(++userAmount, UserManagement.getUsers().size());
			assertTrue(UserManagement.getInstance().registerUser("Programmer", "also_root") == 0);
			assertEquals(userAmount, UserManagement.getUsers().size());
			assertTrue(UserManagement.getInstance().registerUser("Other programmer", "root") != 0);
			assertEquals(++userAmount, UserManagement.getUsers().size());
		}catch(RemoteException exception){
			fail("Remote Exception was thrown");
		}
	}
	
	@Test
	public void testLogin(){
		try{
			// Test the login of users
			assertTrue(UserManagement.getInstance().userLogin("Person a", "a") == 0);
			int a = UserManagement.getInstance().registerUser("Person a", "a");
			assertTrue(UserManagement.getInstance().userLogin("Person a", "a") == 0);
			assertTrue(UserManagement.isUserLoggedIn(UserManagement.getUserByID(a)));
			UserManagement.getInstance().userLogout(a);
			assertFalse(UserManagement.isUserLoggedIn(UserManagement.getUserByID(a)));
			assertTrue(UserManagement.getInstance().userLogin("Person a", "b") == 0);
			assertTrue(UserManagement.getInstance().userLogin("person a", "a") != 0);
			assertTrue(UserManagement.getInstance().userLogin("Person a", "a") == 0);
			assertTrue(UserManagement.isUserLoggedIn(UserManagement.getUserByID(a)));
		}catch(RemoteException exception){
			fail("Remote Exception was thrown");
		}
	}
	
	@Test
	public void testRatings(){
		try{
			// Test if each user is producing an RatingWrapper
			assertEquals(UserManagement.getUsers().size(), UserManagement.getInstance().getUserRatings().size());
			UserManagement.getInstance().registerUser("test person for ratings", "r");
			assertEquals(UserManagement.getUsers().size(), UserManagement.getInstance().getUserRatings().size());
		}catch(RemoteException exception){
			fail("Remote Exception was thrown");
		}
	}
	
}
