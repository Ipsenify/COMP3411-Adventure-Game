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
    public boolean gold;
    
    public Enums.Direction direction;
    
    public Coordinate c;
    
    public int pastCost;
    public int futureCost;
    
    public int moveCount;
    
    public ArrayList<Character> movesMade;
    
    public Map map;
    
    // Creator method
    public State() {

    	this.moveCount = 0;
        this.bombs = 0;
        this.key = false;
        this.axe = false;
        this.gold = false;
        this.direction = Enums.Direction.NORTH;        
        this.c = new Coordinate(Map.MAP_WIDTH/2, Map.MAP_HEIGHT/2);
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
    	//System.out.print("\n Moves Made:");
    	//for(char c : this.movesMade) {
    	//	System.out.print(" " + c);
    	//}
    	System.out.println("MoveCount = " + moveCount);
    }
 
 	// Move the agent forward (relative to their direction)
    // If removable object in front, then remove it before moving forward
    // ASSUMING it is legal to move forward
 	public char moveForward() {
 		
 		Coordinate coordInFront = coordinateInFront();
 		Enums.Symbol symbolInFront = this.map.getSymbolAtCoord(coordInFront);
 		
 		char moveMade = 'f';
 		
 		// IGNORE BOMBS FOR NOW
 		if (symbolInFront == Enums.Symbol.TREE) {
 			movesMade.add('c');
 	 		moveCount++;
 	 		moveMade = 'c';
 		} else if (symbolInFront == Enums.Symbol.DOOR) {
 			movesMade.add('o');
 	 		moveCount++;
 	 		moveMade = 'o';
 		} else {
 			movesMade.add('f');
 	 		moveCount++;
 	 		this.c = coordInFront;
 		}

 		// Pick up items
 		if (symbolInFront == Enums.Symbol.AXE) {
 			this.axe = true;
 		} else if (symbolInFront == Enums.Symbol.BOMB) {
 			this.bombs++;
 		} else if (symbolInFront == Enums.Symbol.KEY) {
 			this.key = true;
 		} else if (symbolInFront == Enums.Symbol.GOLD) {
 			this.gold = true;
 		}
 		
 		this.map.clearLocation(coordInFront);

 		return moveMade;
 	}
 	
 	// Update the direction of the agent when it turns left
 	public void turnLeft() {
 		
 		movesMade.add('l');
 		moveCount++;
 		
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
 		moveCount++;
 		
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
 		moveCount++;
 		this.map.clearLocation(coordinateInFront());
 		this.movesMade.add('o');
 	}
 	public void useBomb() {
 		moveCount++;
 		this.map.clearLocation(coordinateInFront());
 		this.bombs--;
 		this.movesMade.add('b');
 	}
 	public void useAxe() {
 		moveCount++;
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
 		if (canMoveForward() == true) {
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
 	
 	public Coordinate coordinateOnLeft() {
 		Coordinate retval = new Coordinate(this.c.x, this.c.y);
 		
 		if (this.direction == Enums.Direction.NORTH) {
    		retval.x--;
    	} else if (this.direction == Enums.Direction.SOUTH) {
    		retval.x++;
    	} else if (this.direction == Enums.Direction.EAST) {
    		retval.y--;
    	} else if (this.direction == Enums.Direction.WEST) {
    		retval.y++;
    	}
 		
 		return retval;
 	} 
 	
 	public boolean canMoveForward() {
 		Enums.Symbol inFront = this.map.getSymbolAtCoord(this.coordinateInFront());
 		return validMove(inFront);
 	}
 	
 	// Assuming no BOMBS
 	public boolean validMove(Enums.Symbol inFront) {
 		boolean retval = false;
 		
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
 	
 	public char lastMove() {
 		return this.movesMade.get(this.movesMade.size()-1);
 	}
 
}
