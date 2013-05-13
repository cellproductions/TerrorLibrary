package tl.GUI;

public interface TIGUICollection extends TIGUISelectable, TIGUIScrollable
{
	public void sort(TSortDirection direction);
	public boolean isEmpty();
	public int getSize();
}
