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
	private int[] widthUpTo; 			// W[] from section 3.2 (i)
	private int[] remainingWidthUpTo;	// W'[] from section 3.2 (ii)

	public CompressedBWTIndex(BitBuffer bwtRLX, int bucketSize, int[] bucketBoundaries, int[] leadingZeroes) {
		this.bwtRLX = bwtRLX;
		this.bucketSize = bucketSize;
		this.bucketBoundaries = bucketBoundaries;
		this.leadingZeroes = leadingZeroes;
		this.widthUpTo = setWidthUpTo();
		this.remainingWidthUpTo = setRemainingWidthUpTo();
	}

	/*
	 * Return Occ(c, q): the number of occurrences of c in bwt[0, q]
	 */
	public int getNumberOfOccurrences(char c, int q) {
		// TODO
		return 0;
	}

	public int getBeginIndex(int bucket) {
		return bucketBoundaries[bucket];
	}

	public int getNumberOfLeadingZeroes(int bucket) {
		// for now
		return leadingZeroes[bucket];
	}
	
	public int getWidthUpTo(int bucket) {
		return widthUpTo[bucket];
	}

	public int getRemainingWidthUpTo(int bucket) {
		return remainingWidthUpTo[bucket];
	}
	
	public String toString() {
		return bwtRLX.toString();
	}

	// TODO: more testing... ?
	public int[] setWidthUpTo() {
		int[] width = new int[bucketBoundaries.length];
		for (int i = 0; i < width.length - 1; i++) {
			width[i] = bucketBoundaries[i + 1];
		}
		width[width.length - 1] = bwtRLX.getNextIndex(); 

		return width;
	}

	// TODO: more testing... ?
	public int[] setRemainingWidthUpTo() {
		int[] width = new int[bucketBoundaries.length];

		int lastBoundary = 0;
		for (int i = 0; i < bucketBoundaries.length - 1; i++) {
			if (i > 0 && i % bucketSize == 0) {
				lastBoundary = i - 1;
			}
			width[i] = bucketBoundaries[i + 1] - width[i - lastBoundary];
		}
		width[width.length - 1] = bwtRLX.getNextIndex() - lastBoundary;

		return width;
	}

}
