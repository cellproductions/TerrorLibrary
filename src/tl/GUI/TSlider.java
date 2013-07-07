package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.Util.TCursor;

public class TSlider extends TGUIComponent
{
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
		type = ComponentType.slider;
	}

	public TSlider(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.slider;
	}

	public TSlider(TGUIComponent parent, float x, float y, int width, int height, int max, int min, int v) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.slider;
		this.max = max;
		this.min = min;
		value = v;
		changed = true;
	}

	public boolean mouseIsOver()
	{
		return TCursor.getX() >= screenPos.x + 4 && TCursor.getX() <= screenPos.x + size.width && TCursor.getY() >= screenPos.y && TCursor.getY() <= screenPos.y + size.height;
	}
	
	private float valuex;
	private final int xOffSet = 2;

	private void updateSlider() throws SlickException
	{
		if (graphic == TGUIManager.emptyImage)
		{
			int w = size.width - xOffSet;
			int h = size.height;
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
			
			float fWidth = size.width - xOffSet * 2, fMax = max, fValue = value, fMin = min;
			graphic = new Image(size.width + xOffSet * 2, h);
			canvas = graphic.getGraphics();
			canvas.drawImage(box, xOffSet, h / 2 - (box.getHeight() / 2));
			valuex = (fWidth * (fValue - fMin)) / (fMax - fMin); // thanks wolframalpha
			canvas.drawImage(slide, valuex, 0); 
			canvas.flush();
		}
		
		float pos = valuex - slide.getWidth() / xOffSet;
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.drawImage(box, xOffSet, slide.getHeight() / xOffSet - (box.getHeight() / xOffSet));
		if (pos < xOffSet)
			pos = xOffSet;
		else if (pos >= box.getWidth() + xOffSet) // these checks keep the slider within the bounds of its box
			pos = box.getWidth() + xOffSet;
		canvas.drawImage(slide, pos, 0);
		if (TGUIManager.debug)
		{
			canvas.setColor(Color.yellow);
			canvas.drawRect(0, 0, size.width - 1, size.height - 1);
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
			valuex = TCursor.getX() - screenPos.x;
			int offset = (valuex <= size.width / 2 ? slide.getWidth() / 2 : 0);
			valuex -= offset;
			long check = Math.round((valuex * (max - min)) / size.width) + min;
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
		if (visible && alpha > 0.00F)
			g.drawImage(graphic, screenPos.x, screenPos.y);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			
			if (button == 0)
				pressing = true;
			if (mousePress != null)
			{
				mousePress.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			if (button == 0)
			{
				if (pressing)
				{
					pressing = false;
					valuex = TCursor.getX() - screenPos.x;
					int offset = (valuex <= size.width / 2 ? slide.getWidth() / 2 : 0);
					valuex -= offset;
					long check = Math.round((valuex * (max - min)) / size.width) + min;
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
			if (mouseRelease != null)
			{
				mouseRelease.execute(button, x, y, this);
				changed = true;
			}
		}
	}
	
	public void onValueChange(TGUIValueEvent function)
	{
		valueChange = function;
	}
	
	public void onFinalValueChange(TGUIValueEvent function) // when the user releases button 0
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