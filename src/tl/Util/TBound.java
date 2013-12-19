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
	
	public void setPosition(TPoint point)
	{
		bCorner.set((float)getWidth() + point.x, (float)getHeight() + point.y);
		tCorner.set(point);
	}
	
	public void setPosition(float x, float y)
	{
		bCorner.set((float)getWidth() + x, (float)getHeight() + y);
		tCorner.set(x, y);
	}
	
	public void setBottomCorner(TPoint bottom)
	{
		tCorner.set(bottom);
	}
	
	public void setBottomCorner(float bx, float by)
	{
		tCorner.set(bx, by);
	}
	
	public void setSize(TSize size)
	{
		bCorner.set(tCorner.x + (float)size.width, tCorner.y + (float)size.height);
	}
	
	public void setSize(int width, int height)
	{
		bCorner.set(tCorner.x + (float)width, tCorner.y + (float)height);
	}
	
	public TPoint getTopLeftCorner()
	{
		return new TPoint(tCorner);
	}
	
	public TPoint getTopRightCorner()
	{
		return new TPoint(tCorner.x + getWidth(), tCorner.y);
	}
	
	public TPoint getBottomRightCorner()
	{
		return new TPoint(bCorner);
	}
	
	public TPoint getBottomLeftCorner()
	{
		return new TPoint(tCorner.x, tCorner.y + getHeight());
	}
	
	public float getX()
	{
		return tCorner.x;
	}
	
	public float getY()
	{
		return tCorner.y;
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
	
	/**
	 * Checks whether or not a TPoint is within this object's bounds. Being perfectly aligned does not 
	 * count as being within. E.g.
	 * <p><blockquote><pre>
	 * 		TBound bound = new TBound(1, 1, 2, 2);
	 * 		TPoint point = new TPoint(1, 1);
	 * 		System.out.println(bound.isWithin(point)); // this will print "false"
	 * </pre></blockquote></p>
	 * @param point - The TPoint to check for
	 * @return - True if the point is within this object's bounds
	 */
	public boolean isWithin(TPoint point)
	{
		//return point.x > tCorner.x && point.x <= bCorner.x && point.y > tCorner.y && point.y < bCorner.y;
		if (point.y <= tCorner.y)
			return false;
		if (point.x <= tCorner.x)
			return false;
		if (point.y >= bCorner.y)
			return false;
		if (point.x >= bCorner.x)
			return false;
		return true;
	}
	
	/**
	 * Checks whether or not a point is within this object's bounds. Being perfectly aligned does not 
	 * count as being within. E.g.
	 * <p><blockquote><pre>
	 * 		TBound bound = new TBound(1, 1, 2, 2);
	 * 		float x = 1, y = 1;
	 * 		System.out.println(bound.isWithin(x, y)); // this will print "false"
	 * </pre></blockquote></p>
	 * @param x - The x position to check for
	 * @param y - The y position to check for
	 * @return - True if the point is within this object's bounds
	 */
	public boolean isWithin(float x, float y)
	{
		if (y <= tCorner.y)
			return false;
		if (x <= tCorner.x)
			return false;
		if (y >= bCorner.y)
			return false;
		if (x >= bCorner.x)
			return false;
		return true;
	}
	
	private boolean isWithinTouch(float x, float y)
	{
		if (y < tCorner.y)
			return false;
		if (x < tCorner.x)
			return false;
		if (y > bCorner.y)
			return false;
		if (x > bCorner.x)
			return false;
		return true;
	}
	
	public boolean isWithin(TBound bounds)
	{
		return isWithin(bounds.tCorner) && isWithin(bounds.bCorner);
	}
	
	public boolean isWithin(float x, float y, int width, int height)
	{
		return isWithin(x, y) && isWithin(x + (float)width, y + (float)height);
	}
	
	public boolean isWithin(TPoint point, TSize size)
	{
		return isWithin(point.x, point.y, size.width, size.height);
	}
	
	public boolean intersects(float x, float y, int width, int height)
	{
		if (isWithin(x, y, width, height)) // if the whole thing is inside, then its not intersecting
			return false;
		
		TBound bounds = new TBound(x, y, width, height); // TODO I dont like creating a new TBound here, find a better solution
		if (isWithinTouch(x, y) || bounds.isWithinTouch(tCorner.x, tCorner.y))
			return true;
		if (isWithinTouch(x + width, y) || bounds.isWithinTouch(bCorner.x, tCorner.y))
			return true;
		if (isWithinTouch(x, y + height) || bounds.isWithinTouch(tCorner.x, bCorner.y))
			return true;
		if (isWithinTouch(x + width, y + height) || bounds.isWithinTouch(bCorner.x, bCorner.y))
			return true;
		return false;
	}
	
	public boolean intersects(TPoint point, TSize size)
	{
		return intersects(point.x, point.y, size.width, size.height);
	}
	
	public boolean intersects(TBound bounds)
	{
		if (isWithin(bounds))
			return false;
		
		if (isWithinTouch(bounds.tCorner.x, bounds.tCorner.y) || bounds.isWithinTouch(tCorner.x, tCorner.y))
			return true;
		if (isWithinTouch(bounds.bCorner.x, bounds.tCorner.y) || bounds.isWithinTouch(bCorner.x, tCorner.y))
			return true;
		if (isWithinTouch(bounds.tCorner.x, bounds.bCorner.y) || bounds.isWithinTouch(tCorner.x, bCorner.y))
			return true;
		if (isWithinTouch(bounds.bCorner.x, bounds.bCorner.y) || bounds.isWithinTouch(bCorner.x, bCorner.y))
			return true;
		return false;
	}
	
	public String toFormattedString()
	{
		return tCorner.toFormattedString() + "," + bCorner.toFormattedString();
	}
}
