package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import tl.Util.TCursor;
import tl.Util.TPoint;
import tl.Util.TSize;

public class TSlider extends TGUIComponent
{
	private long max;
	private long min;
	private long value;
	//private Image box;
	//private Image slide;
	private boolean pressing; // whether or not the user is holding down the mouse button while moving the slider
	private int xOffset;
	private tl.Util.TPoint boxPos;
	private tl.Util.TSize boxSize;
	private tl.Util.TPoint slidePos;
	private tl.Util.TSize slideSize;
	public final Color background = new Color(TGUIManager.BLACK);
	public final Color border_grey = new Color(TGUIManager.BUTTON_BORDER);
	public final Color border_white = new Color(TGUIManager.WHITE);
	public final Color slider_background = new Color(TGUIManager.BUTTON_MAIN);
	
	private TGUIValueEvent valueChange;
	private TGUIValueEvent valueFinal;
	
	public TSlider()
	{
		super();
		type = ComponentType.slider;
		slideSize = new TSize();
		slidePos = new TPoint();
		boxSize = new TSize();
		boxPos = new TPoint();
	}

	public TSlider(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.slider;
		slideSize = new TSize();
		slidePos = new TPoint();
		boxSize = new TSize();
		boxPos = new TPoint();
	}

	public TSlider(TGUIComponent parent, float x, float y, int width, int height, int max, int min, int v) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.slider;
		this.max = max;
		this.min = min;
		value = v;
		slideSize = new tl.Util.TSize(9, height);
		xOffset = slideSize.width / 2;
		slidePos = new TPoint(getSlideXFromValue(), 0);
		boxSize = new TSize(width - xOffset * 2, (int)(height * .67f));
		boxPos = new TPoint(xOffset, (int)((height / 2) - (boxSize.height / 2) + 1));
		changed = true;
	}

	public boolean mouseIsOver()
	{
		return TCursor.getX() >= screenPos.x + 4 && TCursor.getX() <= screenPos.x + size.width && TCursor.getY() >= screenPos.y && TCursor.getY() <= screenPos.y + size.height && isVisible();
	}
	
	protected void change()
	{
		background.a = alpha;
		border_grey.a = alpha;
		border_white.a = alpha;
		slider_background.a = alpha;
		changed = false;
	}

	protected void draw(Graphics g) throws SlickException
	{/*
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
		canvas.flush();*/
		
		/*
		 * Draws the box part
		 */
		tl.Util.TPoint bpoint = screenPos.add(boxPos);
		g.setColor(background);
		g.fillRect(bpoint.x, bpoint.y, boxSize.width, boxSize.height);
		g.setColor(border_grey);
		g.drawLine(bpoint.x, bpoint.y, bpoint.x + boxSize.width, bpoint.y);
		g.drawLine(bpoint.x, bpoint.y, bpoint.x, bpoint.y + boxSize.height);
		g.setColor(border_white);
		g.drawLine(bpoint.x + boxSize.width, bpoint.y + 1, bpoint.x + boxSize.width, bpoint.y + boxSize.height);
		g.drawLine(bpoint.x + 1, bpoint.y + boxSize.height, bpoint.x + boxSize.width, bpoint.y + boxSize.height);
		
		/*
		 * Draws the slider part 
		 */
		tl.Util.TPoint spoint = screenPos.add(slidePos.x - 4, slidePos.y);
		g.setColor(slider_background);
		g.fillRect(spoint.x, spoint.y, slideSize.width, slideSize.height);
		g.setColor(border_white);
		g.drawLine(spoint.x, spoint.y, spoint.x + slideSize.width, spoint.y);
		g.drawLine(spoint.x, spoint.y, spoint.x, spoint.y + slideSize.height);
		g.setColor(border_grey);
		g.drawLine(spoint.x + slideSize.width, spoint.y + 1, spoint.x + slideSize.width, spoint.y + slideSize.height);
		g.drawLine(spoint.x + 1, spoint.y + slideSize.height, spoint.x + slideSize.width, spoint.y + slideSize.height);
		
		if (TGUIManager.debug)
		{
			g.setColor(TGUIManager.YELLOW);
			g.drawRect(screenPos.x, screenPos.y, size.width, size.height);
		}
		g.setColor(TGUIManager.BLACK);
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
			setSlideXFromCursor();
			long old = value;
			setValueFromX();
			
			if (old != value)
				if (valueChange != null)
					valueChange.execute(value, this);
		}
		
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
	
	private float getSlideXFromValue()
	{
		float v = ((size.width - xOffset - 1) * (value - min)) / (max - min);
		if (v <= xOffset)
			v = xOffset;
		return v;
	}
	
	private void setSlideXFromCursor()
	{
		slidePos.x = TCursor.getX() - screenPos.x;
		if (slidePos.x < xOffset)
			slidePos.x = xOffset;
		else if (slidePos.x >= size.width - xOffset)
			slidePos.x = size.width - xOffset - 1;
	}
	
	private void setValueFromX()
	{
		value = (long)((((slidePos.x - xOffset - 1) * (max - min)) / (boxSize.width - xOffset - 1)) + min);
		if (value < min)
			value = min;
		else if (value > max)
			value = max;
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
			if (button == 0)
			{
				if (pressing)
				{
					pressing = false;
					setSlideXFromCursor();
					setValueFromX();
					
					if (valueFinal != null)
					{
						valueFinal.execute(value, this);
						changed = true;
					}
				}
			}
			if (!mouseIsOver())
				return;
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
	
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		slideSize = new tl.Util.TSize(9, height);
		xOffset = slideSize.width / 2;
		slidePos = new TPoint(getSlideXFromValue(), 0);
		boxSize = new TSize(width - xOffset * 2, (int)(height * .67f));
		boxPos = new TPoint(xOffset, (int)((height / 2) - (boxSize.height / 2) + 1));
	}
	
	public void setSize(TSize size)
	{
		super.setSize(size);
		slideSize = new tl.Util.TSize(9, size.height);
		xOffset = slideSize.width / 2;
		slidePos = new TPoint(getSlideXFromValue(), 0);
		boxSize = new TSize(size.width - xOffset * 2, (int)(size.height * .67f));
		boxPos = new TPoint(xOffset, (int)((size.height / 2) - (boxSize.height / 2) + 1));
	}
	
	public void setValue(long value) throws TGUIException
	{
		if (value < min || value > max)
			throw new TGUIException("value " + value + " out of bounds! [" + min + "-" + max + "]");
		
		this.value = value;
		slidePos.x = getSlideXFromValue();
		
		changed = true;
		if (valueChange != null)
			valueChange.execute(this.value, this);
	}
	
	public void setRange(long max, long min)
	{
		this.max = max;
		this.min = min;
		if (min >= max)
			max = min + 1;
		if (value > max)
			setValue(max);
		if (value < min)
			setValue(min);
		changed = true;
	}
	
	public long getValue()
	{
		return value;
	}
}