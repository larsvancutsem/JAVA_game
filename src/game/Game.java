package game;

import java.lang.Math;
import java.util.ArrayList;

import gui.GameGUI;
import gui.IntroGUI;

public class Game {

	//class variables
	public Board board; // configuration of board and its tiles
	public Unit[][] state; // current configuration of characters
	public GameGUI myGui; // graphic user interface for actual game
	public IntroGUI myIntroGUI; // graphic user interface for introduction of game
	public boolean endPointReached=false; //game variables indicating end x2
	public boolean deadPrince=false;
	public int turnsLeft = 15; //game turns left
	public String gameOverString;
	public ArrayList<Unit> allUnits = new ArrayList<Unit>();
	public String[] gameParameters = new String[2];
	public HighScoreCalculator myHighScoreCalculator;

	//class constructor
	public Game(){
		this.board = new Board();
		setInitialState();
	}

	//initialise game
	public void setInitialState() {

		//initialising units with respective statistics
		Prince prince = new Prince("Prince",25,75,7,5,2);
		Ally guard1 = new Ally("Guard",22,75,7,6,2);
		Ally guard2 = new Ally("Retainer",22,75,7,6,2);
		Enemy bandit1 = new Enemy("Bandit",13,70,7,4,2);
		Enemy bandit2 = new Enemy("Bandit",13,70,7,4,2);
		Enemy bandit3 = new Enemy("Bandit",13,70,7,4,2);
		Enemy bandit4 = new Enemy("Bandit",13,70,7,4,2);
		Boss boss = new Boss("Boss",25,75,8,5);

		//add all units to list for GUI display of statistics
		allUnits.add(prince);
		allUnits.add(guard1);
		allUnits.add(guard2);
		allUnits.add(bandit1);
		allUnits.add(bandit2);
		allUnits.add(bandit3);
		allUnits.add(bandit4);
		allUnits.add(boss);
		
		//initial coordinates for units
		int[][] allyCoord = {{0,2},{2,0}};
		int[] princeCoord = {1,1};
		int[][] enemyCoord = {{1,4},{4,2},{4,5},{6,4}};
		int[] bossCoord = {6,6};

		//temporal unit setting for game
		Unit[][] tempSetting = new Unit[8][8];

		//set units in place
		tempSetting[allyCoord[0][0]][allyCoord[0][1]]=guard1;
		tempSetting[allyCoord[1][0]][allyCoord[1][1]]=guard2;
		tempSetting[princeCoord[1]][princeCoord[1]]=prince;
		tempSetting[enemyCoord[0][0]][enemyCoord[0][1]]=bandit1;
		tempSetting[enemyCoord[1][0]][enemyCoord[1][1]]=bandit2;
		tempSetting[enemyCoord[2][0]][enemyCoord[2][1]]=bandit3;
		tempSetting[enemyCoord[3][0]][enemyCoord[3][1]]=bandit4;
		tempSetting[bossCoord[0]][bossCoord[1]]=boss;

		//set class state accordingly
		this.state= tempSetting;
	}

	//set difficulty parameters for game 
	public void setGameParameters(String[] gameParam) {
		this.gameParameters = gameParam;
	}

	// Method to set Enemy statistics
	public static void setEnemyStats (int maxhp, int atk, Unit ... enemies) {
		for (Unit enemy : enemies) {
			enemy.setMaxHp(maxhp);
			enemy.setHp(maxhp);
			enemy.setAtk(atk);
		}
	}

	//adjust unit statistics based on game difficulty
	public void setUnitStats(String[] gameParam,ArrayList<Unit> allUnits) {

		//Adjusting enemy statistics according to the difficulty
		switch(gameParam[0]) {
		case "Easy":
			break;
		case "Standard": 
			setEnemyStats(18,9, allUnits.get(3),allUnits.get(4),allUnits.get(5),allUnits.get(6));
			setEnemyStats(30,10,allUnits.get(7));
			break;
		case "Hard":
			setEnemyStats(21,10,allUnits.get(3),allUnits.get(4),allUnits.get(5),allUnits.get(6));
			setEnemyStats(35,11,allUnits.get(7));
			break;
		}

		//Adjusting the prince's statistics according to the choice made
		switch (gameParam[1]) {
		case "HIT":
			allUnits.get(0).setHit(90);
			break;
		case "ATK":
			allUnits.get(0).setAtk(9);
			break;
		case "DEF":
			allUnits.get(0).setDef(7);
			break;
		}
	}

	//checks if unit move is valid
	public boolean validUnitMove(int row1, int col1, int row2, int col2) {
		int manhattanDistance = manhattanDistance(row1, col1, row2, col2);
		if((returnUnit(new int[]{row2,col2})==(returnUnit(new int[]{row1,col1})))||(returnUnit(new int[]{row2,col2})==null)&&(manhattanDistance<=state[row1][col1].getMov())) {
			return true;
		} else {
			return false;
		}
	}

	//apply unit move (checks if unit move is valid)
	public void moveUnit(int row1, int col1, int row2, int col2) {
		if(validUnitMove(row1,col1,row2,col2)&&(row1==row2&&col1==col2)){
			this.state[row2][col2] = this.state[row1][col1];
		} else if(validUnitMove(row1,col1,row2,col2)){
			this.state[row2][col2] = this.state[row1][col1];
			this.state[row1][col1]=null;
		} else {
			System.out.println("invalid move!");
		}
		updateGUI(); // GUI has to be updated in order for move to be visible
	}

	//start the actual game
	public void startGame() {

		//local variables
		ArrayList<Unit> allies = new ArrayList<Unit>();
		ArrayList<Unit> enemies = new ArrayList<Unit>();

		//initiate combat calculator and respective turns for the game
		Combat myCombat = new Combat();
		allyTurn myAllyTurn = new allyTurn(this,allies,myCombat);
		enemyTurn myEnemyTurn = new enemyTurn(this,enemies,myCombat);

		//Actual game play while ending criteria are not met
		while((endPointReached==false)&&(deadPrince==false)&&(turnsLeft>0)) {
			//clean grid from dead units + re-initiate living units
			allies.clear();
			enemies.clear();
			for(int i=0;i<8;i++) {
				for(int j=0;j<8;j++) {
					if (this.state[i][j]==null) {
					} else if(!this.state[i][j].getAlive()) {
						this.state[i][j]=null;
					} else if(this.state[i][j] instanceof Ally) {
						allies.add(this.state[i][j]);
					} else if(this.state[i][j] instanceof Enemy) {
						enemies.add(this.state[i][j]);
					}
				}
			}

			//Ally turn
			myAllyTurn.start();

			//Enemy turn
			myEnemyTurn.start();

			//update play conditions
			deadPrince=!princeLives();
			endPointReached=endPointReached();
			turnsLeft-=1;
		}
		//the game is over
		System.out.println("Game Over");
		if(deadPrince) {
			gameOverString="Prince died";
		} else if(turnsLeft<=0) {
			gameOverString="Out of Turns";
		} else {
			gameOverString="";
		}
	}

	//return unit statistics for GUI display
	public int endGameAllies() {
		int sum=0;
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(state[r][c] instanceof Ally) {
					sum+=1;
				}
			}
		}
		return sum;
	}

	//return a unit (or null) based on coordinates
	public Unit returnUnit(int[] coord_in) {
		return this.state[coord_in[0]][coord_in[1]];
	}

	//return coordinates of a specific unit
	public int[] returnCoordinates(Unit unit_in) {
		int[] coord = new int[2];

		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				if(this.state[i][j]==unit_in) {
					coord=new int[] {i,j};
				}
			}
		}
		return coord;
	}

	//check if the prince still lives for determining game outcome
	public boolean princeLives() {
		boolean result=false;
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(state[r][c] instanceof Prince){
					result=true;
				}
			}
		}
		return result;
	}

	//check if the end point is reached for determining game outcome
	public boolean endPointReached() {
		boolean result=false;
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(this.board.getTileType(r, c)=="end point") {
					result=state[r][c] instanceof Ally;
				}
			}
		}
		return result;
	}

	//convenient function for measuring Manhattan distance between tiles
	public int manhattanDistance(int row1, int col1, int row2, int col2) {
		return Math.abs(row1-row2) + Math.abs(col1-col2);
	}

	//start the Graphic User Interface for the game
	public void startGui() {
		this.myIntroGUI = new IntroGUI(this);
		while(this.gameParameters[0]== null) {
			this.pause(10);
		}
		this.myGui=new GameGUI(this.state,this.board.boardLayout,this.allUnits, this.turnsLeft);
		this.myGui.initiate();
	}

	//update graphic panel with unit statistics
	public void updateGUIStats() {
		this.myGui.updateInfo(turnsLeft, allUnits);
	}
	
	//pause system
	public void pause(int millisec) {
		try
		{
			Thread.sleep(millisec);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
	}

	//update the Graphic User Interface for the game
	public void updateGUI() {
		this.myGui.updateState();
		this.myGui.updateScenery();
		this.myGui.updateVisibility();
	}

	//main game method
	public static void main(String[] args) {
		Game myGame = new Game();
		myGame.startGui();
		myGame.setUnitStats(myGame.gameParameters,myGame.allUnits);
		myGame.updateGUIStats();
		myGame.startGame();
		myGame.myHighScoreCalculator=new HighScoreCalculator(myGame.endGameAllies(),myGame.turnsLeft,myGame.gameParameters[0]);
		myGame.myGui.showGameOver(myGame.gameOverString, myGame.myHighScoreCalculator);
	}
}