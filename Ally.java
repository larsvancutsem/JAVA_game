package Game;

public class Ally extends Unit {
	
	public Ally(String name, int maxhp, int hit, int atk, int def) {
		//ally can only move two tiles per turn
		super(name,maxhp,hit,atk,def,2);
	}
	
}