package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameTest{
	
	@Test
	public void test(){
		Game game = new Game();
		
		assertNull(game.getGameField());
		assertTrue(game.getPlayers().size() == 0);
		assertTrue(game.getCurrentRound() == 0);
		assertNull(game.getScorer());
		assertFalse(game.isReadyToPlay());
		assertFalse(game.startGame());
		
		game.addPlayer(true);
		
		assertTrue(game.getPlayers().size() == 1);
		assertNull(game.getScorer());
		assertFalse(game.isReadyToPlay());
		assertFalse(game.startGame());
		
		Player player2 = game.addPlayer(false);
		
		assertTrue(game.getPlayers().size() == 2);
		
		Player player3 = game.addPlayer(false);
		
		assertTrue(game.getPlayers().size() == 3);
		assertTrue(game.isReadyToPlay());

		game.removePlayer(player2);
		
		assertTrue(game.getPlayers().size() == 2);
		assertFalse(game.isReadyToPlay());
		assertFalse(game.startGame());
		
		
		Player player4 = game.addPlayer(false);
		
		assertTrue(game.getPlayers().size() == 3);
		assertTrue(game.isReadyToPlay());
		assertTrue(game.startGame());
		
		assertNotNull(game.getGameField());
		assertNotNull(player3.getBat());
		
		
		
		assertEquals(player3.getPoints(), 20);
		assertEquals(player4.getPoints(), 20);
		
		game.setScorer(player3.getColour());
		
		assertSame(game.getScorer(), player3);
		
		try{
			game.increaseRound(player4.getColour());
		}catch(NullPointerException exception){}// Because the program did't set the feedbackpanel
		
		assertTrue(game.getCurrentRound() == 1);
		assertTrue(player3.getPoints() > 20);
		assertTrue(player4.getPoints() < 20);
		assertNull(game.getScorer());
		
	}
	
}
