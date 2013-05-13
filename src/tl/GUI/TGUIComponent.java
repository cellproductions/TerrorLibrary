package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.GUI.TGUIManager.GUIColor;
import tl.Util.TCursor;

public class TGUIComponent implements TGUIInterface, Comparable<TGUIComponent>
{
	protected Image graphic;
	protected Graphics canvas; // canvas used to draw the default textures onto the graphic
	protected TGUIComponent parent;
	protected ArrayList<TGUIComponent> children;
	protected int compcounter;
	protected int priority = 0; // controls are drawn from lowest priority to highest
	protected boolean changed = false;
	public final GUIColor background = GUIColor.GUI_MAIN;
	public final GUIColor border = GUIColor.GUI_BORDER;
	protected float alpha = 1f; // only for the constructor
	protected boolean setalpha;
	
	/* PROPERTIES_START */
	protected int ID;
	protected float x; // where on the gui the position of this control is
	protected float y;
	protected float gx; // where on the screen the position of this control is
	protected float gy;
	protected int width;
	protected int height;
	protected boolean visible;
	protected boolean enabled;
	public static final ComponentType type = ComponentType.component;
	/* PROPERTIES_END */

	protected TGUIClickedEvent mouseClick;
	protected TGUIMouseOverEvent mouseOver;

	public static enum ComponentType
	{
		component, button, buttonToggle, container, label, labelExtended, listBox, listBoxGen, listBoxDrop, listBoxDropGen, slider, textBox
	}
	
	public TGUIComponent()
	{
		enabled = true;
	}

	public TGUIComponent(TGUIComponent parent)
	{
		if (parent == null)
			ID = TGUIManager.numGUIs++;
		else
		{
			try
			{
				setParent(parent);
			}
			catch (TGUIException e)
			{
				e.printStackTrace();
			}
		}
		enabled = parent != null ? parent.enabled : true;
		visible = parent != null ? parent.visible : true;
	}

	public TGUIComponent(TGUIComponent parent, float i, float j, int w, int h)
	{
		this(parent);
		x = i;
		y = j;
		gx = (parent != null ? parent.x : 0) + x;
		gy = (parent != null ? parent.y : 0) + y;
		width = w;
		height = h;
		visible = true;
		changed = true;
	}
	
	public TGUIComponent(TGUIComponent parent, float i, float j, boolean visible, int w, int h, float t)
	{
		if (parent == null)
			ID = TGUIManager.numGUIs++;
		else
		{
			try
			{
				setParent(parent);
			}
			catch (TGUIException e)
			{
				e.printStackTrace();
			}
		}
		x = i;
		y = j;
		gx = (parent != null ? parent.x : 0) + x;
		gy = (parent != null ? parent.y : 0) + y;
		width = w;
		height = h;
		enabled = parent != null ? parent.enabled : true;
		this.visible = visible;
		changed = true;
		alpha = t;
		setalpha = true;
	}
	
	public int getID()
	{
		return ID;
	}
	
	private void updateC() throws SlickException
	{
		if (graphic == null)
			graphic = new Image(width, height);
		if (setalpha)
			graphic.setAlpha(alpha);
		canvas = graphic.getGraphics();
		canvas.setColor(background.get());
		canvas.fillRect(0, 0, width - 1, height - 1);
		canvas.setColor(border.get());
		canvas.drawRect(0, 0, width - 1, height - 1);
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
				updateC();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		
		if (graphic != null)
			if (visible && graphic.getAlpha() > 0.00f)
				g.drawImage(graphic, gx, gy);
		drawAll(g);
	}
	
	protected void drawAll(Graphics g)
	{
		if (children != null)
			for (TGUIComponent child : children)
				child.update(g);
	}

	public boolean mouseIsOver()
	{
		float x = TCursor.getX();
		float y = TCursor.getY();
		return x >= gx && x <= gx + width && y >= gy && y <= gy + height && isVisible();
	}

	public boolean mouseButtonDown(int button) // obsolete?
	{
		return TGUIManager.guiInput.isMouseButtonDown(button) && mouseIsOver() && enabled && visible;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getScreenX()
	{
		return gx;
	}
	
	public float getScreenY()
	{
		return gy;
	}

	public void setPosition(float i, float j)
	{
		x = i;
		y = j;
		gx = (parent != null ? parent.x : 0) + x;
		gy = (parent != null ? parent.y : 0) + y;
	}
	
	public int width()
	{
		return width;
	}
	
	public int height()
	{
		return height;
	}

	public void setSize(int w, int h)
	{
		width = w;
		height = h;
		graphic = null;
		changed = true;
	}

	/*
	 * For literal use only. Is this control actually visible to the player?
	 */
	protected boolean isVisible()
	{
		boolean alpha = false;
		if (graphic != null)
			alpha = graphic.getAlpha() > 0.00f;
		return visible && alpha;
	}

	public boolean getVisibility()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
		if (children != null)
			for (TGUIComponent child : children)
				child.setVisible(visible);
	}
	
	public void setTransparency(float transparency) throws TGUIException
	{
		if (transparency < 0f || transparency > 1f)
			throw new TGUIException(type.toString() + "[" + ID + "]: transparency " + transparency + " out of bounds");
		if (graphic != null)
			graphic.setAlpha(transparency);
		if (children != null)
			for (TGUIComponent child : children)
				child.setTransparency(transparency);
	}
	
	public float getTransparency()
	{
		return graphic != null ? graphic.getAlpha() : 1f;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		if (children != null)
			for (TGUIComponent child : children)
				child.setEnabled(enabled);
	}
	
	public boolean getEnabled()
	{
		return enabled;
	}
	
	protected void setProperties(TGUIComponent parent)
	{
		boolean notnull = parent != null;
		gx = (notnull ? parent.x : 0) + x;
		gy = (notnull ? parent.y : 0) + y;
		visible = (notnull ? parent.visible : true);
		enabled = (notnull ? parent.enabled : true);
		if (graphic != null)
			graphic.setAlpha(notnull ? (parent.graphic  != null ? parent.graphic.getAlpha() : 1f) : 1f);
	}
	
	public TGUIComponent child(int index) throws TGUIException
	{
		if (children == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: component has no children!");
		if (index < 0 || index >= children.size())
			throw new TGUIException(type.toString() + "[" + ID + "]: index " + index + " out of bounds! [" + children.size() + "]");
		return children.get(index);
	}
	
	public void addComponent(TGUIComponent child)
	{
		try
		{
			if (child == null)
				throw new TGUIException(type.toString() + "[" + ID + "]: child component is NULL!");
			if (child.parent != this)
				throw new TGUIException(type.toString() + "[" + ID + "]: childs[" + child.ID + "] parent has not been set as this component!");
			if (children == null)
				children = new ArrayList<TGUIComponent>();
			if (!children.contains(child))
			{
				child.setProperties(this);
				children.add(child);
				int id = 0;
				for (TGUIComponent c : children)
					c.ID = id++;
			}
		}
		catch(TGUIException e)
		{
			e.printStackTrace();
		}
	}
	
	public void removeComponent(TGUIComponent child) throws TGUIException
	{
		if (children == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: component has no children!");
		if (child != null)
			child.parent = null;
		children.remove(child);
		child.setPosition(child.x, child.y);
	}
	
	public void removeComponent(int index) throws TGUIException
	{
		if (children == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: component has no children!");
		if (index < 0 || index >= children.size())
			throw new TGUIException(type.toString() + "[" + ID + "]: index " + index + " out of bounds! [" + children.size() + "]");
		TGUIComponent child = children.get(index);
		child.parent = null;
		child.setProperties(null);
		children.remove(index);
	}
	
	public int childCount()
	{
		return children != null ? children.size() : 0;
	}
	
	public TGUIComponent getParent()
	{
		return parent;
	}
	
	public void setParent(TGUIComponent parent) throws TGUIException
	{
		if (parent == null)
		{
			if (this.parent != null)
				this.parent.removeComponent(this);
			setProperties(null);
			return;
		}
		if (this.parent != null)
			this.parent.children.remove(this);
		this.parent = parent;
		parent.addComponent(this);
		setProperties(parent);
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
	}
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}

	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (mouseIsOver())
			{
				if (mouseClick != null)
					mouseClick.execute(button, x, y, this);
			}
			if (children != null)
				for (TGUIComponent child : children)
					child.mousePressed(button, x, y);
		}
	}

	public void mouseReleased(int button, int x, int y)
	{
		if (enabled)
			if (children != null)
				for (TGUIComponent child : children)
					child.mouseReleased(button, x, y);
	}

	public void mouseWheelMoved(int change)
	{
		if (enabled)
			if (children != null)
				for (TGUIComponent child : children)
					child.mouseWheelMoved(change);
	}

	public void keyPressed(int key, char c)
	{
		if (enabled)
			if (children != null)
				for (TGUIComponent child : children)
					child.keyPressed(key, c);
	}

	public void keyReleased(int key, char c)
	{
		if (enabled)
			if (children != null)
				for (TGUIComponent child : children)
					child.keyReleased(key, c);
	}

	public int compareTo(TGUIComponent arg0)
	{
		return priority < arg0.priority ? -1 : (priority == arg0.priority ? 0 : 1);
	}
}