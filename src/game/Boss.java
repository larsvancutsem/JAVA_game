package game;

public class Boss extends Enemy {

	//class variables: none
	
	//class constructors
	public Boss(String name, int maxhp, int hit, int atk, int def) {
		//Boss can not move in any turn (he only can attack)
		super(name,maxhp,hit,atk,def,0);
	}
}