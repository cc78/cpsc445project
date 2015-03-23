package cpsc445project;

import java.util.TreeMap;

public class CompressedSuffixArrayBuilder {

	public CompressedSuffixArray build(String text) {
		/* Divide text into segments */
		// TODO

		/* Build CSA for last text segment */
		// TODO; use buildLastCSASegment

		return new CompressedSuffixArray(new int[0]);  // FIXME
	}

	private void computeLongSuffixRank(String suffix) {
		// TODO
	}

	private void computeShortSuffixRank(String suffix) {
		// TODO
	}

	/*
	 * Fast suffix sorting as per Larsson NJ, Sadakane K (2007). 'Faster suffix sorting.' Theor. Comp. Sc. 387:258–272.
	 * Basic version (for now).
	 */
	private void fastSuffixSort(String text) {
		// TODO
		int[] suffixArray = new int[text.length() + 1];
		int[] groups = new int[text.length() + 1];		// stores group numbers
		int[] lengths = new int[text.length() + 1];		// stores lengths of unsorted groups and combined sorted groups

		int h = 1; 		// length of prefix by which suffixes are sorted

		/* first pass --- use heapsort to sort prefixes of length 1 (is there a better way?) */

		TreeMap<String, Integer> sa = new TreeMap<String, Integer>();
		sa.put("", text.length());

		for (int i = 0; i < text.length(); i++) {
			sa.put(text.substring(i, i + 1), i);
		}

		int i = 0;
		for (String key : sa.navigableKeySet()) {
			suffixArray[i] = sa.get(key);
			i++;
		}

		/* initialize groups[] */
		// FIXME is there a better way?
		int first = 0;
		int last;
		while (first < suffixArray.length) {
			last = first + 1;
			char s = text.charAt(suffixArray[first]);
			while (last < suffixArray.length && text.charAt(suffixArray[last]) == s) {
				last++;
			}
			for (int j = first; j < last; j++) {
				groups[j] = last - 1;
			}
			first = last;
		}

		/* initialize lengths[] */
		// FIXME is there a better way?
		first = 0;
		int sorted = 1;
		while (first < groups.length) {
			last = first + 1;
			if (groups[last] != groups[first]) {
				sorted = 1;
				while (groups[last] != groups[last - 1]) {  // while sorted, continue
					last++;
				}
			} else {
				sorted = -1;
				while (groups[last] == groups[last - 1]) {  // while unsorted, continue
					last++;
				}
			}
			for (int j = first; j < last; j++) {
				lengths[j] = sorted * (last - first);
			}
		}

		// TODO: continue from part (3) of the basic algorithm

	}

	/* Relatively inefficient method for building CSA for one (short) text segment */
	private CompressedSuffixArray buildLastCSASegment(String text) {

		int[] suffixArray;
		int[] inverseSuffixArray;
		int[] csa = new int[text.length()];

		suffixArray = buildSuffixArray(text);
		inverseSuffixArray = buildInverseSuffixArray(suffixArray);

		csa[0] = inverseSuffixArray[0];
		for (int i = 1; i < csa.length; i++) {
			csa[i] = inverseSuffixArray[suffixArray[i] + 1];
		}

		return new CompressedSuffixArray(csa);  // TODO
	}

	private int[] buildInverseSuffixArray(int[] suffixArray) {
		int[] inverseSuffixArray = new int[suffixArray.length];

		for (int i = 0; i < suffixArray.length; i++) {
			int j = suffixArray[i];
			inverseSuffixArray[j] = i;
		}

		return inverseSuffixArray;
	}

	// TODO: generalize to use prefixes of suffixes (with given length)?
	private int[] buildSuffixArray(String text) {

		int[] suffixArray = new int[text.length() + 1];
		TreeMap<String, Integer> sa = new TreeMap<String, Integer>();

		sa.put("", text.length());

		// inserts into TreeMap result in sorted order
		for (int i = 0; i < text.length(); i++) {
			sa.put(text.substring(i), i);
		}

		// convert to integer array
		int i = 0;
		for (String key : sa.navigableKeySet()) {
			suffixArray[i] = sa.get(key);
			i++;
		}

		return suffixArray;
	}
}
