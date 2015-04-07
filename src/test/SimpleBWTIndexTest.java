import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc445project.SimpleBWTIndex;
import cpsc445project.SimpleBWTIndexBuilder;


public class SimpleBWTIndexTest {
	
	private SimpleBWTIndexBuilder builder = new SimpleBWTIndexBuilder();
	private SimpleBWTIndex bwt;
	private static List<Character> alphabet;
	private static String text = "acaacg";
	
	@Before
	public void initialize() {
		alphabet = new ArrayList<Character>();
		alphabet.add('\0');
		alphabet.add('a');
		alphabet.add('c');
		alphabet.add('g');
		
		bwt = (SimpleBWTIndex) builder.build(text, alphabet);
	}
	
	@Test
	public void testGetSuffixRange() {
		
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, '\0')[0] == 0);
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, '\0')[1] == 0);
		
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, 'a')[0] == 1);
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, 'a')[1] == 3);
		
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, 'c')[0] == 4);
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, 'c')[1] == 5);
		
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, 'g')[0] == 6);
		assertTrue(bwt.getSuffixRange(0, bwt.size() - 1, 'g')[1] == 6);
		
		// get suffix range for 'ac':
		int[] saRangeC = bwt.getSuffixRange(0, bwt.size() - 1, 'c');
		int[] saRangeA = bwt.getSuffixRange(saRangeC[0], saRangeC[1], 'a');
		assertTrue(saRangeA[0] == 2);
		assertTrue(saRangeA[1] == 3);
	}

}
