package Game;

public class Enemy extends Unit {

	public Enemy(String name, int maxhp, int hit, int atk, int def) {
		//enemy can only move two tiles per turn
		super(name,maxhp,hit,atk,def,2);
	}

}

