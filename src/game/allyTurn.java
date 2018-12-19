package game;

import java.util.ArrayList;
import java.util.Arrays;

public class allyTurn {
	
	//class variables
	Game currentGame;
	ArrayList<Unit> allies;
	Unit[][] state;
	Combat combat;
	
	//class constructor
	public allyTurn(Game game_in, ArrayList<Unit> allies_in, Combat combatIn) {
		this.currentGame=game_in;
		this.state=currentGame.state;
		this.allies=allies_in;
		this.combat=combatIn;
	}
	
	//start the ally turn
	public void start() {
		
		//reset selected buttons
		currentGame.myGui.pressedButton=false;
		
		for(int i=0;(i<allies.size())&&(currentGame.princeLives())&&(!currentGame.endPointReached());i++) {
			//new place for ally
			step(i);
			//update ally statistics
			updateStats(i);
			//possible ally attack
			attack(i);
			//update GUI stats
			currentGame.updateGUIStats();
			// GUI has to be updated + focus of ally has to change
			updateTurn();
		}
	}
	
	
	//ally placement method
	public void step(int index) {
		//local variables
		int[] coordinatesFrom = currentGame.returnCoordinates(allies.get(index));
		
		//update which button is in focus & update GUI
		currentGame.myGui.buttonFocus(coordinatesFrom[0], coordinatesFrom[1]);
		currentGame.updateGUI();
	
		//wait for pressed button
		while(currentGame.myGui.pressedButton==false) {
			currentGame.pause(10);
		}
	
		//set coordinates to pressed button location
		int[] coordinatesTo=currentGame.myGui.indexPressedButton;
	
		//in the case that move to new location is impossible
		//wait for new button location
		while(!currentGame.validUnitMove(coordinatesFrom[0],coordinatesFrom[1],coordinatesTo[0],coordinatesTo[1])) {
			currentGame.pause(10);
			coordinatesTo=currentGame.myGui.indexPressedButton;
		}
	
		//move unit to new location & reset pressedButton variable
		currentGame.moveUnit(coordinatesFrom[0],coordinatesFrom[1],coordinatesTo[0],coordinatesTo[1]);
		currentGame.myGui.pressedButton=false;
	}
	
	public void updateStats(int i) {
		//local variables
		int[] tileCoord=currentGame.returnCoordinates(allies.get(i));
		int newEVA=0;
		String tileType= currentGame.board.getTileType(tileCoord[0], tileCoord[1]);

		//update EVA based on tile
		if(tileType.equals("tent")) {
			newEVA=30;
		} else if(tileType.equals("forest")){
			newEVA=15;
		}
		allies.get(i).updateEva(newEVA);
	}
	
	public void attack(int index) {

		//local variables
		int[] coordinates = currentGame.returnCoordinates(allies.get(index));
		
		//only attack when enemy is nearby
		if(isEnemyNearby(coordinates)==true) {
			//wait for pressed button
			while(currentGame.myGui.pressedButton==false) {
				currentGame.pause(10);
			}
				
			//set coordinates to pressed button location
			int[] coordinatesAttack=currentGame.myGui.indexPressedButton;
				
			//in the case that move to new location is impossible
			//wait for new button location
			while((currentGame.manhattanDistance(coordinates[0], coordinates[1], coordinatesAttack[0], coordinatesAttack[1])!=1)||!(state[coordinatesAttack[0]][coordinatesAttack[1]] instanceof Enemy)) {
					currentGame.pause(10);
					coordinatesAttack=currentGame.myGui.indexPressedButton;
			}
			combat.performCombat(allies.get(index), currentGame.returnUnit(coordinatesAttack), "allies");
		} else {
			System.out.println("ally can't attack!");
		}
	}
	
	public void updateTurn() {
		currentGame.myGui.clearFocus();
		currentGame.updateGUI();
		this.state=currentGame.state;
		
	}
	
	public boolean isEnemyNearby(int[] cIn) {
		boolean nearbyEnemy=false;
		for(int r=cIn[0]-1;r<=cIn[0]+1;r++) {
			for(int c=cIn[1]-1+Math.abs(r-cIn[0]);c<=cIn[1]+1-Math.abs(r-cIn[0]);c++) {
				if((c<8)&&(c>=0)&&(r<8)&&(r>=0)&&(this.state[r][c] instanceof Enemy)) {
					nearbyEnemy=true;
				}
			}
		}
		return nearbyEnemy;
	}
}