
public class Explore {
	
	// VARIABLES
	private Map m;
	private State s;
	
	private int phase;
	
	private boolean followingWall;
	
	private int turnCount;
	
	private Enums.Direction initialDirection;
	
	// CONSTANTS
	private static final int PLEDGE_PHASE = 1;
	
	
	
	public Explore(Map m, State s) {
		this.m = m;
		this.s = s;
		this.phase = PLEDGE_PHASE;
		this.followingWall = false;
		this.turnCount = 0;
		this.initialDirection = s.direction;
	}
	
	public char run() {
		char move = '-';
		
		if (phase == PLEDGE_PHASE) {
			
			// Move forward if possible
			if (followingWall == false) {
				
				if (this.canMoveForward()) {
					move = moveForward();
				} else {
					
					// When we can't move forward, turn right and start wallflower
					move = turnRight();
					followingWall = true;
				}
				
				
			} else {
			 	
				// Keeping our left hand on the wall...
				
				// Move left if possible
				if (this.canMoveLeft() == true) {
					move = turnLeft();
				} 
				// Move forward if possible
				else if (this.canMoveForward() == true) {
					move = moveForward();
				}
				// Otherwise turn right
				else if (this.canMoveForward() == false) {
					move = turnRight();
				}

				// Stop following the wall when pledge condition is met
				if (s.direction == initialDirection && turnCount == 0) {
					followingWall = false;
				}
				
			}
		}
		
		return move;
	}
	
	private boolean canMoveLeft() {
		boolean retval = false;
		
		// Turn left
		s.turnLeft();
		
		// Check if wall is in front
		Enums.Symbol inFront = this.m.getSymbolAtCoord(s.coordinateInFront());
		if (inFront == Enums.Symbol.WALL || 
				inFront == Enums.Symbol.WATER) {
			retval = true;
		}
		
		if (inFront == Enums.Symbol.DOOR && s.key == true) {
			retval = true;
		}
		
		if (inFront == Enums.Symbol.TREE && s.axe == true) {
			retval = true;
		}
		
		// Turn back afterwards
		s.turnRight();
		
		return retval;
	}
	
	public boolean stillExploring() {
		return true;
	}
	
	private char turnLeft() {
		s.turnLeft();
		this.turnCount--;
		return 'l';
	}
	
	private char turnRight() {
		s.turnRight();
		this.turnCount++;
		return 'r';
	}
	
	private char moveForward() {
		s.moveForward();
		return 'f';
	}
	
	private boolean canMoveForward() {
 		boolean retval = false;
 		
 		Enums.Symbol inFront = this.m.getSymbolAtCoord(s.coordinateInFront());
 		
 		System.out.println("\n\n\n\n\n\n\n\n\nInfront == " + inFront);
 		if (inFront == Enums.Symbol.EMPTY) {
 			retval = true;
 		}
 		
 		if (inFront == Enums.Symbol.DOOR && s.key == true) {
 			retval = true;	
 		}
 		
 		if (inFront == Enums.Symbol.TREE && s.axe == true) {
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