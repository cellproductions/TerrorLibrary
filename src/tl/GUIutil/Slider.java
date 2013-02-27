package tl.GUIutil;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import tl.Util.Cursor;

public class Slider extends GUIControl
{
	private int max;
	private int min;
	private int value;
	private Image box;
	private Image slide;
	private boolean pressing; // whether or not the user is holding down the mouse button while moving the slider
	
	private GUIValueFunction valueChange;
	private GUIValueFunction valueFinal;

	public Slider()
	{
		super();
		type = GUIControl.ControlType.slider;
		max = 0;
		min = 0;
		value = 0;
		box = null;
		slide = null;
		pressing = false;
	}

	public Slider(int x, int y, int w, int h, int ma, int mi, int v) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.slider;
		max = ma;
		min = mi;
		value = v;
		pressing = false;
		
		int xOffSet = 2;

		box = new Image(w, h); // this goes here, only ever need to redraw the slide part
		Graphics s = box.getGraphics();
		s.setColor(new Color(0, 0, 0));
		s.fill(new Rectangle(0, 0, w - 1, h));
		s.setColor(new Color(70, 70, 70));
		s.drawLine(0, 0, w - 1, 0);
		s.drawLine(0, 0, 0, h - 1);
		s.setColor(new Color(255, 255, 255));
		s.drawLine(w - 1, 1, w - 1, h - 1);
		s.drawLine(1, h - 1, w - 1, h - 1);
		
		w = 9;
		h += 12;
		slide = new Image(w, h);
		s = slide.getGraphics();
		s.setColor(new Color(160, 160, 160));
		s.fill(new Rectangle(0, 0, w - 1, h - 1));
		s.setColor(new Color(255, 255, 255));
		s.drawLine(0, 0, w - 1, 0);
		s.drawLine(0, 0, 0, h - 1);
		s.setColor(new Color(70, 70, 70));
		s.drawLine(w - 2, 1, w - 2, h - 1);
		s.drawLine(1, h - 2, w - 1, h - 2);
		
		float fWidth = width - xOffSet * 2, fMax = max, fValue = value, fMin = min;
		graphic = new Image(width + xOffSet * 2, h);
		s = graphic.getGraphics();
		s.drawImage(box, xOffSet, h / 2 - (box.getHeight() / 2));
		valuex = (fWidth * (fValue - fMin)) / (fMax - fMin); // thanks wolframalpha
		s.drawImage(slide, valuex, 0); 
		
		changed = true;
	}

	public boolean mouseIsOver()
	{
		return Cursor.getX() >= gx + 4 && Cursor.getX() <= gx + width && Cursor.getY() >= gy && Cursor.getY() <= gy + height;
	}
	
	private float valuex;

	private void updateSlider() throws SlickException
	{
		float pos = valuex - slide.getWidth() / 2;
		Graphics s = graphic.getGraphics();
		s.clear();
		s.drawImage(box, 2, slide.getHeight() / 2 - (box.getHeight() / 2));
		if (pos < 2)
			pos = 2;
		else if (pos >= box.getWidth() + 2) // these checks keep the slider within the bounds of its box
			pos = box.getWidth() + 2;
		s.drawImage(slide, pos, 0);
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
		
		if (pressing)
		{
			valuex = Cursor.getX() - gx;
			int offset = (valuex <= width / 2 ? slide.getWidth() / 2 : 0);
			valuex -= offset;
			int check = Math.round((valuex * (max - min)) / width) + min;
			if (check >= min && check <= max)
			{
				value = check;
				changed = true;
				if (valueChange != null)
					valueChange.execute(value, this);
			}
		}
		
		try
		{
			if (changed)
			{
				updateSlider();
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
			boolean mover = mouseIsOver();
			if (button == 0)
				if (mover)
					pressing = true;
			if (mover)
			{
				if (mouseClick != null)
				{
					mouseClick.execute(button, x, y, this);
					changed = true;
				}
			}
		}
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		if (enabled)
		{
			if (button == 0)
			{
				if (pressing)
				{
					pressing = false;
					valuex = Cursor.getX() - gx;
					int offset = (valuex <= width / 2 ? slide.getWidth() / 2 : 0);
					valuex -= offset;
					int check = Math.round((valuex * (max - min)) / width) + min;
					if (check >= min && check <= max)
					{
						value = check;
						changed = true;
						if (valueChange != null)
							valueChange.execute(value, this);
					} // the last update of the value
					if (valueFinal != null)
					{
						valueFinal.execute(value, this);
						changed = true;
					}
				}
			}
		}
	}
	
	public void onMouseClick(GUIClickedFunction function)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(GUIMouseOverFunction function)
	{
		mouseOver = function;
	}
	
	public void onValueChange(GUIValueFunction function)
	{
		valueChange = function;
	}
	
	public void onMouseReleased(GUIValueFunction function)
	{
		valueFinal = function;
	}
	
	public void setValue(int value)
	{
		if (value >= min && value <= max)
		{
			this.value = value;
			changed = true;
			if (valueChange != null)
				valueChange.execute(this.value, this);
		}
	}
	
	public int getValue()
	{
		return value;
	}
}