package game;

public class Prince extends Ally {
	
	//class variables: none
	
	//class constructors
	public Prince(String name, int maxhp, int hit, int atk, int def, int mov) {
		//prince can only move two tiles per turn
		super(name,maxhp,hit,atk,def,mov);
	}
}