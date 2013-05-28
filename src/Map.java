/*********************************************
/*  Map.java 
/*  
/*  COMP3411 Artificial Intelligence
/*  UNSW Session 1, 2013
*/

import java.util.*;
import java.io.*;
import java.lang.management.MemoryType;
import java.net.*;

public class Map {
    
    // Class constants
    public static final int MAP_WIDTH = 160;
    public static final int MAP_HEIGHT = 160;

    // Class variables
    //private Enums.Symbol[][] map;
    private Point[][] map;
    
    // Creator method
    public Map() {
        map = new Point[MAP_WIDTH][MAP_HEIGHT];

        // Set all coordinated of the map to UNKNOWN at the start
        for (int i=0; i < MAP_WIDTH; i++) {
            for (int j=0; j < MAP_HEIGHT; j++) {
            
            	map[i][j] = new Point(i, j, Enums.Symbol.UNKNOWN);
            }
        }
        
    }
    
    public Map(Map m) {
    	 for (int i=0; i < MAP_WIDTH; i++) {
             for (int j=0; j < MAP_HEIGHT; j++) {
             
             	this.map[i][j] = m.map[i][j];
             }
         }
    }
    
    // Update the explored map given a new view that has been explored
    // A state is passed in to find the direction of the agent
    // We assume the given view is a 5x5 square with the agent in the middle
    public void updateMap(char view[][], State state) {
    
    	//state.printState();
    
    	// TODO Might not have ifs for all directions (just can't be bothered thinking of
    	// indices' hax right now).
    	if (state.direction == Enums.Direction.NORTH) {
    	
    		int x_m = state.c.x - view.length/2;
    		for (int x_v=0; x_v < view.length; x_v++) {
    			
    			int y_m = state.c.y - view[0].length/2;
    			
    			for (int y_v=0; y_v < view[0].length; y_v++) {
    				
    				if (x_m == state.c.x && y_m == state.c.y) {
    					map[x_m][y_m].symbol = Enums.agentDirection(state.direction);
    				} else {	
    					map[x_m][y_m].symbol = Enums.charToEnum(view[y_v][x_v]);
    				}
    				
    				y_m++;
    			}
    			
    			x_m++;
    		}
    	} else if (state.direction == Enums.Direction.SOUTH) {
    	
    		int x_m = state.c.x - view.length/2;
    		for (int x_v=view.length-1; x_v >= 0; x_v--) {
    			
    			int y_m = state.c.y - view[0].length/2;
    			for (int y_v=view[0].length-1; y_v >= 0; y_v--) {
    			
    				if (x_m == state.c.x && y_m == state.c.y) {
    					map[x_m][y_m].symbol = Enums.agentDirection(state.direction);
    				} else {	
    					map[x_m][y_m].symbol = Enums.charToEnum(view[y_v][x_v]);
    				}
    				
    				y_m++;
    			}
    			
    			x_m++;
    		}
    	} else if (state.direction == Enums.Direction.EAST) {
    	
    		int x_m = state.c.x - view.length/2;
    		
			for (int y_v=view[0].length-1; y_v >= 0; y_v--) {
    			
    			int y_m = state.c.y - view[0].length/2;
				for (int x_v=0; x_v < view.length; x_v++) {
    				if (x_m == state.c.x && y_m == state.c.y) {
    					map[x_m][y_m].symbol = Enums.agentDirection(state.direction);
    				} else {	
    					map[x_m][y_m].symbol = Enums.charToEnum(view[y_v][x_v]);
    				}
    				
    				y_m++;
    			}
    			
    			x_m++;
    		}
    	} else if (state.direction == Enums.Direction.WEST) {
    	
    		int x_m = state.c.x - view.length/2;
    		for (int y_v=0; y_v < view[0].length; y_v++) {
    		
    			
    			int y_m = state.c.y - view[0].length/2;
    			for (int x_v=view.length-1; x_v >= 0; x_v--) {
    				if (x_m == state.c.x && y_m == state.c.y) {
    					map[x_m][y_m].symbol = Enums.agentDirection(state.direction);
    				} else {	
    					map[x_m][y_m].symbol = Enums.charToEnum(view[y_v][x_v]);
    				}
    				
    				y_m++;
    			}
    			
    			x_m++;
    		}
    	}
    }
    
    public void clearLocation(Coordinate c) {
    	this.map[c.x][c.y].symbol = Enums.Symbol.EMPTY;
    }
    
    public Enums.Symbol getSymbolAtCoord(Coordinate c) {
    	return this.map[c.x][c.y].symbol;
    }
    
    // Print a ascii version of the entire map
    public void printMap() {
    
    	// Set all coordinated of the map to UNKNOWN at the start
        for (int y=70; y < 90; y++) {
            for (int x=70; x < 90; x++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
    
    }
    
    // Return a coordinate that is surrounded by the most UNKNOWN symbols
    // Used for exploring stage
    public Coordinate findMostUnknowns() {
    	
    	Coordinate retval = null;
    	int maxCount = 0;
    	
    	// For each UNKNOWN point on the map...
    	for (int i=0; i < MAP_WIDTH; i++) {
            for (int j=0; j < MAP_HEIGHT; j++) {
            
            	if (map[i][j].symbol == Enums.Symbol.UNKNOWN) {
            	
	            	// Count the number of UNKOWNS: 
	            	int tempCount = 0;
	            	boolean valid = true;
            		int k;
            		
	            	// ABOVE
	            	for (k=j; k >= 0 && map[i][k].symbol == Enums.Symbol.UNKNOWN; k--) {
	            		tempCount++;
	            	}
	            	if (k == 0) {
	            		valid = false;
	            	}
	            	
	            		
	            	// BELOW
	            	for (k=j; k < MAP_HEIGHT && map[i][k].symbol == Enums.Symbol.UNKNOWN; k++) {
	            		tempCount++;
	            	}
	            	if (k == MAP_HEIGHT) {
	            		valid = false;
	            	}
	            	
	            	// LEFT
	            	for (k=i; k >= 0 && map[k][j].symbol == Enums.Symbol.UNKNOWN; k--) {
	            		tempCount++;
	            	}
	            	if (k == 0) {
	            		valid = false;
	            	}
	            		
	            	// RIGHT
	            	for (k=i; k < MAP_WIDTH && map[k][j].symbol == Enums.Symbol.UNKNOWN; k++) {
	            		tempCount++;
	            	}
	            	if (k == MAP_WIDTH) { 
	            		valid = false;
	            	}
	            	
	            	// Update the new max count
	            	if (valid == true && tempCount > maxCount) {
	            		maxCount = tempCount;
	            		retval = map[i][j].convertToCoord();
	            	}
            		
            	} else {
            		// DO NOTHING
            	}
            }
        }
    	
    	return retval;
    }
    
}
