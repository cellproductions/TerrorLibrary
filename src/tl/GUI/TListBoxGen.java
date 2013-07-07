package tl.GUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class TListBoxGen<T> extends TListBox
{
	public List<Item> items;
	
	public TListBoxGen()
	{
		super();
		type = ComponentType.listBoxGen;
	}
	
	public TListBoxGen(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxGen;
		items = new ArrayList<Item>();
	}
	
	public TListBoxGen(TGUIComponent parent, float x, float y, int width, int height, int index) throws SlickException
	{
		super(parent, x, y, width, height, index);
		type = ComponentType.listBoxGen;
		items = new ArrayList<Item>();
	}
	
	private void updateLB() throws SlickException
	{
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawRect(0, 0, size.width - 1, size.height - 1);
		canvas.setFont(TGUIManager.guiFont);
		if (parent.getType() == ComponentType.component)
			background = parent.background;
		
		int fontHeight = gapHeight - 5;

		for (int i = 0; i < numItems; i++) // this could probably be optimized
		{
			if (fontHeight * i + fontHeight < size.height)
			{
				toobig = false;
				if (numDown + i < numItems)
				{
					canvas.setColor(TGUIManager.BLACK);
					if (numDown + i == selected) // draw the black box underneath the text if selected
					{
						canvas.fillRect(1, i * (size.height / (size.height / fontHeight)) + 3, size.width - 15, fontHeight + 2);
						canvas.setColor(background != null ? background : TGUIManager.LISTBOX_BACKGROUND);
					}
					canvas.drawString(items.get(i + numDown).text, 3, i * (size.height / (size.height / fontHeight)));
				}
			}
			else
			{
				toobig = true;
				i = numItems; // break out of loop
			}
		}

		if (toobig)
		{
			canvas.setColor(TGUIManager.BLACK);
			canvas.drawLine(size.width - 13, size.height / 2, size.width - 1, size.height / 2);
			canvas.drawLine(size.width - 14, 1, size.width - 14, size.height);
			Polygon up = new Polygon();
			up.addPoint(size.width - 12, 10);
			up.addPoint(size.width - 2, 10);
			up.addPoint(size.width - 7, 2);
			canvas.draw(up);
			Polygon down = new Polygon();
			down.addPoint(size.width - 12, size.height - 10);
			down.addPoint(size.width - 2, size.height - 10);
			down.addPoint(size.width - 7, size.height - 2);
			canvas.draw(down);
			canvas.fill(up);
			canvas.fill(down);
		}
		canvas.flush();
	}
	
	public void update(Graphics g)
	{
		if (mouseIsOver())
		{
			if (mouseOver != null)
			{
				mouseOver.execute(this);
				changed = true;
			}
		}

		try
		{
			if (changed)
			{
				updateLB();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}

		if (visible && alpha > 0.00F)
			g.drawImage(graphic, screenPos.x, screenPos.y);
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}

	public void addItem(String text, T object)
	{
		items.add(new Item(text, object));
		numItems++;
		changed = true;
	}

	public void addItemAt(int index, String text, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, new Item(text, object));
		numItems++;
		changed = true;
	}

	public void setItem(int index, String text, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		Item item = items.get(index);
		item.text = text;
		item.object = object;
		changed = true;
	}

	public void removeItem(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.remove(index);
		numItems--;
		if (!toobig)
			numDown = 0;
		changed = true;
	}
	
	public List<String> getItemStrings()
	{
		List<String> list = new LinkedList<String>();
		for (Item itr : items)
			list.add(itr.text);
		return list;
	}
	
	public List<T> getItemObjects()
	{
		List<T> list = new LinkedList<T>();
		for (Item itr : items)
			list.add(itr.object);
		return list;
	}
	
	public String getText(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index).text;
	}
	
	public T getObject(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index).object;
	}

	public void clear()
	{
		items.clear();
		numItems = 0;
		numDown = 0;
		toobig = false;
		selected = -1;
		changed = true;
	}
	
	public void sort(TESortDirection direction)
	{
		java.util.Collections.sort(items);
		if (direction == TESortDirection.SORT_HIGHEST)
			java.util.Collections.reverse(items);
	}
	
	private class Item implements Comparable<Item>
	{
		public String text;
		public T object;
		
		public Item(String text, T object)
		{
			this.text = text;
			this.object = object;
		}

		public int compareTo(Item o)
		{
			int comp = text.compareTo(o.text);
			return comp == 0 ? -1 : comp;
		}
	}
}
