import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import processing.core.PApplet;

public class Main extends PApplet {
	/*
	 * TODO:  
	 * Allow a pasted list of students into a box
	 * as the input.  
	 * 
	 * Allow a way to save the seating positions.
	 * 
	 * Allow a way to specify that two students can't
	 * sit in the same group
	 * 
	 * Allow a way to mark one person per group as
	 * the group lead.  Group lead is assigned to whoever
	 * has been lead least often in the past, or randomly.
	 * 
	 */
	public int HIGHLIGHTED_COLOR = 0;
	public int TRANSPARENT = 0;
	private int LIGHT_GREY;
	private int TEMP_HIGHLIGHT = 0;
	private boolean reverse = false;	// should display horiztonally reversed for projection?

	SeatingChart chart;
	SelectionGroup selectedDesks;

	boolean snapToGrid = false;
	int gridSize = 10;

	Region region;
	Desk selectedDesk;
	String[] randomNames = { "bill", "joel", "karl", "mcnugget" };

	public static final int DEFAULT = 0;
	public static final int RENAME_STUDENT = 1;
	int mode = DEFAULT;

	private int regionX;
	private int regionY;
	private boolean regionSelectStarted;
	private int prevMouseX;
	private int prevMouseY;
	private QueryBox queryBox;

	private boolean FULLSCREEN = false;

	public void settings() {
		if (FULLSCREEN)
			fullScreen();
		else
			size(1024, 600);

		queryBox = new QueryBox(0, height - 50, width, 50);
	}

	public void setup() {
		chart = new SeatingChart();
		
		HIGHLIGHTED_COLOR = color(0, 255, 0);
		TRANSPARENT = color(255, 255, 255, 0);
		LIGHT_GREY = color(200, 200, 200);
		TEMP_HIGHLIGHT = color(0, 200, 100);
		
		roomA214Init();
		
		//List<Student> students = chart.readStudentsFromFile("../block7.txt");
		//System.out.println(students.size());
		//chart.randomlyAssignStudents(students);
	}

	public void draw() {
		background(255);
		textSize(16);

		if (snapToGrid)
			displayGrid(reverse);

		for (Desk d : chart.getDesks()) {
			if (d.containsPoint(mouseX, mouseY))
				d.drawSelf(this, TEMP_HIGHLIGHT);
			else
				d.drawSelf(this, reverse);
		}

		if (regionSelectStarted) {
			fill(TRANSPARENT);
			stroke(0);
			rect(regionX, regionY, mouseX - regionX, mouseY - regionY);
		}

		queryBox.draw(this);
		if (reverse) {
			textSize(32);
			text("reverse display", width - 100, height - 30);
		}
	}

	private void displayGrid(boolean reverse) {
		stroke(LIGHT_GREY);
		for (int x = 0; x < width; x += gridSize) {
			if (!reverse) {
				line(x, 0, x, height);
			} else  {
				line(width - x, 0, width - x, height);
			}
		}

		for (int y = 0; y < height; y += gridSize) {
			line(0, y, width, y);
		}
	}

	private void selectDesk(int x, int y) {
		Desk desk = getDeskAt(x, y);
		if (desk == null)
			return;
		selectedDesk = desk;
		selectedDesk.setSelected(mouseX, mouseY);
	}

	public void mousePressed() {
		prevMouseX = mouseX;
		prevMouseY = mouseY;

		regionSelectStarted = false;
		regionX = Integer.MIN_VALUE;
		regionY = Integer.MIN_VALUE;

		if (mouseButton == RIGHT) {
			regionX = mouseX;
			regionY = mouseY;
			regionSelectStarted = true;
		} else if (mouseEvent.getClickCount() == 2)
			handleDoubleClick();
		else {
			if (selectedDesks == null)
				selectDesk(mouseX, mouseY);
		}
	}

	public void mouseClicked() {
		selectDesk(mouseX, mouseY);
	}

	public void mouseReleased() {
		if (selectedDesk != null)
			selectedDesk.setUnSelected();
		selectedDesk = null; // if we were dragging, set back to null;

		if (selectedDesks != null) {
			selectedDesks.deHighlightAll();
			selectedDesks = null;
		}

		if (regionSelectStarted) {
			region = new Region(regionX, regionY, mouseX, mouseY);

			List<Desk> ds = region.getDesksInRegion(chart.getDesks());
			this.selectedDesks = new SelectionGroup(ds);
			region = null;
			regionSelectStarted = false;
		}
	}

	public void mouseDragged() {
		if (selectedDesk != null) {
			selectedDesk.setX(snap(mouseX - selectedDesk.getClickedRelX()));
			selectedDesk.setY(snap(mouseY - selectedDesk.getClickedRelY()));
		}

		if (selectedDesks != null) {
			selectedDesks.move(mouseX - prevMouseX, mouseY - prevMouseY);
			prevMouseX = mouseX;
			prevMouseY = mouseY;
		}
	}

	private void handleDoubleClick() {
		selectDesk(mouseX, mouseY);
		if (selectedDesk != null) {
			// TODO: Run in separate thread so highlighting occurs
			selectedDesk.setName(JOptionPane
					.showInputDialog("Enter a new name"));
			selectedDesk.setHighlighted(false);
			selectedDesk = null;
		}
	}

	// Returns desk at (x, y) coords or null if there is no desk
	private Desk getDeskAt(int x, int y) {
		for (Desk d : chart.getDesks()) {
			if (d.containsPoint(x, y))
				return d;
		}

		return null;
	}

	private void randomInit() {
		for (int i = 0; i < 5; i++) {
			Student s = new Student(
					randomNames[(int) (Math.random() * randomNames.length)]);

			chart.addDesk(new Desk(snap(i * 80), snap(100), s));
		}
	}

	/*
	 * 'n' 'a' to add desk 'd' to delete a selected desk 'g' to make selected
	 * desks a group 's' to toggle the grid '+' / '-' to change grid size
	 */
	public void keyReleased() {
		queryBox.addLetter(""+key);

		if (!queryBox.isCurrentDone()) return;
		String command = queryBox.getLastFullQuery();

		if (command.equals("add")) { // add a new desk
			chart.addDesk(new Desk(snap(width / 2), snap(height / 2)));
		}

		if (command.equals("delete")) { // delete a desk
			if (selectedDesk != null)
				chart.removeDesk(selectedDesk);
		}

		if (command.equals("g")) {
			if (selectedDesks != null) {
				System.out.println("making group");
				chart.addGroup(new Group(selectedDesks.getDesks()));
				selectedDesks.deHighlightAll();
				selectedDesks = null;
			}
		}

		if (key == '+') {
			gridSize += 5;
		}

		if (key == '-') {
			gridSize -= 5;
		}

		if (command.equals("snap")) {
			snapToGrid = !snapToGrid;
		}
		
		if (command.equals("save")) {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION ) {
				File f = fileChooser.getSelectedFile();
				chart.saveDeskLayoutToFile(f.getAbsolutePath());
				JOptionPane.showMessageDialog(null, "Saved to " + f.getAbsolutePath());
			}
		}
		
		if (command.equals("load")) {
			JFileChooser fileChooser = new JFileChooser();
			File workingDirectory = new File(System.getProperty("user.dir"));
			fileChooser.setCurrentDirectory(workingDirectory);
			int result = fileChooser.showDialog(null, "load");
			if (result == JFileChooser.APPROVE_OPTION ) {
				File f = fileChooser.getSelectedFile();
				chart.loadDeskLayoutFromFile(f.getAbsolutePath());
				JOptionPane.showMessageDialog(null, "Loaded from " + f.getAbsolutePath());
			}
		}

		if (command.equals("save image")) {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION ) {
				File f = fileChooser.getSelectedFile();
				save(f.getAbsolutePath());
				JOptionPane.showMessageDialog(null, "Saved image to " + f.getAbsolutePath());
			}
		}
		
		if (command.equals("random")) {
			List<Student> students = chart.getStudents();
			chart.randomlyAssignStudents(students);
		}

		if (command.equals("r")) {
			reverse = !reverse;
		}
		
		if (key == CODED) {
			if (keyCode == ESC) {
				queryBox.clear();
				if (this.selectedDesks != null) {
					selectedDesks.deHighlightAll();
					selectedDesks = null;
				}
			}
		}
	}

	public int snap(int coord) {
		if (snapToGrid) {
			return gridSize * (coord / gridSize);
		}
		return coord;
	}

	private void room127Init() {
		chart.addDesks(chart.createDeskArray(50, 50, 5, 2));
		chart.addDesks(chart.createDeskArray(250, 50, 5, 3));
		chart.addDesks(chart.createDeskArray(500, 50, 5, 2));
	}
	
	private void room129Init() {
		chart.addDesks(chart.createDeskArray(50, 50, 6, 3));
		chart.addDesks(chart.createDeskArray(350, 50, 6, 4));
	}
	
	private void roomA214Init() {
		chart.addDesks( chart.createDeskPairsArray(60, 10, 3, 3, 400, 500) );
		chart.addDesks( chart.createDeskPairsArray(600, 10, 3, 3, 400, 500) );
	}

	public static void main(String[] args) {
		PApplet.main("Main");
	}
}
