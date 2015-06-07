package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PowerUpTest{
	
	@Test
	public void testKind(){
		// Test whether the power up kind is set correctly
		PowerUp powerUp = new PowerUp(PowerUp.Kind.PUCK_BOOST);
		assertSame(powerUp.getKind(), PowerUp.Kind.PUCK_BOOST);
		assertNotSame(powerUp.getKind(), PowerUp.Kind.UPSIDE_DOWN);
	}
	
}
