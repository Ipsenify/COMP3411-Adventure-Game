/*********************************************
/*  State.java 
/*  
/*  COMP3411 Artificial Intelligence
/*  UNSW Session 1, 2013
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class State {

    // Class variables
    public int bombs;
    
    public boolean key;
    public boolean axe;
    
    public Enums.Direction direction;
    
    public int posX;
    public int posY;
    
    // Creator method
    public State() {
            
        bombs = 0;
        key = false;
        axe = false;
        direction = Enums.Direction.NORTH;
        posX = Map.MAP_WIDTH/2;
        posY = Map.MAP_HEIGHT/2;        
    }
    
    // Print method used for debugging
    public void printState() {
    	System.out.println("| bombs = "+ bombs +" | key = "+ key +" | axe = "+ axe +" | direction = "+ direction +" | posX = "+ posX +" | posY = "+ posY +" |");
    }
 
 	// Update the position of the agent relative to their direction
 	public void moveForward() {

 		switch (direction) {
 			case NORTH:
 				posY--;
 				break;
 				
 			case SOUTH:
 				posY++;
 				break;
 				
 			case EAST:
 				posX++;
 				break;
 				
 			case WEST:
 				posX--;
 				break;
 		}
 	}
 	
 	// Update the direction of the agent when it turns left
 	public void turnLeft() {
 		
 		switch (direction) {
 			case NORTH:
 				direction = Enums.Direction.WEST;
 				break;
 				
 			case SOUTH:
 				direction = Enums.Direction.EAST;
 				break;
 				
 			case EAST:
 				direction = Enums.Direction.NORTH;
 				break;
 				
 			case WEST:
 				direction = Enums.Direction.SOUTH;
 				break;
 		}
 	}
 
 	// Update the direction of the agent when it turns right
 	public void turnRight() {
 		
 		switch (direction) {
 			case NORTH:
 				direction = Enums.Direction.EAST;
 				break;
 				
 			case SOUTH:
 				direction = Enums.Direction.WEST;
 				break;
 				
 			case EAST:
 				direction = Enums.Direction.SOUTH;
 				break;
 				
 			case WEST:
 				direction = Enums.Direction.NORTH;
 				break;
 		}
 	}
 
}
