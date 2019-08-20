import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SeatingChart {
    ArrayList<Group> groups;
    ArrayList<Desk> desks;

    public SeatingChart() {
        groups = new ArrayList<Group>();
        desks = new ArrayList<Desk>();
    }

    public void addDesks(List<Desk> newdesks) {
        desks.addAll(newdesks);
    }

    public ArrayList<Desk> createDeskArray(int x, int y, int rows, int cols) {
        ArrayList<Desk> newDesks = new ArrayList<Desk>();

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                desks.add(new Desk(x + i * Desk.DEFAULT_WIDTH, y + j
                        * (Desk.DEFAULT_HEIGHT + 10)));
            }
        }

        return newDesks;
    }

    public ArrayList<Desk> createDeskPairsArray(int x, int y, int rows, int cols, int width, int height) {
        ArrayList<Desk> newDesks = new ArrayList<Desk>();

        int dy = height / rows;
        int dx = width / cols;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                desks.add(new Desk(x + i * dx, y + j * dy));
                desks.add(new Desk(x + i * dx, y + j * dy + Desk.DEFAULT_HEIGHT));
            }
        }

        return newDesks;
    }

    public void randomlyAssignStudents(List<Student> students) {
        if (desks.size() < students.size()) {
            System.err.println("there are: " + desks.size() + " desks and " + students.size() + " students");
            return;
        }

        Collections.shuffle(students);
        Collections.shuffle(desks);

        for (int i = 0; i < students.size(); i++) {
            desks.get(i).setStudent(students.get(i));
        }
    }

    // count how many students are sitting with people they shouldn't be
  /*  public int getScore() {
        int score = 0;

        for (Group group : groups) {
            List<Student> students = group.getStudents();

            for (int i = 0; i < students.size(); i++) {
                for (int j = i + 1; j < students.size(); j++) {
                    if (students.get(i).cannotSitWith(students.get(j))) score++;
                }
            }
        }

        return score;
    }
    */

    public ArrayList<Student> readStudentsFromFile(String filepath) {
        ArrayList<Student> students = new ArrayList<Student>();

        try {
            Scanner fileReader = new Scanner(new File(filepath));
            fileReader.useDelimiter("\\n");
            while (fileReader.hasNext()) {
                students.add(new Student(fileReader.next()));
            }

        } catch (FileNotFoundException e) {
            System.err.println("There was a problem reading the file "
                    + filepath);
            e.printStackTrace();
        }

        return students;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeDesk(Desk selectedDesk) {
        desks.remove(selectedDesk);
    }

    public void addDesk(Desk desk) {
        desks.add(desk);
    }

    public List<Desk> getDesks() {
        return desks;
    }

    public void loadDeskLayoutFromFile(String filepath) {
        String content = FHSFileIO.readFileAsString(filepath);
        System.out.println(content);
        String[] lines = content.split("#");
        desks.clear();
        for (String line : lines) {
            System.out.println("read: " + line);
            String[] data = line.split(",");
            if (data.length != 5) continue;
            int x = Integer.parseInt(data[0].trim());
            int y = Integer.parseInt(data[1].trim());
            int w = Integer.parseInt(data[2].trim());
            int h = Integer.parseInt(data[3].trim());
            String name = data[4].trim();
            Desk d = new Desk(x, y, w, h);
            Student student = new Student(name);
            d.setStudent(student);
            desks.add(d);
        }
    }

    public void saveDeskLayoutToFile(String filepath) {
        String output = "";
        for (Desk d : desks) {
            String name = (d.getStudent() == null) ? " " : d.getStudent().getName();
            output += d.getX() + ", " + d.getY() + ", " + d.getW() + ", " + d.getH() + ", " + name + "#\n";
        }

        FHSFileIO.writeDataToFile(filepath, output);
    }

    public List<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<Student>();

        for (Desk d : desks) {
            students.add(d.getStudent());
        }

        return students;
    }

    public int size() {
        return getDesks().size();
    }

}