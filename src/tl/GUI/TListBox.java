package tl.GUI;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import tl.Util.TCursor;

public class TListBox extends TGUIComponent implements TIGUICollection
{
	public static final ComponentType type = ComponentType.listBox;
	protected List<String> items;
	protected int numItems;
	protected int selected;
	protected int numDown;
	protected boolean toobig;
	protected Color background;
	
	protected static int gapHeight = 25;
	
	TGUISelectionEvent selectionChange;
	
	public TListBox()
	{
		super();
	}

	public TListBox(TGUIComponent parent)
	{
		super(parent);
		items = new ArrayList<String>();
		selected = -1;
	}
	
	public TListBox(TGUIComponent parent, float x, float y, int w, int h) throws SlickException
	{
		super(parent, x, y, w, h);
		items = new ArrayList<String>();
		selected = -1;
		changed = true;
	}

	public TListBox(TGUIComponent parent, float x, float y, int w, int h, int index) throws SlickException
	{
		super(parent, x, y, w, h);
		items = new ArrayList<String>();
		selected = index;
		changed = true;
	}

	@SuppressWarnings("static-access")
	private void updateLB() throws SlickException
	{
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawRect(0, 0, width - 1, height - 1);
		canvas.setFont(TGUIManager.guiFont);
		if (parent.type == ComponentType.component)
			background = parent.background;
		
		int fontHeight = gapHeight - 5;

		for (int i = 0; i < numItems; i++) // this could probably be optimized
		{
			if (fontHeight * i + fontHeight < height)
			{
				toobig = false;
				if (numDown + i < numItems)
				{
					canvas.setColor(TGUIManager.BLACK);
					if (numDown + i == selected) // draw the black box underneath the text if selected
					{
						canvas.fillRect(1, i * (height / (height / fontHeight)) + 3, width - 15, fontHeight + 2);
						canvas.setColor(background != null ? background : TGUIManager.LISTBOX_BACKGROUND);
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
			canvas.setColor(TGUIManager.BLACK);
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
	
	public void onSelectionChange(TGUISelectionEvent function)
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
				if (mouseIsWithinScroll())
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
					try
					{
						int where = getIndexFromCoordinates(x - gx, y - gy); // place here instead of in setSelected() so that exception isnt thrown in mid call
						setSelected(where);
					}
					catch (TGUIException e) {} // do nothing in this case
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
	
	protected boolean mouseIsOverItem()
	{
		return mouseIsOver() && !mouseIsWithinScroll();
	}
	
	protected boolean mouseIsWithinScroll()
	{
		int x = (int)(TCursor.getX() - gx);
		int y = (int)(TCursor.getY() - gy);
		return x >= width - 12 && x < width - 2 && y >= 0 && y < height;
	}
	
	public int getIndexFromCoordinates(float xx, float yy) throws TGUIException
	{
		int where = (int)((yy) / gapHeight) + numDown;
		if (where >= numItems || !mouseIsOverItem())
			throw new TGUIException("coordinates " + x + "," + y + " do not match any index!");
		return where;
	}
	
	public void setSelected(int index) throws TGUIException
	{
		if (selected < -1 || selected >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		selected = index;
		if (selectionChange != null)
			selectionChange.execute(selected, this);
		changed = true;
	}
	
	public int getSelected()
	{
		return selected;
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

	public void addItemAt(int index, String text) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, new String(text));
		numItems++;
		changed = true;
	}

	public void setItem(int index, String t) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.set(index, t);
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

	public String getText(int index) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index);
	}
	
	public void sort(TSortDirection direction)
	{
		java.util.Collections.sort(items);
		if (direction == TSortDirection.SORT_HIGHEST)
			java.util.Collections.reverse(items);
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

	public int getSize()
	{
		return numItems;
	}
}