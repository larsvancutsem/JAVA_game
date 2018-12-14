package Game;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Board{
	
	final static int length = 8;
	public Tile[][] boardLayout = new Tile[length][length];
	

    public Board() {
    	//initiate configuration of board
    	setTiles();
    }
    
    public void setTiles() {
    	// create tiles
    	// tiles can be of type forest, castle, plain or end point.
    	
    	//strings will be retrieved from file
    	String forestCoord = "2,6 3,7 4,2 5,1 5,5 5,7 6,0 6,1 6,2 6,5 7,0 7,1 7,3 7,6 7,7";
    	String tentCoord = "0,2 1,1 1,5 2,0 3,4";
    	String endPointCoord = "5,5";
    	
    	// initiate different tiles
    	for(int row=0;row<length;row++) {
    		for( int col=0;col<length;col++) {
    			if(forestCoord.contains(row+","+col)) {
    				this.boardLayout[row][col]=new Tile("forest",row,col);
    			} else if(tentCoord.contains(row+","+col)) {
    				this.boardLayout[row][col]=new Tile("tent",row,col);
    			} else if(endPointCoord.contains(row+","+col)) {
    				this.boardLayout[row][col]=new Tile("end point",row,col);
    			} else {
    				this.boardLayout[row][col]=new Tile("plain",row,col);
    			}
    		}
    	}
    }
    
    public String getTileType(int row_in, int col_in) {
    	return this.boardLayout[row_in][col_in].getType();
    }
}
