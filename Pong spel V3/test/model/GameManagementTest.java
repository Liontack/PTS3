package model;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Test;

public class GameManagementTest{
	
	@Test
	public void testJoinGame(){
		try{
			int a = UserManagement.getInstance().registerUser("a", "a");
			int b = UserManagement.getInstance().registerUser("b", "b");
			int c = UserManagement.getInstance().registerUser("c", "c");
			int d = UserManagement.getInstance().registerUser("d", "d");
			
			// Test when players can join a game
			assertTrue(GameManagement.getInstance().joinGame(a) != 0);
			assertTrue(GameManagement.getInstance().joinGame(b) != 0);
			assertTrue(GameManagement.getInstance().joinGame(c) != 0);
			assertTrue(GameManagement.getInstance().joinGame(d) != 0);
			assertTrue(GameManagement.getInstance().joinGame(a) == 0);
			assertTrue(GameManagement.getInstance().startGame(b));
		}catch(RemoteException exception){
			fail("Remote Exception was thrown");
		}
	}
	
}
