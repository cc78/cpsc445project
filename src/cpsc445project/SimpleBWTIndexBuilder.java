package cpsc445project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 *  Provides a simple but inefficient method for building a BWTIndex.
 *  Use for testing purposes.
 */
public class SimpleBWTIndexBuilder implements BWTIndexBuilder {

	private CompressedBWTIndexBuilder bwtRLXBuilder = new CompressedBWTIndexBuilder();
	
	@Override
	public BWTIndex build(String text, List<Character> alphabet) {
		/* build BWT */
		char [] bwt = new char[text.length() + 1];
		TreeMap<String, Integer> suffixArray = buildSuffixArray(text);

		int i = 0;
		for (String key : suffixArray.navigableKeySet()) {
			if (suffixArray.get(key) == 0) {
				bwt[i] = '\0';
			} else {
				bwt[i] = text.charAt(suffixArray.get(key) - 1);
			}
			i++;
		}

		/* construct c */
		Map<Character, Integer> c = countLesserOccurrences(text, alphabet);

		/* construct occ */
		int[][] occ = countOccurrencesByIndex("", bwt, alphabet); 
		//int[][] occ = new int[0][0];  // placeholder

		return new SimpleBWTIndex(bwt, c, alphabet, occ);

	}

	private TreeMap<String, Integer> buildSuffixArray(String text) {
		TreeMap<String, Integer> suffixArray = new TreeMap<String, Integer>();

		suffixArray.put("", text.length());

		// sorted order will be maintained in TreeMap
		for (int i = 0; i < text.length(); i++) {
			suffixArray.put(text.substring(i), i);
		}

		return suffixArray;
	}

	/*
	 * For each character c in the alphabet, count the number of occurrences in text
	 * of characters that are lexicographically smaller than c.
	 */
	private Map<Character, Integer> countLesserOccurrences(String text, List<Character> alphabet) {
		Map<Character, Integer> lesserOccurrences = new HashMap<Character, Integer>();
		Map<Character, Integer> occurrences = countMatches(text, alphabet);
		
		for (char c : alphabet) {
			int count = 0;
			for (char d : alphabet) {
				if (d < c) {
					count += occurrences.get(d);
				}
			}
			lesserOccurrences.put(c, count);
		}

		return lesserOccurrences;
	}

	private Map<Character, Integer> countMatches(String text, List<Character> alphabet) {
		HashMap<Character, Integer> occurrences = new HashMap<Character, Integer>();
		char[] myText = text.toCharArray();

		for (char c : alphabet) {
			occurrences.put(c, 0);
		}
		
		for (char d : myText) {
			occurrences.put(d, occurrences.get(d) + 1);
		}
		
		return occurrences;
	}

	/*
	 * Build auxiliary data structure as described in Ferragina and Manzini (2005)
	 * using word-size truncated recursion.
	 */
	private int[][] countOccurrencesByIndex(String text, char[] bwt, List<Character> alphabet) {
//		int[][] occ = new int[alphabet.size()][text.length()];

		List<Character> bwtAlphabet = new ArrayList<Character>(alphabet);
		bwtAlphabet.add('\0');

		// FIXME: is there a better way?
		if (!alphabet.contains('\0')) {
			alphabet.add('\0');
		}

		// need to (logically) partition bwt
		int bucketSize = (int) Math.floor(Math.log(bwt.length)/Math.log(2));
		int nBuckets = (int) Math.ceil(bwt.length / Math.floor(Math.log(bwt.length)/Math.log(2)));
		
		CompressedBWTIndex bwtRLX = bwtRLXBuilder.buildBwtRLX(bwt, alphabet, nBuckets, bucketSize);

		int[][] occ = { //t$a for occ[a],occ[c],occ[t],occ[g] aaaaact$g
				{1, 2, 3, 4, 5, 5, 5, 5, 5}, 
				{0, 0, 0, 0, 0, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 0, 0, 0, 0, 0, 1, 0},	
				};
		
		return occ;
	}

}
