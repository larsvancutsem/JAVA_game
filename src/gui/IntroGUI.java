package gui;

//Temp storage of imports; divide/remove this later
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import game.Unit;
import game.Ally;
import game.Enemy;
import game.Game;


public class IntroGUI extends JFrame{

	//class variables - introduction text
	//Flavour text
	String introText1 =  "<html>The relationship between your kingdom and its neighbor has always been strained:<br>"
			+"Over the centuries, many wars have been fought between the two countries, and even in times of relative peace, small-scale battles occur frequently.<br>"
			+"But a change is happening this winter: An envoy with royal blood was sent by the neighbouring kingdom to arrange talks for peace and tariffs,<br>"
			+"and as the Prince, you are tasked with escorting the envoy back to his homeland, as a sign of sincerity and goodwill.<br>"
			+"While engaging in idle conversation with the envoy, curiosity gets the better of him and he asks you how your life was, growing up as a Prince.<br>"
			+"Reluctantly, you respond with: 'My life was ...'</html>";

	String choiceText1 = "Easy. Aside from combat-training, I've had no difficulties growing up.";
	String choiceText2 = "Standard. I've fought in some battles and have gotten seriously injured a few times.";
	String choiceText3 = "Hard. I've been fighting on battlefields my whole life. I've forgotten the amount of times I nearly died.";

	String introText2 = "<html>The envoy persistently keeps asking question after question, and before losing your patience, you permit him to ask one last question.<br>"
			+"The envoy mutters in protest for a while, before asking his last question: 'What is the thing you pride yourself the most in?</html>";

	String choiceText4 = "My aim. I always succeed in hitting my opponent.";
	String choiceText5 = "My strength. I crush my opponents with overwhelming power.";
	String choiceText6 = "My armor. I shrug off my opponents attacks like my morning robe.";

	String introText3 = "<html>At nightfall your group sets up camp, and everyone, except for those on night duty, eventually goes to sleep in their respective tents.<br>"
			+"Suddenly you wake up to screams. You hear a guard yell: 'Bandits! The mountain bandits are attacking! They've taken the envoy!'<br>"
			+"You immediately realize the severity of the situation: If something were to happen to the envoy, it would mean war. And it would not end in your life time...<br>"
			+" You run outside, prepared for battle, and find some allies to fight back and rescue the envoy...</html>";

	//Initialising panels, labels, buttons ...
	Font font = new Font("Calibri",Font.PLAIN, 16);
	JLabel introLabel1 = new JLabel(introText1, JLabel.LEFT);
	JLabel introLabel2 = new JLabel(introText2, JLabel.LEFT);
	JLabel introLabel3 = new JLabel(introText3, JLabel.LEFT);

	JPanel choicePanel1 = new JPanel();
	ButtonGroup choiceGroup1 = new ButtonGroup();
	JRadioButton choiceButton1 = new JRadioButton(choiceText1);
	JRadioButton choiceButton2 = new JRadioButton(choiceText2);
	JRadioButton choiceButton3 = new JRadioButton(choiceText3);

	JPanel choicePanel2 = new JPanel();
	ButtonGroup choiceGroup2 = new ButtonGroup();
	JRadioButton choiceButton4 = new JRadioButton(choiceText4);
	JRadioButton choiceButton5 = new JRadioButton(choiceText5);
	JRadioButton choiceButton6 = new JRadioButton(choiceText6);

	JButton startButton =new JButton("Start Battle");

	//class constructor
	public IntroGUI(Game myGame){

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Flame Insignia: Prologue");

		//Setting the font of the text
		introLabel1.setFont(font);
		introLabel2.setFont(font);
		introLabel3.setFont(font);
		startButton.setFont(new Font("Calibri",Font.BOLD, 18));

		//Setting the layout of the choices
		choicePanel1.setLayout(new GridLayout(3,1));
		addComponents(choicePanel1, font, choiceButton1, choiceButton2, choiceButton3);		
		addButtonGroup(choiceGroup1, choiceButton1, choiceButton2, choiceButton3);

		choicePanel2.setLayout(new GridLayout(3,1));
		addComponents(choicePanel2, font, choiceButton4, choiceButton5, choiceButton6);
		addButtonGroup(choiceGroup2, choiceButton4, choiceButton5, choiceButton6);

		//Setting the overall layout
		setLayout(new GridLayout(6,1));
		add(introLabel1);
		add(choicePanel1);
		add(introLabel2);
		add(choicePanel2);
		add(introLabel3);
		add(startButton);

		//Setting default choices
		choiceButton2.setSelected(true);
		choiceButton4.setSelected(true);

		//Setting actionListener to start the GameGUI with the variables gotten from the choices
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event){

				//local variables
				String[] gameParam = getGameParameters();

				myGame.setGameParameters(gameParam);
				dispose();
			}
		});

		setPreferredSize(new Dimension(1050,800));
		pack();
		setVisible(true);
	}

	//Method to add labels or buttons to a container
	private static void addComponents (Container contentPane, Font font, JComponent ... components) {
		for(JComponent component : components) {
			contentPane.add(component);
			component.setFont(font);
		}
	}

	//Method to add buttons to a ButtonGroup
	private static void addButtonGroup (ButtonGroup buttonGroup, JRadioButton ... buttons) {
		for(JRadioButton button : buttons) {
			buttonGroup.add(button);
		}
	}

	// Method to get variables from the choices
	public String[] getGameParameters() {
		String[] gameParam = new String[2];
		if(choiceButton1.isSelected()) {
			gameParam[0] = "Easy";
		} else if (choiceButton2.isSelected()) {
			gameParam[0] = "Standard";
		} else if (choiceButton3.isSelected()) {
			gameParam[0] = "Hard";
		}

		if(choiceButton4.isSelected()) {
			gameParam[1] = "HIT";
		} else if (choiceButton5.isSelected()) {
			gameParam[1] = "ATK";
		} else if (choiceButton6.isSelected()) {
			gameParam[1] = "DEF";
		}
		return gameParam;
	}
}