package cpsc445project;

import java.util.List;

public interface BWTIndexBuilder {

	BWTIndex build(String text, List<Character> alphabet);

}
