package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import tl.Util.TCursor;
import tl.Util.TSize;

public class TContainer<T> extends TGUIComponent implements TIGUICollection
{
	private ArrayList<Item> items;
	private int numItems;
	private int numDown;
	private boolean toobig;
	private int iWidth;
	private int iHeight;
	private int gap;
	private boolean scaled;
	private int selected;
	public final Color border = new Color(Color.black);
	public final Color selected_colour = new Color(Color.yellow);
	
	TGUISelectionEvent selectionChange;
	TGUILoopEvent looped; // NOT NEEDED?
	
	public TContainer()
	{
		super();
		type = ComponentType.container;
	}
	
	public TContainer(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.container;
		items = new ArrayList<Item>();
		selected = -1;
	}
	
	public TContainer(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.container;
		items = new ArrayList<Item>();
		iWidth = 30;
		iHeight = 30;
		gap = 20;
		selected = -1;
		changed = true;
	}
	
	public TContainer(TGUIComponent parent, float x, float y, int width, int height, int imageWidth, int imageHeight, int gap) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.container;
		items = new ArrayList<Item>();
		iWidth = imageWidth;
		iHeight = imageHeight;
		scaled = true;
		this.gap = gap;
		selected = -1;
		changed = true;
	}
	
	protected void change()
	{
		int wgap = iWidth + gap;
		int hgap = iHeight + gap;
		float numNeededRows;
		int numInRow = (this.width() - (toobig ? 15 : 0) - gap) / wgap; // needs a scrollbar width
		int numRows = (this.height() - gap) / hgap;
		numNeededRows = Math.round((float)numItems / Math.max(numInRow, 1));
		toobig = (hgap) * (numItems % numNeededRows) + gap > size.height;
		
		float row = gap;
		float col = gap;
		for (int i = numDown * numInRow; i < (numItems < numInRow * numRows ? numItems : numInRow * numRows); ++i)
		{
			items.get(i).pos.set(col, row);
			col += wgap;
			if (col / wgap > numInRow)
			{
				col = gap;
				row += hgap;
				if (row / hgap > numRows)
					row = gap;
			}
		}
		
		border.a = alpha;
		selected_colour.a = alpha;
		changed = false;
	}
	
	protected void draw(Graphics g) throws SlickException
	{/*
		if (graphic == TGUIManager.emptyImage)
			graphic = new Image(size.width, size.height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(new Color(0, 0, 0));
		canvas.drawRect(0, 0, size.width - 1, size.height - 1);
		
		 ? true : false; // + gap for starting gap*/
		/*
		double w = width;
		double iw = iWidth;
		double g = gap;
		double ni = numItems;
		int fitWidth = (int)Math.round(w / ((iw + g) * (ni > 0 ? ni : 1))); // how many can fit horizontally
		
		for (int i = 0; i < numItems; i += fitWidth)
		{
			for (int n = i; n < i + fitWidth; n++)
			{
				int j = 0;
				if (numDown + i < numItems)
				{
					if (items.get(n) != null)
						canvas.drawImage(items.get(n).iGraphic, gap + j * iWidth, i * (iHeight + gap) + gap);
				}
				j++;
			}
		}
		*/
		// this wont draw items after scrolling down (cant use for-each loop)
		/*int fitWidth = (int)Math.round(size.width / (iWidth + gap)); // how many items can fit horizontally
		int vert = 0;
		int numhor = 0;
		Image todraw;
		for (Item i : items)
		{
			if (numDown + vert < numItems)
			{
				todraw = scaled ? i.iGraphic.getScaledCopy(iWidth, iHeight) : i.iGraphic;
				if ((vert * fitWidth) + numhor == selected)
				{
					Graphics g = todraw.getGraphics();
					g.setColor(Color.yellow);
					g.drawRect(0, 0, iWidth - 1, iHeight - 1);
					g.flush();
				}
				canvas.drawImage(todraw, (gap * (numhor + 1)) + numhor * iWidth, (gap * (vert + 1)) + vert * iHeight);
				numhor++;
				if (numhor >= fitWidth)
				{
					numhor = 0;
					vert++;
				}
			}
		}
		
		if (toobig)
		{
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
		canvas.flush();*/
		
		g.setColor(border);
		g.drawRect(screenPos.x, screenPos.y, size.width, size.height);
		int numInRow = (this.width() - (toobig ? 15 : 0) - gap) / (iWidth + gap);
		int numRows = (this.height() - gap) / (iHeight + gap);
		for (int i = numDown * numInRow; i < (numItems < numInRow * numRows ? numItems : numInRow * numRows); ++i)
		{
			Item item = items.get(i);
			float x = screenPos.x + item.pos.x;
			float y = screenPos.y + item.pos.y;
			g.drawImage(item.iGraphic, x, y);
			if (i == selected)
			{
				g.setColor(selected_colour);
				g.drawRect(x, y, iWidth - 1, iHeight - 1);
				g.setColor(TGUIManager.BLACK);
			}
		}
		if (toobig)
		{
			g.setColor(border);
			float x = screenPos.x + size.width;
			float y = screenPos.y + size.height;
			g.drawLine(x - 13, y / 2, x, y / 2);
			g.drawLine(x - 14, screenPos.y, x - 14, y);
			Polygon up = new Polygon();
			up.addPoint(x - 12, screenPos.y + 10);
			up.addPoint(x - 2, screenPos.y + 10);
			up.addPoint(x - 7, screenPos.y + 2);
			g.draw(up);
			Polygon down = new Polygon();
			down.addPoint(x - 12, y - 10);
			down.addPoint(x - 2, y - 10);
			down.addPoint(x - 7, y - 2);
			g.draw(down);
			g.fill(up);
			g.fill(down);
		}
		g.setColor(Color.black);
	}
	
	public void update(Graphics g)
	{
		if (mouseButtonDown(0))
		{
			float y = TCursor.getY() - screenPos.y;
			float x = TCursor.getX() - screenPos.x;

			if (toobig)
			{
				if (x >= size.width - 12 && x <= size.width - 2 && y <= 10 && y >= 2)
				{
					if (numDown - 1 >= 0)
					{
						numDown--;
						changed = true;
					}
				}
				if (x >= size.width - 12 && x <= size.width - 2 && y >= size.height - 10 && y <= size.height - 2)
				{
					if (numDown + 1 < numItems)
					{
						numDown++;
						changed = true;
					}
				}
			}
			/*
			if (x >= this.x && x < this.x + (width - 12))
			{
				int gapSize = 25;
				int where = (int)(y / gapSize) + numDown;
				selected = (where < numItems ? where : -1);
			}
			*/
		}
		
		if (mouseIsOver())
		{
			if (mouseOver != null)
			{
				mouseOver.execute(this);
				changed = true;
			}
		}
		
		if (looped != null)
		{
			looped.execute(this);
			changed = true;
		}
		
		if (changed)
			change();
		
		try
		{
			if (visible && alpha > 0.00f)
				draw(g);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public void onSelectionChange(TGUISelectionEvent function)
	{
		selectionChange = function;
	}
	
	public void onLoop(TGUILoopEvent function)
	{
		looped = function;
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (button == 0)
			{
				/*
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
				*/
				try
				{
					int where = getIndexFromCoordinates(x - screenPos.x, y - screenPos.y); // place here instead of in setSelected() so that exception isnt thrown in mid call
					setSelected(where);
				}
				catch (TGUIException e) {} // do nothing in this case
			}
			
			if (mousePress != null) // mouseIsOver() is already checked at the top
			{
				mousePress.execute(button, x, y, this);
				changed = true;
			}
		}
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
	
	public void removeSelected()
	{
		items.remove(selected);
		numItems--;
		if (!toobig)
			numDown = 0;
		changed = true;
	}
	
	private class Item
	{
		public Image iGraphic;
		public T object;
		public tl.Util.TPoint pos;
		
		public Item(Image i, T object)
		{
			pos = new tl.Util.TPoint();
			iGraphic = scaled ? i.getScaledCopy(iWidth, iHeight) : i;
			this.object = object;
		}
		
		public void setSize(int width, int height)
		{
			iGraphic = iGraphic.getScaledCopy(width, height);
		}
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
	
	public void addItem(Image image, T object)
	{
		items.add(new Item(image, object));
		numItems++;
		changed = true;
	}
	
	public void addItemAt(int index, Image image, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.add(index, new Item(image, object));
		numItems++;
		changed = true;
	}
	
	public void setItem(int index, Image image, T object) throws TGUIException
	{
		if (index < 0 || index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		items.set(index, new Item(image, object));
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
	
	public void removeItem(T object) throws TGUIException
	{
		if (!items.contains(object))
			throw new TGUIException("object does not exist!");
		items.remove(object);
		numItems--;
		if (!toobig)
			numDown = 0;
		changed = true;
	}
	
	public Image getGraphic(int index) throws TGUIException
	{
		if (index < 0 && index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index).iGraphic;
	}
	
	public T getObject(int index) throws TGUIException
	{
		if (index < 0 && index >= numItems)
			throw new TGUIException("index " + index + " out of bounds! [" + numItems + "]");
		return items.get(index).object;
	}
	
	public int getIndexFromCoordinates(float x, float y) throws TGUIException
	{
		int fitWidth = (int)(size.width / (iWidth + gap));
		int column = (int)(x / (gap + iWidth));
		int row = (int)(y / (gap + iHeight));
		if ((row * fitWidth) + column < 0 || (row * fitWidth) + column >= numItems)
			throw new TGUIException("coordinates " + x + "," + y + " do not match any index!");
		return (row * fitWidth) + column;
	}
	
	public void clear()
	{
		items.clear();
		numItems = 0;
		changed = true;
	}
	
	public void setImageSize(int width, int height)
	{
		iWidth = width;
		iHeight = height;
		if (items != null)
			for (Item item : items)
				item.setSize(width, height);
		changed = true;
	}
	
	public void setImageSize(TSize size)
	{
		iWidth = size.width;
		iHeight = size.height;
		if (items != null)
			for (Item item : items)
				item.setSize(size.width, size.height);
		changed = true;
	}
	
	public void setGapSize(int size)
	{
		gap = size;
		changed = true;
	}
	
	public int itemWidth()
	{
		return iWidth;
	}
	
	public int itemHeight()
	{
		return iHeight;
	}
	
	public int getGapSize()
	{
		return gap;
	}
	
	public void setTransparency(float transparency) throws TGUIException
	{
		//if (transparency < 0f || transparency > 1f)
			//throw new TGUIException(type.toString() + "[" + ID + "]: transparency " + transparency + " out of bounds");
		alpha = transparency;
		if (graphic != null)
			graphic.setAlpha(transparency);
		for (Item item : items)
			if (item.iGraphic != null)
				item.iGraphic.setAlpha(alpha);
		if (children != null)
			for (TGUIComponent child : children)
				child.setTransparency(transparency);
	}

	@Override
	public void scrollUp()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollDown()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sort(TESortDirection direction)
	{
		// TODO Auto-generated method stub
		
	}

	public int itemCount()
	{
		return items.size();
	}

	public void deselect()
	{
		selected = -1;
		changed = true;
	}
}