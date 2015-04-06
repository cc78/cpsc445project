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
		//int[][] occ = countOccurrencesByIndex("", bwt, alphabet); 
		//int[][] occ = new int[0][0];  // placeholder

		int bucketSize = (int) Math.floor(Math.log(bwt.length)/Math.log(2));
		int[][] fpocc = computeFpocc(bwt, alphabet, bucketSize);
		int[][] spocc = computeSpocc(bwt, alphabet, bucketSize);
		
		int nBuckets = (int) Math.ceil(((double) bwt.length) / bucketSize);  // check this
		AuxiliaryDSBuilder auxBuilder = new AuxiliaryDSBuilder();
		AuxiliaryDS aux = auxBuilder.buildBwtRLX(bwt, alphabet, nBuckets, bucketSize);
		
		return new SimpleBWTIndex(bwt, c, alphabet, bucketSize, fpocc, spocc, aux);

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
		// FIXME: is there a better way? i.e. will the alphabet already contain '\0'?
		List<Character> bwtAlphabet = new ArrayList<Character>(alphabet);
		if (!bwtAlphabet.contains('\0')) {
			bwtAlphabet.add('\0');
		}
		
		int[][] occ = new int[alphabet.size()][text.length()];
		
		/* (logically) partition bwt */
		int bucketSize = (int) Math.floor(Math.log(bwt.length)/Math.log(2));
		int nBuckets = (int) Math.ceil(bwt.length / Math.floor(Math.log(bwt.length)/Math.log(2)));

		AuxiliaryDS bwtRLX = bwtRLXBuilder.buildBwtRLX(bwt, alphabet, nBuckets, bucketSize);

		/* build other auxiliary structures required for retrieving occ */
		
		
		/*int[][] occ = { //t$a for occ[a],occ[c],occ[t],occ[g] aaaaact$g
				{1, 2, 3, 4, 5, 5, 5, 5, 5}, 
				{0, 0, 0, 0, 0, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 0, 0, 0, 0, 0, 1, 0},	
				};*/
		
		return occ;
	}

	/*
	 * Compute 'first prefix occ', see section 3.2 (i)
	 */
	private int[][] computeFpocc(char[] index, List<Character> alphabet, int bucketSize) {
		int[][] occ = new int[alphabet.size()][index.length / (int) Math.pow(bucketSize, 2) + 1];  // FIXME?
		Collections.sort(alphabet);  // just in case 
		
		int bucket = 0;
		for (int i = 0; i < index.length; i++) {
			if (i == (bucket + 1) * (int) Math.pow(bucketSize, 2)) {
				for (int j = 0; j < alphabet.size(); j++) {
					occ[j][bucket + 1] = occ[j][bucket];
				}
				bucket++;
			}
			for (int k = 0; k < alphabet.size(); k++) {
				if (index[i] == alphabet.get(k)) {
					occ[k][bucket] += 1;
					break;
				}
			}
		}
		return occ;
	}
	
	/*
	 * Compute 'second prefix occ', see section 3.2 (ii)
	 */
	private int[][] computeSpocc(char[] index, List<Character> alphabet, int bucketSize) {
		int[][] occ = new int[alphabet.size()][index.length / bucketSize + 1];  // FIXME?
		Collections.sort(alphabet);
		
		int bucket = 0;
		for (int i = 0; i < index.length; i++) {
			if (i == (bucket + 1) * bucketSize) {
				for (int j = 0; j < alphabet.size(); j++) {
					occ[j][bucket + 1] = occ[j][bucket];
				}
				bucket++;
			}
			if (i == (bucket - 1) * (int) Math.pow(bucketSize, 2)) {
				// crossed boundary; restart count
				for (int j = 0; j < alphabet.size(); j++) {
					occ[j][bucket] = 0;
				}
			}
			for (int j = 0; j < alphabet.size(); j++) {
				if (index[i] == alphabet.get(j)) {
					occ[j][bucket] += 1;
					break;
				}
			}
		}
		return occ;
	}
	
}
