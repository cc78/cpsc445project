package cpsc445project;

public class SimpleBWTIndex implements BWTIndex {

	private char [] index = new char[0];

	public SimpleBWTIndex(char[] index) {
		this.index = index;
	}

	@Override
	public char get(int i) {
		return index[i];
	}

	@Override
	public int[] getSARange(int i, int j) {
		return new int[2];
	}

	@Override
	public int size() {
		return index.length;
	}
}
