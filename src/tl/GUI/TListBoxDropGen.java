package tl.GUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class TListBoxDropGen<T> extends TListBoxDrop
{
	public List<Item> items;
	
	public TListBoxDropGen()
	{
		super();
		type = ComponentType.listBoxDropGen;
	}
	
	public TListBoxDropGen(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDropGen;
		items = new ArrayList<Item>();
	}
	
	public TListBoxDropGen(TGUIComponent parent, float x, float y, int width, int height, int index, Color background) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDropGen;
		items = new ArrayList<Item>();
	}
	
	public TListBoxDropGen(TGUIComponent parent, float x, float y, int width, int height, int index, Color background, int priority) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDropGen;
		items = new ArrayList<Item>();
	}
	
	private void updateLBD() throws SlickException	// IS THIS NEEDED? INHERITED FROM LISTBOXDROP ALREADY
	{
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawRect(0, 0, size.width - 1, (dropped ? size.height : defH) - 1); // draw the box outline/background
		canvas.setFont(TGUIManager.guiFont);
		if (parent.getType() == ComponentType.component)
			background = parent.background;
		
		canvas.setColor(background);
		canvas.fillRect(1, 1, size.width - 2, (dropped ? size.height : defH) - 2);
		
		int fontHeight = gapHeight - 5;

		if (dropped)
		{
			for (int i = 0; i < numItems; i++)
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
							canvas.setColor(newSelected != null ? newSelected : defSelected);
						}
						canvas.drawString(items.get(i + numDown).text, 3, i * (size.height / (size.height / fontHeight))); // draw the item
					}
				}
				else
				{
					toobig = true; // size.height of the number of items in the list is greater than the size.height of the list itself
					i = numItems; // break out of loop
				}
			}
	
			if (toobig) // draw the scroll arrows
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
		}
		else // draw the selected item
		{
			if (numItems > 0)
			{
				canvas.setColor(TGUIManager.BLACK);
				canvas.fillRect(1, 3, size.width - 15, fontHeight + 2);
				canvas.setColor(newSelected != null ? newSelected : defSelected);
				canvas.drawString(items.get(selected > -1 && selected < numItems ? selected : 0).text, 3, 0);
				if (selected < 0 || selected >= numItems)
					selected = 0;
			}
			
			canvas.setColor(TGUIManager.BLACK);
			Polygon down = new Polygon(); // draw the drop down button
			down.addPoint(size.width - 12, 11);
			down.addPoint(size.width - 2, 11);
			down.addPoint(size.width - 7, 19);
			canvas.draw(down);
			canvas.fill(down);
		}
		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, size.width - 1, size.height - 1);
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
				updateLBD();
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
	
	public void addItem(String text, T object)
	{
		items.add(new Item(text, object));
		numItems++;
		if (resizeable)
			resize();
		changed = true;
	}

	public void addItemAt(int index, String text, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, new Item(text, object));
		numItems++;
		if (resizeable)
			resize();
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
		if (resizeable)
			if (!toobig)
				resize();
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
		if (resizeable)
			size.height = defH;
		changed = true;
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
