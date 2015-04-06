package cpsc445project;

import java.util.List;

public interface BWTIndex {

	char get(int i);
	int[] getSuffixRange(int suffixstart, int suffixend, char z) ;
	int size();
	List<Character> getAlphabet();
	int[] getSARange(int i, char[] suffix);
}
