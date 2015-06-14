package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;


public class GameFieldTest{
	
	@Test
	public void testProperties(){
		// Test if a new gamefield has some properties
		GameField gamefield = new GameField(new Game(false), 0);
		assertNotNull(gamefield.getPuck());
		assertEquals(gamefield.getBarricades().size(), 0);
		assertNotNull(gamefield.getSide(Player.Colour.values()[0]));
		assertNotNull(gamefield.getSide(Player.Colour.values()[Player.Colour.values().length - 1]));
		
		// Test whether the average rating influences the number of barricades correctly
		gamefield = new GameField(new Game(false), 40);
		assertEquals(gamefield.getBarricades().size(), 5);
		
		gamefield = new GameField(new Game(false), 19);
		assertEquals(gamefield.getBarricades().size(), 2);
		
		gamefield = new GameField(new Game(false), 21);
		assertEquals(gamefield.getBarricades().size(), 3);
	}
	
	@Test
	public void testRandomPosition(){
		// Test if a random point is within the gamefield
		int tests = 99;
		GameField gamefield = new GameField(new Game(false), 0);
		while(tests-- > 0){
			Point p = gamefield.getRandomPosition();
			assertTrue(p.toString() + " was not above red line", gamefield.getSide(Player.Colour.RED).isAboveLine(p));
			assertFalse(p.toString() + " was not under blue line", gamefield.getSide(Player.Colour.BLUE).isAboveLine(p));
			assertFalse(p.toString() + " was not under green line", gamefield.getSide(Player.Colour.GREEN).isAboveLine(p));
		}
	}
	
	@Test
	public void testUpdaterThread(){
		// Test whether the updater thread influences the position of the puck
		GameField gamefield = new GameField(new Game(false), 0);
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
