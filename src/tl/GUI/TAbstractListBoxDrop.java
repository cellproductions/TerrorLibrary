package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import tl.Util.TCursor;

public abstract class TAbstractListBoxDrop<T> extends TAbstractListBox<T>
{
	protected boolean dropped;
	protected int defH = 30; // box height, h is height of box once dropped
	protected int normalHeight;
	
	protected static final Color defSelected = new Color(TGUIManager.LISTBOX_BACKGROUND);
	protected boolean resizeable;
	public final Color background = new Color(TGUIManager.LISTBOX_BACKGROUND);
	
	public TAbstractListBoxDrop()
	{
		super();
		type = ComponentType.listBoxDrop;
	}
	
	public TAbstractListBoxDrop(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.listBoxDrop;
		priority = 1; // higher priority over other types more likely
	}
	
	public TAbstractListBoxDrop(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.listBoxDrop;
		normalHeight = height;
		if (normalHeight < defH)
			defH = normalHeight;
		priority = 1;
	}
	
	public TAbstractListBoxDrop(TGUIComponent parent, float x, float y, int width, int height, int index) throws SlickException
	{
		super(parent, x, y, width, height, index);
		type = ComponentType.listBoxDrop;
		normalHeight = height;
		if (normalHeight < defH)
			defH = normalHeight;
		priority = 1;
	}
	
	public void toggleResizeable() // DO NOT USE, DOES NOT WORK PROPERLY YET
	{
		resizeable = !resizeable;
		if (resizeable)
			resize();
		else
			size.height = normalHeight;
		changed = true;
	}
	
	protected void resize()
	{
		int size = (int)(defH * numItems - screenPos.y);
		if (size <= TGUIManager.screenHeight - 1 && defH * numItems >= defH)
			this.size.height = (defH - 5) * numItems;
		if (defH * numItems < defH)
			this.size.height = defH;
	}
	
	protected void change()
	{
		super.change();
		background.a = alpha;
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
			float y = 0;
			for (int i = numDown; i < (numItems <= numRows ? numItems : numRows); ++i)
			{
				g.setColor(font_colour);
				if (i == selected)
				{
					g.setColor(selected_background);
					g.fillRect(screenPos.x + 1, screenPos.y + y * gapHeight, size.width - 15, fontHeight + 2);
					g.setColor(selected_colour);
				}
				g.drawString(items.get(i).toString(), screenPos.x + 3, screenPos.y + y * (gapHeight - 5));
				++y;
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
				g.drawString(items.get(selected > -1 && selected < numItems ? selected : 0).toString(), screenPos.x + 3, screenPos.y);
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
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
			{
				if (dropped)
				{
					dropped = false;
					changed = true;
				}
				return;
			}
			if (button == 0)
			{
				if (enabled && visible)
				{
					if (mouseIsWithinScroll()) // click outside of drop box to close box
					{
						if (dropped)
						{
							if (tooBig())
							{
								if (y - screenPos.y < size.height / 2 && y - screenPos.y >= 0 && numDown - 1 >= 0)
								{
									numDown--;
									changed = true;
								}
								if (y - screenPos.y >= size.height / 2 && y - screenPos.y < size.height && numDown < numItems - visibleItems())
								{
									numDown++;
									changed = true;
								}
							}
						}
						else
						{
							dropped = true;
							changed = true;
						}
					}
					else
					{
						if (dropped)
						{
							try
							{
								int where = getIndexFromCoordinates(x - screenPos.x, y - screenPos.y); // place here instead of in setSelected() so that exception isnt thrown in mid call
								setSelected(where);
								dropped = false;
							}
							catch (TGUIException e) {} // do nothing in this case
						}
					}
				}
			}
			
			if (mousePress != null)
			{
				mousePress.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public int getIndexFromCoordinates(float xx, float yy) throws TGUIException
	{
		int where = (int)((yy) / gapHeight) + numDown;
		if (where >= numItems || !mouseIsOverItem())
			throw new TGUIException("coordinates " + position.x + "," + position.y + " do not match any index!");
		return where;
	}
	
	protected boolean mouseIsWithinScroll()
	{
		int x = (int)(TCursor.getX() - screenPos.x);
		int y = (int)(TCursor.getY() - screenPos.y);
		return x >= size.width - 12 && x < size.width - 2 && y >= 0 && y < (dropped ? size.height : defH);
	}
	
	public boolean mouseIsOver()
	{
		return TCursor.getX() >= screenPos.x && TCursor.getX() <= screenPos.x + size.width && TCursor.getY() >= screenPos.y && TCursor.getY() <= screenPos.y + (dropped ? size.height : defH) && visible;
	}
	
	public void addItem(T item)
	{
		items.add(item);
		numItems++;
		if (resizeable)
			resize();
		changed = true;
	}

	public void addItemAt(int index, T item) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, item);
		numItems++;
		if (resizeable)
			resize();
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
	
	public void scrollDown()
	{
		int fontHeight = gapHeight - 5;
		if (numItems >= 0)
		{
			if (dropped)
			{
				if (fontHeight * numItems >= size.height)
				{
					if (numDown < numItems - visibleItems())
					{
						numDown++;
						changed = true;
					}
				}
			}
		}
	}
	
	public void scrollUp()
	{
		int fontHeight = gapHeight - 5;
		if (numItems >= 0)
		{
			if (dropped)
			{
				if (fontHeight * numItems >= size.height)
				{
					if (numDown - 1 >= 0)
					{
						numDown--;
						changed = true;
					}
				}
			}
		}
	}
	
	public void clear()
	{
		items.clear();
		numItems = 0;
		numDown = 0;
		selected = -1;
		if (resizeable)
			size.height = defH;
		changed = true;
	}
}
