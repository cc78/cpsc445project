package cpsc445project;

import java.nio.ByteBuffer;

/*
 * A (probably not great) bit biffer implementation to support compressing.
 */
public class BitBuffer {
	private ByteBuffer buffer;
	private int length;
	private int nextIndex;	// the index of next bit to be set

	public BitBuffer(int capacity) {
		this.buffer = ByteBuffer.allocate((int) Math.ceil(capacity / 8));
		this.length = capacity;  // effective length
		this.nextIndex = 0;
	}

	public void setBitsToBinaryValueOf(int index, int value) {
		int quotient = value;
		int lastIndex = index + (int) Math.max((Math.floor(Math.log10(value)/Math.log10(2))), 0);
		int i = 0;
		do {
			if (quotient % 2 == 0) {
				setBit(lastIndex - i, false);
			} else {
				setBit(lastIndex - i, true);
			}
			quotient = quotient / 2;
			i++;
		} while (quotient > 0);
	}

	public void setBit(int index, boolean value) {
		byte b = buffer.get(index / 8);
		byte leftMask = (byte) (0x000000FF << (byte) (8 - (index % 8)));    	   // 1's on the left of index
		byte rightMask = (byte) ((0x000000FF >> ((index % 8) + 1)) & 0x000000FF);  // 1's on the right of index
		byte newBit = value? (byte) ((0x00000080 >>> (index % 8)) & 0x000000FF) : (byte) 0x00;
		byte newByte = (byte) ((b & leftMask) | newBit | (b & rightMask));
		buffer.put(index / 8, newByte);
		if (index > nextIndex) {
			nextIndex = index + 1;
		}
	}

	public boolean getBit(int index) {
		// TODO
		return false;
	}

	public ByteBuffer asByteBuffer() {
		return buffer;  // FIXME: return a copy?
	}

	public int length() {
		return length;
	}

}