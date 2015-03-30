package cpsc445project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cpsc445project.BWTIndex;
import cpsc445project.BWTIndexBuilder;
import cpsc445project.SimpleBWTIndex;

/*
 *  Provides a simple but inefficient method for building a BWTIndex.
 *  Use for testing purposes.
 */
public class SimpleBWTIndexBuilder implements BWTIndexBuilder {

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
		Map<String, Integer> c = countLesserOccurrences(text, alphabet);

		/* construct occ */
		int[][] occ = countOccurrencesByIndex(bwt, alphabet);

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
	private Map<String, Integer> countLesserOccurrences(String text, List<Character> alphabet) {
		HashMap<String, Integer> occurrences = new HashMap<String, Integer>();

		for (char c : alphabet) {
			int count = 0;
			for (char d : alphabet) {
				if (d < c) {
					count += countMatches(text, d);
				}
			}
			occurrences.put(String.valueOf(c), count);
		}

		return occurrences;
	}

	private int countMatches(String text, char c) {
		int count = 0;
		char[] myText = text.toCharArray();

		for (char d : myText) {
			if (d == c) {
				count++;
			}
		}
		return count;
	}

	/*
	 * Build auxiliary data structure as described in Ferragina and Manzini (2005)
	 * using word-size truncated recursion.
	 */
	private int[][] countOccurrencesByIndex(String text, List<Character> alphabet) {
		int[][] occ = new int[alphabet.size()][text.length()];

		List<Character> bwtAlphabet = new ArrayList<Character>(alphabet);
		bwtAlphabet.add('\0');

		// TODO

		return occ;
	}
	
	/*
	 * Compress BWT as per Ferragina and Manzini (2005). (Required for building occ.)
	 */
	private void bwtRLX(char[] bwt, List<Character> alphabet) {
		List<Integer> mtf = new ArrayList<Integer>(bwt.length);
		Collections.sort(alphabet);
		
		/* move-to-front transform */
		for (int i = 0; i < bwt.length; i++) {
			char c = bwt[i];
			int mtfValue = alphabet.indexOf(c);
			mtf.add(i, mtfValue);
			// move c to the front of alphabet
			alphabet.remove(mtfValue);
			alphabet.add(0, c);
		}
		
		/* encode each run of 0's in mtf using run-length encoder */
		// TODO
		
		
	}

}
