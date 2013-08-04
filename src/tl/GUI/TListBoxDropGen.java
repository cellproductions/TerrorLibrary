package tl.GUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class TListBoxDropGen<T> extends TAbstractListBoxDrop<TListBoxItem<T>>
{
	public TListBoxDropGen()
	{
		super();
		type = ComponentType.listBoxDropGen;
	}
	
	public TListBoxDropGen(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDropGen;
		items = new ArrayList<TListBoxItem<T>>();
	}
	
	public TListBoxDropGen(TGUIComponent parent, float x, float y, int width, int height, int index, Color background) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDropGen;
		items = new ArrayList<TListBoxItem<T>>();
	}
	
	public TListBoxDropGen(TGUIComponent parent, float x, float y, int width, int height, int index, Color background, int priority) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDropGen;
		items = new ArrayList<TListBoxItem<T>>();
	}
	
	protected void draw(Graphics g) throws SlickException
	{
		g.setColor(background);
		g.fillRect(screenPos.x, screenPos.y, size.width, dropped ? size.height : defH);
		g.setColor(border);
		g.drawRect(screenPos.x, screenPos.y, size.width, dropped ? size.height : defH);
		g.setFont(TGUIManager.guiFont);
		
		int fontHeight = gapHeight - 5;
		if (dropped)
		{
			int numRows = (this.height() - 5) / (gapHeight - 5);
			for (int i = numDown; i < (numItems < numRows ? numItems : numRows); ++i)
			{
				g.setColor(font_colour);
				if (i == selected)
				{
					g.setColor(selected_background);
					g.fillRect(screenPos.x + 1, screenPos.y + i * gapHeight, size.width - 15, fontHeight + 2);
					g.setColor(selected_colour);
				}
				g.drawString(items.get(i).item, screenPos.x + 3, screenPos.y + i * (gapHeight - 5));
			}
			
			if (tooBig())
			{
				g.setColor(border);
				g.drawLine(screenPos.x + size.width - 13, screenPos.y + size.height / 2, screenPos.x + size.width, screenPos.y + size.height / 2);
				g.drawLine(screenPos.x + size.width - 14, screenPos.y + 1, screenPos.x + size.width - 14, screenPos.y + size.height);
				Polygon up = new Polygon();
				up.addPoint(screenPos.x + size.width - 12, screenPos.y + 10);
				up.addPoint(screenPos.x + size.width - 2, screenPos.y + 10);
				up.addPoint(screenPos.x + size.width - 7, screenPos.y + 2);
				g.draw(up);
				Polygon down = new Polygon();
				down.addPoint(screenPos.x + size.width - 12, screenPos.y + size.height - 10);
				down.addPoint(screenPos.x + size.width - 2, screenPos.y + size.height - 10);
				down.addPoint(screenPos.x + size.width - 7, screenPos.y + size.height - 2);
				g.draw(down);
				g.fill(up);
				g.fill(down);
			}
		}
		else
		{
			if (numItems > 0)
			{
				g.setColor(border);
				g.fillRect(screenPos.x + 1, screenPos.y + 3, size.width - 15, fontHeight + 2);
				g.setColor(selected_colour);
				g.drawString(items.get(selected > -1 && selected < numItems ? selected : 0).item, screenPos.x + 3, screenPos.y);
				if (selected < 0 || selected >= numItems)
					selected = 0;
			}
			
			g.setColor(border);
			Polygon down = new Polygon(); // draw the drop down button
			down.addPoint(screenPos.x + size.width - 12, screenPos.y + 11);
			down.addPoint(screenPos.x + size.width - 2, screenPos.y + 11);
			down.addPoint(screenPos.x + size.width - 7, screenPos.y + 19);
			g.draw(down);
			g.fill(down);
		}
		if (TGUIManager.debug)
		{
			g.setColor(TGUIManager.YELLOW);
			g.drawRect(screenPos.x, screenPos.y, size.width, size.height);
		}
		g.setColor(TGUIManager.BLACK);
	}
	
	public void addItem(String text, T object)
	{
		items.add(new TListBoxItem<T>(text, object));
		numItems++;
		if (resizeable)
			resize();
		changed = true;
	}

	public void addItemAt(int index, String text, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, new TListBoxItem<T>(text, object));
		numItems++;
		if (resizeable)
			resize();
		changed = true;
	}
	
	public void setItem(int index, String text, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		TListBoxItem<T> item = items.get(index);
		item.item = text;
		item.object = object;
		changed = true;
	}
	
	public void removeItem(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.remove(index);
		numItems--;
		if (!tooBig())
			numDown = 0;
		if (resizeable)
			if (!tooBig())
				resize();
		changed = true;
	}
	
	public List<String> getItemStrings()
	{
		List<String> list = new LinkedList<String>();
		for (TListBoxItem<T> itr : items)
			list.add(itr.item);
		return list;
	}
	
	public List<T> getItemObjects()
	{
		List<T> list = new LinkedList<T>();
		for (TListBoxItem<T> itr : items)
			list.add(itr.object);
		return list;
	}
	
	public String getText(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index).item;
	}
	
	public T getObject(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index).object;
	}

	public void sort(TESortDirection direction)
	{
		java.util.Collections.sort(items);
		if (direction == TESortDirection.SORT_HIGHEST)
			java.util.Collections.reverse(items);
	}
}
