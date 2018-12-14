package Game;

public abstract class Unit {
	//Unit variables
	//HP:/Health; HIT: Hit%; ATK: Attack; DEF: Defence; EVA: Evasion/dodge; MOV: Movement range
	//only Eva, Hp and unitAlive can be altered so only they have update functions
	private String unitName;
	private int unitHp;
	private int unitMaxHp;
	private int unitHit;
	private int unitAtk;
	private int unitDef;
	private int unitEva;
	private int unitMov;
	private boolean unitAlive;

	Unit(String NAME, int MAXHP, int HIT, int ATK, int DEF, int MOV) {
		setName(NAME);
		setMaxHp(MAXHP);
		setHit(HIT);
		setAtk(ATK);
		setDef(DEF);
		setMov(MOV);
		setAlive();
		setEva(0);
		setHp(MAXHP); //unit with full health is created
	}

	// get and set functions for variables
	public String getName() {
		return this.unitName;
	}
	public void setName(String name) {
		this.unitName = name;
	}
	
	public int getHp() {
		return this.unitHp;
	}
	public void setHp(int Hp) {
		this.unitHp = Hp;
	}
	
	public int getMaxHp() {
		return this.unitMaxHp;
	}
	public void setMaxHp(int maxhp) {
		this.unitMaxHp=maxhp;		
	}
	
	public int getHit() {
		return this.unitHit;
	}
	public void setHit(int hit) {
		this.unitHit=hit;		
	}
	
	public int getAtk() {
		return this.unitAtk;
	}
	public void setAtk(int atk) {
		this.unitAtk=atk;	
	}
	
	public int getDef() {
		return this.unitDef;
	}
	public void setDef(int def) {
		this.unitDef=def;	
	}	
	
	public int getEva() {
		return this.unitEva;
	}
	public void setEva(int eva) {
		this.unitEva=eva;	
	}
	
	public int getMov() {
		return this.unitMov;
	}
	public void setMov(int mov) {
		this.unitMov=mov;
	}
	
	public boolean getAlive() {
		return this.unitAlive;
	}
	public void setAlive() {
		this.unitAlive=true;
	}
	
	// update functions
	public void updateHp(int damage) {
		//make sure Hp will never drop below 0
		int currentHp=getHp();
		if(currentHp<=damage) {
			letUnitDie();
		}
		this.unitHp=Math.max(0,currentHp-damage);
	}
	public void updateEva(int extra) {
		int currentEva=getEva();
		setEva(currentEva+extra);
	}
	public void letUnitDie() {
		this.unitAlive=false;
	}
	
	// information parsers
	public String getInfo() {
		return this.getName() +"   HP "+ getHp()+"/"+getMaxHp();
		}
	public String getStats() {
		return "HIT " +getHit()+ "   ATK " +getAtk()+ "   DEF "+getDef()+ "   EVA "+ getEva();
	}
}
