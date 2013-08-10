package tl.Util;

public enum TTriBool
{
	TRUE((byte)1), FALSE((byte)0), UNDEFINED((byte)-1);
	
	byte value;
	private TTriBool(byte v)
	{
		value = v;
	}
	
	public byte value()
	{
		return value;
	}
	
	public static TTriBool parseTriBool(String s) throws TException
	{
		String cmp = s.toUpperCase();
		if (cmp.equals("TRUE") || cmp.equals("1"))
			return TRUE;
		else if (cmp.equals("FALSE") || cmp.equals("0"))
			return FALSE;
		else if (cmp.equals("UNDEFINED") || cmp.equals("-1"))
			return UNDEFINED;
		else
			throw new TException("value " + s + " is an invalid TTriBol value!");
	}
	
	public static TTriBool parseTriBool(byte b) throws TException
	{
		if (b < -1 || b > 1)
			throw new TException("value " + b + " is an invalid TTriBool value!");
		
		if (b == 1)
			return TRUE;
		else if (b == 0)
			return FALSE;
		else
			return UNDEFINED;
	}
	
	public String toString()
	{
		return "" + value;
	}
}
