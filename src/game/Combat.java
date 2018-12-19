package game;

import java.util.Random;

public class Combat {
	
	//class constructor
	public Combat() {
		;
	}

	public static int calculateHit(int hit, int eva) {
		int trueHit = hit - eva;
		return trueHit;
	}
	
	public static int calculateDamage(int atk, int def) {
	int damage = atk - def;
	return damage;
	}
	
	protected static int generateRN() {
		Random rand = new Random();
		int rn = rand.nextInt(101);
		return rn;	
	}
	
	//Calculating the Hit threshold
	//The Hit threshold is an average of 2 randomly generated numbers
	// BL: Got this from: https://serenesforest.net/general/true-hit/
	protected static int calculateHitThreshold(int n1, int n2) {
		int h = (n1+n2)/2;
		return h;
	}
	
	//Hit + damage of Ally unit 
	public static void attacks(Unit attacker, Unit attackee) {
		int rn1 = generateRN();
		int rn2 = generateRN();
		int hitThreshold = calculateHitThreshold(rn1,rn2);
		int trueHit = calculateHit(attacker.getHit(),attackee.getEva());

		if (trueHit > hitThreshold) {

			int damage = calculateDamage(attacker.getAtk(),attackee.getDef());
			attackee.updateHp(damage);
		}
	}
	
	
	//Combat Calculations: Successful hit + damage
	public static void performCombat(Unit ally, Unit enemy, String turn) {
		
		if (turn.equals("Allies")) { 

			attacks(ally, enemy);
			if(enemy.getAlive()) { 
				//counterattack
				attacks(enemy,ally);
			}
		} else {
			
			attacks(enemy, ally);			
			if(ally.getAlive()) {
				//counterattack
				attacks(ally, enemy);
			}
		}
		
	}
}