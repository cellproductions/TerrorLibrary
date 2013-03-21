package tl.GUIutil;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import tl.Util.Cursor;

public class ListBox extends GUIControl
{
	protected List<String> items;
	public int numItems;
	public int selected;
	protected int numDown;
	protected boolean toobig;
	protected Color background;
	
	protected static int gapHeight = 25;
	protected int oldIndex;
	
	GUISelectionFunction selectionChange;
	
	protected Color black = new Color(0, 0, 0);

	public ListBox()
	{
		super();
		type = GUIControl.ControlType.listBox;
		graphic = null;
		numItems = 0;
		items = new ArrayList<String>();
		selected = -1;
		numDown = 0;
		toobig = false;
		oldIndex = selected;
		background = null;
	}
	
	public ListBox(int x, int y, int w, int h) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.listBox;
		background = null;
		graphic = new Image(w, h);
		numItems = 0;
		items = new ArrayList<String>();
		selected = -1;
		numDown = 0;
		toobig = false;
		oldIndex = selected;
		changed = true;
	}

	public ListBox(int x, int y, int w, int h, int index) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.listBox;
		background = null;
		graphic = new Image(w, h);
		numItems = 0;
		items = new ArrayList<String>();
		selected = index;
		numDown = 0;
		toobig = false;
		oldIndex = selected;
		changed = true;
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
					canvas.drawString(items.get(i + numDown), 3, i * (height / (height / fontHeight)));
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
	
	public void onSelectionChange(GUISelectionFunction function)
	{
		selectionChange = function;
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (button == 0)
			{
				if (isMouseWithinScroll())
				{
					if (toobig)
					{
						if (y - gy < height / 2 && y - gy >= 0 && numDown - 1 >= 0)
						{
							numDown--;
							changed = true;
						}
						if (y - gy >= height / 2 && y - gy < height && numDown < numItems - visibleItems())
						{
							numDown++;
							changed = true;
						}
					}
				}
				else
				{
					int where = getIndexFromCoordinates(x, y - gy);
					if (where > -1)
					{
						oldIndex = selected;
						selected = where;
						changed = true;
					}
				}
			}
			
			if (mouseClick != null) // mouseIsOver() is already checked at the top
			{
				mouseClick.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void mouseWheelMoved(int change)
	{
		if (mouseIsOver())
		{
			change /= 120;
			if (change > 0)
			{
				for (int i = 0; i < change; ++i)
					scrollUp();
			}
			else if (change < 0)
			{
				for (int i = 0; i > change; --i)
					scrollDown();
			}
		}
	}
	
	public boolean mouseIsOverItem()
	{
		return mouseIsOver() && !isMouseWithinScroll();
	}
	
	private boolean isMouseWithinScroll()
	{
		int x = (int)(Cursor.getX() - gx);
		int y = (int)(Cursor.getY() - gy);
		return x >= width - 12 && x < width - 2 && y >= 0 && y < height;
	}
	
	public int getIndexFromCoordinates(float xx, float yy)
	{
		int where = (int)((yy) / gapHeight) + numDown;
		if (where >= numItems || !mouseIsOverItem())
			return -1;
		return where;
	}
	
	protected int hasChanged()
	{
		if (oldIndex != selected)
			return (oldIndex = selected);
		return -1;
	}

	public boolean isEmpty()
	{
		return items.isEmpty();
	}

	public void addItem(String text)
	{
		items.add(new String(text));
		numItems++;
		changed = true;
	}

	public void addItemAt(int i, String text)
	{
		items.add(i, new String(text));
		numItems++;
		changed = true;
	}

	public void setItem(int num, String t)
	{
		items.set(num, t);
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
	
	public List<String> getItems()
	{
		return items;
	}
	
	public void scrollDown()
	{
		int fontHeight = gapHeight - 5;
		if (numItems >= 0)
		{
			if (fontHeight * numItems >= height)
			{
				if (numDown < numItems - visibleItems())
				{
					numDown++;
					changed = true;
				}
			}
		}
	}
	
	public void scrollUp()
	{
		int fontHeight = gapHeight - 5;
		if (numItems >= 0)
		{
			if (fontHeight * numItems >= height)
			{
				if (numDown - 1 >= 0)
				{
					numDown--;
					changed = true;
				}
			}
		}
	}
	
	public int visibleItems() // returns the number of visible items in the list, not very accurate, fix
	{
		return height / (gapHeight - 5);
	}

	public String getText(int index)
	{
		return items.get(index);
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
}