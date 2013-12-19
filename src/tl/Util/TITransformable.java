package tl.Util;

public interface TITransformable<T>
{
	public void translate(T distance);
	public void translate(T xdistance, T ydistance);
	public void scale(T amount);
	public void scale(T xscale, T yscale);
	public void rotate(double angle);
	public void rotate(double angle, T x, T y);
}
