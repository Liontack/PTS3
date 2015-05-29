package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class PuckTest{
	
	@Test
	public void testProperties(){
		int angle1 = 90;
		Point p1 = new Point(5, 15);
		Puck puck1 = new Puck(angle1, p1, 20);
		assertEquals(p1, puck1.getPosition());
		assertEquals(angle1, puck1.getAngle(), 1.000);
		assertEquals(Puck.DIAMETER_PERCENT_OF_SIDE_LENGTH * Side.LENGTH / 100, Puck.getDiameter());
		
		
		
		puck1.setAngle(359);
		assertEquals(puck1.getAngle(), 359, 1.000);
		
		try{
			puck1.setAngle(360);
			fail("Puck should not be able to be set to 360");
		}catch(IllegalArgumentException ex){}
		try{
			puck1.setAngle(-1);
			fail("Puck should not be able to be set to 360");
		}catch(IllegalArgumentException ex){}
		
		assertEquals(puck1.getAngle(), 359, 1.000);
		puck1.setAngle(0);
		assertEquals(puck1.getAngle(), 0, 1.000);
	}
	
	@Test
	public void testVelocity(){
		Point zero = new Point(0, 0);
		Puck puckSlow = new Puck(0, zero, 0);
		Puck puckAverage = new Puck(0, zero, 20);
		Puck puckFast = new Puck(0, zero, 40);
		assertTrue(puckSlow.getVelocity() < Puck.DEFAULT_VELOCITY);
		assertTrue(puckAverage.getVelocity() == Puck.DEFAULT_VELOCITY);
		assertTrue(puckFast.getVelocity() > Puck.DEFAULT_VELOCITY && puckFast.getVelocity() <= Puck.DEFAULT_VELOCITY * 2);
	}
	
	@Test
	public void testMove(){
		int angle0 = 0;
		int angle90 = 90;
		Point startPoint = new Point(10, 10);
		int averageRating20 = 20;
		Puck puck1 = new Puck(angle0, startPoint, averageRating20);
		
		puck1.move();
		assertTrue(puck1.getPosition().distance(startPoint) == puck1.getVelocity());
		assertEquals(puck1.getPosition(), new Point(10 + (1 * Puck.DEFAULT_VELOCITY), 10));
		puck1.move();
		assertEquals(puck1.getPosition(), new Point(10 + (2 * Puck.DEFAULT_VELOCITY), 10));
		puck1.setAngle(angle90);
		puck1.move();
		assertEquals(puck1.getPosition(), new Point(10 + (2 * Puck.DEFAULT_VELOCITY), 10 + (1 * Puck.DEFAULT_VELOCITY)));
	}
}
