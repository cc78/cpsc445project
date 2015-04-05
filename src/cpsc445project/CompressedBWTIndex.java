package cpsc445project;

/*
 * A compressed BWT index (that does not implement the BWTIndex interface) ...
 * Essentially a wrapper for BitBuffer that knows about logical bucket boundaries and
 * missing leading zeroes in each bucket (see Ferragina & Manzini 2005, Appendix C). 
 */
public class CompressedBWTIndex {
	
	private BitBuffer bwtRLX;			// compressed bwt
	private int[] bucketBoundaries;		// logical bucket boundaries (start index of each bucket)
	private int[] leadingZeroes;		// number of leading zeroes in logical bucket not encoded in the bucket itself
	
	public CompressedBWTIndex(BitBuffer bwtRLX, int[] bucketBoundaries, int[] leadingZeroes) {
		this.bwtRLX = bwtRLX;
		this.bucketBoundaries = bucketBoundaries;
		this.leadingZeroes = leadingZeroes;
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

}
