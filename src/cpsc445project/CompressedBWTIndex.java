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
	public boolean isSuffix(int i, char[] suffix, char z) {  // TODO
		return false;
	}

	public List<Character> getAlphabet() {
		return new ArrayList<Character>();
		
	}
	
}
