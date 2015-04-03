package cpsc445project;

import java.util.ArrayList;
import java.util.List;

public class CompressedBWTIndex implements BWTIndex {

	@Override
	public char get(int i) {
		return '\0';  // TODO
	};

	@Override
	public int size() {
		return 0;  // TODO
	};

	@Override
	public int[] isSuffixRange(int suffixstart, int suffixend, char z) {  // TODO
		return new int[0]; //TODO
	}

	public List<Character> getAlphabet() {
		return new ArrayList<Character>();
		
	}
	
	public int[] getSARange(int i, char[] suffix) {
		return new int[0]; //TODO
	}
}
