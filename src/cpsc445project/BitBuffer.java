package cpsc445project;

import java.nio.ByteBuffer;

/*
 * A (probably not great) bit biffer implementation to support compressing.
 */
public class BitBuffer {
	private ByteBuffer buffer;
	private int length;
	private int nextIndex;	// first index in buffer that has not been set to 0 or 1

	public BitBuffer(int size) {
		this.buffer = ByteBuffer.allocate((int) Math.ceil(size / 8));
		this.length = size;  // effective length in bits; do not write beyond this
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

		if (index >= nextIndex) {
			nextIndex = index + 1;
		}
	}

	public int getBit(int index) {
		byte b = buffer.get(index / 8);
		byte bitMask = (byte) ((0x00000080 >>> (index % 8)) & 0x000000FF);
		return (b & bitMask) == 0? 0 : 1;
	}

	public ByteBuffer asByteBuffer() {
		return buffer;  // FIXME: return a copy?
	}

	public int getNextIndex() {
		return this.nextIndex;
	}

	public int length() {
		return length;
	}

	@Override
	public String toString() {  // not very nice; only meant for debugging
		char[] array = new char[nextIndex];
		for (int i = 0; i < nextIndex; i++) {
			array[i] = getBit(i) == 0? '0' : '1';
		}
		return String.valueOf(array);
	}

}