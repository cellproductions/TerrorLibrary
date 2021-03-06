package tl.Util;

public class TSize implements Comparable<TSize>, TITransformable<Integer>
{
	public int width;
	public int height;
	public static final TSize ZERO = new TSize();
	
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
	public int compareTo(TSize o)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toFormattedString()
	{
		return width + ", " + height;
	}

	@Override
	public void translate(Integer distance)
	{
		width += distance;
		height += distance;
	}

	@Override
	public void translate(Integer xdistance, Integer ydistance)
	{
		width += xdistance;
		height += ydistance;
	}

	@Override
	public void scale(Integer amount)
	{
		width *= amount;
		height *= amount;
	}

	@Override
	public void scale(Integer xscale, Integer yscale)
	{
		width *= xscale;
		height *= yscale;
	}

	@Override
	public void rotate(double angle)
	{
		 // TODO dont forget this shit
	}

	@Override
	public void rotate(double angle, Integer x, Integer y)
	{
		// TODO dont forget this shit
	}
}
