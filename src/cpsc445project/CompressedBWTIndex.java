package cpsc445project;

/*
 * A compressed BWT index (that does not implement the BWTIndex interface) ...
 * Essentially a wrapper for BitBuffer that knows about logical bucket boundaries and
 * missing leading zeroes in each bucket (see Ferragina & Manzini 2005, Appendix C). 
 */
public class CompressedBWTIndex {
	
	private BitBuffer bwtRLX;			// compressed bwt
	private int bucketSize;
	private int[] bucketBoundaries;		// logical bucket boundaries (start index of each bucket)
	private int[] leadingZeroes;		// number of leading zeroes in logical bucket not encoded in the bucket itself
	private int[] cumulativeLength; 	// W[] from section 3.2
	
	public CompressedBWTIndex(BitBuffer bwtRLX, int bucketSize, int[] bucketBoundaries, int[] leadingZeroes) {
		this.bwtRLX = bwtRLX;
		this.bucketSize = bucketSize;
		this.bucketBoundaries = bucketBoundaries;
		this.leadingZeroes = leadingZeroes;
		this.cumulativeLength = getCumulativeLengths();
	}
	
	/*
	 * Return Occ(c, q): the number of occurrences of c in bwt[0, q]
	 */
	public int getNumberOfOccurrences(char c, int q) {
		/* logically partition bwt[0, q] */
		//int firstPartition = getFirstPartition(q);
		//int secondPartition = getSecondPartition(firstPartition, q);
		return 0;
	}

	public int getBeginIndex(int bucket) {
		return bucketBoundaries[bucket];
	}
	
	public int getNumberOfLeadingZeroes(int bucket) {
		// for now
		return leadingZeroes[bucket];
	}
	
	public String toString() {
		return bwtRLX.toString();
	}
	
	private int[] getCumulativeLengths() {
		int[] cumulativeLength = new int[bucketBoundaries.length];
		for (int i = 0; i < cumulativeLength.length - 1; i++) {
			cumulativeLength[i] = bucketBoundaries[i + 1];
		}
		cumulativeLength[cumulativeLength.length - 1] = bwtRLX.length(); 
		
		return cumulativeLength;
	}

}
