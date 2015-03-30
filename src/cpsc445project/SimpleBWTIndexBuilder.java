package cpsc445project;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/*
 *  Provides a simple but inefficient method for building a BWTIndex.
 *  Use for testing purposes.
 */
public class SimpleBWTIndexBuilder implements BWTIndexBuilder {

	@Override
	public BWTIndex build(String text, char[] alphabet) {
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

		/* calculate c */
		Map<String, Integer> c = countLesserOccurrences(text, alphabet);

		return new SimpleBWTIndex(bwt, c);
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
	private Map<String, Integer> countLesserOccurrences(String text, char[] alphabet) {
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

}
