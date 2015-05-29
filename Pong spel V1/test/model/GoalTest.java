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
		
		assertEquals(goalH.gety_perx(), 0, 1.000);
		assertEquals(goalD1.gety_perx(), -1, 1.000);
		assertEquals(goalD2.gety_perx(), (5/4), 1.000);
		assertEquals(goalD3.gety_perx(), -(3/4), 1.000);
	}
	
	@Test
	public void testInGoal(){
		Point p0_0 = new Point(0, 0);
		Point p1_0 = new Point(100, 0);
		Point p1_1 = new Point(100, 100);
		Side sideH = new Side(Colour.RED, p0_0, p1_0);
		Side sideD = new Side(Colour.BLUE, p0_0, p1_1);
		
		Point pNotAboveGoal = new Point(105, 4);
		Puck puckDown_notInGoal = new Puck(270, pNotAboveGoal, 20);
		assertFalse(sideH.getGoal().isInGoal(sideH, puckDown_notInGoal) == PuckState.IN_GOAL);
		puckDown_notInGoal.move();
		assertFalse(sideH.getGoal().isInGoal(sideH, puckDown_notInGoal) == PuckState.IN_GOAL);
		puckDown_notInGoal.move();
		assertFalse(sideH.getGoal().isInGoal(sideH, puckDown_notInGoal) == PuckState.IN_GOAL);
		puckDown_notInGoal.move();
		puckDown_notInGoal = null;
		
		Point pAboveGoal = new Point(65, 4);
		Puck puckDown_inGoal = new Puck(280, pAboveGoal, 20);
		assertTrue(sideH.getGoal().isInGoal(sideH, puckDown_inGoal) == PuckState.IN_GOAL);
		puckDown_inGoal.move();
		assertTrue(sideH.getGoal().isInGoal(sideH, puckDown_inGoal) == PuckState.IN_GOAL);
		puckDown_inGoal.move();
		assertTrue(sideH.getGoal().isInGoal(sideH, puckDown_inGoal) == PuckState.IN_GOAL);
		puckDown_inGoal = null;
		
		Point p4_0 = new Point(35, 30);
		Puck puck_4Up = new Puck(90, p4_0, 20);
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up) == PuckState.IN_GOAL);
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up) == PuckState.IN_GOAL);
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up) == PuckState.IN_GOAL);
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up) == PuckState.IN_GOAL);
		puck_4Up.move();
		assertTrue(sideD.getGoal().isInGoal(sideD, puck_4Up) == PuckState.IN_GOAL);
		
	}
	
}
