package tl.GUIutil;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Button extends GUIControl
{
	private Image normal;
	private Image pushed;
	private String text;
	
	public Button()
	{
		super();
		type = GUIControl.ControlType.button;
		normal = null;
		pushed = null;
		text = null;
		priority = 0;
	}
	
	public Button(int x, int y, int w, int h)
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.button;
		text = null;
		normal = null;
		pushed = null;
		graphic = null;
		priority = 0;
	}

	public Button(int x, int y, int w, int h, String t) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.button;
		text = t;
		normal = new Image(w, h);
		pushed = new Image(w, h);
		priority = 0;
		graphic = normal;
		changed = true;
	}

	public Button(int x, int y, int w, int h, String t, String n) throws SlickException
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.button;
		text = t;
		pushed = new Image(n);
		normal = new Image(n);
		graphic = normal.copy();
		super.width = graphic.getWidth();
		super.height = graphic.getHeight();
		changed = true;
	}

	public Button(int x, int y, int w, int h, String t, String n, String p)
	{
		super(x, y, w, h);
		type = GUIControl.ControlType.button;
		text = t;
		try
		{
			normal = new Image(n);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		try
		{
			pushed = new Image(p);
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		graphic = normal.copy();
		super.width = graphic.getWidth();
		super.height = graphic.getHeight();
		changed = true;
	}

	@SuppressWarnings("deprecation")
	private void updateB() throws SlickException
	{
		if (normal != null)
		{
			canvas = normal.getGraphics();
			canvas.clear();
			canvas.setFont(GUIManager.guiFont);
			canvas.setColor(new Color(160, 160, 160));
			canvas.fillRect(0, 0, width, height);
			canvas.setColor(new Color(255, 255, 255));
			canvas.drawLine(0, 0, width, 0);
			canvas.drawLine(0, 0, 0, height);
			canvas.setColor(new Color(70, 70, 70));
			canvas.drawLine(width - 1, 1, width - 1, height);
			canvas.drawLine(1, height - 1, width, height - 1);
			canvas.setColor(new Color(0, 0, 0));
			canvas.drawString(text, width / 2 - (GUIManager.guiFont.getWidth(text) / 2), height / 2 - (GUIManager.guiFont.getHeight(text) / 2));
			canvas.flush();
		}

		if (pushed != null)
		{
			canvas = pushed.getGraphics();
			canvas.clear();
			canvas.setFont(GUIManager.guiFont);
			canvas.setColor(new Color(160, 160, 160));
			canvas.fillRect(0, 0, width, height);
			canvas.setColor(new Color(70, 70, 70));
			canvas.drawLine(0, 0, width, 0);
			canvas.drawLine(0, 0, 0, height);
			canvas.setColor(new Color(255, 255, 255));
			canvas.drawLine(width - 1, 1, width - 1, height);
			canvas.drawLine(1, height - 1, width, height - 1);
			canvas.setColor(new Color(0, 0, 0));
			canvas.drawString(text, width / 2 - (GUIManager.guiFont.getWidth(text) / 2) - 1, height / 2 - (GUIManager.guiFont.getHeight(text) / 2) - 1);
			canvas.flush();
		}
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
			if (visible && graphic.getAlpha() > 0.00f)
				g.drawImage(graphic, gx, gy);
	}
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (button == 0)
			{
				if (mouseIsOver())
					graphic = pushed;
			}
		}
	}
	
	public void mouseReleased(int button, int x, int y)
	{
		if (enabled)
		{
			if (button == 0)
				graphic = normal;
			
			if (mouseIsOver())
			{
				if (mouseClick != null)
				{
					mouseClick.execute(button, x, y, this);
					changed = true;
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
	
	public void setText(String text)
	{
		this.text = text;
		changed = true;
	}
}