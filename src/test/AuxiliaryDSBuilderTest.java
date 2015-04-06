import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc445project.AuxiliaryDS;
import cpsc445project.AuxiliaryDSBuilder;

public class AuxiliaryDSBuilderTest {
	
	private AuxiliaryDSBuilder builder = new AuxiliaryDSBuilder();
	private char[] bwt1;
	private char[] bwt2;
	private List<Character> alphabet;
	
	@Before
	public void initialize() { 
		alphabet = new ArrayList<Character>();
		alphabet.add('\0');
		alphabet.add('a');
		alphabet.add('b');
		
		bwt1 = new char[] {'a', 'b', 'b', 'a', '\0', 'a', 'a',};
		bwt2 = new char[] {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'};  // not really a bwt; used to test partitioning of runs of zeroes in mtf over bucket boundaries 
	}
	
	@Test
	public void testBuildBwtRLX() {
		/* case with no runs of zeroes split between logical buckets */
		AuxiliaryDS index1 = builder.buildBwtRLX(bwt1, alphabet, 2, 4);  // 2 buckets of 4 and 3 items each
		assertTrue(index1.toString().equals("0100111001001101010"));
		// check bucket boundaries
		assertTrue(index1.getBeginIndex(0) == 0);
		assertTrue(index1.getBeginIndex(1) == 11);
		// check leading zeroes
		assertTrue(index1.getNumberOfLeadingZeroes(0) == 0);
		assertTrue(index1.getNumberOfLeadingZeroes(1) == 0);
		
		/* case with runs of zeroes split between logical buckets */
		AuxiliaryDS index2 = builder.buildBwtRLX(bwt2, alphabet, 2, 4);  // 2 buckets of 4 and 3 items each
		assertTrue(index2.toString().equals("010101010"));
		// check bucket boundaries
		assertTrue(index2.getBeginIndex(0) == 0);
		assertTrue(index2.getBeginIndex(1) == 7);
		// check leading zeroes
		assertTrue(index2.getNumberOfLeadingZeroes(0) == 0);
		assertTrue(index2.getNumberOfLeadingZeroes(1) == 3);
		
		/* test setting of width and remaningWidth */  // TODO: this test maybe should be elsewhere?
		AuxiliaryDS index3 = builder.buildBwtRLX(bwt1, alphabet, 4, 2);
		assertTrue(index3.toString().equals("0100111001001101010"));
		
		assertTrue(index3.getWidthUpTo(0) == 6);
		assertTrue(index3.getWidthUpTo(1) == 11);
		assertTrue(index3.getWidthUpTo(2) == 17);
		assertTrue(index3.getWidthUpTo(3) == 19);
		
		assertTrue(index3.getRemainingWidthUpTo(0) == 6);
		assertTrue(index3.getRemainingWidthUpTo(1) == 11);
		assertTrue(index3.getRemainingWidthUpTo(2) == 6);
		assertTrue(index3.getRemainingWidthUpTo(3) == 18);
	}
	
	//@Test
	/*public void testgetHalfEncoding() {
		assertTrue(builder.getHalfEncoding("01") == 5);
	}*/

}