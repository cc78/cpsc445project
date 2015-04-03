package cpsc445project;

import java.util.List;
import java.util.Map;

/*
 * A simple version of the BWT index as described in Lam et al (2008):
 * 'Compressed indexing and local alignment of DNA.'
 */
public class SimpleBWTIndex implements BWTIndex {

	private char [] index = new char[0];
	private Map<String, Integer> c;
	private List<Character> alphabet;
	private int[][] occ;

	public SimpleBWTIndex(char[] index, Map<String, Integer> c, List<Character> alphabet, int[][] occ) {
		this.index = index;
		this.c = c;
		this.alphabet = alphabet;
		this.occ = occ;
	}

	@Override
	public char get(int i) {
		return index[i];
	}

	@Override
	public boolean isSuffix(int i, char[] suffix, char z) {  // TODO
		int[] saRange = getSARange(i, suffix, z);
		return saRange[1] >= saRange[0];
	}

	@Override
	public int size() {
		return index.length;
	}

	private int[] getSARange(int i, char[] suffix, char z) {
		int p = c.get(String.valueOf(z));
		int q = occ[alphabet.indexOf(z)][i];

		return new int[] {p, q};
	}
	
	public List<Character> getAlphabet() {
		return this.alphabet;
		
	}
}
