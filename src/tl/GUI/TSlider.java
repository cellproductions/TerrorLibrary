package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.Util.TCursor;

public class TSlider extends TGUIComponent
{
	public static final ComponentType type = ComponentType.slider;
	private long max;
	private long min;
	private long value;
	private Image box;
	private Image slide;
	private boolean pressing; // whether or not the user is holding down the mouse button while moving the slider
	
	private TGUIValueEvent valueChange;
	private TGUIValueEvent valueFinal;
	
	public TSlider()
	{
		super();
	}

	public TSlider(TGUIComponent parent)
	{
		super(parent);
	}

	public TSlider(TGUIComponent parent, float x, float y, int w, int h, int ma, int mi, int v) throws SlickException
	{
		super(parent, x, y, w, h);
		max = ma;
		min = mi;
		value = v;
		changed = true;
	}

	public boolean mouseIsOver()
	{
		return TCursor.getX() >= gx + 4 && TCursor.getX() <= gx + width && TCursor.getY() >= gy && TCursor.getY() <= gy + height;
	}
	
	private float valuex;

	private void updateSlider() throws SlickException
	{
		if (graphic == null)
		{
			int xOffSet = 2;
			int w = width;
			int h = height;
			box = new Image(w, h);
			canvas = box.getGraphics();
			canvas.setColor(TGUIManager.BLACK);
			canvas.fillRect(0, 0, w - 1, h);
			canvas.setColor(TGUIManager.BUTTON_BORDER);
			canvas.drawLine(0, 0, w - 1, 0);
			canvas.drawLine(0, 0, 0, h - 1);
			canvas.setColor(TGUIManager.WHITE);
			canvas.drawLine(w - 1, 1, w - 1, h - 1);
			canvas.drawLine(1, h - 1, w - 1, h - 1);
			canvas.flush();
			
			w = 9;
			h += 12;
			slide = new Image(w, h);
			canvas = slide.getGraphics();
			canvas.setColor(TGUIManager.BUTTON_MAIN);
			canvas.fillRect(0, 0, w - 1, h - 1);
			canvas.setColor(TGUIManager.WHITE);
			canvas.drawLine(0, 0, w - 1, 0);
			canvas.drawLine(0, 0, 0, h - 1);
			canvas.setColor(TGUIManager.BUTTON_BORDER);
			canvas.drawLine(w - 2, 1, w - 2, h - 1);
			canvas.drawLine(1, h - 2, w - 1, h - 2);
			canvas.flush();
			
			float fWidth = width - xOffSet * 2, fMax = max, fValue = value, fMin = min;
			graphic = new Image(width + xOffSet * 2, h);
			canvas = graphic.getGraphics();
			canvas.drawImage(box, xOffSet, h / 2 - (box.getHeight() / 2));
			valuex = (fWidth * (fValue - fMin)) / (fMax - fMin); // thanks wolframalpha
			canvas.drawImage(slide, valuex, 0); 
			canvas.flush();
		}
		
		float pos = valuex - slide.getWidth() / 2;
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.drawImage(box, 2, slide.getHeight() / 2 - (box.getHeight() / 2));
		if (pos < 2)
			pos = 2;
		else if (pos >= box.getWidth() + 2) // these checks keep the slider within the bounds of its box
			pos = box.getWidth() + 2;
		canvas.drawImage(slide, pos, 0);
		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, width - 1, height - 1);
		}
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
		
		if (pressing)
		{
			valuex = TCursor.getX() - gx;
			int offset = (valuex <= width / 2 ? slide.getWidth() / 2 : 0);
			valuex -= offset;
			long check = Math.round((valuex * (max - min)) / width) + min;
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
					valuex = TCursor.getX() - gx;
					int offset = (valuex <= width / 2 ? slide.getWidth() / 2 : 0);
					valuex -= offset;
					long check = Math.round((valuex * (max - min)) / width) + min;
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
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
	}
	
	public void onValueChange(TGUIValueEvent function)
	{
		valueChange = function;
	}
	
	public void onMouseReleased(TGUIValueEvent function)
	{
		valueFinal = function;
	}
	
	public void setValue(int value) throws TGUIException
	{
		if (value < min || value > max)
			throw new TGUIException("value " + value + " out of bounds! [" + min + "-" + max + "]");
		
		this.value = value;
		changed = true;
		if (valueChange != null)
			valueChange.execute(this.value, this);
	}
	
	public void setRange(long max, long min)
	{
		this.max = max;
		if (value > max)
			value = max;
		this.min = min;
		if (value < min)
			value = min;
		changed = true;
	}
	
	public long getValue()
	{
		return value;
	}
}