package test;

import cpsc445project.ListMatrix;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ListMatrixTest {
	private static ListMatrix matrix = new ListMatrix();

	@Test
	public void testSetGet() {
		assertTrue(matrix.get(0,0) == Double.NEGATIVE_INFINITY);
		matrix.set(0,1, 5.0);
		assertTrue(matrix.get(0,1) == 5.0);
		assertTrue(matrix.get(1,0) == Double.NEGATIVE_INFINITY);
	}
}





