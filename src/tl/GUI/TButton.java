package tl.GUI;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.GUI.TGUIManager.GUIColor;

public class TButton extends TGUIComponent
{
	public static final ComponentType type = ComponentType.button;
	private Image normal;
	private Image pushed;
	private String text;
	
	public TButton()
	{
		super();
	}
	
	public TButton(TGUIComponent parent)
	{
		super(parent);
		text = "";
	}
	
	public TButton(TGUIComponent parent, float x, float y, int w, int h)
	{
		super(parent, x, y, w, h);
		text = "";
		changed = true;
	}

	public TButton(TGUIComponent parent, float x, float y, int w, int h, String t) throws SlickException
	{
		super(parent, x, y, w, h);
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
	@SuppressWarnings("deprecation")
	private void updateB() throws SlickException
	{
		if (normal == null)
		{
			normal = new Image(width, height);
			graphic = normal;
		}
		canvas = normal.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(GUIColor.BUTTON_MAIN.get());
		canvas.fillRect(0, 0, width, height);
		canvas.setColor(GUIColor.WHITE.get());
		canvas.drawLine(0, 0, width, 0);
		canvas.drawLine(0, 0, 0, height);
		canvas.setColor(GUIColor.BUTTON_BORDER.get());
		canvas.drawLine(width - 1, 1, width - 1, height);
		canvas.drawLine(1, height - 1, width, height - 1);
		canvas.setColor(GUIColor.BLACK.get());
		canvas.drawString(text, width / 2 - (TGUIManager.guiFont.getWidth(text) / 2), height / 2 - (TGUIManager.guiFont.getHeight(text) / 2));
		canvas.flush();

		if (pushed == null)
			pushed = new Image(width, height);
		canvas = pushed.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(GUIColor.BUTTON_MAIN.get());
		canvas.fillRect(0, 0, width, height);
		canvas.setColor(GUIColor.BUTTON_BORDER.get());
		canvas.drawLine(0, 0, width, 0);
		canvas.drawLine(0, 0, 0, height);
		canvas.setColor(GUIColor.WHITE.get());
		canvas.drawLine(width - 1, 1, width - 1, height);
		canvas.drawLine(1, height - 1, width, height - 1);
		canvas.setColor(GUIColor.BLACK.get());
		canvas.drawString(text, width / 2 - (TGUIManager.guiFont.getWidth(text) / 2) - 1, height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 1);
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
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
	}
	
	public void setText(String text)
	{
		this.text = text;
		changed = true;
	}
	
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		normal = null;
		pushed = null;
	}
}