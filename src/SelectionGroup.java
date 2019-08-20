import java.util.ArrayList;
import java.util.List;


public class SelectionGroup {
	private List<Desk> desks;
	private int clickedRelX, clickedRelY;
	
	public SelectionGroup() {
		desks = new ArrayList<Desk>();
	}
	
	public SelectionGroup(List<Desk> ds) {
		desks = ds;
		setAllSelected();
	}
	
	public void setAllSelected() {
		for (Desk d:desks) {
			d.setHighlighted(true);
		}
	}
	
	public void deHighlightAll() {
		for (Desk d:desks) {
			d.setHighlighted(false);
		}
	}

	public void addDesk(Desk d) {
		desks.add(d);
		d.setHighlighted(true);
	}
	
	public void clear() {
		deHighlightAll();
		desks.clear();
	}
	
	public void remove(Desk d) {
		desks.remove(d);
		d.setHighlighted(false);
	}
	
	public void moveTo(int dx, int dy) {
		
	}

	public List<Desk> getDesks() {
		return desks;
	}

	public void move(int i, int j) {
		for (Desk d:desks) {
			d.setX(d.getX()+i);
			d.setY(d.getY()+j);
		}
	}
}
