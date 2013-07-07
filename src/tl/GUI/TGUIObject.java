package tl.GUI;

import tl.Util.TPoint;
import tl.Util.TSize;

/**
 * TGUIObject is an abstract class extended by most TGUI objects (i.e. TGUIComponent, TGUILayout, etc.)
 * 
 * @author Callum Nichols
 * @since 2.1
 * @see TGUIComponent
 * @see TGUILayout
 */
public class TGUIObject
{
	/**
	 * The x and y position of this object.
	 */
	protected TPoint position;
	/**
	 * The width and height of this object.
	 */
	protected TSize size;
	
	public TGUIObject()
	{
		position = new TPoint();
		size = new TSize();
	}
	
	public TGUIObject(TPoint position, TSize size)
	{
		this.position = new TPoint(position);
		this.size = new TSize(size);
	}
	
	public void setPosition(float x, float y)
	{
		position.set(x, y);
	}
	
	public void setPosition(TPoint position)
	{
		this.position.set(position);
	}
	
	public TPoint getPosition()
	{
		return new TPoint(position);
	}
	
	public void setSize(int width, int height)
	{
		size.set(width, height);
	}
	
	public void setSize(TSize size)
	{
		this.size.set(size);
	}
	
	public TSize getSize()
	{
		return new TSize(size);
	}
}
