package tl.Util;

public class TPoint implements Comparable<TPoint>
{
	public float x;
	public float y;
	public static final TPoint ZERO = new TPoint();
	
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
	
	public int compareTo(TPoint other)
	{
		return compare(other);
	}
	
	public String toFormattedString()
	{
		return x + "," + y;
	}
	
	public void translate(float xPos, float yPos)
	{
		x += xPos;
		y += yPos;
	}
	
	public void scale(float xScale, float yScale)
	{
		x *= xScale;
		y *= yScale;
	}
	
	public void rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		float xx = x;
		float yy = y;
		x = (float)(xx * FastTrig.cos(rad) - yy * FastTrig.sin(rad));
		y = (float)(xx * FastTrig.sin(rad) + yy * FastTrig.cos(rad));
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
	 * @see #get2DIndex(int, int, TPoint)
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
	 * @param point - The point to store the result in
	 * @return - The newly edited point
	 */
	public static TPoint get2DIndex(int index, int width, TPoint point)
	{
		int y = index / width;
		point.set(index - y * width, y);
		return point;
	}
	
	/**
	 * Used to find the distance between two points, using the linear distance algorithm.
	 * @param source - The first point
	 * @param destination - The second point
	 * @return - The distance between a and b, square rooted
	 */
	public static double distance(TPoint source, TPoint destination)
	{
		//return Math.sqrt(Math.pow((destination.x - source.x), 2) + Math.pow((destination.y - source.y), 2));
		return length(destination.subtract(source));
	}
	
	/**
	 * Used by {@link #distance(TPoint, TPoint)}, {@link #normalize(TPoint)}, and {@link #normalizeDestination(TPoint, TPoint)} to find the length for a single point. 
	 * @param point - The point to find the length of
	 * @return - A double representing the length
	 */
	public static double length(TPoint point)
	{
		return Math.sqrt(point.x * point.x + point.y * point.y);
	}
	
	/**
	 * Creates and returns a direction from a source point to a destination point.
	 * @param source - The point to start from
	 * @param destination - The point to arrive at
	 * @return - The direction as a TPoint
	 */
	public static TPoint normalizeDestination(TPoint source, TPoint destination)
	{
		TPoint point = destination.subtract(source);
		return new TPoint(point.x / (float)length(point), point.y / (float)length(point));
	}
	
	/**
	 * Creates and returns a direction from a source point.
	 * @param source - The point to start from
	 * @return - The direction as a TPoint
	 */
	public static TPoint normalize(TPoint source)
	{
		return new TPoint(source.x / (float)length(source), source.y / (float)length(source));
	}
	
	/**
	 * Returns the angle of a straight line between two points, converted to degrees.
	 * @param source - The source point
	 * @param destination - The destination point
	 * @return - The angle as a double
	 * @see #angleRadians(TPoint, TPoint)
	 */
	public static double angle(TPoint source, TPoint destination)
	{
		return Math.toDegrees(Math.atan2(destination.y - source.y, destination.x - source.x));
	}
	
	/**
	 * Returns the angle of a straight line between two points, converted to radians.
	 * @param source - The source point
	 * @param destination - The destination point
	 * @return - The angle as a double
	 * @see #angle(TPoint, TPoint)
	 */
	public static double angleRadians(TPoint source, TPoint destination)
	{
		return Math.atan2(destination.y - source.y, destination.x - source.x);
	}
	
	/**
	 * Applies a rotation transformation to a given TPoint.
	 * @param angle - The angle in degrees to rotate by
	 * @param source - The point to rotate
	 * @return - A new TPoint representing the transformation
	 */
	public static TPoint transformRotate(float angle, TPoint source)
	{
		return new TPoint((float)(source.x * FastTrig.cos(angle) - source.y * FastTrig.sin(angle)), (float)(source.x * FastTrig.sin(angle) + source.y * FastTrig.cos(angle)));
	}
	
	/**
	 * Applies a rotation transformation to a given position.
	 * @param angle - The angle in degrees to rotate by
	 * @param sourceX - The x position to rotate
	 * @param sourceY - The y position to rotate
	 * @return - A new TPoint representing the transformation
	 */
	public static TPoint transformRotate(float angle, float sourceX, float sourceY)
	{
		return new TPoint((float)(sourceX * FastTrig.cos(angle) - sourceY * FastTrig.sin(angle)), (float)(sourceX * FastTrig.sin(angle) + sourceY * FastTrig.cos(angle)));
	}
	/*
	public static TPoint transformRotate(float angle, TPoint source, TPoint around)
	{
		float sinangle = (float)FastTrig.sin(angle);
		float cosangle = 1f - (float)FastTrig.cos(angle);
		float aroundX = around.x * cosangle + around.y * sinangle;
		float aroundY = around.y * cosangle - around.x * sinangle;
		
	}*/
	
	public boolean equals(TPoint point)
	{
		return x == point.x && y == point.y;
	}
	
	public int hashCode()
	{
		return (int)(13f * x + 43f * y);
	}
	
	/**
	 * Direction provides a quick and easy abstraction for directions based on a 2D point.
	 * @author Callum Nichols
	 * @version 2.2
	 */
	public static enum Direction
	{
		NONE(0, 0), NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0),
		NORTH_EAST(1, -1), NORTH_WEST(-1, -1), SOUTH_EAST(1, 1), SOUTH_WEST(-1, 1);
		
		private TPoint dir;
		
		private Direction(float x, float y)
		{
			dir = new TPoint(x, y);
		}
		
		/**
		 * Returns the 2D point representation of this direction.
		 * @return - A TPoint storing the direction
		 */
		public TPoint toTPoint()
		{
			return dir;
		}
		
		/**
		 * Converts a TPoint to a direction. If the point does not match any of the directions, then it returns Direction.NONE.
		 * @param point - The TPoint to convert
		 * @return - A Direction representing the TPoint
		 * @see #valueOf(float, float)
		 */
		public static Direction valueOf(TPoint point)
		{
			return valueOf(point.x, point.y);
		}
		
		/**
		 * Converts a 2D point into a direction. If the point does not match any of the directions, then it returns Direction.NONE.
		 * @param x - The x position of the point
		 * @param y - The y position of the point
		 * @return - A Direction representing the 2D point
		 * @see #valueOf(TPoint)
		 */
		public static Direction valueOf(float x, float y)
		{
			if (x == 0 && y == 0)
				return NONE;
			else if (x == 0 && y == -1)
				return NORTH;
			else if (x == 0 && y == 1)
				return SOUTH;
			else if (x == 1 && y == 0)
				return EAST;
			else if (x == -1 && y == 0)
				return WEST;
			else if (x == 1 && y == -1)
				return NORTH_EAST;
			else if (x == -1 && y == -1)
				return NORTH_WEST;
			else if (x == 1 && y == 1)
				return SOUTH_EAST;
			else if (x == -1 && y == 1)
				return SOUTH_WEST;
			return NONE;
		}
	}
}
