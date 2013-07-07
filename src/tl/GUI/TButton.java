package tl.GUI;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.Util.TSize;

public class TButton extends TGUIComponent
{
	private Image normal;
	private Image pushed;
	private String text;
	
	public TButton()
	{
		super();
		type = ComponentType.button;
		normal = pushed = TGUIManager.emptyImage;
	}
	
	public TButton(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.button;
		normal = pushed = TGUIManager.emptyImage;
		text = "";
	}
	
	public TButton(TGUIComponent parent, float x, float y, int width, int height)
	{
		super(parent, x, y, width, height);
		type = ComponentType.button;
		normal = pushed = TGUIManager.emptyImage;
		text = "";
		changed = true;
	}

	public TButton(TGUIComponent parent, float x, float y, int width, int height, String t) throws SlickException
	{
		super(parent, x, y, width, height);
		type = ComponentType.button;
		normal = pushed = TGUIManager.emptyImage;
		text = t;
		changed = true;
	}
/*	NOT YET SUPPORTED
	public Button(int x, int y, int w, int h, String t, String n) throws SlickException
	{
		super(x, y, w, h);
		type = GUIComponent.ControlType.button;
		text = t;
		graphic = normal.copy();
		super.width = graphic.getWidth();
		super.height = graphic.getHeight();
		changed = true;
	}

	public Button(int x, int y, int w, int h, String t, String n, String p)
	{
		super(x, y, w, h);
		type = GUIComponent.ControlType.button;
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
*/
	public ComponentType getType()
	{
		return type;
	}
	
	@SuppressWarnings("deprecation")
	private void updateB() throws SlickException
	{
		if (normal == TGUIManager.emptyImage)
		{
			normal = new Image(size.width, size.height);
			graphic = normal;
		}
		canvas = normal.getGraphics();
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

		if (pushed == TGUIManager.emptyImage)
			pushed = new Image(size.width, size.height);
		canvas = pushed.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(TGUIManager.BUTTON_MAIN);
		canvas.fillRect(0, 0, size.width, size.height);
		canvas.setColor(TGUIManager.BUTTON_BORDER);
		canvas.drawLine(0, 0, size.width, 0);
		canvas.drawLine(0, 0, 0, size.height);
		canvas.setColor(TGUIManager.WHITE);
		canvas.drawLine(size.width - 1, 1, size.width - 1, size.height);
		canvas.drawLine(1, size.height - 1, size.width, size.height - 1);
		canvas.setColor(TGUIManager.BLACK);
		canvas.drawString(text, size.width / 2 - (TGUIManager.guiFont.getWidth(text) / 2) - 1, size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 1);
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
		
		if (visible && alpha > 0.00f)
			g.drawImage(graphic, screenPos.x, screenPos.y);
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
			
			if (mouseIsOver())
			{
				if (mousePress != null)
				{
					mousePress.execute(button, x, y, this);
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
				graphic = normal;
			
			if (mouseIsOver())
			{
				if (mouseRelease != null)
				{
					mouseRelease.execute(button, x, y, this);
					changed = true;
				}
			}
		}
	}
	
	public void setText(String text)
	{
		this.text = text;
		changed = true;
	}
	
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		normal = TGUIManager.emptyImage;
		pushed = TGUIManager.emptyImage;
	}
	
	public void setSize(TSize size)
	{
		super.setSize(size);
		normal = TGUIManager.emptyImage;
		pushed = TGUIManager.emptyImage;
	}
}