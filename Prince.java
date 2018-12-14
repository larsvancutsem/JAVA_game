package Game;

public class Prince extends Unit {

	public Prince(String name, int maxhp, int hit, int atk, int def) {
		//prince can only move two tiles per turn
		super(name,maxhp,hit,atk,def,2);
	}
}
