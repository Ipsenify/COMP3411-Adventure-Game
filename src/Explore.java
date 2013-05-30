import java.util.ArrayList;


public class Explore {
	
	// VARIABLES
	private Map m;
	private State s;
	
	private int phase;
	
	private boolean followingWall;
	
	private int turnCount;
	
	private Enums.Direction initialDirection;
	
	private Coordinate c1;
	private Coordinate c2;
	
	private ArrayList<Coordinate> coordinatesSeen;
	// CONSTANTS
	private static final int PLEDGE_PHASE = 1;
	private static final int CENTRE_PHASE = 2;
	
	
	
	public Explore(State s) {
		this.m = s.map;
		this.s = s;
		this.phase = PLEDGE_PHASE;
		this.followingWall = false;
		this.turnCount = 0;
		this.initialDirection = s.direction;
		this.coordinatesSeen = new ArrayList<Coordinate>();
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
					// Set the initial Points of contact with object
					// c2 will be set when the agent moves
					c1 = s.c;
					c2 = null;
				}
				
				
			} else {
			 	
				// Keeping our left hand on the wall...
				
				// Move left if possible
				if (this.canMoveLeft() == true && s.movesMade.get(s.movesMade.size()-1) != 'l') {
					move = turnLeft();
				} 
				// Move forward if possible
				else if (s.canMoveForward() == true) {
					move = moveForward();
					// Update c2
					if (c2 == null) {
						c2 = s.c;
//						System.out.println("\n\n\n Update C2!!! \n\n\n");
//						try {
//							Thread.sleep(4000);
//						} catch (Exception e) {
//							
//						}
					} else if (c1.equals(coordinatesSeen.get(coordinatesSeen.size()-2)) &&
								c2.equals(coordinatesSeen.get(coordinatesSeen.size()-1))) {
						// Stop Pledge
						phase = CENTRE_PHASE;
					}
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
//					System.out.println("\n\n\n Not following wall!!! \n\n\n");
//					try {
//						Thread.sleep(4000);
//					} catch (Exception e) {
//						
//					}
				}
				
			}
		} else if (phase == CENTRE_PHASE) {
			System.out.println("\n\n\n Centre Phase!!! \n\n\n");
			try {
				Thread.sleep(999999);
			} catch (Exception e) {
				
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
		coordinatesSeen.add(s.c);
		return 'f';
	}
}