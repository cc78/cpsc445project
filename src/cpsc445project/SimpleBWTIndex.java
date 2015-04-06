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
	//private int[][] occ;
	private int[][] fpocc;  // see section 3.2 (i), Ferragina and Manzini (2005)
	private int[][] spocc;  // see section 3.2 (ii), Ferragina and Manzini (2005)
	private AuxiliaryDS aux;

	public SimpleBWTIndex(char[] index, Map<Character, Integer> c, List<Character> alphabet,
			int bucketSize, int[][] fpocc, int[][] spocc, AuxiliaryDS aux) {
		this.index = index;
		this.alphabet = alphabet;
		this.bucketSize = bucketSize;
		this.c = c;
		//this.occ = occ;
		this.fpocc = fpocc;
		this.spocc = spocc;
		this.aux = aux;
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
		
		int first = c.get(z) + this.getNumberOfOccurrences(z, suffixstart);
		int last = c.get(z) + this.getNumberOfOccurrences(z, suffixend - 1);

		return new int[] {first, last};
//		return new int[0];
	}
	
	// TODO: correction for runs of zeroes split between buckets ... ?
	public int getNumberOfOccurrences(char c, int q) {
		int bwtBucket = (int) Math.floor(((double) q) / bucketSize);
		int spIndex = q - bwtBucket * bucketSize;
		int t = (int) Math.ceil(((double) bwtBucket) / bucketSize) - 1;  // FIXME check this -- probably OK as is?
		
		// occurrences of c in first partition; 3.2 (i)
		int occFP = fpocc[c][t];  
		
		// occurrences of c in second partition; 3.2 (ii)
		int occSP = 0;
		if (bwtBucket % bucketSize != 0) {
			occSP = spocc[c][bwtBucket - 1];
		}
		
		// occurrences of c in third partition; 3.2 (iii)
		int tpIndex;
		if (bwtBucket % bucketSize == 0) {
			tpIndex = aux.getWidthUpTo(t); // check this
		} else {
			tpIndex = aux.getWidthUpTo(t) + aux.getRemainingWidthUpTo(bwtBucket);  // check this
		}
		
		return occFP + occSP;
	}
		
	public int[] getSARange(int i, char[] pattern) {
		i = pattern.length;
		char charToGet = pattern[i-1];
		int first = c.get(charToGet);
		System.out.println(first);
		int last = first;
		while ((first <= last) && (i >= 2)) {
			charToGet = pattern[i-1];
			//first = c.get(charToGet) + occ[alphabet.indexOf(charToGet)][first] + 1;
			//last = c.get(charToGet) + occ[alphabet.indexOf(charToGet)][last];
			first = 0;
			last = 0;
		}
		
		return new int[] {first, last};
	}	
	
	public List<Character> getAlphabet() {
		return this.alphabet;  // FIXME? return a copy?		
	}
	
}
