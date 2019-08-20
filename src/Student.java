import java.util.HashMap;
import java.util.Set;

public class Student {
	public String name;
	public HashMap<String, String> data;

	public Student(String name) {
		this.name = name;
		this.data = new HashMap<>();
	}

	public void addData(String[] headers, String[] data) {
		if (headers.length != data.length) {
			System.err.println("WARNING: data and headers not the same length");
		}

		for (int i = 0; i < Math.min(headers.length, data.length); i++){
			this.data.put(headers[i], data[i]);
		}
	}

	public Set<String> getDataKeys() {
		return this.data.keySet();
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}
}