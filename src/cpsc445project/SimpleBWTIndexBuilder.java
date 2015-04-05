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

		BitBuffer bwtRLX = buildBwtRLX(bwt, alphabet);

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
	public BitBuffer buildBwtRLX(char[] bwt, List<Character> alphabet) {
		// FIXME: asymptotic size calculation
		int asymptoticSize = 5 * bwt.length +
				(int) Math.floor(Math.log(bwt.length)/Math.log(2));
		BitBuffer bwtRLX = new BitBuffer(asymptoticSize);
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

		/* perform steps (2) and (3) of the algorithm simultaneously */
		for (int i = 0; i < mtf.size(); i++) {
			int mtfValue = mtf.get(i);
			if (mtfValue > 0) {
				int zeros = (int) Math.floor(Math.log10(mtfValue + 1)/Math.log10(2));
				for (int j = 0; j < zeros; j++) {
					bwtRLX.setBit(bwtRLX.getNextIndex(), false);
				}
				bwtRLX.setBitsToBinaryValueOf(bwtRLX.getNextIndex(), mtfValue + 1);
			} else {
				int runLength = 1;
				// advance to the end of the run of 0's
				while (i + 1 < mtf.size() && mtf.get(i + 1) == 0) {
					runLength++;
					i++;
				}
				String rle = getRunLengthEncoding(runLength);
				for (int k = 0; k < rle.length(); k++) {
					int value = rle.charAt(k) == '0'? 2 : 3;
					bwtRLX.setBitsToBinaryValueOf(bwtRLX.getNextIndex(), value);
				}
			}
		}

		return bwtRLX;
	}

	private String getRunLengthEncoding(int runLength) {
		// Note: will break if input < 1 (but this should never really happen)
		String str =  Integer.toBinaryString(runLength + 1);
		String reversed = new StringBuilder(str).reverse().toString();
		return reversed.substring(0, reversed.length() - 1);  // drop last position
	}

}
