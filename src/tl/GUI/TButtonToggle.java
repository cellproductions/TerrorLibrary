package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TButtonToggle extends TGUIComponent
{
	protected String text;
	private ArrayList<String> toggles;
	protected int index;
	
	TGUIClickedEvent mousePress;
	TGUISelectionEvent selectionChange;

	public TButtonToggle()
	{
		super();
		type = ComponentType.buttonToggle;
	}
	
	public TButtonToggle(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.buttonToggle;
		text = "";
		toggles = new ArrayList<String>();
	}
	
	public TButtonToggle(TGUIComponent parent, float x, float y, int width, int height) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.buttonToggle;
		text = "";
		toggles = new ArrayList<String>();
		changed = true;
	}

	@SuppressWarnings("deprecation")
	private void updateB() throws SlickException
	{
		if (graphic == TGUIManager.emptyImage)
			graphic = new Image(size.width, size.height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(TGUIManager.BUTTON_MAIN);
		canvas.fillRect(0, 0, size.width, size.height);
		canvas.setColor(TGUIManager.WHITE);
		canvas.drawLine(0, 0, size.width, 0);
		canvas.drawLine(0, 0, 0, size.height);
		canvas.setColor(TGUIManager.BUTTON_BORDER);
		canvas.drawLine(size.width - 1, 1, size.width - 1, size.height);
		canvas.drawLine(1, size.height - 1, size.width, size.height - 1);
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawString(text, size.width / 2 - (TGUIManager.guiFont.getWidth(text) / 2), size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2));
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
				updateB();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		
		if (graphic != null)
			if (visible && alpha > 0.00F)
				g.drawImage(graphic, screenPos.x, screenPos.y);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (mouseIsOver())
			{
				if (mousePress != null)
					mousePress.execute(button, x, y, this);
				changed = true;
			}
		}
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
				if (mouseRelease != null)
					mouseRelease.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void onToggle(TGUISelectionEvent function)
	{
		selectionChange = function;
	}
	
	public void addItem(String item)
	{
		if (toggles.isEmpty())
			text = item;
		toggles.add(item);
	}
	
	public void removeItem(String item) throws TGUIException
	{
		if (!toggles.remove(item))
			throw new TGUIException("item " + item + " does not exist!");
		else
			prev();
	}
	
	public void removeItem(int index) throws TGUIException
	{
		if (index < 0 || index >= toggles.size())
			throw new TGUIException("index " + index + " out of bounds!");
		toggles.remove(index);
		prev();
	}
	
	public String getItem(int index) throws TGUIException
	{
		if (index < 0 || index >= toggles.size())
			throw new TGUIException("index " + index + " out of bounds!");
		return toggles.get(index);
	}
	
	public void setIndex(int index) throws TGUIException
	{
		if (index < 0 || index >= toggles.size())
			throw new TGUIException("index " + index + " out of bounds!");
		
		this.index = index;
		text = toggles.get(index);
		if (selectionChange != null)
			selectionChange.execute(this.index, this);
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