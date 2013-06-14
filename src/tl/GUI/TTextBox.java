package tl.GUI;

import java.lang.StringBuffer;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class TTextBox extends TGUIComponent
{
	public static TTextBox activeTB;
	
	private String oldText;
	private String text;
	private Position position;
	private float typos; // text y position
	
	private TGUITextEvent textChange;
	private TGUITextEvent enterKey;
	
	public TTextBox()
	{
		super();
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
	}

	public TTextBox(TGUIComponent parent)
	{
		super(parent);
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = "";
		position = new Position();
	}
	
	public TTextBox(TGUIComponent parent, float x, float y, int w, int h) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = "";
		position = new Position();
		changed = true;
	}

	@SuppressWarnings("deprecation")
	public TTextBox(TGUIComponent parent, float x, float y, int w, int h, String def) throws SlickException
	{
		super(parent, x, y, w, h);
		type = ComponentType.textBox;
		if (TTextBox.activeTB == null)
			TTextBox.activeTB = this;
		text = oldText = def;
		position = new Position();
		typos = height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 2;
		changed = true;
	}

	public boolean isActive()
	{
		return enabled && TTextBox.activeTB == this;
	}
	
	@SuppressWarnings("deprecation")
	private void updateText() throws SlickException
	{
		if (graphic == null)
			graphic = new Image(width, height);
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setFont(TGUIManager.guiFont);
		canvas.setColor(new Color(0, 0, 0));
		canvas.draw(new Rectangle(1, 1, width - 1, height - 1));
		typos = height / 2 - (TGUIManager.guiFont.getHeight(text) / 2) - 2;
		if (isActive())
			canvas.drawString("_", TGUIManager.guiFont.getWidth(position.get() < text.length() ? text.substring(0, position.get()) : text), typos + 2);
		canvas.drawString(text, 3, typos);
		canvas.flush();
	}
	
	private boolean arrowdown;
	private boolean backspacedown = false;
	private boolean ctrlDown = false;
	private Time bstime = new Time(15f, 3f);
	private Time arrowtime = new Time(15f, 3f);

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
		
		if (isActive() && !text.isEmpty())
		{
			ctrlDown = TGUIManager.guiInput.isKeyDown(Input.KEY_LCONTROL) || TGUIManager.guiInput.isKeyDown(Input.KEY_RCONTROL);
			
			if (backspacedown) // if the backspace key is being held down
			{
				if (bstime.hasFinished())
				{
					if (TGUIManager.guiInput.isKeyDown(Input.KEY_BACK))
					{
						if (position.get() - 1 > -1)
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(position.get() - 1).toString();
							position.left();
							changed = true;
						}
					}
					else if (TGUIManager.guiInput.isKeyDown(Input.KEY_DELETE))
					{
						if (position.get() < text.length())
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(position.get()).toString();
							changed = true;
						}
					}
				}
			}
			else if (arrowdown)
			{
				if (arrowtime.hasFinished())
				{
					if (TGUIManager.guiInput.isKeyDown(Input.KEY_LEFT))
					{
						position.left();
						changed = true;
					}
					else if (TGUIManager.guiInput.isKeyDown(Input.KEY_RIGHT))
					{
						position.right();
						changed = true;
					}
				}
			}
		}
		
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
				updateText();
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
			if (mouseIsOver())
			{
				if (button == 0)
				{
					if (TTextBox.activeTB != null)
						TTextBox.activeTB.changed = true;
					TTextBox.activeTB = this;
					changed = true;
				}
			}
			
			if (mouseClick != null)
			{
				mouseClick.execute(button, x, y, this);
				changed = true;
			}
		}
		
		if (button == 1)
		{
			if (TTextBox.activeTB != null)
				TTextBox.activeTB.changed = true;
			TTextBox.activeTB = null;
			changed = true;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void keyPressed(int key, char c)
	{
		if (enabled)
		{
			if (isActive())
			{
				if (c >= 32 && c <= 126)
				{
					int w = TGUIManager.guiFont.getWidth(text + Character.toString(c));
					if (w < width - 4)
					{
						oldText = text;
						text = new StringBuffer(text).insert(position.get(), c).toString();
						position.right();
						changed = true;
					}
				}
				
				if (key == Input.KEY_BACK)
				{
					backspacedown = true;
					bstime.start();
					
					if (!text.isEmpty())
					{
						if (position.get() - 1 > -1)
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(position.get() - 1).toString();
							position.left();
							changed = true;
						}
					}
				}
				else if (key == Input.KEY_DELETE)
				{
					backspacedown = true;
					bstime.start();
					
					if (!text.isEmpty())
					{
						if (position.get() < text.length())
						{
							oldText = text;
							text = new StringBuffer(text).deleteCharAt(position.get()).toString();
							changed = true;
						}
					}
				}
				else if (key == Input.KEY_V)
				{
					if (ctrlDown)
					{
						try
						{
							oldText = text;
							text += (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); // paste data from clipboard into this text box
							changed = true;
						}
						catch (HeadlessException e)
						{
							e.printStackTrace();
						}
						catch (UnsupportedFlavorException e)
						{
							e.printStackTrace();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						ctrlDown = false;
					}
				}
				else if (key == Input.KEY_ENTER)
				{
					// oldText = text; 			perhaps?
					if (enterKey != null)
					{
						enterKey.execute(text, this);
						changed = true;
					}
				}
				else if (key == Input.KEY_LEFT)
				{
					arrowtime.start();
					position.left();
					arrowdown = true;
					changed = true;
				}
				else if (key == Input.KEY_RIGHT)
				{
					arrowtime.start();
					position.right();
					arrowdown = true;
					changed = true;
				}
			}
		}
	}
	
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_BACK)
			backspacedown = false;
		else if (key == Input.KEY_DELETE)
			backspacedown = false;
		else if (key == Input.KEY_LEFT)
			arrowdown = false;
		else if (key == Input.KEY_RIGHT)
			arrowdown = false;
	}
	
	public void onMouseClick(TGUIClickedEvent function)
	{
		mouseClick = function;
	}
	
	public void onMouseOver(TGUIMouseOverEvent function)
	{
		mouseOver = function;
	}
	
	public void onTextChange(TGUITextEvent function)
	{
		textChange = function;
	}
	
	public void onEnterPressed(TGUITextEvent function)
	{
		enterKey = function;
	}
	
	public void setText(String text)
	{
		oldText = text;
		this.text = text;
		if (position.get() > text.length())
			position.set(text.length());
		changed = true;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void clear()
	{
		text = "";
		position.set(0);
	}
	
	private class Position
	{
		int position;
		public void set(int i) { position = i; }
		public void left() { position = position - 1 > -1 ? position - 1 : 0; }
		public void right() { position = position + 1 < text.length() + 1 ? position + 1 : text.length(); }
		public int get() { return position; }
	}
	
	private class Time
	{
		double wait;
		boolean waitdone;
		public double limit;
		public double time;
		
		public Time(double waittime, double limit) { wait = waittime; this.limit = limit; }
		
		public void start()
		{
			waitdone = false;
			time = 0;
		}
		
		public boolean hasFinished()
		{
			if (time < wait && !waitdone)
				++time;
			else
				waitdone = true;
			if (waitdone)
			{
				if (time < limit)
					++time;
				else
				{
					time = 0;
					return true;
				}
			}
			return false;
		}
	}
}