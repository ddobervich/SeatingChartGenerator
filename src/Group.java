import java.util.ArrayList;
import java.util.List;

public class Group {
	private List<Desk> desks;
	private List<Student> students;

	public Group() {
		desks = new ArrayList<Desk>();
		students = new ArrayList<Student>();
	}

	public Group(List<Desk> ds) {
		desks = ds;
		students = new ArrayList<Student>();
		addStudentsFor(desks);
	}

	private void addStudentsFor(List<Desk> desks2) {
		for (Desk d:desks2) {
			if (d.getStudent() != null)
				students.add(d.getStudent());
				d.registerToGroup(this);
		}		
	}

	public void addDesk(Desk d) {
		if (d == null)
			return;

		desks.add(d);
		d.registerToGroup(this);
		if (d.getStudent() != null)
			students.add(d.getStudent());
	}

	public void removeDesk(Desk d) {
		if (d == null)
			return;

		desks.remove(d);
		d.unregisterFromGroup();
		if (d.getStudent() != null)
			students.remove(d.getStudent());
	}

	public List<Desk> getDesks() {
		return desks;
	}

	public List<Student> getStudents() {
		return students;
	}
	
	// returns a score for how many student constraints are violated
/*	public int getScore() {
		int score = 0;
		for (int i = 0; i < students.size(); i++) {
			for (int j = i+1; j < students.size(); j++) {
				Student s = students.get(i);
				Student s2 = students.get(j);
				
				if (s.cannotSitWith(s2)) {
					score += s.getSittingViolation(s2);
				}
			}
		}
		
		return score;
	}

 */
}