import java.util.*;


public class Path {
	
	PriorityQueue<State> pq;
	Point goal;
	
	
	public Path(State initialState, Point goal) {
		this.pq = new PriorityQueue<State>();
		this.goal = goal;
	}
	
	// Return the list of actions required to reach the goal
	public ArrayList<char> movesToPoint() {
		
	}
	
	// Return the items required to reach the goal
	public ArrayList<Enums.Symbol> itemsToPoint() {
		
	}
}
