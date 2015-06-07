package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class BatTest{
	
	@Test
	public void testMoving(){
		// Test if the bat moves to the correct direction
		Bat bat = new Bat(100);
		int lastPos = bat.getPositionInGoal();
		bat.moveLeft();
		assertTrue(bat.getPositionInGoal() <= lastPos);
		lastPos = bat.getPositionInGoal();
		bat.moveLeft();
		assertTrue(bat.getPositionInGoal() <= lastPos);
		lastPos = bat.getPositionInGoal();
		bat.moveRight();
		assertTrue(bat.getPositionInGoal() >= lastPos);
		lastPos = bat.getPositionInGoal();
	}
	
	@Test
	public void testHitHorizontal(){
		// Test if an puck hits the bat in a horizontal goal
		Point goalA = new Point(0, 0);
		Point goalB = new Point(Side.LENGTH, 0);
		Goal goal = new Goal(goalA, goalB);
		Bat bat = goal.getBat();
		Puck puckHit = new Puck(270, new Point(Side.LENGTH / 2, 1), 0);
		Puck puckNoHit = new Puck(270, new Point(Side.LENGTH * 8 / 10, 0), 0);
		assertTrue(bat.hit(puckHit, goalA, goalB, Player.Colour.RED));
		assertFalse(bat.hit(puckNoHit, goalA, goalB, Player.Colour.RED));
	}
	
	@Test
	public void testHitDiagonal(){
		// Test if an puck hits the bat in a diagonal goal
		Point goalA = new Point(0, 0);
		Point goalB = new Point(Side.LENGTH, Side.LENGTH);
		Goal goal = new Goal(goalB, goalA);
		Bat bat = goal.getBat();
		Puck puckHit = new Puck(270, new Point(Side.LENGTH / 2, Side.LENGTH * 51 / 100), 0);
		Puck puckNoHit = new Puck(270, new Point(Side.LENGTH * 6 / 10, Side.LENGTH * 49 / 100), 0);
		assertTrue(bat.hit(puckHit, goalA, goalB, Player.Colour.BLUE));
		assertFalse(bat.hit(puckNoHit, goalA, goalB, Player.Colour.BLUE));
	}
	
}
