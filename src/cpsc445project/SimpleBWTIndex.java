package cpsc445project;

public class SimpleBWTIndex implements BWTIndex {

	private char [] index;

	public SimpleBWTIndex(char[] index) {
		this.index = index;
	}

	public char get(int i) {
		return index[i];
	}
}
