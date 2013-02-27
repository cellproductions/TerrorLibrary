package tl.GUIutil;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import tl.Util.Cursor;

public class GUIControl implements GUIInterface
{
	public float x; // where on the gui the position of this control is
	public float y;
	public float gx; // where on the screen the position of this control is
	public float gy;
	public int width;
	public int height;
	protected boolean visible;
	public boolean enabled;
	public Image graphic;
	protected Graphics canvas; // canvas used to draw the default textures onto the graphic
	public int ID;
	protected GUI owningGUI;
	public ControlType type;
	public int priority = 0; // controls are drawn from lowest priority to highest

	protected GUIClickedFunction mouseClick;
	protected GUIMouseOverFunction mouseOver;

	public static enum ControlType
	{
		button, buttonToggle, container, label, labelExtended, listBox, listBoxDrop, slider, textBox
	}

	protected boolean changed = false;

	public GUIControl()
	{
		x = 0;
		y = 0;
		gx = 0;
		gy = 0;
		width = 0;
		height = 0;
		enabled = true;
		graphic = null;
	}

	public GUIControl(int i, int j, int w, int h)
	{
		x = i;
		y = j;
		width = w;
		height = h;
		enabled = true;
	}

	public void setGUI(GUI g)
	{
		owningGUI = g;
		gx = owningGUI.x + x;
		gy = owningGUI.y + y;
		ID = owningGUI.numControls;
		owningGUI.numControls++;
	}

	public void update(Graphics g)
	{

	}

	public boolean mouseIsOver()
	{
		return Cursor.getX() >= gx && Cursor.getX() <= gx + width && Cursor.getY() >= gy && Cursor.getY() <= gy + height && isVisible();
	}

	public boolean mouseButtonDown(int button) // obsolete?
	{
		return GUIManager.guiInput.isMouseButtonDown(button) && mouseIsOver() && enabled && visible;
	}

	public void setPosition(int i, int j)
	{
		x = i;
		y = j;
		gx = owningGUI.x + x;
		gy = owningGUI.y + y;
	}

	public void setSize(int w, int h)
	{
		width = w;
		height = h;
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
	}

	@Override
	public void mousePressed(int button, int x, int y)
	{
	}

	@Override
	public void mouseReleased(int button, int x, int y)
	{
	}

	@Override
	public void mouseWheelMoved(int change)
	{
	}

	@Override
	public void keyPressed(int key, char c)
	{
	}

	@Override
	public void keyReleased(int key, char c)
	{
	}
}