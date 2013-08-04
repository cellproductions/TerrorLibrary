package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

public class TListBox extends TAbstractListBox<String>
{
	public TListBox()
	{
		super();
		type = ComponentType.listBox;
	}

	public TListBox(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.listBox;
		items = new ArrayList<String>();
		selected = -1;
	}
	
	public TListBox(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBox;
		items = new ArrayList<String>();
		selected = -1;
		changed = true;
	}

	public TListBox(TGUIComponent parent, float x, float y, int width, int height, int index) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBox;
		items = new ArrayList<String>();
		selected = index;
		changed = true;
	}
	
	public void sort(TESortDirection direction)
	{
		java.util.Collections.sort(items);
		if (direction == TESortDirection.SORT_HIGHEST)
			java.util.Collections.reverse(items);
	}
}