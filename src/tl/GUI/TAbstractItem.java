package tl.GUI;

public class TAbstractItem<T extends Comparable<T>> implements Comparable<TAbstractItem<T>>
{
	protected T item;

	public int compareTo(TAbstractItem<T> arg0)
	{
		int comp = item.compareTo(arg0.item);
		return comp == 0 ? -1 : comp;
	}
}
