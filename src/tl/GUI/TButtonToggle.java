package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TButtonToggle extends TGUIComponent
{
	protected String text;
	private ArrayList<String> toggles;
	protected int index;
	public final Color background = new Color(TGUIManager.BUTTON_MAIN);
	public final Color border_white = new Color(TGUIManager.WHITE);
	public final Color border_grey = new Color(TGUIManager.BUTTON_BORDER);
	public final Color font_colour = new Color(TGUIManager.BLACK);
	
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
	
	protected void change()
	{
		background.a = alpha;
		border_white.a = alpha;
		border_grey.a = alpha;
		font_colour.a = alpha;
		changed = false;
	}

	@SuppressWarnings("deprecation")
	protected void draw(Graphics g) throws SlickException
	{
		/*if (graphic == TGUIManager.emptyImage)
			graphic = new Image(size.width, size.height);
		canvas = graphic.getGraphics();
		canvas.clear();
		g.setFont(TGUIManager.guiFont);
		g.setColor(TGUIManager.BUTTON_MAIN);
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(TGUIManager.WHITE);
		g.drawLine(0, 0, size.width, 0);
		g.drawLine(0, 0, 0, size.height);
		g.setColor(TGUIManager.BUTTON_BORDER);
		g.drawLine(size.width - 1, 1, size.width - 1, size.height);
		g.drawLine(1, size.height - 1, size.width, size.height - 1);
		g.setColor(TGUIManager.BLACK);
		g.drawString(text, size.width / 2 - (TGUIManager.guiFont.getWidth(text) / 2), size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2));
		//canvas.flush();*/
		int widthpos = (int)(screenPos.x + size.width);
		int heightpos = (int)(screenPos.y + size.height);
		g.setFont(TGUIManager.guiFont);
		g.setColor(background);
		g.fillRect(screenPos.x, screenPos.y, size.width, size.height);
		g.setColor(border_white);
		g.drawLine(screenPos.x, screenPos.y, widthpos, screenPos.y);
		g.drawLine(screenPos.x, screenPos.y, screenPos.x, heightpos);
		g.setColor(border_grey);
		g.drawLine(widthpos, screenPos.y + 1, widthpos, heightpos);
		g.drawLine(screenPos.x + 1, heightpos, widthpos, heightpos);
		g.setColor(font_colour);
		g.drawString(text, screenPos.x + (size.width / 2 - (TGUIManager.guiFont.getWidth(text) / 2)), screenPos.y + (size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2)));
		g.setColor(TGUIManager.BLACK);
	}
	
	protected boolean isOver = false; // JUST FOR 7dRTS

	public void update(Graphics g)
	{
		if (mouseIsOver())
		{
			if (mouseOver != null && !isOver)
			{
				mouseOver.execute(this);
				changed = true;
				isOver = true;
			}
		}
		else
			isOver = false;
		
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