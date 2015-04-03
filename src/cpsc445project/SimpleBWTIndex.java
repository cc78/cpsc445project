package cpsc445project;

import java.util.List;
import java.util.Map;

/*
 * A simple version of the BWT index as described in Lam et al (2008):
 * 'Compressed indexing and local alignment of DNA.'
 */
public class SimpleBWTIndex implements BWTIndex {

	private char [] index = new char[0];
	private Map<Character, Integer> c;
	private List<Character> alphabet;
	private int[][] occ;

	public SimpleBWTIndex(char[] index, Map<Character, Integer> c, List<Character> alphabet, int[][] occ) {
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
	public int size() {
		return index.length;
	}

//	private int[] getSARange(int i, char[] suffix, char z) {
//		int p = c.get(z);
//		int q = occ[alphabet.indexOf(z)][i];
//
//		return new int[] {p, q};
//	}
//
	
	public int[] isSuffixRange(int suffixstart, int suffixend, char z) {
		
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
		}
		
		return new int[] {first, last};
	}	
	
	public List<Character> getAlphabet() {
		return this.alphabet;
		
	}
}
