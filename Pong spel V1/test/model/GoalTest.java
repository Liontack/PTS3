package model;

import static org.junit.Assert.*;

import java.awt.Point;

import model.Player.Colour;

import org.junit.Test;

public class GoalTest{
	
	@Test
	public void testyPerx(){
		Point p0_0 = new Point(0, 0);
		Point p0_8 = new Point(0, 8);
		Point p8_0 = new Point(8, 0);
		Point p4_5 = new Point(4, 5);
		Goal goalH = new Goal(p0_0, p8_0);
		Goal goalD1 = new Goal(p8_0, p0_8);
		Goal goalD2 = new Goal(p0_0, p4_5);
		Goal goalD3 = new Goal(p4_5, p0_8);
		
		assertEquals(goalH.gety_perx(), 0);
		assertEquals(goalD1.gety_perx(), -1);
		assertEquals(goalD2.gety_perx(), (5/4));
		assertEquals(goalD3.gety_perx(), -(3/4));
	}
	
	@Test
	public void testInGoal(){
		Point p0_0 = new Point(0, 0);
		Point p8_0 = new Point(8, 0);
		Point p8_8 = new Point(8, 8);
		Side sideH = new Side(Colour.RED, p0_0, p8_0);
		Side sideD = new Side(Colour.BLUE, p0_0, p8_8);
		
		Point p8_3 = new Point(8, 3);
		Puck puck_8Down = new Puck(270, p8_3, 0);
		assertFalse(sideH.getGoal().isInGoal(sideH, puck_8Down));
		puck_8Down.move();
		assertFalse(sideH.getGoal().isInGoal(sideH, puck_8Down));
		puck_8Down.move();
		assertFalse(sideH.getGoal().isInGoal(sideH, puck_8Down));
		puck_8Down.move();
		puck_8Down = null;
		
		Point p4_4 = new Point(4, 4);
		Puck puck_4Down = new Puck(270, p4_4, 0);
		assertFalse(sideH.getGoal().isInGoal(sideH, puck_4Down));
		puck_4Down.move();
		assertFalse(sideH.getGoal().isInGoal(sideH, puck_4Down));
		puck_4Down.move();
		assertTrue(sideH.getGoal().isInGoal(sideH, puck_4Down));
		
		Point p4_0 = new Point(4, 0);
		Puck puck_4Up = new Puck(90, p4_0, 0);
		assertFalse(sideD.getGoal().isInGoal(sideD, puck_4Up));
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up));
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up));
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up));
		puck_4Up.move();
		assertFalse(sideD.getGoal().isInGoal(sideD, puck_4Up));
		
	}
	
}
