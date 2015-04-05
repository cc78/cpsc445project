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
	private int bucketSize;
	private int[][] occ;
	private int[][] fpocc;  // see section 3.2 (i), Ferragina and Manzini (2005)
	private int[][] spocc;  // see section 3.2 (ii), Ferragina and Manzini (2005)

	public SimpleBWTIndex(char[] index, Map<Character, Integer> c, List<Character> alphabet,
			int bucketSize, int[][] fpocc, int[][] spocc, int[][] occ) {
		this.index = index;
		this.alphabet = alphabet;
		this.bucketSize = bucketSize;
		this.c = c;
		this.occ = occ;
		this.fpocc = fpocc;
		this.spocc = spocc;
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
	
	public int getBucketSize() {
		return bucketSize;
	}

	public int[] getSuffixRange(int suffixstart, int suffixend, char z) {
		
		int first = c.get(z) + occ[alphabet.indexOf(z)][suffixstart-1] + 1;
		int last = c.get(z) + occ[alphabet.indexOf(z)][suffixend];

		return new int[] {first, last};
	}
	
	public int getNumberOfOccurrences(char c, int q) {
		// TODO: these could be calculated on initialization ... ? same with bucket size 
		int firstPartition = (int) Math.pow(bucketSize, 2) * (index.length / (int) Math.pow(bucketSize, 2)) - 1;  // end index of first partition
		int secondPartition = firstPartition + bucketSize * ((index.length - firstPartition) / bucketSize) - 1;   // end index of second partition
		
		//return getOccInFirstPartition() + getOccInSecondPartition + getOccInLastPartition();
		
		return 0;
	}
	
	private int getOccInFirstPartition(char c, int lastIndex) {
		
		return 0;
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
		return this.alphabet;  // FIXME? return a copy?		
	}
	
}
