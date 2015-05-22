package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class GameFieldTest{
	
	@Test
	public void testProperties(){
		GameField gamefield = new GameField(new Game(), 0);
		assertNotNull(gamefield.getPuck());
		assertEquals(gamefield.getBarricades().size(), 0);
		assertNotNull(gamefield.getSide(Player.Colour.values()[0]));
		assertNotNull(gamefield.getSide(Player.Colour.values()[Player.Colour.values().length - 1]));
		
		gamefield = new GameField(new Game(), 40);
		assertEquals(gamefield.getBarricades().size(), 5);
		
		gamefield = new GameField(new Game(), 19);
		assertEquals(gamefield.getBarricades().size(), 2);
		
		gamefield = new GameField(new Game(), 21);
		assertEquals(gamefield.getBarricades().size(), 3);
	}
	
	@Test
	public void testUpdaterThread(){
		GameField gamefield = new GameField(new Game(), 0);
		Point puckPosition = gamefield.getPuck().getPosition();
		
		gamefield.startUpdaterThread();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
			fail("Test did not run as expected");
		}
		gamefield.stopUpdaterThread();
		
		assertTrue(gamefield.getPuck().getPosition() != puckPosition);
	}
	
}
