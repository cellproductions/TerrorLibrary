package tl.Util;

public class TBound
{
	public TPoint tCorner;
	public TPoint bCorner;
	
	public TBound()
	{
		tCorner = new TPoint();
		bCorner = new TPoint();
	}
	
	public TBound(TBound bound)
	{
		tCorner = new TPoint(bound.tCorner);
		bCorner = new TPoint(bound.bCorner);
	}
	
	public TBound(TPoint top, TPoint bottom)
	{
		tCorner = new TPoint(top);
		bCorner = new TPoint(bottom);
	}
	
	public TBound(float tx, float ty, float bx, float by)
	{
		tCorner = new TPoint(tx, ty);
		bCorner = new TPoint(bx, by);
	}
	
	public TBound(TPoint top, TSize dimensions)
	{
		tCorner = new TPoint(top);
		bCorner = new TPoint(top.add(dimensions.width, dimensions.height));
	}
	
	public TBound(float x, float y, int width, int height)
	{
		tCorner = new TPoint(x, y);
		bCorner = new TPoint(x + width, y + height);
	}
	
	public void set(TBound bound)
	{
		tCorner.set(bound.tCorner);
		bCorner.set(bound.bCorner);
	}
	
	public void set(TPoint top, TPoint bottom)
	{
		tCorner.set(top);
		bCorner.set(bottom);
	}
	
	public void set(float tx, float ty, float bx, float by)
	{
		tCorner.set(tx, ty);
		bCorner.set(bx, by);
	}
	
	public void set(TPoint topLeft, TSize dimensions)
	{
		tCorner.set(topLeft);
		bCorner.set(topLeft.x + dimensions.width, topLeft.y + dimensions.height);
	}
	
	public void set(float tx, float ty, int width, int height)
	{
		tCorner.set(tx, ty);
		bCorner.set(tx + width, ty + height);
	}
	
	public void setTopCorner(TPoint top)
	{
		tCorner.set(top);
	}
	
	public void setTopCorner(float tx, float ty)
	{
		tCorner.set(tx, ty);
	}
	
	public void setBottomCorner(TPoint bottom)
	{
		tCorner.set(bottom);
	}
	
	public void setBottomCorner(float bx, float by)
	{
		tCorner.set(bx, by);
	}
	
	public TPoint getTopCorner()
	{
		return new TPoint(tCorner);
	}
	
	public TPoint getBottomCorner()
	{
		return new TPoint(bCorner);
	}
	
	public TSize getSize()
	{
		TPoint pos = bCorner.subtract(tCorner);
		return new TSize((int)pos.x, (int)pos.y);
	}
	
	public int getWidth()
	{
		return (int)(bCorner.x - tCorner.x);
	}
	
	public int getHeight()
	{
		return (int)(bCorner.y - tCorner.y);
	}
	
	public TSize getAbsSize()
	{
		return new TSize(Math.abs((int)(bCorner.x - tCorner.x)), Math.abs((int)(bCorner.y - tCorner.y)));
	}
	
	public int getAbsWidth()
	{
		return Math.abs((int)(bCorner.x - tCorner.x));
	}
	
	public int getAbsHeight()
	{
		return Math.abs((int)(bCorner.y - tCorner.y));
	}
	
	public boolean isWithin(TPoint point)
	{
		return point.x > tCorner.x && point.x <= bCorner.x && point.y > tCorner.y && point.y < bCorner.y;
	}
	
	public boolean isWithin(float x, float y)
	{
		return x > tCorner.x && x <= bCorner.x && y > tCorner.y && y < bCorner.y;
	}
	
	public String toFormattedString()
	{
		return tCorner.toFormattedString() + "," + bCorner.toFormattedString();
	}
}
