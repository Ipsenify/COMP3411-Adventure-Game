
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
	
	
	
	public Explore(State s) {
		this.m = s.map;
		this.s = s;
		this.phase = PLEDGE_PHASE;
		this.followingWall = false;
		this.turnCount = 0;
		this.initialDirection = s.direction;
	}
	
	public char run() {
		char move = '?';	// Should never happen
		
		if (phase == PLEDGE_PHASE) {
			
			// Move forward if possible
			if (followingWall == false) {
				
				if (s.canMoveForward()) {
					move = moveForward();
				} else {
					
					// When we can't move forward, turn right and start wallflower
					move = turnRight();
					followingWall = true;
				}
				
				
			} else {
			 	
				// Keeping our left hand on the wall...
				
				// Move left if possible
				
				if (this.canMoveLeft() == true && s.movesMade.get(s.movesMade.size()-1) != 'l') {
					System.out.println("Turning Left!!!");
					move = turnLeft();
				} 
				// Move forward if possible
				else if (s.canMoveForward() == true) {
					move = moveForward();
				}
				// Otherwise turn right
				else {
					move = turnRight();
				}
				
				/*if (this.canMoveLeft() == false && s.canMoveForward() == false) {
					move = turnRight();
				} else if (this.canMoveLeft() == false && s.canMoveForward() == true) {
					move = moveForward();
				} else if (this.canMoveLeft() == true && s.canMoveForward() == true) {
					System.out.println(s.lastMove());
					if (s.lastMove() == 'l') {
						move = moveForward();
					} else {
						move = turnLeft();
					}
				}*/
					
				// Stop following the wall when pledge condition is met
				if (s.direction == initialDirection && turnCount == 0) {
					followingWall = false;
				}
				
			}
		}
		
		return move;
	}
	
	private boolean canMoveLeft() {
		
		// Look to the left
		Enums.Symbol onLeft = s.map.getSymbolAtCoord(s.coordinateOnLeft());
		
		// See if we can go there
		return s.validMove(onLeft);
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
}