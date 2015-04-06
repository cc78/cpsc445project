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
	
	public int getOcc(char c, int q) {
		return occ[alphabet.indexOf(c)][q];
	}

	public int[] getSuffixRange(int suffixstart, int suffixend, char z) {
		
		int first = c.get(z) + occ[alphabet.indexOf(z)][suffixstart-1] + 1;
		int last = c.get(z) + occ[alphabet.indexOf(z)][suffixend];

		return new int[] {first, last};
	}
			
	public int[] getSARange(int i, char[] pattern) {
		i = pattern.length;
		char charToGet = pattern[i-1];
		int first = c.get(charToGet);
		System.out.println(first);
		int last = first;
		while ((first <= last) && (i >= 2)) {
			charToGet = pattern[i-1];
			first = c.get(charToGet) + occ[alphabet.indexOf(charToGet)][first] + 1;
			last = c.get(charToGet) + occ[alphabet.indexOf(charToGet)][last];
			first = 0;
			last = 0;
		}
		
		return new int[] {first, last};
	}	
	
	/*public List<Character> getAlphabet() {
		return this.alphabet;  // FIXME? return a copy?		
	}*/
	
}
