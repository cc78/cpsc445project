import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import cpsc445project.BitBuffer;

public class BitBufferTest {

	private BitBuffer buf;

	@Before
	public void createBuffer() {
		buf = new BitBuffer(16);
	}

	@Test
	public void testSetBit() {
		// set some bits
		buf.setBit(0, true);
		buf.setBit(3, true);
		buf.setBit(4, true);
		buf.setBit(5, true);
		buf.setBit(11, true);
		buf.setBit(15, true);
		// check result
		ByteBuffer byteBuf = buf.asByteBuffer();
		assertTrue(byteBuf.get(0) == -100);  // 10011100
		assertTrue(byteBuf.get(1) == 17);    // 00010001
	}

	@Test
	public void testGetBit() {
		buf.setBit(0, true);
		assertTrue(buf.getBit(0) == 1);

		buf.setBit(5, true);
		assertTrue(buf.getBit(5) == 1);

		buf.setBit(5, false);
		assertTrue(buf.getBit(5) == 0);
	}

	@Test
	public void testSetBitsToBinaryValueOf() {
		ByteBuffer byteBuf = buf.asByteBuffer();

		buf.setBitsToBinaryValueOf(0, 1);
		assertTrue(byteBuf.get(0) == -128);

		buf.setBitsToBinaryValueOf(1, 3);
		assertTrue(byteBuf.get(0) == -32);  // 11100000

		buf.setBitsToBinaryValueOf(8, 39);
		assertTrue(byteBuf.get(1) == -100);  // 10011100

		// cross byte boundary
		buf.setBitsToBinaryValueOf(6, 15);
		assertTrue(byteBuf.get(0) == -29);  // 11100011
		assertTrue(byteBuf.get(1) == -36);  // 11011100

		// set to 0
		buf.setBitsToBinaryValueOf(1, 0);
		assertTrue(byteBuf.get(0) == -93);  // 10100011
	}

	@Test
	public void testToString() {
		buf.setBit(0, true);
		buf.setBit(3, true);
		buf.setBit(4, true);
		buf.setBit(5, true);
		buf.setBit(11, true);
		buf.setBit(15, true);
		System.out.println(buf.toString());
		assertTrue(buf.toString().equals("1001110000010001"));
	}
}
