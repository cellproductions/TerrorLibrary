package tl.GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tl.Util.TSize;

public class TButton extends TGUIComponent
{
	private Image normal;
	private Image pushed;
	private String text;
	private boolean isPushed;
	public final Color background = new Color(TGUIManager.BUTTON_MAIN);
	public final Color border_white = new Color(TGUIManager.WHITE);
	public final Color border_grey = new Color(TGUIManager.BUTTON_BORDER);
	public final Color font_colour = new Color(TGUIManager.BLACK);
	
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
	
	protected void change()
	{
		background.a = alpha;
		border_white.a = alpha;
		border_grey.a = alpha;
		font_colour.a = alpha;
		changed = false;
	}
	
	@SuppressWarnings("deprecation")
	protected void draw(Graphics g) throws SlickException
	{/*
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
		canvas.flush();*/
		if (!isPushed)
		{
			int widthpos = (int)(screenPos.x + size.width);
			int heightpos = (int)(screenPos.y + size.height);
			g.setFont(TGUIManager.guiFont);
			g.setColor(background);
			g.fillRect(screenPos.x, screenPos.y, size.width, size.height);
			g.setColor(border_white);
			g.drawLine(screenPos.x, screenPos.y, widthpos, screenPos.y);
			g.drawLine(screenPos.x, screenPos.y, screenPos.x, heightpos);
			g.setColor(border_grey);
			g.drawLine(widthpos, screenPos.y + 1, widthpos, heightpos);
			g.drawLine(screenPos.x + 1, heightpos, widthpos, heightpos);
			g.setColor(font_colour);
			g.drawString(text, screenPos.x + (size.width / 2 - (TGUIManager.guiFont.getWidth(text) / 2)), screenPos.y + (size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2)));
			g.setColor(TGUIManager.BLACK);
		}
		else if (isPushed)
		{
			int widthpos = (int)(screenPos.x + size.width);
			int heightpos = (int)(screenPos.y + size.height);
			g.setFont(TGUIManager.guiFont);
			g.setColor(background);
			g.fillRect(screenPos.x, screenPos.y, size.width, size.height);
			g.setColor(border_grey);
			g.drawLine(screenPos.x, screenPos.y, widthpos, screenPos.y);
			g.drawLine(screenPos.x, screenPos.y, screenPos.x, heightpos);
			g.setColor(border_white);
			g.drawLine(widthpos, screenPos.y + 1, widthpos, heightpos);
			g.drawLine(screenPos.x + 1, heightpos, widthpos, heightpos);
			g.setColor(font_colour);
			g.drawString(text, screenPos.x + (size.width / 2 - (TGUIManager.guiFont.getWidth(text) / 2) - 1), screenPos.y + (size.height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 1));
			g.setColor(TGUIManager.BLACK);
		}
	}
	
	protected boolean isOver = false; // JUST FOR 7dRTS

	public void update(Graphics g)
	{
		if (mouseIsOver())
		{
			if (mouseOver != null && !isOver)
			{
				mouseOver.execute(this);
				changed = true;
				isOver = true;
			}
		}
		else
			isOver = false;
		
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
	
	public void mousePressed(int button, int x, int y)
	{
		if (enabled)
		{
			if (!mouseIsOver())
				return;
			
			if (button == 0)
			{
				graphic = pushed;
				isPushed = true;
			}
			
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
				graphic = normal;
			
			if (mouseIsOver())
			{
				if (mouseRelease != null && isPushed)
				{
					mouseRelease.execute(button, x, y, this);
					changed = true;
				}
			}
			
			if (button == 0)
				isPushed = false;
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