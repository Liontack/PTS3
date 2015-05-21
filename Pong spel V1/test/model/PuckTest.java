package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class PuckTest{
	
	@Test
	public void testProperties(){
		int angle1 = 90;
		Point p1 = new Point(5, 15);
		int averageRating1 = 20;
		Puck puck1 = new Puck(angle1, p1, averageRating1);
		assertEquals(averageRating1 * Puck.DEFAULT_VELOCITY, puck1.getVelocity());
		assertEquals(p1, puck1.getPosition());
		assertEquals(angle1, puck1.getAngle());
		assertEquals(Puck.DIAMETER_PERCENT_OF_SIDE_LENGTH * Side.LENGTH / 100, puck1.getDiameter());
		
		
		
		puck1.setAngle(359);
		assertEquals(puck1.getAngle(), 359);
		
		try{
			puck1.setAngle(360);
			fail("Puck should not be able to be set to 360");
		}catch(IllegalArgumentException ex){}
		try{
			puck1.setAngle(-1);
			fail("Puck should not be able to be set to 360");
		}catch(IllegalArgumentException ex){}
		
		assertEquals(puck1.getAngle(), 359);
		puck1.setAngle(0);
		assertEquals(puck1.getAngle(), 0);
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
		assertEquals(puck1.getPosition(), new Point(12, 10));
		puck1.move();
		assertEquals(puck1.getPosition(), new Point(14, 10));
		puck1.setAngle(angle90);
		puck1.move();
		assertEquals(puck1.getPosition(), new Point(14, 12));
		// XXX More tests for Puck.move
	}
}
