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
	public void testRandomPosition(){
		int tests = 99;
		GameField gamefield = new GameField(new Game(), 0);
		while(tests-- > 0){
			Point p = gamefield.getRandomPosition();
			assertTrue(p.toString() + " was not above red line", gamefield.getSide(Player.Colour.RED).isAboveLine(p));
			assertFalse(p.toString() + " was not under blue line", gamefield.getSide(Player.Colour.BLUE).isAboveLine(p));
			assertFalse(p.toString() + " was not under green line", gamefield.getSide(Player.Colour.GREEN).isAboveLine(p));
		}
	}
	
	@Test
	public void testUpdaterThread(){
		GameField gamefield = new GameField(new Game(), 0);
		Point puckPosition = gamefield.getPuck().getPosition();
		
		gamefield.startUpdaterThread();
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
			fail("Test did not run as expected");
		}
		gamefield.stopUpdaterThread();
		
		assertTrue(gamefield.getPuck().getPosition() != puckPosition);
	}
	
}
