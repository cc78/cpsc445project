package cpsc445project;

import java.util.HashMap;


public class ListMatrix {
	private HashMap<String, Double> matrix;
	
	public ListMatrix() {
		this.matrix = new HashMap<String, Double>();
	}
	
	public void set(int x, int y, double value) {
		String key = Integer.toString(x)+","+Integer.toString(y);
		this.matrix.put(key, value);
	}
	
	public double get(int x, int y) {
		String key = Integer.toString(x)+","+Integer.toString(y);
		if (this.matrix.get(key) == null) {
			return Double.NEGATIVE_INFINITY;	
		} else {
			return this.matrix.get(key);
		}
	}
	
}
