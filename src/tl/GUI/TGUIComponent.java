package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.Util.TCursor;
import tl.Util.TPoint;
import tl.Util.TSize;

/**
 * TGUIComponent is the base class from which all other TGUIComponents derive from.<br>
 * The TGUIComponent can be inherited to create almost any other user defined TGUI.
 * 
 * @author Callum Nichols
 * @version 2.0
 */
public class TGUIComponent extends TGUIObject implements TIGUIInterface, Comparable<TGUIComponent>
{
	protected Image graphic;
	//protected Graphics canvas; // canvas used to draw the default textures onto the graphic
	protected TGUIComponent parent;
	protected ArrayList<TGUIComponent> children;
	protected int compcounter;
	protected int priority = 0; // controls are drawn from lowest priority to highest [OBSOLETE?]
	protected boolean changed = false;
	public final Color background = new Color(TGUIManager.GUI_MAIN);
	public final Color border = new Color(TGUIManager.GUI_BORDER);
	protected float alpha = 1f; // only for the constructor
	
	/* PROPERTIES_START */
	/**
	 * TGUIComponent ID relative to its parent (relative to the TGUIManager if parent is null).
	 * @see #getID()
	 */
	protected int ID;
	
	/**
	 * The x and y position of the component on the screen.
	 * @see #getScreenX()
	 * @see #getScreenY()
	 * @see #setPosition(float, float)
	 */
	protected TPoint screenPos;
	/**
	 * Whether or not the component is visible to the user (true if visible).
	 * @see #getVisibility()
	 * @see #setVisible(boolean)
	 */
	protected boolean visible;
	/**
	 * Whether or not the component is enabled for use (true if enabled).
	 * @see #getEnabled()
	 * @see #setEnabled(boolean)
	 */
	protected boolean enabled;
	/**
	 * A type that represents what kind of component the TGUIComponent is.
	 * @see ComponentType
	 */
	protected ComponentType type;
	/* PROPERTIES_END */

	/**
	 * An interface instance that is used to run a function upon detecting a mouse button being pressed down over a TGUIComponent.
	 * @see TGUIClickedEvent
	 * @see #onMousePressed(TGUIClickedEvent)
	 */
	protected TGUIClickedEvent mousePress;
	/**
	 * An interface instance that is used to run a function upon detecting a mouse button release while over a TGUIComponent.
	 * @see TGUIClickedEvent
	 * @see #onMouseReleased(TGUIClickedEvent)
	 */
	protected TGUIClickedEvent mouseRelease;
	/**
	 * An interface instance that is used to run a function upon detecting a cursor positioned over a TGUIComponent.
	 * @see TGUIMouseOverEvent
	 * @see #onMouseOver(TGUIMouseOverEvent)
	 */
	protected TGUIMouseOverEvent mouseOver;

	/**
	 * ComponentType represents a type of TGUIComponent for easy comparison and removal of ambiguity.<br>
	 * E.g.<br><br>
	 * <pre>
	 * {@code
	 * void checkComponent(TGUIComponent comp)
	 * {
	 * 	// if comp is a TGUIComponent (and not a TButton or any other type of component)
	 * 	if (comp.type == TGUIComponent.type)
	 * 		// do something
	 * }
	 * TButton button = new TButton();
	 * checkComponent(button);
	 * }
	 * </pre>
	 * @author Callum Nichols
	 * @since 1.5
	 */
	public static enum ComponentType
	{
		component, button, buttonToggle, container, label, labelExtended, listBox, listBoxGen, listBoxDrop, listBoxDropGen, slider, textBox
	}
	
	public TGUIComponent()
	{
		type = ComponentType.component;
		graphic = TGUIManager.emptyImage;
		enabled = true;
		position = new TPoint();
		screenPos = new TPoint();
		size = new TSize();
	}

	public TGUIComponent(TGUIComponent parent)
	{
		type = ComponentType.component;
		graphic = TGUIManager.emptyImage;
		position = new TPoint();
		screenPos = new TPoint();
		size = new TSize();
		if (parent == null)
			ID = TGUIManager.numGUIs++;
		else
			parent.addComponent(this);
		enabled = parent != null ? parent.enabled : true;
		visible = parent != null ? parent.visible : true;
	}

	public TGUIComponent(TGUIComponent parent, float x, float y, int width, int height)
	{
		this(parent);
		position = new TPoint(x, y);
		screenPos = new TPoint(parent != null ? parent.getX() : 0, parent != null ? parent.getY() : 0);
		screenPos = screenPos.add(position);
		size = new TSize(width, height);
		changed = true;
	}
	
	public TGUIComponent(TGUIComponent parent, float x, float y, boolean visible, int width, int height, float t)
	{
		this();
		if (parent == null)
			ID = TGUIManager.numGUIs++;
		else
			parent.addComponent(this);
		position.set(x, y);
		screenPos.set(parent != null ? parent.getX() : 0, parent != null ? parent.getY() : 0);
		screenPos = screenPos.add(position);
		size.set(width, height);
		enabled = parent != null ? parent.enabled : true;
		this.visible = visible;
		changed = true;
		alpha = t;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public ComponentType getType()
	{
		return type;
	}
	
	protected void change()
	{
		background.a = alpha;
		border.a = alpha;
		changed = false;
	}
	
	protected void draw(Graphics g) throws SlickException
	{/*
		if (graphic == TGUIManager.emptyImage)
		{
			graphic = new Image(size.width, size.height);
			graphic.setAlpha(alpha);
		}
		canvas = graphic.getGraphics();
		canvas.setColor(background);
		canvas.fillRect(0, 0, size.width - 1, size.height - 1);
		canvas.setColor(border);
		canvas.drawRect(0, 0, size.width - 1, size.height - 1);
		canvas.flush();*/
		g.setColor(background);
		g.fillRect(screenPos.x + 1, screenPos.y + 1, size.width - 1, size.height - 1);
		g.setColor(border);
		g.drawRect(screenPos.x, screenPos.y, size.width, size.height);
		g.setColor(TGUIManager.BLACK);
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
		if (children != null)
		{
			for (TGUIComponent child : children)
				if (child.mouseIsOver())
					return false;
		}
		return mOver();
	}
	
	/**
	 * Protected mouseIsOver() function, called by mouseIsOver().
	 * @return - whether or not the cursor is over the component
	 * @see #mouseIsOver()
	 */
	protected boolean mOver()
	{
		float x = TCursor.getX();
		float y = TCursor.getY();
		return x >= screenPos.x && x <= screenPos.x + size.width && y >= screenPos.y && y <= screenPos.y + size.height && isVisible();
	}

	public boolean mouseButtonDown(int button) // obsolete?
	{
		return TGUIManager.guiInput.isMouseButtonDown(button) && mouseIsOver() && enabled && visible;
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public float getScreenX()
	{
		return screenPos.x;
	}
	
	public float getScreenY()
	{
		return screenPos.y;
	}
	
	public TPoint getScreenPosition()
	{
		return new TPoint(screenPos.x, screenPos.y);
	}

	public void setPosition(float x, float y)
	{
		position.set(x, y);
		screenPos.set(parent != null ? parent.getX() : 0, parent != null ? parent.getY() : 0);
		screenPos = screenPos.add(position);
		if (children != null)
			for (TGUIComponent child : children)
				child.setPosition(child.getX(), child.getY());
	}
	
	public void setPosition(TPoint position)
	{
		this.position.set(position);
		screenPos.set(parent != null ? parent.getX() : 0, parent != null ? parent.getY() : 0);
		screenPos = screenPos.add(position);
		if (children != null)
			for (TGUIComponent child : children)
				child.setPosition(child.getPosition());
	}
	
	public int width()
	{
		return size.width;
	}
	
	public int height()
	{
		return size.height;
	}

	public void setSize(int width, int height)
	{
		size.set(width, height);
		graphic = TGUIManager.emptyImage;
		changed = true;
	}
	
	public void setSize(TSize size)
	{
		this.size.set(size);
		graphic = TGUIManager.emptyImage;
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
		//if (transparency < 0f || transparency > 1f)
			//throw new TGUIException(type.toString() + "[" + ID + "]: transparency " + transparency + " out of bounds");
		alpha = transparency;
		if (graphic != null)
			graphic.setAlpha(transparency);
		changed = true;
		if (children != null)
			for (TGUIComponent child : children)
				child.setTransparency(transparency);
	}
	
	public float getTransparency()
	{
		return alpha;
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
		screenPos.set(parent != null ? parent.getX() : 0, parent != null ? parent.getY() : 0);
		screenPos = screenPos.add(position);
		visible = (notnull ? parent.visible : true);
		enabled = (notnull ? parent.enabled : true);
		if (graphic != null)
			graphic.setAlpha(notnull ? (parent.graphic  != null ? parent.graphic.getAlpha() : 1f) : 1f);
	}
	
	public synchronized TGUIComponent child(int index) throws TGUIException
	{
		if (children == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: component has no children!");
		if (index < 0 || index >= children.size())
			throw new TGUIException(type.toString() + "[" + ID + "]: index " + index + " out of bounds! [" + children.size() + "]");
		return children.get(index);
	}
	
	public synchronized void addComponent(TGUIComponent child)
	{
		if (child == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: child component is NULL!");
		//if (child.parent != this)
			//throw new TGUIException(type.toString() + "[" + ID + "]: child's[" + child.ID + "] parent[" + (parent == null ? "null" : parent.ID) + "] has not been set as this component!");
		if (children == null)
			children = new ArrayList<TGUIComponent>();
		if (!children.contains(child))
		{
			if (child.parent != null && child.parent != this)
				child.parent.children.remove(child);
			child.parent = this;
			child.setProperties(this);
			children.add(child);
			int id = 0;
			for (TGUIComponent c : children)
				c.ID = id++;
		}
	}
	
	public synchronized void removeComponent(TGUIComponent child) throws TGUIException
	{
		if (children == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: component has no children!");
		if (child == null)
			throw new TGUIException(type.toString() + "[" + ID + "]: child component is NULL!");
		else
			child.parent = null;
		children.remove(child);
		child.setPosition(child.getX(), child.getY());
	}
	
	public synchronized void removeComponent(int index) throws TGUIException
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
	
	public synchronized void clearChildren()
	{
		if (children != null)
		{
			while (!children.isEmpty())
			{
				TGUIComponent child = children.get(0);
				child.parent = null;
				child.setProperties(null);
				children.remove(0);
			}
		}
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
			if (this.parent.children.contains(this))
				this.parent.children.remove(this);
		parent.addComponent(this);
	}
	
	public void setColour(Color toChange, Color newColor)
	{
		toChange.a = newColor.a;
		toChange.r = newColor.r;
		toChange.g = newColor.g;
		toChange.b = newColor.b;
		changed = true;
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
	}
	
	public void onMousePress(TGUIClickedEvent function)
	{
		mousePress = function;
	}
	
	public void onMouseRelease(TGUIClickedEvent function)
	{
		mouseRelease = function;
	}

	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (mouseIsOver())
			{
				if (mousePress != null)
					mousePress.execute(button, x, y, this);
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