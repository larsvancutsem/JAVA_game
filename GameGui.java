package Gui;

import Game.Ally;
import Game.Boss;
import Game.Enemy;
import Game.Game;
import Game.Prince;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ToolTipUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import java.text.DecimalFormat;

public class GameGui extends JFrame implements ActionListener{
	
	//variables
	Game currentGame;
	int[] indexLastPressed;
	
	
	//components grid panel
	JButton[][] tileButton = new JButton[8][8];
	
	
	//panel
	ImagePanel gridPanel = new ImagePanel("src/pictures/terrainImage.png");
	
	//creation of GUI
	public GameGui(Game game_in){
		super("Flame Insignia: the Game");
		
		// link game
		this.currentGame=game_in;
		
		// setup gridPanel
		gridPanelSetup();
		

		//format JFrame
		setLayout(new GridLayout(1,1));
		

		
		
		add(gridPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public void gridPanelSetup() {
		//create button group
		ButtonGroup group = new ButtonGroup();
		
		//create buttons+contours
	    for(int r=0;r<8;r++){
	        for(int c=0;c<8;c++) {
	            if(this.currentGame.board.getTileType(r, c).equals("forest")) {
	            	tileButton[r][c] = new MyButton("green");
	            	tileButton[r][c].setToolTipText("Here is a forest: +X% evasion");
		            tileButton[r][c].addActionListener(this);
		            tileButton[r][c].setContentAreaFilled(false);
	            } else if(this.currentGame.board.getTileType(r, c).equals("tent")) {
	            	tileButton[r][c] = new MyButton("red");
	            	tileButton[r][c].setToolTipText("Here is a tent: +X% evasion");
		            tileButton[r][c].addActionListener(this);
		            tileButton[r][c].setContentAreaFilled(false);
	            } else {
	            	tileButton[r][c] = new MyButton("gray");
		            tileButton[r][c].addActionListener(this);
		            tileButton[r][c].setContentAreaFilled(false);
	            }
	            
	            group.add(tileButton[r][c]);
	            gridPanel.add(tileButton[r][c]);
	        }
	    }
	    
	    //add unit elements (we do this by updating the playfield and visibility)
	    updateState();
	    updateVisibility();
	    
		//format panel
	    gridPanel.setLayout(new GridLayout(8,8));
	}
	
	public void updateScenery() {
		for(int r=0;r<8;r++){
	        for(int c=0;c<8;c++) {
	            if(this.currentGame.board.getTileType(r, c).equals("forest")) {
	            	tileButton[r][c].setToolTipText("Here is a forest: +X% evasion");
	            	tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.GREEN,2));
		            tileButton[r][c].setContentAreaFilled(false);
	            } else if(this.currentGame.board.getTileType(r, c).equals("tent")) {
	            	tileButton[r][c].setToolTipText("Here is a tent: +X% evasion");
	            	tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.RED,2));
		            tileButton[r][c].setContentAreaFilled(false);
	            } else {
		            tileButton[r][c].setContentAreaFilled(false);
		            if(((MyButton) tileButton[r][c]).getFocus()==true) {
		            	tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
		            } else {
		            	tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		            }
	            }
	        }
	    }
	}
	
	public void updateState() {
		//method to display current state of game
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(currentGame.state[r][c] instanceof Ally) {
					//paste "stretchy" image of ally
	            	tileButton[r][c].setIcon(new StretchIcon("src/pictures/allyImage.png"));
	            } else if (currentGame.state[r][c] instanceof Enemy) {
	            	//paste "stretchy" image of enemy
	            	tileButton[r][c].setIcon(new StretchIcon("src/pictures/enemyImage.png"));
	            } else if (currentGame.state[r][c] instanceof Prince) {
	            	//paste "stretchy" image of enemy
	            	tileButton[r][c].setIcon(new StretchIcon("src/pictures/princeImage.png"));
	            } else if (currentGame.state[r][c] instanceof Boss) {
	            	//paste "stretchy" image of enemy
	            	tileButton[r][c].setIcon(new StretchIcon("src/pictures/bossImage.png"));
	            } else {
	            	//remove any present icon
	            	tileButton[r][c].setIcon(null);
	            }
			}
		}
	}
	
	public void updateVisibility() {
		//visibility is only in manhattaDistance of 3 from player
		String coorBlack = returnIndexesBlack(3); 
		
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(!coorBlack.contains(r+","+c)){
					tileButton[r][c].setContentAreaFilled(true);
					tileButton[r][c].setIcon(null);
					tileButton[r][c].setToolTipText(null);
					tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
					tileButton[r][c].setBackground(Color.BLACK);
				}
			}
		}
	}
	
	public String returnIndexesBlack(int range) {
		String returnString = "";		
		
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(((currentGame.state[r][c] instanceof Ally)||(currentGame.state[r][c] instanceof Prince))){
					//only tiles in Manhattan distance<=3 and tile itself are included
					for(int r1=(r-range);r1<(r+range+1);r1++) {
						for(int c1=c-range+Math.abs(r1-r);c1<(c+range-Math.abs(r1-r))+1;c1++) {
							returnString=returnString+r1+","+c1+" ";
						}
					}
				}
			}
		}
		return returnString;
	}
	
	public String returnIndexesBlue(int[] point, int range) {
		String returnString = "";		
		int col=point[1];
		int row=point[0];
		
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if((Math.abs(r-row)+Math.abs(c-col)<=2&&(currentGame.state[r][c]==null))){
							returnString=returnString+r+","+c+" ";
				}
			}
		}
		if(currentGame.state[row][col]==null) {
			returnString=returnString+row+","+col+" ";
		}
		
		return returnString;
	}
	
	public void initiate() {
		setSize(600, 550);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int[] pressedButton=new int[2];
		
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				if(e.getSource()==tileButton[i][j]) {
					//find correct pressed button
					pressedButton= new int[] {i,j};
					//movement
					if(((MyButton) tileButton[i][j]).getFocus()==true) {
						this.currentGame.moveUnit(indexLastPressed[0], indexLastPressed[1], i, j);
					}
				}
				//clear focus
				((MyButton) tileButton[i][j]).focusOff();
			}
		}
		
		//update last pressed button
		indexLastPressed=pressedButton;
		
		if(!(currentGame.state[pressedButton[0]][pressedButton[1]] instanceof Ally)&&!(currentGame.state[pressedButton[0]][pressedButton[1]] instanceof Prince)) {
			System.out.println("invalid choice");
		}else {
			updateOptions(pressedButton);
		}
		updateScenery();
		updateState();
		updateVisibility();
	}

	public void updateOptions(int[] coorIn) {
		String coorBlue = returnIndexesBlue(coorIn,2); 
		
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(coorBlue.contains(r+","+c)){
					//update focus
					((MyButton) tileButton[r][c]).focusOn();
				}
			}
		}
	}
	
}
