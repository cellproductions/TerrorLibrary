package tl.Util;

public class TSize
{
	public int width;
	public int height;
	
	public TSize()
	{
		width = height = 0;
	}
	
	public TSize(TSize size)
	{
		width = size.width;
		height = size.height;
	}
	
	public TSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public void set(TSize size)
	{
		width = size.width;
		height = size.height;
	}
	
	public void set(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public TSize add(TSize p)
	{
		return new TSize(width + p.width, height + p.height);
	}
	
	public TSize subtract(TSize p)
	{
		return new TSize(width - p.width, height - p.height);
	}
	
	public TSize multiply(TSize p)
	{
		return new TSize(width * p.width, height * p.height);
	}
	
	public TSize divide(TSize p)
	{
		return new TSize(width / p.width, height / p.height);
	}
	
	public TSize add(int width, int height)
	{
		return new TSize(this.width + width, this.height + height);
	}
	
	public TSize subtract(int width, int height)
	{
		return new TSize(this.width - width, this.height - height);
	}
	
	public TSize multiply(int width, int height)
	{
		return new TSize(this.width * width, this.height * height);
	}
	
	public TSize divide(int width, int height)
	{
		return new TSize(this.width / width, this.height / height);
	}
	
	// COMPARISON METHODS GO HERE
	
	public String toFormattedString()
	{
		return width + ", " + height;
	}
}
