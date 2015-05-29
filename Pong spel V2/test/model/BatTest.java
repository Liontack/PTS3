package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class BatTest{
	
	@Test
	public void testMoving(){
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
		Point goalA = new Point(0, 0);
		Point goalB = new Point(100, 0);
		Goal goal = new Goal(goalA, goalB);
		Bat bat = goal.getBat();
		Puck puckHit = new Puck(270, new Point(50, 1), 0);
		Puck puckNoHit = new Puck(270, new Point(80, 0), 0);
		assertTrue(bat.hit(puckHit, goalA, goalB, Player.Colour.RED));
		assertFalse(bat.hit(puckNoHit, goalA, goalB, Player.Colour.RED));
	}
	
	@Test
	public void testHitDiagonal(){
		Point goalA = new Point(0, 0);
		Point goalB = new Point(100, 100);
		Goal goal = new Goal(goalB, goalA);
		Bat bat = goal.getBat();
		Puck puckHit = new Puck(270, new Point(50, 51), 0);
		Puck puckNoHit = new Puck(270, new Point(60, 49), 0);
		assertTrue(bat.hit(puckHit, goalA, goalB, Player.Colour.BLUE));
		assertFalse(bat.hit(puckNoHit, goalA, goalB, Player.Colour.BLUE));
	}
	
}
