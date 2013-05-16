package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TButtonToggle extends TGUIComponent
{
	public static ComponentType type = ComponentType.buttonToggle;
	public String text;
	private ArrayList<String> toggles;
	public int index;
	public int oldIndex;
	
	TGUISelectionEvent selectionChange;

	public TButtonToggle()
	{
		super();
	}
	
	public TButtonToggle(TGUIComponent parent)
	{
		super(parent);
		text = "";
		toggles = new ArrayList<String>();
	}
	
	public TButtonToggle(TGUIComponent parent, float x, float y, int w, int h) throws SlickException
	{
		super(parent, x, y, w, h);
		text = "";
		toggles = new ArrayList<String>();
		changed = true;
	}

	@SuppressWarnings("deprecation")
	private void updateB() throws SlickException
	{
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(TGUIManager.BUTTON_MAIN);
		canvas.fillRect(0, 0, width, height);
		canvas.setColor(TGUIManager.WHITE);
		canvas.drawLine(0, 0, width, 0);
		canvas.drawLine(0, 0, 0, height);
		canvas.setColor(TGUIManager.BUTTON_BORDER);
		canvas.drawLine(width - 1, 1, width - 1, height);
		canvas.drawLine(1, height - 1, width, height - 1);
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawString(text, width / 2 - (TGUIManager.guiFont.getWidth(text) / 2), height / 2 - (TGUIManager.guiFont.getHeight(text) / 2));
		canvas.flush();
	}

	public void update(Graphics g)
	{
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
	
	public void onMouseClick(TGUIClickedEvent function) // overloadable click (whatever user wants to do with it)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
	}
	
	public void onToggle(TGUISelectionEvent function)
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
	
	public void setIndex(int index) throws TGUIException
	{
		if (index < 0 || index >= toggles.size())
			throw new TGUIException("index " + index + " out of bounds!");
		
		oldIndex = this.index;
		this.index = index;
		text = toggles.get(index);
		changed = true;
	}
	
	public int getIndex()
	{
		return index;
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