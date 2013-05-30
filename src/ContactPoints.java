import java.util.ArrayList;


public class ContactPoints {
	public Coordinate c1;
	public Coordinate c2;
	
	public ContactPoints(ArrayList<Coordinate> c) {
		this.c1 = c.get(c.size() - 2);
		this.c2 = c.get(c.size() - 1);
	}
	
	public boolean compare(ArrayList<Coordinate> c) {
		boolean retval = false;
		
		if (this.c1 == c.get(c.size() - 2) &&
				this.c2 == c.get(c.size() - 1)) {
			retval = true;
		}
		
		return retval;
	}
}
