package model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.*;

public class SideTest{
	
	@Test
	public void testColour(){
		// Test if the colour is set and kept correctly
		Side s = new Side(Player.Colour.values()[0], new Point(0, 0), new Point(1, 1));
		assertEquals(Player.Colour.values()[0], s.getColour());
	}
	
	@Test
	public void testAboveLine(){
		// Test whether an point or puck is above a side
		Point zero = new Point(0, 0);
		Point five = new Point(Side.LENGTH, 0);
		Point ten = new Point(Side.LENGTH * 2, -(Side.LENGTH * 2));
		
		Point three_one = new Point(3, -1);
		Puck p3_1 = new Puck(0, three_one, 0);
		Puck p10 = new Puck(0, ten, 0);
		Puck p5 = new Puck(0, five, 0);
		
		// Test for an horizontal side
		Side horizontal = new Side(Player.Colour.values()[0], zero, five);
		assertTrue(horizontal.isAboveLine(three_one));
		assertTrue(horizontal.isAboveLine(ten));
		assertFalse(horizontal.isAboveLine(zero));
		assertFalse(horizontal.isAboveLine(five));
		assertFalse(PuckState.IN_FIELD == horizontal.isAboveLine(p3_1));
		assertEquals(PuckState.IN_FIELD, horizontal.isAboveLine(p10));
		
		// Test for an diagonal side
		Side diagonal= new Side(Player.Colour.values()[1], zero, ten);
		assertFalse(diagonal.isAboveLine(three_one));
		assertFalse(diagonal.isAboveLine(ten));
		assertFalse(diagonal.isAboveLine(zero));
		assertFalse(diagonal.isAboveLine(five));
		assertFalse(PuckState.IN_FIELD == diagonal.isAboveLine(p3_1));
		assertTrue(PuckState.IN_FIELD == diagonal.isAboveLine(p5));
	}
	
}
