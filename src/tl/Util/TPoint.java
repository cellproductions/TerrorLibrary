package tl.Util;

public class TPoint
{
	public float x;
	public float y;
	
	public TPoint()
	{
		x = 0;
		y = 0;
	}
	
	public TPoint(TPoint p)
	{
		x = p.x;
		y = p.y;
	}
	
	public TPoint(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(TPoint p)
	{
		x = p.x;
		y = p.y;
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public TPoint add(TPoint p)
	{
		return new TPoint(x + p.x, y + p.y);
	}
	
	public TPoint subtract(TPoint p)
	{
		return new TPoint(x - p.x, y - p.y);
	}
	
	public TPoint multiply(TPoint p)
	{
		return new TPoint(x * p.x, y * p.y);
	}
	
	public TPoint divide(TPoint p)
	{
		return new TPoint(x / p.x, y / p.y);
	}
	
	public TPoint add(float x, float y)
	{
		return new TPoint(this.x + x, this.y + y);
	}
	
	public TPoint subtract(float x, float y)
	{
		return new TPoint(this.x - x, this.y - y);
	}
	
	public TPoint multiply(float x, float y)
	{
		return new TPoint(this.x * x, this.y * y);
	}
	
	public TPoint divide(float x, float y)
	{
		return new TPoint(this.x / x, this.y / y);
	}
	
	protected boolean greaterThan(TPoint p)
	{
		return y > p.y ? true : (y == p.y ? x > p.x : false);
	}
	
	protected boolean lessThan(TPoint p)
	{
		return y < p.y ? true : (y == p.y ? x < p.x : false);
	}
	
	public byte compare(TPoint p)
	{
		if (lessThan(p))
			return -1;
		if (greaterThan(p))
			return 1;
		return 0;
	}
	
	public byte compare(float x, float y)
	{
		TPoint p = new TPoint(x, y);
		if (lessThan(p))
			return -1;
		if (greaterThan(p))
			return 1;
		return 0;
	}
	
	public String toFormattedString()
	{
		return x + "," + y;
	}
}
