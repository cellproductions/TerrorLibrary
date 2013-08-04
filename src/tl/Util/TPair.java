package tl.Util;

public class TPair<L extends Comparable<L>, R extends Comparable<R>> implements Comparable<TPair<L, R>>
{
	private L left;
	private R right;
	CompareBy compareBy;
	
	public static enum CompareBy
	{
		LEFT, RIGHT;
	}
	
	public TPair(L left, R right)
	{
		this.left = left;
		this.right = right;
		compareBy = CompareBy.LEFT;
	}
	
	public TPair(L left, R right, CompareBy compare)
	{
		this.left = left;
		this.right = right;
		compareBy = compare;
	}
	
	public void set(L left, R right)
	{
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(L left)
	{
		this.left = left;
	}
	
	public void setRight(R right)
	{
		this.right = right;
	}
	
	public L left()
	{
		return left;
	}
	
	public R right()
	{
		return right;
	}

	public int compareTo(TPair<L, R> pair)
	{
		if (compareBy == CompareBy.LEFT)
			return left.compareTo(pair.left);
		return right.compareTo(pair.right);
	}
}
