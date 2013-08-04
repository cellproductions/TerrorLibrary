package tl.GUI;

import org.newdawn.slick.Graphics;

/**
 * TGUIInterface is the main interface for all TGUIComponents.<br> It provides TGUIComponents the 
 * ability to update(draw, etc.) and to accept input commands.<br>
 * The functions provided are to be used in conjunction with Slick2D's update/render/command functions.
 * @author Callum Nichols
 * @since 1.5
 * @see TGUIComponent
 */
public interface TIGUIInterface
{
	/**
	 * Used to update upon a keyboard button being released.<br>
	 * Is usually called after keyPressed(int key, char c);
	 * @param key - The keyboard button ID that is being pressed.
	 * @param c - The character that the keyboard button represents (if any).
	 * @see #keyPressed(int, char)
	 */
	public void keyReleased(int key, char c);
	
	
	/**
	 * Used to update upon a keyboard button being pressed down.<br>
	 * Is usually called before keyReleased(int key, char c);
	 * @param key - The keyboard button ID that is being pressed.
	 * @param c - The character that the keyboard button represents (if any).
	 * @see #keyReleased(int, char)
	 */
	public void keyPressed(int key, char c);
	
	
	/**
	 * Used to update upon the middle mouse wheel being scrolled in any direction.
	 * @param change - The length in degrees that the mouse wheel moved. 0-360 = north, 0--360 = south
	 */
	public void mouseWheelMoved(int change);
	
	
	/**
	 * Used to update upon a mouse button being released.<br>
	 * Is usually called after mousePressed(int button, int x, int y).
	 * @param button - The button ID that is being pressed. 0 - LMB, 1 - RMB, 2 - MMB
	 * @param x - The x co-ordinate that the button was pressed on.
	 * @param y - The y co-ordinate that the button was pressed on.
	 * @see #mousePressed(int, int, int)
	 */
	public void mouseReleased(int button, int x, int y);
	
	
	/**
	 * Used to update upon a mouse button being pressed down.<br>
	 * Is usually called before mouseReleased(int button, int x, int y).
	 * @param button - The button ID that is being pressed. 0 - LMB, 1 - RMB, 2 - MMB
	 * @param x	- The x co-ordinate that the button was pressed on.
	 * @param y	- The y co-ordinate that the button was pressed on.
	 * @see #mouseReleased(int, int, int)
	 */
	public void mousePressed(int button, int x, int y);
	
	
	/**
	 * The main update function used to update and draw the TGUIComponent.<br>
	 * It takes a Graphics object to draw on.
	 * @param graphics - The graphics context on which to draw the TGUIComponent
	 */
	public void update(Graphics graphics);
}