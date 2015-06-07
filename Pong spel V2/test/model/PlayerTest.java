package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest{
	
	@Test
	public void testProperties(){
		// Test the properties of a new player
		Bat bat = new Bat(0);
		Player.Colour colour = Player.Colour.values()[0];
		boolean isAI = true;
		Player player = new Player(colour, isAI);
		
		assertEquals(player.getColour(), colour);
		assertEquals(player.isAI(), isAI);
		assertNull(player.getPowerUp(0));
		
		assertNull(player.getBat());
		player.setBat(bat);
		assertEquals(player.getBat(), bat);
	}
	
	/**
	 * Tests for the powerUps property of a player.
	 * A player can't have less than 0 or more than 3 powerUps
	 * and can use only one powerUp at a time.
	 * The amount of powerUps is tested by accessing an index in the powerUp list,
	 * as the powerUps should stay as low as possible, i.e.
	 * if there are three powerUps and the one at index 0 is removed,
	 * then one goes to zero, two goes to one and two is the index which ends up empty.
	 */
	@Test
	public void testPowerUps(){
		// A test scenario with gaining and using power ups, and what the power up properties look like
		Player player = new Player(Player.Colour.values()[0], false);
		PowerUp boost1 = new PowerUp(PowerUp.Kind.PUCK_BOOST);
		PowerUp boost2 = new PowerUp(PowerUp.Kind.PUCK_BOOST);
		PowerUp updown = new PowerUp(PowerUp.Kind.UPSIDE_DOWN);
		
		assertNull(player.getPowerUp(0));
		player.addPowerUp(boost1);
		assertNotNull(player.getPowerUp(0));
		assertNull(player.getPowerUp(1));
		assertEquals(player.getPowerUps()[0], boost1);
		player.addPowerUp(updown);
		player.addPowerUp(boost2);
		assertNotNull(player.getPowerUp(2));
		assertEquals(player.getPowerUps()[0], boost1);
		assertEquals(player.getPowerUp(1), updown);
		assertEquals(player.getPowerUp(2), boost2);
		player.usePowerUp(1);
		assertNotNull(player.getPowerUp(1));
		assertNull(player.getPowerUp(2));
		assertEquals(player.getPowerUps()[0], boost1);
		assertEquals(player.getPowerUps()[1], boost2);
		assertTrue(player.addPowerUp(boost1));
		assertFalse(player.addPowerUp(updown));
		player.removePowerUp(0);
		player.removePowerUp(0);
		assertFalse(player.removePowerUp(2));
		player.removePowerUp(0);
		assertFalse(player.removePowerUp(0));
		assertNull(player.getPowerUp(0));
	}
	
}
