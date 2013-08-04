package tl.GUI;

import java.util.LinkedList;

import tl.Util.TPoint;
import tl.Util.TSize;

/**
 * TGUIView is an abstract class that provides the basic functionality for TGUIVerticalView and TGUIHorizontalView.<br>
 * TGUIViews set properties for each component in the layout, and positions/stretches/organises them to meet the size of its parent.<br>
 * If the parent is set to null, then the TGUIView uses the TGUIManager's properties instead.
 * @author Callum Nichols
 * @since 2.1
 * @see TGUIVBoxLayout
 * @see TGUIHBoxLayout
 */
public abstract class TGUILayout extends TGUIObject
{
	/**
	 * The spacing in pixels between each component. 3 by default.
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
	 * @see #setParent(TGUIObject)
	 */
	protected TGUIObject parent;
	/**
	 * The layout's collection of components.
	 * @see #clear()
	 */
	protected LinkedList<TGUIObject> components;
	/**
	 * Whether or not the layout should re-organise every time a new component is added.
	 * @see #TGUIView(TGUIComponent, boolean)
	 * @see #addComponent(TGUIObject)
	 */
	boolean organiseOnAdd;
	
	/**
	 * TGUIView's constructor. Used to set its parent on construction.
	 * @param parent - The parent of this TGUIView. Can be set to null.
	 * @param onAdd - Whether or not the layout should re-organise every time a new component is added (true re-organises on add).
	 * @see #addComponent(TGUIObject)
	 */
	public TGUILayout(TGUIObject parent, boolean onAdd)
	{
		this.parent = parent;
		components = new LinkedList<>();
		space = 3;
		organiseOnAdd = onAdd;
		//position = parent != null ? (parent instanceof TGUIComponent ? new TPoint() : parent.position) : new TPoint();
		//size = parent != null ? parent.size : new TSize(TGUIManager.screenWidth, TGUIManager.screenHeight);
		position = new TPoint();
		size = new TSize();
	}
	
	/**
	 * Sets the parent of the layout. This will re-organise each component.
	 * @param parent - The parent of this TGUIView. Can be set to null.
	 */
	public void setParent(TGUIObject parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Adds a component to the layout to be organised.<br>
	 * It will throw a TGUIException if component is null (note that is actually catches its own throw, but it will still exit the application gracefully).
	 * @param component - The component to add. It must not be null.
	 */
	public void addComponent(TGUIObject component)
	{
		try
		{
			if (component == null)
				throw new TGUIException("component is NULL!");
			
			components.add(component);
			if (organiseOnAdd)
				pOrganise();
		}
		catch (TGUIException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds multiple components to the layout, in the one call, to be organised.<br>
	 * It will throw a TGUIException if a component is null (note that is actually catches its own throw, but it will still exit the application gracefully).
	 * @param components - The components to add. None of them should be null.
	 */
	public void addComponent(TGUIObject ... components)
	{
		try
		{
			for (TGUIObject component : components)
			{
				if (component == null)
					throw new TGUIException("component is NULL!");
				
				this.components.add(component);
				if (organiseOnAdd)
					pOrganise();
			}
		}
		catch (TGUIException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a component to the layout to be organised at an index.<br>
	 * It will throw a TGUIException if component is null (note that is actually catches its own throw, but it will still exit the application gracefully).
	 * @param component - The component to add. It must not be null.
	 * @param index - The index to insert the component at.
	 */
	public void addComponent(TGUIObject component, int index)
	{
		try
		{
			if (component == null)
				throw new TGUIException("component is NULL!");
			if (index < 0 || index >= components.size())
				throw new TGUIException("index " + index + " out of bounds! [" + components.size() + "]");
			
			components.add(index, component);
			if (organiseOnAdd)
				pOrganise();
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
	 * Toggles organising the children upon adding them to the layout.
	 * @see #isOrganisingOnAdd()
	 */
	public void toggleOrganiseAdd()
	{
		organiseOnAdd = !organiseOnAdd;
	}
	
	/**
	 * Used to check if the layout is organising upon adding children.
	 * @return - True if the layout is organising upon adding children.
	 * @see #toggleOrganiseAdd()
	 */
	public boolean isOrganisingOnAdd()
	{
		return organiseOnAdd;
	}
	
	/**
	 * Toggles component stretching off and on (off by default).
	 * @see #isStretching()
	 */
	public void toggleStretching()
	{
		stretch = !stretch;
	}
	
	/**
	 * Used to tell whether or not the layout has stretching turned on.
	 * @return - True if the layout is stretching its objects.
	 * @see #toggleStretching()
	 */
	public boolean isStretching()
	{
		return stretch;
	}
	
	/**
	 * @return - Returns the number of components in the layout.
	 */
	public int childCount()
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
	
	/**
	 * Used by inheriting layout objects. 
	 * @return - The size of the parent object, depending on whether or not it is null.
	 */
	protected TSize getParentSize()
	{
		return new TSize(parent == null ? new TSize(TGUIManager.screenWidth, TGUIManager.screenHeight) : parent.getSize());
	}
	
	/**
	 * Used by inheriting layout objects.
	 * @param parentSize - Size of the parent.
	 * @param direction - Direction to organise, vertical or horizontal.
	 * @return - The size that each child must be if stretching is turned on, otherwise, 0.
	 */
	protected int childSizes(TSize parentSize, TEDirection direction) // total size each object must be to fit into the parent if stretching is on
	{
		int csize = components.size();
		float size = (((direction == TEDirection.TOP_TO_BOTTOM ? parentSize.height : parentSize.width) - ((csize + 1) * space)) / (float)components.size());
		if ((float)Math.ceil(size) * csize + (space * (csize + 1)) > parent.getSize().height)
			size = (float)Math.floor(size);
		else
			size = (float)Math.ceil(size);
		return (int)size;
	}
}
