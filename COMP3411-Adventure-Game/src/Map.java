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
    
    	state.printState();	
   
    	// NORTH: Top left --> Bottom Right
    	if (state.direction == Enums.Direction.NORTH) {
    	
    		int mapRow = state.posX - view.length/2;
    		for (int viewRow=0; viewRow < view.length; viewRow++) {
    			
    			int mapCol = state.posY - view[0].length/2;
    			for (int viewCol=0; viewCol < view[0].length; viewCol++) {
    			
    				if (mapRow == state.posX && mapCol == state.posY) {
    					map[mapCol][mapRow] = Enums.agentDirection(state.direction);
    				} else {
    					map[mapCol][mapRow] = Enums.charToEnum(view[viewCol][viewRow]);
    				}
    				
    				mapCol++;
    			}
    			
    			mapRow++;
    		}
    	}
    	
    	// EAST: Bottom left --> Top Right
    	if (state.direction == Enums.Direction.EAST) {
    	
    		int mapRow = state.posX - view.length/2;
    		for (int viewRow=0; viewRow < view.length; viewRow++) {
    			
    			int mapCol = state.posY - view[0].length/2;
    			for (int viewCol=view[0].length-1; viewCol >= 0; viewCol--) {
    			
    				if (mapRow == state.posX && mapCol == state.posY) {
    					map[mapCol][mapRow] = Enums.agentDirection(state.direction);
    				} else {
    					map[mapCol][mapRow] = Enums.charToEnum(view[viewCol][viewRow]);
    				}
    				
    				mapCol++;
    			}
    			
    			mapRow++;
    		}
    	}
    	
    	
    	
    	
    	
    	
    	/*
    	else if (state.direction == Enums.Direction.SOUTH) {
        	
    		int x = state.posX - view.length/2;
    		for (int i=view.length-1; i >= 0; i--) {
    			
    			int y = state.posY - view[0].length/2;
    			for (int j=view[0].length-1; j >= 0; j--) {
    			
    				if (x == state.posX && y == state.posY) {
    					map[y][x] = Enums.agentDirection(state.direction);
    				} else {
    					map[y][x] = Enums.charToEnum(view[j][i]);
    				}
    				
    				y++;
    			}
    			
    			x++;
    		}
    	}
    	
    	else if (state.direction == Enums.Direction.EAST) {
        	
    		int x = state.posX - view.length/2;
    		for (int i=0; i < view.length; i++) {
    			
    			int y = state.posY - view[0].length/2;
    			for (int j=view[0].length-1; j >= 0; j--) {
    			
    				if (x == state.posX && y == state.posY) {
    					map[y][x] = Enums.agentDirection(state.direction);
    				} else {
    					map[y][x] = Enums.charToEnum(view[j][i]);
    				}
    				
    				y++;
    			}
    			
    			x++;
    		}
    	}
    	
    	else if (state.direction == Enums.Direction.WEST) {
        	
    		int x = state.posX - view.length/2;
    		for (int i=view.length-1; i >= 0; i--) {
    			
    			int y = state.posY - view[0].length/2;
    			for (int j=view[0].length-1; j >= 0; j--) {
    			
    				if (x == state.posX && y == state.posY) {
    					map[y][x] = Enums.agentDirection(state.direction);
    				} else {
    					map[y][x] = Enums.charToEnum(view[j][i]);
    				}
    				
    				y++;
    			}
    			
    			x++;
    		}
    	}
    	
    	else {
    		System.out.println("Error: Unknown direction of agent when trying to update map");
    	}
		*/
    	// Print the result for testing
    	printMap();
	
    }
    
    // Print a ascii version of the entire map
    public void printMap() {
    
    	// Set all coordinated of the map to UNKNOWN at the start
        for (int i=0; i < map.length; i++) {
            for (int j=0; j < map[0].length; j++) {
            	
            	if (i > 75 && i < 85 && j > 75 && j < 85) {
        			System.out.print(map[i][j] + "  ");	
            	}
            }
            
            if (i > 75 && i < 85) {
            	System.out.println();
            }
        }
    
    }
    
}
