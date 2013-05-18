package tl.GUI;

import java.util.ArrayList;
import java.util.LinkedList;

import tl.Util.TPoint;

/**
 * TGUIView is an abstract class that provides the basic functionality for TGUIVerticalView and TGUIHorizontalView.<br>
 * TGUIViews set properties for each component in the layout, and positions/stretches/organises them to meet the size of its parent.<br>
 * If the parent is set to null, then the TGUIView uses the TGUIManager's properties instead.
 * @author Callum Nichols
 * @since 2.1
 * @see TGUIVBoxLayout
 * @see TGUIHBoxLayout
 */
public abstract class TGUILayout
{
	/**
	 * The spacing in pixels between each component.
	 * @see #setSpacing(int)
	 */
	protected int space;
	/**
	 * Whether or not the size of the components should be stretched to fill the layout.
	 * @see #toggleStretching()
	 */
	protected boolean stretch;
	/**
	 * The layout's parent. If set to null, the layout uses the TGUIManager's properties instead.
	 * @see #TGUIView(TGUIComponent, boolean)
	 * @see #setParent(TGUIComponent)
	 */
	protected TGUIComponent parent;
	/**
	 * The layout's collection of components.
	 * @see #clear()
	 * @see #getSize()
	 */
	protected LinkedList<TGUIComponent> components;
	/**
	 * Whether or not the layout should re-organise every time a new component is added.
	 * @see #TGUIView(TGUIComponent, boolean)
	 * @see #addComponent(TGUIComponent)
	 */
	boolean organiseOnAdd;
	/**
	 * The position, if any, that the layout should start from in the parent.
	 */
	TPoint position;
	
	/**
	 * TGUIView's constructor. Used to set its parent on construction.
	 * @param parent - The parent of this TGUIView. Can be set to null.
	 * @param onAdd - Whether or not the layout should re-organise every time a new component is added (true re-organises on add).
	 * @see #addComponent(TGUIComponent)
	 */
	public TGUILayout(TGUIComponent parent, boolean onAdd)
	{
		this.parent = parent;
		components = new LinkedList<>();
		space = 3;
		organiseOnAdd = onAdd;
		position = new TPoint();
	}
	
	/**
	 * Sets the parent of the layout. This will re-organise each component.
	 * @param parent - The parent of this TGUIView. Can be set to null.
	 */
	public void setParent(TGUIComponent parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Adds a component to the layout to be organised.<br>
	 * It will throw a TGUIException if component is null (note that is actually catches its own throw, but it will still exit the application gracefully).
	 * @param component - The component to add. It must not be null.
	 */
	public void addComponent(TGUIComponent component)
	{
		try
		{
			if (component == null)
				throw new TGUIException("component is NULL!");
		}
		catch (TGUIException e)
		{
			e.printStackTrace();
		}
		components.add(component);
		if (organiseOnAdd)
			pOrganise();
	}
	
	/**
	 * Adds multiple components to the layout, in the one call, to be organised.<br>
	 * It will throw a TGUIException if a component is null (note that is actually catches its own throw, but it will still exit the application gracefully).
	 * @param components - The components to add. None of them should be null.
	 */
	public void addComponent(TGUIComponent ... components)
	{
		try
		{
			for (TGUIComponent component : components)
			{
				if (component == null)
					throw new TGUIException("component is NULL!");
				else
				{
					this.components.add(component);
					if (organiseOnAdd)
						pOrganise();
				}
			}
		}
		catch (TGUIException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * A protected organise function. Made to be overridden by TGUIVerticalView and TGUIHorizontalView.
	 * @see TGUIVBoxLayout
	 * @see TGUIHBoxLayout
	 */
	protected void pOrganise() {}
	
	/**
	 * Used to manually re-organise the layout.
	 */
	public void organise()
	{
		pOrganise();
	}
	
	/**
	 * Sets the starting position on the parent to begin organising from (0, 0 by default).
	 * @param position - The new position.
	 */
	public void setPosition(TPoint position)
	{
		this.position.set(position);
	}
	
	/**
	 * Sets the spacing in pixels between each component.
	 * @param spacing - The spacing in pixels. If less than 0, it will throw a TGUIException.
	 * @throws TGUIException
	 */
	public void setSpacing(int spacing) throws TGUIException
	{
		if (spacing < 0)
			throw new TGUIException("spacing [" + spacing + "] must be higher than 0.");
		space = spacing;
	}
	
	/**
	 * Adds spacing in pixels to the current spacing.
	 * @param spacing - The spacing in pixels. If less than 0, it will throw a TGUIException.
	 * @throws TGUIException
	 */
	public void addSpacing(int spacing) throws TGUIException
	{
		if (spacing < 0)
			throw new TGUIException("spacing [" + spacing + "] must be higher than 0.");
		space += spacing;
	}
	
	/**
	 * Toggles component stretching off and on (off by default).
	 */
	public void toggleStretching()
	{
		stretch = !stretch;
	}
	
	/**
	 * @return Returns the number of components in the layout.
	 */
	public int getSize()
	{
		return components.size();
	}
	
	/**
	 * Removes the components from the layout.
	 */
	public void clear()
	{
		components.clear();
	}
}
