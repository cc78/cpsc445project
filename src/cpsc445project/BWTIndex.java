package cpsc445project;

public interface BWTIndex {

	char get(int i);
	boolean isSuffix(int i, char[] suffix, char z);  // TODO
	int size();

}
