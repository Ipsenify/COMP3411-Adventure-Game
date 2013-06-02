import java.util.*;


public class Path {
	
	private PriorityQueue<State> openSet;
	private ArrayList<State> closedSet;
	private Point goal;
	private State initialState;
	
	private boolean firstCall;
	private int bombsAdded;
	
	public Path(State initialState, Point goal) {
		this.openSet = new PriorityQueue<State>();
		this.goal = goal;
		this.initialState = initialState;
		initialState.movesMade.clear();
		this.closedSet = new ArrayList<State>();
		
		initialState.calculateFutureCost(goal);
		openSet.add(initialState);
		
		this.firstCall = true;
		this.bombsAdded = 0;
	}
	
	// Return the list of actions required to reach the goal
	public ArrayList<Character> movesToPoint() {
		
		State current;

		while (this.openSet.size() >= 0) {
			current = this.openSet.poll();
			
//			System.out.println("\nPARENT STATE:");
//			current.printState();
//			current.map.printMap(current.c, current.direction);
//			
//			try {
// 				Thread.sleep(4000);
// 			} catch (Exception e) {
// 				
// 			}
			
			closedSet.add(current);
			
			// Current = Goal
			if (current.c.x == goal.x && current.c.y == goal.y) {
//				System.out.println("\nGOAL FOUND!!!!");
//				current.printState();
//				current.map.printMap(current.c, current.direction);
//				
//				try {
//	 				Thread.sleep(4000);
//	 			} catch (Exception e) {
//	 				
//	 			}

				return current.movesMade;
			}
			
			ArrayList<State> children = current.getChildren(goal);
			
//			System.out.println("\n\nChildren list size = "+children.size());
			
			for (State child : children) {
				
//				child.printState();
////				child.map.printMap(child.c, child.direction);
//				
//				try {
//	 				Thread.sleep(2000);
//	 			} catch (Exception e) {
//	 				
//	 			}

				if (listContains(closedSet, child) || listContains(openSet, child)) {
					//Do Nothing
				} else {
//					child.printState();
//					child.map.printMap(child.c, child.direction);
//					
//					try {
//		 				Thread.sleep(2000);
//		 			} catch (Exception e) {
//		 				
//		 			}
					this.openSet.offer(child);
				}
			}
			
			
		}
		
		return null;
	}
	
	private boolean listContains(Iterable<State> l, State s) {
		for (State cur : l) {
			if (cur.isEqual(s)) {
				return true;
			}
		}
		return false;
	}
	
	// Return the items required to reach the goal
	// Given a state of already held items (after exploring)
	// and a list of items available on the map
	public ArrayList<Enums.Symbol> itemsToPointRequired(ArrayList<Point> itemsAvailable) {
		
			/*
			 * A* to the goal point
			 * For each child generated to reach the goal, record the cost of getting there
			 * If an item is required we FIRST check that we NEED that item (we may already have it)
			 * If we don't already have the item, add this to the ITEMS REQUIRED LIST that we will return
			 * Continue until we have found the lowest cost path to the goal and return all the
			 * items (we don't already have) needed for this path
			 */
			
		if (firstCall == true) {
			
			// Give the items available to the initial state
			for (Point p : itemsAvailable) {
				if (p.symbol == Enums.Symbol.AXE) {
					this.initialState.axe = true;
					
				} else if (p.symbol == Enums.Symbol.BOMB) {
					bombsAdded++;
					this.initialState.bombs++;
					
				} else if (p.symbol == Enums.Symbol.KEY) {
					this.initialState.key = true;
				}
			}
			// Now we can assume that initialState has all the items it might require
			firstCall = false;
		}	
		
		// Determine the set of moves required to get to the goal
		ArrayList<Character> moves = movesToPoint();
		
		if (moves == null) {
			return null;
		}
		
		// Determine which items were required for the given list of moves
		ArrayList<Enums.Symbol> itemsRequired = new ArrayList<Enums.Symbol>();
		for (Character c : moves) {
			
			if (c == 'c' && containsItem(Enums.Symbol.AXE, itemsAvailable)) {
				itemsRequired.add(Enums.Symbol.AXE);
				
			} else if (c == 'o' && containsItem(Enums.Symbol.KEY, itemsAvailable)) {
				itemsRequired.add(Enums.Symbol.KEY);
				
			} else if (c == 'b') {
				if (bombsAdded != 0) {		// Assumes number of bombs used is less than bombs available
					itemsRequired.add(Enums.Symbol.BOMB);
					bombsAdded--;
				}
			}
		}
		
		return itemsRequired;
	}
	
	// Return whether a given symbol is in an arraylist of points
	private boolean containsItem(Enums.Symbol s, ArrayList<Point> list) {
		for (Point p : list) {
			if (p.symbol == s) {
				return true;
			}
		}
		return false;
	}
}
