/*
 * NOTE:
 * isnt finished
 * needs the check variable to check if the extended label needs updating
 */

package tl.GUI;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TLabelExtended extends TLabel
{
	public static final ComponentType type = ComponentType.labelExtended;
	private int size;
	
	public TLabelExtended(TGUIComponent parent, float x, float y, int w, String t) throws SlickException
	{
		super(parent, x, y, w, 0, t);
		
		toDraw = text;
		int h = 1;
		if (toDraw.contains("[["))
		{
			while (toDraw.contains("[["))
			{
				int i = toDraw.indexOf("[[");
				toDraw = toDraw.substring(i + 2);
				h++;
			}
			h++;
		}
		
		size = h;
		
		graphic = new Image(w, h * 24);
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
		
		if (size != list.size())
			graphic = new Image(width, list.size() * 24);
		
		size = list.size();
		
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(new Color(0, 0, 0));
		int size = list.size();
		for (int i = 0; i < size; i++)
			canvas.drawString(list.get(i), 0, i * 24);
		canvas.flush();
	}
	
	public void update(Graphics g)
	{
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
}