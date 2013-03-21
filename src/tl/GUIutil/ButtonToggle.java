package tl.GUIutil;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ButtonToggle extends GUIControl
{
	public String text;
	private List<String> toggles;
	public int index;
	public int oldIndex;
	
	GUISelectionFunction selectionChange;

	public ButtonToggle()
	{
		super();
		type = GUIControl.ControlType.buttonToggle;
		text = null;
		toggles = new LinkedList<String>();
	}
	
	public ButtonToggle(int x, int y, int w, int h) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.buttonToggle;
		text = "";
		index = oldIndex = 0;
		graphic = new Image(w, h);
		toggles = new LinkedList<String>();
		changed = true;
	}

	@SuppressWarnings("deprecation")
	private void updateB() throws SlickException
	{
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(GUIManager.guiFont);
		canvas.setColor(new Color(160, 160, 160));
		canvas.fillRect(0, 0, width, height);
		canvas.setColor(new Color(255, 255, 255));
		canvas.drawLine(0, 0, width, 0);
		canvas.drawLine(0, 0, 0, height);
		canvas.setColor(new Color(70, 70, 70));
		canvas.drawLine(width - 1, 1, width - 1, height);
		canvas.drawLine(1, height - 1, width, height - 1);
		canvas.setColor(new Color(0, 0, 0));
		canvas.drawString(text, width / 2 - (GUIManager.guiFont.getWidth(text) / 2), height / 2 - (GUIManager.guiFont.getHeight(text) / 2));
		canvas.flush();
	}

	public void update(Graphics g)
	{
		if (graphic != null)
			graphic.setAlpha(owningGUI.graphic.getAlpha());
		
		if (hasChanged() > -1)
		{
			if (selectionChange != null)
				selectionChange.execute(index, this);
			changed = true;
		}
		
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
				updateB();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		
		if (graphic != null)
			if (visible && graphic.getAlpha() > 0.00F)
				g.drawImage(graphic, gx, gy);
	}
	
	public void mousePressed(int button, int x, int y)
	{
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		if (enabled)
		{
			if (mouseIsOver())
			{
				if (button == 0 || button == 1)
				{
					if (button == 0)
						next();
					else if (button == 1)
						prev();
				}
				if (mouseClick != null)
					mouseClick.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void onMouseClick(GUIClickedFunction function) // overloadable click (whatever user wants to do with it)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(GUIMouseOverFunction function)
	{
		mouseOver = function;
	}
	
	public void onToggle(GUISelectionFunction function)
	{
		selectionChange = function;
	}
	
	private int hasChanged()
	{
		if (oldIndex != index)
			return (oldIndex = index);
		return -1;
	}
	
	public void addItem(String item)
	{
		if (toggles.isEmpty())
			text = item;
		toggles.add(item);
	}
	
	public void setIndex(int index)
	{
		if (index > -1 && index < toggles.size())
		{
			oldIndex = this.index;
			this.index = index;
			text = toggles.get(index);
			changed = true;
		}
	}
	
	public void next()
	{
		if (++index == toggles.size())
			index = 0;
		text = toggles.get(index);
		changed = true;
	}
	
	public void prev()
	{
		if (--index < 0)
			index = toggles.size() - 1;
		text = toggles.get(index);
		changed = true;
	}
}