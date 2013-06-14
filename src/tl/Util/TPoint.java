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
	
	/**
	 * Used for converting an x index and a y index into a single index for use in a 1D array.<br>
	 * E.g.<br>
	 * <pre>
	 * {@code
	 * Image image = new Image(5, 10); // 5 width, 10 height
	 * byte data[] = image.getTexture().getTextureData();
	 * for (int y = 0; y < 10; ++y)
	 *     for (int x = 0; x < 5; ++x)
	 *         System.out.println(data[Point.get1DIndex(y, 5, x)]); // will print out indexes 0 to 50
	 * }
	 * </pre>
	 * @param py - Index y
	 * @param width - Width of a row in the 2D array
	 * @param px - Index x
	 * @return - The 1D index
	 * @see #get2DIndex(int, int, int)
	 */
	public static int get1DIndex(int py, int width, int px)
	{
		return py * width + px;
	}
	
	/**
	 * Used for converting a single 1D index into a 2D index.<br>
	 * E.g.<br>
	 * <pre>
	 * {@code
	 * Image image = new Image(5, 10); // 5 width, 10 height
	 * byte newdata[][] = new byte[image.getWidth()][image.getHeight()];
	 * byte data[] = image.getTexture().getTextureData();
	 * int len = data.length;
	 * for (int i = 0; i < len; ++i)
	 * {
	 *     TPoint index = TPoint.get2DIndex(i, image.getWidth());
	 *     newdata[index.x][index.y] = data[i];
	 * }
	 * }
	 * </pre>
	 * @param index - The 1D index
	 * @param width - The width of a row in the 2D array
	 * @return - The new 2D index
	 */
	public static TPoint get2DIndex(int index, int width)
	{
		return new TPoint(index % width, index / width);
	}
}
