/*********************************************
/*  State.java 
/*  
/*  COMP3411 Artificial Intelligence
/*  UNSW Session 1, 2013
*/

import java.util.*;
import java.io.*;
import java.net.*;

import sun.font.LayoutPathImpl.EndType;

public class State {

    // Class variables
    public int bombs;
    
    public boolean key;
    public boolean axe;
    public boolean gold;
    
    public Enums.Direction direction;
    
    public Coordinate c;
    
    public int pastCost;
    public int futureCost;
    
    public ArrayList<Character> movesMade;
    
    public Map map;
    
    // Creator method
    public State() {
            
        this.bombs = 0;
        this.key = false;
        this.axe = false;
        this.gold = false;
        this.direction = Enums.Direction.NORTH;
        this.c.x = Map.MAP_WIDTH/2;
        this.c.y = Map.MAP_HEIGHT/2;
        this.movesMade = new ArrayList<Character>();
        this.map = new Map();
        
    }
    
    public State (State s) {
    	this.axe = s.axe;
    	this.bombs = s.bombs;
    	this.direction = s.direction;
    	this.gold = s.gold;
    	this.key = s.key;
    	this.pastCost = s.pastCost + 1;
    	this.c.x = s.c.x;
    	this.c.y = s.c.y;
    	this.movesMade = new ArrayList<Character>(s.movesMade);
    	this.map = new Map(s.map);
    }
    
    
    // Print method used for debugging
    public void printState() {
    	System.out.println("| bombs = "+ bombs +" | key = "+ key +" | axe = "+ axe +" | direction = "+ direction +" | posX = "+ c.x +" | posY = "+ c.y +" |");
    }
 
 	// Update the position of the agent relative to their direction
 	public void moveForward() {
 		movesMade.add('f');
 		
 		Coordinate coordInFront = coordinateInFront();
 		
 		Enums.Symbol itemInFront = this.map.getSymbolAtCoord(coordInFront);
 		
 		if (itemInFront == Enums.Symbol.AXE) {
 			this.axe = true;
 		} else if (itemInFront == Enums.Symbol.BOMB) {
 			this.bombs++;
 		} else if (itemInFront == Enums.Symbol.KEY) {
 			this.key = true;
 		} else if (itemInFront == Enums.Symbol.GOLD) {
 			this.gold = true;
 		}
 		
 		this.map.clearLocation(coordInFront);
 		
 		this.c = coordInFront;

 	}
 	
 	// Update the direction of the agent when it turns left
 	public void turnLeft() {
 		
 		movesMade.add('l');
 		
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
 		
 		movesMade.add('r');
 		
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
 	
 	// Assume usage is valid
 	public void useKey() {
 		this.map.clearLocation(coordinateInFront());
 		this.movesMade.add('o');
 	}
 	public void useBomb() {
 		this.map.clearLocation(coordinateInFront());
 		this.bombs--;
 		this.movesMade.add('b');
 	}
 	public void useAxe() {
 		this.map.clearLocation(coordinateInFront());
 		this.movesMade.add('c');
 	}
 	
 	public Iterable<State> getChildren() {
 		ArrayList<State> children = new ArrayList<State>();
 		State s;
 		
 		// Left
 		s = new State(this);
 		s.turnLeft();
 		children.add(s);
 		
 		// Right
 		s = new State(this);
 		s.turnRight();
 		children.add(s);
 		
 		Enums.Symbol inFront = this.map.getSymbolAtCoord(coordinateInFront());
 		
 		// Forward
 		if (canMoveForward(inFront) == true) {
 			s = new State(this);
 			s.moveForward();
 			children.add(s);
 		}
 		
 		// Use items if possible
 		switch(inFront) {
 			case DOOR:
 				s = new State(this);
 				if (this.key == true) {
 					s.useKey();
 					children.add(s);
 				} else if (this.bombs > 0) {
 					s.useBomb();
 					children.add(s);
 				}
 				break;
			case TREE:
				s = new State(this);
 				if (this.axe == true) {
 					s.useAxe();
 					children.add(s);
 				} else if (this.bombs > 0) {
 					s.useBomb();
 					children.add(s);
 				}
 				break;
			case WALL:
				s = new State(this);
				if (this.bombs > 0) {
 					s.useBomb();
 					children.add(s);
 				}
				break;
			default:
				break;
			

 		}
 		
 		
 		return children;
 	}
 	
 	public Coordinate coordinateInFront() {
 		Coordinate retval = new Coordinate(this.c.x, this.c.y);
 		
 		if (this.direction == Enums.Direction.NORTH) {
    		retval.y--;
    	} else if (this.direction == Enums.Direction.SOUTH) {
    		retval.y++;
    	} else if (this.direction == Enums.Direction.EAST) {
    		retval.x++;
    	} else if (this.direction == Enums.Direction.WEST) {
    		retval.x--;
    	}
 		
 		return retval;
 	}
 	
 	private boolean canMoveForward(Enums.Symbol inFront) {
 		if (inFront == Enums.Symbol.DOOR || 
 			inFront == Enums.Symbol.TREE || 
 			inFront == Enums.Symbol.WALL ||
 			inFront == Enums.Symbol.WATER) {
 			
 			return false;
 		} else {
 			return true;
 		}
 	}
 	
 	// added for exploring, do we need the above method?????
 	// IGNORE BOMBS FOR NOW
 	public boolean canMoveForward() {
 		boolean retval = false;
 		
 		Enums.Symbol inFront = this.map.getSymbolAtCoord(this.coordinateInFront());
 		
 		if (inFront == Enums.Symbol.EMPTY) {
 			retval = true;
 		}
 		
 		if (inFront == Enums.Symbol.DOOR && this.key == true) {
 			retval = true;	
 		}
 		
 		if (inFront == Enums.Symbol.TREE && this.axe == true) {
 			retval = true;	
 		}
 		
 		if (inFront == Enums.Symbol.AXE || 
 				inFront == Enums.Symbol.KEY || 
 				inFront == Enums.Symbol.BOMB ||
 				inFront == Enums.Symbol.GOLD) {
 			retval = true;
 		}
 		
 		return retval;
 	}
}
