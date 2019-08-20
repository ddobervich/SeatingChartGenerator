import processing.core.PApplet;

public class QueryBox {
    private String WHITE_LIST = "abcdefghijklmnopqrstuvwxyz\n";
    private int x, y, w, h;
    private String query = "";
    private String lastQuery = "";
    private boolean isDone = false;

    public QueryBox(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void draw(PApplet window) {
        window.textSize(32);
        System.out.println(this.getCurrentSoFar());
        window.fill(200, 200, 200, 220);
        window.rect(x, y, w, h);
        window.fill(0, 100, 0);
        window.stroke(0,100,0);
        window.text(query, x + 5, y + h/2);
    }

    public void addLetter(String letter) {
        // exit if letter isn't in the white list
        if (WHITE_LIST.indexOf(letter.toLowerCase()) == -1) {
            return;
        }

        if (letter.equals("\n")) {
            lastQuery = query;
            query = "";
            isDone = true;
        } else {
            query += letter;
            isDone = false;
        }
    }

    public String getCurrentSoFar() {
        return query;
    }

    public String getLastFullQuery() {
        return lastQuery;
    }

    public boolean isCurrentDone() {
        return isDone;
    }

    public void clear() {
        query = "";
    }
}
