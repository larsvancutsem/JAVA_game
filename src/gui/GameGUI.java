package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ToolTipUI;

import game.Ally;
import game.Boss;
import game.Enemy;
import game.Game;
import game.HighScoreCalculator;
import game.Prince;
import game.Tile;
import game.Unit;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import java.text.DecimalFormat;

public class GameGUI extends JFrame implements ActionListener{

	//class variables
	Unit[][] mostRecentState;
	Tile[][] gameBoard;
	public boolean pressedButton = false;
	public int[] indexPressedButton=new int[2];

	//components grid panel
	MyButton[][] tileButton = new MyButton[8][8];

	//panels
	ImagePanel gridPanel = new ImagePanel("src/pictures/terrainImage.png");
	JPanel infoPanel = new JPanel();
	static Font font = new Font("Calibri",Font.PLAIN, 16);	

	//class constructor
	public GameGUI(Unit[][] initialState, Tile[][] gameBoard_in, ArrayList<Unit> allUnits, int startTurns){
		super("Flame Insignia: the Game");

		// link game
		this.mostRecentState=initialState;
		this.gameBoard=gameBoard_in;

		// Setting Layout of the panel with status information
		infoPanel.setLayout(new GridLayout(13,1));
		updateInfo(startTurns, allUnits);

		// setup gridPanel
		gridPanelSetup();
		setLayout(new GridLayout(1,1));
		add(gridPanel);

		//format JFrame
		setLayout(new BorderLayout());
		add(infoPanel, BorderLayout.EAST);
		add(gridPanel, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1100,900));
		pack();
		setVisible(true);
	}

	// Method to generate the information on the infoPanel:
	// Amount of turns left; Name + Health, Health bar, statistics of a Unit.
	public void updateInfo(int turns, ArrayList<Unit> allUnits) {
		//clear info panel
		infoPanel.removeAll();
		JLabel turn = new JLabel("Turns left: " +turns);
		turn.setFont(font);
		turn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.infoPanel.add(turn);

		for(int i=0;i<allUnits.size();i++) {

			Unit unit=allUnits.get(i);
			JPanel panel = new JPanel();
			JLabel info = new JLabel(unit.getInfo());
			JLabel stats = new JLabel(unit.getStats());
			JProgressBar progressBar = new JProgressBar(0,unit.getMaxHp());

			info.setFont(font);
			stats.setFont(font);
			progressBar.setValue(unit.getHp());

			panel.setLayout(new GridLayout(2,1));
			panel.add(info);
			panel.add(progressBar);
			panel.add(stats);
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			infoPanel.add(panel);
		}
	}

	//setup board Tiles 
	public void gridPanelSetup() {
		//create button group
		ButtonGroup group = new ButtonGroup();

		//create buttons+contours
		for(int r=0;r<8;r++){
			for(int c=0;c<8;c++) {
				if(this.gameBoard[r][c].getType().equals("forest")) {
					tileButton[r][c] = new MyButton("green");
					//tileButton[r][c].setToolTipText("Here is a forest: +15% evasion");
					tileButton[r][c].addActionListener(this);
					//tileButton[r][c].setContentAreaFilled(false);
				} else if(this.gameBoard[r][c].getType().equals("tent")) {
					tileButton[r][c] = new MyButton("red");
					//tileButton[r][c].setToolTipText("Here is a tent: +30% evasion");
					tileButton[r][c].addActionListener(this);
					//tileButton[r][c].setContentAreaFilled(false);
				} else {
					tileButton[r][c] = new MyButton("gray");
					tileButton[r][c].addActionListener(this);
					//tileButton[r][c].setContentAreaFilled(false);
				}

				group.add(tileButton[r][c]);
				gridPanel.add(tileButton[r][c]);
			}
		}

		//format panel
		gridPanel.setLayout(new GridLayout(8,8));
	}

	//update Unit placement
	public void updateBoard(Unit[][] state_in) {
		this.mostRecentState=state_in;
	}

	//update grid scenery (because of changing visibility)
	public void updateScenery() {
		for(int r=0;r<8;r++){
			for(int c=0;c<8;c++) {
				if(this.gameBoard[r][c].getType().equals("forest")) {
					tileButton[r][c].setToolTipText("Here is a forest: +15% evasion");
					tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.GREEN,2));
					tileButton[r][c].setContentAreaFilled(false);
				} else if(this.gameBoard[r][c].getType().equals("tent")) {
					tileButton[r][c].setToolTipText("Here is a tent: +30% evasion");
					tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.RED,2));
					tileButton[r][c].setContentAreaFilled(false);
				} else {
					tileButton[r][c].setContentAreaFilled(false);
					tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
				}
				if(tileButton[r][c].getFocus()==true) {
					tileButton[r][c].setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
				}
			}
		}
	}

	//graphic update of Units
	public void updateState() {
		//method to display current state of game
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if (mostRecentState[r][c] instanceof Prince) {
					//paste "stretchy" image of enemy
					tileButton[r][c].setIcon(new StretchIcon("src/pictures/princeImage.png"));
				} else if (mostRecentState[r][c] instanceof Boss) {
					//paste "stretchy" image of enemy
					tileButton[r][c].setIcon(new StretchIcon("src/pictures/bossImage.png"));
				}else if(mostRecentState[r][c] instanceof Ally) {
					//paste "stretchy" image of ally
					tileButton[r][c].setIcon(new StretchIcon("src/pictures/allyImage.png"));
				} else if (mostRecentState[r][c] instanceof Enemy) {
					//paste "stretchy" image of enemy
					tileButton[r][c].setIcon(new StretchIcon("src/pictures/enemyImage.png"));
				} else {
					//remove any present icon
					tileButton[r][c].setIcon(null);
				}
			}
		}
	}

	//update visibility of grid
	public void updateVisibility() {
		//local variable
		String coorBlack = returnIndexesBlack(3); //visibility is only in manhattaDistance of 3 from player 

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

	//determine which tiles to black out
	public String returnIndexesBlack(int range) {
		//local variable
		String returnString = "";		

		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				if(((mostRecentState[r][c] instanceof Ally)||(mostRecentState[r][c] instanceof Prince))){
					//only tiles in Manhattan distance<=3 and tile itself are included
					for(int r1=(r-range);r1<=(r+range);r1++) {
						for(int c1=c-range+Math.abs(r1-r);c1<=(c+range-Math.abs(r1-r));c1++) {
							if((r1<8)&&(c1<8)&&(r1>=0)&&(c1>=0))
								returnString=returnString+r1+","+c1+" ";
						}
					}
				}
			}
		}
		//System.out.println(returnString);
		return returnString;
	}

	//initiate grid GUI
	public void initiate() {
		//setSize(600, 550);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				if(e.getSource()==tileButton[i][j]) {
					//find correct pressed button
					indexPressedButton= new int[] {i,j};
					pressedButton=true;
				}
			}
		}
	}

	//clear all tiles of focus
	public void clearFocus() {
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				tileButton[r][c].focusOff();
			}
		}
	}

	//set focus on Ally unit
	public void buttonFocus(int row, int col) {
		clearFocus();
		tileButton[row][col].focusOn();
	}

	//gameOver sequence
	// Showing the GameOver Dialog
	public void showGameOver(String gameOverReason, HighScoreCalculator input) {
		JDialog gameOverDialog= new JDialog();
		JPanel textPanel = new JPanel();
		JPanel scorePanel = new JPanel();

		JLabel label = new JLabel();
		JLabel yourScore = new JLabel();
		JButton closeButton = new JButton("Exit Game");

		gameOverDialog.setTitle("Flame Insignia: Epilogue");
		gameOverDialog.setLayout(new BorderLayout());
		//If the game ended because the prince died:
		if (gameOverReason.equals("Prince died")){
			String gameOverText = "<html>You were killed ...<br>"
					+ "With your death, your retainers lost their will to fight and they died at the hand of the bandits as well. <br>"
					+ "The bandits massacred everyone else that was left in the camp and ran away with the envoy.<br>"
					+ "After a week, a travelling merchant discovered the mutilated body of the envoy in a ditch by the road.<br>"
					+ "A war that would last longer than any war previously, erupted between the two countries ...<html/>";	
			label = new JLabel(gameOverText);

			label.setFont(font);
			closeButton.setFont(font);

			gameOverDialog.setLayout(new BorderLayout());
			gameOverDialog.add(label, BorderLayout.CENTER);
			gameOverDialog.add(closeButton, BorderLayout.SOUTH);
		}else if(gameOverReason.equals("Out of Turns")) {
			String gameOverText = "<html> You were too late...<br>"
					+ "The bandits escaped with the envoy. No one was able to pursue them due to their wounds.<br>"
					+ "The next day, a search party combed the area, but to no avail. <br>"
					+ "After a week, a travelling merchant discovered the mutilated body of the envoy in a ditch by the road.<br>"
					+ "A war that would last longer than any war previously, erupted between the two countries ...<html/>";
			label = new JLabel(gameOverText);

			label.setFont(font);
			closeButton.setFont(font);

			gameOverDialog.setLayout(new BorderLayout());
			gameOverDialog.add(label, BorderLayout.CENTER);
			gameOverDialog.add(closeButton, BorderLayout.SOUTH);
			//If the game ended because the the player won:
		}else{
			int highScore = input.getHighScore();
			input.addHighScore(highScore);
			ArrayList<Integer> bestScores = input.getHighScoreList();

			String gameOverText = "<html>You succeeded in rescuing the Envoy!<br>"
					+"You let out a sigh of relief, knowing that the peace between the two countries is not in danger.<br>"
					+ "For now at least.</html>";
			label  = new JLabel(gameOverText);
			yourScore = new JLabel("Your score : "+highScore);

			label.setFont(font);
			yourScore.setFont(font);
			closeButton.setFont(font);
			textPanel.setLayout(new BorderLayout());
			textPanel.add(label, BorderLayout.CENTER);
			textPanel.add(yourScore, BorderLayout.SOUTH);

			scorePanel.setLayout(new GridLayout(5,2));
			for (int i = 0; i<bestScores.size(); i++) {
				JLabel score = new JLabel(" "+(i+1)+": "+bestScores.get(i));
				score.setFont(font);
				scorePanel.add(score);
			}

			gameOverDialog.add(textPanel, BorderLayout.NORTH);
			gameOverDialog.add(scorePanel, BorderLayout.CENTER);
			gameOverDialog.add(closeButton, BorderLayout.SOUTH);
		}

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event){
				System.exit(0);
			}
		});

		gameOverDialog.setPreferredSize(new Dimension(400,500));
		gameOverDialog.pack();
		gameOverDialog.setVisible(true);	
	}
}