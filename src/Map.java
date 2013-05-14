/*********************************************
/*  Map.java 
/*  
/*  COMP3411 Artificial Intelligence
/*  UNSW Session 1, 2013
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class Map {
    
    // Class constants
    public static final int MAP_WIDTH = 160;
    public static final int MAP_HEIGHT = 160;

    // Class variables
    private Enums.Symbol[][] map;
    
    // Creator method
    public Map() {
        
        map = new Enums.Symbol[MAP_WIDTH][MAP_HEIGHT];

        // Set all coordinated of the map to UNKNOWN at the start
        for (int i=0; i < MAP_WIDTH; i++) {
            for (int j=0; j < MAP_HEIGHT; j++) {
            
            	map[i][j] = Enums.Symbol.UNKNOWN;
            }
        }
        
    }
    
    // Update the explored map given a new view that has been explored
    // A state is passed in to find the direction of the agent
    // We assume the given view is a 5x5 square with the agent in the middle
    public void updateMap(char view[][], State state) {
    
    	System.out.println("==> Updating Map:");
    	System.out.println("- posX = "+state.posX + "\n- posY = "+state.posY + "\n- direction = "+state.direction);
    
    	// TODO Might not have ifs for all directions (just can't be bothered thinking of
    	// indices' hax right now).
    	if (state.direction == Enums.Direction.NORTH) {
    	
    		int x = state.posX - view.length/2;
    		for (int i=0; i < view.length; i++) {
    			
    			int y = state.posY - view[0].length/2;
    			for (int j=0; j < view[0].length; j++) {
    			
    				map[x][y] = Enums.charToEnum(view[i][j]);
    			
    				y++;
    			}
    			
    			x++;
    		}
    	}
    
    	// Print the result for testing
    	printMap();
    
    /*
    	XXXXXXXXXXX	
    	XXXXXXXXXXX
    	XXX+ d  XXX
    	XXX     XXX
    	XXX  ^  XXX
    	XXX     XXX
    	XXX     XXX
    	XXXXXXXXXXX
    	XXXXXXXXXXX
    	
    	^ : i=80, 	j=80
    	+ : i=80-2	j=80-2
    */	
    	
    	
    }
    
    // Print a ascii version of the entire map
    public void printMap() {
    
    	// Set all coordinated of the map to UNKNOWN at the start
        for (int i=0; i < MAP_WIDTH; i++) {
            for (int j=0; j < MAP_HEIGHT; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    
    }
    
}
