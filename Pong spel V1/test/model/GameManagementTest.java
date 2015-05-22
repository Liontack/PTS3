package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameManagementTest{
	
	@Test
	public void testJoinGame(){
		User a = UserManagement.addUser("a", "a");
		User b = UserManagement.addUser("b", "b");
		User c = UserManagement.addUser("c", "c");
		User d = UserManagement.addUser("d", "d");
		
		assertTrue(GameManagement.joinGame(a));
		assertTrue(GameManagement.joinGame(b));
		assertTrue(GameManagement.joinGame(c));
		assertTrue(GameManagement.joinGame(d));
		assertFalse(GameManagement.joinGame(a));
		assertTrue(GameManagement.startGame(b.getPlayer()));
	}
	
}
