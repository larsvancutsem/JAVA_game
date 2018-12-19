package game;

public class Ally extends Unit {
	
	//class variables: none
	
	//class constructors
	public Ally(String name, int maxhp, int hit, int atk, int def, int mov) {
		//ally can only move two tiles per turn
		super(name,maxhp,hit,atk,def,mov);
	}
}