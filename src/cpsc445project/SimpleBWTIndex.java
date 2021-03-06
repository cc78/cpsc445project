package cpsc445project;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * A simple version of the BWT index as described in Lam et al (2008):
 * 'Compressed indexing and local alignment of DNA.'
 * Calculation of occ(c, q) from Ferragina and Manzini (2005). 
 */
public class SimpleBWTIndex implements BWTIndex {

	private char [] index;
	private List<Character> alphabet;
	private Map<Character, Integer> c;
	private int[][] occ;

	public SimpleBWTIndex(char[] index, List<Character> alphabet, Map<Character, Integer> c,
			int[][] occ) {
		this.index = index;
		this.alphabet = alphabet;
		this.c = c;
		this.occ = occ;
	}

	@Override
	public char get(int i) {
		return index[i];
	}

	@Override
	public int size() {
		return index.length;
	}

	public char[] getBWTIndex() {
		return index;
	}
	
	public int getC(char z) {
		return c.get(z);
	}
	
	public int getOcc(char c, int q) {
		if (q < 0) {
			return 0;
		}
		return occ[alphabet.indexOf(c)][q];
	}

	public int[] getSuffixRange(int suffixstart, int suffixend, char z) {
		
		int first = c.get(z) + getOcc(z, suffixstart - 1);
		int last = c.get(z) + getOcc(z, suffixend) - 1;

		return new int[] {first, last};
	}
				
	public List<Character> getAlphabet() {
		return this.alphabet;  // FIXME? return a copy?		
	}
	
}
