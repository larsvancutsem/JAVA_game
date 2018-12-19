package game;

public class Enemy extends Unit {

	//class variables: none
	
	//class constructors
	public Enemy(String name, int maxhp, int hit, int atk, int def, int mov) {
		//enemy can only move two tiles per turn
		super(name,maxhp,hit,atk,def,mov);
	}
}