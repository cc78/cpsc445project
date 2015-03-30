package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cpsc445project.BWTIndex;
import cpsc445project.BWTIndexBuilder;
import cpsc445project.SimpleBWTIndexBuilder;

public class SimpleBWTIndexBuilderTest {

	private static char[] alphabet = new char[] {'a','b'};
	private static String text = "abaaba";
	private static BWTIndexBuilder builder = new SimpleBWTIndexBuilder();

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

}
