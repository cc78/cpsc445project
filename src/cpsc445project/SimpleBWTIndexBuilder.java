package cpsc445project;

import java.util.ArrayList;
import java.util.BitSet;
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
		int[][] occ = countOccurrencesByIndex(bwt, alphabet); 
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
		HashMap<Character, Integer> occurrences = new HashMap<Character, Integer>();

		for (char c : alphabet) {
			int count = 0;
			for (char d : alphabet) {
				if (d < c) {
					count += countMatches(text, d);
				}
			}
			occurrences.put(c, count);
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
	private int[][] countOccurrencesByIndex(char[] text, List<Character> alphabet) {
//		int[][] occ = new int[alphabet.size()][text.length];

		List<Character> bwtAlphabet = new ArrayList<Character>(alphabet);
		bwtAlphabet.add('\0');
		// TODO
		
		int[][] occ = { //t$a for occ[a],occ[c],occ[t],occ[g] aaaaact$g
				{1, 2, 3, 4, 5, 5, 5, 5, 5}, 
				{0, 0, 0, 0, 0, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 0, 0, 0, 0, 0, 1, 0},	
				};

		return occ;
	}

	/*
	 * Compress BWT as per Ferragina and Manzini (2005). (Required for building occ.)
	 * Note: assumes alphabet size <= 127
	 */
	private void bwtRLX(char[] bwt, List<Character> alphabet) {
		// TODO: throw exception if alphabet is too large
		BitSet bwtRLX = new BitSet();
		List<Short> mtf = new ArrayList<Short>(bwt.length);
		Collections.sort(alphabet);

		/* move-to-front transform */
		for (int i = 0; i < bwt.length; i++) {
			char c = bwt[i];
			short mtfValue = (short) alphabet.indexOf(c);
			mtf.add(i, mtfValue);
			// move c to the front of alphabet
			alphabet.remove(mtfValue);
			alphabet.add(0, c);
		}

		/* perform steps (2) and (3) of the algorithm simultaneously */
		for (int i = 0; i < mtf.size(); i++) {
			int count = 0;
			short mtfValue = mtf.get(i);
			if (mtfValue > 0) {
				int zeros = (int) Math.floor(Math.log10(mtfValue + 1)/Math.log10(2));
			} else {
				// TODO: encode a run of 0's
			}
		}
	}

	// FIXME: return type?
	public short getRunLengthEncoding(short runLength) {
		String str =  Integer.toBinaryString(runLength + 1);
		String reversed = new StringBuilder(str).reverse().toString();
		short rle = (short) (Short.valueOf(reversed, 2) >>> 1);  // drop the rightmost bit
		//short rle = (Short.valueOf(reversed));  // drop the rightmost bit
		return rle;
	}

}
