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
	BWTIndex bwt;
	BWTIndex rbwt;
	
	@Before
	public void init() {
		alphabet.add('a');
		alphabet.add('c');
		alphabet.add('t');
		alphabet.add('g');
		bwt = builder.build("actg", alphabet); //Build BWT
		rbwt = builder.build("gtca", alphabet); //Build reverse of BWT
	}
	
	@Test
	public void testAlignment() {
		Alignment a = new Alignment(bwt, "at");
		a.computeAlignment(rbwt);
	}	
	
}
