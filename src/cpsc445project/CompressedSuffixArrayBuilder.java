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

	private int[] buildSuffixArray(String text) {

		int[] suffixArray = new int[text.length()];
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
