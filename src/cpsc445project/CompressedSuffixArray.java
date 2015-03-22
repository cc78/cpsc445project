package cpsc445project;

public class CompressedSuffixArray {

	private int[] csa;

	public CompressedSuffixArray(int[] csa) {
		this.csa = csa;
	}

	// TODO: what does this method take in/return?
	public int get(int i) {
		return csa[i];
	}

}
