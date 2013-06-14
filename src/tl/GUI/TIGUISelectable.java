package tl.GUI;

public interface TIGUISelectable extends TGUIInterface
{
	public void onSelectionChange(TGUISelectionEvent function);
	public void setSelected(int index) throws TGUIException;
	public void removeSelected();
}
