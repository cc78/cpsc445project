package cpsc445project;

public class SimpleBWTIndex implements BWTIndex {

	private char [] index = new char[0];

	public SimpleBWTIndex(char[] index) {
		this.index = index;
	}

	public char get(int i) {
		return index[i];
	}

	public int size() {
		return index.length;
	}
}
