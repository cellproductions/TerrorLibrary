package tl.GUIutil;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Label extends GUIControl
{
	protected String text;
	protected String oldText;
	protected String toDraw;
	
	protected GUITextFunction textChange;

	public Label()
	{
		super();
		type = GUIControl.ControlType.label;
		text = oldText = null;
		toDraw = null;
		priority = 0;
	}

	public Label(int x, int y, int w, int h, String t) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.label;
		text = oldText = t;
		priority = 0;
		graphic = new Image(w, h);
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
		
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(GUIManager.guiFont);
		canvas.setColor(new Color(0, 0, 0));
		Iterator<String> itr = list.iterator();
		for (int i = 0; itr.hasNext(); i++)
			canvas.drawString(itr.next(), 0, i * 24); //Config.font.getHeight(list.get(i)));// h / 2 - (Config.font.getHeight(t) / 2));
		if (GUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, width - 1, height - 1);
		}
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
	
	public void onTextChange(GUITextFunction function)
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
			int size = GUIManager.guiFont.getWidth(s);
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