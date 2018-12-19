package game;

import java.util.ArrayList;

public class enemyTurn {
	
	//class variables
	Game currentGame;
	ArrayList<Unit> enemies;
	int[][] allyCoord;
	int[][] enemyCoord;
	Unit[][] state;
	Combat combat;
	
	//class constructor
	public enemyTurn(Game game_in, ArrayList<Unit> enemies_in, Combat combatIn){
		this.currentGame=game_in;
		this.state=game_in.state;
		this.enemies=enemies_in;
		this.combat=combatIn;
	}
	
	//start the enemy turn
	public void start() {
		//loop through active enemies and let them get new place on grid + let them attack if possible
		for(int i =0;((i<enemies.size())&&(currentGame.princeLives())&&(!currentGame.endPointReached()));i++) {
			//new algorithmic place for enemy
			newPlace(i);
			//update enemy statistics
			updateStats(i);
			//possible enemy attack
			attack(i);
			//small pause for oversight 
			currentGame.pause(500);
			//update GUI stats
			currentGame.updateGUIStats();
			//update graphic GUI
			currentGame.updateGUI();
		}
	}
	
	//find new place for enemy
	public void newPlace(int in) {
		//local variables
		Unit enemy = enemies.get(in);
		int[] direction = new int[2];
		int[] enemyCoord = currentGame.returnCoordinates(enemy);
		int[] targetCoord=findTarget(enemyCoord);
		int currentShortest=9999; // high integer
		
		//direction should return on of the closest free spaces closeby target (and within moving range of unit) 
		for(int r1=(enemyCoord[0]-2);r1<=(enemyCoord[0]+2);r1++) {
			for(int c1=enemyCoord[1]-2+Math.abs(r1-enemyCoord[0]);c1<=(enemyCoord[1]+2-Math.abs(r1-enemyCoord[0]));c1++) {
				int dist=currentGame.manhattanDistance(r1,c1,targetCoord[0],targetCoord[1]);
				if((r1<=7)&&(r1>=0)&&(c1<=7)&&(c1>=0)&&(currentGame.validUnitMove(enemyCoord[0], enemyCoord[1], r1, c1))&&(dist<currentShortest)) {
					currentShortest=dist;
					direction[0]=r1;direction[1]=c1;
				}
			}
		}
		currentGame.moveUnit(enemyCoord[0],enemyCoord[1],direction[0],direction[1]);
		currentGame.updateGUI(); // GUI has to be updated in order for move to be visible
		updateState(); // state has to be updated in order to avoid moving conflicts with next enemy
	}
	
	//find the closest ally target for the enemy in question
	public int[] findTarget(int[] enemyCoord) {
		//local variables
		int[] pointer= new int[2];
		int currentBest=9999; //large integer
		
		//pointer returns coordinates to ally that is closest by
		for(int row=0;row<7;row++) {
			for(int col=0;col<7;col++) {
				if(state[row][col] instanceof Ally) {
				int dist=currentGame.manhattanDistance(row,col,enemyCoord[0],enemyCoord[1]);
				if(dist<currentBest) {
					currentBest=dist;
					pointer= new int[] {row,col};
				}
				}
			}
		}
		return pointer;
	}
	
	//update EVA for enemy when it is placed on special type tile
	public void updateStats(int i) {
		//local variables
		int[] tileCoord=currentGame.returnCoordinates(enemies.get(i));
		int newEVA=0;
		String tileType= currentGame.board.getTileType(tileCoord[0], tileCoord[1]);

		//update EVA based on tile
		if(tileType.equals("tent")) {
			newEVA=30;
		} else if(tileType.equals("forest")){
			newEVA=15;
		}
		enemies.get(i).updateEva(newEVA);
	}
	
	//let enemy perform attack
	public void attack(int i) {
		//local variables
		int[] coordinates = currentGame.returnCoordinates(enemies.get(i));
		int[] attackCoordinates = giveCoordNearbyAlly(coordinates);
		
		//attack has to be possible, only when valid coordinates are passed on enemy can attack target
		if(!(attackCoordinates.length==0)) {
			combat.performCombat(enemies.get(i), currentGame.returnUnit(attackCoordinates), "enemies");
		} else {
			System.out.println("enemy can't attack");
		}
	}
	
	//returns coordinates to nearest ally if possible, gives back empty array if no nearby allies
	public int[] giveCoordNearbyAlly(int[] cIn) {
		//local variables
		int[] coord = new int[] {};
		
		//loop through local neighbourhood of input coordinates and return coordinates of nearby ally
		for(int r=cIn[0]-1;r<=cIn[0]+1;r++) {
			for(int c=cIn[1]-1+Math.abs(r-cIn[0]);c<=cIn[1]+1-Math.abs(r-cIn[0]);c++) {
				if((c<8)&&(c>=0)&&(r<8)&&(r>=0)&&(this.state[r][c] instanceof Ally)) {
					coord=new int[] {r,c};
				}
			}
		}
		return coord;
	}
	
	//update the current game state (necessary after every move)
	public void updateState() {
		this.state=currentGame.state;
	}
}