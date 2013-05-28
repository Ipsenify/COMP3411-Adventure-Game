
public class Point {

	public int x;
	public int y;
	
	Enums.Symbol symbol;
	
	public Point (int x, int y, Enums.Symbol symbol) {
		this.x = x;
		this.y = y;
		this.symbol = symbol;
	}
	
	// Convert the Point into a Coordinate type
	public Coordinate convertToCoord() {
		Coordinate coord = new Coordinate(this.x, this.y);
		return coord;
	}
}
