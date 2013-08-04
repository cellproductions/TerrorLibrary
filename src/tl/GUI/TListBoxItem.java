package tl.GUI;

public class TListBoxItem<T> extends TAbstractItem<String>
{
	T object;
	public TListBoxItem(String text, T object)
	{
		item = text;
		this.object = object;
	}
	
	public String toString()
	{
		return item;
	}
}
