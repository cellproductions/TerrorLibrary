package tl.GUIutil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class ListBoxGen<T> extends ListBox
{
	public List<Item> items;
	
	public ListBoxGen(int x, int y, int w, int h) throws SlickException
	{
		super(x, y, w, h);
		items = new ArrayList<Item>();
	}
	
	public ListBoxGen(int x, int y, int w, int h, int index) throws SlickException
	{
		super(x, y, w, h, index);
		items = new ArrayList<Item>();
	}
	
	private void updateLB() throws SlickException
	{
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(black);
		canvas.drawRect(0, 0, width - 1, height - 1);
		canvas.setFont(GUIManager.guiFont);
		if (owningGUI.background != null)
			background = owningGUI.background;
		
		int fontHeight = gapHeight - 5;

		for (int i = 0; i < numItems; i++) // this could probably be optimized
		{
			if (fontHeight * i + fontHeight < height)
			{
				toobig = false;
				if (numDown + i < numItems)
				{
					canvas.setColor(black);
					if (numDown + i == selected) // draw the black box underneath the text if selected
					{
						canvas.fillRect(1, i * (height / (height / fontHeight)) + 3, width - 15, fontHeight + 2);
						canvas.setColor(background != null ? background : new Color(168, 168, 168));
					}
					canvas.drawString(items.get(i + numDown).text, 3, i * (height / (height / fontHeight)));
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
			canvas.setColor(black);
			canvas.drawLine(width - 13, height / 2, width - 1, height / 2);
			canvas.drawLine(width - 14, 1, width - 14, height);
			Polygon up = new Polygon();
			up.addPoint(width - 12, 10);
			up.addPoint(width - 2, 10);
			up.addPoint(width - 7, 2);
			canvas.draw(up);
			Polygon down = new Polygon();
			down.addPoint(width - 12, height - 10);
			down.addPoint(width - 2, height - 10);
			down.addPoint(width - 7, height - 2);
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
		
		if (hasChanged() > -1)
		{
			if (selectionChange != null)
				selectionChange.execute(selected, this);
			changed = true;
		}
		if (selected < 0 || selected >= numItems)
			selected = oldIndex = -1;

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

		if (visible && graphic.getAlpha() > 0.00F)
			g.drawImage(graphic, gx, gy);
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

	public void addItemAt(int i, String text, T object)
	{
		items.add(i, new Item(text, object));
		numItems++;
		changed = true;
	}

	public void setItem(int num, String text, T object)
	{
		Item item = items.get(num);
		item.text = text;
		item.object = object;
		changed = true;
	}

	public void removeItem(int num)
	{
		items.remove(num);
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
	
	public String getText(int index)
	{
		return items.get(index).text;
	}
	
	public T getObject(int index)
	{
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
	
	private class Item
	{
		public String text;
		public T object;
		
		public Item(String text, T object)
		{
			this.text = text;
			this.object = object;
		}
	}
}
