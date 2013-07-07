package tl.GUI;

public interface TIGUICollection extends TIGUISelectable, TIGUIScrollable
{
	public void sort(TESortDirection direction);
	public boolean isEmpty();
	public int itemCount();
}
