package cpsc445project;

/*
 * Auxiliary data structures for retrieving the number of occurrences of c in a prefix of BWT.
 * See Ferragina and Manzini (2005). 
 */
public class AuxiliaryDS {

	private BitBuffer bwtRLX;			// compressed bwt
	private int bucketSize;
	private int[] bucketBoundaries;		// logical bucket boundaries (start index of each bucket)
	private int[] leadingZeroes;		// number of leading zeroes in logical bucket not encoded in the bucket itself
	private int[] widthUpTo; 			// W[] from section 3.2 (i)
	private int[] remainingWidthUpTo;	// W'[] from section 3.2 (ii)
	private int[][] fpocc;  			// see section 3.2 (i), Ferragina and Manzini (2005)
	private int[][] spocc;  			// see section 3.2 (ii), Ferragina and Manzini (2005)

	public AuxiliaryDS(BitBuffer bwtRLX, int bucketSize, int[] bucketBoundaries, int[] leadingZeroes,
			int[][] fpocc, int[][] spocc) {
		this.bwtRLX = bwtRLX;
		this.bucketSize = bucketSize;
		this.bucketBoundaries = bucketBoundaries;
		this.leadingZeroes = leadingZeroes;
		this.widthUpTo = setWidthUpTo();
		this.remainingWidthUpTo = setRemainingWidthUpTo();
		this.fpocc = fpocc;
		this.spocc =spocc;
				
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
	
	/*
	 * Return Occ(c, q): the number of occurrences of c in bwt[0, q]
	 * TODO: correction for runs of zeroes split between buckets ... ?
	 */
	public int getNumberOfOccurrences(char c, int q) {
		int bwtBucket = (int) Math.floor(((double) q) / bucketSize);
		int spIndex = q - bwtBucket * bucketSize;
		int t = (int) Math.ceil(((double) bwtBucket) / bucketSize) - 1;  // FIXME check this -- probably OK as is?
		
		// occurrences of c in first partition; 3.2 (i)
		int occFP = fpocc[c][t];  
		
		// occurrences of c in second partition; 3.2 (ii)
		int occSP = 0;
		if (bwtBucket % bucketSize != 0) {
			occSP = spocc[c][bwtBucket - 1];
		}
		
		// TODO: occurrences of c in third partition; 3.2 (iii)
		int tpIndex;
		if (bwtBucket % bucketSize == 0) {
			tpIndex = getWidthUpTo(t); // check this
		} else {
			tpIndex = getWidthUpTo(t) + getRemainingWidthUpTo(bwtBucket);  // check this
		}
		
		return occFP + occSP;
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
