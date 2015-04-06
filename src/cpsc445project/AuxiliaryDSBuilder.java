package cpsc445project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuxiliaryDSBuilder {

	/*
	 * Compress BWT as per Ferragina and Manzini (2005).
	 * Build other auxiliary data structures required for computing occ(c, q).
	 */
	public AuxiliaryDS build(char[] bwt, List<Character> alphabet, int nBuckets, int bucketSize) {
		// int bucketSize = (int) Math.floor(Math.log10(bwt.length) / Math.log10(2));
		// int nBuckets = (int) Math.ceil(((double) bwt.length) / bucketSize);
		// FIXME: asymptotic size calculation
		int asymptoticSize = 5 * bwt.length +
				(int) Math.floor(Math.log(bwt.length)/Math.log(2));
		BitBuffer bwtRLX = new BitBuffer(asymptoticSize);
		int[] leadingZeroes = new int[nBuckets];
		int[] bwtRLXBoundaries = new int[nBuckets];
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

		/* 
		 * perform steps (2) and (3) of the algorithm simultaneously
		 * keep track of current bucket; encode runs of zeroes at bucket boundaries as per appendix C
		 */
		int bucket = 0;
		int nextBucketBoundary = bucketSize;  // first index in next bucket; given bucket size s, first bucket is indexed [0, s-1] 
		for (int i = 0; i < mtf.size(); i++) {
			if (i >= nextBucketBoundary) {
				if (i == nextBucketBoundary) {
					bwtRLXBoundaries[bucket + 1] = bwtRLX.getNextIndex();
				}
				bucket++;
				nextBucketBoundary = (bucket + 1) * bucketSize;
			}
			int mtfValue = mtf.get(i);
			if (mtfValue > 0) {
				int zeros = (int) Math.floor(Math.log10(mtfValue + 1)/Math.log10(2));
				for (int j = 0; j < zeros; j++) {
					bwtRLX.setBit(bwtRLX.getNextIndex(), false);
				}
				bwtRLX.setBitsToBinaryValueOf(bwtRLX.getNextIndex(), mtfValue + 1);
			} else {
				int startIndex = i;
				int runLength = 1;
				// advance to the end of the run of 0's
				while (i + 1 < mtf.size() && mtf.get(i + 1) == 0) {
					runLength++;
					i++;
				}
				String rle = getRunLengthEncoding(runLength);
				// handle possible bucket boundary here
				if (i >= nextBucketBoundary) {
					int inNextBucket = i - nextBucketBoundary + 1;
					int inPrevBucket = runLength - inNextBucket;
					int prevBucketPrefixLength = getShortestRLEPrefixGreaterThan(rle, inPrevBucket);
					bwtRLXBoundaries[bucket + 1] = bwtRLX.getNextIndex() + prevBucketPrefixLength * 2;
					leadingZeroes[bucket + 1] = inNextBucket - getHalfEncoding(rle.substring(prevBucketPrefixLength));
				}
				
				for (int k = 0; k < rle.length(); k++) {
					int value = rle.charAt(k) == '0'? 2 : 3;
					bwtRLX.setBitsToBinaryValueOf(bwtRLX.getNextIndex(), value);
				}
			}
		}
		
		int[][] fpocc = computeFpocc(bwt, alphabet, bucketSize);
		int[][] spocc = computeSpocc(bwt, alphabet, bucketSize);
				
		return new AuxiliaryDS(bwtRLX, bucketSize, bwtRLXBoundaries, leadingZeroes, fpocc, spocc);
	}

	private String getRunLengthEncoding(int runLength) {
		// Note: will break if input < 1 (but this should never really happen)
		String str =  Integer.toBinaryString(runLength + 1);
		String reversed = new StringBuilder(str).reverse().toString();
		return reversed.substring(0, reversed.length() - 1);  // drop last position
	}
	
	/*
	 * Helper method for handling runs of zeroes at bucket boundaries.
	 * See appendix C.
	 */
	private int getShortestRLEPrefixGreaterThan(String rle, int value) {
		int prefixLength;
		for (prefixLength = 1; prefixLength <= rle.length(); prefixLength++) {
			if (getHalfEncoding(rle.substring(0, prefixLength)) >= value)
				break;
		}
		return prefixLength;
	}
	
	private int getHalfEncoding(String rle) {
		int sum = 0;
		char[] chars = rle.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			sum += (Character.getNumericValue(chars[i]) + 1) * Math.pow(2, i);
		}
		return sum;
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
