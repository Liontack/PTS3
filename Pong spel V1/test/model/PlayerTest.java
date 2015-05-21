package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest{
	
	@Test
	public void testProperties(){
		Bat bat = new Bat(0);
		Player.Colour colour = Player.Colour.values()[0];
		boolean isAI = true;
		Player player = new Player(colour, bat, isAI);
		
		assertEquals(player.getBat(), bat);
		assertEquals(player.getColour(), colour);
		assertEquals(player.isAI(), isAI);
		assertNull(player.getPowerUp(0));
		
	}
	
	@Test
	public void testPowerUps(){
		Player player = new Player(Player.Colour.values()[0], new Bat(0), false);
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
