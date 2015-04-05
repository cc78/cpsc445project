import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc445project.BWTIndex;
import cpsc445project.BitBuffer;
import cpsc445project.SimpleBWTIndex;
import cpsc445project.SimpleBWTIndexBuilder;

public class SimpleBWTIndexBuilderTest {

	private static List<Character> alphabet;
	private static List<Character> bwtAlphabet = new ArrayList<Character>();
	private static String text = "abaaba";
	private static SimpleBWTIndexBuilder builder = new SimpleBWTIndexBuilder();

	@Before
	public void buildAlphabet() {
		alphabet = new ArrayList<Character>();
		alphabet.add('a');
		alphabet.add('b');
	}

	@Before
	public void buildBWTAlphabet() {
		bwtAlphabet = new ArrayList<Character>();
		bwtAlphabet.add('\0');
		bwtAlphabet.add('a');
		bwtAlphabet.add('b');
	}

	@Test
	public void testBuild() {
		BWTIndex bwt = builder.build(text, alphabet);
		assertTrue(bwt.size() == 7);
		assertTrue(bwt.get(0) == 'a');
		assertTrue(bwt.get(1) == 'b');
		assertTrue(bwt.get(2) == 'b');
		assertTrue(bwt.get(3) == 'a');
		assertTrue(bwt.get(4) == '\0');
		assertTrue(bwt.get(5) == 'a');
		assertTrue(bwt.get(6) == 'a');
	}

	//@Test
	/*public void testRunLengthEncoding() {
		assertTrue(builder.getRunLengthEncoding(5).equals("01"));
		assertTrue(builder.getRunLengthEncoding(7).equals("000"));
	}*/

}
