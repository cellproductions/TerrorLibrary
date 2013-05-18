package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import tl.Util.TCursor;

public class TListBoxDrop extends TListBox
{
	protected boolean dropped;
	protected int defH = 30; // box height, h is height of box once dropped
	protected int normalHeight;
	
	protected static final Color defSelected = TGUIManager.LISTBOX_BACKGROUND;
	protected Color newSelected = null; // colour of selected text chosen by the user (not yet implemented)
	protected boolean resizeable;
	
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
	
	public TListBoxDrop(TGUIComponent parent, float x, float y, int w, int h) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.listBoxDrop;
		normalHeight = h;
		if (normalHeight < defH)
			defH = normalHeight;
		priority = 1;
	}
	
	public TListBoxDrop(TGUIComponent parent, float x, float y, int w, int h, int index) throws SlickException
	{
		super(parent, x, y, w, h, index);
		type = ComponentType.listBoxDrop;
		normalHeight = h;
		if (normalHeight < defH)
			defH = normalHeight;
		priority = 1;
	}
/*	UN-NEEDED?
	public ListBoxDrop(GUIComponent parent, int x, int y, int w, int h, int index, Color background, int priority) throws SlickException
	{
		super(parent, x, y, w, h, index);
		type = GUIComponent.ComponentType.listBoxDrop;
		normalHeight = h;
		this.background = background;
		this.priority = priority;
		dropped = false;
	}
	*/
	
	public void toggleResizeable() // DO NOT USE, DOES NOT WORK PROPERLY YET
	{
		resizeable = !resizeable;
		if (resizeable)
			resize();
		else
			height = normalHeight;
		changed = true;
	}
	
	protected void resize()
	{
		int size = (int)(defH * numItems - gy);
		if (size <= TGUIManager.screenHeight - 1 && defH * numItems >= defH)
			height = (defH - 5) * numItems;
		if (defH * numItems < defH)
			height = defH;
	}
	
	private void updateLBD() throws SlickException
	{
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawRect(0, 0, width - 1, (dropped ? height : defH) - 1); // draw the box outline/background
		canvas.setFont(TGUIManager.guiFont);
		if (parent.getType() == ComponentType.component)
			background = parent.background;
		
		canvas.setColor(background);
		canvas.fillRect(1, 1, width - 2, (dropped ? height : defH) - 2);
		
		int fontHeight = gapHeight - 5;

		if (dropped)
		{
			for (int i = 0; i < numItems; i++)
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
							canvas.setColor(newSelected != null ? newSelected : defSelected);
						}
						canvas.drawString(items.get(i + numDown), 3, i * (height / (height / fontHeight))); // draw the item
					}
				}
				else
				{
					toobig = true; // height of the number of items in the list is greater than the height of the list itself
					i = numItems; // break out of loop
				}
			}
	
			if (toobig) // draw the scroll arrows
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
		}
		else // draw the selected item
		{
			if (numItems > 0)
			{
				canvas.setColor(TGUIManager.BLACK);
				canvas.fillRect(1, 3, width - 15, fontHeight + 2);
				canvas.setColor(newSelected != null ? newSelected : defSelected);
				canvas.drawString(items.get(selected > -1 && selected < numItems ? selected : 0), 3, 0);
				if (selected < 0 || selected >= numItems)
					selected = 0;
			}
			
			canvas.setColor(TGUIManager.BLACK);
			Polygon down = new Polygon(); // draw the drop down button
			down.addPoint(width - 12, 11);
			down.addPoint(width - 2, 11);
			down.addPoint(width - 7, 19);
			canvas.draw(down);
			canvas.fill(down);
		}
		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, width - 1, height - 1);
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

		if (visible && graphic.getAlpha() > 0.00F)
			g.drawImage(graphic, gx, gy);
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
								int where = getIndexFromCoordinates(x - gx, y - gy); // place here instead of in setSelected() so that exception isnt thrown in mid call
								setSelected(where);
								dropped = false;
							}
							catch (TGUIException e) {} // do nothing in this case
						}
					}
				}
			}
			
			if (mouseClick != null)
			{
				mouseClick.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public int getIndexFromCoordinates(float xx, float yy) throws TGUIException
	{
		int where = (int)((yy) / gapHeight) + numDown;
		if (where >= numItems || !mouseIsOverItem())
			throw new TGUIException("coordinates " + x + "," + y + " do not match any index!");
		return where;
	}
	
	protected boolean mouseIsWithinScroll()
	{
		int x = (int)(TCursor.getX() - gx);
		int y = (int)(TCursor.getY() - gy);
		return x >= width - 12 && x < width - 2 && y >= 0 && y < (dropped ? height : defH);
	}
	
	public boolean mouseIsOver()
	{
		return TCursor.getX() >= gx && TCursor.getX() <= gx + width && TCursor.getY() >= gy && TCursor.getY() <= gy + (dropped ? height : defH) && visible;
	}
	
	public void addItem(String text)
	{
		items.add(new String(text));
		numItems++;
		if (resizeable)
			resize();
		changed = true;
	}

	public void addItemAt(int index, String text) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, new String(text));
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
		if (!toobig)
			numDown = 0;
		if (resizeable)
			if (!toobig)
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
	}
	
	public void scrollUp()
	{
		int fontHeight = gapHeight - 5;
		if (numItems >= 0)
		{
			if (dropped)
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
	}
	
	public void clear()
	{
		items.clear();
		numItems = 0;
		numDown = 0;
		toobig = false;
		selected = -1;
		if (resizeable)
			height = defH;
		changed = true;
	}
}