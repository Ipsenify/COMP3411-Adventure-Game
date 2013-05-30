import java.util.ArrayList;


public class Explore {
	
	// VARIABLES
	private Map m;
	private State s;
	
	private int phase;
	
	private boolean followingWall;
	
	private int turnCount;
	
	private Enums.Direction initialDirection;
	
	private boolean addContactPoints;
	private ArrayList<ContactPoints> contactPoints;
	
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
		
		this.addContactPoints = false;
		this.contactPoints = new ArrayList<ContactPoints>();
	}
	
	public char run() {
		char move = '?';	// Should never happen
		
		if (phase == PLEDGE_PHASE) {
			
			// Move forward if possible
			if (followingWall == false) {
				
				if (s.canMoveForward()) {
					move = moveForward();
				} else {
					
					// When we can't move forward, turn right and start wall flower
					move = turnRight();
					followingWall = true;
					
					// Add new contact points when setting followingWall
					this.addContactPoints = true;
				}
				
				
			} else {
			 	
				// Keeping our left hand on the wall...
				
				// Move left if possible
				if (s.lastMove() == 'o' || s.lastMove() == 'c') {
					move = moveForward();
				} else if (this.canMoveLeft() == true && s.lastMove() != 'l') {
					move = turnLeft();
				} 
				// Move forward if possible
				else if (s.canMoveForward() == true) {
					move = moveForward();
					// Update c2
					if (addContactPoints == true) {
						
						ContactPoints cps = new ContactPoints(coordinatesSeen);
						contactPoints.add(cps);
						addContactPoints = false;
						
					} else if (compareContactPoints()) {
						
						// Stop Pledge
						phase = CENTRE_PHASE;
					}
				}
				// Otherwise turn right
				else {
					move = turnRight();
				}

				// Stop following the wall when pledge condition is met
				if (s.direction == initialDirection && turnCount == 0) {
					followingWall = false;
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
	
	// Check that the last two points visited have been
	// visited previously 
	private boolean compareContactPoints() {
		boolean retval = false;
		
		for (ContactPoints cps : contactPoints) {
			if (cps.compare(coordinatesSeen)) {
				retval = true;
			}
		}
		
		return retval;
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
		char retval = s.moveForward();
		
		// Only update the explored coordinates if we actually move forward
		if (retval == 'f') {
			coordinatesSeen.add(s.c);
		} else {
			addContactPoints = true;
			
			// TODO check
			contactPoints.clear();
		}
		
		return retval;
	}
}