package cpsc445project;

import java.util.List;

/*
 * Builds a compressed BWTIndex.
 * Algorithm from Hon et al. (2007) 'A space and time efficient algorithm
 * for building compressed suffix arrays.' Algorithmica 48: 23-26.
 */
public class CompressedBWTIndexBuilder implements BWTIndexBuilder {

	//private CompressedSuffixArray csa;

	@Override
	public BWTIndex build(String text, List<Character> alphabet) {
		return new CompressedBWTIndex();  // TODO
	}

}
