package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MyButton extends JButton {
	//variables
	public Color borderColor;
	public int borderSize;
	public boolean focus;
	
	 public MyButton(String borderColor_in) {
	        super();
	        this.borderColor=myColor(borderColor_in);
	        this.borderSize=mySize(borderColor_in);
	        this.focus=false;
	        setBorder(BorderFactory.createLineBorder(borderColor,borderSize));
	        userInterface();
	    }
	 
	 public boolean getFocus() {
		 return this.focus;
	 }
	 
	 public void focusOn(){
		 this.focus=true;
	 }
	 
	 public void focusOff() {
		 this.focus=false;
	 }
	 
	 private void userInterface() {
		 addMouseListener(new MouseAdapter() {

	            @Override
	            public void mouseEntered(MouseEvent e) {
	                setBorder(BorderFactory.createLineBorder(Color.yellow,4));
	            }

	            @Override
	            public void mouseExited(MouseEvent e) {
	            	if(isContentAreaFilled()) {
	            		setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
	            	}  else if (focus==true){
	            		setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
	            	} else {
	            		setBorder(BorderFactory.createLineBorder(borderColor,borderSize));
	            	}
	            }
	        });
	 }
	 
	public Color myColor(String input) {
		//local variable
		Color color=Color.GRAY;
		
		switch (input) {
		//forest
	    case "green":
	        color = Color.GREEN;
	        break;
	     //castle
	    case "red":
	        color = Color.RED;
	        break;
	     //plain
	    case "gray":
	        color = Color.GRAY;
	        break;
		}
		return color;
	}
	
	public int mySize(String input) {
		//local variable
		int size=1;
		
		switch (input) {
		//forest
	    case "green":
	        size=2;
	        break;
	     //castle
	    case "red":
	        size=2;
	        break;
	     //plain
	    case "gray":
	        size=1;
	        break;
		}
		return size;
	}
}
