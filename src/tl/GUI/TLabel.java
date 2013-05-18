package tl.GUI;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author Callum
 *
 */
/**
 * @author Callum
 *
 */
/**
 * @author Callum
 *
 */
public class TLabel extends TGUIComponent
{
	protected String text;
	protected String oldText;
	protected String toDraw;
	
	protected TGUITextEvent textChange;
	
	public TLabel()
	{
		super();
		type = ComponentType.label;
	}

	public TLabel(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.label;
		text = oldText = "";
	}

	public TLabel(TGUIComponent parent, float x, float y, int w, int h, String t) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.label;
		text = oldText = t;
		changed = true;
	}

	private void updateLabel() throws SlickException
	{
		toDraw = text;
		ArrayList<String> list = new ArrayList<String>();
		if (toDraw.contains("[["))
		{
			while (toDraw.contains("[["))
			{
				int i = toDraw.indexOf("[[");
				list.add(toDraw.substring(0, i));
				toDraw = toDraw.substring(i + 2);
			}
			list.add(toDraw);
		}
		else
			list.add(toDraw);
		
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(new Color(0, 0, 0));
		Iterator<String> itr = list.iterator();
		for (int i = 0; itr.hasNext(); i++)
			canvas.drawString(itr.next(), 0, i * 24); //Config.font.getHeight(list.get(i)));// h / 2 - (Config.font.getHeight(t) / 2));
		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, width - 1, height - 1);
		}
		canvas.flush();
	}

	public void update(Graphics g)
	{
		if (!oldText.contentEquals(text))
		{
			oldText = text;
			if (textChange != null)
			{
				textChange.execute(text, this);
				changed = true;
			}
		}
		
		try
		{
			if (changed)
			{
				updateLabel();
				changed = false;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		if (visible && graphic.getAlpha() > 0.00F)
			g.drawImage(graphic, gx, gy);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (mouseClick != null)
			{
				mouseClick.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}
	
	public void onTextChange(TGUITextEvent function)
	{
		textChange = function;
	}
	
	public int getWidthOfText()
	{
		String tmp = text;
		ArrayList<String> list = new ArrayList<String>();
		if (tmp.contains("[["))
		{
			while (tmp.contains("[["))
			{
				int i = tmp.indexOf("[[");
				list.add(tmp.substring(0, i));
				tmp = tmp.substring(i + 2);
			}
			list.add(tmp);
		}
		else
			list.add(tmp);
		
		int biggest = 0;
		for (String s : list)
		{
			@SuppressWarnings("deprecation")
			int size = TGUIManager.guiFont.getWidth(s);
			if (biggest < size)
				biggest = size;
		}
		return biggest;
	}
	
	public void setText(String text)
	{
		oldText = this.text;
		this.text = text;
		changed = true;
	}
	
	public String getText()
	{
		return text;
	}
}