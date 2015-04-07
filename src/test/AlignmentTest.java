package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc445project.Alignment;
import cpsc445project.BWTIndex;
import cpsc445project.BWTIndexBuilder;
import cpsc445project.SimpleBWTIndexBuilder;

public class AlignmentTest {
	private static BWTIndexBuilder builder = new SimpleBWTIndexBuilder();
	private static List<Character> alphabet = new ArrayList<Character>();
	BWTIndex rbwt;
	
	@Before
	public void init() {
		alphabet.add('\0');
		alphabet.add('a');
		alphabet.add('c');
		alphabet.add('t');
		alphabet.add('g');
		String reversedString = new StringBuilder("gacgc").reverse().toString();
		//Build the BWT for the reverse of the text instead of the text
		rbwt = builder.build(reversedString, alphabet);
	}
	
	@Test
	public void testAlignment() {
		Alignment a = new Alignment(rbwt, "tatct");
		double result = a.computeAlignment();
		System.out.println(result);
		assertTrue(result == 3.0);
	}	
	
}
