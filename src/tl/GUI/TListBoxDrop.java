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
	
	private void updateLBD() throws SlickException
	{
		if (graphic == TGUIManager.emptyImage)
			graphic = new Image(size.width, size.height);
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
						canvas.drawString(items.get(i + numDown), 3, i * (size.height / (size.height / fontHeight))); // draw the item
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
				canvas.drawString(items.get(selected > -1 && selected < numItems ? selected : 0), 3, 0);
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
		toobig = false;
		selected = -1;
		if (resizeable)
			size.height = defH;
		changed = true;
	}
}