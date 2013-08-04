package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

public class TListBoxDrop extends TAbstractListBoxDrop<String>
{
	protected boolean dropped;
	protected int defH = 30; // box height, h is height of box once dropped
	protected int normalHeight;
	
	protected static final Color defSelected = new Color(TGUIManager.LISTBOX_BACKGROUND);
	protected boolean resizeable;
	public final Color background = new Color(TGUIManager.LISTBOX_BACKGROUND);
	
	public TListBoxDrop()
	{
		super();
		type = ComponentType.listBoxDrop;
	}
	
	public TListBoxDrop(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.listBoxDrop;
		priority = 1; // higher priority over other types more likely
	}
	
	public TListBoxDrop(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDrop;
		normalHeight = height;
		if (normalHeight < defH)
			defH = normalHeight;
		priority = 1;
	}
	
	public TListBoxDrop(TGUIComponent parent, float x, float y, int width, int height, int index) throws SlickException
	{
		super(parent, x, y, width, height, index);
		type = ComponentType.listBoxDrop;
		normalHeight = height;
		if (normalHeight < defH)
			defH = normalHeight;
		priority = 1;
	}

	public void sort(TESortDirection direction)
	{
		java.util.Collections.sort(items);
		if (direction == TESortDirection.SORT_HIGHEST)
			java.util.Collections.reverse(items);
	}
}