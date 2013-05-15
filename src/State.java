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
 
 	// Update the position of the agent relative to their direction
 	public void moveForward() {

 		switch (direction) {
 			case NORTH:
 				posY++;
 				break;
 				
 			case SOUTH:
 				posY--;
 				break;
 				
 			case EAST:
 				posX++;
 				break;
 				
 			case WEST:
 				posX--;
 				break;
 		}
 	}
 	
 
}
