package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cpsc445project.ScoringMatrix;

public class ScoringMatrixTest {
	private static ScoringMatrix scores = new ScoringMatrix();
	
	@Test
	public void testScores() {
		assertTrue(scores.getScore("a".charAt(0), "A".charAt(0)) == 3.0);
		assertTrue(scores.getScore("a".charAt(0), "B".charAt(0)) == Double.NEGATIVE_INFINITY);
		assertTrue(scores.getScore("A".charAt(0), "a".charAt(0)) == 3.0);
		assertTrue(scores.getScore("A".charAt(0), "c".charAt(0)) == 0.0);		
	}
}
