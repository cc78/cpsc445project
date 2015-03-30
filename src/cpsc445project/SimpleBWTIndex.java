package cpsc445project;

import java.util.Map;

/*
 * A simple version of the BWT index as described in Lam et al (2008):
 * 'Compressed indexing and local alignment of DNA.'
 */
public class SimpleBWTIndex implements BWTIndex {

	private char [] index = new char[0];
	private Map<String, Integer> c;

	public SimpleBWTIndex(char[] index, Map<String, Integer> c) {
		this.index = index;
		this.c = c;
	}

	@Override
	public char get(int i) {
		return index[i];
	}

	@Override
	public boolean isSuffix(int i, int j, char[] suffix, char z) {  // TODO
		int[] saRange = getSARange(i, j, suffix, z);
		return saRange[0] <= saRange[1];
	}

	@Override
	public int size() {
		return index.length;
	}

	private int[] getSARange(int i, int j, char[] suffix, char z) {
		int p = this.c.get(String.valueOf(z));
		return new int[2];
	}
}
