import java.util.ArrayList;
import java.util.List;


public class Region {
	// (x,y) must be upper left, (x2, y2) must be lower right
	private int x, y, x2, y2;
	
	public Region(int x, int y, int x2, int y2) {
		int upperLeftX = Math.min(x,  x2);
		int lowerRightX = Math.max(x, x2);
		int upperLeftY = Math.min(y, y2);
		int lowerRightY = Math.max(y, y2);
		
		this.x = upperLeftX;
		this.y = upperLeftY;
		this.x2 = lowerRightX;
		this.y2 = lowerRightY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public List<Desk> getDesksInRegion(List<Desk> desks) {
		ArrayList<Desk> toReturn = new ArrayList<Desk>();
		for (Desk d:desks) {
			if (d.inRegion(this)) {
				toReturn.add(d);
			}
		}
		
		return toReturn;
	}
}
