package cpsc445project;

import java.util.List;

public interface BWTIndex {

	char get(int i);
	boolean isSuffix(int i, char[] suffix, char z);  // TODO
	int size();
	List<Character> getAlphabet();

}
