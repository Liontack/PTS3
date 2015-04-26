package model;

public class PowerUp{

	public enum Kind{
		UPSIDE_DOWN,
		PUCK_BOOST
	}
	
	public static int DURATION = 5;
	
	
	
	public final Kind kind;
	
	public PowerUp(Kind kind){
		this.kind = kind;
	}
	
}
