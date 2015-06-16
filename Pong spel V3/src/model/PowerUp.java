package model;

public class PowerUp{

	public enum Kind{
		UPSIDE_DOWN,
		PUCK_BOOST
	}
	
	public static int DURATION = 5;
	
	
	
	private final Kind kind;
	
	public PowerUp(Kind kind){
		this.kind = kind;
	}
	
	public Kind getKind(){
		return this.kind;
	}
	
	//By the use of a power up, nothing happens; will not be implemented
	public void use(){
		switch(this.kind){
			case UPSIDE_DOWN:
				break;
			case PUCK_BOOST:
				break;
		}
	}
	
}
