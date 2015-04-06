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

	private AuxiliaryDSBuilder bwtRLXBuilder = new AuxiliaryDSBuilder();
	
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
		
		/* from this point on, alphabet should contain the null char */
		if (!alphabet.contains('\0')) {
			alphabet.add('\0');
		}

		/* construct c */
		Map<Character, Integer> c = countLesserOccurrences(text, alphabet);

		/* construct occ */
		int[][] occ = countOccurrencesByIndex(bwt, alphabet); 
		
		return new SimpleBWTIndex(bwt, alphabet, c, occ);

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
	 * Build occ[c][q]
	 */
	private int[][] countOccurrencesByIndex(char[] bwt, List<Character> alphabet) {
		int[][] occ = new int[alphabet.size()][bwt.length];
		
		for (int i = 0; i < bwt.length; i++) {
			for (int j = 0; j < alphabet.size(); j++) {
				if (i > 0) {
					occ[j][i] = occ[j][i - 1];
				}
				if (bwt[i] == alphabet.get(j)) {
					occ[j][i] += 1;
				}
			}
		}
		
		return occ;
	}	
}
