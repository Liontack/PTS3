package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class BarricadeTest{
	
	@Test
	public void testAverateRating(){
		Barricade barricadeSmall = new Barricade(new Point(20, 20), 0);
		assertEquals(barricadeSmall.getDiameter(), Barricade.DIAMETER_PERCENT_MIN);
		Barricade barricadeBig = new Barricade(new Point(20, 20), 40);
		assertEquals(barricadeBig.getDiameter(), Barricade.DIAMETER_PERCENT_MAX);
		Barricade barricade = new Barricade(new Point(20, 20), 18);
		assertTrue(barricade.getDiameter() < Barricade.DIAMETER_PERCENT_MAX - Barricade.DIAMETER_PERCENT_MIN);
	}
	
	@Test
	public void testHit(){
		Barricade barricade = new Barricade(new Point(20, 20), 0);
		Puck puck1 = new Puck(0, new Point(20 + (barricade.getDiameter() / 2), 20), 0);
		Puck puck2 = new Puck(0, new Point(20 + (barricade.getDiameter() / 2) + puck1.getDiameter(), 20), 0);
		Puck puck3 = new Puck(0, new Point(20, 20 + (barricade.getDiameter() / 2) + puck1.getDiameter()), 0);
		
		assertTrue(barricade.hit(puck1));
		assertFalse(barricade.hit(puck2));
		assertFalse(barricade.hit(puck3));
	}
	
}
