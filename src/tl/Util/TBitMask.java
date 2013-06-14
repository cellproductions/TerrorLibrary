package tl.Util;

/**
 * TBitMask is a class used for storing and manipulating multiple bits in a single byte.<br>
 * Bits can be pushed, popped, toggled, flipped en mass, and returned.
 * @author Callum Nichols
 * @since 2.1
 */
public class TBitMask
{
	/** The byte to store the options in. */
	byte options;
	
	/**
	 * Default constructor, for starting with an empty mask.
	 */
	public TBitMask()
	{
		options = 0;
	}
	
	/**
	 * Constructor, used to set an initial value.
	 * @param value - The value to be set to.
	 * @see #setValue(byte)
	 */
	public TBitMask(byte value)
	{
		options = value;
	}
	
	/**
	 * Copy constructor, used to set an initial value.
	 * @param mask - The bit mask to copy from.
	 * @see #setValue(TBitMask)
	 */
	public TBitMask(TBitMask mask)
	{
		options = mask.options;
	}
	
	/**
	 * Shifts the mask to the left by 1 and sets the first bit.<br>
	 * E.g.<br><br>
	 * <pre>
	 * {@code
	 * TBitMask mask = new TBitMask(10);
	 * mask.push(true); // mask was 1010(10), is now 10101(21), pushed 1
	 * }
	 * </pre>
	 * @param option - The option to push to the end of the mask.
	 * @see #pop()
	 */
	public void push(boolean option)
	{
		options = (byte)((options << 1) ^ (byte)(option ? 1 : 0));
	}
	
	/**
	 * Shifts the mask to the right by 1 and returns the truncated bit.<br>
	 * E.g.<br><br>
	 * <pre>
	 * {@code
	 * TBitMask mask = new TBitMask(10);
	 * mask.pop(); // mask was 1010(10), is now 101(5), returned 0
	 * }
	 * </pre>
	 * @return - The bit that was truncated from the mask (1 or 0).
	 * @see #push(boolean)
	 */
	public byte pop()
	{
		byte ret = (byte)(options & 1);
		options >>= 1;
		return ret;
	}
	
	/**
	 * Simple function that flips all the bits in the mask (inverts it).
	 */
	public void flip()
	{
		options ^= 255;
	}
	
	/**
	 * Flips a single bit determined by an index. Throws an exception if the index is < 0 or > 7.
	 * @param index - The index in the mask to be flipped.
	 * @throws TException
	 * @see #flip()
	 */
	public void toggleBit(byte index) throws TException
	{
		if (index < 0 || index > 7)
			throw new TException("index [" + index + "] is out of range[0-7]!");
		options ^= (1 << index);
	}
	
	/**
	 * Sets a bit at a specified index to a specific value. Throws an exception if the index is < 0 or > 7.
	 * @param index - The index in the mask to be set.
	 * @param option - The bit to be given to the bit at the index.
	 * @throws TException
	 * @see #getBit(byte)
	 */
	public void setBit(byte index, boolean option) throws TException
	{
		if (index < 0 || index > 7)
			throw new TException("index [" + index + "] is out of range[0-7]!");
		byte set = (byte)(option ? (1 << index) : (1 << index) ^ 255);
		options = (byte)(option ? options | set : options & set);
	}
	
	/**
	 * Returns a value specified at an index. Throws an exception if the index is < 0 or > 7.
	 * @param index - The index in the mask to be returned.
	 * @return - The bit at the index (1 or 0).
	 * @throws TException
	 * @see #setBit(byte, boolean)
	 * @see #getBits()
	 */
	public byte getBit(byte index) throws TException
	{
		if (index < 0 || index > 7)
			throw new TException("index [" + index + "] is out of range[0-7]!");
		return (byte)((options & (1 << index)) >> index);
	}
	
	/**
	 * Returns the value as an array of each of the value's bits (from right to left).
	 * @return - The bits as a boolean array.
	 * @see #getBit(byte)
	 * @see #getValue()
	 */
	public boolean[] getBits()
	{
		boolean ret[] = new boolean[8];
		TBitMask mask = new TBitMask(this);
		for (byte i = 0; i < 8; ++i)
			ret[i] = mask.getBit((byte)(7 - i)) == 1 ? true : false;
		return ret;
	}
	
	/**
	 * Sets the mask to a specific value.
	 * @param value - The value to be set to.
	 * @see #setValue(TBitMask)
	 * @see #getValue()
	 */
	public void setValue(byte value)
	{
		options = value;
	}
	
	/**
	 * Sets the mask to the value of another TBitMask.
	 * @param mask - The bit mask to copy from.
	 * @see #setValue(byte)
	 * @see #getValue()
	 */
	public void setValue(TBitMask mask)
	{
		options = mask.options;
	}
	
	/**
	 * Returns the whole value of the mask.
	 * @return - The value of this TBitMask.
	 */
	public byte getValue()
	{
		return options;
	}
}
