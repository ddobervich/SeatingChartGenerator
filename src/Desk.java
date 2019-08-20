import java.util.ArrayList;

import processing.core.PApplet;

public class Desk implements Cloneable {
	public static final int DEFAULT_WIDTH = 100;
	public static final int DEFAULT_HEIGHT = 70;
	private static final int MAX_BOXES = 6;
	private static final int TEXT_SIZE = 16;

	private int x, y, w = DEFAULT_WIDTH, h = DEFAULT_HEIGHT;
	private Student student;
	private boolean highlighted = false;
	private boolean selected;
	private int clickedRelX;
	private int clickedrelY;
	Group group;
	
	// DOES THIS WORK??
	@ Override
	public Desk clone() {
		Desk d = new Desk(x, y, w, h);
		d.student = student;
		d.highlighted = highlighted;
		d.selected = selected;
		d.group = group;
		
		return d;
	}

	public Desk(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Desk(int x, int y, int w, int h, Student student) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.student = student;
	}

	public Desk(int x, int y, Student student) {
		this.x = x;
		this.y = y;
		this.student = student;
	}

	public Desk(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void drawSelf(Main p, boolean reverse) {
		p.fill(255);
		if (highlighted)
			p.stroke(p.HIGHLIGHTED_COLOR);
		else
			p.stroke(0);

		if (!reverse) {
			p.rect(x, y, w, h);
		} else {
			p.rect(p.width - x - w, y, w, h);
		}

		float dx = (float)w/MAX_BOXES;
		float dy = dx;
		for (int i = 0; i < MAX_BOXES; i++) {
			if (!reverse) {
				p.rect(x + i * dx, y + h - dy, dx, dy);
			} else {
				p.rect((p.width - x - w) + i*dx, y + h - dy, dx, dy);
			}
		}
		
		
		p.stroke(100); // text color to black
		p.fill(100);
		p.textSize(TEXT_SIZE);
		if (student != null && student.getName() != null) {
			if (!reverse) {
				p.text(student.getName(), x + w / 4, y + h / 2);
			} else {
				p.text(student.getName(), (p.width - x - w) + w / 4, y + h / 2);
			}
		}
	}

	public void setName(String newName) {
		if (student != null)
			student.setName(newName);
		else
			student = new Student(newName);
	}

	public boolean containsPoint(int px, int py) {
		return ((px >= x) && (px <= x + w) && (py >= y) && (py <= y + h));
	}

	public void setHighlighted(boolean b) {
		highlighted = b;
	}

	public void setSelected(int mouseX, int mouseY) {
		selected = true;
		setHighlighted(true);
		clickedRelX = mouseX - x;
		clickedrelY = mouseY - y;
	}

	public void setUnSelected() {
		selected = false;
		setHighlighted(false);
	}

	public int getClickedRelX() {
		return clickedRelX;
	}

	public int getClickedRelY() {
		return clickedrelY;
	}

	public boolean inRegion(Region r) {
		int cx = (x + (x + w)) / 2; // x coord of desk center
		int cy = (y + (y + h)) / 2; // y coord of desk center

		return (cx > r.getX()) && (cy > r.getY()) && (cx < r.getX2()) && (cy < r.getY2());
	}
	
	public void registerToGroup(Group g) {
		group = g;
	}
	
	public void unregisterFromGroup() {
		group = null;
	}

	// TODO: add reverse to this!!
	public void drawSelf(Main window, int color) {
		window.fill(color);
		window.rect(x, y, w, h);
		window.stroke(100); // text color to black
		window.fill(100);

		float dx = (float)w/MAX_BOXES;
		float dy = dx;
		for (int i = 0; i < MAX_BOXES; i++) {
			window.rect(x+i*dx, y+h-2*dy, dx, dy);
			window.rect(x+i*dx, y+h-dy, dx, dy);
		}
		
		if (student != null && student.getName() != null)
			window.text(student.getName(), x + w / 4, y + h / 2);
	}
}