import java.util.*;


public class Path {
	PriorityQueue<State> pq;
	Coordinate goal;
	
	
	public Path(State initialState, Coordinate goal) {
		this.pq = new PriorityQueue<State>();
		this.goal = goal;
	}
	
	
}
