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
public abstract class TGUIObject
{
	/**
	 * The x and y position of this object.
	 */
	protected TPoint position;
	/**
	 * The width and height of this object.
	 */
	protected TSize size;
}
