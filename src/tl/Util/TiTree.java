package tl.Util;

public interface TiTree<T>
{
	public void addObject(T object);
	public void removeObject(T Object) throws TException;
	public int size();
}
